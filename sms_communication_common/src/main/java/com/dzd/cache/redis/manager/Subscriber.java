package com.dzd.cache.redis.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author WHL
 * @Date 2017-3-29.
 */
public class Subscriber extends JedisPubSub {
    private static Logger logger = LoggerFactory.getLogger(Subscriber.class);
    public void onMessage(String channel, String message) {
        logger.info("Message received. Channel: {}, Msg: {}", channel, message);
    }
}
