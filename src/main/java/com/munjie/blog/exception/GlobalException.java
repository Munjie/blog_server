package com.munjie.blog.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.munjie.blog.enums.ExceptionEnum;
import com.munjie.blog.pojo.Response;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:03
 * @Description:
 */
@RestControllerAdvice
public class GlobalException {

    private static  Logger log = LoggerFactory.getLogger(GlobalException.class);


    @ExceptionHandler
    public void sqlExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("全局异常",ex);
        response.setContentType("text/html;charset=UTF-8");
        Response errorResponse = Response.error(ExceptionEnum.UNKNOWN_ERROR);
        if (ex instanceof ParamsException) {
            errorResponse = Response.error(((ParamException)ex).getCode(), ex.getMessage());
        } else if (ex instanceof NoHandlerFoundException) {
            errorResponse = Response.error(ExceptionEnum.NOT_FOUND);
        } else if (ex instanceof SQLException) {
            errorResponse = Response.error(ExceptionEnum.DATABASE_ERROR);
        } else if (ex instanceof ConnectException) {
            errorResponse = Response.error(ExceptionEnum.CONNECTION_ERROR);
        } else if (ex instanceof BindException) {
            errorResponse = Response.error(ExceptionEnum.BAD_REQUEST);
        } else if (ex instanceof IOException) {
            errorResponse = Response.error(ExceptionEnum.IOEXCEPTION);
        } else if (ex instanceof ConstraintViolationException) {
            errorResponse = Response.error(ExceptionEnum.CONSTRAINTVIOLATION);
        } else if (ex instanceof MyBatisSystemException) {
            errorResponse = Response.error(ExceptionEnum.MYBATISSYSTEMEXCEPTION);
        } else if (ex instanceof MultipartException) {
            errorResponse = Response.error(ExceptionEnum.FILE_TOO_LARGE);
        } else if (ex instanceof MissingServletRequestParameterException) {
            errorResponse = Response.error(ExceptionEnum.PARAM_NULL);
        } else if (ex instanceof HttpMessageNotReadableException) {
            errorResponse = Response.error(ExceptionEnum.JSONPARSEERROR);
        } else if (ex instanceof CustomizeException) {
            errorResponse = Response.error(((CustomizeException)ex).getCode(), ex.getMessage());
        }
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        try {
            response.getWriter().write(gson.toJson(errorResponse));
        } catch (IOException var7) {
            log.error("返回信息异常{}", var7);
        }

    }
}
