package com.zzy.pony.exam.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.service.EntranceAverageService;
import com.zzy.pony.exam.service.EntranceClassAverageService;
import com.zzy.pony.exam.service.EntranceClassAverageServiceImpl;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2018/5/6 0006.
 */
@Controller
@RequestMapping(value = "/examAdmin/entranceClassAverage")
public class EntranceClassAverageController {

    @Autowired
    private EntranceClassAverageService entranceClassAverageService;

    @Value("${entranceClassAveragePath}")
    private String entranceClassAveragePath;


    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        return "examAdmin/entranceClassAverage/main";
    }


    @RequestMapping(value = "exportTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportAssignTemplate(Model model) {
        String fileName = "入学分班成绩模板.xlsx";
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

        File localFile = new File(entranceClassAveragePath, name);
        String fileName = "入学分班均量值.xls";
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
            for (int i = 3;i<titles.length;i++){
                subjects.add(titles[i]);
            }
            List<EntranceExcelVo> vos = new ArrayList<EntranceExcelVo>();
            try {
                vos  = entranceClassAverageService.getEntranceExcelVo(wb);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            List<String> className = entranceClassAverageService.getClassName(vos);

            Map<String, List<BigDecimal>> levelMap = entranceClassAverageService.getLevelByExcel(wb);

            for (String subject:
            subjects) {

                entranceClassAverageService.sort(vos,subject);
                entranceClassAverageService.calculate(vos,subject,levelMap.get(subject));
            }
            //modify 不计算总分
            //计算总分情况,应放在进行单科计算之后
            /*entranceClassAverageService.sort(vos,"total");
            entranceClassAverageService.calculate(vos,"total",levelMap.get("total"));*/


            HSSFWorkbook workbook = new HSSFWorkbook();
            //sheet1
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("班级");
            titleRow.createCell(1).setCellValue("姓名");
            titleRow.createCell(2).setCellValue("学号");
            int index = 3;
            for (String subject:
            subjects) {
                titleRow.createCell(index).setCellValue(subject);
                index ++;
            }
            /*titleRow.createCell(index).setCellValue("总分");

            Collections.sort(vos, new Comparator<EntranceExcelVo>() {
                @Override
                public int compare(EntranceExcelVo o1, EntranceExcelVo o2) {
                    if (o1.getClassName().equals(o2.getClassName())){
                        return o1.getLevelMap().get("total").compareTo(o2.getLevelMap().get("total"));
                    }
                    return o1.getClassName().compareTo(o2.getClassName());
                }
            });*/

            HSSFRow dataRow = null;
            int idx = 1;
            for (EntranceExcelVo vo:
                 vos) {
                dataRow = sheet.createRow(idx);
                dataRow.createCell(0).setCellValue(vo.getClassName()==null?"":vo.getClassName().toString());
                dataRow.createCell(1).setCellValue(vo.getStudentName().toString());
                dataRow.createCell(2).setCellValue(vo.getStudentNo()==null?"":vo.getStudentNo().toString());
                index = 3;
                for (String subject:
                     subjects) {
                    dataRow.createCell(index).setCellValue(vo.getLevelMap().get(subject));
                    index++;
                }
            //    dataRow.createCell(index).setCellValue(vo.getLevelMap().get("total"));
                idx++;
            }
            if (workbook != null) {
                String titleName = "入学分班均量值.xls";
                File localFile = new File(entranceClassAveragePath, titleName);
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
