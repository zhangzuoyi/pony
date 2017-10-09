package com.zzy.pony.exam.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.service.AverageService;
import com.zzy.pony.exam.vo.AverageIndexRowVo;
import com.zzy.pony.util.TemplateUtil;

@Controller
@RequestMapping(value="/examAdmin/average")
public class AverageController {
	@Autowired
	private AverageService service;
	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/average/main";
	}
	@RequestMapping(value="getIndexRows",method=RequestMethod.GET)
	@ResponseBody
	public List<AverageIndexRowVo> getIndexRows(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){
		return service.findIndexRowVo(examId, gradeId);
	}
	@RequestMapping(value="submitIndexList",method=RequestMethod.POST)
	@ResponseBody
	public void submitIndexList(@RequestBody List<AverageIndexRowVo> indexList){
		List<AverageIndex> list=new ArrayList<AverageIndex>();
		for(AverageIndexRowVo vo: indexList) {
			list.addAll(vo.getIndexList());
		}
		service.saveIndexList(indexList.get(0).getExamId(), indexList.get(0).getGradeId(), list);
	}
	@RequestMapping(value="uploadIndexList",method = RequestMethod.POST)
	@ResponseBody
	public String uploadIndexList(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="file") MultipartFile file, Model model){
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			Row titleRow=sheet.getRow(0);
			List<String> subjectList=new ArrayList<String>();
			int ti=1;
			while(true) {//取得科目名
				Cell cell=titleRow.getCell(ti);
				if(cell != null && StringUtils.isNoneBlank(cell.getStringCellValue())) {
					subjectList.add(cell.getStringCellValue().trim());
					ti++;
				}else {
					break;
				}
			}
			List<List<Float>> list=new ArrayList<List<Float>>();//按分段存放指标
			int subjectsLen=subjectList.size();
			int i=1;
			while(true){
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String section=cell.getStringCellValue();
				if(StringUtils.isBlank(section)){
					break;
				}
				List<Float> values=new ArrayList<Float>();
				for(int j=0;j<subjectsLen;j++) {
					float value = 0f;
					if (row.getCell(j+1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						 value=(float)row.getCell(j+1).getNumericCellValue();

					}else {
						 value=Float.valueOf(row.getCell(j+1).getStringCellValue());
					}
					//row.getCell(j+1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					values.add(value);
				}
				list.add(values);
				i++;
				if(i>AverageService.SECTION_COUNT) {
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
	@RequestMapping(value="exportTemplate",method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportTemplate(Model model){
		String fileName="均量值导入模板.xlsx";
		HttpHeaders headers = new HttpHeaders(); 
		try {
			headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
	}
}
