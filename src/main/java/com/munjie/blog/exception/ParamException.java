package com.munjie.blog.exception;

import com.munjie.blog.enums.ExceptionEnum;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:02
 * @Description:
 */
public class ParamException extends RuntimeException{

    private int code;
    private String message;

    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
        this.code = 0;
        this.message = message;
    }

    public ParamException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ParamException(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
