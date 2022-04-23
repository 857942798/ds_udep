package com.ais.udep.server.util;

import com.ais.udep.server.xml.bean.UdepRoot;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 */
public class JAXBXmlUtils {

    private static final String JAXB_SCHEMA_LOCATION = "http://www.udep.com/udep /udep/udep-config.xsd";

    public static String toXML(Object obj) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, JAXB_SCHEMA_LOCATION);// 是否省略xm头声明信息

        StringWriter out = new StringWriter();
        OutputFormat format = new OutputFormat();
        format.setIndent(true);
        format.setNewlines(true);
        format.setNewLineAfterDeclaration(false);
        XMLWriter writer = new XMLWriter(out, format);
        marshaller.marshal(obj, writer);
        return out.toString();
    }

    public static <T> T fromXML(InputStream in, Class<T> valueType) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(valueType);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(in);
    }

    public static void main(String[] args) {
        try {
            InputStream inputStream = JAXBXmlUtils.class.getClassLoader().getResourceAsStream("udep/mysql/udep-mysql.xml");//在classpath下读取xml的文件流
            UdepRoot root = fromXML(inputStream, UdepRoot.class);
            System.out.println(toXML(root));
            String dir = JAXBXmlUtils.class.getResource("/").getFile().split("target")[0] + "src/main/resources/udep2";
            File file = new File(dir);
            if (!file.exists()) {
//                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
