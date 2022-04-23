package com.ais.udep.core.netty.serialize.serializer;

import com.ais.udep.core.bean.ClientInfo;
import com.ais.udep.core.netty.serialize.UdepSerializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.nio.charset.StandardCharsets;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/18
 * @Description:
 */
public class JsonSerializer implements UdepSerializer {

    @Override
    public byte[] serialize(Object object) {
        String body= JSON.toJSONString(object);
        return body.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        String body = new String(bytes,StandardCharsets.UTF_8);
        return  JSON.parseObject(body,clazz);
    }
}
