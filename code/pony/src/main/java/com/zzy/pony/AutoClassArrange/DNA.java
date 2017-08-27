package com.zzy.pony.AutoClassArrange;

import java.util.*;


import com.zzy.pony.util.GAUtil;







public class DNA {
	//资源池    单例模式
	private  int teacherIdBit = 4;
	private  int subjectIdBit = 2;
	private  int weekdayIdBit = 1;
	private  int seqIdBit = 1;
	private  int classIdBit = 3;
	private  int dnaBit = 11;
	private  int seqMornigLength = 5;  //早上上课数
	private  int seqAfternoonLength = 3;//下午上课数
	private  int reselctCount = 1000;//超过该值将上次值删除重新分配

	

	private String[] teacherIdCandidate;  
	private String[] subjectIdCandidate;
	private String[] weekdayIdCandidate;
	private String[] seqIdCandidate;
	private String[] classIdCandidate;
	private Map<String,String> TeacherSubjectweekArrange;//班级老师与课时的映射关系  key:teacherId+classId+subjectId value:weekArrange
	private Map<String, Map<String,Integer>> teacherSubjectClassMap;//平课设置  key:teacherId+subjectId value:(key classId value count(初始为0))
	private Map<String, Map<String,Integer>> teacherSubjectIrregularClassMap;//平课设置 不规则班级 key:teacherId+subjectId value:(key classId value count(初始为0))
	private Map<String, List<String>> classNoCourse;
	private Map<String, List<String>> teacherNoCourse;
	private Map<String, List<String>> subjectNoCourse;
	private Map<String, String> gradeNoCourse;
	private Map<String, List<String>> preNoCourse;
	private Map<String, Map<String,Integer>> preWeekArrange;
	private Map<String, String> classInMorning;
	private Map<String, String> classInAfternoon;
	private Map<String, Set<Integer>> teacherSubjectRegularClassMap;
	private Map<String, List<String>> seqSubjectMap;
	private List<Integer> significantSeq;
	private List<Integer> importantSeq;
	private List<Integer> commonSeq;
	private Map<String, Integer> subjectImportanceMap;
	private Map<String, Integer> arrangeRotationMap;
	private Map<String, Integer> arrangeCombineMap;
	private Map<String,String> specialMap;
	private Map<String, Set<Integer>> combineMap;//合班资源池  新的个体需要初始化
	private Map<String, List<Integer>> alreadyTeacherSeqMap;//老师已上课列表，解决同一时间老师不能上两节课的问题(即ruleOne)    新的个体需要初始化
	private Map<String, List<String>> teacherClassMap;//老师与任教班级关系
    private List<String> arrangeSeq;


 



	
	private String dnaString;//基因序列   由1个班组成，课表安排形成一个最小单位  4位teacherId  3位classId 2位subjectId  1位星期weekday   1位课时seq
	
	
	private static DNA instance = new DNA();
	private DNA(){}
	public static DNA getInstance(){
		return instance;
	}

