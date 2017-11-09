package com.zzy.test.arrange;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 老师任课分析
 * @author ZHANGZUOYI499
 *
 */
public class LessonPeriodAnalysis {
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException{
		LessonPeriodAnalysis obj=new LessonPeriodAnalysis();
		obj.analysis();
	}
	void analysis() throws SQLException, IOException, ClassNotFoundException{
		List<Obj> list=getObjs();
		List<Obj> teacherList=new ArrayList<Obj>();
		String teacherName=null;
		String subjectName=null;
		for(Obj obj: list) {
			if( ! obj.teacherName.equals(teacherName) || ! obj.subjectname.equals(subjectName)) {
				analysis(teacherList);
				analysisPQ(teacherList);
				teacherName=obj.teacherName;
				subjectName=obj.subjectname;
				teacherList.clear();
			}
			teacherList.add(obj);
		}
		analysis(teacherList);
		analysisPQ(teacherList);
	}
	//分析下午课时
	void analysis(List<Obj> teacherList) {
		if(teacherList.size()==0) {
			return;
		}
		String teacherName=teacherList.get(0).teacherName;
		String subjectName=teacherList.get(0).subjectname;
		Set<Integer> classSet=new HashSet<Integer>();//班级列表，为了算出班级数
		int amNum=0;//上午课时数
		int pmNum=0;//下午课时数
		for(Obj obj : teacherList) {
			classSet.add(obj.classSeq);
			if(obj.period <= 4) {
				amNum++;
			}else {
				pmNum++;
			}
		}
		int classNum=classSet.size();
		int ratio=2;//普通课程是2
		if("语文".equals(subjectName) || "数学".equals(subjectName) || "英语".equals(subjectName)) {
			ratio=1;
		}
		if(pmNum == 0) {
			System.out.println(teacherName+":"+subjectName+" 下午没课");
		}else if((pmNum*1.0/classNum) > ratio){
			System.out.println(teacherName+":"+subjectName+" 下午课过多 "+(pmNum*1.0/classNum));
		}
	}
	//分析课程平齐
	void analysisPQ(List<Obj> teacherList) {
		if(teacherList.size()==0) {
			return;
		}
		String teacherName=teacherList.get(0).teacherName;
		String subjectName=teacherList.get(0).subjectname;
		Set<Integer> classSet=new HashSet<Integer>();//班级列表，为了算出班级数
		for(Obj obj : teacherList) {
			classSet.add(obj.classSeq);
		}
		int classNum=classSet.size();
		if(classNum == 1) {
			return;
		}
		Map<Integer, Integer> map=new HashMap<Integer, Integer>();
		boolean isPQ=true;
		int classSeq=0;
		int lsNum=1;//课程在同一班连上数量
		int i=0;
		for(Obj obj : teacherList) {
			i++;
			if(obj.classSeq != classSeq) {
				classSeq=obj.classSeq;
				lsNum=1;
			}else {
				lsNum++;
			}
			Integer num=map.get(obj.classSeq);
			if(num == null) {
				map.put(obj.classSeq, 1);
			}else {
				map.put(obj.classSeq, num+1);
			}
			if(i % (lsNum * classNum) == 0) {
				classSeq=0;
				isPQ=isPQ(map);
				map.clear();
				if( ! isPQ) {
					break;
				}
			}
		}
		if( ! isPQ) {
			System.out.println(teacherName+":"+subjectName+" 课程不平齐");
		}
	}
	private boolean isPQ(Map<Integer, Integer> map) {
		int p=0;
		for(Integer num : map.values()) {
			if(p == 0) {
				p=num;
			}else if(p != num) {
				return false;
			}
			p=num;
		}
		return true;
	}

	List<Obj> getObjs() throws SQLException, IOException, ClassNotFoundException{
		Connection conn=getHrmConnection();
		Statement stat=conn.createStatement();
		String sql="select t.week_day,a.teacher_id,te.name teachername,t.class_id,sc.seq,ts.name subjectname,lp.seq period from t_lesson_arrange t \r\n" + 
				"left join t_teacher_subject a on t.CLASS_ID = a.CLASS_ID and t.SUBJECT_ID = a.SUBJECT_ID \r\n" + 
				"left join t_teacher te on a.teacher_id=te.teacher_id \r\n" + 
				"left join t_subject ts on t.subject_id=ts.subject_id \r\n" + 
				"left join t_school_class sc on t.class_id=sc.class_id\r\n" + 
				"left join t_lesson_period lp on t.period_id=lp.period_id\r\n" + 
				"where t.year_id=5 and t.term_id=1 and t.CLASS_ID in \r\n" + 
				"(select class_id from t_school_class where grade_id = '1') and ts.name not in ('体育','音乐')\r\n" + 
				"order by a.teacher_id,week_day,lp.seq";
		List<Obj> list=new ArrayList<Obj>();
		try{
			ResultSet rs=stat.executeQuery(sql);
			while(rs.next()){
				String teacherName=rs.getString("teachername");
				int weekDay=rs.getInt("week_day");
				String subjectName=rs.getString("subjectname");
				int seq=rs.getInt("seq");
				int period=rs.getInt("period");
				Obj obj=new Obj();
				obj.classSeq=seq;
				obj.period=period;
				obj.subjectname=subjectName;
				obj.teacherName=teacherName;
				obj.weekDay=weekDay;
				list.add(obj);
				
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
	
	static class Obj{
		Integer weekDay;
		String teacherName;
		String subjectname;
		Integer classSeq;
		Integer period;
	}
}
