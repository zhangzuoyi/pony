package com.zzy.pony.exam.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.service.AverageService;
import com.zzy.pony.exam.service.EntranceAverageService;
import com.zzy.pony.exam.vo.EntranceExcelVo;
import com.zzy.pony.util.ReadExcelUtils;
import com.zzy.pony.util.TemplateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/6 0006.
 */
@Controller
@RequestMapping(value = "/examAdmin/entranceAverage")
public class EntranceAverageController {

    @Autowired
    private EntranceAverageService entranceAverageService;
    @Value("${entranceAveragePath}")
    private String entranceAveragePath;


    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        return "examAdmin/entranceAverage/main";
    }


    @RequestMapping(value = "exportTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportAssignTemplate(Model model) {
        String fileName = "入学成绩模板.xlsx";
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "exportAverageFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportNewAverageFile(@RequestParam(value="name") String name, HttpServletRequest request, HttpServletResponse response) {

        File localFile = new File(entranceAveragePath, name);
        String fileName = "入学均量值.xls";
        HttpHeaders headers = new HttpHeaders();
        byte[] content = new byte[0];
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            content = IOUtils.toByteArray(new FileInputStream(localFile));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);

    }

    @RequestMapping(value = "exportAverage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> exportNewAverage(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            Workbook wb = ReadExcelUtils.ReadExcelByFile(file);
            String[] titles = ReadExcelUtils.readExcelTitle(wb);
            List<String> subjects = new ArrayList<String>();
            for (int i = 4;i<titles.length;i++){
                subjects.add(titles[i]);
            }
            List<EntranceExcelVo> vos = new ArrayList<EntranceExcelVo>();
            try {
                vos  = entranceAverageService.getEntranceExcelVo(wb);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            List<String> schoolNames = entranceAverageService.getSchoolName(vos);
            List<Map<String,Map<String,BigDecimal>>> list = new ArrayList<Map<String, Map<String, BigDecimal>>>();
            Map<String,List<Map<String,Map<String,BigDecimal>>>> dataMap = new HashMap<String, List<Map<String, Map<String, BigDecimal>>>>();
            List<Map<String,Map<String,BigDecimal>>> schoolList = new ArrayList<Map<String, Map<String, BigDecimal>>>();
            Map<String,List<Map<String,Map<String,BigDecimal>>>> schoolDataMap = new HashMap<String, List<Map<String, Map<String, BigDecimal>>>>();

            for (String subject:
            subjects) {
                entranceAverageService.sort(vos,subject);
                list =  entranceAverageService.calculate(vos,subject);
                schoolList = entranceAverageService.calculateSchool(vos,subject, Constants.SCHOOL_NAME);
                dataMap.put(subject,list);
                schoolDataMap.put(subject,schoolList);
            }
            //计算总分情况,应放在进行单科计算之后
            List<Map<String,Map<String,BigDecimal>>> totalList = new ArrayList<Map<String, Map<String, BigDecimal>>>();
            entranceAverageService.sort(vos,"total");
            totalList =  entranceAverageService.calculate(vos,"total");

            List<Map<String,Map<String,BigDecimal>>> schoolTotalList = new ArrayList<Map<String, Map<String, BigDecimal>>>();
            //entranceAverageService.sort(vos,"total");
            schoolTotalList =  entranceAverageService.calculateSchool(vos,"total",Constants.SCHOOL_NAME);


            HSSFWorkbook workbook = new HSSFWorkbook();
            //sheet1
            HSSFSheet sheet = workbook.createSheet();
            int index = 0;
            for (String subject:
            subjects) {
                List<Map<String,Map<String,BigDecimal>>> dataList = dataMap.get(subject);
                HSSFRow titleRow = sheet.createRow(index);
                HSSFRow titleRow2 = sheet.createRow(index+1);
                titleRow.createCell(0).setCellValue(subject);
                titleRow2.createCell(0).setCellValue("段名");
                titleRow2.createCell(1).setCellValue("分段%");
                int i = 0;
                for (String schoolName:
                     schoolNames) {
                    titleRow.createCell(2*i+2).setCellValue(schoolName);
                    titleRow2.createCell(2*i+2).setCellValue("档数");
                    titleRow2.createCell(2*i+3).setCellValue("累数");
                    i++;
                }
                titleRow.createCell(2*i+2).setCellValue("全部");
                titleRow2.createCell(2*i+2).setCellValue("档数");
                titleRow2.createCell(2*i+3).setCellValue("累数");
                HSSFRow  dataRow = null;
                for(int j = 1;j<=5;j++){
                    dataRow = sheet.createRow(index+j+1);

                    switch (j){
                        case 1 :
                            dataRow.createCell(0).setCellValue("A");
                            dataRow.createCell(1).setCellValue("(0,15]"); break;
                        case 2 :
                            dataRow.createCell(0).setCellValue("B");
                            dataRow.createCell(1).setCellValue("(15,45]"); break;
                        case 3 :
                            dataRow.createCell(0).setCellValue("C");
                            dataRow.createCell(1).setCellValue("(45,75]"); break;
                        case 4 :
                            dataRow.createCell(0).setCellValue("D");
                            dataRow.createCell(1).setCellValue("(75,95]"); break;
                        case 5 :
                            dataRow.createCell(0).setCellValue("E");
                            dataRow.createCell(1).setCellValue("(95,100]"); break;
                        default: ;
                    }
                    int idx = 2;
                    for (String schoolName:
                            schoolNames) {
                        if(dataList.get(j-1).get("current").get(schoolName) != null){
                            dataRow.createCell(idx).setCellValue(dataList.get(j-1).get("current").get(schoolName).toString());
                            dataRow.createCell(idx+1).setCellValue(dataList.get(j-1).get("current").get(schoolName+"Sum").toString());
                        }else{
                            dataRow.createCell(idx).setCellValue("0");
                            dataRow.createCell(idx+1).setCellValue("0");
                        }
                        idx+=2;
                    }
                    dataRow.createCell(idx).setCellValue(dataList.get(j-1).get("current").get("acc").toString());
                    dataRow.createCell(idx+1).setCellValue(dataList.get(j-1).get("current").get("accSum").toString());
                }
                index += 8;
            }
            //总分
            HSSFRow titleRow = sheet.createRow(index);
            HSSFRow titleRow2 = sheet.createRow(index+1);
            titleRow.createCell(0).setCellValue("总分");
            titleRow2.createCell(0).setCellValue("段名");
            titleRow2.createCell(1).setCellValue("分段%");
            int i = 0;
            for (String schoolName:
                    schoolNames) {
                titleRow.createCell(2*i+2).setCellValue(schoolName);
                titleRow2.createCell(2*i+2).setCellValue("档数");
                titleRow2.createCell(2*i+3).setCellValue("累数");
                i++;
            }
            titleRow.createCell(2*i+2).setCellValue("全部");
            titleRow2.createCell(2*i+2).setCellValue("档数");
            titleRow2.createCell(2*i+3).setCellValue("累数");
            HSSFRow  dataRow = null;
            for(int j = 1;j<=5;j++){
                dataRow = sheet.createRow(index+j+1);
                switch (j){
                    case 1 :
                        dataRow.createCell(0).setCellValue("A");
                        dataRow.createCell(1).setCellValue("(0,15]"); break;
                    case 2 :
                        dataRow.createCell(0).setCellValue("B");
                        dataRow.createCell(1).setCellValue("(15,45]"); break;
                    case 3 :
                        dataRow.createCell(0).setCellValue("C");
                        dataRow.createCell(1).setCellValue("(45,75]"); break;
                    case 4 :
                        dataRow.createCell(0).setCellValue("D");
                        dataRow.createCell(1).setCellValue("(75,95]"); break;
                    case 5 :
                        dataRow.createCell(0).setCellValue("E");
                        dataRow.createCell(1).setCellValue("(95,100]"); break;
                    default: ;
                }
                int idx = 2;
                for (String schoolName:
                        schoolNames) {
                    if(totalList.get(j-1).get("current").get(schoolName) != null){
                        dataRow.createCell(idx).setCellValue(totalList.get(j-1).get("current").get(schoolName).toString());
                        dataRow.createCell(idx+1).setCellValue(totalList.get(j-1).get("current").get(schoolName+"Sum").toString());
                    }else{
                        dataRow.createCell(idx).setCellValue("0");
                        dataRow.createCell(idx+1).setCellValue("0");
                    }
                    idx+=2;
                }
                dataRow.createCell(idx).setCellValue(totalList.get(j-1).get("current").get("acc").toString());
                dataRow.createCell(idx+1).setCellValue(totalList.get(j-1).get("current").get("accSum").toString());
            }

            //sheet2 校内均量值
            //sheet1
            List<String> classNames = entranceAverageService.getClassName(vos, Constants.SCHOOL_NAME);
            HSSFSheet sheet2 = workbook.createSheet();
            int index2 = 0;
            for (String subject:
                    subjects) {
                List<Map<String,Map<String,BigDecimal>>> dataList = schoolDataMap.get(subject);
                HSSFRow titleClassRow = sheet2.createRow(index2);
                HSSFRow titleClassRow2 = sheet2.createRow(index2+1);
                titleClassRow.createCell(0).setCellValue(subject);
                titleClassRow2.createCell(0).setCellValue("段名");
                titleClassRow2.createCell(1).setCellValue("分段%");
                int m = 0;
                for (String className:
                        classNames) {
                    titleClassRow.createCell(2*m+2).setCellValue(className);
                    titleClassRow2.createCell(2*m+2).setCellValue("档数");
                    titleClassRow2.createCell(2*m+3).setCellValue("累数");
                    m++;
                }
                titleClassRow.createCell(2*m+2).setCellValue("全部");
                titleClassRow2.createCell(2*m+2).setCellValue("档数");
                titleClassRow2.createCell(2*m+3).setCellValue("累数");
                HSSFRow  dataClassRow = null;
                for(int j = 1;j<=5;j++){
                    dataClassRow = sheet2.createRow(index2+j+1);

                    switch (j){
                        case 1 :
                            dataClassRow.createCell(0).setCellValue("A");
                            dataClassRow.createCell(1).setCellValue("(0,15]"); break;
                        case 2 :
                            dataClassRow.createCell(0).setCellValue("B");
                            dataClassRow.createCell(1).setCellValue("(15,45]"); break;
                        case 3 :
                            dataClassRow.createCell(0).setCellValue("C");
                            dataClassRow.createCell(1).setCellValue("(45,75]"); break;
                        case 4 :
                            dataClassRow.createCell(0).setCellValue("D");
                            dataClassRow.createCell(1).setCellValue("(75,95]"); break;
                        case 5 :
                            dataClassRow.createCell(0).setCellValue("E");
                            dataClassRow.createCell(1).setCellValue("(95,100]"); break;
                        default: ;
                    }
                    int idx = 2;
                    for (String className:
                            classNames) {
                        if(dataList.get(j-1).get("current").get(className) != null){
                            dataClassRow.createCell(idx).setCellValue(dataList.get(j-1).get("current").get(className).toString());
                            dataClassRow.createCell(idx+1).setCellValue(dataList.get(j-1).get("current").get(className+"Sum").toString());
                        }else{
                            dataClassRow.createCell(idx).setCellValue("0");
                            dataClassRow.createCell(idx+1).setCellValue("0");
                        }
                        idx+=2;
                    }
                    dataClassRow.createCell(idx).setCellValue(dataList.get(j-1).get("current").get("acc").toString());
                    dataClassRow.createCell(idx+1).setCellValue(dataList.get(j-1).get("current").get("accSum").toString());
                }
                index2 += 8;
            }
            //总分
            HSSFRow titleClassRow = sheet2.createRow(index2);
            HSSFRow titleClassRow2 = sheet2.createRow(index2+1);
            titleClassRow.createCell(0).setCellValue("总分");
            titleClassRow2.createCell(0).setCellValue("段名");
            titleClassRow2.createCell(1).setCellValue("分段%");
            int n = 0;
            for (String className:
                    classNames) {
                titleClassRow.createCell(2*n+2).setCellValue(className);
                titleClassRow2.createCell(2*n+2).setCellValue("档数");
                titleClassRow2.createCell(2*n+3).setCellValue("累数");
                n++;
            }
            titleClassRow.createCell(2*n+2).setCellValue("全部");
            titleClassRow2.createCell(2*n+2).setCellValue("档数");
            titleClassRow2.createCell(2*n+3).setCellValue("累数");
            HSSFRow  dataClassRow = null;
            for(int j = 1;j<=5;j++){
                dataClassRow = sheet2.createRow(index+j+1);
                switch (j){
                    case 1 :
                        dataClassRow.createCell(0).setCellValue("A");
                        dataClassRow.createCell(1).setCellValue("(0,15]"); break;
                    case 2 :
                        dataClassRow.createCell(0).setCellValue("B");
                        dataClassRow.createCell(1).setCellValue("(15,45]"); break;
                    case 3 :
                        dataClassRow.createCell(0).setCellValue("C");
                        dataClassRow.createCell(1).setCellValue("(45,75]"); break;
                    case 4 :
                        dataClassRow.createCell(0).setCellValue("D");
                        dataClassRow.createCell(1).setCellValue("(75,95]"); break;
                    case 5 :
                        dataClassRow.createCell(0).setCellValue("E");
                        dataClassRow.createCell(1).setCellValue("(95,100]"); break;
                    default: ;
                }
                int idx = 2;
                for (String className:
                        classNames) {
                    if(schoolTotalList.get(j-1).get("current").get(className) != null){
                        dataClassRow.createCell(idx).setCellValue(schoolTotalList.get(j-1).get("current").get(className).toString());
                        dataClassRow.createCell(idx+1).setCellValue(schoolTotalList.get(j-1).get("current").get(className+"Sum").toString());
                    }else{
                        dataClassRow.createCell(idx).setCellValue("0");
                        dataClassRow.createCell(idx+1).setCellValue("0");
                    }
                    idx+=2;
                }
                dataClassRow.createCell(idx).setCellValue(schoolTotalList.get(j-1).get("current").get("acc").toString());
                dataClassRow.createCell(idx+1).setCellValue(schoolTotalList.get(j-1).get("current").get("accSum").toString());
            }


            if (workbook != null) {
                String titleName = "入学均量值.xls";
                File localFile = new File(entranceAveragePath, titleName);
                FileOutputStream outputStream = new FileOutputStream(localFile);
                workbook.write(outputStream);
                outputStream.close();
                result.put("name", titleName);
            }

        } catch (OldExcelFormatException e) {
            e.printStackTrace();
            result.put("error", "版本必须为2003及以上");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
