package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.CombineAndRotationVo;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.TeacherSubjectVo;

/**
 * @author WANGCHAO262
 *
 */
public class GAUtilTwo {

	/**
	 * @Author : Administrator
	 * @Description : 默认按照5*8的课程表计算 1 9 17 25 33 2 10 18 26 34 3 11 19 27 35 4 12 20
	 *              28 36 5 13 21 29 37 6 14 22 30 38 7 15 23 31 39 8 16 24 32 40
	 */

	public static void getPre(List<ArrangeVo> preArrangeVos, Map<Integer, Map<Integer, List<Integer>>> classMap,
			Map<Integer, List<Integer>> teacherMap, Map<Integer, Map<Integer, List<Integer>>> teacherClassMap,
			Map<Integer, Map<Integer, List<Integer>>> preTeacherMap, Map<Integer, Set<Integer>> classAlreadyMap) {
		for (ArrangeVo vo : preArrangeVos) {
			if (classMap.containsKey(vo.getClassId())) {
				Map<Integer, List<Integer>> innerClassMap = classMap.get(vo.getClassId());
				if (innerClassMap.containsKey(vo.getTeacherId())) {
					innerClassMap.get(vo.getTeacherId())
							.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				} else {
					List<Integer> innerClassList = new ArrayList<Integer>();
					innerClassList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
					innerClassMap.put(vo.getTeacherId(), innerClassList);
				}
			} else {
				Map<Integer, List<Integer>> innerClassMap = new HashMap<Integer, List<Integer>>();
				List<Integer> innerClassList = new ArrayList<Integer>();
				innerClassList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				innerClassMap.put(vo.getTeacherId(), innerClassList);
				classMap.put(vo.getClassId(), innerClassMap);
			}
			if (teacherClassMap.containsKey(vo.getTeacherId())) {
				Map<Integer, List<Integer>> innerTeacherMap = teacherClassMap.get(vo.getTeacherId());
				if (innerTeacherMap.containsKey(vo.getClassId())) {
					innerTeacherMap.get(vo.getClassId())
							.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				} else {
					List<Integer> innerTeacherList = new ArrayList<Integer>();
					innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
					innerTeacherMap.put(vo.getClassId(), innerTeacherList);
				}
			} else {
				Map<Integer, List<Integer>> innerTeacherMap = new HashMap<Integer, List<Integer>>();
				List<Integer> innerTeacherList = new ArrayList<Integer>();
				innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				innerTeacherMap.put(vo.getClassId(), innerTeacherList);
				teacherClassMap.put(vo.getTeacherId(), innerTeacherMap);
			}
			if (teacherMap.containsKey(vo.getTeacherId())) {
				List<Integer> innerList = teacherMap.get(vo.getTeacherId());
				innerList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
			} else {
				List<Integer> innerList = new ArrayList<Integer>();
				innerList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				teacherMap.put(vo.getTeacherId(), innerList);
			}
			if (preTeacherMap.containsKey(vo.getTeacherId())) {
				Map<Integer, List<Integer>> innerTeacherMap = preTeacherMap.get(vo.getTeacherId());
				if (innerTeacherMap.containsKey(vo.getClassId())) {
					innerTeacherMap.get(vo.getClassId())
							.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				} else {
					List<Integer> innerTeacherList = new ArrayList<Integer>();
					innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
					innerTeacherMap.put(vo.getClassId(), innerTeacherList);
				}
			} else {
				Map<Integer, List<Integer>> innerTeacherMap = new HashMap<Integer, List<Integer>>();
				List<Integer> innerTeacherList = new ArrayList<Integer>();
				innerTeacherList.add(WeekSeqUtil.getWeekPeriod(vo.getWeekdayId(), vo.getPeriodSeq()));
				innerTeacherMap.put(vo.getClassId(), innerTeacherList);
				preTeacherMap.put(vo.getTeacherId(), innerTeacherMap);
			}
		}
		for (int classId : classMap.keySet()) {
			Map<Integer, List<Integer>> innerClassMap = classMap.get(classId);
			Set<Integer> set = new HashSet<Integer>();
			for (int teacherId : innerClassMap.keySet()) {
				set.addAll(innerClassMap.get(teacherId));
			}
			classAlreadyMap.put(classId, set);
		}

	}

	
	public static void rotationHandle(List<TeacherSubjectVo> teacherSubjectVos,Map<String, Integer> rotationMap) {
		//key rotationId value weekarrange
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<TeacherSubjectVo> removeVos = new ArrayList<TeacherSubjectVo>();
		for (TeacherSubjectVo vo : teacherSubjectVos) {
			for (String key : rotationMap.keySet()) {
				int teacherId = Integer.valueOf(key.substring(0, 4));
				int classId = Integer.valueOf(key.substring(4, 7));
				if (vo.getClassId() == classId && vo.getTeacherId() == teacherId) {				
					map.put(rotationMap.get(key),vo.getWeekArrange());
					removeVos.add(vo);	
				}		
			}
		}
		teacherSubjectVos.removeAll(removeVos);
		for (Integer rotationId : map.keySet()) {
			TeacherSubjectVo vo = new TeacherSubjectVo();
			int classId = 0;
			for (String key : rotationMap.keySet()) {
				if (rotationMap.get(key) == rotationId) {
					classId = Integer.valueOf(key.substring(4, 7));
					break;
				}
			}
			vo.setClassId(classId);
			vo.setTeacherId(Integer.valueOf("99"+rotationId));
			vo.setWeekArrange(map.get(rotationId));
			teacherSubjectVos.add(vo);	
		}
		
		
	}
	
	
	public static void getTeacherSubject(List<TeacherSubjectVo> teacherSubjectVos,
			Map<Integer, Map<Integer, Integer>> classTSMap, Map<Integer, Map<Integer, Integer>> teacherTSMap,
			Map<Integer, Integer> teacherSubjectMap, Map<Integer, Map<Integer, Integer>> subjectTeacherMap) {
		for (TeacherSubjectVo vo : teacherSubjectVos) {
			// classTSMap初始化
			if (classTSMap.containsKey(vo.getClassId())) {	
				classTSMap.get(vo.getClassId()).put(vo.getTeacherId(), Integer.valueOf(vo.getWeekArrange()));			
			} else {
				Map<Integer, Integer> innerClassMap = new HashMap<Integer, Integer>();
				innerClassMap.put(vo.getTeacherId(), Integer.valueOf(vo.getWeekArrange()));
				classTSMap.put(vo.getClassId(), innerClassMap);
			}
			// teacherTSMap初始化
			if (teacherTSMap.containsKey(vo.getTeacherId())) {
				teacherTSMap.get(vo.getTeacherId()).put(vo.getClassId(), Integer.valueOf(vo.getWeekArrange()));
			} else {
				Map<Integer, Integer> innerTeacherMap = new HashMap<Integer, Integer>();
				innerTeacherMap.put(vo.getClassId(), Integer.valueOf(vo.getWeekArrange()));
				teacherTSMap.put(vo.getTeacherId(), innerTeacherMap);
			}
			// teacherSubjectMap初始化
			if (!teacherSubjectMap.containsKey(vo.getTeacherId())) {
				teacherSubjectMap.put(vo.getTeacherId(), vo.getSubjectId());
			}
			// subjectTeacherMap初始化
			if (subjectTeacherMap.containsKey(vo.getClassId())) {
				subjectTeacherMap.get(vo.getClassId()).put(vo.getSubjectId(), vo.getTeacherId());
			} else {
				Map<Integer, Integer> innerSubjectTeacherMap = new HashMap<Integer, Integer>();
				innerSubjectTeacherMap.put(vo.getSubjectId(), vo.getTeacherId());
				subjectTeacherMap.put(vo.getClassId(), innerSubjectTeacherMap);
			}
		}

	}

