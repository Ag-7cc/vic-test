package com.vic.test.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: vic
 * @CreateTime : 2018/8/16 17:36
 */
@Component
public class Mylistener implements ApplicationListener<MyEvent> {

    @Override
    public void onApplicationEvent(MyEvent event) {
        System.err.println("接收到消息" + event.getMessage());
    }
}
