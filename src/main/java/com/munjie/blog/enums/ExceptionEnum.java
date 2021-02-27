package com.munjie.blog.enums;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:02
 * @Description:
 */
public enum ExceptionEnum {

    UNKNOWN_ERROR(0, "未知错误"),
    BAD_REQUEST(1001, "错误的请求参数"),
    NOT_FOUND(1002, "请求路径不存在！"),
    CONNECTION_ERROR(1003, "网络连接请求失败！"),
    DATABASE_ERROR(1004, "数据库异常"),
    PARAM_NULL(1010, "您有参数未添加"),
    FILE_TOO_LARGE(1023, "上传文件过大"),
    NO_PERMISSION(1024, "您没有权限访问"),
    IOEXCEPTION(1025, "IO输入输出异常"),
    CONSTRAINTVIOLATION(1026, "校验不通过"),
    MYBATISSYSTEMEXCEPTION(1027, "mybatis参数绑定异常"),
    JSONPARSEERROR(1028, "json转换异常");


    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
