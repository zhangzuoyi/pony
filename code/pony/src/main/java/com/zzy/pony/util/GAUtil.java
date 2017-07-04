package com.zzy.pony.util;

import java.util.*;
import java.util.Map.Entry;

import javax.persistence.Cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.hibernate.id.IntegralDataTypeHolder;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.ArrangeRotation;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.CombineAndRotationVo;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.SubjectNoCourseVo;
import com.zzy.pony.vo.TeacherNoCourseVo;
import com.zzy.pony.vo.TeacherSubjectVo;

public class GAUtil {

    private static final int MAX_ATTEMPT = 800;

	
	
	/*** 
	* <p>Description:获取候选数组,bit为位数,list为id集合，如teacherId flag是否增加空元素，在teacher和subject时为true，用于班级不排课设置</p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 上午10:22:30
	*/
	public static String[] getCandidateStrings(List<Integer> list ,int bit,Boolean flag){
		if (!flag) {
			String[] result  = new String[list.size()];
			for (int i = 0; i < result.length; i++) {
				String idStr = String.format("%0"+bit+"d", list.get(i));
				System.out.println(idStr);
				result[i] =idStr;
			}
			return result;
		}else{
			String[] result  = new String[list.size()+1];
			for (int i = 0; i < result.length-1; i++) {
				String idStr = String.format("%0"+bit+"d", list.get(i));
				System.out.println(idStr);
				result[i] =idStr;
			}
			result[result.length-1] = String.format("%0"+bit+"d", Integer.valueOf(0));
			return result;
		}						
	}
	/*** 
	* <p>Description: 获取班级老师与课时的映射关系  key:teacherId+classId+subjectId value:weekArrange</p>
	* @author  WANGCHAO262
	* @date  2017年4月10日 下午3:23:39
	* 
	* modify weekArrange可以为2+1形式，表示2次独立的课，1次连排课,将返回类型修改
	* 
	*/
	public static Map<String,String> getTeacherSubjectweekArrange(List<TeacherSubjectVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (TeacherSubjectVo teacherSubjectVo : list) {			
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;			
			if (teacherSubjectVo.getWeekArrange() == null) {
				teacherSubjectVo.setWeekArrange("0");
			}
			result.put(teacherId+classId+subjectId, teacherSubjectVo.getWeekArrange());
		}		
		return result;
		
	}
	
	/*** 
	* <p>Description:获取老师任课班级Map </p>
	* @author  WANGCHAO262
	* @date  2017年6月22日 下午12:07:16
	*/
	public static Map<String, List<String>> getTeacherClassMap(List<TeacherSubjectVo> list){
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		
		for (TeacherSubjectVo teacherSubjectVo : list) {			
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			if (teacherSubjectVo.getWeekArrange() != null) {			
				if (result.containsKey(teacherId)) {
					result.get(teacherId).add(classId);				
				}else {
					List<String> classIds = new ArrayList<String>();
					classIds.add(classId);
					result.put(teacherId, classIds);
				}							
			}
		}
		
		return result;		
	}
	
	/**
	 * @param list   key teacherId+subjectId value(key classId value count(计数，初始为0))
	 * @return
	 */
	public static Map<String, Map<String, Integer>>	 getTeacherSubjectClass(List<TeacherSubjectVo> list){
		Map<String, Map<String, Integer>> result =  new HashMap<String, Map<String,Integer>>();
		for (TeacherSubjectVo teacherSubjectVo : list) {
			 
				
			
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;	
			String key = teacherId+subjectId;
			if (result.containsKey(key)) {
				result.get(key).put(classId, 0);
			}else {
				Map<String, Integer> innerMap =  new HashMap<String, Integer>();
				innerMap.put(classId, 0);
				result.put(key, innerMap);
			}
			}
		
		
		return result;
	}
	/**
	 * @param list   key teacherId+subjectId value(list<integer> 星期)
	 * @return
	 */
	public static Map<String, Set<Integer>>	 getTeacherSubjectRegularClass(List<TeacherSubjectVo> list){
		Map<String, Set<Integer>> result =  new HashMap<String,Set<Integer>>();
		Random random = new Random();
		for (TeacherSubjectVo teacherSubjectVo : list) {
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("语文")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("数学")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("英语")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("物理")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("化学")
					){						
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;	
			String key = teacherId+subjectId;
			int weekarranges = 0;
			String weekarrange = teacherSubjectVo.getWeekArrange();
			if (weekarrange.indexOf("+")>0) {
				String[] a = weekarrange.split("\\+");
				weekarranges =  Integer.valueOf(a[0]) + Integer.valueOf(a[1]);				
			}else {
				weekarranges = Integer.valueOf(weekarrange);
			}	
			Set<Integer> weekSet = new HashSet<Integer>();
			while(weekSet.size()<weekarranges){
				weekSet.add(random.nextInt(5)+1);
			}		
			result.put(key, weekSet);			
		}
		}
		
