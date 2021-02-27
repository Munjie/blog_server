package com.munjie.blog.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/** @Author: 母哥 @Date: 2019-03-08 10:22 @Version 1.0 */
@Component
public class FileUtil {

  private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

  public static String accessKey = "b-lHQMCnIt3TO4P7QL1hVM06sm2cxAqN-53uwWDe";
  public static String secretKey = "mSll8IcTHHotFymXpvrkPETXwPm9sfUrhn_VtWF_";
  public static String bucket = "muwenjie";
  private static final String QINIU_IMAGE_DOMAIN = "http://cdn.munjie.com";

  // 文件流上传
  public static String upload(MultipartFile file) throws IOException {
    /* 华东	Region.region0(), Region.huadong()
    华北	Region.region1(), Region.huabei()
    华南	Region.region2(), Region.huanan()
    北美	Region.regionNa0(), Region.beimei()
    东南亚	Region.regionAs0(), Region.xinjiapo()*/

    // 构造一个带指定 Region 对象的配置类
    Configuration cfg = new Configuration(Region.region0());
    // ...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    // ...生成上传凭证，然后准备上传
    // 默认不指定key的情况下，以文件内容的hash值作为文件名
    String key = null;
    DefaultPutRet putRet = null;
    try {
      byte[] uploadBytes = file.getBytes();
      ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
      Auth auth = Auth.create(accessKey, secretKey);
      String upToken = auth.uploadToken(bucket);

      try {
        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        // 解析上传成功的结果
        putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
      } catch (QiniuException ex) {
        Response r = ex.response;
        System.err.println(r.toString());
        try {
          System.err.println(r.bodyString());
        } catch (QiniuException ex2) {
          // ignore
        }
      }
    } catch (UnsupportedEncodingException ex) {
      // ignore
    }
    return QINIU_IMAGE_DOMAIN + "/" + putRet.key;
  }
}
