package com.zzy.pony.exam.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.mapper.AverageIndexMapper;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.service.AverageService;
import com.zzy.pony.exam.vo.*;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.*;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ReadExcelUtils;
import com.zzy.pony.util.TemplateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(value = "/examAdmin/average")
public class AverageController {
    @Autowired
    private AverageService service;
    @Autowired
    private ExamService examService;
    @Autowired
    private SchoolClassService schoolClassService;
    @Autowired
    private SchoolYearService yearService;
    @Autowired
    private AverageIndexMapper averageIndexMapper;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private GradeService gradeService;
    @Value("${averagePath}")
    private String averagePath;
    @Value("${averageAssignPath}")
    private String averageAssignPath;

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        return "examAdmin/averageNew/main";
    }

    @RequestMapping(value = "main2", method = RequestMethod.GET)
    public String main2(Model model) {
        return "examAdmin/averageByFile/main";
    }

    @RequestMapping(value = "mainAssign", method = RequestMethod.GET)
    public String mainAssign(Model model) {
        return "examAdmin/averageAssignByFile/main";
    }

    @RequestMapping(value = "getIndexRows", method = RequestMethod.GET)
    @ResponseBody
    public List<AverageIndexRowVo> getIndexRows(@RequestParam(value = "examId") int examId,
                                                @RequestParam(value = "gradeId") int gradeId) {
        return service.findIndexRowVo(examId, gradeId);
    }

    @RequestMapping(value = "submitIndexList", method = RequestMethod.POST)
    @ResponseBody
    public void submitIndexList(@RequestBody List<AverageIndexRowVo> indexList) {
        List<AverageIndex> list = new ArrayList<AverageIndex>();
        for (AverageIndexRowVo vo : indexList) {
            list.addAll(vo.getIndexList());
        }
        service.saveIndexList(indexList.get(0).getExamId(), indexList.get(0).getGradeId(), list);
    }

    @RequestMapping(value = "uploadIndexList", method = RequestMethod.POST)
    @ResponseBody
    public String uploadIndexList(@RequestParam(value = "examId") int examId,
                                  @RequestParam(value = "gradeId") int gradeId, @RequestParam(value = "file") MultipartFile file,
                                  Model model) {
        try {
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            Row titleRow = sheet.getRow(0);
            List<String> subjectList = new ArrayList<String>();
            int ti = 1;
            while (true) {// 取得科目名
                Cell cell = titleRow.getCell(ti);
                if (cell != null && StringUtils.isNoneBlank(cell.getStringCellValue())) {
                    subjectList.add(cell.getStringCellValue().trim());
                    ti++;
                } else {
                    break;
                }
            }
            List<List<Float>> list = new ArrayList<List<Float>>();// 按分段存放指标
            int subjectsLen = subjectList.size();
            int i = 1;
            while (true) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    break;
                }
                Cell cell = row.getCell(0);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String section = cell.getStringCellValue();
                if (StringUtils.isBlank(section)) {
                    break;
                }
                List<Float> values = new ArrayList<Float>();
                for (int j = 0; j < subjectsLen; j++) {
                    float value = 0f;
                    if (row.getCell(j + 1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        value = (float) row.getCell(j + 1).getNumericCellValue();

                    } else {
                        value = Float.valueOf(row.getCell(j + 1).getStringCellValue());
                    }
                    // row.getCell(j+1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    values.add(value);
                }
                list.add(values);
                i++;
                if (i > AverageService.SECTION_COUNT) {
                    break;
                }
            }
            service.uploadIndexList(examId, gradeId, subjectList, list);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping(value = "exportAssignTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportAssignTemplate(Model model) {
        String fileName = "7选3赋分模板.xlsx";
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "exportTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportTemplate(Model model) {
        String fileName = "均量值导入模板.xlsx";
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
    @RequestMapping(value = "exportAverage", method = RequestMethod.GET)
    public void exportAverage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int examId = Integer.valueOf(request.getParameter("examId"));
        int gradeId = Integer.valueOf(request.getParameter("gradeId"));
        Exam exam = examService.get(examId);
        String title = exam.getName() + "均量值统计";
        SchoolYear year = yearService.getCurrent();
        List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);
        Map<Integer, Map<String, Map<String, BigDecimal>>> dataMap = service.calculateAverage(examId, gradeId);

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
            List<Map<String, Object>> headList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

            int range = 0;
            for (Integer subjectId : dataMap.keySet()) {
                Map<String, Map<String, BigDecimal>> innerMap = dataMap.get(subjectId);
                Subject subject = subjectService.get(subjectId);
                List<AverageIndexVo> averageIndexVos = averageIndexMapper.findByExamAndGradeAndSubject(examId, gradeId,
                        subject.getSubjectId());
                // 产生表格标题行
                HSSFRow titleRow = sheet.createRow(range);
                HSSFCell titleCell = titleRow.createCell(0);
                sheet.addMergedRegion(new Region(range, (short) 0, range, (short) (1)));
                titleCell.setCellValue(subject.getName());
                HSSFRow headRow = sheet.createRow(range + 1);
                headRow.createCell(0).setCellValue("段名");
                headRow.createCell(1).setCellValue("各档指标");
                int colNums = 2;
                int classSize = 1;
                for (SchoolClass schoolClass : schoolClasses) {
                    HSSFCell classSeqCell = titleRow.createCell(classSize * 2);
                    classSeqCell.setCellValue(schoolClass.getSeq());
                    headRow.createCell(classSize * 2).setCellValue("档数");
                    headRow.createCell(classSize * 2 + 1).setCellValue("累数");
                    classSize++;
                    colNums += 2;
                }
                titleRow.createCell(colNums).setCellValue("全部");
                headRow.createCell(colNums).setCellValue("档数");
                headRow.createCell(colNums + 1).setCellValue("累数");
                int index = 0;
                for (String section : innerMap.keySet()) {
                    HSSFRow dataRow = sheet.createRow(range + index + 2);
                    dataRow.createCell(0).setCellValue(section);
                    dataRow.createCell(1).setCellValue(String.valueOf(averageIndexVos.get(index).getIndexValue()));
                    for (int j = 1; j <= schoolClasses.size(); j++) {
                        dataRow.createCell(j * 2).setCellValue(innerMap.get(section).get("level" + j).toString());
                        dataRow.createCell(j * 2 + 1)
                                .setCellValue(innerMap.get(section).get("levelSum" + j).toString());
                    }
                    dataRow.createCell(schoolClasses.size() * 2 + 2)
                            .setCellValue(innerMap.get(section).get("allLevel").toString());
                    dataRow.createCell(schoolClasses.size() * 2 + 3)
                            .setCellValue(innerMap.get(section).get("allLevelSum").toString());
                    index++;
                }
                range += 25;
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < 30; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null && !"".equals(currentRow.getCell(colNum))) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }

            if (workbook != null) {
                try {
                    String fileName = new String(title.getBytes("utf-8"), "ISO8859-1")
                            + DateTimeUtil.dateToStr(new Date()) + ".xls";
                    // String fileName = "Excel-" +
                    // String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    // response = getResponse();
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "exportNewAllTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportNewAllTemplate(Model model) {
        String fileName = "均量值(全县)模板.xlsx";
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
    @RequestMapping(value = "exportResultFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportResultFile(@RequestParam(value = "name") String name, HttpServletRequest request, HttpServletResponse response) {
        File localFile = new File(averagePath, name);
        String fileName = "均量值(全县).xls";
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


    @SuppressWarnings("deprecation")
    @RequestMapping(value = "exportResult", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> exportResult(MultipartFile fileUpload, HttpServletRequest request,
                                            HttpServletResponse response
            /*,@RequestParam(value = "gradeId") int gradeId*/) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("upoadfile");
        if (file == null) {
            return null;
        }
        String title = "均量值统计";
        String title3 = "学校统计表";
        List<String> subjectNames = new ArrayList<String>();

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
            Workbook wb = ReadExcelUtils.ReadExcelByFile(file);
            String[] titles = ReadExcelUtils.readExcelTitle(wb);
            int range = 26;
            Map<String,Map<String,BigDecimal>>   pqSchool = new HashMap<String, Map<String, BigDecimal>>();
            for (int i = 3; i < titles.length; i++) {
                List<AverageExcelVo> averageExcelVos = service.getAverageExcelVo(wb, i, 0);
                Set<String> schoolNames = service.getSchoolName(averageExcelVos);
                service.sortAverageExcelVo(averageExcelVos);
                Map<Integer, List<AverageExcelVo>> levelMap = service.getLevelMap(averageExcelVos);
                Map<Integer, BigDecimal> levelMapDecimal = service.getLevelMapDecimal(averageExcelVos);
                Map<String,Map<String, BigDecimal>> schoolMap = new HashMap<String, Map<String, BigDecimal>>();
                for (String schoolName:
                     schoolNames) {
                    Map<Integer, BigDecimal> schoolLevelMapDecimal = service.getLevelMapDecimalBySchoolName(levelMap,
                            levelMapDecimal, schoolName);
                    schoolMap.put(schoolName,service.calculateSchoolMap(schoolLevelMapDecimal));
                }
                service.calculateSchoolM(schoolMap);
                pqSchool.put(titles[i],schoolMap.get(Constants.SCHOOL_NAME));
                // 产生表格标题行
                HSSFRow titleRow = sheet.createRow(range);
                HSSFCell titleCell = titleRow.createCell(0);
                sheet.addMergedRegion(new Region(range, (short) 0, range, (short) (2)));
                titleCell.setCellValue(titles[i]);
                int j = 0;
                Iterator<String> it = schoolNames.iterator();
                while (it.hasNext()){
                    titleCell = titleRow.createCell(3+j*2);
                    titleCell.setCellValue(it.next());
                    j++;
                }
                titleCell = titleRow.createCell(3+j*2);
                titleCell.setCellValue("全部");

                HSSFRow headRow = sheet.createRow(range + 1);
                headRow.createCell(0).setCellValue("段名");
                headRow.createCell(1).setCellValue("权重");
                headRow.createCell(2).setCellValue("各档指标");
                int m = 0;
                for(;m<schoolNames.size();m++){
                    titleCell = headRow.createCell(3+m*2);
                    titleCell.setCellValue("档数");
                    titleCell = headRow.createCell(4+m*2);
                    titleCell.setCellValue("累数");
                }
                titleCell = headRow.createCell(3+m*2);
                titleCell.setCellValue("档数");
                titleCell = headRow.createCell(4+m*2);
                titleCell.setCellValue("累数");

                int index = 0;
                for (int section = 1; section <= Constants.AVERAGE_LEVELS.size(); section++) {
                    HSSFRow dataRow = sheet.createRow(range + index + 2);
                    dataRow.createCell(0).setCellValue("A" + section);
                    dataRow.createCell(1).setCellValue(Constants.LEVEL_WEIGHT.get("A" + section).toString());
                    dataRow.createCell(2).setCellValue(Constants.AVERAGE_LEVELS.get(index).toString());
                    int n=0;
                    for (;n<schoolNames.size();n++){
                        dataRow.createCell(3+n * 2).setCellValue(schoolMap.get(titleRow.getCell(3+n * 2).getStringCellValue()).get("A" + section).toString());
                        dataRow.createCell(4+n * 2 )
                                .setCellValue(schoolMap.get(titleRow.getCell(3+n * 2).getStringCellValue()).get("allSum" + section).toString());
                    }
                    dataRow.createCell(schoolNames.size() * 2 + 3)
                            .setCellValue(schoolMap.get("allLevel").get("allLevel" + section).toString());
                    dataRow.createCell(schoolNames.size() * 2 + 4)
                            .setCellValue(schoolMap.get("allLevelSum").get("allLevelSum" + section).toString());
                    index++;
                }
                //
                //M值
                HSSFRow MRow = sheet.createRow(range + index + 2);
                MRow.createCell(0).setCellValue("M值");
                m  = 0;
                for (;m<schoolNames.size();m++){
                    MRow.createCell(3+m * 2).setCellValue(schoolMap.get(titleRow.getCell(3+m * 2).getStringCellValue()).get("M").toString());
                }
                MRow.createCell(schoolNames.size() * 2 +3).setCellValue(schoolMap.get("allM").get("allM").toString());
                range += 26;
            }
            //整体情况
            HSSFRow titleRow = sheet.createRow(0);
            HSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("段名");
            titleCell = titleRow.createCell(1);
            titleCell.setCellValue("权重");
            for(int i = 3;i<titles.length;i++){
                titleCell = titleRow.createCell(i-1);
                titleCell.setCellValue(titles[i]);
            }
            HSSFRow dataRow = null;
            for(int level = 1;level<=22;level++){
                dataRow = sheet.createRow(level);
                dataRow.createCell(0).setCellValue("A"+level);
                dataRow.createCell(1).setCellValue(Constants.LEVEL_WEIGHT.get("A" + level).toString());
                for(int i = 3;i<titles.length;i++){
                    dataRow.createCell(i-1).setCellValue(pqSchool.get(titles[i]).get("A"+level).toString());
                }
            }
            dataRow = sheet.createRow(23);
            dataRow.createCell(0).setCellValue("M值");
            for(int i = 3;i<titles.length;i++){
                dataRow.createCell(i-1).setCellValue(pqSchool.get(titles[i]).get("M").toString());
            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < 30; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null && !"".equals(currentRow.getCell(colNum))) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }

            if (workbook != null) {

                String titleName = System.currentTimeMillis() + ".xls";
                File localFile = new File(averagePath, titleName);
                FileOutputStream outputStream = new FileOutputStream(localFile);
                workbook.write(outputStream);
                outputStream.close();
                result.put("name", titleName);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OldExcelFormatException e) {
            e.printStackTrace();
            result.put("error", "版本必须为2003及以上");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "exportAssignResultFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportAssignResultFile(@RequestParam(value = "name") String name, HttpServletRequest request, HttpServletResponse response) {

        File localFile = new File(averageAssignPath, name);
        String fileName = "7选3赋分.xls";
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

    @RequestMapping(value = "exportAssignResult", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> exportAssignResult(MultipartFile fileUpload, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("upoadfile");
        if (file == null) {
            return null;
        }
        String title = "7选3赋分";
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

            Workbook wb = ReadExcelUtils.ReadExcelByFile(file);
            String[] titles = ReadExcelUtils.readExcelTitle(wb);
            List<AverageAssignExcelVo> averageAssignExcelVos = service.getAverageAssignExcelVo(wb, 2,
                    Constants.SCHOOL_NAME);

            for (int i = 6; i < titles.length; i++) {
                if ("语文".equals(titles[i]) || "数学".equals(titles[i]) || "英语".equals(titles[i])) {
                    continue;
                }
                List<AverageExcelVo> averageExcelVos = service.getAverageExcelVo(wb, i, 2);
                service.sortAverageExcelVo(averageExcelVos);
                Map<Integer, List<AverageExcelVo>> levelMap = service.getLevelAssignMap(averageExcelVos);
                service.calculateAssign(levelMap);
                service.calculateAssignScore(levelMap, averageAssignExcelVos);
            }

            HSSFRow headRow = sheet.createRow(0);
            headRow.createCell(0).setCellValue("考生号");
            headRow.createCell(1).setCellValue("学号");
            headRow.createCell(2).setCellValue("学校");
            headRow.createCell(3).setCellValue("班级");
            headRow.createCell(4).setCellValue("姓名");
            headRow.createCell(5).setCellValue("性别");
            Row wbHeadRow = wb.getSheetAt(0).getRow(0);
            for (int i = 6; i < wbHeadRow.getLastCellNum(); i++) {
                if (!"总分".equals(wbHeadRow.getCell(i).getStringCellValue())) {
                    headRow.createCell(i).setCellValue(wbHeadRow.getCell(i).getStringCellValue());
                }
            }

            for (int j = 1; j <= averageAssignExcelVos.size(); j++) {
                HSSFRow row = sheet.createRow(j);
                AverageAssignExcelVo vo = averageAssignExcelVos.get(j - 1);
                row.createCell(0).setCellValue(vo.getExamineeNo());
                row.createCell(1).setCellValue(vo.getStudentNo());
                row.createCell(2).setCellValue(vo.getSchoolName());
                row.createCell(3).setCellValue(vo.getClassCode());
                row.createCell(4).setCellValue(vo.getName());
                row.createCell(5).setCellValue(vo.getSex());
                for (int m = 6; m < headRow.getLastCellNum(); m++) {
                    if ("语文".equals(headRow.getCell(m).getStringCellValue())
                            || "数学".equals(headRow.getCell(m).getStringCellValue())
                            || "英语".equals(headRow.getCell(m).getStringCellValue())) {
                        row.createCell(m).setCellValue(
                                vo.getInitScore().get(headRow.getCell(m).getStringCellValue()).toString());
                    } else {
                        if (vo.getAssignScore().get(headRow.getCell(m).getStringCellValue()) != null) {
                            row.createCell(m).setCellValue(
                                    vo.getAssignScore().get(headRow.getCell(m).getStringCellValue()).toString());
                        } else {
                            row.createCell(m).setCellValue(String.valueOf(0));
                            ;
                        }
                    }
                }

            }
            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < 30; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null && !"".equals(currentRow.getCell(colNum))) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }

            if (workbook != null) {
                String titleName = System.currentTimeMillis() + ".xls";
                File localFile = new File(averageAssignPath, titleName);
                FileOutputStream outputStream = new FileOutputStream(localFile);
                workbook.write(outputStream);
                outputStream.close();
                result.put("name", titleName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (OldExcelFormatException e) {
            e.printStackTrace();
            result.put("error", "版本必须为2003及以上");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "exportNewTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportNewTemplate(Model model) {
        String fileName = "均量值模板.xlsx";
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
    @RequestMapping(value = "exportNewAverageFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportNewAverageFile(@RequestParam(value="name") String name, HttpServletRequest request, HttpServletResponse response) {
        File localFile = new File(averagePath, name);
        String fileName = "均量值.xls";
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

    @RequestMapping(value = "exportNewAverage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> exportNewAverage(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            String title = "各科均量值详表";
            String title2 = "各科均量值简表";


            Workbook wb = ReadExcelUtils.ReadExcelByFile(file);
            String[] titles = ReadExcelUtils.readExcelTitle(wb);
            Sheet dataSheet = wb.getSheetAt(0);
            Sheet averageSheet = wb.getSheetAt(1);
            Map<String, Map<String, BigDecimal>> subjectLevelMap = new LinkedHashMap<String, Map<String, BigDecimal>>();
            Row headRowOne = averageSheet.getRow(0);
            Row dataSheetRow = null;
            List<String> levels = new ArrayList<String>();
            Map<String, Integer> levelWeight = new HashMap<String, Integer>();
            for (int i = 1; i <= averageSheet.getLastRowNum(); i++) {
                dataSheetRow = averageSheet.getRow(i);
                levels.add(dataSheetRow.getCell(0).getStringCellValue());
                levelWeight.put(dataSheetRow.getCell(0).getStringCellValue(), (int) dataSheetRow.getCell(1).getNumericCellValue());
                for (int j = 2; j < headRowOne.getLastCellNum(); j++) {
                    if (Cell.CELL_TYPE_STRING == headRowOne.getCell(j).getCellType()) {
                        if (subjectLevelMap.containsKey(headRowOne.getCell(j).getStringCellValue())) {
                            subjectLevelMap.get(headRowOne.getCell(j).getStringCellValue()).put(
                                    dataSheetRow.getCell(0).getStringCellValue(),
                                    new BigDecimal(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(j)).toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
                            innerMap.put(dataSheetRow.getCell(0).getStringCellValue(),
                                    new BigDecimal(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(j)).toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            subjectLevelMap.put(headRowOne.getCell(j).getStringCellValue(), innerMap);
                        }
                    }
                }
            }
            // 获取均量值指标
            Map<String, Map<String, BigDecimal>> map = new LinkedHashMap<String, Map<String, BigDecimal>>();
            List<String> subjects = new ArrayList<String>();
            // 第三列开始是科目
            for (int i = 2; i < titles.length; i++) {
                subjects.add(titles[i]);
            }
            Set<String> schoolClasses = new TreeSet<String>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    // TODO Auto-generated method stub
                    int length = o1.length() - o2.length();
                    if (length == 0) {
                        return o1.compareTo(o2);
                    }
                    return length;
                }
            });
            Map<String, List<AverageNewVo>> subjectResults = new HashMap<String, List<AverageNewVo>>();
            Row headSheetRow = dataSheet.getRow(0);
            for (int i = 2; i < headSheetRow.getLastCellNum(); i++) {
                List<AverageNewVo> vos = new ArrayList<AverageNewVo>();
                for (int j = 1; j <= dataSheet.getLastRowNum(); j++) {
                    AverageNewVo vo = new AverageNewVo();
                    dataSheetRow = dataSheet.getRow(j);
                    vo.setClassName(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(0)).toString());
                    vo.setStudentName(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(1)).toString());
                    vo.setRowNum(dataSheetRow.getRowNum());
                    vo.setSubjectName(headSheetRow.getCell(i).getStringCellValue());
                    if (i == 2) {
                        schoolClasses.add(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(0)).toString());
                    }
                    if (StringUtils.isNotEmpty(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(i)).toString())
                            &&Math.abs(Float.valueOf(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(i)).toString()))>0.1) {
                            vo.setSubjectScore(
                                    Float.valueOf(ReadExcelUtils.getCellFormatValue(dataSheetRow.getCell(i)).toString()));
                        // 0分或空不参与计算
                    } else {
                        continue;
                    }
                    vos.add(vo);
                }
                subjectResults.put(headSheetRow.getCell(i).getStringCellValue(), vos);
            }
            List<String> classList = new ArrayList<String>(schoolClasses);
            Map<String, Map<String, Map<String, BigDecimal>>> dataMap = service.calculateNewAverage(subjects,
                    schoolClasses, subjectLevelMap, subjectResults, levelWeight);

            try {
                HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
                HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
                HSSFSheet sheet2 = workbook.createSheet(title2); // 创建工作表
                Map<String,Map<String,BigDecimal>> simpleMap = new HashMap<String, Map<String, BigDecimal>>();
                int range = 0;
                for (String subject : dataMap.keySet()) {
                    Map<String, Map<String, BigDecimal>> innerMap = dataMap.get(subject);
                    Map<String, BigDecimal> subjectLevel = subjectLevelMap.get(subject);
                    // 产生表格标题行
                    HSSFRow titleRow = sheet.createRow(range);
                    HSSFCell titleCell = titleRow.createCell(0);
                    sheet.addMergedRegion(new Region(range, (short) 0, range, (short) (1)));
                    titleCell.setCellValue(subject);
                    HSSFRow headRow = sheet.createRow(range + 1);
                    headRow.createCell(0).setCellValue("段名");
                    headRow.createCell(1).setCellValue("各档指标");
                    int colNums = 2;
                    int classSize = 1;
                    Iterator<String> iterator = schoolClasses.iterator();
                    while (iterator.hasNext()) {
                        HSSFCell classSeqCell = titleRow.createCell(classSize * 2);
                        classSeqCell.setCellValue(iterator.next());
                        headRow.createCell(classSize * 2).setCellValue("档数");
                        headRow.createCell(classSize * 2 + 1).setCellValue("累数");
                        classSize++;
                        colNums += 2;
                    }

                    titleRow.createCell(colNums).setCellValue("全部");
                    headRow.createCell(colNums).setCellValue("档数");
                    headRow.createCell(colNums + 1).setCellValue("累数");
                    int index = 0;

                    for (int i = 1; i <= levels.size(); i++) {
                        String section = levels.get(i - 1);
                        HSSFRow dataRow = sheet.createRow(range + index + 2);
                        dataRow.createCell(0).setCellValue(section);
                        dataRow.createCell(1).setCellValue(subjectLevel.get(section).toString());
                        for (int j = 1; j <= schoolClasses.size(); j++) {
                            dataRow.createCell(j * 2).setCellValue(innerMap.get(classList.get(j - 1)).get(section).toString());
                            dataRow.createCell(j * 2 + 1)
                                    .setCellValue(innerMap.get(classList.get(j - 1)).get("classAllSum" + i).toString());
                        }
                        dataRow.createCell(schoolClasses.size() * 2 + 2)
                                .setCellValue(innerMap.get("allLevel").get("allLevel" + i).toString());
                        dataRow.createCell(schoolClasses.size() * 2 + 3)
                                .setCellValue(innerMap.get("allLevelSum").get("allLevelSum" + i).toString());
                        index++;
                    }
                    //M值
                    HSSFRow MRow = sheet.createRow(range + index + 2);
                    MRow.createCell(0).setCellValue("M值");
                    for (int j = 1; j <= schoolClasses.size(); j++) {
                        MRow.createCell(j * 2).setCellValue(innerMap.get(classList.get(j - 1)).get("M").toString());
                        if (simpleMap.containsKey(classList.get(j - 1))){
                            simpleMap.get(classList.get(j - 1)).put(subject,innerMap.get(classList.get(j - 1)).get("M"));
                        }else{
                            Map<String,BigDecimal> simpleInnerMap = new HashMap<String, BigDecimal>();
                            simpleInnerMap.put(subject,innerMap.get(classList.get(j - 1)).get("M"));
                            simpleMap.put(classList.get(j - 1),simpleInnerMap);
                        }
                    }
                    MRow.createCell(schoolClasses.size()*2+2).setCellValue(innerMap.get("allM").get("allM").toString());
                    range += 26;
                }
                //sheet2
                HSSFRow headRow = sheet2.createRow(0);
                headRow.createCell(0).setCellValue("班级");
                for(int i=1;i<=subjects.size();i++){
                    headRow.createCell(i).setCellValue(subjects.get(i-1));
                }
               for(int index = 1;index<=classList.size();index++){
                   headRow = sheet2.createRow(index);
                   headRow.createCell(0).setCellValue(classList.get(index-1));
                   for(int i=1;i<=subjects.size();i++){
                       headRow.createCell(i).setCellValue(simpleMap.get(classList.get(index-1)).get(subjects.get(i-1)).toString());
                   }
               }

                // 让列宽随着导出的列长自动适应
                for (int colNum = 0; colNum < 30; colNum++) {
                    int columnWidth = sheet.getColumnWidth(colNum) / 256;
                    for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                        HSSFRow currentRow;
                        // 当前行未被使用过
                        if (sheet.getRow(rowNum) == null) {
                            currentRow = sheet.createRow(rowNum);
                        } else {
                            currentRow = sheet.getRow(rowNum);
                        }
                        if (currentRow.getCell(colNum) != null && !"".equals(currentRow.getCell(colNum))) {
                            HSSFCell currentCell = currentRow.getCell(colNum);
                            if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                int length = currentCell.getStringCellValue().getBytes().length;
                                if (columnWidth < length) {
                                    columnWidth = length;
                                }
                            }
                        }
                    }
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
                if (workbook != null) {
                    String titleName = System.currentTimeMillis() + ".xls";
                    File localFile = new File(averagePath, titleName);
                    FileOutputStream outputStream = new FileOutputStream(localFile);
                    workbook.write(outputStream);
                    outputStream.close();
                    result.put("name", titleName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (OldExcelFormatException e) {
            e.printStackTrace();
            result.put("error", "版本必须为2003及以上");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}
