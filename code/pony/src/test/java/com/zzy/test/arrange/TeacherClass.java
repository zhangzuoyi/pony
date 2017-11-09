package com.zzy.test.arrange;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * 老师每天在每个班级的任课次数
 * @author ZHANGZUOYI499
 *
 */
public class TeacherClass {
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException{
		TeacherClass obj=new TeacherClass();
		obj.analysis();
	}
	void analysis() throws SQLException, IOException, ClassNotFoundException{
		int classSize=11;
		String targetFile="D:\\tmp\\老师班级任课分析.xls";
		List<User> hrmUsers=getHrmUsers();
		Workbook wb = new HSSFWorkbook();
		CellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_THIN);
	    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderLeft(CellStyle.BORDER_THIN);
	    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderRight(CellStyle.BORDER_THIN);
	    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		String sheetName = "结果";
		Sheet sheet = wb.createSheet(sheetName);
		Row titleRow = sheet.createRow(0);
		String[] titles=new String[]{"姓名","科目","星期"};
	    for(int i=0;i<titles.length;i++){
	    	Cell cell = titleRow.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(style);
	    }
	    for(int i=0;i<classSize;i++){
	    	Cell cell = titleRow.createCell(i+titles.length);
			cell.setCellStyle(style);
			cell.setCellValue(String.valueOf(i+1)+"班");
			cell.setCellStyle(style);
	    }
	    int row=1;
		for(User user : hrmUsers){
			Row sr = sheet.createRow(row++);
			Cell cell = sr.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue(user.teacherName);
			cell.setCellStyle(style);
			
			cell = sr.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue(user.subjectname);
			cell.setCellStyle(style);
			
			cell = sr.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(user.weekDay);
			cell.setCellStyle(style);
			
			for(int i=0;i<classSize;i++){
		    	cell = sr.createCell(i+3);
				cell.setCellStyle(style);
				if(user.nums[i]>0)
					cell.setCellValue(user.nums[i]);
				cell.setCellStyle(style);
		    }
		}
		try {
			wb.write(new FileOutputStream(targetFile));
			System.out.println("导出文件成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	List<User> getHrmUsers() throws SQLException, IOException, ClassNotFoundException{
		Connection conn=getHrmConnection();
		Statement stat=conn.createStatement();
		String sql="select t.week_day,a.teacher_id,te.name teachername,t.class_id,sc.seq,count(1) num,ts.name subjectname from t_lesson_arrange t \r\n" + 
				"left join t_teacher_subject a on t.CLASS_ID = a.CLASS_ID and t.SUBJECT_ID = a.SUBJECT_ID \r\n" + 
				"left join t_teacher te on a.teacher_id=te.teacher_id \r\n" + 
				"left join t_subject ts on t.subject_id=ts.subject_id \r\n" + 
				"left join t_school_class sc on t.class_id=sc.class_id\r\n" + 
				"where t.year_id=5 and t.term_id=1 and t.CLASS_ID in \r\n" + 
				"(select class_id from t_school_class where grade_id = '1') and a.teacher_id in \r\n" + 
				"(select t.teacher_id from t_teacher_subject t where t.year_id=5 and t.term_id=1 and t.CLASS_ID in (select class_id from t_school_class where grade_id = '1') \r\n" + 
				"group by t.teacher_id having count(1)>1) group by t.week_day,a.teacher_id,t.class_id,subjectname order by a.teacher_id,week_day";
		List<User> list=new ArrayList<User>();
		try{
			ResultSet rs=stat.executeQuery(sql);
			User user=new User();
			while(rs.next()){
				String teacherName=rs.getString("teachername");
				int weekDay=rs.getInt("week_day");
				String subjectName=rs.getString("subjectname");
				int seq=rs.getInt("seq");
				int num=rs.getInt("num");
				if( ! teacherName.equals(user.teacherName) || weekDay != user.weekDay || ! subjectName.equals(user.subjectname)) {
					user=new User();
					user.teacherName=teacherName;
					user.weekDay=weekDay;
					user.subjectname=subjectName;
					list.add(user);
				}
				user.nums[seq-1]=num;
				
			}
		}finally{
			stat.close();
			conn.close();
		}
		return list;
	}
	
	public Connection getHrmConnection() throws SQLException, IOException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
//		String url = "jdbc:oracle:thin:@10.28.42.160:1521:orclgbk";
//		String username = "sszc";
//		String password = "paic1234";
		String url = "jdbc:mysql://10.20.20.100/pony_test?useUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "root";

		return DriverManager.getConnection(url, username, password);
	}
	
	static class User{
		Integer weekDay;
		String teacherName;
		String subjectname;
		int[] nums=new int[11];//在每个班级的任课数据，默认高一，11个班
	}

}
