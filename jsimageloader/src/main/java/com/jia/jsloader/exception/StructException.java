package com.jia.jsloader.exception;

/**
 * Created by hm on 2016/1/12.
 *
 *  这里是框架中关于异常相关的一个自定义异常类，用来表示框架中可能出现的异常
 *
 *  当框架中存在异常的时候使用异常将错误抛出
 *
 *
 */
public class StructException extends RuntimeException {

    public StructException(String exMessage){
        super(exMessage);
    }
}
