package com.vic.test.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by vic
 * Create time : 2017/6/7 下午8:02
 */
public class ThreadPoolUtil {

    private static ExecutorService instance = Executors.newFixedThreadPool(10);

    public static synchronized ExecutorService getInstance() {
        if (null == instance) {
            instance = Executors.newFixedThreadPool(10);
        }
        return instance;
    }
}
