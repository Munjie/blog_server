package com.munjie.blog.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;


public class AddressUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressUtil.class);

    /**
     * 代码参考官方：https://github.com/lionsoul2014/ip2region/blob/master/binding/java/src/main/java/org/lionsoul/ip2region/test/TestSearcher.java
     *
     * @param request
     * @return
     */
    public static String getAddress(HttpServletRequest request)  {
        String ip = getIpAddress(request);
        if (StringUtils.isEmpty(ip)){
            return "IP is NULL";
        }
        //db
        String dbPath = AddressUtil.class.getResource("/config/ip2region.db").getPath();

        File file = new File(dbPath);

        if (!file.exists()) {
            String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
            dbPath = tmpDir + "ip.db";
            file = new File(dbPath);
            try {
                FileUtils.copyInputStreamToFile(AddressUtil.class.getClassLoader().getResourceAsStream("classpath:config/ip2region.db"), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree

        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, file.getPath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            //define the method
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }

            DataBlock dataBlock = null;
            if (!Util.isIpAddress(ip)) {
                System.out.println("Error: Invalid ip address");
            }
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            reader.close();
            searcher.close();
            return dataBlock.getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                LOGGER.info("HTTP_X_FORWARDED_FOR={}",ip);
            }else if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip) ) {
                ip = request.getHeader("X-Forwarded-For");
                LOGGER.info("X-Forwarded-For={}",ip);
            }else if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip) ) {
                ip = request.getHeader("X-Real-IP");
                LOGGER.info("X-Real-IP={}",ip);
            }else if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip) ) {
                ip = request.getRemoteAddr();
                LOGGER.info("OTHER={}",ip);
            }
        } catch (Exception e) {
            LOGGER.error("IPUtils ERROR ", e);
        }

        //        //使用代理，则获取第一个IP地址
        //        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
        //          if(ip.indexOf(",") > 0) {
        //              ip = ip.substring(0, ip.indexOf(","));
        //          }
        //      }
        return ip;
    }

    public  static String getIpAddress(HttpServletRequest request)  {
        // 获取nginx代理前的ip地址
        String ip = null;
        ip = request.getHeader("X-Real-IP");
        LOGGER.info("X-Real-IP={}",ip);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("getIpAddress(X-real-ip) - X-real-ip - String ip=" + ip);
        }
        // 获取所有代理记录的ip地址
        String refererIps = request.getHeader("X-Forwarded-For");
        LOGGER.info("refererIps={}",refererIps);
        if (StringUtils.isNotEmpty(refererIps)){
            String[] split = refererIps.trim().split(",");
            if (split != null && split.length >= 2) {
                // 获取请求最开始的ip
                ip = split[0];
                LOGGER.info("getIpAddress(x-forwarded-for) - x-forwarded-for - String ip=" + refererIps);
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        LOGGER.info("final request ip : {}", ip);
        return ip;
    }
}
