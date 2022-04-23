package com.ais.udep.server.netty.channel;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/21
 * @Description:
 */
public class ChannelManage {

    // 客户端与channel进行绑定
    private final ConcurrentHashMap<String, Channel> channelMap;

    private static ChannelManage instance = null;

    public static ChannelManage getInstance() {
        if (instance == null) {
            synchronized (ChannelManage.class) {
                if (instance == null) {
                    instance = new ChannelManage();
                }
            }
        }
        return instance;
    }

    public ChannelManage() {
        this.channelMap= new ConcurrentHashMap<>();
    }

    /**
     * 判断一个通道是否已经初始化
     * @param channel
     * @return
     */
    public boolean hasInit(Channel channel) {
        AttributeKey<String> key = AttributeKey.valueOf("clientId");
        return (channel.hasAttr(key) || channel.attr(key).get() != null);//netty移除了这个map的remove方法,这里的判断谨慎一点
    }

    /**
     * 客户端与clientId进行绑定
     * @param channel
     * @param json
     */
    public void init(Channel channel, JSONObject json) {
        String clientId=json.getString("id");
        this.channelMap.put(clientId, channel);
        AttributeKey<String> key = AttributeKey.valueOf("clientId");
        channel.attr(key).set(clientId);
    }

    /**
     * 根据客户端id获取该客户端的通道
     *
     * @param clientId
     * @return
     */
    public Channel getChannelByClientId(String clientId) {
        return this.channelMap.get(clientId);
    }

    /**
     * 判断一个客户端是否在线
     *
     * @param clientId
     * @return
     */
    public Boolean isOnline(String clientId) {
        return this.channelMap.containsKey(clientId) && this.channelMap.get(clientId) != null;
    }

}
