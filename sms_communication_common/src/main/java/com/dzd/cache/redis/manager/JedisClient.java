package com.dzd.cache.redis.manager;

import com.dzd.cache.redis.connection.RedisConnectionFactory;
import com.dzd.utils.ObjectUtils;
import com.dzd.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

/**
 * @Author WHL
 * @Date 2017-3-23.
 */
public class JedisClient implements RedisClient {

    private static Logger logger = LoggerFactory.getLogger(JedisClient.class);

    public JedisPool jedisPool = null;//SpringContextHolder.getBean(JedisPool.class);
    public RedisConnectionFactory redisConnectionFactory;
    public static final String KEY_PREFIX = "A";//Global.getConfig("redis.keyPrefix");
    
    private Jedis redis;
    
	/**
     * 获取缓存
     * @param key 键
     * @return 值
     */

    public  String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("get {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public Set<byte[]> hkeysObject(Object key ) {
        Set<byte[]> value = null;
        Jedis jedis = null;
        try
        {
            jedis = getResource();
            byte[] bK = getBytesKey(key);
            value = jedis.hkeys(bK);
            logger.debug("get {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("get {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }
    public Set<String> hkeys(String key ){
        Set<String> value = null;
        Jedis jedis = null;
        try
        {
            jedis = getResource();
            value = jedis.hkeys(key);
            //logger.debug("get {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("get {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public String hget(String key, String mapKey) {
        String value = null;
        if ( redis == null ) {
            Jedis jedis = null;
            try {
                    jedis = getResource();

                //if (jedis.hexists(key,mapKey)) {
                    value = jedis.hget(key,mapKey);
                    value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                    //logger.debug("get {} = {}", key, value);
                //}
            } catch (Exception e) {
                logger.warn("get {} = {}", key, value, e);
            } finally {
                returnResource(jedis);
            }
        }else{
            try {
                //if (jedis.hexists(key,mapKey)) {
                value = redis.hget(key,mapKey);
                //logger.debug("get {} = {}", key, value);
                //}
            } catch (Exception e) {
                logger.warn("get {} = {}", key, value, e);

            }
        }
        return value;
    }

    public List<String> hmget(String key, String... mapKey) {
        List<String> value = null;
        if ( redis == null ) {
            Jedis jedis = null;
            try {
                jedis = getResource();

                //if (jedis.hexists(key,mapKey)) {
                value = jedis.hmget(key,mapKey);
                //logger.debug("get {} = {}", key, value);
                //}
            } catch (Exception e) {
                logger.warn("get {} = {}", key, value, e);
            } finally {
                returnResource(jedis);
            }
        }else{
            try {
                //if (jedis.hexists(key,mapKey)) {
                value = redis.hmget(key,mapKey);
                //logger.debug("get {} = {}", key, value);
                //}
            } catch (Exception e) {
                logger.warn("get {} = {}", key, value, e);

            }
        }
        return value;
    }

    public Object hgetObject(Object key, Object mapKey) {
        Object value = null;
        Jedis jedis = null;
		try
		{
			jedis = getResource();
			byte[] bK = getBytesKey(key);
			byte[] bMk = getBytesKey(mapKey);
			byte[] val = jedis.hget(bK, bMk);
			value = toObject(val);
			logger.debug("get {} = {}", key, value);
		} catch (Exception e) {
            logger.warn("get {} = {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }
    
    public void hdel(String key, String mapKey) {
        if ( redis == null ) {
            Jedis jedis = null;
            try {
                jedis = getResource();
                jedis.hdel( key,mapKey);

            } catch (Exception e) {
                logger.warn("get {} = {}", key,   e);
            } finally {
                returnResource(jedis);
            }
        }else{
            try {
                redis.hdel( key,mapKey);
            } catch (Exception e) {
                logger.warn("get {} = {}", key,  e);
            }
        }
    }

    public void hdel(String key, String... mapKey) {
        if ( redis == null ) {
            Jedis jedis = null;
            try {
                jedis = getResource();
                jedis.hdel( key,mapKey);

            } catch (Exception e) {
                logger.warn("get {} = {}", key,   e);
            } finally {
                returnResource(jedis);
            }
        }else{
            try {
                redis.hdel( key,mapKey);
            } catch (Exception e) {
                logger.warn("get {} = {}", key,  e);
            }
        }
    }

    public void hdelObject(String key, Object mapKey) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            byte[] bK = getBytesKey(key);
            byte[] bMk = getBytesKey(mapKey);
            jedis.hdel( bK,bMk);

        } catch (Exception e) {
            logger.warn("get {} = {}", key,   e);
        } finally {
            returnResource(jedis);
        }
    }
    public Long incr(String key) {
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.incr(key);

        } catch (Exception e) {
            logger.warn("getObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public  Object getObject(String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)));
                logger.debug("getObject {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  String set(String key, String value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("set {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("set {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String set(String key, String value) {
        return set(key,value,0);
    }

    public String set(String key, Object value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(key.getBytes(), toBytes(value));

            logger.debug("set {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("set {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Long hset(String key, String mapKey, String mapValue) {

          return  hset2(key, mapKey, mapValue);

    }
    public Long hset2(String key,String mapKey, String mapValue ){
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hset(key, mapKey,mapValue);
            //logger.debug("set {} = {}", key, mapValue);
        } catch (Exception e) {
            logger.warn("set {} = {}", key, mapValue, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }
    public String hsetMap(String key, Map<String,String> values){
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result =  jedis.hmset(key,values);

        } catch (Exception e) {
            logger.warn("set {} = {}", key, values.size(), e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }
    public Long hsetObject(String key, Object mapKey, Object mapValue) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hset(key.getBytes(), getBytesKey(mapKey), toBytes(mapValue));
            logger.debug("set {} = {}", key, mapValue);
        } catch (Exception e) {
            logger.warn("set {} = {}", key, mapValue, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String setObject(String key, Object value) {
        return setObject(key,value,0);
    }

    public long llen(String key) {
        long result = 0;
        Jedis jedis = null;
        try {

            jedis = getResource();
            result = jedis.llen(getBytesKey(key) );

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("setObject {} = {}", key, result, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Object lpop(String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = toObject(jedis.lpop( getBytesKey(key)));
        } catch (Exception e) {
            logger.warn("lpop {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }
    public String lpopString(String key) {
        String value = null;
        Jedis jedis = null;
        if ( redis != null )
        {
            try
            {
                value = redis.lpop(key );
            } catch (Exception e)
            {
                logger.warn("rpush {} = {}", key, value, e);
            }
        } else
        {
            try
            {
                jedis = getResource();
                value = jedis.lpop(key);
            } catch (Exception e)
            {
                logger.warn("rpush {} = {}", key, value, e);
            } finally
            {
                returnResource(jedis);
            }
        }
        return value;
    }


	public void rpush(String key, Object value)
	{

		Jedis jedis = null;
		if ( redis != null )
		{
			singleRpush(key, value);
		} else
		{
			jedsiRpush(key, value, jedis);
		}

	}


    public void rpushString(String key, String value)
    {

        Jedis jedis = null;
        if ( redis != null )
        {
            try
            {
                redis.rpush(key, value);
            } catch (Exception e)
            {
                logger.warn("rpush {} = {}", key, value, e);
            }
        } else
        {
            try
            {

                jedis = getResource();
                jedis.rpush(key, value);

            } catch (Exception e)
            {
                logger.warn("rpush {} = {}", key, value, e);
            } finally
            {
                returnResource(jedis);
            }
        }

    }

	private void jedsiRpush(String key, Object value, Jedis jedis)
	{
		try
		{

			jedis = getResource();
			jedis.rpush(getBytesKey(key), toBytes(value));

		} catch (Exception e)
		{
			logger.warn("rpush {} = {}", key, value, e);
		} finally
		{
			returnResource(jedis);
		}
	}


	private void singleRpush(String key, Object value)
	{
		try
		{
			redis.rpush(getBytesKey(key), toBytes(value));
		} catch (Exception e)
		{
			logger.warn("rpush {} = {}", key, value, e);
		}
	}

    public boolean hexists(String key, String mkey) {
        boolean re = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            re=jedis.hexists( getBytesKey(key), getBytesKey(mkey) );
        } catch (Exception e) {
            logger.warn("hexists {} = {}", key, re, e);
        } finally {
            returnResource(jedis);
        }
        return re;
    }

    public long hlen(String key ) {
        long re = 0l;
        Jedis jedis = null;
        try {
            jedis = getResource();
            re=jedis.hlen( getBytesKey(key)  );
        } catch (Exception e) {
            logger.warn("hlen {} = {}", key, re, e);
        } finally {
            returnResource(jedis);
        }
        return re;
    }

    public void hmset(String key, Map<byte[],byte[]> value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.hmset( key.getBytes(), value );
        } catch (Exception e) {
            logger.warn("hmset {} = {}", key,   e);
        } finally {
            returnResource(jedis);
        }
    }

    public Map<byte[], byte[]> hgetAll(String key) {
        Map<byte[],byte[]> re = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            re = jedis.hgetAll( key.getBytes() );
        } catch (Exception e) {
            logger.warn("hgetAll {} = {}", key,   e);
        } finally {
            returnResource(jedis);
        }
        return re;
    }

    public Long publish(String key, String value) {
        Long re = 0l;
        Jedis jedis = null;
        try {
            jedis = getResource();
            re = jedis.publish(key,value);
        } catch (Exception e) {
            logger.warn("publish {} = {}", key,   e);
        } finally {
            returnResource(jedis);
        }
        return re;
    }

    public Long publishObject(Object key, Object value){
        Long re = 0l;
        Jedis jedis = null;
        try {
            jedis = getResource();
            re = jedis.publish(toBytes(key),toBytes(value));
        } catch (Exception e) {
            logger.warn("publishObject {} = {}", key,   e);
        } finally {
            returnResource(jedis);
        }
        return re;
    }


    public void psubscribe(JedisPubSub jedisPubSub, String channel){
        Long re = 0l;
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.psubscribe(jedisPubSub,channel);
        } catch (Exception e) {
            logger.warn("psubscribe {} = {}", re,   e);
        } finally {
            returnResource(jedis);
        }
    }
    public void subscribe(JedisPubSub jedisPubSub, String channel){
        Long re = 0l;
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.subscribe(jedisPubSub,channel);
        } catch (Exception e) {
            logger.warn("psubscribe {} = {}", re,   e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public  List<String> getList(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
                logger.debug("getList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public  List<Object> getObjectList(String key) {
        List<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
                value = new ArrayList<Object>();
                for (byte[] bs : list){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObjectList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }


    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.rpush(key, (String[])value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setList {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setList(String key, List<String> value) {
        return setList(key,value,0);
    }

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  long setObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            List<byte[]> list = new ArrayList<byte[]>();
            for (Object o : value){
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][])list.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectList {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObjectList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setObjectList(String key, List<Object> value) {
        return setObjectList(key,value,0);
    }

    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public  long listAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.rpush(key, value);
            logger.debug("listAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("listAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public  long listObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            List<byte[]> list = new ArrayList<byte[]>();
            for (Object o : value){
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][])list.toArray());
            logger.debug("listObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("listObjectAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public  Set<String> getSet(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);
                logger.debug("getSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public  Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashSet<Object>();
                Set<byte[]> set = jedis.smembers(getBytesKey(key));
                for (byte[] bs : set){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObjectSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }



    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.sadd(key, (String[])value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setSet {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setSet(String key, Set<String> value) {
        return setSet(key,value,0);
    }

    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            Set<byte[]> set = new HashSet<byte[]>();
            for (Object o : value){
                set.add(toBytes(o));
            }
            result = jedis.sadd(getBytesKey(key), (byte[][])set.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectSet {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObjectSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setObjectSet(String key, Set<Object> value) {
        return setObjectSet(key,value,0);
    }

    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public  long setSetAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.sadd(key, value);
            logger.debug("setSetAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSetAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public  long setSetObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            Set<byte[]> set = new HashSet<byte[]>();
            for (Object o : value){
                set.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][])set.toArray());
            logger.debug("setSetObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSetObjectAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public  Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);
                logger.debug("getMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public  Map<String, Object> getObjectMap(String key) {
        Map<String, Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashMap<String, Object>();
                Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()){
                    value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
                }
                logger.debug("getObjectMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObjectMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setMap {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String setMap(String key, Map<String, String> value) {
        return setMap(key,value,0);
    }

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public  String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectMap {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObjectMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String setObjectMap(String key, Map<String, Object> value) {
        return setObjectMap(key,value,0);
    }

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public  String mapPut(String key, Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hmset(key, value);
            logger.debug("mapPut {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("mapPut {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public  String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
            logger.debug("mapObjectPut {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("mapObjectPut {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public  long mapRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(key, mapKey);
            logger.debug("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapRemove {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public  long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public  boolean mapExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(key, mapKey);
            logger.debug("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.error(  e.getMessage() );
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public  boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectExists {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapObjectExists {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public  long del(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)){
                result = jedis.del(key);
                logger.debug("del {}", key);
            }else{
                logger.debug("del {} not exists", key);
            }
        } catch (Exception e) {
            logger.warn("del {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public  long delObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))){
                result = jedis.del(getBytesKey(key));
                logger.debug("delObject {}", key);
            }else{
                logger.debug("delObject {} not exists", key);
            }
        } catch (Exception e) {
            logger.warn("delObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public  boolean exists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(key);
            logger.debug("exists {}", key);
        } catch (Exception e) {
            logger.warn("exists {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public  boolean existsObject(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(getBytesKey(key));
            logger.debug("existsObject {}", key);
        } catch (Exception e) {
            logger.warn("existsObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取资源
     * @return
     */
    public  Jedis getResource() throws JedisException {

        return redisConnectionFactory.getJedisConnection();
//
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
////			logger.debug("getResource.", jedis);
//        } catch (JedisException e) {
//            logger.warn("getResource.", e);
//            returnBrokenResource(jedis);
//            throw e;
//        }
//        return jedis;
    }

    /**
     * 归还资源
     * @param jedis
     */
    public  void returnBrokenResource(Jedis jedis) {
        if (jedis != null) {
            //jedisPool.returnBrokenResource(jedis);
            //redisConnectionFactory.destroy();
        }
    }

    /**
     * 释放资源
     * @param jedis
     * @param jedis
     */
	public void returnResource(Jedis jedis)
	{
		if ( jedis != null )
		{
			jedis.close();
		}
	}

    /**
     * 获取byte[]类型Key
     * @param object
     * @return
     */
    public static byte[] getBytesKey(Object object){
        if(object instanceof String){
            return StringUtils.getBytes((String)object);
        }else{
            return ObjectUtils.serialize(object);
        }
    }

    /**
     * Object转换byte[]类型
     * @param object
     * @return
     */
    public static byte[] toBytes(Object object){
        return ObjectUtils.serialize(object);
    }

    /**
     * byte[]型转换Object
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes){
        return ObjectUtils.unserialize(bytes);
    }


	public void openResource()
	{
		setRedis(getResource());
	}


	public void closeResource()
	{
        try {
            if (redis != null) {
                redis.close();
                redis = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

	}
	
	public Jedis getRedis()
	{
		return redis;
	}


	public void setRedis(Jedis redis)
	{
        if( this.redis !=null ){
            this.redis.close();
        }
		this.redis = redis;
	}
}
