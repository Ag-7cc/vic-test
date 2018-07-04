package com.vic.test.concurrent;

import junit.framework.TestCase;
import lombok.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by vic
 * Create time : 2018/7/3 11:10
 */
public class ReentrantLockTest extends TestCase {

    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    public void test1() {

        int threadCount = 10;

        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        IncrementDemo incrementDemo = new IncrementDemo();
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10000; j++) {
                    incrementDemo.inc();
                }
                countDownLatch.countDown();
            });
        }
        executorService.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(incrementDemo.getNum());
    }

    /**
     * 读锁可以有很多个锁同时上锁，只要当前没有写锁；
     * 写锁是排他的，上了写锁，其他线程既不能上读锁，也不能上写锁；同样，需要上写锁的前提是既没有读锁，也没有写锁；
     * 两个写锁不能同时获得无需说明，下面一段程序说明下上了读锁以后，其他线程需要上写锁也无法获得
     * @throws InterruptedException
     */
    public void test2() throws InterruptedException {

        final int count = 2;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        executorService.submit(() -> {
            System.out.println("get readLock");
            readWriteLock.readLock().lock();
            System.out.println("get readLock already");
            try {
                Thread.currentThread().sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("get readUnLock");
            readWriteLock.readLock().unlock();
            System.out.println("get readUnLock already");

            countDownLatch.countDown();
        });

        executorService.submit(() -> {
            System.out.println("get writeLock");
            readWriteLock.writeLock().lock();
            System.out.println("get writeLock already");

            System.out.println("get writeUnLock");
            readWriteLock.writeLock().unlock();
            System.out.println("get writeUnLock already");

            countDownLatch.countDown();
        });

        countDownLatch.await();
        System.out.println("finish");
    }


    @Data
    public static class IncrementDemo {
        private ReentrantLock reentrantLock = new ReentrantLock();

        private int num;

        public void inc() {
            reentrantLock.lock();
            try {
                num++;
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
