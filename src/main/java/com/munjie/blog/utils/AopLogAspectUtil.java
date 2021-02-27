package com.munjie.blog.utils;

import com.alibaba.fastjson.JSON;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/** @Auther: munjie @Date: 2/19/2021 21:40 @Description: */
public class AopLogAspectUtil {
  public static StringBuffer getNameAndArgs(
      Class<?> cls, String clazzName, String methodName, Object[] args) throws NotFoundException {

    Map<String, Object> nameAndArgs = new HashMap<>();

    ClassPool pool = ClassPool.getDefault();
    ClassClassPath classPath = new ClassClassPath(cls);
    pool.insertClassPath(classPath);

    CtClass cc = pool.get(clazzName);
    CtMethod cm = cc.getDeclaredMethod(methodName);
    MethodInfo methodInfo = cm.getMethodInfo();
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
    LocalVariableAttribute attr =
        (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
    if (attr == null) {
      // exception
    }
    boolean isArgs = true;
    int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
    int size = args.length;
    for (int i = 0; i < size; i++) {
      // 解决序列化异常
      if (args[i] instanceof ServletRequest
          || args[i] instanceof ServletResponse
          || args[i] instanceof MultipartFile) {
        isArgs = false;
      }
    }
    if (isArgs) {
      size = cm.getParameterTypes().length;
      for (int i = 0; i < size; i++) {
        nameAndArgs.put(attr.variableName(i + pos), args[i]); // paramNames即参数名
      }
    }

    boolean flag = false;
    if (nameAndArgs != null && nameAndArgs.size() > 0) {
      for (Map.Entry<String, Object> entry : nameAndArgs.entrySet()) {
        if (entry.getValue() instanceof String) {
          flag = true;
          break;
        }
      }
    }
    StringBuffer sb = new StringBuffer();
    if (flag) {
      // 从Map中获取
      sb.append(JSON.toJSONString(nameAndArgs));
    } else {
      if (args != null) {
        for (Object object : args) {
          if (object != null) {
            if (object instanceof MultipartFile
                || object instanceof ServletRequest
                || object instanceof ServletResponse) {
              continue;
            }
            sb.append(JSON.toJSONString(object));
            if (sb != null) {
              return sb;
            }
          }
        }
      }
    }
    return sb;
  }
    }