	public static void getSubjectList(List<Subject> subjects, List<Integer> sigList, List<Integer> impList,
			List<Integer> comList) {
		for (Subject subject : subjects) {
			if (subject.getImportance() == Constants.SUBJECT_SIGNIFICANT) {
				sigList.add(subject.getSubjectId());
			}
			if (subject.getImportance() == Constants.SUBJECT_IMPORTANT) {
				impList.add(subject.getSubjectId());
			}
			if (subject.getImportance() == Constants.SUBJECT_COMMON) {
				comList.add(subject.getSubjectId());
			}
		}
	}

	public static Map<Integer, Integer> sortBySubject(Map<Integer, Integer> classTSInnerMap,
			Map<Integer, Integer> classSubjectTeacherMap, List<Subject> subjects, List<TeacherSubjectVo> voSeq) {

		Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
		// 按照科目顺序排
		/*
		 * for (Subject subject : subjects) { if
		 * (classSubjectTeacherMap.get(subject.getSubjectId())!= null) { int teacherId =
		 * classSubjectTeacherMap.get(subject.getSubjectId()); if
		 * (classTSInnerMap.containsKey(teacherId)) { result.put(teacherId,
		 * classTSInnerMap.get(teacherId)); } } }
		 */
		// 按照科目课程数量排
		/*
		 * List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer,
		 * Integer>>(classTSInnerMap.entrySet()); // 通过比较器来实现排序 Collections.sort(list,
		 * new Comparator<Map.Entry<Integer, Integer>>() {
		 * 
		 * @Override public int compare(Map.Entry<Integer, Integer> o1,
		 * Map.Entry<Integer, Integer> o2) { // 降序排序 return
		 * o2.getValue().compareTo(o1.getValue()); } }); for(int i=0;i<list.size();i++)
		 * { result.put(list.get(i).getKey(), list.get(i).getValue()); }
		 */

		// 语文数学英语保证排在首位 先按照总课程数量安排,后按照本班课程数量排
		for (TeacherSubjectVo teacherSubjectVo : voSeq) {
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("语文")
					&& classTSInnerMap.containsKey(teacherSubjectVo.getTeacherId())) {
				result.put(teacherSubjectVo.getTeacherId(), classTSInnerMap.get(teacherSubjectVo.getTeacherId()));
			}
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("数学")
					&& classTSInnerMap.containsKey(teacherSubjectVo.getTeacherId())) {
				result.put(teacherSubjectVo.getTeacherId(), classTSInnerMap.get(teacherSubjectVo.getTeacherId()));
			}
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("英语")
					&& classTSInnerMap.containsKey(teacherSubjectVo.getTeacherId())) {
				result.put(teacherSubjectVo.getTeacherId(), classTSInnerMap.get(teacherSubjectVo.getTeacherId()));
			}
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("政治")
					&& classTSInnerMap.containsKey(teacherSubjectVo.getTeacherId())) {
				result.put(teacherSubjectVo.getTeacherId(), classTSInnerMap.get(teacherSubjectVo.getTeacherId()));
			}
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("信息技术")
					&& classTSInnerMap.containsKey(teacherSubjectVo.getTeacherId())) {
				result.put(teacherSubjectVo.getTeacherId(), classTSInnerMap.get(teacherSubjectVo.getTeacherId()));
			}
		}

		for (TeacherSubjectVo teacherSubjectVo : voSeq) {
			if (classTSInnerMap.containsKey(teacherSubjectVo.getTeacherId())
					&& !(teacherSubjectVo.getSubjectName().equalsIgnoreCase("语文")
							|| teacherSubjectVo.getSubjectName().equalsIgnoreCase("数学")
							|| teacherSubjectVo.getSubjectName().equalsIgnoreCase("英语"))) {
				result.put(teacherSubjectVo.getTeacherId(), classTSInnerMap.get(teacherSubjectVo.getTeacherId()));
			}
		}
		//走班
		/*for (Integer teacherId : classTSInnerMap.keySet()) {
			if (!result.containsKey(teacherId)) {
				result.put(teacherId, classTSInnerMap.get(teacherId));
			}
		}*/
		

		return result;

	}

	public static Map<Integer, List<Integer>> getGradeNoCourse(int gradeId, List<GradeNoCourseVo> gradeNoCourseVos) {
		// key classId
		Map<Integer, List<Integer>> result = new LinkedHashMap<Integer, List<Integer>>();
		for (GradeNoCourseVo gradeNoCourseVo : gradeNoCourseVos) {
			if (gradeNoCourseVo.getGradeId() == gradeId && gradeNoCourseVo.getGradeClassIds() != null
					&& gradeNoCourseVo.getGradeClassIds().size() > 0) {
				for (Integer classId : gradeNoCourseVo.getGradeClassIds()) {
					if (result.containsKey(classId)) {
						result.get(classId).add(WeekSeqUtil.getWeekPeriod(gradeNoCourseVo.getWeekdayId(),
								gradeNoCourseVo.getLessonPeriodSeq()));
					} else {
						List<Integer> innerList = new ArrayList<Integer>();
						innerList.add(WeekSeqUtil.getWeekPeriod(gradeNoCourseVo.getWeekdayId(),
								gradeNoCourseVo.getLessonPeriodSeq()));
						result.put(classId, innerList);
					}

				}
			}
		}
		return result;
	}

	/**
	 * @param map  key rotationId  value subjectId
	 * @return
	 */
	public static Map<String, Integer> getArrangeRotation(List<CombineAndRotationVo> combineAndRotationVos,Map<Integer, List<Integer>> map) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (CombineAndRotationVo vo : combineAndRotationVos) {
			String teacherId = String.format("%04d", vo.getTeacherId());
			String classId = String.format("%03d", vo.getClassId());
			result.put(teacherId + classId,vo.getRotationId());
			
			if (map.containsKey(Integer.valueOf("99"+vo.getRotationId()))) {
				map.get(Integer.valueOf("99"+vo.getRotationId())).add(vo.getSubjectId());					
			}else {
				List<Integer> innerList = new ArrayList<Integer>();
				innerList.add(vo.getSubjectId());
				map.put(Integer.valueOf("99"+vo.getRotationId()), innerList);
			}
			
		}
		return result;
	}

	// week:seq
	public static List<String> getAvailableWeekseq(List<LessonPeriod> lessonPeriods,
			List<LessonArrange> lessonArranges) {
		List<String> result = new ArrayList<String>();
		for (int i = 1; i <= 5; i++) {
			for (LessonPeriod lp : lessonPeriods) {
				if (lp.getSeq() <= 8) {
					result.add(i + ";" + lp.getPeriodId());
				}
			}
		}
		for (LessonArrange la : lessonArranges) {
			String key = la.getWeekDay() + ";" + la.getLessonPeriod().getPeriodId();
			if (result.contains(key)) {
				result.remove(key);
			}
		}
		return result;

	}

	/**
	 * teacherIds的大小应该和weekSeqs一致
	 * 
	 * @param teacherIds
	 * @param weekSeqs
	 * @return 随机的一种排列
	 */
	public static List<List<String>> availableArrange(List<Integer> initTeacherIds, List<String> initWeekSeqs,
			List<String> conflictList) {
		/*
		 * List<Integer> teacherIds = new ArrayList<Integer>(); List<String> weekSeqs =
		 * new ArrayList<String>(); for (Integer key : initTeacherIds) {
		 * teacherIds.add(key); } for (String key : initWeekSeqs) { weekSeqs.add(key); }
		 */
		List<List<String>> result = new ArrayList<List<String>>();
		int size = initTeacherIds.size();
		Integer[] dataList = new Integer[size];
		for (int i = 0; i < size; i++) {
			dataList[i] = i;
		}
		List<List<Integer>> arrangements = arrangementSelect(dataList, size);
		for (List<Integer> outerList : arrangements) {
			for (List<Integer> innerList : arrangements) {
				List<String> innerResultList = new ArrayList<String>();
				for (int j = 0; j < size; j++) {
					String weekSeqTeacher = initWeekSeqs.get(outerList.get(j)) + ";"
							+ initTeacherIds.get(innerList.get(j));
					if (conflictList.contains(weekSeqTeacher)) {
						break;
					} else {
						innerResultList.add(weekSeqTeacher);
					}
				}
				if (innerResultList.size() == size) {
					result.add(innerResultList);
				}
			}
		}
		if (result.size() == 0) {
			System.out.println("aa");
		}

		/*
		 * Random random = new Random(); while(result.size()<size) { int teacherId =
		 * teacherIds.get(random.nextInt(teacherIds.size())); String weekSeq =
		 * weekSeqs.get(random.nextInt(weekSeqs.size()));
		 * teacherIds.remove(Integer.valueOf(teacherId)); weekSeqs.remove(weekSeq); if
		 * (conflictList.contains(weekSeq+";"+teacherId)) { continue; }else {
		 * result.add(weekSeq+";"+teacherId); } }
		 */
		return result;

	}

	/**
	 * 排列选择（从列表中选择n个排列）
	 * 
	 * @param dataList
	 *            待选列表
	 * @param n
	 *            选择个数
	 */
	public static List<List<Integer>> arrangementSelect(Integer[] dataList, int n) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		arrangementSelect(dataList, new Integer[n], 0, result);
		return result;
	}

	/**
	 * 排列选择
	 * 
	 * @param dataList
	 *            待选列表
	 * @param resultList
	 *            前面（resultIndex-1）个的排列结果
	 * @param resultIndex
	 *            选择索引，从0开始
	 */
	public static void arrangementSelect(Integer[] dataList, Integer[] resultList, int resultIndex,
			List<List<Integer>> result) {
		int resultLen = resultList.length;
		if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
			// System.out.println(Arrays.asList(resultList));
			List<Integer> dest = new ArrayList<Integer>(Arrays.asList(resultList));
			result.add(dest);
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < dataList.length; i++) {
			// 判断待选项是否存在于排列结果中
			boolean exists = false;
			for (int j = 0; j < resultIndex; j++) {
				if (dataList[i].equals(resultList[j])) {
					exists = true;
					break;
				}
			}
			if (!exists) { // 排列结果不存在该项，才可选择
				resultList[resultIndex] = dataList[i];
				arrangementSelect(dataList, resultList, resultIndex + 1, result);
			}
		}
	}

}
