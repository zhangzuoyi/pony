package com.zzy.pony.exam.service;







import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.dao.ExamineeDao;
import com.zzy.pony.exam.dao.ExamineeRoomArrangeDao;
import com.zzy.pony.exam.mapper.ExamineeRoomArrangeMapper;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.vo.ExamArrangeVo;
import com.zzy.pony.exam.vo.ExamRoomAllocateVo;
import com.zzy.pony.exam.vo.ExamineeVo;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.*;
import com.zzy.pony.util.CollectionsUtil;
import com.zzy.pony.vo.ExamineeRoomArrangeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;







@Service
@Transactional
public class ExamineeRoomArrangeServiceImpl implements ExamineeRoomArrangeService {

	static Logger log=LoggerFactory.getLogger(ExamineeRoomArrangeServiceImpl.class);

	
	@Autowired
	private ExamineeRoomArrangeDao examineeRoomArrangeDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ExamArrangeService examArrangeService;
	@Autowired
	private ExamRoomService examRoomService;
	@Autowired
	private ExamineeRoomArrangeMapper examineeRoomArrangeMapper;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ExamineeService examineeService;
	@Autowired
    private ExamineeDao examineeDao;
	@Autowired
    private SubjectService subjectService;

	
	
	
	
	@Override
	public void autoExamineeRoomArrange(int examId, int gradeId) {
		// TODO Auto-generated method stub
		
		int autoMode = 1;//2 默认按照考场容量排 1 按照考场平均排
		
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		//1 先将之前排的考场删除
		examineeRoomArrangeMapper.deleteByExamId(examId);
		//2 确定要排的考场，其中有组ID的一起排，没有组ID的单独排
        //modify 只选取语文数学英语中一门作为座位号
        /*ExamArrangeVo vo = null;
        Subject subject =  subjectService.findByName("语文");
        vo = examArrangeService.findVoByExamAndGradeAndSubject(examId,gradeId,subject.getSubjectId());
        if (vo == null){
            subject =  subjectService.findByName("数学");
            vo = examArrangeService.findVoByExamAndGradeAndSubject(examId,gradeId,subject.getSubjectId());
            if (vo == null){
                subject =  subjectService.findByName("英语");
                vo = examArrangeService.findVoByExamAndGradeAndSubject(examId,gradeId,subject.getSubjectId());
                if (vo == null){
                    log.info("必须参加语文数学英语任一科目");
                    return ;
                }
            }
        }
        //根据examArrange分别去找考生以及考场
        List<ExamineeVo> examinees = examineeService.findVoByArrangeId(vo.getArrangeId(),year.getYearId());//所有该门考试的考生
        List<ExamRoomAllocateVo> examRoomAllocates = examRoomService.findByArrangeId(vo.getArrangeId());//所有该门考试的考场
        //同班同学不相临
        if (autoMode == Constants.AUTO_MODE_ONE) {
            //考生平均分配到考场
            autoModeOne(examinees, examRoomAllocates);
        }
        if (autoMode == Constants.AUTO_MODE_TWO) {
            //按考场容量分配
            autoModeTwo(examinees, examRoomAllocates);
        }*/

        List<ExamArrangeVo> examArranges = examArrangeService.findVoByExamAndGradeAndGroupIsNull(examId,gradeId);//所有不在组里面的考试
		List<ExamArrangeVo> ExamArrangeVos = examArrangeService.findVoByExamAndGrade(examId, gradeId);//所有处于同一组的考试 
		Map<Integer, String> groupMap = new HashMap<Integer, String>();
		for (ExamArrangeVo vo : ExamArrangeVos) {
			if (vo.getGroupId() != 0) {
				if (groupMap.get(vo.getGroupId()) != null) {
					groupMap.put(vo.getGroupId(), groupMap.get(vo.getGroupId())+";"+vo.getArrangeId());
				}else {
					groupMap.put(vo.getGroupId(), vo.getArrangeId()+"");
				}
			}							
		}

        boolean isNotExist = true;
		//3排考场(排不在组里面的)
		for (ExamArrangeVo vo : examArranges) {
			//根据examArrange分别去找考生以及考场
			boolean subjectFlag = false;
            List<ExamineeVo> examinees = examineeService.findVoByArrangeId(vo.getArrangeId(),year.getYearId());//所有该门考试的考生
			List<ExamRoomAllocateVo> examRoomAllocates = examRoomService.findByArrangeId(vo.getArrangeId());//所有该门考试的考场
            Subject subject =   subjectService.get(vo.getSubjectId());
            if ("语文".equals(subject.getName())||"数学".equals(subject.getName())||"英语".equals(subject.getName())){
                subjectFlag = true;
            }
            //同班同学不相临
			if (autoMode == Constants.AUTO_MODE_ONE) {
				//考生平均分配到考场
				autoModeOne(examinees, examRoomAllocates,isNotExist&&subjectFlag);
			}
			if (autoMode == Constants.AUTO_MODE_TWO) {
				//按考场容量分配
                autoModeTwo(examinees, examRoomAllocates);
			}
            if ("语文".equals(subject.getName())||"数学".equals(subject.getName())||"英语".equals(subject.getName())){
                isNotExist = false;
            }
		}
		//4排考场(在组里面的,默认按照组里面的第一个arrangeId取考生以及考场)
		for (Integer groupId   : groupMap.keySet()) {
			String examArrangeIds =  groupMap.get(groupId); 
			String[] arrangeIds = examArrangeIds.split(";");
			List<ExamineeVo> examinees = examineeService.findVoByArrangeId(Integer.valueOf(arrangeIds[0]),year.getYearId());///所有该门考试的考生
			//Collections.sort(examinees);			
			for (String arrangeId : arrangeIds) {
                boolean subjectFlag = false;
                ExamArrange vo = examArrangeService.get(Integer.valueOf(arrangeId));
                Subject subject =  vo.getSubject();
                if ("语文".equals(subject.getName())||"数学".equals(subject.getName())||"英语".equals(subject.getName())){
                    subjectFlag = true;
                }

                List<ExamRoomAllocateVo> examRoomAllocates = examRoomService.findByArrangeId(Integer.valueOf(arrangeId));//所有该门考试的考场
				
	            if (autoMode == Constants.AUTO_MODE_ONE) {				
					autoModeOne(examinees, examRoomAllocates,isNotExist&&subjectFlag);
				}
				if (autoMode == Constants.AUTO_MODE_TWO) {
					autoModeTwo(examinees, examRoomAllocates);										
				}
                if ("语文".equals(subject.getName())||"数学".equals(subject.getName())||"英语".equals(subject.getName())){
                    isNotExist = false;
                }
            }
			
		}
		
		
		
		
		
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		examineeRoomArrangeDao.deleteAll();
	}
	
	
	
	
	
