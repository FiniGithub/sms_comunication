package jedis;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
//
//import jedis.util.IRedisLockHandler;
//import jedis.util.RedisLockHandler;
//import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
//import net.sourceforge.groboutils.junit.v1.TestRunnable;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis 分布式锁并发测试
 * 
 */
public class JedisTest {
//	private JedisPool pool = null;
//
////	@Test
//	public void test() {
//
//		// 1.创建jedis池配置实例
//		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxTotal(1024);
//		config.setMaxIdle(200);
//		config.setMaxWaitMillis(1000);
//		config.setTestOnBorrow(true);
//		config.setTestOnReturn(true);
//		pool = new JedisPool(config, "192.168.1.52", 6379);
//
//		// 2. 并发线程
//		int NUM_THREAD = 100; // 并发数量
//		long start = System.currentTimeMillis();// 开始时间
//		TestRunnable[] test = new TestRunnable[NUM_THREAD];
//		for (int i = 0; i < NUM_THREAD; i++) {
//			test[i] = new ThreadA();
//		}
//
//		// 3.生成运行测试线程运行器
//		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(test);
//		try {
//			mttr.runTestRunnables();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//
//		long used = System.currentTimeMillis() - start;// 结束时间
//		System.out.println("并发数量:" + NUM_THREAD + ",花费时间" + used);
//
//	}
//
//	/**
//	 * 线程A
//	 *
//	 * @author Administrator
//	 *
//	 */
//	private class ThreadA extends TestRunnable {
//
//		@Override
//		public void runTest() throws Throwable {
//			myCommMethod1();
//		}
//	}
//
//	private void myCommMethod1() throws Exception {
//		String key = "test";
//		System.out.println("-------------------- 》》》线程id:" + Thread.currentThread().getId() + " 开始线程 myCommMethod1");
//
//		// 获取分布式锁标识
//		IRedisLockHandler lock = new RedisLockHandler(pool);
//		if (lock.tryLock(key, 20, TimeUnit.SECONDS)) {
//			System.out.println("获取到锁");
//		} else {
//			System.out.println("没有获取到锁");
//		}
//		// 释放分布式锁
//		lock.unLock(key);
//
//		System.out.println("-------------------- 》》》线程id:" + Thread.currentThread().getId() + " 结束线程 myCommMethod1");
//
//	}

}
