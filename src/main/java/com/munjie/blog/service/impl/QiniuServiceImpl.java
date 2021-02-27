package com.munjie.blog.service.impl;

import com.munjie.blog.service.QiniuService;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;

/**
 * @Auther: munjie
 * @Date: 2/23/2021 00:47
 * @Description:
 */
@Service
public class QiniuServiceImpl implements QiniuService {

    public static String accessKey = "b-lHQMCnIt3TO4P7QL1hVM06sm2cxAqN-53uwWDe";
    public static String secretKey = "mSll8IcTHHotFymXpvrkPETXwPm9sfUrhn_VtWF_";
    public static String bucket = "muwenjie";
    private static final String QINIU_IMAGE_DOMAIN = "http://cdn.munjie.com";

    @Override
    public String getToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }
}
