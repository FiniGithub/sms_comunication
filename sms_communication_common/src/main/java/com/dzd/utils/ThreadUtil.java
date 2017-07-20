package com.dzd.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/9
 * Time: 13:01
 */
public class ThreadUtil {
    public static ThreadPoolTaskExecutor getPoolTskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(5);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(1000);
        //线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    public static ThreadPoolTaskExecutor getPoolTskExecutor(int n){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(5>n?n:5);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(n);
        //线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

}
