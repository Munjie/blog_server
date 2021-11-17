package com.munjie.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.munjie.blog.dao.AddressMapper;
import com.munjie.blog.dao.TaskInfoMapper;
import com.munjie.blog.enums.ExceptionEnum;
import com.munjie.blog.exception.CustomizeException;
import com.munjie.blog.pojo.AddressDO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.pojo.TaskInfoDO;
import com.munjie.blog.pojo.TaskInfoDTO;
import com.munjie.blog.service.TaskService;
import com.munjie.blog.utils.CommonUtil;
import com.munjie.blog.utils.ExportExcelUtils;
import com.munjie.blog.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;


@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Value("${baidu.key}")
    private String key;
    @Value("${baidu.geocoding}")
    private String geocoding;
    @Value("${baidu.routematrix}")
    private String routematrix;


        //2003格式
        private static final String SUFFIX_XLS = ".xls";
        //2007格式
        private static final String SUFFIX_XLSX = ".xlsx";

        @Autowired
        private AddressMapper addressMapper;

        @Autowired
        private TaskInfoMapper taskInfoMapper;
        @Override
        public Response createTask(TaskInfoDO info, MultipartFile file, HttpServletRequest request) {
            String taskId = "AK" + System.currentTimeMillis();
            Map<String, String> lngAndLat = getLngAndLat(info.getOriginAdd());
            String oriLng = lngAndLat.get("lng");
            String oriLat = lngAndLat.get("lat");
            if (info != null) {
                info.setCreateTime(new Date());
                info.setTaskId(taskId);
            }
            int insert = taskInfoMapper.insert(info);
            int count = 0;
            if (file == null) {
                throw new CustomizeException(ExceptionEnum.PARAM_NULL);
            }
            //获取文件的名字
            String originalFilename = file.getOriginalFilename();
            Workbook workbook = null;
            try {
                if (originalFilename.endsWith(SUFFIX_XLS)) {
                    workbook = new HSSFWorkbook(file.getInputStream());
                } else if (originalFilename.endsWith(SUFFIX_XLSX)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                }
            } catch (Exception e) {
                LOGGER.error("数据文件格式异常", e);
                throw new CustomizeException(ExceptionEnum.EXCEL_ERROR);
            }
            if (workbook == null) {
                LOGGER.info(originalFilename);
                throw new CustomizeException(ExceptionEnum.EXCEL_ERROR);
            } else {
                int numOfSheet = workbook.getNumberOfSheets();
                //循环sheet
                for (int i = 0; i < numOfSheet; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    if (sheet != null) {
                        int lastRowNum = sheet.getLastRowNum();
                        //循环table
                        for (int j = 0; j <= lastRowNum; j++) {
                            Row row = sheet.getRow(j);
                            if (row != null) {
                                    AddressDO address = new AddressDO();
                                    boolean flag = true;
                                    String  province = "";
                                    String  city = "";
                                    String  country = "";
                                    try {
                                        if (row.getCell(0) != null) {
                                            row.getCell(0).setCellType(CellType.STRING);
                                            province= row.getCell(0).getStringCellValue();
                                            province = CommonUtil.getChinese(province);
                                            List<String> stringList = listString(province);
                                            if (stringList != null) {
                                                address.setProvince(stringList.get(0));
                                                if (stringList.size() > 1) {
                                                    address.setCity(stringList.get(1));
                                                }
                                                if (stringList.size() > 2) {
                                                    address.setCountry(stringList.get(2));
                                                }
                                            }
                                            address.setCreateTime(new Date());
                                            address.setTaskId(taskId);
//                                            Map<String, String> des = getLngAndLat(info.getOriginAdd());
//                                            String desLng = des.get("lng");
//                                            String desLat = des.get("lat");
//                                            Map<String, String> stringStringMap = calcMap(oriLat,oriLng,desLat, desLng);
                                            Map<String, String> stringStringMap = calcMap2(info.getOriginAdd(), province);
                                            if (stringStringMap != null) {
                                                if (stringStringMap.get("mZlc") != null) {
                                                    address.setDistance(stringStringMap.get("mZlc"));
                                                }
                                                if (stringStringMap.get("mTime") != null) {
                                                    address.setDuration(stringStringMap.get("mTime"));
                                                }
                                            }
                                            int i1 = addressMapper.addAddress(address);
                                            count = count + 1;
                                        }
                                    } catch (CustomizeException e) {
                                        LOGGER.error("插入数据异常", e);
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                        throw new CustomizeException(ExceptionEnum.DATABASE_ERROR);
                                    }


                            }
                        }
                    }
                }
            }
            Map map = new HashMap(16);
            map.put("count",count);
            map.put("taskId",taskId);
            return Response.ok(map);
        }

        public Map<String,String> getLngAndLat (String address){
            String coding = HttpClientUtil.doGet(geocoding + address + "&output=json&ak=" + key);
            JSONObject jsonObject = JSONObject.parseObject(coding);
            Map<String,String> map = new HashMap<>();
            if (jsonObject != null) {
                JSONObject result = jsonObject.getJSONObject("result");
                if (result != null) {
                    JSONObject location = result.getJSONObject("location");
                    if (location != null) {
                        String lng = location.getString("lng");
                        String lat = location.getString("lat");
                        map.put("lng",lng);
                        map.put("lat",lat);
                    }

                }

            }
            return map;
        }
    public  Map<String, String> calcMap(String oriLat, String oriLng,String desLat,String desLng) {
        Map<String, String> params = new HashMap<String, String>(16);
        params.put("output", "json");//输出方式为json
        params.put("tactics", "11");//10不走高速11常规路线12 距离较短（考虑路况）13距离较短（不考虑路况）
        params.put("ak", key);
        // origins 起点 destinations 目的地
        params.put("origins", oriLat + "," + oriLng);
        params.put("destinations", desLat + "," + desLng);
        String result = HttpClientUtil.doGet(routematrix, params);
        Map<String,String> map = null ;
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("result");
            if (jsonArray != null) {
                //获取json长度
                int  JsonLen = 0;
                for (Object object : jsonArray) {
                    JsonLen++;
                }
                int i;
                for (i = 0; i < JsonLen; i++) {
                    map = new HashMap<String,String>();
                    map.put("mTime",jsonArray.getJSONObject(i).getJSONObject("duration").getString("text"));
                    map.put("mZlc",jsonArray.getJSONObject(i).getJSONObject("distance").getString("text"));
                    map.put("mZzlc",jsonArray.getJSONObject(i).getJSONObject("distance").getString("value"));
                }
            }
        }
        return map;
    }

    public Map<String, String> calcMap2(String originAdd, String destinations) {
        Map<String, String> params = new HashMap<String, String>(16);
        // 起始点
        String originDouble = HttpClientUtil
                .doGet(geocoding + originAdd + "&output=json&ak=" + key);
        // 终点
        String desDouble = HttpClientUtil
                .doGet(geocoding + destinations + "&output=json&ak=" + key);
        JSONObject jsonObjectOri = JSONObject.parseObject(originDouble);
        JSONObject jsonObjectDes = JSONObject.parseObject(desDouble);
        // String oriLng = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lng");// 经度值ֵ
        // String oriLat = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lat");// 纬度值ֵ
        // String desLng = jsonObjectDes.getJSONObject("result").getJSONObject("location").getString("lng");
        // String desLat = jsonObjectDes.getJSONObject("result").getJSONObject("location").getString("lat");
        if (jsonObjectOri != null && jsonObjectDes != null) {
            JSONObject oriJSONObject = jsonObjectOri.getJSONObject("result");
            JSONObject desJSONObject = jsonObjectDes.getJSONObject("result");
            if (oriJSONObject != null && desJSONObject != null) {
                JSONObject oriJSONObjectJSONObject = oriJSONObject.getJSONObject("location");
                JSONObject desJSONObjectJSONObject = desJSONObject.getJSONObject("location");
                if (oriJSONObjectJSONObject != null && desJSONObjectJSONObject != null) {
                    String oriLng = oriJSONObjectJSONObject.getString("lng");
                    String oriLat = oriJSONObjectJSONObject.getString("lat");
                    String desLng = desJSONObjectJSONObject.getString("lng");
                    String desLat = desJSONObjectJSONObject.getString("lat");
                    params.put("output", "json");//输出方式为json
                    params.put("tactics", "11");//10不走高速11常规路线12 距离较短（考虑路况）13距离较短（不考虑路况）
                    params.put("ak", key);
                    // origins 起点 destinations 目的地
                    String originsLaLn = "?origins=" + oriLat + "," + oriLng;
                    String destinationsLaLn = "&destinations=" + oriLat + "," + oriLng;
                    params.put("origins", oriLat + "," + oriLng + "|" + oriLat + "," + oriLng);
                    params.put("destinations", desLat + "," + desLng + "|" + desLat + "," + desLng);
                }

            }
        }


        String result = HttpClientUtil.doGet(routematrix, params);
        Map<String, String> map = null;
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("result");
            if (jsonArray != null) {
                //获取json长度
                int JsonLen = 0;
                for (Object object : jsonArray) {
                    JsonLen++;
                }
                int i;
                for (i = 0; i < JsonLen; i++) {
                    map = new HashMap<String, String>();
                    map.put("mTime", jsonArray.getJSONObject(i).getJSONObject("duration").getString("text"));
                    map.put("mZlc", jsonArray.getJSONObject(i).getJSONObject("distance").getString("text"));
                    map.put("mZzlc", jsonArray.getJSONObject(i).getJSONObject("distance").getString("value"));
                }
            }
        }
        return map;
    }

        @Override
        public Response deleteTask(String taskId) throws Exception {
            int i = taskInfoMapper.deleteTask(taskId);
            int i1 = addressMapper.deleteAddressByTaskId(taskId);
            return Response.ok("删除成功");
        }

    @Override
    public TaskInfoDTO listTask(Integer pageSize, Integer pageNo) {
        PageHelper.startPage(pageNo, pageSize);
        List<TaskInfoDO> list = taskInfoMapper.listTask();
        for (TaskInfoDO ta:list) {
            if (ta != null) {
                ta.setCreateDate(CommonUtil.formatDate(ta.getCreateTime()));
            }
        }
        PageInfo<TaskInfoDO> taskInfoDOPageInfo = new PageInfo<>(list);
        return (new TaskInfoDTO(taskInfoDOPageInfo.getList(),taskInfoDOPageInfo.getTotal()));
    }

    @Override
    public void downloadExcel(HttpServletResponse response, String taskId) {
        //得到所有要导出的数据
        List<AddressDO> addressDOList =addressMapper.listAddress(taskId);
        //定义导出的excel名字
        String excelName = "批量算路_" + taskId;
        //获取需要转出的excel表头的map字段
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("province","省份");
        fieldMap.put("city","地级市");
        fieldMap.put("country","县城（区县）");
        fieldMap.put("address","城市（具体地址）");
        fieldMap.put("distance","公里数");
        fieldMap.put("duration","送货时间 （天）");
        ExportExcelUtils.export(excelName,addressDOList,fieldMap,response);
    }


    //导入为模版

    public List<String> listString(String text)  {
        //创建分词对象
        List<String> stringList = new ArrayList<>();
        Analyzer anal=new IKAnalyzer(true);
        StringReader reader=new StringReader(text);
        try {
            //分词
            TokenStream ts=anal.tokenStream("", reader);
            CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while(ts.incrementToken()){
                stringList.add(term.toString());
            }
            reader.close();
        }catch (IOException exception) {
            throw new CustomizeException(ExceptionEnum.IK_ERROR);
        }
        return stringList;
    }

}
