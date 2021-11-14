package com.munjie.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/map")
public class MapController {

    /**
     * https://api.map.baidu.com/geocoding/v3/?address=北京市海淀区上地十街10号&output=json&ak=您的ak&callback=showLocation
     * @param origins
     * @param destinations
     * @param
     * @return
     * @throws UnsupportedEncodingException
     */
    //@RequestMapping(value = "showMap",method = RequestMethod.POST)
   /* public static String showMap(@RequestParam("origins") String origins,
                                 @RequestParam("destinations") String destinations
                                 ) throws UnsupportedEncodingException {
        String ak = "ihobW2ZNw3i96MOadGGRx2BN7pjxX3bQ";
        Map<String, String> params = new HashMap<String, String>();
        // 起始点
        String originDouble = HttpClientUtil
                .doGet("https://api.map.baidu.com/geocoding/v3/?address="+origins+"&output=json&ak="+ak);
        // 终点
        String desDouble = HttpClientUtil
                .doGet("https://api.map.baidu.com/geocoding/v3/?address="+destinations+"&output=json&ak="+ak);
        JSONObject jsonObjectOri = JSONObject.parseObject(originDouble);
        JSONObject jsonObjectDes = JSONObject.parseObject(desDouble);
        String oriLng = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lng");// 经度值ֵ
        String oriLat = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lat");// 纬度值ֵ

        String desLng = jsonObjectDes.getJSONObject("result").getJSONObject("location").getString("lng");
        String desLat = jsonObjectDes.getJSONObject("result").getJSONObject("location").getString("lat");
        params.put("output", "json");//输出方式为json
        params.put("tactics", "11");//10不走高速11常规路线12 距离较短（考虑路况）13距离较短（不考虑路况）
        params.put("ak", ak);
        // origins 起点 destinations 目的地
        params.put("origins", oriLat + "," + oriLng + "|" + oriLat + "," + oriLng);
        params.put("destinations", desLat + "," + desLng + "|" + desLat + "," + desLng);

        String result = HttpClientUtil.doGet("http://api.map.baidu.com/routematrix/v2/driving", params);
        JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("result");
        //获取json长度
        int  JsonLen = 0;
        for (Object object : jsonArray) {
            //System.out.println(object);
            JsonLen++;
        }
        //System.out.println("推荐方案:");
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        Map<String,String> map = null;
        int i;
        for (i = 0; i < JsonLen; i++) {
            map = new HashMap<String,String>();
            map.put("mTime",jsonArray.getJSONObject(i).getJSONObject("duration").getString("text"));
            map.put("mZlc",jsonArray.getJSONObject(i).getJSONObject("distance").getString("text"));
            map.put("mZzlc",jsonArray.getJSONObject(i).getJSONObject("distance").getString("value"));
        }
        mapList.add(map);
        return "map";
    }*/

 /*   @SneakyThrows
    public static void main(String[] args) {
        String s = MapController.showMap("无锡江阴市亚包大道199号GLP仓库", "安徽宣城");
    }*/


}

