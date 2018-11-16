package com.vic.test.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: vic
 * @CreateTime : 2018/8/16 17:35
 */
public class MyMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyPublisher bean = context.getBean(MyPublisher.class);
        bean.sendMessage("哈哈哈");
        context.close();
    }
}