		return result;
	}
	
	
	/**
	 * @param list   key teacherId+subjectId value classId   获取不平课的班级，即某个老师在A班上3节课，在B班上4节课
	 * @return
	 */
	public static Map<String, Map<String, Integer>>	 getTeacherSubjectIrregularClass(List<TeacherSubjectVo> list){
		Map<String, Map<String, Integer>> tmp =  new HashMap<String, Map<String,Integer>>();
		Map<String, Map<String, Integer>> result =  new HashMap<String, Map<String,Integer>>();

		for (TeacherSubjectVo teacherSubjectVo : list) {
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;	
			int weekarranges = 0;
			String weekarrange = teacherSubjectVo.getWeekArrange();
			if (weekarrange.indexOf("+")>0) {
				String[] a = weekarrange.split("\\+");
				weekarranges =  Integer.valueOf(a[0]) + 2*Integer.valueOf(a[1]);				
			}else {
				weekarranges = Integer.valueOf(weekarrange);
			}						
			String key = teacherId+subjectId;
			if (tmp.containsKey(key)) {
				for (String classKey : tmp.get(key).keySet()) {
					if (tmp.get(key).get(classKey) !=  weekarranges) {
						if (!result.containsKey(key)) {
							Map<String, Integer> innerMap = new HashMap<String, Integer>();
							innerMap.put(classId, 0);
							innerMap.put(classKey, 0);
							result.put(key, innerMap);							
						}else {							
							result.get(key).put(classId, 0);							
						}																	
					}
				}  
				
				tmp.get(key).put(classId, weekarranges);
				
			}else {
				Map<String, Integer> innerMap =  new HashMap<String, Integer>();								
				innerMap.put(classId, weekarranges);
				tmp.put(key, innerMap);
			}
		}
		
		return result;
	}
	
	/*** 
	* <p>Description:
	* input   key:teacherId+classId+subjectId value weekArrange  rotationMap key: teacherId+classId+subjectId,value:rotationId
	* output  key:classId value( key:teacherId+subjectId value:weekArrange) </p>
	* @author  WANGCHAO262
	* @date  2017年5月3日 下午2:14:46
	* @add 增加支持走课   其中走课key  R+%05d 其中为rotationId
	* @add 增加支持合课   其中合课key  C+%05d 其中为combineId
     * @add 增加走课与合课是相同的逻辑  将所有R的先排，C的只排完全不出现在R中的
	*/
	public static Map<String, Map<String, String>> getClassTeacherSubjectweekArrange(Map<String,String> map,Map<String, Integer> rotationMap,Map<String, Integer> combineMap){
		Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();
		for (String key : map.keySet()) {
			String classId = key.substring(4, 7);//key
			if (result.containsKey(classId)) {
				Map<String, String> innerMap = result.get(classId);
				if (rotationMap.get(key)!=null) {
					if (!innerMap.containsKey("R"+String.format("%05d",rotationMap.get(key) ))) {
						innerMap.put("R"+String.format("%05d",rotationMap.get(key) ), map.get(key));
					}
				}else if (combineMap.get(key)!= null) {
					innerMap.put("C"+String.format("%05d",combineMap.get(key) ), map.get(key));
				}else{
					innerMap.put(key.substring(0, 4)+key.substring(7, 9), map.get(key));

				}													
			}else{
				Map<String, String> innerMap = new HashMap<String, String>();
				if (rotationMap.get(key)!=null) {
					innerMap.put("R"+String.format("%05d",rotationMap.get(key) ), map.get(key));
				}else if (combineMap.get(key)!= null) {
					innerMap.put("C"+String.format("%05d",combineMap.get(key) ), map.get(key));
				}else{
					innerMap.put(key.substring(0, 4)+key.substring(7, 9), map.get(key));
				}
				result.put(classId, innerMap);

					
			}		
		}
		return result;		
	}
	/*** 
	* <p>Description: 获取seq</p>
	* @author  WANGCHAO262
	* @date  2017年6月8日 上午9:14:54
	*/
	public static String getSeq(int classNumber,int seqLength){
		
		int result = seqLength - (classNumber-1)%seqLength ;		
		return result+"";
	}
	/*** 
	* <p>Description:获取星期几 </p>
	* @author  WANGCHAO262
	* @date  2017年6月9日 下午3:42:38
	*/
	public static Integer getWeek(int classNumber,int seqLength){
		
		int result = 5- (classNumber-1)/seqLength  ;		
		return result;
	}
	
	/*** 
	* <p>Description: seqSubjectMap:seq与subject映射关系,classNumber key:techerId+subjectId</p>
	* @author  wangchao262
	* @date  2017年6月7日 下午5:26:22
	*/
	public static boolean  isSeqSubjectMatch(Map<String, List<String>> seqSubjectMap,int classNumber,int seqLength,String key,Set<Integer> alreadyClassNumber,
			List<Integer> significantSeq,List<Integer> importantSeq,List<Integer> commonSeq,Map<String, Integer> subjectImportanceMap,int attempCount){
		
		String seq = getSeq(classNumber, seqLength);	
		/*List<String> subjectList = seqSubjectMap.get(seq);
		for (String string : subjectList) {
			if (string.equalsIgnoreCase(key.substring(4))) {
				return true;
			}
		}*/
		//若上述条件不满足，在判断当天的   非常重要--》重要--》一般
		/*if (alreadyClassNumber == null || alreadyClassNumber.size() == 0) {
			return true;
		}*/
		if (attempCount > MAX_ATTEMPT){
		    return true;
        }
		int week = getWeek(classNumber, seqLength); 
		if (!key.startsWith("C")&&!key.startsWith("R")&& subjectImportanceMap.get(key.substring(4)) != null && Constants.SUBJECT_SIGNIFICANT==subjectImportanceMap.get(key.substring(4))) {
			
			boolean flag = false ;
			boolean flag2 = false;
			int count = 0;
			
			for (Integer seqInt : significantSeq) {
				
				if (alreadyClassNumber.isEmpty() &&  classNumber ==(5-week)*seqLength+(seqLength-seqInt+1))  {
					flag = true;
				}								
				if (alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					count ++;
				}
				if (classNumber == ((5-week)*seqLength+(seqLength-seqInt+1))) {
					flag2 = true;
				}
			}
			if(flag){
				return true;
			}
			if (flag2) {
				return true;
			}
			if(!flag2 && count!=significantSeq.size()){
				return false;
			}									
			if(importantSeq.contains(Integer.valueOf(seq))){
				return true;
			}			
			flag = false ;
			count = 0;			
			for (Integer seqInt : importantSeq) {
				if (alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					count ++;
				}								
			}
			if(count!=importantSeq.size()){
				return false;
			}
			if(commonSeq.contains(Integer.valueOf(seq))){
				return true;
			}									
		}
		//@add 新增重要的优先排非常重要未排完的，然后排重要的
		if (!key.startsWith("C")&&!key.startsWith("R")&& subjectImportanceMap.get(key.substring(4)) != null &&Constants.SUBJECT_IMPORTANT==subjectImportanceMap.get(key.substring(4))) {
			boolean flag = false ;
			boolean flag2 = false ;
			boolean flag3 = false;
			int count = 0;
			int countSig = 0;

			for (Integer seqInt : significantSeq) {
				if (alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					countSig ++;
				}
				if (classNumber == ((5-week)*seqLength+(seqLength-seqInt+1))) {
					flag3 = true;
				}
			}
			if (countSig!=significantSeq.size()){
				if (flag3){
					return true;
				}else{
					return false;
				}
			}

			for (Integer seqInt : importantSeq) {
				if (alreadyClassNumber.isEmpty() &&  classNumber ==(5-week)*seqLength+(seqLength-seqInt+1))  {
					flag = true;
				}								
				if (alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					count ++;
				}
				if (classNumber == ((5-week)*seqLength+(seqLength-seqInt+1))) {
					flag2 = true;
				}
			}
			if(flag){
				return true;
			}
			if (flag2) {
				return true;
			}
			if(!flag2&&count!=importantSeq.size()){
				return false;
			}
			if(commonSeq.contains(Integer.valueOf(seq))){
				return true;
			}									
		}
		if (!key.startsWith("C")&&!key.startsWith("R")&& subjectImportanceMap.get(key.substring(4))!= null&&Constants.SUBJECT_COMMON==subjectImportanceMap.get(key.substring(4))) {
			boolean flag = false ;
			for (Integer seqInt : commonSeq) {
				if (alreadyClassNumber.isEmpty() &&  classNumber ==(5-week)*seqLength+(seqLength-seqInt+1))  {
					flag = true;
				}																
			}
			if(flag){
				return true;
			}						
			if(commonSeq.contains(Integer.valueOf(seq))){
				return true;
			}									
		}
		return true;
	}
	
	
	/**
	 * @param randomMap   已经排好的课程 
	 * @param classNumber 即将安排的课程序号
	 * @param key 即将安排的课程
	 *                    35 28 21 14 7
						  34 27 20 13 6
						  33 26 19 12 5
						  32 25 18 11 4

						  31 24 17 10 3
						  30 23 16  9 2
						  29 22 15  8 1
	 * @return
	 */
	public static boolean isExistClass(Map<Integer, String> randomMap,int classNumber,String key,int seqLength ){
		int ceil = 0;
		int floor = 0;
		//@add 增加如果排6节课，那么第六节课则不启用该约束条件
          Collection<String> collection =  randomMap.values();
          Iterator<String> iterator =  collection.iterator();
          int count = 0 ;
          while (iterator.hasNext()){
            if (iterator.next().equalsIgnoreCase(key)){
                count++;
            }
          }
          if (count > 5 ){
              return false;
          }

		if (classNumber%seqLength ==0 ) {
			ceil = classNumber;
			floor = classNumber-seqLength;
		}else{
			ceil =  (classNumber/seqLength+1)*seqLength;
			floor =  classNumber-classNumber%seqLength;
		}	
		for (int i = floor+1; i <= ceil; i++) {
			if (randomMap.containsKey(i)&&randomMap.get(i).equalsIgnoreCase(key)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static  boolean isMorning(int classNumber,int seqLength, int seqMornigLength){
		
		if ((classNumber>=(seqLength-seqMornigLength+1) && classNumber<=seqLength)||(classNumber>=(seqLength*2-seqMornigLength+1) && classNumber<=seqLength*2)||(classNumber>=(seqLength*3-seqMornigLength+1) && classNumber<=seqLength*3)||(classNumber>=(seqLength*4-seqMornigLength+1) && classNumber<=seqLength*4)||(classNumber>=(seqLength*5-seqMornigLength+1) && classNumber<=seqLength*5)) {
			return true;
		}
		
		return false;
	}
	public static boolean isAfternoon(int classNumber,int seqLength, int seqAfternoonLength){
		
		if ((classNumber>=1 && classNumber<=seqAfternoonLength)||(classNumber>=(seqLength+1) && classNumber<=(seqLength+seqAfternoonLength))||(classNumber>=(seqLength*2+1) && classNumber<=(seqLength*2+seqAfternoonLength))||(classNumber>=(seqLength*3+1) && classNumber<=(seqLength*3+seqAfternoonLength))||(classNumber>=(seqLength*4+1) && classNumber<=(seqLength*4+seqAfternoonLength))) {
			return true;
		}
		
		return false;
	}
	public static boolean isInWeekSet(int classNumber,Set<Integer> weekSet,int seqLength){
		
		for (Integer integer : weekSet) {
			switch (integer) {
			case 5:
				if (classNumber>=1&&classNumber<=seqLength) {
					return true;
				}
				break;
			case 4:
				if (classNumber>=(seqLength+1)&&classNumber<=(2*seqLength)) {
					return true;
				}
				break;
			case 3:
				if (classNumber>=(2*seqLength+1)&&classNumber<=(3*seqLength)) {
					return true;
				}
				break;
			case 2:
				if (classNumber>=(3*seqLength+1)&&classNumber<=(4*seqLength)) {
					return true;
				}
				break;
			case 1:
				if (classNumber>=(4*seqLength+1)&&classNumber<=(5*seqLength)) {
					return true;
				}
				break;	
			default:
				break;
			}						
		}
		
		return false;
	}
	
	public static Map<String, String> classInMorning(List<Subject> subjects){
		Map<String, String> result = new HashMap<String, String>();
		for (Subject subject : subjects) {
			if ("语文".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}
			if ("数学".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}
			if ("英语".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}			
		}				
		return result;		
	}
	
	public static Map<String, String> classInAfternoon(List<Subject> subjects){
		Map<String, String> result = new HashMap<String, String>();
		for (Subject subject : subjects) {
			if ("政治".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}
			if ("历史".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}						
		}				
		return result;		
	}
	public static Map<String, String> sortMapByVPriority(Map<String, String> oriMap,Map<String, String> classInMorning,Map<String, String> classInAfternoon){
		 Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
		    if (oriMap != null && !oriMap.isEmpty()) { 
		    	for (String key : oriMap.keySet()) {
					if(classInMorning.containsKey(key.substring(4, 6))){
						sortedMap.put(key, oriMap.get(key));
					}
					if(classInAfternoon.containsKey(key.substring(4, 6))){
						sortedMap.put(key, oriMap.get(key));
					}
				}
		    	for (String key  : oriMap.keySet()) {
					if (!sortedMap.containsKey(key)) {
						sortedMap.put(key, oriMap.get(key));
					}
				}
	       
		    }  
		    return sortedMap;
		
	}
	
	
	
	
	public static Map<String, String> sortMapByValue(Map<String, String> oriMap){
		 Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
		    if (oriMap != null && !oriMap.isEmpty()) { 		    	
		        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());  
		        Collections.sort(entryList,  
		                new Comparator<Map.Entry<String, String>>() {  
		                    public int compare(Entry<String, String> entry1,  
		                            Entry<String, String> entry2) {  
		                        int value1 = 0, value2 = 0;  
		                        try {  
		                            if (entry1.getValue().indexOf("+")>0) {
										String[] a = entry1.getValue().split("\\+");
										value1 = Integer.valueOf(a[0])+2*Integer.valueOf(a[1]);
									}else {
										value1 = Integer.valueOf(entry1.getValue());
									}
		                            if (entry2.getValue().indexOf("+")>0) {
										String[] b = entry2.getValue().split("\\+");
										value2 = Integer.valueOf(b[0])+2*Integer.valueOf(b[1]);
									}else {
										value2 = Integer.valueOf(entry2.getValue());
									}	                        	 
		                        } catch (NumberFormatException e) {  
		                            value1 = 0;  
		                            value2 = 0;  
		                        } 		                        		                        		                        		                        
		                        return value2 - value1;  
		                    }  
		                });  
		        Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
		        Map.Entry<String, String> tmpEntry = null;  
		        while (iter.hasNext()) {  
		            tmpEntry = iter.next();  
		            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
		        }  
		    }  
		    return sortedMap;
		
	}
/**
    @Author : Administrator
    @Description :
    @Date : 下午 11:00 2017/6/25

*/
	public static Map<String,String> sortMapByAlready(Map<String, String> oriMap,Map<String, List<Integer>> alreadyTeacherSeqMap,int teacherIdBit){
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Entry<String, String> entry1,
                                           Entry<String, String> entry2) {
                            int value1 = 0, value2 = 0;
                            try {
                                if (entry1.getValue().indexOf("+")>0) {
                                    String[] a = entry1.getValue().split("\\+");
                                    value1 = Integer.valueOf(a[0])+2*Integer.valueOf(a[1]);
                                }else {
                                    value1 = Integer.valueOf(entry1.getValue());
                                }
                                if (entry2.getValue().indexOf("+")>0) {
                                    String[] b = entry2.getValue().split("\\+");
                                    value2 = Integer.valueOf(b[0])+2*Integer.valueOf(b[1]);
                                }else {
                                    value2 = Integer.valueOf(entry2.getValue());
                                }
                            } catch (NumberFormatException e) {
                                value1 = 0;
                                value2 = 0;
                            }
                            return value2 - value1;
                        }
                    });
            Iterator<Map.Entry<String, String>> iter = entryList.iterator();
            Map.Entry<String, String> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        for (String key:
             sortedMap.keySet()) {
            if (sortedMap.get(key).indexOf("+")>0){
                String[] a = sortedMap.get(key).split("\\+");
                sortedMap.put(key,Integer.valueOf(a[0])+2*Integer.valueOf(a[1])+"");
            }
        }
        List<String> list = new ArrayList<String>();
        Iterator iterator = sortedMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Entry) iterator.next();
            list.add((String)entry.getKey());
        }
        for (int i = 0 ;i<list.size()-1;i++){
            //如果前后的值一样且后面的在alreadyMap中就交换
            if (sortedMap.get(list.get(i)).equalsIgnoreCase(sortedMap.get(list.get(i+1))) && alreadyTeacherSeqMap.containsKey(list.get(i+1).substring(0,teacherIdBit))){
               String tmp  =  list.get(i);
               list.set(i,list.get(i+1));
               list.set(i+1,tmp);
            }
        }
        
        //在alreadyTeacherSeqMap中更多限制的先排
        for (int j = 0; j < list.size()-1; j++) {
        	for (int i = 0; i < list.size()-1; i++) {
        		if (alreadyTeacherSeqMap.containsKey(list.get(i).substring(0,teacherIdBit)) &&
    					alreadyTeacherSeqMap.containsKey(list.get(i+1).substring(0,teacherIdBit)) &&
    					  sortedMap.get(list.get(i)).equalsIgnoreCase(sortedMap.get(list.get(i+1))) && 
    						alreadyTeacherSeqMap.get(list.get(i+1).substring(0,teacherIdBit)).size() > alreadyTeacherSeqMap.get(list.get(i).substring(0,teacherIdBit)).size()
    							) {
    				   String tmp  =  list.get(i);
    	               list.set(i,list.get(i+1));
    	               list.set(i+1,tmp);    				   				
    			}
			}
			
		}
        
        Map<String,String> result = new LinkedHashMap<String, String>();
        for (int i=0;i<list.size();i++){
            result.put(list.get(i),oriMap.get(list.get(i)));
        }
        return result;
	}
	
	
	public static Map<String,String> getClassNoCourse(List<ClassNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (ClassNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			String classId=String.format("%03d", vo.getClassId()) ;
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;			
			
		result.put(classId, seqId+periodId);
		}		
		return result;
		
	}
	public static Map<String,String> getTeacherNoCourse(List<TeacherNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (TeacherNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			String teacherId=String.format("%04d", vo.getTeacherId()) ;
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;			
			
		result.put(teacherId, seqId+periodId);
		}		
		return result;
		
	}
	public static Map<String,String> getSubjectNoCourse(List<SubjectNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (SubjectNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;
			String subjectId =String.format("%02d", vo.getSubjectId())  ;
			List<Integer> gradeClassIds = vo.getGradeClassIds();			
			for (Integer gradeClassId : gradeClassIds) {
				String classId= String.format("%03d", gradeClassId);
				result.put(classId+subjectId, seqId+periodId);
			}
		}		
		return result;
		
	}
	public static Map<String,String> getGradeNoCourse(List<GradeNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (GradeNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			String periodSeq =String.format("%01d", vo.getLessonPeriodSeq())  ;
			List<Integer> gradeClassIds = vo.getGradeClassIds();			
			for (Integer gradeClassId : gradeClassIds) {
				String classId= String.format("%03d", gradeClassId);
				if (result.containsKey(classId)) {
					result.put(classId, result.get(classId)+";"+seqId+periodSeq);
				}else {
					result.put(classId, seqId+periodSeq);
				}
			}			
		}		
		return result;
		
	}
	
	public static List<ArrangeVo> getLessonArranges(String bestChromosome){
		List<ArrangeVo> result = new ArrayList<ArrangeVo>();		
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
	    int weekdayIdBit = DNA.getInstance().getWeekdayIdBit();
	    int seqIdBit = DNA.getInstance().getSeqIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {				
				//一个上课单元
				ArrangeVo vo = new ArrangeVo();
				String dnaString = bestChromosome.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				
				if (dnaString.substring(0, teacherIdBit).startsWith("R")) {
					Integer rotationId = Integer.valueOf(dnaString.substring(1, teacherIdBit)+dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit));
					Integer classId = Integer.valueOf(dnaString.substring(teacherIdBit, teacherIdBit+classIdBit)); 
					Integer weekdayId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit)); 
					Integer seqId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit+seqIdBit)); 
					vo.setRotationId(rotationId);
					vo.setClassId(classId);
					vo.setWeekdayId(weekdayId);
					vo.setSeqId(seqId);
					vo.setSourceType("1");
									
					
				}else if (dnaString.substring(0, teacherIdBit).startsWith("C")) {
					Integer combineId = Integer.valueOf(dnaString.substring(1, teacherIdBit)+dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit));
					Integer classId = Integer.valueOf(dnaString.substring(teacherIdBit, teacherIdBit+classIdBit)); 
					Integer weekdayId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit)); 
					Integer seqId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit+seqIdBit)); 
					vo.setCombineId(combineId);
					vo.setClassId(classId);
					vo.setWeekdayId(weekdayId);
					vo.setSeqId(seqId);
					vo.setSourceType("1");
					
				}else{
					Integer teacherId = Integer.valueOf(dnaString.substring(0, teacherIdBit)); 
					Integer classId = Integer.valueOf(dnaString.substring(teacherIdBit, teacherIdBit+classIdBit)); 
					Integer subjectId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit)); 
					Integer weekdayId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit)); 
					Integer seqId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit+seqIdBit)); 
					vo.setTeacherId(teacherId);
					vo.setClassId(classId);
					vo.setSubjectId(subjectId);
					vo.setWeekdayId(weekdayId);
					vo.setSeqId(seqId);
					vo.setSourceType("1");
					
				}
				result.add(vo);
				
				}
			}
		}
	
		return result;
	}
	
	/*** 
	* <p>Description: 获取rotationMap</p>
	* @author  WANGCHAO262
	* @date  2017年6月14日 下午3:33:55
	*/
	public static Map<String, Integer> getArrangeRotation(List<CombineAndRotationVo> combineAndRotationVos){
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (CombineAndRotationVo vo : combineAndRotationVos) {
			String teacherId = String.format("%04d", vo.getTeacherId())  ;
			String classId = String.format("%03d", vo.getClassId())  ;
			String subjectId = String.format("%02d", vo.getSubjectId())  ;
			
			result.put(teacherId+classId+subjectId, vo.getRotationId());								
		}
		
		
		
		return result;
		
		
	}
	/*** 
	* <p>Description: 获取combineMap</p>
	* @author  WANGCHAO262
	* @date  2017年6月15日 下午5:05:08
	*/
	public static Map<String, Integer> getArrangeCombine(List<CombineAndRotationVo> combineAndRotationVos){
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (CombineAndRotationVo vo : combineAndRotationVos) {
			String teacherId = String.format("%04d", vo.getTeacherId())  ;
			String classId = String.format("%03d", vo.getClassId())  ;
			String subjectId = String.format("%02d", vo.getSubjectId())  ;
			
			result.put(teacherId+classId+subjectId, vo.getCombineId());								
		}
		return result;
	}

	
	
	/*** 
	* <p>Description: </p>
	* @author  WANGCHAO262
	* @date  2017年7月3日 下午5:57:17
	* 
	* 走班
44  17/18    R00001
45  17/5     R00003
46  17/6

合班 
44--45  17

走班资源池  rotationId
合班资源池  combineId
走班的合班池 rotationId<-->rotationId<-->rotationId

第一种:走班按照走班表的设置R资源池
第二种:
合班的
情况1:如果均未出现在走班中的，那么使用C资源池
情况2:如果出现部分出现在走班中的，例如44的17和另一个不在走班中的17(teacherId+subjetcId)合
使用走班的合班资源池，形式为rotationId<-->teacherId+subjectId
情况3:如果出现全部在走班中的，使用走班的合班资源池rotationId<-->rotationId

如果合班出现3个以上的情况
情况1 一样
情况2 形式为rotationId<-->rotaionId<-->teacherId+subjectId
情况3 形式为rotationId<-->rotationId<-->rotationId

走班的合班资源池格式为:  key: rotationId(teacherId+subjectId)  value:combineId
在排课时记录combineId的排课记录
	* 
	* 
	*/
	//弃用
	public static Map<String,Integer> getArrangeSpecial(Map<String,Integer> arrangeCombineMap,Map<String,Integer> arrangeRotationMap){
		Map<String,Integer> result= new HashMap<String, Integer>();
        for (String key:
             arrangeRotationMap.keySet()) {
            if (arrangeCombineMap.containsKey(key)){
                result.put(key,arrangeRotationMap.get(key));
            }
        }

		return result;

	}
	
	public static boolean isInCombineMap(Map<String, Set<Integer>> combineMap,int classNumber){
		if (combineMap!= null) {			
			for (String	 key : combineMap.keySet()) {
				Iterator<Integer> iterator = combineMap.get(key).iterator();
				while (iterator.hasNext()) {
					Integer integer = (Integer) iterator.next();
					if (integer == classNumber) {
						return true;
					}
				}
			}
			
		}				
		return false;
		
	}
	
	/*** 
	* <p>Description: 解决排课不均匀的问题</p>
	* @author  WANGCHAO262
	* @date  2017年6月22日 上午11:28:42
	*/
	public static boolean isClassInOrder(Set<Integer> alreadyClassNumber,int classNumber,int seqLength,int attempCount){
		
		if (attempCount > MAX_ATTEMPT) {
			return true;
		}
		
		int[] week = new int[5];		
		Iterator<Integer> iterator = alreadyClassNumber.iterator();
		while (iterator.hasNext()) {
			Integer integer = (Integer) iterator.next();
			int i = getWeek(integer,seqLength);
			switch (i) {
			case 1:
				week[0]++;
				break;
			case 2:
				week[1]++;
				break;
			case 3:
				week[2]++;
				break;
			case 4:
				week[3]++;
				break;
			case 5:
				week[4]++;
				break;	
			default:
				break;
			}
		}
		int x = getWeek(classNumber,seqLength);
		week[x-1]++;
		int max = week[0];
		int min = week[0];
		for (int i = 0; i < week.length; i++) {
			if (week[i] > max) {
				max = week[i];
			}
			if (week[i] < min) {
				min = week[i];
			}
		}
		if (max-min>2) {
			return false;
		}
		return true;
	}
	
	/*** 
	 * 每一次选择后不能让剩余的课程约束与当前的排课时段产生全集，否则会存在死循环
	* <p>Description: </p>
	* @author  WANGCHAO262
	* @date  2017年6月22日 下午2:07:08
	*/
	public static boolean isAlreadyComplete(int classNum,Set<Integer> alreadyClassNum,int maxClassNum,String teacherKey,Map<String, List<Integer>> alreadyTeacherSeqMap,Set<String> remainClassSet,Map<String, Integer> remainClassMap,int seqLength,int teacherIdLength){
		
		
		Map<String, Set<Integer>> teacherRemainClassMap = new HashMap<String, Set<Integer>>();
		int  count = 0;
		int  remainClassCount = 0;
		boolean isFirst = false;//是否第一次进入
		Set<Integer> remainClassNumFinal = new HashSet<Integer>();

		Set<Integer> sameSet = new HashSet<Integer>();
		Set<Integer> sameSetInit = new HashSet<Integer>();

		for (int i = 1; i <= maxClassNum; i++) {
			remainClassNumFinal.add(i);
		}	
		sameSet.addAll(remainClassNumFinal);
		sameSet.removeAll(alreadyClassNum);
		sameSet.remove(classNum);
		sameSetInit.addAll(remainClassNumFinal);
		sameSetInit.removeAll(alreadyClassNum);
		sameSetInit.remove(classNum);
		
		if (!alreadyTeacherSeqMap.isEmpty() &&  alreadyClassNum.size() !=(maxClassNum-1) && !remainClassSet.isEmpty() ) {
				for (String teacherId : remainClassSet) {
					//记录剩余的应排课程的总数
					for (String key : remainClassMap.keySet()) {
						if (key.startsWith(teacherId)) {
							remainClassCount += remainClassMap.get(key);
						}
					}
					 					
					if(alreadyTeacherSeqMap.get(teacherId) != null){
						count ++;
						isFirst = true;
						Set<Integer> remainClassNum = new HashSet<Integer>();
						remainClassNum.addAll(remainClassNumFinal);
						Set<Integer> set = new HashSet<Integer>();
						set.addAll(alreadyClassNum);
						set.add(classNum);
						set.addAll(alreadyTeacherSeqMap.get(teacherId));
						if (set.size() == maxClassNum) {
							return true;
						}																		
						for (Integer integer : set) {
							remainClassNum.remove(integer);
						}
						//后续几个班级开始增加规则，即未排课程不能同时出现在剩余的不能排限制中
						if (alreadyClassNum.size() >= maxClassNum/2 ) {
							sameSet.retainAll(alreadyTeacherSeqMap.get(teacherId));							
						}
						teacherRemainClassMap.put(teacherId, remainClassNum);
						
						if (isRemainComplete(remainClassNum, teacherId, remainClassMap, seqLength, teacherIdLength,teacherRemainClassMap)) {
							return true;
						}																														
					}														
				}	
				int i=0;
				for (Integer integer : sameSet) {
					if (alreadyTeacherSeqMap.get(teacherKey.substring(0, teacherIdLength))!= null && !alreadyTeacherSeqMap.get(teacherKey.substring(0, teacherIdLength)).contains(integer)) {
						i++;
					}
				}
				
				
				//当剩余课程相同的限制大未排课的数量(针对原本未达到满课的增加剩余课程数小于剩余需要排课两倍的逻辑)
				if (alreadyClassNum.size() > maxClassNum/2 && isFirst && sameSetInit.size()<2*remainClassCount && (sameSet.size()-i) > (remainClassSet.size()-count)&& !sameSet.contains(classNum) ) {
					return true;
				}
		}				
		return false;
	}
	
	public static boolean isRemainComplete(Set<Integer> remainClassNum,String teacherId ,Map<String, Integer> remainClassMap,int seqLength,int teacherIdLength,Map<String, Set<Integer>> teacherRemainClassMap ){
		
		int countTwo = 0;//2的个数
		int countOne = 0;//1的个数 
		
		for (String string : remainClassMap.keySet()) {
			if (remainClassMap.get(string) == 2) {
				countTwo ++;
			}
			if (remainClassMap.get(string) == 1) {
				countOne ++;
			}
		}
		
		Set<Integer> weekSet = new HashSet<Integer>();
		for (Integer integer : remainClassNum) {
			 int i =  getWeek(integer,seqLength);
			 weekSet.add(i);			 
		}
		for (String string : remainClassMap.keySet()) {
			/*if (string.substring(0, teacherIdLength).equalsIgnoreCase(teacherId) && (weekSet.size() < remainClassMap.get(string) || (remainClassMap.get(string) != 1 && (countOne+countTwo) == remainClassMap.size()&& countTwo >1 && remainClassNum.size() < 2*remainClassMap.get(string)))) {
				return true;
			}*/
			//增加<=5
			if (remainClassMap.get(string) <= 5 &&string.substring(0, teacherIdLength).equalsIgnoreCase(teacherId) && weekSet.size() < remainClassMap.get(string) ) {
			return true;
			}
			for (String key : teacherRemainClassMap.keySet()) {
				if (key != teacherId &&  CollectionUtils.isEqualCollection(teacherRemainClassMap.get(key),remainClassNum) && remainClassMap.get(string) != 1 && (countOne+countTwo) == remainClassMap.size()&& countTwo >1 && remainClassNum.size()<(remainClassMap.get(string)*2) ) {
					System.out.println(teacherId+"-------------"+key);
					return true;
				}
			}								
		}
		int countRemainTwo = 0;
		Set<Integer> remainTwoSet = new HashSet<Integer>();
		if (teacherRemainClassMap.size()>1) {
			for (String first : teacherRemainClassMap.keySet()) {														
				//记录剩余的应排课程的总数
				int countRemainFirst = 0;
				for (String key : remainClassMap.keySet()) {
					if (key.startsWith(first)) {
						countRemainFirst += remainClassMap.get(key);
					}
					
					if(key.startsWith(first)&&remainClassMap.get(key)==2 ){
						countRemainTwo++;
						remainTwoSet.addAll(teacherRemainClassMap.get(first));
					}
					
				}				
				for (String second : teacherRemainClassMap.keySet()) {					
					if (!second.equalsIgnoreCase(first)) {
						int countRemainSecond = 0;
						for (String key : remainClassMap.keySet()) {
							if (key.startsWith(second)) {
								countRemainSecond += remainClassMap.get(key);
							}								
						}																			
						Set<Integer> weekSetCombine = new HashSet<Integer>();
						Set<Integer> weekSetAll = new HashSet<Integer>();						
						weekSetAll.addAll(teacherRemainClassMap.get(first));
						weekSetAll.addAll(teacherRemainClassMap.get(second));
						for (Integer integer : weekSetAll) {
							 int i =  getWeek(integer,seqLength);
							 weekSetCombine.add(i);			 
						}
						int x = Math.abs(teacherRemainClassMap.get(first).size()-teacherRemainClassMap.get(second).size());
						int y = Math.max(countRemainFirst, countRemainSecond);	
						
						
						
						if(countRemainFirst+countRemainSecond<=5&&(countRemainFirst+countRemainSecond>2)&&weekSetCombine.size()<countRemainFirst+countRemainSecond&& weekSetAll.size()<countRemainFirst+countRemainSecond&& x<y){
							return true;
						}
					}									
				}
				
			if (remainTwoSet.size()<2*countRemainTwo) {
				return true;
			}					
			}		
		}
		
		
		
		return false;
	}
	
	/*public static boolean isRemian(Map<String, List<Integer>> alreadyTeacherSeqMap,Set<String> remainClassSet,Set<Integer> sameSet,Map<String, Integer> remainClassMap){
		
		for (String string : remainClassSet) {
			
		}
		
		
		return false;
	}*/
	
	
	public static boolean  isTeacherInAlreadySeqMap(Map<String, List<Integer>> alreadyTeacherSeqMap ,int classNumber,String key){
		
		if (alreadyTeacherSeqMap.containsKey(key)&&alreadyTeacherSeqMap.get(key)!= null ) {
			for (Integer classNum : alreadyTeacherSeqMap.get(key)) {
				if (classNum == classNumber) {
					return true;
				}
			}
		}
						
		return false;
	}
	
	
	
	
	
	
	
	public static void print(String a){
		System.out.println();	

		for (int i = 0; i < 140; i++) {		
			System.out.print(a.substring(i*11, (i+1)*11)+"  ");
			if (i>0 && (i+1)%7==0) {
			System.out.println();	
			}
		}
	}
	public static void print2(String a){
		System.out.println();	

		for (int i = 0; i < 35; i++) {		
			System.out.print(a.substring(i*11, (i+1)*11)+"  ");
			if (i>0 && (i+1)%7==0) {
			System.out.println();	
			}
		}
	}
	
	
	 
	
	
	
	public static void main(String[] args){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			list.add(new Integer(i));
		}		
		getCandidateStrings(list, 4,true);
	}
}
