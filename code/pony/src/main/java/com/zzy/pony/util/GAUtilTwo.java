package com.zzy.pony.util;

import java.util.*;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.TeacherSubjectVo;



public class GAUtilTwo {

	/**
	  * @Author : Administrator
	  * @Description : 默认按照5*8的课程表计算
	 1	9   17	25	33
	 2	10	18	26	34
	 3	11	19	27	35
	 4	12	20	28	36
	 5	13	21	29	37
	 6	14	22	30	38
	 7	15	23	31	39
	 8	16	24	32	40
	*/

	public static void getPre(List<ArrangeVo> preArrangeVos, Map<Integer,Map<Integer,List<Integer>>> classMap, Map<Integer,List<Integer>> teacherMap,
							  Map<Integer,Map<Integer,List<Integer>>> teacherClassMap,
							  Map<Integer,Map<Integer,List<Integer>>> preTeacherMap, Map<Integer,Set<Integer>> classAlreadyMap){
        for (ArrangeVo vo:
                preArrangeVos) {
            if (classMap.containsKey(vo.getClassId())){
                Map<Integer,List<Integer>> innerClassMap =  classMap.get(vo.getClassId());
                if (innerClassMap.containsKey(vo.getTeacherId())){
                    innerClassMap.get(vo.getTeacherId()).add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
                }else{
                    List<Integer> innerClassList = new ArrayList<Integer>();
                    innerClassList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
                    innerClassMap.put(vo.getTeacherId(),innerClassList);
                }
            }else{
                Map<Integer,List<Integer>> innerClassMap = new HashMap<Integer, List<Integer>>();
                List<Integer> innerClassList = new ArrayList<Integer>();
                innerClassList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
                innerClassMap.put(vo.getTeacherId(),innerClassList);
                classMap.put(vo.getClassId(),innerClassMap);
            }
			if (teacherClassMap.containsKey(vo.getTeacherId())){
				Map<Integer,List<Integer>> innerTeacherMap =  teacherClassMap.get(vo.getTeacherId());
				if (innerTeacherMap.containsKey(vo.getClassId())){
					innerTeacherMap.get(vo.getClassId()).add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
				}else{
					List<Integer> innerTeacherList = new ArrayList<Integer>();
					innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
					innerTeacherMap.put(vo.getClassId(),innerTeacherList);
				}
			}else{
				Map<Integer,List<Integer>> innerTeacherMap = new HashMap<Integer, List<Integer>>();
				List<Integer> innerTeacherList = new ArrayList<Integer>();
				innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
				innerTeacherMap.put(vo.getClassId(),innerTeacherList);
				teacherClassMap.put(vo.getTeacherId(),innerTeacherMap);
			}
			if (teacherMap.containsKey(vo.getTeacherId())){
				List<Integer> innerList = teacherMap.get(vo.getTeacherId());
				innerList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
			}else{
				List<Integer> innerList = new ArrayList<Integer>();
				innerList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
				teacherMap.put(vo.getTeacherId(),innerList);
			}
			if (preTeacherMap.containsKey(vo.getTeacherId())){
				Map<Integer,List<Integer>> innerTeacherMap =  preTeacherMap.get(vo.getTeacherId());
				if (innerTeacherMap.containsKey(vo.getClassId())){
					innerTeacherMap.get(vo.getClassId()).add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
				}else{
					List<Integer> innerTeacherList = new ArrayList<Integer>();
					innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
					innerTeacherMap.put(vo.getClassId(),innerTeacherList);
				}
			}else{
				Map<Integer,List<Integer>> innerTeacherMap = new HashMap<Integer, List<Integer>>();
				List<Integer> innerTeacherList = new ArrayList<Integer>();
				innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(),vo.getPeriodSeq()));
				innerTeacherMap.put(vo.getClassId(),innerTeacherList);
				preTeacherMap.put(vo.getTeacherId(),innerTeacherMap);
			}
        }
		for (int classId:
		classMap.keySet()) {
			Map<Integer,List<Integer>> innerClassMap = classMap.get(classId);
			Set<Integer> set  = new HashSet<Integer>();
			for (int teacherId:
				 innerClassMap.keySet()) {
				set.addAll(innerClassMap.get(teacherId));
			}
			classAlreadyMap.put(classId,set);
		}

	}

	public static void getTeacherSubject(List<TeacherSubjectVo> teacherSubjectVos,Map<Integer,Map<Integer,Integer>> classTSMap,Map<Integer,Map<Integer,Integer>> teacherTSMap,
    		Map<Integer,Integer> teacherSubjectMap,Map<Integer,Map<Integer,Integer>> subjectTeacherMap){
        for (TeacherSubjectVo vo:
        teacherSubjectVos) {
        	//classTSMap初始化
            if (classTSMap.containsKey(vo.getClassId())){
                classTSMap.get(vo.getClassId()).put(vo.getTeacherId(),Integer.valueOf(vo.getWeekArrange()));
            }else{
                Map<Integer,Integer> innerClassMap = new HashMap<Integer, Integer>();
                innerClassMap.put(vo.getTeacherId(),Integer.valueOf(vo.getWeekArrange()));
                classTSMap.put(vo.getClassId(),innerClassMap);
            }
            //teacherTSMap初始化
            if (teacherTSMap.containsKey(vo.getTeacherId())){
                teacherTSMap.get(vo.getTeacherId()).put(vo.getClassId(),Integer.valueOf(vo.getWeekArrange()));
            }else{
                Map<Integer,Integer> innerTeacherMap = new HashMap<Integer, Integer>();
                innerTeacherMap.put(vo.getClassId(),Integer.valueOf(vo.getWeekArrange()));
                teacherTSMap.put(vo.getTeacherId(),innerTeacherMap);
            }
			//teacherSubjectMap初始化
			if (!teacherSubjectMap.containsKey(vo.getTeacherId())){
            	teacherSubjectMap.put(vo.getTeacherId(),vo.getSubjectId());
			}
			//subjectTeacherMap初始化
			if (subjectTeacherMap.containsKey(vo.getClassId())){
				subjectTeacherMap.get(vo.getClassId()).put(vo.getSubjectId(),vo.getTeacherId());
            }else{
                Map<Integer,Integer> innerSubjectTeacherMap = new HashMap<Integer, Integer>();
                innerSubjectTeacherMap.put(vo.getSubjectId(),vo.getTeacherId());
                subjectTeacherMap.put(vo.getClassId(),innerSubjectTeacherMap);
            }
        }

    }
	public static void getSubjectList(List<Subject> subjects,List<Integer> sigList,List<Integer> impList,List<Integer> comList) {
		for (Subject subject:
			 subjects) {
			if (subject.getImportance()==Constants.SUBJECT_SIGNIFICANT){
				sigList.add(subject.getSubjectId());
			}
			if (subject.getImportance()==Constants.SUBJECT_IMPORTANT){
				impList.add(subject.getSubjectId());
			}
			if (subject.getImportance()==Constants.SUBJECT_COMMON){
				comList.add(subject.getSubjectId());
			}
		}
	}
	
	public static Map<Integer,Integer> sortBySubject(Map<Integer,Integer> classTSInnerMap,Map<Integer, Integer> classSubjectTeacherMap,List<Subject> subjects){
		
		Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
		for (Subject subject : subjects) {
			if (classSubjectTeacherMap.get(subject.getSubjectId())!= null) {
				int teacherId = classSubjectTeacherMap.get(subject.getSubjectId());
				if (classTSInnerMap.containsKey(teacherId)) {
					result.put(teacherId, classTSInnerMap.get(teacherId));
				}
			}
		}
		return result;
		
	}



}