	@Override
	public String findExamineeRoomArrangeByClassId(
			int classId,int gradeId,int examId,String type) {
		// TODO Auto-generated method stub
		
		StringBuilder result = new StringBuilder();
		String[] arrangeStrings = {"colOne","colTwo","colThree","colFour","colFive","colSix","colSeven","colEight","colNine","colTen","colEleven","colTwelve"};
		String[] groupStrings = {"columnOne","columnTwo","columnThree","columnFour","columnFive","columnSix","columnSeven","columnEight","columnNine","columnTen","columnEleven","columnTwelve"};
		String[] arrangeSeqStrings = {"colSeqOne","colSeqTwo","colSeqThree","colSeqFour","colSeqFive","colSeqSix","colSeqSeven","colSeqEight","colSeqNine","colSeqTen","colSeqEleven","colSeqTwelve"};
		String[] groupSeqStrings = {"columnSeqOne","columnSeqTwo","columnSeqThree","columnSeqFour","columnSeqFive","columnSeqSix","columnSeqSeven","columnSeqEight","columnSeqNine","columnSeqTen","columnSeqEleven","columnSeqTwelve"};

		List<ExamArrangeVo> examArranges = examArrangeService.findVoByExamAndGrade(examId, gradeId);
		Map<Integer , String> arrangeMap = new LinkedHashMap<Integer, String>();//不在组内
		Map<Integer, String> groupMap = new LinkedHashMap<Integer, String>();//在组内
		Map<Integer, String> arrangeHeadMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> groupHeadMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> arrangeSeqHeadMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> groupSeqHeadMap = new LinkedHashMap<Integer, String>();
		
		for (ExamArrangeVo examArrange : examArranges) {
			if (examArrange.getGroupId() == 0) {
				arrangeMap.put(examArrange.getArrangeId(), examArrange.getSubjectName());
			}else{
				if (groupMap.get(examArrange.getGroupId()) != null) {
					groupMap.put(examArrange.getGroupId(), groupMap.get(examArrange.getGroupId())+examArrange.getSubjectName());
				}else{
					groupMap.put(examArrange.getGroupId(), examArrange.getSubjectName());
				}								
			}							
		}
		int i=0;
		for (Integer key : arrangeMap.keySet()) {
			arrangeHeadMap.put(key,arrangeStrings[i]);
			i++;
		}
		int j=0;
		for (Integer key : groupMap.keySet()) {
			groupHeadMap.put(key,groupStrings[j]);
			j++;
		}
		int m=0;
		for (Integer key : arrangeMap.keySet()) {
			arrangeSeqHeadMap.put(key,arrangeSeqStrings[m]);
			m++;
		}
		int n=0;
		for (Integer key : groupMap.keySet()) {
			groupSeqHeadMap.put(key,groupSeqStrings[n]);
			n++;
		}
		List<ExamineeRoomArrangeVo> examineeRoomArrangeVos =  examineeRoomArrangeMapper.findExamineeRoomArrangeByClassId(classId, examId);		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String,Object>>();//key:studentId
		for (ExamineeRoomArrangeVo vo : examineeRoomArrangeVos) {
			if (map.containsKey(vo.getStudentId())) {
				Map<String, Object> innerMap = map.get(vo.getStudentId());
				if (vo.getGroupId() == 0) {
					innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getRoomName());
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeq());
				}else {
					if (innerMap.containsKey(groupHeadMap.get(vo.getGroupId()))
							&&(!innerMap.get(groupHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getRoomName()) 
							||!innerMap.get(groupSeqHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getSeq()+""))
							 ) {
						innerMap.put(groupHeadMap.get(vo.getGroupId()),innerMap.get(groupHeadMap.get(vo.getGroupId()))+";"+vo.getRoomName() );
						innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getSeq());
					}else {
						innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getRoomName());
						innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeq());
					}
				}														
			}else{
				Map<String, Object> innerMap = new HashMap<String, Object>();
				innerMap.put("seatNo", vo.getSeatNo());
				/*if (Constants.SELECT.equalsIgnoreCase(type)) {
					innerMap.put("regNo", vo.getRegNo());
				}
				if (Constants.EXPORT.equalsIgnoreCase(type)) {
					innerMap.put("regNo", vo.getRegNo().substring(vo.getRegNo().length()-4, vo.getRegNo().length()));
				}*/
				innerMap.put("studentName", vo.getStudentName());
				innerMap.put("className", vo.getClassName());
				innerMap.put("studentNo", vo.getStudentNo()); 
				if(vo.getGroupId() == 0){
				innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getRoomName());
				innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeq());
				}else {
				innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getRoomName());
				innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeq());	
				}
				map.put(vo.getStudentId(), innerMap);
			}								
		}		
		for (Integer studentId : map.keySet()) {
			dataList.add(map.get(studentId));
		}					
		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
		Map<String, Object> regNoMap = new HashMap<String, Object>();
		regNoMap.put("prop", "seatNo");
		regNoMap.put("label", "准考证号");
		/*if (Constants.SELECT.equalsIgnoreCase(type)) {
			regNoMap.put("prop", "regNo");
			regNoMap.put("label", "准考证号");
		}
		if (Constants.EXPORT.equalsIgnoreCase(type)) {
			regNoMap.put("prop", "regNo");
			regNoMap.put("label", "后四位");
		}*/		
		Map<String, Object> studentNameMap = new HashMap<String, Object>();
		studentNameMap.put("prop", "studentName");
		studentNameMap.put("label", "姓名");
		Map<String, Object> classNameMap = new HashMap<String, Object>();
		classNameMap.put("prop", "className");
		classNameMap.put("label", "班级");
		Map<String, Object> studentNoMap = new HashMap<String, Object>();
		studentNoMap.put("prop", "studentNo");
		studentNoMap.put("label", "学号");
		headList.add(regNoMap);
		headList.add(studentNameMap);
		headList.add(classNameMap);
		headList.add(studentNoMap);		
		for (Integer arrangeId : arrangeHeadMap.keySet()) {
			 // arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeHeadMap.get(arrangeId));
			headMap.put("label", arrangeMap.get(arrangeId));
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "座号");
			headList.add(headMap);
			headList.add(headSeqMap);
		} 
		/*for (Integer arrangeId : arrangeSeqHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
			headMap.put("label", "序号");
			headList.add(headMap);		
		} */
		for (Integer arrangeId : groupHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", groupHeadMap.get(arrangeId));
			headMap.put("label", groupMap.get(arrangeId));
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "座号");
			headList.add(headMap);
			headList.add(headSeqMap);

		} 
		/*for (Integer arrangeId : groupSeqHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", groupSeqHeadMap.get(arrangeId));
			headMap.put("label", "序号");
			headList.add(headMap);		
		}*/
		
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		String data = gson.toJson(dataList);
		Gson gson2 = gb.create();
		String head= gson2.toJson(headList);		
		result.append("{\"total\"");
		result.append(":");
		result.append(dataList.size());
		result.append(",\"rows\":");
		result.append(data);
		result.append(",\"title\":");
		result.append(head);		
		result.append("}");		
		return result.toString();
	}

	@Override
	public String findExamineeRoomArrangeByRoomId(String roomId,int gradeId,int examId,String type) {
		// TODO Auto-generated method stub
		StringBuilder result = new StringBuilder();
		String[] arrangeStrings = {"colOne","colTwo","colThree","colFour","colFive","colSix","colSeven","colEight","colNine","colTen","colEleven","colTwelve"};
		String[] groupStrings = {"columnOne","columnTwo","columnThree","columnFour","columnFive","columnSix","columnSeven","columnEight","columnNine","columnTen","columnEleven","columnTwelve"};
		String[] arrangeSeqStrings = {"colSeqOne","colSeqTwo","colSeqThree","colSeqFour","colSeqFive","colSeqSix","colSeqSeven","colSeqEight","colSeqNine","colSeqTen","colSeqEleven","colSeqTwelve"};
		String[] groupSeqStrings = {"columnSeqOne","columnSeqTwo","columnSeqThree","columnSeqFour","columnSeqFive","columnSeqSix","columnSeqSeven","columnSeqEight","columnSeqNine","columnSeqTen","columnSeqEleven","columnSeqTwelve"};

		List<ExamArrangeVo> examArranges = examArrangeService.findVoByExamAndGrade(examId, gradeId);
		Map<Integer , String> arrangeMap = new LinkedHashMap<Integer, String>();//不在组内
		Map<Integer, String> groupMap = new LinkedHashMap<Integer, String>();//在组内
		Map<Integer, String> arrangeHeadMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> groupHeadMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> arrangeSeqHeadMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> groupSeqHeadMap = new LinkedHashMap<Integer, String>();
		
		for (ExamArrangeVo examArrange : examArranges) {
			if (examArrange.getGroupId() == 0) {
				arrangeMap.put(examArrange.getArrangeId(), examArrange.getSubjectName());
			}else{
				if (groupMap.get(examArrange.getGroupId()) != null) {
					groupMap.put(examArrange.getGroupId(), groupMap.get(examArrange.getGroupId())+examArrange.getSubjectName());
				}else{
					groupMap.put(examArrange.getGroupId(), examArrange.getSubjectName());
				}								
			}							
		}
		int i=0;
		for (Integer key : arrangeMap.keySet()) {
			arrangeHeadMap.put(key,arrangeStrings[i]);
			i++;
		}
		int j=0;
		for (Integer key : groupMap.keySet()) {
			groupHeadMap.put(key,groupStrings[j]);
			j++;
		}
		int m=0;
		for (Integer key : arrangeMap.keySet()) {
			arrangeSeqHeadMap.put(key,arrangeSeqStrings[m]);
			m++;
		}
		int n=0;
		for (Integer key : groupMap.keySet()) {
			groupSeqHeadMap.put(key,groupSeqStrings[n]);
			n++;
		}
		List<ExamineeRoomArrangeVo> examineeRoomArrangeVos =  examineeRoomArrangeMapper.findExamineeRoomArrangeByRoomId(roomId, examId);		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String,Object>>();//key:seq  有序 
		for (ExamineeRoomArrangeVo vo : examineeRoomArrangeVos) {
			if (map.containsKey(vo.getSeq())) {
				Map<String, Object> innerMap = map.get(vo.getSeq());
				if (vo.getGroupId() == 0) {
					innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getStudentName());
					
					if (Constants.SELECT.equalsIgnoreCase(type)) {
						innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeatNo());
					}
					if (Constants.EXPORT.equalsIgnoreCase(type)) {
						innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeatNo().substring(vo.getSeatNo().length()-4, vo.getSeatNo().length()));
					}
					
				}else {
					if (innerMap.containsKey(groupHeadMap.get(vo.getGroupId()))
							&&(!innerMap.get(groupHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getStudentName()) 
							||!innerMap.get(groupSeqHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getSeatNo()))
							 ) {
						innerMap.put(groupHeadMap.get(vo.getGroupId()),innerMap.get(groupHeadMap.get(vo.getGroupId()))+";"+vo.getStudentName() );
						
						if (Constants.SELECT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getSeatNo());
						}
						if (Constants.EXPORT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getSeatNo().substring(vo.getSeatNo().length()-4, vo.getSeatNo().length()));
						}
					}else {
						innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getStudentName());
						if (Constants.SELECT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeatNo());
						}
						if (Constants.EXPORT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeatNo().substring(vo.getSeatNo().length()-4, vo.getSeatNo().length()));
						}
					}
				}								
			}else {
				Map<String, Object> innerMap = new HashMap<String, Object>();
				innerMap.put("roomName", vo.getRoomName());
				if(vo.getGroupId() == 0){
				innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getStudentName());
				if (Constants.SELECT.equalsIgnoreCase(type)) {
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeatNo());
				}
				if (Constants.EXPORT.equalsIgnoreCase(type)) {
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeatNo().substring(vo.getSeatNo().length()-4, vo.getSeatNo().length()));
				}
				
				}else {
				innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getStudentName());
				if (Constants.SELECT.equalsIgnoreCase(type)) {
					innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeatNo());
				}
				if (Constants.EXPORT.equalsIgnoreCase(type)) {
					innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeatNo().substring(vo.getSeatNo().length()-4, vo.getSeatNo().length()));
				}
				
				}
				map.put(vo.getSeq(), innerMap);
			}
															
		}		
			
		for (Integer seq : map.keySet()) {
			Map<String, Object> innerMap = new HashMap<String, Object>();
			innerMap.put("seq", seq);
			innerMap.putAll(map.get(seq));
			dataList.add(innerMap);
		}
		
		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
		Map<String, Object> roomNameMap = new HashMap<String, Object>();
		roomNameMap.put("prop", "roomName");
		roomNameMap.put("label", "考场");
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.put("prop", "seq");
		seqMap.put("label", "座号");	
		headList.add(roomNameMap);
		headList.add(seqMap);		
		for (Integer arrangeId : arrangeHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeHeadMap.get(arrangeId));
			headMap.put("label", arrangeMap.get(arrangeId));
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			if (Constants.SELECT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "准考证号");
			}
			if (Constants.EXPORT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "后四位");
			}						
			headList.add(headMap);
			headList.add(headSeqMap);

		} 
		/*for (Integer arrangeId : arrangeSeqHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
			headMap.put("label", "准考证号");
			headList.add(headMap);		
		}*/
		for (Integer arrangeId : groupHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", groupHeadMap.get(arrangeId));
			headMap.put("label", groupMap.get(arrangeId));
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			if (Constants.SELECT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "准考证号");
			}
			if (Constants.EXPORT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "后四位");
			}			
			headList.add(headMap);
			headList.add(headSeqMap);

		} 
		/*for (Integer arrangeId : groupSeqHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", groupSeqHeadMap.get(arrangeId));
			headMap.put("label", "准考证号");
			headList.add(headMap);		
		} */
		
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		String data = gson.toJson(dataList);
		Gson gson2 = gb.create();
		String head= gson2.toJson(headList);		
		result.append("{\"total\"");
		result.append(":");
		result.append(dataList.size());
		result.append(",\"rows\":");
		result.append(data);
		result.append(",\"title\":");
		result.append(head);		
		result.append("}");		
		return result.toString();
	}
	
	@Override
	public String findExamineeRoomArrangeBySubjectId(int subjectId,int gradeId,int examId,String type) {
		// TODO Auto-generated method stub
		StringBuilder result = new StringBuilder();
		String[] arrangeStrings = {"colOne","colTwo","colThree","colFour","colFive","colSix","colSeven","colEight","colNine","colTen","colEleven","colTwelve"};
		String[] groupStrings = {"columnOne","columnTwo","columnThree","columnFour","columnFive","columnSix","columnSeven","columnEight","columnNine","columnTen","columnEleven","columnTwelve"};
		String[] arrangeSeqStrings = {"colSeqOne","colSeqTwo","colSeqThree","colSeqFour","colSeqFive","colSeqSix","colSeqSeven","colSeqEight","colSeqNine","colSeqTen","colSeqEleven","colSeqTwelve"};
		String[] groupSeqStrings = {"columnSeqOne","columnSeqTwo","columnSeqThree","columnSeqFour","columnSeqFive","columnSeqSix","columnSeqSeven","columnSeqEight","columnSeqNine","columnSeqTen","columnSeqEleven","columnSeqTwelve"};

		ExamArrangeVo examArrange = examArrangeService.findVoByExamAndGradeAndSubject(examId, gradeId, subjectId);
		Map<Integer , String> arrangeMap = new HashMap<Integer, String>();//不在组内
		Map<Integer, String> groupMap = new HashMap<Integer, String>();//在组内
		Map<Integer, String> arrangeHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> groupHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> arrangeSeqHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> groupSeqHeadMap = new HashMap<Integer, String>();
		
			if (examArrange.getGroupId() == 0) {
				arrangeMap.put(examArrange.getArrangeId(), examArrange.getSubjectName());
			}else{
				if (groupMap.get(examArrange.getGroupId()) != null) {
					groupMap.put(examArrange.getGroupId(), groupMap.get(examArrange.getGroupId())+examArrange.getSubjectName());
				}else{
					groupMap.put(examArrange.getGroupId(), examArrange.getSubjectName());
				}								
			}							
		
		int i=0;
		for (Integer key : arrangeMap.keySet()) {
			arrangeHeadMap.put(key,arrangeStrings[i]);
			i++;
		}
		int j=0;
		for (Integer key : groupMap.keySet()) {
			groupHeadMap.put(key,groupStrings[j]);
			j++;
		}
		int m=0;
		for (Integer key : arrangeMap.keySet()) {
			arrangeSeqHeadMap.put(key,arrangeSeqStrings[m]);
			m++;
		}
		int n=0;
		for (Integer key : groupMap.keySet()) {
			groupSeqHeadMap.put(key,groupSeqStrings[n]);
			n++;
		}
		List<ExamineeRoomArrangeVo> examineeRoomArrangeVos =  examineeRoomArrangeMapper.findExamineeRoomArrangeBySubjectId(subjectId, examId);		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<String, Map<String, Object>> map = new LinkedHashMap<String, Map<String,Object>>();//key:roomId+seq  有序 
		for (ExamineeRoomArrangeVo vo : examineeRoomArrangeVos) {
			if (map.containsKey(vo.getRoomId()+"#"+vo.getSeq())) {
				Map<String, Object> innerMap = map.get(vo.getRoomId()+"#"+vo.getSeq());
				if (vo.getGroupId() == 0) {
					innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getStudentName());
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeatNo());
					/*if (Constants.SELECT.equalsIgnoreCase(type)) {
						innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getRegNo());
					}
					if (Constants.EXPORT.equalsIgnoreCase(type)) {
						innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getRegNo().substring(vo.getRegNo().length()-4, vo.getRegNo().length()));
					}*/
					
				}else {
					if (innerMap.containsKey(groupHeadMap.get(vo.getGroupId()))
							&&(!innerMap.get(groupHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getStudentName()) 
							||!innerMap.get(groupSeqHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getSeatNo()))
							 ) {
						innerMap.put(groupHeadMap.get(vo.getGroupId()),innerMap.get(groupHeadMap.get(vo.getGroupId()))+";"+vo.getStudentName() );
						innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getSeatNo());

						/*if (Constants.SELECT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getRegNo());
						}
						if (Constants.EXPORT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getRegNo().substring(vo.getRegNo().length()-4, vo.getRegNo().length()));
						}*/
					}else {
						innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getStudentName());
						innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeatNo());
						/*if (Constants.SELECT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getRegNo());
						}
						if (Constants.EXPORT.equalsIgnoreCase(type)) {
							innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getRegNo().substring(vo.getRegNo().length()-4, vo.getRegNo().length()));
						}*/
					}
				}								
			}else {
				Map<String, Object> innerMap = new HashMap<String, Object>();
				innerMap.put("studentNo", vo.getStudentNo());
				if(vo.getGroupId() == 0){
				innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getStudentName());
				innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getSeatNo());
				/*if (Constants.SELECT.equalsIgnoreCase(type)) {
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getRegNo());
				}
				if (Constants.EXPORT.equalsIgnoreCase(type)) {
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getRegNo().substring(vo.getRegNo().length()-4, vo.getRegNo().length()));
				}*/
				
				}else {
				innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getStudentName());
				innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getSeatNo());
				/*if (Constants.SELECT.equalsIgnoreCase(type)) {
					innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getRegNo());	
				}
				if (Constants.EXPORT.equalsIgnoreCase(type)) {
					innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getRegNo().substring(vo.getRegNo().length()-4, vo.getRegNo().length()));	
				}*/				
				}
				innerMap.put("className", vo.getClassName());
				innerMap.put("roomName", vo.getRoomName());

				map.put(vo.getRoomId()+"#"+vo.getSeq(), innerMap);
			}
															
		}		
			
		for (String seq : map.keySet()) {
			Map<String, Object> innerMap = new HashMap<String, Object>();
			innerMap.put("seq", seq.substring(seq.indexOf("#")+1));
			innerMap.putAll(map.get(seq));
			dataList.add(innerMap);
		}
		
		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
		Map<String, Object> studentNoMap = new HashMap<String, Object>();
		studentNoMap.put("prop", "studentNo");
		studentNoMap.put("label", "学号");
		headList.add(studentNoMap);
		for (Integer arrangeId : arrangeHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeHeadMap.get(arrangeId));
			headMap.put("label", "姓名");
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "准考证号");
			/*if (Constants.SELECT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "准考证号");
			}
			if (Constants.EXPORT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "后四位");
			}*/	
			headList.add(headSeqMap);
			headList.add(headMap);

		} 		
		for (Integer arrangeId : groupHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", groupHeadMap.get(arrangeId));
			headMap.put("label", "姓名");
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "准考证号");
			/*if (Constants.SELECT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "准考证号");
			}
			if (Constants.EXPORT.equalsIgnoreCase(type)) {
				headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
				headSeqMap.put("label", "后四位");
			}*/	
			headList.add(headSeqMap);
			headList.add(headMap);

		} 
		
		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("prop", "className");
		classMap.put("label", "班级");				
		Map<String, Object> roomNameMap = new HashMap<String, Object>();
		roomNameMap.put("prop", "roomName");
		roomNameMap.put("label", "考场");
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.put("prop", "seq");
		seqMap.put("label", "座号");	
		headList.add(classMap);
		headList.add(roomNameMap);
		headList.add(seqMap);								
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		String data = gson.toJson(dataList);
		Gson gson2 = gb.create();
		String head= gson2.toJson(headList);		
		result.append("{\"total\"");
		result.append(":");
		result.append(dataList.size());
		result.append(",\"rows\":");
		result.append(data);
		result.append(",\"title\":");
		result.append(head);		
		result.append("}");		
		return result.toString();
	}

	//考生平均分配到考场
	private void autoModeOne(List<ExamineeVo> examinees,List<ExamRoomAllocateVo> examRoomAllocates,boolean flag){
		int examineeCount = examinees.size();
		int examRoomCount = examRoomAllocates.size();
		int averageExaminee = examineeCount/examRoomCount;//每个考场分配多少考生
		int remainExaminee = examineeCount%examRoomCount;//剩余的考生
		int remainSize = remainExaminee;
		//将所有考生排序
		//Collections.sort(examinees);
		//todo 同班同学不相临
		swap(examinees);
        int i=0;
        int fromIndex = 0;
        int toIndex = 0;
        String pre = null;//前缀
        int bit = 4 ;//位数
        for (ExamRoomAllocateVo era:
        examRoomAllocates) {
        	
        	if(remainExaminee > 0) {
        		fromIndex = toIndex ;
        		toIndex = (i+1)*averageExaminee + 1+i;//modify 此处应该为i+1
        	}else {
        		fromIndex = toIndex ;
        		toIndex = (i+1)*averageExaminee + remainSize ;
        	}
        	
            List<ExamineeVo> averageExaminees = examinees.subList(fromIndex,toIndex);
            //List<ExamineeVo> averageExaminees = examinees.subList(i*averageExaminee,(i+1)*averageExaminee);
            //add  update seat_no
			List<Examinee> examineeList = new ArrayList<Examinee>();
			List<ExamineeRoomArrangeVo> examineeRoomArrangeVos = new ArrayList<ExamineeRoomArrangeVo>();



			int seq=1;
			//前缀抽取,需保证前缀最后一位不为0
			if(flag && i==0 && seq == 1){
				pre = getPre(averageExaminees.get(0).getRegNo());
                bit = getBit(averageExaminees.get(0).getRegNo());
			}
            for (ExamineeVo examinee:
            averageExaminees) {
				ExamineeRoomArrangeVo vo = new ExamineeRoomArrangeVo();
				vo.setRoomId(era.getRoomId());
				vo.setExamineeId(examinee.getExamineeId());
				vo.setSeq(seq);
                examineeRoomArrangeVos.add(vo);

				if(flag) {
					Examinee ex = examineeDao.findOne(examinee.getExamineeId());
					if (ex != null && StringUtils.isNotEmpty(ex.getRegNo())) {
						//seatNo = pre+bit顺序
						ex.setSeatNo(pre + String.format("%0" + bit + "d", seq));
						examineeList.add(ex);
					}
				}
                seq++;
            }
            examineeRoomArrangeMapper.insertBatchExamineeRoomArrang(examineeRoomArrangeVos);
            if (flag) {
				examineeDao.save(examineeList);
			}

            i++;
            remainExaminee--;
        }
        
        
	}
	//按考场容量分配
	private void autoModeTwo(List<ExamineeVo> examinees,List<ExamRoomAllocateVo> examRoomAllocates){
        //int examineeCount = examinees.size();
        //将所有考生排序
        //Collections.sort(examinees);
        swap(examinees);
        int m=0;
        int count=1;
        ExamRoomAllocateVo era = examRoomAllocates.get(m);
        for (ExamineeVo examinee:
             examinees) {
         if(count>era.getCapacity()){
             m++;
             era=examRoomAllocates.get(m);
             count = 1;
         }
         ExamineeRoomArrangeVo vo = new ExamineeRoomArrangeVo();
         vo.setRoomId(era.getRoomId());
         vo.setExamineeId(examinee.getExamineeId());
         vo.setSeq(count);

         examineeRoomArrangeMapper.insertExamineeRoomArrange(vo);
         count++;
        }	
		
	}
	//同班同学不相邻
	private void swap(List<ExamineeVo> examinees){
		
		for (int i = 0; i < examinees.size()-2; i++) {
			int j = i;
			if (examinees.get(i+1).getClassId()==examinees.get(i).getClassId()) {
				while((j+2)<examinees.size() && examinees.get(i+1).getClassId()==(examinees.get(j+2).getClassId())){
					j++;
				}
				if((j+2)<examinees.size())
					CollectionsUtil.swap(examinees, j+2, i+1);
			}						
		}				
	}

	private   String getPre(String regNo){
		StringBuilder pre = new StringBuilder();
		char[] chars =  StringUtils.reverse(regNo).toCharArray();
		int start = StringUtils.reverse(regNo).indexOf("0");
		for (int index = start;index<chars.length;index++){
			if (chars[index] != '0' ){
				return pre.append(Arrays.copyOfRange(chars,index,chars.length)).reverse().toString() ;
			}
		}
		return null;
	}
	private  int getBit(String regNo){
        StringBuilder pre = new StringBuilder();
        char[] chars =  StringUtils.reverse(regNo).toCharArray();
        int start = StringUtils.reverse(regNo).indexOf("0");
        for (int index = start;index<chars.length;index++){
            if (chars[index] != '0' ){
                return index ;
            }
        }
	    return 4;//默认4位
    }






	
	

	
	
	
	
	
	
	

	
	
	

	
	
}
