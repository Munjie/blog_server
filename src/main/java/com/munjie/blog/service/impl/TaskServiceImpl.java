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
import com.munjie.blog.utils.HttpClientUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class TaskServiceImpl implements TaskService {

        protected static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Value("${baidu.originAdd}")
    private String originAdd;
    @Value("${baidu.key}")
    private String key;
    @Value("${baidu.geocoding}")
    private String geocoding;
    @Value("${baidu.routematrix}")
    private String routematrix;
    @Value("${baidu.logisticsRoutematrix}")
    private String logisticsRoutematrix;

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
            if (info != null) {
                info.setCreateTime(new Date());
                info.setTaskId(taskId);
            }
            int insert = taskInfoMapper.insert(info);
            int count = 0;
            StringBuilder stringBuffer = new StringBuilder();
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
                throw new CustomizeException(ExceptionEnum.UNKNOWN_ERROR);
            }
            if (workbook == null) {
                LOGGER.info(originalFilename);
                throw new CustomizeException(ExceptionEnum.UNKNOWN_ERROR);
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
                                if (j == 0) {
                                    if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null) {
                                        throw new CustomizeException("");
                                    } else {
                                        row.getCell(0).setCellType(CellType.STRING);
                                        row.getCell(1).setCellType(CellType.STRING);
                                        row.getCell(2).setCellType(CellType.STRING);
                                    }
                                } else if (j >= 1) {
                                    AddressDO address = new AddressDO();
                                    boolean flag = true;
                                    String  province = "";
                                    String  city = "";
                                    String  country = "";
                                    try {
                                        if (row.getCell(0) != null) {
                                            row.getCell(0).setCellType(CellType.STRING);
                                            province= row.getCell(0).getStringCellValue();
                                            address.setProvince(province);
                                            if (province.contains("提货地址")) {
                                                flag = false;
                                            }
                                        }
                                        if (row.getCell(1) != null) {
                                            row.getCell(1).setCellType(CellType.STRING);
                                            city = row.getCell(1).getStringCellValue();
                                            address.setCity(city);
                                        }
                                        if (row.getCell(2) != null) {
                                            row.getCell(2).setCellType(CellType.STRING);
                                            country = row.getCell(2).getStringCellValue();
                                            address.setCountry(country);
                                        }
                                        if (row.getCell(3) != null) {
                                            row.getCell(3).setCellType(CellType.STRING);
                                            String add = row.getCell(3).getStringCellValue();
                                            address.setAddress(add);
                                        }
                                        if (flag) {
                                            Map<String, String> stringStringMap = calcMap(province + country);
                                            address.setCreateTime(new Date());
                                            address.setTaskId(taskId);
                                            address.setDistance(stringStringMap.get("mZlc"));
                                            address.setDuration(stringStringMap.get("mTime"));
                                            int i1 = addressMapper.addAddress(address);
                                        }
                                    } catch (CustomizeException e) {
                                        LOGGER.error("插入数据异常", e);
                                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                        throw new CustomizeException(ExceptionEnum.UNKNOWN_ERROR);
                                    }

                                }
                            }
                        }
                    }
                }
            }

            return null;
        }

    public  Map<String, String> calcMap(String destinations) {
        Map<String, String> params = new HashMap<String, String>(16);
        // 起始点
        String originDouble = HttpClientUtil
                .doGet(geocoding+originAdd+"&output=json&ak="+key);
        // 终点
        String desDouble = HttpClientUtil
                .doGet(geocoding+destinations+"&output=json&ak="+key);
        JSONObject jsonObjectOri = JSONObject.parseObject(originDouble);
        JSONObject jsonObjectDes = JSONObject.parseObject(desDouble);
        String oriLng = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lng");// 经度值ֵ
        String oriLat = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lat");// 纬度值ֵ

        String desLng = jsonObjectDes.getJSONObject("result").getJSONObject("location").getString("lng");
        String desLat = jsonObjectDes.getJSONObject("result").getJSONObject("location").getString("lat");
        params.put("output", "json");//输出方式为json
      //  params.put("tactics", "11");//10不走高速11常规路线12 距离较短（考虑路况）13距离较短（不考虑路况）
        params.put("ak", key);
        // origins 起点 destinations 目的地
        String originsLaLn = "?origins="+oriLat + "," + oriLng;
        String destinationsLaLn = "&destinations="+oriLat + "," + oriLng;
        params.put("origins", oriLat + "," + oriLng + "|" + oriLat + "," + oriLng);
        params.put("destinations", desLat + "," + desLng + "|" + desLat + "," + desLng);

        String result = HttpClientUtil.doGet(routematrix, params);

        JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("result");
        //获取json长度
        int  JsonLen = 0;
        for (Object object : jsonArray) {
            JsonLen++;
        }
        Map<String,String> map = null;
        int i;
        for (i = 0; i < JsonLen; i++) {
            map = new HashMap<String,String>();
            map.put("mTime",jsonArray.getJSONObject(i).getJSONObject("duration").getString("text"));
            map.put("mZlc",jsonArray.getJSONObject(i).getJSONObject("distance").getString("text"));
            map.put("mZzlc",jsonArray.getJSONObject(i).getJSONObject("distance").getString("value"));
        }
        return map;
    }

        @Override
        public PageInfo<TaskInfoDO> page(Map<String, Object> map) {
            return null;
        }

        @Override
        public Response deleteTask(Integer id) {
            return null;
        }

    @Override
    public TaskInfoDTO listTask(Integer pageSize, Integer pageNo) {
        PageHelper.startPage(pageNo, pageSize);
        List<TaskInfoDO> list = taskInfoMapper.listTask();
        PageInfo<TaskInfoDO> taskInfoDOPageInfo = new PageInfo<>(list);
        return (new TaskInfoDTO(taskInfoDOPageInfo.getList(),taskInfoDOPageInfo.getTotal()));
    }


    /**
     * 获取下载模版
     */
    @Override
    public void exportExcel(String taskId, HttpServletResponse response) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        exportExcel(workbook);
        response.setHeader("Content-type","application/vnd.ms-excel");

        // 解决导出文件名中文乱码
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+new String("路程距离_".getBytes("UTF-8"),"ISO-8859-1")+".xls");
        workbook.write(response.getOutputStream());
    }

    //导入为模版
    private void exportExcel(HSSFWorkbook workbook) throws Exception {
        //创建创建sheet
        HSSFSheet sheet = workbook.createSheet("路程距离");

        //创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        //设置首行标题标题
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellStyle(cellStyle);
        headerRow.createCell(0).setCellValue("省份");
        headerRow.createCell(1).setCellStyle(cellStyle);
        headerRow.createCell(1).setCellValue("地级市");
        headerRow.createCell(2).setCellStyle(cellStyle);
        headerRow.createCell(2).setCellValue("县城(区县)");
        headerRow.createCell(3).setCellStyle(cellStyle);
        headerRow.createCell(3).setCellValue("城市(具体地址)");
        headerRow.createCell(4).setCellStyle(cellStyle);
        headerRow.createCell(4).setCellValue("公里数");
        headerRow.createCell(5).setCellStyle(cellStyle);
        headerRow.createCell(5).setCellValue("送货时间(天)");
        headerRow.createCell(6).setCellStyle(cellStyle);
        headerRow.createCell(6).setCellValue("0-5吨");
        headerRow.createCell(7).setCellStyle(cellStyle);
        headerRow.createCell(7).setCellValue("5-10吨");
        headerRow.createCell(8).setCellStyle(cellStyle);
        headerRow.createCell(8).setCellValue("10-20吨");
        headerRow.createCell(9).setCellStyle(cellStyle);
        headerRow.createCell(9).setCellValue("20吨以上 ");


        //创建三行数据
        HSSFRow row;
        for (int i = 0; i <4; i++) {
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellStyle(cellStyle);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellStyle(cellStyle);
            row.createCell(1).setCellValue("张三");
            row.createCell(2).setCellStyle(cellStyle);
            row.createCell(2).setCellValue(19);
        }
    }

}