	/*** 
	* <p>Description:获取一个dna个体,index为classIdCandidate的索引 </p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 下午2:22:42
	*/
	public String getDnaString(int classIndex){
		StringBuilder sb = new StringBuilder();
		Random random  = new Random();
		for (int i = 0; i < this.weekdayIdCandidate.length; i++) {
			for (int j = 0; j < this.seqIdCandidate.length; j++) {				
				String noClassTeacherString =  this.teacherIdCandidate[random.nextInt(this.teacherIdCandidate.length)] ;
				sb.append(noClassTeacherString);
				sb.append(this.classIdCandidate[classIndex]);
				
				if (noClassTeacherString.equalsIgnoreCase("0000")) {
					sb.append("00"); //0000classId00weekdayIdseqId为不上课编码
				}else {
					sb.append(this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)]);
				}
				
				sb.append(this.weekdayIdCandidate[i]);
				sb.append(this.seqIdCandidate[j]);	
			}
				
		}			
		this.dnaString = sb.toString();		
		System.out.println(this.dnaString);
		return this.dnaString;	
	}
	/*** 
	* <p>Description:获取一个dna个体,index为classIdCandidate的索引  key:teacherId+classId+subjectId value:weekArrange </p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 下午2:22:42
	* 
	* modify 支持2+1形式
	* 
	* 合班: 配置表中的课程一起上(主要不同班级)
	* 走班: 配置表中的课程一起上(主要同一班级，在某一时间点可以同时上两门课,保证两节课上的课时数是一致的)
	* 
	* @add isMutation 是否突变，判断初始化操作
     * @todo 排课不冲突
	*/
	public String getDnaStringRuleTwo(int classIndex,Map<String, Map<String, String>> map,boolean isMutation ){
		
		//classIndex为说明是新的个体
		if (classIndex == 0 && !isMutation) {
			combineMap = new HashMap<String, Set<Integer>>();
			alreadyTeacherSeqMap = new HashMap<String, List<Integer>>();
		}
		
		StringBuilder sb = new StringBuilder();
		Random random  = new Random();
		int k = this.weekdayIdCandidate.length * this.seqIdCandidate.length;//总时间段数 5*7
		//key:classId value( key:teacherId+subjectId value:weekArrange)	
		String classId = this.classIdCandidate[classIndex];
		Map<String, String> tmpMap = map.get(this.classIdCandidate[classIndex]);	
		Map<String, String> classtmpMap = GAUtil.sortMapByVPriority(tmpMap, classInMorning, classInAfternoon);
		Map<String, String> classSortedMap =   GAUtil.sortMapByValue(classtmpMap);
		Map<Integer, String> randomMap = new HashMap<Integer, String>();
        //1每次排优先排已经存在alreadyTeacherSeqMap中的，剩下的等优先排完后再排   2 每次排将已排课限制更多的先牌
      //  Map<String,String> classMap = GAUtil.sortMapByAlready(classSortedMap,this.alreadyTeacherSeqMap,this.teacherIdBit);
        Map<String,String> classMap = new LinkedHashMap<String, String>();
        classMap  =   GAUtil.sortMapBySeq(classSortedMap,this.arrangeSeq);
        Map<String, Integer> preClassMap = this.preWeekArrange.get(classId);
        int lastClassNum = 0;
		
		//剩余未安排教师列表
		Set<String> remainClassSet = new HashSet<String>();
		for (String string : classMap.keySet()) {
			remainClassSet.add(string.substring(0, this.teacherIdBit));
		} 
		//剩余为安排教师与课程数Map
		Map<String, Integer> remainClassMap = new HashMap<String, Integer>();
		for (String string : classMap.keySet()) {
			if (classMap.get(string).indexOf("+")<0) {
				remainClassMap.put(string, Integer.valueOf(classMap.get(string)));
			}else {
				String[] a = classMap.get(string).split("\\+");
				remainClassMap.put(string, Integer.valueOf(a[0])+Integer.valueOf(a[1]));
			}
		}		
		

		//年级不排课设置 
		if (this.gradeNoCourse.get(this.classIdCandidate[classIndex]) != null) {
			String[] weekSeqs = this.gradeNoCourse.get(this.classIdCandidate[classIndex]).split(";");
			for (String weekSeq : weekSeqs) {
				int week = Integer.valueOf(weekSeq.substring(0, 1))  ;
				int seq = Integer.valueOf(weekSeq.substring(1, 2))  ;
				randomMap.put(k-((week-1)*this.seqIdCandidate.length+seq)+1,"000000");
			}
		}
		//班级不排课设置
		if (this.classNoCourse.get(this.classIdCandidate[classIndex]) != null) {
			List<String> seqPeriods = this.classNoCourse.get(this.classIdCandidate[classIndex]);
			for (String seqPeriod:
			seqPeriods) {
				int week = Integer.valueOf(seqPeriod.substring(0, 1))  ;
				int seq = Integer.valueOf(seqPeriod.substring(1, 2))  ;
				randomMap.put(k-((week-1)*this.seqIdCandidate.length+seq)+1,"000000");
			}

		}
		
		//预排课不排课设置
		if (this.preNoCourse.get(this.classIdCandidate[classIndex]) != null) {
			List<String> seqPeriods = this.preNoCourse.get(this.classIdCandidate[classIndex]);
			for (String seqPeriod:
					seqPeriods){
				int week = Integer.valueOf(seqPeriod.substring(0, 1))  ;
				int seq = Integer.valueOf(seqPeriod.substring(1, 2))  ;
				randomMap.put(k-((week-1)*this.seqIdCandidate.length+seq)+1,"000000");
			}
		}
		
		
		
		
		
		for (String key : classMap.keySet()) {
            int attempCount = 0;//每个key的最大尝试次数，若超过此次数，则默认满足重要程度匹配规则

            remainClassSet.remove(key.substring(0, this.teacherIdBit));
			remainClassMap.remove(key);
			
			//分两种情况，第一种是weekArrange不含+,第二种是含+
			//第一种									
			if (classMap.get(key).indexOf("+")<0) {
			
			int keyCount = 0;
			if (preClassMap !=null && preClassMap.get(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit))!= null) {
				keyCount = Integer.valueOf(classMap.get(key)) - preClassMap.get(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit));
			}else{
				keyCount =  Integer.valueOf(classMap.get(key)) ;
			}
				
				
			outer1:	for (int i = 0; i <  keyCount ; i++) {
				     int classNumber=0 ;
					

						//@todo  随机从set中取值赋给当前classNumber且不能够重复
						
						if (combineMap != null && !combineMap.isEmpty()&&combineMap.containsKey(key)&& combineMap.get(key).size()==Integer.valueOf(classMap.get(key))) {
							Iterator<Integer> iterator =   combineMap.get(key).iterator();
							while (iterator.hasNext()) {
								Integer integer = (Integer) iterator.next();
								if (!randomMap.containsKey(integer)) {
									randomMap.put(integer, key);																								
								}
							}
						}else if(combineMap != null && !combineMap.isEmpty()&&combineMap.containsKey(specialMap.get(key))&& combineMap.get(specialMap.get(key)).size()==Integer.valueOf(classMap.get(key))){
							Iterator<Integer> iterator =   combineMap.get(specialMap.get(key)).iterator();
							while (iterator.hasNext()) {
								Integer integer = (Integer) iterator.next();
								if (!randomMap.containsKey(integer)) {
									randomMap.put(integer, key);																								
								}
							}
						}
						
												
					else {
						classNumber = random.nextInt(k)+1;
						//@todo  增加规则在combineMap已经排过的不能够在下面排
												
					while(randomMap.containsKey(classNumber) ||GAUtil.isTeacherNoCourse(this.teacherNoCourse, key, this.teacherIdBit, classNumber, k, this.seqIdCandidate.length)
							|| GAUtil.isSubjectNoCourse(this.subjectNoCourse, key, this.teacherIdBit, this.subjectIdBit, classId, classNumber, k, this.seqIdCandidate.length)
							||GAUtil.isExistClass(randomMap, classNumber,key,this.seqIdCandidate.length,attempCount)||GAUtil.isTeacherInAlreadySeqMap(alreadyTeacherSeqMap, classNumber, key.substring(0, this.teacherIdBit)/*,attempCount*/)
							/*||( !key.startsWith("C")&&!key.startsWith("R")&&  this.classInMorning.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isMorning(classNumber,this.seqIdCandidate.length,this.seqMornigLength))
							||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInAfternoon.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isAfternoon(classNumber,this.seqIdCandidate.length,this.seqAfternoonLength))*/
							||GAUtil.isAlreadyComplete(classNumber, randomMap.keySet(), k, key,alreadyTeacherSeqMap,remainClassSet,remainClassMap,this.seqIdCandidate.length,this.teacherIdBit,attempCount)
							||(this.teacherSubjectRegularClassMap.containsKey(key)&& !GAUtil.isInWeekSet(classNumber, teacherSubjectRegularClassMap.get(key),this.seqIdCandidate.length,attempCount)
							||!GAUtil.isSeqSubjectMatch(this.seqSubjectMap, classNumber, this.seqIdCandidate.length, key,randomMap.keySet(),this.significantSeq,this.importantSeq,this.commonSeq,this.subjectImportanceMap,attempCount)
							|| GAUtil.isInCombineMap(combineMap, classNumber,classMap.keySet(),specialMap)|| !GAUtil.isClassInOrder(randomMap.keySet(), classNumber, this.seqIdCandidate.length,attempCount)	)
							){
						classNumber = random.nextInt(k)+1;
						attempCount ++;
						
						if (attempCount > reselctCount&& randomMap.get(lastClassNum)!= null && randomMap.get(lastClassNum).equalsIgnoreCase(key)) {
							randomMap.remove(lastClassNum);
							i--;
							attempCount = 0;
							continue outer1;
						}
						
						}
					
					randomMap.put(classNumber, key);
					lastClassNum=classNumber;
					
					if (alreadyTeacherSeqMap.containsKey(key.substring(0, this.teacherIdBit))) {
						if (teacherClassMap.containsKey(key.substring(0, this.teacherIdBit))&&teacherClassMap.get(key.substring(0, this.teacherIdBit)).contains(this.classIdCandidate[classIndex])) {
							alreadyTeacherSeqMap.get(key.substring(0, this.teacherIdBit)).add(classNumber);						
						}
					}else {
						List<Integer> seqList = new ArrayList<Integer>();
						if (teacherClassMap.containsKey(key.substring(0, this.teacherIdBit))&&teacherClassMap.get(key.substring(0, this.teacherIdBit)).contains(this.classIdCandidate[classIndex])) {
							seqList.add(classNumber);
							alreadyTeacherSeqMap.put(key.substring(0, this.teacherIdBit), seqList);	
						}
						
					}
										
					}
					
					
					if (key.startsWith("C") && classNumber != 0) {
						if (combineMap.containsKey(key)) {
							combineMap.get(key).add(classNumber);
						}else {
							Set<Integer> set = new HashSet<Integer>();
							set.add(classNumber);
							combineMap.put(key, set);
						}
					}
					if (specialMap.get(key)!= null&& classNumber != 0) {
						if (combineMap.containsKey(specialMap.get(key))) {
							combineMap.get(specialMap.get(key)).add(classNumber);
						}else {
							Set<Integer> set = new HashSet<Integer>();
							set.add(classNumber);
							combineMap.put(specialMap.get(key), set);
						}
					}
					
				}
			}else {
				//第二种  2+1  暂不支持合班设置
				   String[] a = classMap.get(key).split("\\+");
				   
				   int keyCount = 0;
					if (preClassMap != null && preClassMap.get(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit))!= null) {
						keyCount = Integer.valueOf(a[0]) - preClassMap.get(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit));
					}else{
						keyCount = Integer.valueOf(a[0]) ;
					}
				   
				   
				   outer2:for (int i = 0; i <  keyCount ; i++) {
						int classNumber = random.nextInt(k)+1;
						
						while(randomMap.containsKey(classNumber) ||GAUtil.isTeacherNoCourse(this.teacherNoCourse, key, this.teacherIdBit, classNumber, k, this.seqIdCandidate.length)
								|| GAUtil.isSubjectNoCourse(this.subjectNoCourse, key, this.teacherIdBit, this.subjectIdBit, classId, classNumber, k, this.seqIdCandidate.length)
								||GAUtil.isExistClass(randomMap, classNumber,key,this.seqIdCandidate.length,attempCount)||GAUtil.isTeacherInAlreadySeqMap(alreadyTeacherSeqMap, classNumber, key.substring(0, this.teacherIdBit)/*, attempCount*/)
								/*||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInMorning.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isMorning(classNumber,this.seqIdCandidate.length,this.seqMornigLength))
								||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInAfternoon.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isAfternoon(classNumber,this.seqIdCandidate.length,this.seqAfternoonLength))*/
								||GAUtil.isAlreadyComplete(classNumber, randomMap.keySet(), k,key, alreadyTeacherSeqMap,remainClassSet,remainClassMap,this.seqIdCandidate.length,this.teacherIdBit,attempCount)
								||(this.teacherSubjectRegularClassMap.containsKey(key)&& !GAUtil.isInWeekSet(classNumber, teacherSubjectRegularClassMap.get(key),this.seqIdCandidate.length,attempCount))
								||!GAUtil.isSeqSubjectMatch(this.seqSubjectMap, classNumber, this.seqIdCandidate.length, key,randomMap.keySet(),this.significantSeq,this.importantSeq,this.commonSeq,this.subjectImportanceMap,attempCount)
								|| GAUtil.isInCombineMap(combineMap, classNumber,classMap.keySet(),specialMap)||!GAUtil.isClassInOrder(randomMap.keySet(), classNumber, this.seqIdCandidate.length,attempCount)){
							classNumber = random.nextInt(k)+1;
                            attempCount ++;
                            
                            if (attempCount > reselctCount) {
    							randomMap.remove(lastClassNum);
    							i--;
    							attempCount = 0;
    							continue outer2;
    						}

						}
						randomMap.put(classNumber, key);
						lastClassNum = classNumber;
						if (alreadyTeacherSeqMap.containsKey(key.substring(0, this.teacherIdBit))) {
							if (teacherClassMap.containsKey(key.substring(0, this.teacherIdBit))&&teacherClassMap.get(key.substring(0, this.teacherIdBit)).contains(this.classIdCandidate[classIndex])) {
								alreadyTeacherSeqMap.get(key.substring(0, this.teacherIdBit)).add(classNumber);						
							}
						}else {
							List<Integer> seqList = new ArrayList<Integer>();
							if (teacherClassMap.containsKey(key.substring(0, this.teacherIdBit))&&teacherClassMap.get(key.substring(0, this.teacherIdBit)).contains(this.classIdCandidate[classIndex])) {
								seqList.add(classNumber);
								alreadyTeacherSeqMap.put(key.substring(0, this.teacherIdBit), seqList);	
							}
							
						}
						
						/*if (key.startsWith("C")) {
							if (combineMap.containsKey(key)) {
								combineMap.get(key).add(classNumber);
							}else {
								Set<Integer> set = new HashSet<Integer>();
								set.add(classNumber);
								combineMap.put(key, set);
							}
						}*/
						
					}
				   outer3:for (int i = 0; i <  Integer.valueOf(a[1]) ; i++) {
						int classNumber = random.nextInt(k)+1;		
						//头一天的最后一节不能与第二天的第一节形成联排课  classNumber%this.seqIdCandidate.length == 1
						//早上的第一节课不能与下午的第一节课形成里联排课 classNumber%this.seqIdCandidate.length == 4
						//classNumber+1>k 不能超出范围
						/*40 32 24 16 8
						  39 31 23 15 7
						  38 30 22 14 6
						  37 29 21 13 5
						  36 28 20 12 4

						  35 27 19 11 3
						  34 26 18 10 2
						  33 25 17  9 1*/
				       //当天已经上过该课就不能再上
						if (attempCount > reselctCount) {
							randomMap.remove(lastClassNum);
							randomMap.remove(lastClassNum+1);
							i--;
							attempCount = 0;


						}
						while(randomMap.containsKey(classNumber)||randomMap.containsKey(classNumber-1)||classNumber%this.seqIdCandidate.length == 1|| classNumber%this.seqIdCandidate.length == 4 ||GAUtil.isExistClass(randomMap, classNumber,key,this.seqIdCandidate.length,attempCount)
								/*||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInMorning.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isMorning(classNumber,this.seqIdCandidate.length,this.seqMornigLength))
								||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInAfternoon.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isAfternoon(classNumber,this.seqIdCandidate.length,this.seqAfternoonLength))*/						
								||GAUtil.isAlreadyComplete(classNumber, randomMap.keySet(), k,key, alreadyTeacherSeqMap,remainClassSet,remainClassMap,this.seqIdCandidate.length,this.teacherIdBit,attempCount)
								||GAUtil.isAlreadyComplete(classNumber-1, randomMap.keySet(), k,key, alreadyTeacherSeqMap,remainClassSet,remainClassMap,this.seqIdCandidate.length,this.teacherIdBit,attempCount)
								||GAUtil.isTeacherInAlreadySeqMap(alreadyTeacherSeqMap, classNumber, key.substring(0, this.teacherIdBit)/*,attempCount*/)||GAUtil.isTeacherInAlreadySeqMap(alreadyTeacherSeqMap, classNumber-1, key.substring(0, this.teacherIdBit)/*,attempCount*/)
								||(this.teacherSubjectRegularClassMap.containsKey(key)&& !GAUtil.isInWeekSet(classNumber, teacherSubjectRegularClassMap.get(key),this.seqIdCandidate.length,attempCount))
								||!GAUtil.isSeqSubjectMatch(this.seqSubjectMap, classNumber, this.seqIdCandidate.length, key,randomMap.keySet(),this.significantSeq,this.importantSeq,this.commonSeq,this.subjectImportanceMap,attempCount)
								|| GAUtil.isInCombineMap(combineMap, classNumber,classMap.keySet(),specialMap)|| GAUtil.isInCombineMap(combineMap, classNumber-1,classMap.keySet(),specialMap)||!GAUtil.isClassInOrder(randomMap.keySet(), classNumber, this.seqIdCandidate.length,attempCount)){
							classNumber = random.nextInt(k)+1;
							attempCount++;
							if (attempCount > reselctCount) {
    							randomMap.remove(lastClassNum);
    							i--;
    							attempCount = 0;
    							continue outer3;
    						}
							
						}
						randomMap.put(classNumber, key);
						randomMap.put(classNumber-1, key);
						lastClassNum=classNumber;
						
						
						
						if (alreadyTeacherSeqMap.containsKey(key.substring(0, this.teacherIdBit))) {
							if (teacherClassMap.containsKey(key.substring(0, this.teacherIdBit))&&teacherClassMap.get(key.substring(0, this.teacherIdBit)).contains(this.classIdCandidate[classIndex])) {
								alreadyTeacherSeqMap.get(key.substring(0, this.teacherIdBit)).add(classNumber);	
								alreadyTeacherSeqMap.get(key.substring(0, this.teacherIdBit)).add(classNumber-1);

							}
						}else {
							List<Integer> seqList = new ArrayList<Integer>();
							if (teacherClassMap.containsKey(key.substring(0, this.teacherIdBit))&&teacherClassMap.get(key.substring(0, this.teacherIdBit)).contains(this.classIdCandidate[classIndex])) {
								seqList.add(classNumber);
								seqList.add(classNumber-1);
								alreadyTeacherSeqMap.put(key.substring(0, this.teacherIdBit), seqList);	
							}
							
						}
						
						
						
						/*if (key.startsWith("C")) {
							if (combineMap.containsKey(key)) {
								combineMap.get(key).add(classNumber);
								combineMap.get(key).add(classNumber-1);

							}else {
								Set<Integer> set = new HashSet<Integer>();
								set.add(classNumber);
								set.add(classNumber-1);
								combineMap.put(key, set);
							}
						}*/
					}			
				
			}					
		}


		for (int i = 0; i < this.weekdayIdCandidate.length; i++) {
			for (int j = 0; j < this.seqIdCandidate.length; j++) {								
				if (randomMap.containsKey(k)) {
					
						sb.append(randomMap.get(k).substring(0, this.teacherIdBit));
						sb.append(this.classIdCandidate[classIndex]);	
						sb.append(randomMap.get(k).substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit));
					
					
				}else {
					String noClassTeacherString =  this.teacherIdCandidate[random.nextInt(this.teacherIdCandidate.length)] ;
					sb.append(noClassTeacherString);
					sb.append(this.classIdCandidate[classIndex]);				
					if (noClassTeacherString.equalsIgnoreCase("0000")) {
						sb.append("00"); //0000classId00weekdayIdseqId为不上课编码
					}else {
						String subjectString= this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)];
						while (classMap.containsKey(noClassTeacherString+subjectString) ||subjectString.equalsIgnoreCase("00") ) {
							subjectString= this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)];						
						}
						sb.append(subjectString);
						
					}
				}			
				sb.append(this.weekdayIdCandidate[i]);
				sb.append(this.seqIdCandidate[j]);	
				k--;
			}
				
		}
		
		
		
		
		this.dnaString = sb.toString();		
		//System.out.println(this.dnaString);
		//GAUtil.print2(this.dnaString);
		return this.dnaString;
	}

	public String[] getTeacherIdCandidate() {
		return teacherIdCandidate;
	}
	public void setTeacherIdCandidate(String[] teacherIdCandidate) {
		this.teacherIdCandidate = teacherIdCandidate;
	}	
	public String[] getClassIdCandidate() {
		return classIdCandidate;
	}
	public void setClassIdCandidate(String[] classIdCandidate) {
		this.classIdCandidate = classIdCandidate;
	}
	public String[] getSubjectIdCandidate() {
		return subjectIdCandidate;
	}
	public void setSubjectIdCandidate(String[] subjectIdCandidate) {
		this.subjectIdCandidate = subjectIdCandidate;
	}
	public String[] getWeekdayIdCandidate() {
		return weekdayIdCandidate;
	}
	public void setWeekdayIdCandidate(String[] weekdayIdCandidate) {
		this.weekdayIdCandidate = weekdayIdCandidate;
	}
	public String[] getSeqIdCandidate() {
		return seqIdCandidate;
	}
	public void setSeqIdCandidate(String[] seqIdCandidate) {
		this.seqIdCandidate = seqIdCandidate;
	}	
	public Map<String, String> getTeacherSubjectweekArrange() {
		return TeacherSubjectweekArrange;
	}
	public void setTeacherSubjectweekArrange(
			Map<String, String> teacherSubjectweekArrange) {
		TeacherSubjectweekArrange = teacherSubjectweekArrange;
	}
	public int getTeacherIdBit() {
		return teacherIdBit;
	}
	public int getSubjectIdBit() {
		return subjectIdBit;
	}
	public int getWeekdayIdBit() {
		return weekdayIdBit;
	}
	public int getSeqIdBit() {
		return seqIdBit;
	}
	public int getClassIdBit() {
		return classIdBit;
	}
	public int getDnaBit() {
		return dnaBit;
	}

	public Map<String, List<String>> getClassNoCourse() {
		return classNoCourse;
	}

	public void setClassNoCourse(Map<String, List<String>> classNoCourse) {
		this.classNoCourse = classNoCourse;
	}

	public Map<String, List<String>> getTeacherNoCourse() {
		return teacherNoCourse;
	}

	public void setTeacherNoCourse(Map<String, List<String>> teacherNoCourse) {
		this.teacherNoCourse = teacherNoCourse;
	}

	public Map<String, List<String>> getSubjectNoCourse() {
		return subjectNoCourse;
	}

	public void setSubjectNoCourse(Map<String, List<String>> subjectNoCourse) {
		this.subjectNoCourse = subjectNoCourse;
	}

	public Map<String, String> getGradeNoCourse() {
		return gradeNoCourse;
	}

	public void setGradeNoCourse(Map<String, String> gradeNoCourse) {
		this.gradeNoCourse = gradeNoCourse;
	}

	public Map<String, Map<String, Integer>> getTeacherSubjectClassMap() {
		return teacherSubjectClassMap;
	}
	public void setTeacherSubjectClassMap(
			Map<String, Map<String, Integer>> teacherSubjectClassMap) {
		this.teacherSubjectClassMap = teacherSubjectClassMap;
	}
	public Map<String, Map<String, Integer>> getTeacherSubjectIrregularClassMap() {
		return teacherSubjectIrregularClassMap;
	}
	public void setTeacherSubjectIrregularClassMap(
			Map<String, Map<String, Integer>> teacherSubjectIrregularClassMap) {
		this.teacherSubjectIrregularClassMap = teacherSubjectIrregularClassMap;
	}
	public Map<String, String> getClassInMorning() {
		return classInMorning;
	}
	public void setClassInMorning(Map<String, String> classInMorning) {
		this.classInMorning = classInMorning;
	}
	public Map<String, String> getClassInAfternoon() {
		return classInAfternoon;
	}
	public void setClassInAfternoon(Map<String, String> classInAfternoon) {
		this.classInAfternoon = classInAfternoon;
	}
	public Map<String, Set<Integer>> getTeacherSubjectRegularClassMap() {
		return teacherSubjectRegularClassMap;
	}
	public void setTeacherSubjectRegularClassMap(
			Map<String, Set<Integer>> teacherSubjectRegularClassMap) {
		this.teacherSubjectRegularClassMap = teacherSubjectRegularClassMap;
	}
	public Map<String, List<String>> getSeqSubjectMap() {
		return seqSubjectMap;
	}
	public void setSeqSubjectMap(Map<String, List<String>> seqSubjectMap) {
		this.seqSubjectMap = seqSubjectMap;
	}
	public List<Integer> getSignificantSeq() {
		return significantSeq;
	}
	public void setSignificantSeq(List<Integer> significantSeq) {
		this.significantSeq = significantSeq;
	}
	public List<Integer> getImportantSeq() {
		return importantSeq;
	}
	public void setImportantSeq(List<Integer> importantSeq) {
		this.importantSeq = importantSeq;
	}
	public List<Integer> getCommonSeq() {
		return commonSeq;
	}
	public void setCommonSeq(List<Integer> commonSeq) {
		this.commonSeq = commonSeq;
	}
	public Map<String, Integer> getSubjectImportanceMap() {
		return subjectImportanceMap;
	}
	public void setSubjectImportanceMap(Map<String, Integer> subjectImportanceMap) {
		this.subjectImportanceMap = subjectImportanceMap;
	}
	public Map<String, Integer> getArrangeRotationMap() {
		return arrangeRotationMap;
	}
	public void setArrangeRotationMap(Map<String, Integer> arrangeRotationMap) {
		this.arrangeRotationMap = arrangeRotationMap;
	}
	public Map<String, Integer> getArrangeCombineMap() {
		return arrangeCombineMap;
	}
	public void setArrangeCombineMap(Map<String, Integer> arrangeCombineMap) {
		this.arrangeCombineMap = arrangeCombineMap;
	}
	public Map<String, Set<Integer>> getCombineMap() {
		return combineMap;
	}
	public void setCombineMap(Map<String, Set<Integer>> combineMap) {
		this.combineMap = combineMap;
	}
	public Map<String, List<Integer>> getAlreadyTeacherSeqMap() {
		return alreadyTeacherSeqMap;
	}
	public void setAlreadyTeacherSeqMap(
			Map<String, List<Integer>> alreadyTeacherSeqMap) {
		this.alreadyTeacherSeqMap = alreadyTeacherSeqMap;
	}
	public Map<String, List<String>> getTeacherClassMap() {
		return teacherClassMap;
	}
	public void setTeacherClassMap(Map<String, List<String>> teacherClassMap) {
		this.teacherClassMap = teacherClassMap;
	}

	public Map<String, String> getSpecialMap() {
		return specialMap;
	}

	public void setSpecialMap(Map<String, String> specialMap) {
		this.specialMap = specialMap;
	}

	public Map<String, List<String>> getPreNoCourse() {
		return preNoCourse;
	}

	public void setPreNoCourse(Map<String, List<String>> preNoCourse) {
		this.preNoCourse = preNoCourse;
	}

	public Map<String, Map<String, Integer>> getPreWeekArrange() {
		return preWeekArrange;
	}
	public void setPreWeekArrange(Map<String, Map<String, Integer>> preWeekArrange) {
		this.preWeekArrange = preWeekArrange;
	}

    public List<String> getArrangeSeq() {
        return arrangeSeq;
    }

    public void setArrangeSeq(List<String> arrangeSeq) {
        this.arrangeSeq = arrangeSeq;
    }
}
