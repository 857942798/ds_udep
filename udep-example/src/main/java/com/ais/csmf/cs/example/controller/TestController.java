package com.ais.csmf.cs.example.controller;

import com.ais.udep.client.netty.send.UdepClientSender;
import com.ais.udep.core.annotation.Retry;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.result.AsyncResult;
import com.ais.udep.server.config.UdepJdbcTemplatePool;
import com.ais.udep.server.netty.send.UdepServerSender;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 */
@RestController
public class TestController {
    @Autowired(required = false)
    private UdepJdbcTemplatePool udepJdbcTemplatePool;
    @Autowired
    private UdepServerSender udepServerSender;
    @Autowired
    private UdepClientSender clientSender;


    @RequestMapping("getPoo")
    public Object getPool(){
        JdbcTemplate jdbcTemplate=udepJdbcTemplatePool.getUdepJdPool().get("default");
        List<Map<String,String>> querylist = jdbcTemplate.query("select * from a_student", new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",resultSet.getString("id"));
                map.put("name",resultSet.getString("name"));
                return map;
            }
        });
        return querylist;
    }

    @RequestMapping("ssend/{msg}")
    public Object ssend(@PathVariable("msg")  String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","this is from server");
        udepServerSender.sendToClient(1L,new UdepRequest(jsonObject));
        return "success";
    }

    @GetMapping("csend/{msg}")
    public Object csend(@PathVariable("msg")  String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","this is from client");
        ExecutorService excutor = Executors.newFixedThreadPool(100);

        for(int i=0;i<100;i++){
            int finalI = i;
            excutor.submit(new Runnable() {
                @Override
                public void run() {
                    AsyncResult asyncResult=clientSender.doSendToServer(new UdepRequest(jsonObject));
                    System.out.println("i="+ finalI +"--------------------->"+asyncResult.getData());
                }
            });
        }
        return jsonObject;
    }


    @RequestMapping("retry")
    public Object retry(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","this is from server");
        try {
            send();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Retry(retryIfException = true, waitStrategySleepTime = 1200)
    public void send() throws RemoteException {
        try {
            int x=0;
            int a=1/x;
        } catch (Exception e) {
            throw new RemoteException("12321");
        }
    }
}
