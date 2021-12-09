package com.munjie.blog.controller;

import com.alibaba.fastjson.JSON;
import com.munjie.blog.pojo.*;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Validated
@Api(tags = "Live2dController")
@RestController
@RequestMapping("/live")
@CrossOrigin
public class Live2dController {


    @GetMapping("/get")
    public JsonRootBean listTask(String id) {
        //提醒
        JsonRootBean jsonRootBean = new JsonRootBean();
        jsonRootBean.setVersion("1.0.0");
        jsonRootBean.setModel("../chitose.moc");
        List<String> textures = new ArrayList<>();
        textures.add("../00.png");
        textures.add("../01.png");
        jsonRootBean.setTextures(textures);
        jsonRootBean.setPhysics("../physics.json");
        jsonRootBean.setPose("../pose.json");
        Layout layout = new Layout();
        layout.setCenter_x("0");
        layout.setCenter_y("-0.5");
        layout.setWidth("3");
        Hit_areas_custom hit_areas_custom = new Hit_areas_custom();
        List<Double> head_x = new ArrayList<>();
        head_x.add(-0.35);
        head_x.add(0.6);
        List<Double> head_y = new ArrayList<>();
        head_y.add(0.19);
        head_y.add(-0.2);
        List<Double> body_x = new ArrayList<>();
        body_x.add(-0.3);
        body_x.add(-0.25);
        List<Double> body_y = new ArrayList<>();
        body_y.add(0.3);
        body_y.add(-0.9);
        hit_areas_custom.setBody_x(body_x);
        hit_areas_custom.setBody_y(body_y);
        hit_areas_custom.setHead_x(head_x);
        hit_areas_custom.setHead_y(head_y);
        jsonRootBean.setHit_areas_custom(hit_areas_custom);
        Motions motions = new Motions();
        List<Idle> idleList = new ArrayList<>();
        Idle idle = new Idle();
        idle.setFile("../04.mtn");
        idle.setFade_in("2000");
        idle.setFade_out("2000");
        Idle idle1 = new Idle();
        idle1.setFile("../01.mtn");
        idle1.setFade_in("2000");
        idle1.setFade_out("2000");
        Idle idle2 = new Idle();
        idle2.setFile("../02.mtn");
        idle2.setFade_in("2000");
        idle2.setFade_out("2000");
        Idle idle3 = new Idle();
        idle3.setFile("../03.mtn");
        idle3.setFade_in("2000");
        idle3.setFade_out("2000");
        Idle idle4 = new Idle();
        idle4.setFile("../04.mtn");
        idle4.setFade_in("2000");
        idle4.setFade_out("2000");
        idleList.add(idle);
        idleList.add(idle1);
        idleList.add(idle2);
        idleList.add(idle3);

        List<Flick_head> flick_headList = new ArrayList<>();
        Flick_head flick_head = new Flick_head();
        flick_head.setFile("../smile.mtn");
        flick_headList.add(flick_head);

        List<Tap_body> tap_bodyList = new ArrayList<>();
        Tap_body tap_body = new Tap_body();
        tap_body.setDialogue(11);
        tap_body.setSound("../001.ogg");
        tap_body.setFile("../nep_touch_head_1.mtn");
        tap_bodyList.add(tap_body);

        motions.setIdle(idleList);
        motions.setFlick_head(flick_headList);
        motions.setTap_body(tap_bodyList);
        jsonRootBean.setMotions(motions);
        List<Expressions> expressions = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Expressions ex = new Expressions();
            ex.setName("f0" + i);
            ex.setFile("../f0" + i + ".exp.json");
            expressions.add(ex);
        }
        jsonRootBean.setExpressions(expressions);
        //jsonRootBean.setLayout(la);


        return jsonRootBean;

    }


    @GetMapping("/{id}")
    public JSON download(HttpServletResponse response, @PathVariable("id") String id) {
        String path = "";
        if (id.contains("json")) {
            String s = readJsonFile("/home/springboot/" + id);
            return JSON.parseObject(s);
        } else if (id.contains("png")) {
            return jpg(response, id);
        } else if (id.contains("moc")) {
            path = "/home/springboot/chitose.moc";
        } else {
            path = "/home/springboot/" + id;

        }
        try {
            // path是指想要下载的文件的路径
            File file = new File(path);

            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();


            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSON jpg(HttpServletResponse response, String id) {

        String path = "/home/springboot/" + id;
        try {
            // path是指想要下载的文件的路径
            File file = new File(path);

            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();


            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    //读取json文件
    private String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}