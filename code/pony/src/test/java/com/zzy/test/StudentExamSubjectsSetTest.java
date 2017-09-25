package com.zzy.test;

import java.io.FileInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.mapper.StudentMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml","/shiroFilter.xml"})
public class StudentExamSubjectsSetTest {
	@Autowired
	private StudentMapper mapper;
	
	@Test
	public void testSet() throws Exception{
		Workbook wb=WorkbookFactory.create(new FileInputStream("D:\\教育软件\\平桥中学\\2015入学高三学生信息 - 副本.xls"));
		Sheet sheet=wb.getSheetAt(0);
		String[] subjects=new String[8];
		Row titleRow=sheet.getRow(0);
		for(int i=0;i<subjects.length;i++){
			subjects[i]=titleRow.getCell(i+3).getStringCellValue();
		}
		int i=1;
		while(true){
			Row row=sheet.getRow(i);
			if(row == null){
				break;
			}
			Cell cell=row.getCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			String studentNo=cell.getStringCellValue();
			System.out.println(studentNo);
			if(StringUtils.isBlank(studentNo)){
				break;
			}
			StringBuilder sb=new StringBuilder();
			for(int k=3;k<3+subjects.length;k++){
				String selected=row.getCell(k)==null ? null : row.getCell(k).getStringCellValue();
				if("选考".equals(selected)){
					sb.append(getSubject(subjects[k-3]));
				}
			}
			mapper.setExamSubjectsByStudentNo(sb.toString(), studentNo);;
			i++;
		}
	}
	private String getSubject(String subject){
		if("语数英".equals(subject)){
			return "语文,数学,英语";
		}else{
			return ","+subject;
		}
	}
}
