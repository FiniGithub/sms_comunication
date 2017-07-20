package com.dzd.sms.task.base;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.utils.ThreadUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/5
 * Time: 8:58
 */

public class BaseTask extends  Thread{

    private  static Logger logger = Logger.getLogger(BaseTask.class );


    public int threadNumber = 1;   //线程数量
    public int singlePackageNumber = 100;  //单个线程数量
    public boolean isMutiThread = false;
    public boolean excep = false; //系统是否异常
    public long sigalTime = 0l; //发送信号的时间
    public Date processDate = new Date();  //上次处理的时间
    public RedisClient redisClient = RedisManager.I.getRedisClient() ;

    public boolean canRun = true;
    int statNum = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    public ThreadPoolTaskExecutor taskExecutor = null;
    public ThreadPoolTaskExecutor getTaskExecutor() {
        if( taskExecutor == null ) {
            taskExecutor = ThreadUtil.getPoolTskExecutor(threadNumber);
        }
        return taskExecutor;
    }

    @Override
    public void run(){
        startExecutor();
    }
    /**
     * 开始执行任务
     */
    public void startExecutor() {

        while ( canRun ) {
            final ReentrantLock takeLock = this.lock;
            //System.out.println("in startExecutor");
            takeLock.lock();
            try {
                statNum++;
                //判断进入队列期
                while ( !isReady() ) {
                    //System.out.println("in startExecutor await");
                    notEmpty.await();
                }

                //记录信号时间
                sigalTime = new Date().getTime();
                System.out.println("in startExecutor executor-statNum="+statNum);
                //执行
                executor();

            } catch (InterruptedException e) {

                e.printStackTrace();


                //异常休息一会
                try{
                    Thread.sleep(100);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();

            } finally {
                takeLock.unlock();
            }
            System.out.println("executor end");
        }
    }

    /**
     * 通知有新队列需要处理
     */
    public void signalNotEmpty() {
        final ReentrantLock takeLock = this.lock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }



    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return true;
    }

    /**
     * 判断队列是否有任务在执行
     * @return
     */
    public boolean isReady() {

        if ( !this.existTask()) {
            return false;
        }
        ThreadPoolTaskExecutor threadPoolTaskExecutor =  getTaskExecutor();
        if (threadPoolTaskExecutor.getActiveCount() >= threadPoolTaskExecutor.getMaxPoolSize()) {
            //System.out.println("BaseTask(taskExecutor): threadPoolTaskExecutor.getActiveCount()=" + threadPoolTaskExecutor.getActiveCount() + " MaxPoolSize=" + threadPoolTaskExecutor.getMaxPoolSize());
            return false;
        }
        return true;
    }

    /**
     * 执行任务
     */
    public void executor() {
        try {
            ThreadPoolTaskExecutor t = this.getTaskExecutor();
            final BaseTask _self = this;
            for (int i = 0; i < threadNumber; i++) {
                if (t.getActiveCount() < t.getMaxPoolSize()) {
                    taskExecutor.execute(new Runnable() {
                        public void run() {
                            _self.singleExecutor();
                            _self.signalNotEmpty();
                        }
                    });
                } else {
                    //System.out.println("BaseTask(taskExecutor).executor: threadPoolTaskExecutor.getActiveCount()=" + t.getActiveCount() + " MaxPoolSize=" + t.getMaxPoolSize());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 执行任务实现
     */
    public void singleExecutor() {


    }


    /**
     * 定义线程处理时的相关属性
     * 注意: 对外使用， 不是给当前类使用
     *
     * @return
     */

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }


    /**
     * 用毫秒来判断
     * @param d
     * @return
     */
    public boolean canHandle(Date d, Long sleepTime){
        Long difSecond =  d.getTime()-this.processDate.getTime();
        if( difSecond.intValue() >= sleepTime ){
            this.processDate = d;
            return true;
        }
        return false;
    }
}
