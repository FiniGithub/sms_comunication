package com.dzd.cache.redis.manager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author WHL
 * @Date 2017-3-23.
 */
public interface RedisClient {
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */

    public String get(String key);
    //public String hget(String key, String mapKey);
    public Set<byte[]> hkeysObject(Object key );
    public Set<String> hkeys(String key );
    public Object hgetObject(Object key, Object mapKey);
    public String hget(String key, String mapKey);
    public List<String> hmget(String key, String...  mapKey);
    public void hdel(String key, String mapKey);
    public void hdel(String key, String... mapKey);
    public void hdelObject(String key, Object mapKey);
    public Long incr(String key);
    
    public void openResource();
    public void closeResource();
    
    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public Object getObject(String key);

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String set(String key, String value, int cacheSeconds);
    public String set(String key, String value);
    public String set(String key, Object value);
    public Long hset(String key, String mapKey, String mapValue);
    public Long hsetObject(String key, Object mapKey, Object mapValue);
    public String hsetMap(String key, Map<String,String> values);
    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setObject(String key, Object value, int cacheSeconds);
    public String setObject(String key, Object value);


    /**
     * 获取长度
     */
    public long llen(String key);
    public Object lpop(String key);
    public String lpopString(String key);
    public void rpush(String key, Object value);
    public void rpushString(String key, String value);
    public boolean hexists(String key, String mkey);
    public long hlen(String key );
    public void hmset(String key, Map<byte[],byte[]> value);
    public Map<byte[], byte[]> hgetAll(String key);

    public Long publish(String key, String value);
    public Long publishObject(Object key, Object value);

    public void psubscribe(JedisPubSub jedisPubSub, String channel);
    public void subscribe(JedisPubSub jedisPubSub, String channel);
    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public List<String> getList(String key);

    /**
     * 获取List缓存
     * @param key 键
     * @return 值
     */
    public List<Object> getObjectList(String key);

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setList(String key, List<String> value, int cacheSeconds);
    public long setList(String key, List<String> value );
    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setObjectList(String key, List<Object> value, int cacheSeconds);
    public long setObjectList(String key, List<Object> value );
    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public long listAdd(String key, String... value);

    /**
     * 向List缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public long listObjectAdd(String key, Object... value);

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public Set<String> getSet(String key);

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public Set<Object> getObjectSet(String key);

    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setSet(String key, Set<String> value, int cacheSeconds);
    public long setSet(String key, Set<String> value );
    /**
     * 设置Set缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setObjectSet(String key, Set<Object> value, int cacheSeconds);
    public long setObjectSet(String key, Set<Object> value );
    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public long setSetAdd(String key, String... value);

    /**
     * 向Set缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public long setSetObjectAdd(String key, Object... value);

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public Map<String, String> getMap(String key);

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    public Map<String, Object> getObjectMap(String key);

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setMap(String key, Map<String, String> value, int cacheSeconds);
    public String setMap(String key, Map<String, String> value );
    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setObjectMap(String key, Map<String, Object> value,
                               int cacheSeconds);
    public String setObjectMap(String key, Map<String, Object> value );
    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public String mapPut(String key, Map<String, String> value);

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    public String mapObjectPut(String key, Map<String, Object> value);

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public long mapRemove(String key, String mapKey);

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public long mapObjectRemove(String key, String mapKey);

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public boolean mapExists(String key, String mapKey);

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param mapKey 值
     * @return
     */
    public boolean mapObjectExists(String key, String mapKey);

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public long del(String key);

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    public long delObject(String key);

    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public boolean exists(String key);

    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    public boolean existsObject(String key);
}
