package com.ais.udep.server.xml.service;

import com.ais.udep.server.config.DbConfig;
import com.ais.udep.server.exception.UdepDuplicateTableInfoException;
import com.ais.udep.server.util.JAXBXmlUtils;
import com.ais.udep.server.xml.bean.UdepRoot;
import com.ais.udep.server.xml.bean.UdepTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 */
@Service("udepTableInfoService")
public class UdepTableInfoService {

    private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private Map<String, UdepTableInfo> tableInfoMap;

    @Value("${config.udep.xmlconfig.loadorder}")
    private String udepLoadOrder;

    @Autowired(required = false)
    private DbConfig dbConfig;

    /**
     * 加载配置内容
     */
    @PostConstruct
    private void load() {
        try {
            if(dbConfig==null){
                return;
            }
            String resourcePath="/udep/"+dbConfig.getXmlReadPath()+"/**/*.xml";
            // 模块权重，后面的覆盖前面的
            String[] loadOrders = udepLoadOrder.split(",");
            HashMap<String, Integer> orderMap = new HashMap();
            AtomicInteger orderNum = new AtomicInteger(loadOrders.length);
            // 获取jar中的xml文件定义的流程，引入的所有jar中不允许有重复的key
            Resource[] jarResources = Arrays.stream(resolver.getResources("classpath*:"+resourcePath)).filter(resource -> {
                try {
                    if(!resource.getURL().toString().contains("jar:")){
                        return false;
                    }
                    // 给所有文件按照模块设置权重，值越低，权重越低
                    for(int i=0;i<loadOrders.length;i++){
                        // 取lib目录之后的内容
                        String moduleName = new File(resource.getURL().toString().split(".jar")[0]).getName();
                        if(moduleName.contains(loadOrders[i])){
                            orderMap.put(resource.getURL().toString(),i);
                            return true;
                        }
                    }
                    orderMap.put(resource.getURL().toString(),orderNum.incrementAndGet());
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }).sorted(new Comparator<Resource>() {
                @Override
                public int compare(Resource o1, Resource o2) {
                    try {
                        // 指定排序，根据权重，按照升序排
                        return orderMap.get(o1.getURL().toString()).compareTo(orderMap.get(o2.getURL().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return 1;
                }
            }).toArray(Resource[]::new);
            // jar包中所有的tableInfo配置，jar包中下不允许有重复的key
            Map<String, UdepTableInfo> jarMap = addJarTableInfos(jarResources);
            // 获取本地的xml文件定义的tableInfo配置，本地路径下不允许有重复的key
            Resource[] localResources = resolver.getResources("classpath:"+resourcePath);
            Map<String, UdepTableInfo> localMap = addLocalTableInfos(localResources);
            // 合并相同的key，本地资源根据相同的key覆盖jar中资源
            jarMap.putAll(localMap);
            this.tableInfoMap = jarMap;
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载配置
     */
    public void refresh() {
        this.tableInfoMap.clear();
        load();
    }

    /**
     * path参数例如：udep/mysql/test-resource.xml
     *
     * @param path
     * @return
     */
    public UdepRoot loadXmlConfig(String path) {
        //在classpath下读取xml的文件流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        try {
            return JAXBXmlUtils.fromXML(inputStream, UdepRoot.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 本地中的配置，不允许出现相同的tableInfo，出现则抛出异常提醒
     *
     * @param localResources
     * @return Map<String, UdepTableInfo>
     * @throws JAXBException
     * @throws IOException
     */
    private Map<String, UdepTableInfo> addLocalTableInfos(Resource[] localResources) throws JAXBException, IOException {
        Map<String, UdepTableInfo> map = new HashMap<>();
        for (Resource resource : localResources) {
            UdepRoot root = JAXBXmlUtils.fromXML(resource.getInputStream(), UdepRoot.class);
            root.getTableInfoList().forEach(tableInfo -> {
                // 如果存在重复的，抛出异常提示
                if (map.containsKey(tableInfo.getKey())) {
                    try {
                        throw new UdepDuplicateTableInfoException(String.format("Duplicate udep tableInfo key '%s' defined in URL [%s]", tableInfo.getKey(), resource.getURL().toString()));
                    } catch (UdepDuplicateTableInfoException | IOException e) {
                        e.printStackTrace();
                    }
                }
                map.put(tableInfo.getKey(), tableInfo);
            });
        }
        return map;
    }


    /**
     * jar包中的配置允许覆盖，按照指定的模版优先级顺序，覆盖相同的tableInfo
     *
     * @param jarResources
     * @return Map<String, UdepTableInfo>
     * @throws JAXBException
     * @throws IOException
     */
    private Map<String, UdepTableInfo> addJarTableInfos(Resource[] jarResources) throws JAXBException, IOException {
        Map<String, UdepTableInfo> map = new HashMap<>();
        for (Resource resource : jarResources) {
            UdepRoot root = JAXBXmlUtils.fromXML(resource.getInputStream(), UdepRoot.class);
            root.getTableInfoList().forEach(tableInfo -> {
                map.put(tableInfo.getKey(), tableInfo);
            });
        }
        return map;
    }

}
