package com.munjie.blog.handler;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.munjie.blog.config.LoggerQueue;
import com.munjie.blog.pojo.LoggerMessage;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
/**
 * @Auther: munjie
 * @Date: 2/25/2021 22:41
 * @Description:
 */
@Service
public class LogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        LoggerMessage loggerMessage = new LoggerMessage(
                event.getMessage()
                , DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())),
                event.getThreadName(),
                event.getLoggerName(),
                event.getLevel().levelStr
        );
        LoggerQueue.getInstance().push(loggerMessage);
        return FilterReply.ACCEPT;
    }
}