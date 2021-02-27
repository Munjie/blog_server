package com.munjie.blog.exception;

import com.munjie.blog.enums.ExceptionEnum;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:01
 * @Description:
 */
public class CustomizeException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    protected Integer code;
    protected String message;

    public CustomizeException(ExceptionEnum enums, String message) {
        this.code = enums.getCode();
        this.message = message;
    }

    public CustomizeException(ExceptionEnum enums) {
        this.code = enums.getCode();
        this.message = enums.getMsg();
    }

    public CustomizeException(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomizeException() {
    }

    public CustomizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizeException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }
}
