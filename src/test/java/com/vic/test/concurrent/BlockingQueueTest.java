package com.vic.test.concurrent;

import junit.framework.TestCase;
import lombok.Data;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Created by vic
 * Create time : 2018/6/22 10:39
 */
public class BlockingQueueTest extends TestCase {

    public void testArray() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        Thread producer = new Thread(new Producer(queue));
        Thread consumer = new Thread(new Consumer(queue));

        producer.start();
        consumer.start();

        Thread.sleep(10000000L);
    }

    public void testPriority() throws InterruptedException {
        BlockingQueue blockingQueue = new PriorityBlockingQueue();

        blockingQueue.put(new Person("张三", 20));
        Thread.sleep(5);
        blockingQueue.put(new Person("李四", 18));
        Thread.sleep(5);
        blockingQueue.put(new Person("王五1", 21));
        Thread.sleep(5);
        blockingQueue.put(new Person("王五", 21));
        Thread.sleep(5);
        blockingQueue.put(new Person("会员", 100));
        Thread.sleep(5);
        blockingQueue.put(new Person("王五2", 21));


        while (blockingQueue.size() > 0) {
            System.out.println(blockingQueue.take());
        }
    }

    /**
     * 利用多个线程同时计算，把每一个数字计算平方
     */
    public void testCountDownLatch() throws InterruptedException {

        StopWatch stopWatch = new StopWatch("计时开始");
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        CountDownLatch countDownLatch = new CountDownLatch(data.length);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        stopWatch.start();
        for (int i = 0; i < data.length; i++) {
            executor.execute(new Processor(countDownLatch, data, i));
        }
        countDownLatch.await();
        stopWatch.stop();
        executor.shutdown();
        System.out.println(Arrays.toString(data));
        System.out.println(stopWatch.prettyPrint());
    }


    // ------------------------ 分割线 -------------------------------- //

    public class Producer implements Runnable {

        private BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                int i = 0;
                while (true) {
//                    int i = new Random().nextInt(1000);
                    queue.put(i++);
                    Thread.sleep(500L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Consumer implements Runnable {

        private BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (queue.size() > 0) {
                        System.out.println("size=" + queue.size() + " take=" + queue.take());
                    }
                    Thread.sleep(1000L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    public class Person implements Comparable {
        private String name;
        private int age;
        private long timestamp;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(Object o) {
            Person person = (Person) o;
            int result = person.getAge() - this.age;
            if (result == 0) {
                return Long.valueOf(this.timestamp).compareTo(Long.valueOf(person.getTimestamp()));
            }
            return result;
        }
    }

    public class Processor implements Runnable {

        private CountDownLatch countDownLatch;
        private int[] data;
        private int index;

        public Processor(CountDownLatch countDownLatch, int[] data, int index) {
            this.countDownLatch = countDownLatch;
            this.data = data;
            this.index = index;
        }

        @Override
        public void run() {
            if (data.length > index) {
                int datum = data[index];
                System.out.println(Thread.currentThread().getName()+">>>"+datum);
                data[index] = datum * datum;
            }
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }
    }
}
