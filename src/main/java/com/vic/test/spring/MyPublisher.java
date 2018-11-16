package com.vic.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: vic
 * @CreateTime : 2018/8/16 17:42
 */
@Component
public class MyPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    public void sendMessage(String message) {
        applicationContext.publishEvent(new MyEvent(this, message));
    }
}
