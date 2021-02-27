package com.munjie.blog.exception;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:06
 * @Description:
 */
public class ParamsException extends Exception{

    private String paramName;
    private String paramType;

    public ParamsException() {
        super("必填参数为空");
    }

    public ParamsException(String paramName, String paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    public ParamsException(String message) {
        super(message);
    }

    public ParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsException(Throwable cause) {
        super(cause);
    }

    public ParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return paramName + "  " + paramType + "  参数不能为null";
    }
}
