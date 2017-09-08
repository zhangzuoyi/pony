package com.zzy.pony.exam.service;







import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.exam.mapper.ExamineeRoomArrangeMapper;
import com.zzy.pony.exam.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.dao.ExamineeRoomArrangeDao;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.util.CollectionsUtil;
import com.zzy.pony.vo.ExamVo;
import com.zzy.pony.vo.ExamineeRoomArrangeVo;







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
	
	
	
	
	@Override
	public void autoExamineeRoomArrange(int examId, int gradeId) {
		// TODO Auto-generated method stub
		
		int autoMode = 1;//2 默认按照考场容量排 1 按照考场平均排
		
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		//1 先将之前排的考场删除
		examineeRoomArrangeMapper.deleteByExamId(examId);
		//2 确定要排的考场，其中有组ID的一起排，没有组ID的单独排
		List<ExamVo> examVos = examService.findByYearAndTermOrderByExamDate(year, term); 
		ExamVo examVo = examVos.get(0);//当前考试
		List<ExamArrange> examArranges = examArrangeService.findByExamAndGroupIsNull(examVo.getExamId());//所有不在组里面的考试
		List<ExamArrangeGroup> examArrangeGroups = examArrangeService.findByExamAndGroup(examId, gradeId);//所有处于同一组的考试 
		Map<Integer, String> groupMap = new HashMap<Integer, String>();
		for (ExamArrangeGroup examArrangeGroup : examArrangeGroups) {
			List<ExamArrange> eas = examArrangeGroup.getExamArranges();
			for (ExamArrange examArrange : eas) {
				if (groupMap.get(examArrangeGroup.getGroupId()) != null) {
					groupMap.put(examArrangeGroup.getGroupId(), groupMap.get(examArrangeGroup.getGroupId())+";"+examArrange.getArrangeId());
				}else {
					groupMap.put(examArrangeGroup.getGroupId(), examArrange.getArrangeId()+"");
				}
			}
		}		
		//3排考场(排不在组里面的)
		for (ExamArrange examArrange : examArranges) {
			//根据examArrange分别去找考生以及考场
			List<Examinee> examinees = examArrange.getExaminees();//所有该门考试的考生
			List<ExamRoomAllocate> examRoomAllocates = new ArrayList<ExamRoomAllocate>();
            examRoomAllocates =  examRoomService.findByExamArrangeOrderByRoomSeq(examArrange);//所有该门考试的考场
			//同班同学不相临			
			if (autoMode == Constants.AUTO_MODE_ONE) {
				//考生平均分配到考场
				autoModeOne(examinees, examRoomAllocates);
			}
			if (autoMode == Constants.AUTO_MODE_TWO) {
				//按考场容量分配
                autoModeTwo(examinees, examRoomAllocates);
			}								
		}
		//4排考场(在组里面的,默认按照组里面的第一个arrangeId取考生以及考场)
		for (Integer groupId   : groupMap.keySet()) {
			String examArrangeIds =  groupMap.get(groupId); 
			String[] arrangeIds = examArrangeIds.split(";");
			ExamArrange examArrange = examArrangeService.get(Integer.valueOf(arrangeIds[0]));
			List<Examinee> examinees = examArrange.getExaminees();//所有该门考试的考生
			//Collections.sort(examinees);			
			for (String arrangeId : arrangeIds) {					
				examArrange = examArrangeService.get(Integer.valueOf(arrangeId));
				List<ExamRoomAllocate> examRoomAllocates = new ArrayList<ExamRoomAllocate>();
	            examRoomAllocates =  examRoomService.findByExamArrangeOrderByRoomSeq(examArrange);//所有该门考试的考场
				
	            if (autoMode == Constants.AUTO_MODE_ONE) {				
					autoModeOne(examinees, examRoomAllocates);										
				}
				if (autoMode == Constants.AUTO_MODE_TWO) {
					autoModeTwo(examinees, examRoomAllocates);										
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
			int classId,int examId) {
		// TODO Auto-generated method stub
		
		StringBuilder result = new StringBuilder();
		String[] arrangeStrings = {"colOne","colTwo","colThree","colFour","colFive","colSix","colSeven","colEight","colNine","colTen","colEleven","colTwelve"};
		String[] groupStrings = {"columnOne","columnTwo","columnThree","columnFour","columnFive","columnSix","columnSeven","columnEight","columnNine","columnTen","columnEleven","columnTwelve"};
		String[] arrangeSeqStrings = {"colSeqOne","colSeqTwo","colSeqThree","colSeqFour","colSeqFive","colSeqSix","colSeqSeven","colSeqEight","colSeqNine","colSeqTen","colSeqEleven","colSeqTwelve"};
		String[] groupSeqStrings = {"columnSeqOne","columnSeqTwo","columnSeqThree","columnSeqFour","columnSeqFive","columnSeqSix","columnSeqSeven","columnSeqEight","columnSeqNine","columnSeqTen","columnSeqEleven","columnSeqTwelve"};

		List<ExamArrange> examArranges = examArrangeService.findByExam(examId);
		Map<Integer , String> arrangeMap = new HashMap<Integer, String>();//不在组内
		Map<Integer, String> groupMap = new HashMap<Integer, String>();//在组内
		Map<Integer, String> arrangeHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> groupHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> arrangeSeqHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> groupSeqHeadMap = new HashMap<Integer, String>();
		
		for (ExamArrange examArrange : examArranges) {
			if (examArrange.getGroup() == null) {
				arrangeMap.put(examArrange.getArrangeId(), examArrange.getSubject().getName());
			}else{
				if (groupMap.get(examArrange.getGroup().getGroupId()) != null) {
					groupMap.put(examArrange.getGroup().getGroupId(), groupMap.get(examArrange.getGroup().getGroupId())+examArrange.getSubject().getName());
				}else{
					groupMap.put(examArrange.getGroup().getGroupId(), examArrange.getSubject().getName());
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
			i++;
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
				innerMap.put("regNo", vo.getRegNo());
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
		regNoMap.put("prop", "regNo");
		regNoMap.put("label", "考生号");
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
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeHeadMap.get(arrangeId));
			headMap.put("label", arrangeMap.get(arrangeId));
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "序号");
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
			headSeqMap.put("label", "序号");
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
	public String findExamineeRoomArrangeByRoomId(int roomId,int examId) {
		// TODO Auto-generated method stub
		StringBuilder result = new StringBuilder();
		String[] arrangeStrings = {"colOne","colTwo","colThree","colFour","colFive","colSix","colSeven","colEight","colNine","colTen","colEleven","colTwelve"};
		String[] groupStrings = {"columnOne","columnTwo","columnThree","columnFour","columnFive","columnSix","columnSeven","columnEight","columnNine","columnTen","columnEleven","columnTwelve"};
		String[] arrangeSeqStrings = {"colSeqOne","colSeqTwo","colSeqThree","colSeqFour","colSeqFive","colSeqSix","colSeqSeven","colSeqEight","colSeqNine","colSeqTen","colSeqEleven","colSeqTwelve"};
		String[] groupSeqStrings = {"columnSeqOne","columnSeqTwo","columnSeqThree","columnSeqFour","columnSeqFive","columnSeqSix","columnSeqSeven","columnSeqEight","columnSeqNine","columnSeqTen","columnSeqEleven","columnSeqTwelve"};

		List<ExamArrange> examArranges = examArrangeService.findByExam(examId);
		Map<Integer , String> arrangeMap = new HashMap<Integer, String>();//不在组内
		Map<Integer, String> groupMap = new HashMap<Integer, String>();//在组内
		Map<Integer, String> arrangeHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> groupHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> arrangeSeqHeadMap = new HashMap<Integer, String>();
		Map<Integer, String> groupSeqHeadMap = new HashMap<Integer, String>();
		
		for (ExamArrange examArrange : examArranges) {
			if (examArrange.getGroup() == null) {
				arrangeMap.put(examArrange.getArrangeId(), examArrange.getSubject().getName());
			}else{
				if (groupMap.get(examArrange.getGroup().getGroupId()) != null) {
					groupMap.put(examArrange.getGroup().getGroupId(), groupMap.get(examArrange.getGroup().getGroupId())+examArrange.getSubject().getName());
				}else{
					groupMap.put(examArrange.getGroup().getGroupId(), examArrange.getSubject().getName());
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
			i++;
		}
		int n=0;
		for (Integer key : groupMap.keySet()) {
			groupSeqHeadMap.put(key,groupSeqStrings[n]);
			n++;
		}
		List<ExamineeRoomArrangeVo> examineeRoomArrangeVos =  examineeRoomArrangeMapper.findExamineeRoomArrangeByRoomId(roomId, examId);		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String,Object>>();//key:studentId  有序 
		for (ExamineeRoomArrangeVo vo : examineeRoomArrangeVos) {
			if (map.containsKey(vo.getStudentId())) {
				Map<String, Object> innerMap = map.get(vo.getStudentId());
				if (vo.getGroupId() == 0) {
					innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getStudentName());
					innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getRegNo());
				}else {
					if (innerMap.containsKey(groupHeadMap.get(vo.getGroupId()))
							&&(!innerMap.get(groupHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getStudentName()) 
							||!innerMap.get(groupSeqHeadMap.get(vo.getGroupId())).toString().equalsIgnoreCase(vo.getRegNo()))
							 ) {
						innerMap.put(groupHeadMap.get(vo.getGroupId()),innerMap.get(groupHeadMap.get(vo.getGroupId()))+";"+vo.getStudentName() );
						innerMap.put(groupSeqHeadMap.get(vo.getGroupId()),innerMap.get(groupSeqHeadMap.get(vo.getGroupId()))+";"+vo.getRegNo());
					}else {
						innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getStudentName());
						innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getRegNo());
					}
				}														
			}else{
				Map<String, Object> innerMap = new HashMap<String, Object>();
				innerMap.put("roomName", vo.getRoomName());
				innerMap.put("seq", vo.getSeq());			
				if(vo.getGroupId() == 0){
				innerMap.put(arrangeHeadMap.get(vo.getArrangeId()), vo.getStudentName());
				innerMap.put(arrangeSeqHeadMap.get(vo.getArrangeId()),vo.getRegNo());
				}else {
				innerMap.put(groupHeadMap.get(vo.getGroupId()), vo.getStudentName());
				innerMap.put(groupSeqHeadMap.get(vo.getGroupId()), vo.getRegNo());	
				}
				map.put(vo.getStudentId(), innerMap);
			}								
		}		
		for (Integer studentId : map.keySet()) {
			dataList.add(map.get(studentId));
		}					
		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
		Map<String, Object> roomNameMap = new HashMap<String, Object>();
		roomNameMap.put("prop", "roomName");
		roomNameMap.put("label", "考场");
		Map<String, Object> seqMap = new HashMap<String, Object>();
		seqMap.put("prop", "seq");
		seqMap.put("label", "座位号");	
		headList.add(roomNameMap);
		headList.add(seqMap);		
		for (Integer arrangeId : arrangeHeadMap.keySet()) {
			  arrangeHeadMap.get(arrangeId);
			Map<String, Object> headMap = new HashMap<String, Object>();
			headMap.put("prop", arrangeHeadMap.get(arrangeId));
			headMap.put("label", arrangeMap.get(arrangeId));
			Map<String, Object> headSeqMap = new HashMap<String, Object>();
			headSeqMap.put("prop", arrangeSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "准考证号");
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
			headSeqMap.put("prop", groupSeqHeadMap.get(arrangeId));
			headSeqMap.put("label", "准考证号");
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

	//考生平均分配到考场
	private void autoModeOne(List<Examinee> examinees,List<ExamRoomAllocate> examRoomAllocates){		
		int examineeCount = examinees.size();
		int examRoomCount = examRoomAllocates.size();
		int averageExaminee = examineeCount/examRoomCount;//每个考场分配多少考生
		int remainExaminee = examineeCount%examRoomCount;//剩余的考生
		//将所有考生排序
		Collections.sort(examinees);
		//todo 同班同学不相临
		swap(examinees);
        int i=0;
        for (ExamRoomAllocate era:
        examRoomAllocates) {
            List<Examinee> averageExaminees = examinees.subList(i*averageExaminee,(i+1)*averageExaminee);
            int seq=1;
            for (Examinee examinee:
            averageExaminees) {
                ExamineeRoomArrange examineeRoomArrange = new ExamineeRoomArrange();
                examineeRoomArrange.setExaminee(examinee);
                examineeRoomArrange.setExamRoomAllocate(era);
                examineeRoomArrange.setSeq(seq);
                examineeRoomArrangeDao.save(examineeRoomArrange);
                seq++;
            }
            i++;
        }
        List<Examinee> remainExaminees = new ArrayList<Examinee>();
        remainExaminees =     examinees.subList(examinees.size()-remainExaminee,examinees.size());
        for (int j=0;j<remainExaminee;j++){
            ExamineeRoomArrange examineeRoomArrange = new ExamineeRoomArrange();
            examineeRoomArrange.setSeq(averageExaminee+1);
            examineeRoomArrange.setExamRoomAllocate(examRoomAllocates.get(j));
            examineeRoomArrange.setExaminee(remainExaminees.get(j));
            examineeRoomArrangeDao.save(examineeRoomArrange);

        }	
	}
	//按考场容量分配
	private void autoModeTwo(List<Examinee> examinees,List<ExamRoomAllocate> examRoomAllocates){		
        //int examineeCount = examinees.size();
        //将所有考生排序
        Collections.sort(examinees);
        swap(examinees);
        int m=0;
        int count=1;
        ExamRoomAllocate era = examRoomAllocates.get(m);
        for (Examinee examinee:
             examinees) {
         if(count>era.getCapacity()){
             m++;
             era=examRoomAllocates.get(m);
             count = 1;
         }
         ExamineeRoomArrange examineeRoomArrange = new ExamineeRoomArrange();
         examineeRoomArrange.setExaminee(examinee);
         examineeRoomArrange.setSeq(count);
         examineeRoomArrange.setExamRoomAllocate(era);
         examineeRoomArrangeDao.save(examineeRoomArrange);
         count++;
        }	
		
	}
	//同班同学不相邻
	private void swap(List<Examinee> examinees){
		
		for (int i = 0; i < examinees.size()-2; i++) {
			int j = i;
			if (examinees.get(i+1).getStudent().getSchoolClass().equals(examinees.get(i).getStudent().getSchoolClass())) {			
				while((j+2)<examinees.size() && examinees.get(i+1).getStudent().getSchoolClass().equals(examinees.get(j+2).getStudent().getSchoolClass())){
					j++;
				}
				if((j+2)<examinees.size())
					CollectionsUtil.swap(examinees, i+1, j+2);
			}						
		}				
	}
	
	

	
	
	
	
	
	
	

	
	
	

	
	
}
