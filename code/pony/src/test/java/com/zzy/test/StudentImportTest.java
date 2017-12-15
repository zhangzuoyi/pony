package com.zzy.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.CommonDict;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.util.DateTimeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml","/shiroFilter.xml"})
public class StudentImportTest {
	@Autowired
	private StudentService service;
	@Autowired
	private DictService dictService;
	@Autowired
	private SchoolClassService scService;
	
	@Test
	public void testImport() throws Exception{
		int grade=3;//年级
		Date entryDate=DateTimeUtil.strToDate("2015-09-01", "yyyy-MM-dd");//入学日期
		String path="D:\\教育软件\\平桥中学\\学生数据\\2015入学高三名单信息.xlsx";
		List<Student> list=new ArrayList<Student>();
		List<CommonDict> sexList=dictService.findSexes();
		List<SchoolClass> scList=scService.findByGrade(grade);
		Map<String,String> sexMap=new HashMap<String,String>();
		for(CommonDict cd: sexList){
			sexMap.put(cd.getValue(), cd.getCode());//从名称到编码
		}
		try {
			InputStream input=new FileInputStream(path);
			Workbook wb=WorkbookFactory.create(input);
			Sheet sheet=wb.getSheetAt(0);
			int i=1;
			while(true){
				Student stu=new Student();
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String studentNo=cell.getStringCellValue();
				if(StringUtils.isBlank(studentNo)){
					break;
				}
				String classStr=cellValue(row.getCell(1));
				String name=cellValue(row.getCell(2));
//				String sex=cellValue(row.getCell(3));
				
				stu.setEntranceDate(entryDate);
				stu.setEntranceType(StudentService.STUDENT_TYPE_TZ);//默认统招
				stu.setIdType(Constants.ID_TYPE_DEFAULT);//默认身份证
				stu.setName(name);
				stu.setSchoolClass(getClass(classStr, scList));
//				stu.setSex(sexMap.get(sex));
				stu.setSex(sexMap.get(getGenderByIdCard(studentNo.substring(1))));//未提供性别
				stu.setStudentNo(studentNo);
				stu.setStatus(StudentService.STUDENT_STATUS_ZD);
				System.out.println(name);
				list.add(stu);
				i++;
			}
			System.out.println(list.size());
			service.upload(list, "zhangzuoyi");
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String cellValue(Cell cell){
		if(cell != null){
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		}
		return null;
	}
	private SchoolClass getClass(String classStr, List<SchoolClass> scList){
		int num=Integer.valueOf(classStr);
		for(SchoolClass sc: scList){
			if(sc.getSeq()== num){
				return sc;
			}
		}
		System.out.println("班级为空");
		return null;
	}
	/**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "未知";

        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "男";//男
        } else {
            sGender = "女";//女
        }
        return sGender;
    }
}
