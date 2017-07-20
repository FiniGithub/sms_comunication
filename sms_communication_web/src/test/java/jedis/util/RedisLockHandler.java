package jedis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * redis 分布式锁实现
 * 
 */
public class RedisLockHandler implements IRedisLockHandler {

	// 单个锁有效期
	private static final int DEFAULT_SINGLE_EXPIRE_TIME = 30;
	// 批量锁有效期
	private static final int DEFAULT_BATCH_EXPIRE_TIME = 60;

	private final JedisPool jedisPool;

	/**
	 * 构造
	 */
	public RedisLockHandler(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 获取锁 如果锁可用 立即返回true， 否则返回false，不等待
	 * 
	 * @return
	 */
	public boolean tryLock(String key) {
		return tryLock(key, 0L, null);
	}

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false (备注:获取一个指定标识的锁,在timeout时间内获取成功
	 * 返回true,失败 返回false)
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean tryLock(String key, long timeout, TimeUnit unit) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			// 系统计时器的当前值，以毫微秒为单位。
			long nano = System.nanoTime();
			do {
				System.out.println("try lock key: " + key);
				// 将 key 的值设为 value 1成功 0失败
				Long i = jedis.setnx(key, key);
				if (i == 1) {
					// 设置过期时间
					jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
					System.out.println("成功获取锁, key: " + key + " , 单个锁有效期" + DEFAULT_SINGLE_EXPIRE_TIME + " 秒.");
					// 成功获取锁，返回true
					return Boolean.TRUE;
				} else { // 存在锁,循环等待锁

					String desc = jedis.get(key);
					System.out.println("存在锁,key: " + key);
					System.out.println("key: " + key + ",被占用：" + desc);

				}
				if (timeout <= 0) {
					// 没有设置超时时间，直接退出等待
					break;
				}
				Thread.sleep(300);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));
			return Boolean.FALSE;
		} catch (JedisConnectionException je) {
			je.printStackTrace();
			returnBrokenResource(jedis);// 释放资源
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);// 释放资源
		}
		return Boolean.FALSE;
	}

	/**
	 * 如果锁空闲立即返回 获取失败 一直等待
	 */
	public void lock(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			do {
				System.out.println("lock key: " + key);
				Long i = jedis.setnx(key, key);
				if (i == 1) {
					jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);
					System.out.println("获取锁, key: " + key + " , 单个锁有效期" + DEFAULT_SINGLE_EXPIRE_TIME + " 秒.");
					return;
				} else {

					String desc = jedis.get(key);
					System.out.println("key: " + key + ",被占用：" + desc);

				}
				Thread.sleep(300);
			} while (true);
		} catch (JedisConnectionException je) {
			je.printStackTrace();
			returnBrokenResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 释放锁
	 */
	public void unLock(String key) {
		List<String> list = new ArrayList<String>();
		list.add(key);
		unLock(list);
	}

	/**
	 * 批量获取锁 如果全部获取 立即返回true, 部分获取失败 返回false
	 * 
	 * @return
	 */
	public boolean tryLock(List<String> keyList) {
		return tryLock(keyList, 0L, null);
	}

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean tryLock(List<String> keyList, long timeout, TimeUnit unit) {
		Jedis jedis = null;
		try {
			// 需要的锁
			List<String> needLocking = new CopyOnWriteArrayList<String>();
			// 得到的锁
			List<String> locked = new CopyOnWriteArrayList<String>();
			jedis = getResource();
			long nano = System.nanoTime();
			do {
				// 构建pipeline，批量提交
				Pipeline pipeline = jedis.pipelined();
				for (String key : keyList) {
					needLocking.add(key);
					pipeline.setnx(key, key);
				}
				System.out.println("尝试获取锁 key: " + needLocking);
				// 提交redis执行计数,批量处理完成返回
				List<Object> results = pipeline.syncAndReturnAll();
				for (int i = 0; i < results.size(); ++i) {
					Long result = (Long) results.get(i);
					String key = needLocking.get(i);
					if (result == 1) { // setnx成功，获得锁
						jedis.expire(key, DEFAULT_BATCH_EXPIRE_TIME);
						locked.add(key);
					}
				}
				needLocking.removeAll(locked); // 已锁定资源去除

				if (needLocking.size() == 0) { // 成功获取全部的锁
					return true;
				} else {
					// 部分资源未能锁住
					System.out.println("key: " + needLocking + " ,部分资源未能锁住");
				}

				if (timeout == 0) {
					break;
				}
				Thread.sleep(500);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));

			// 得不到锁，释放锁定的部分对象，并返回失败
			if (locked.size() > 0) {
				jedis.del(locked.toArray(new String[0]));
			}
			return false;
		} catch (JedisConnectionException je) {
			je.printStackTrace();
			returnBrokenResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return true;
	}

	/**
	 * 批量释放锁
	 */
	public void unLock(List<String> keyList) {
		List<String> keys = new CopyOnWriteArrayList<String>();
		for (String key : keyList) {
			keys.add(key);
		}
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.del(keys.toArray(new String[0]));
			System.out.println("释放锁: " + keys);
		} catch (JedisConnectionException je) {
			je.printStackTrace();
			returnBrokenResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 获取redis客户端
	 * 
	 * @return
	 */
	private Jedis getResource() {
		return jedisPool.getResource();
	}

	/**
	 * 销毁连接
	 * 
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	private void returnBrokenResource(Jedis jedis) {
		if (jedis == null) {
			return;
		}
		try {
			// 中断链接
			jedisPool.returnBrokenResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新初始化对象
	 * 
	 * @param jedis
	 */
	@SuppressWarnings("deprecation")
	private void returnResource(Jedis jedis) {
		if (jedis == null) {
			return;
		}
		try {
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
