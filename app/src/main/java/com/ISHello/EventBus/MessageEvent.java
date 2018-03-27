package com.ISHello.EventBus;

/**
 * Created by zhanglong on 2018/3/2.
 */

/**
 * 消息类
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
