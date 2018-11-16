package com.vic.test.spring;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: vic
 * @CreateTime : 2018/8/16 17:37
 */
@Data
public class MyEvent extends ApplicationEvent{
    private static final long serialVersionUID = -3369644577113675879L;
    private String message;

    public MyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
