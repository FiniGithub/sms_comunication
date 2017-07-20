package com.dzd.cache.redis.manager;

import com.dzd.utils.ObjectUtils;
import com.dzd.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

/**
 * @Author WHL
 * @Date 2017-3-23.
 */
public class JedisClusterClient implements RedisClient {

    private   Logger logger = LoggerFactory.getLogger(JedisClusterClient.class);



    private  JedisCluster jedisCluster ;
    public   final String KEY_PREFIX = "";


    public  String get(String key) {
        String value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("get {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return value;
    }

    public Set<byte[]> hkeysObject(Object key) {
        return null;
    }

    public Set<String> hkeys(String key) {
        return null;
    }

    public String hget(String key, String mapKey) {
        return null;
    }

    public List<String> hmget(String key, String... mapKey) {
        return null;
    }

    public void hdel(String key, String mapKey) {

    }

    public void hdel(String key, String... mapKey) {

    }

    public void hdelObject(String key, Object mapKey) {

    }

    public Object hgetObject(Object key, Object mapKey) {
        return null;
    }

    public void hdel(String key, Object mapKey) {

    }

    public Long incr(String key) {
        return null;
    }


    public   Object getObject(String key) {
        Object value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                value = toObject(jedisCluster.get(getBytesKey(key)));
                logger.debug("getObject {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObject {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
//			try {
//				jedisCluster.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        return value;
    }

    public   String set(String key, String value, int cacheSeconds) {
        String result = null;

        try {

            result = jedisCluster.set(key, value);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("set {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("set {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
        }
        return result;
    }

    public String set(String key, String value) {
        return this.set(key,value,0);
    }

    public String set(String key, Object value) {
        return null;
    }

    public Long hset(String key, String mapKey, String mapValue) {
        return null;
    }

    public Long hsetObject(String key, Object mapKey, Object mapValue) {
        return null;
    }

    public String hsetMap(String key, Map<String, String> values) {
        return null;
    }

    public   String setObject(String key, Object value, int cacheSeconds) {
        String result = null;

        try {
            result = jedisCluster.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObject {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
        }
        return result;
    }

    public String setObject(String key, Object value) {
        return this.setObject(key,value,0);
    }

    public long llen(String key) {
        return 0;
    }

    public Object lpop(String key) {
        return null;
    }

    public String lpopString(String key) {
        return null;
    }

    public void rpush(String key, Object value) {

    }

    public void rpushString(String key, String value) {

    }

    public boolean hexists(String key, String mkey) {
        return false;
    }

    public long hlen(String key) {
        return 0;
    }

    public void hmset(String key, Map<byte[], byte[]> value) {

    }

    public Map<byte[], byte[]> hgetAll(String key) {
        return null;
    }

    public Long publish(String key, String value) {
        return null;
    }

    public Long publishObject(Object key, Object value) {
        return null;
    }

    public void psubscribe(JedisPubSub jedisPubSub, String channel) {

    }

    public void subscribe(JedisPubSub jedisPubSub, String channel) {

    }

    public List<String> getList(String key) {
        List<String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.lrange(key, 0, -1);
                logger.debug("getList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getList {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
        }
        return value;
    }


    public   List<Object> getObjectList(String key) {
        List<Object> value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                List<byte[]> list = jedisCluster.lrange(getBytesKey(key), 0, -1);
                value = new ArrayList<Object>();
                for (byte[] bs : list){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObjectList {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
        }
        return value;
    }

    public   long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.rpush(key, (String[])value.toArray());
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setList {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setList {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
        }
        return result;
    }

    public long setList(String key, List<String> value) {
        return setList(key,value,0);
    }


    public   long setObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(key);
            }
            List<byte[]> list = new ArrayList<byte[]>();
            for (Object o : value){
                list.add(toBytes(o));
            }
            result = jedisCluster.rpush(getBytesKey(key), (byte[][])list.toArray());
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObjectList {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObjectList {} = {}", key, value, e);
        } finally {
            //returnResource(jedis);
        }
        return result;
    }

    public long setObjectList(String key, List<Object> value) {
        return setObjectList(key,value,0);
    }


    public   long listAdd(String key, String... value) {
        long result = 0;
        try {
            result = jedisCluster.rpush(key, value);
            logger.debug("listAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("listAdd {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }

    public   long listObjectAdd(String key, Object... value) {
        long result = 0;
        try {
            List<byte[]> list = new ArrayList<byte[]>();
            for (Object o : value){
                list.add(toBytes(o));
            }
            result = jedisCluster.rpush(getBytesKey(key), (byte[][])list.toArray());
            logger.debug("listObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("listObjectAdd {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   Set<String> getSet(String key) {
        Set<String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.smembers(key);
                logger.debug("getSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getSet {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return value;
    }


    public   Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                value = new HashSet<Object>();
                Set<byte[]> set = jedisCluster.smembers(getBytesKey(key));
                for (byte[] bs : set){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObjectSet {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return value;
    }


    public   long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.sadd(key, (String[])value.toArray());
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setSet {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSet {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }

    public long setSet(String key, Set<String> value) {
        return setSet(key,value,0);
    }


    public   long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(key);
            }
            Set<byte[]> set = new HashSet<byte[]>();
            for (Object o : value){
                set.add(toBytes(o));
            }
            result = jedisCluster.sadd(getBytesKey(key), (byte[][])set.toArray());
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObjectSet {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObjectSet {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }

    public long setObjectSet(String key, Set<Object> value) {
        return setObjectSet(key,value,0);
    }


    public long setSetAdd(String key, String... value) {
        long result = 0;
        try {
            result = jedisCluster.sadd(key, value);
            logger.debug("setSetAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSetAdd {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public  long setSetObjectAdd(String key, Object... value) {
        long result = 0;
        try {
            Set<byte[]> set = new HashSet<byte[]>();
            for (Object o : value){
                set.add(toBytes(o));
            }
            result = jedisCluster.rpush(getBytesKey(key), (byte[][])set.toArray());
            logger.debug("setSetObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSetObjectAdd {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.hgetAll(key);
                logger.debug("getMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getMap {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return value;
    }


    public   Map<String, Object> getObjectMap(String key) {
        Map<String, Object> value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                value = new HashMap<String, Object>();
                Map<byte[], byte[]> map = jedisCluster.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()){
                    value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
                }
                logger.debug("getObjectMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getObjectMap {} = {}", key, value, e);
        } finally {
        }
        return value;
    }


    public   String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.hmset(key, value);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setMap {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setMap {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }

    public String setMap(String key, Map<String, String> value) {
        return setMap(key,value,0);
    }


    public   String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(key);
            }
            Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedisCluster.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObjectMap {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setObjectMap {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }

    public String setObjectMap(String key, Map<String, Object> value) {
        return setObject(key,value,0);
    }


    public   String mapPut(String key, Map<String, String> value) {
        String result = null;
        try {
            result = jedisCluster.hmset(key, value);
            logger.debug("mapPut {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("mapPut {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        try {
            Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedisCluster.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
            logger.debug("mapObjectPut {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("mapObjectPut {} = {}", key, value, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   long mapRemove(String key, String mapKey) {
        long result = 0;
        try {
            result = jedisCluster.hdel(key, mapKey);
            logger.debug("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapRemove {}  {}", key, mapKey, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        try {
            result = jedisCluster.hdel(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   boolean mapExists(String key, String mapKey) {
        boolean result = false;
        try {
            result = jedisCluster.hexists(key, mapKey);
            logger.debug("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapExists {}  {}", key, mapKey, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        try {
            result = jedisCluster.hexists(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectExists {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.warn("mapObjectExists {}  {}", key, mapKey, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   long del(String key) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)){
                result = jedisCluster.del(key);
                logger.debug("del {}", key);
            }else{
                logger.debug("del {} not exists", key);
            }
        } catch (Exception e) {
            logger.warn("del {}", key, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   long delObject(String key) {
        long result = 0;
        try {
            if (jedisCluster.exists(getBytesKey(key))){
                result = jedisCluster.del(getBytesKey(key));
                logger.debug("delObject {}", key);
            }else{
                logger.debug("delObject {} not exists", key);
            }
        } catch (Exception e) {
            logger.warn("delObject {}", key, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   boolean exists(String key) {
        boolean result = false;
        try {
            result = jedisCluster.exists(key);
            logger.debug("exists {}", key);
        } catch (Exception e) {
            logger.warn("exists {}", key, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    public   boolean existsObject(String key) {
        boolean result = false;
        try {
            result = jedisCluster.exists(getBytesKey(key));
            logger.debug("existsObject {}", key);
        } catch (Exception e) {
            logger.warn("existsObject {}", key, e);
        } finally {
//			returnResource(jedis);
        }
        return result;
    }


    /**
     * 获取byte[]类型Key
     * @return
     */
    public static  byte[] getBytesKey(Object object){
        if(object instanceof String){
            return StringUtils.getBytes((String)object);
        }else{
            return ObjectUtils.serialize(object);
        }
    }

    /**
     * Object转换byte[]类型
     * @return
     */
    public static  byte[] toBytes(Object object){
        return ObjectUtils.serialize(object);
    }

    /**
     * byte[]型转换Object
     * @return
     */
    public static Object toObject(byte[] bytes){
        return ObjectUtils.unserialize(bytes);
    }

	public void openResource()
	{
	}

	public void closeResource()
	{
	}

}
