package com.dzd.cache.redis.manager;

import com.dzd.config.data.RedisNode;
import com.dzd.utils.PropertiesUtils;
import redis.clients.jedis.*;
import com.dzd.cache.redis.connection.*;

import java.util.*;

/**
 * @Author WHL
 * @Date 2017-3-23.
 */
public final  class RedisManager {

    public static final RedisManager I = new RedisManager();

    private final RedisConnectionFactory factory = new RedisConnectionFactory();
    JedisClient jedisClient;
    public RedisManager(){
        init();
    }
    public void init() {

        /**
         *
         maxTotal:8,
         maxIdle:4,
         minIdle:1,
         lifo:true,
         fairness:false,
         maxWaitMillis:5000,
         minEvictableIdleTimeMillis:300000,
         softMinEvictableIdleTimeMillis:1800000,
         numTestsPerEvictionRun:3,
         testOnCreate:false,
         testOnBorrow:false,
         testOnReturn:false,
         testWhileIdle:false,
         timeBetweenEvictionRunsMillis:60000,
         blockWhenExhausted:true,
         jmxEnabled:false,
         jmxNamePrefix:pool,
         jmxNameBase:pool

         */
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(PropertiesUtils.getproperties("redis.pool.maxActive","10")));
        config.setMaxIdle(Integer.valueOf(PropertiesUtils.getproperties("redis.pool.maxIdle","10")));
        config.setMaxWaitMillis(Integer.valueOf(PropertiesUtils.getproperties("redis.pool.maxWait","10")));
        config.setTestOnBorrow(Boolean.valueOf(PropertiesUtils.getproperties("redis.pool.testOnBorrow","10")));
        config.setTestOnReturn(Boolean.valueOf(PropertiesUtils.getproperties("redis.pool.testOnReturn","10")));


        List<RedisNode> nodes = new ArrayList<RedisNode>();

        nodes.add(new RedisNode( PropertiesUtils.getproperties("redis.ip","192.168.1.53"), Integer.valueOf(PropertiesUtils.getproperties("redis.port","6379")) ));

        factory.setPassword("");
        factory.setPoolConfig( config );
        factory.setRedisServers( nodes );
        factory.setCluster(false);
        factory.init();

        jedisClient = new JedisClient();
        jedisClient.redisConnectionFactory = factory;
        jedisClient.get("ok");
    }

    public RedisClient getRedisClient(){
        return jedisClient;
    }
    public RedisClient newRedisClient(){
        JedisClient jedisClient1 = new JedisClient();
        jedisClient1.redisConnectionFactory = factory;
        return jedisClient1;
    }
    public RedisConnectionFactory getFactory(){ return factory;}
}
