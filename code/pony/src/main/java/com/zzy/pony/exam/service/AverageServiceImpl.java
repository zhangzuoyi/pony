package com.zzy.pony.exam.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.dao.AverageIndexDao;
import com.zzy.pony.exam.mapper.AverageIndexMapper;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageExcelVo;
import com.zzy.pony.exam.vo.AverageIndexRowVo;
import com.zzy.pony.exam.vo.AverageIndexVo;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.ReadExcelUtils;
import com.zzy.pony.vo.ExamResultVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class AverageServiceImpl implements AverageService {
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private AverageIndexDao indexDao;
	@Autowired
	private AverageIndexMapper averageIndexMapper;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private ExamResultMapper examResultMapper;

	@Override
	public List<AverageIndexRowVo> findIndexRowVo(Integer examId, Integer gradeId) {
		List<Subject> subjects = subjectService.findByExam(examId);
		List<AverageIndex> indexList = indexDao.findByExamIdAndGradeId(examId, gradeId);
		List<AverageIndexRowVo> result = new ArrayList<AverageIndexRowVo>();
		for (int i = 1; i <= SECTION_COUNT; i++) {
			String section = "A" + i;
			AverageIndexRowVo vo = new AverageIndexRowVo();
			vo.setSection(section);
			vo.setExamId(examId);
			vo.setGradeId(gradeId);
			addIndex(vo, subjects, indexList);
			result.add(vo);
		}
		return result;
	}

	private void addIndex(AverageIndexRowVo vo, List<Subject> subjects, List<AverageIndex> indexList) {
		for (Subject subject : subjects) {
			AverageIndex ai = null;
			for (AverageIndex tmp : indexList) {
				if (tmp.getSection().equals(vo.getSection()) && tmp.getSubjectId() == subject.getSubjectId()) {
					ai = tmp;
					break;
				}
			}
			if (ai == null) {
				ai = new AverageIndex();
				ai.setSection(vo.getSection());
				ai.setSubjectId(subject.getSubjectId());
			}
			vo.getIndexList().add(ai);
		}
	}

	@Override
	public void saveIndexList(Integer examId, Integer gradeId, List<AverageIndex> indexList) {
		for (AverageIndex ai : indexList) {
			if (ai.getId() != 0) {
				AverageIndex old = indexDao.findOne(ai.getId());
				old.setIndexValue(ai.getIndexValue());
				indexDao.save(old);
			} else {
				ai.setExamId(examId);
				ai.setGradeId(gradeId);
				indexDao.save(ai);
			}

		}

	}

	@Override
	public void uploadIndexList(Integer examId, Integer gradeId, List<String> subjectList,
			List<List<Float>> valueList) {
		List<Subject> subjects = subjectService.findByExam(examId);
		List<AverageIndex> indexList = indexDao.findByExamIdAndGradeId(examId, gradeId);
		List<Integer> subjectIds = new ArrayList<Integer>();
		for (String name : subjectList) {
			Integer subjectId = 0;
			for (Subject subject : subjects) {
				if (name.equals(subject.getName())) {
					subjectId = subject.getSubjectId();
					break;
				}
			}
			subjectIds.add(subjectId);
		}
		int subjectLen = subjectIds.size();
		for (int j = 0; j < subjectLen; j++) {
			Integer subjectId = subjectIds.get(j);
			if (subjectId > 0) {
				for (int i = 0; i < valueList.size(); i++) {
					List<Float> values = valueList.get(i);
					String section = "A" + (i + 1);
					AverageIndex ai = new AverageIndex();
					ai.setExamId(examId);
					ai.setGradeId(gradeId);
					ai.setIndexValue(values.get(j));
					ai.setSection(section);
					ai.setSubjectId(subjectId);

					saveUploadAverageIndex(indexList, ai);
				}
			}
		}

	}

	private void saveUploadAverageIndex(List<AverageIndex> indexList, AverageIndex ai) {
		boolean isFind = false;
		for (AverageIndex old : indexList) {
			if (old.getSection().equals(ai.getSection()) && old.getSubjectId() == ai.getSubjectId()) {
				old.setIndexValue(ai.getIndexValue());
				indexDao.save(old);
				isFind = true;
				break;
			}
		}
		if (!isFind) {
			indexDao.save(ai);
		}
	}

	@Override
	public Map<Integer,Map<String, Map<String, BigDecimal>>> calculateAverage(int examId, int gradeId) {
		// TODO Auto-generated method stub
		// 获取考试科目
		SchoolYear schoolYear = schoolYearService.getCurrent();
		List<Subject> subjects = subjectService.findByExam(examId);
		List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(),gradeId);
		BigDecimal one = new BigDecimal("1");
		BigDecimal zero = new BigDecimal("0");
		Map<Integer,Map<String, Map<String, BigDecimal>>> result = new LinkedHashMap<Integer, Map<String,Map<String,BigDecimal>>>();

		for (Subject subject : subjects) {
			//key section(A1) value(key seq)
			Map<String, Map<String, BigDecimal>> map = new LinkedHashMap<String, Map<String,BigDecimal>>();
			//前一个段位剩下的
			Map<String, Map<String, BigDecimal>> mapRemain = new HashMap<String, Map<String,BigDecimal>>();
			BigDecimal remainCount = new BigDecimal(String.valueOf(0)); 
			List<Float> indexValues = new ArrayList<Float>();			
			List<AverageIndexVo> averageIndexVos = averageIndexMapper.findByExamAndGradeAndSubject(examId, gradeId,subject.getSubjectId());
			List<ExamResultVo> examResultVos = examResultMapper.findByExamAndGradeAndSubject(examId, gradeId, subject.getSubjectId());
			for (AverageIndexVo averageIndexVo : averageIndexVos) {
				indexValues.add(averageIndexVo.getIndexValue());
				Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
				for(int i = 0;i<schoolClasses.size();i++) {
					innerMap.put("level"+schoolClasses.get(i).getSeq(), zero);
				}
				map.put(averageIndexVo.getSection(), innerMap);
			}
			int j=0;//段位控制
			BigDecimal indexValue = new BigDecimal(indexValues.get(0).toString()) ;
			int lastj = j;
			BigDecimal remainIndexValueDecimal = new BigDecimal(indexValues.get(0).toString());
			BigDecimal remainCountNum = new BigDecimal("0")  ;
			
			for(int i=0;i<examResultVos.size();i++) {
				int classSeq = examResultVos.get(i).getClassSeq();
				BigDecimal indexValueDecimal = new BigDecimal("0")  ;
				int indexValueFloor = 0;
				BigDecimal indexValueFloorDecimal = new BigDecimal("0");

				int indexValueCeil =0;
				if (lastj != j) {
					indexValue = indexValue.add(new BigDecimal(indexValues.get(j).toString()));
					indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
				    indexValueFloor = (int) Math.floor(indexValue.floatValue());
					if (indexValueFloor>=examResultVos.size()) {
						indexValueFloor = examResultVos.size()-1;
					}
					indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
					indexValueCeil  = (int) Math.ceil(indexValue.floatValue());
					if (indexValueCeil>=examResultVos.size()) {
						indexValueCeil = examResultVos.size()-1;
					}	
					lastj = j;
				}else {
					indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
				    indexValueFloor = (int) Math.floor(indexValue.floatValue());
					if (indexValueFloor>=examResultVos.size()) {
						indexValueFloor = examResultVos.size()-1;
					}
					indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
					indexValueCeil  = (int) Math.ceil(indexValue.floatValue());
					if (indexValueCeil>=examResultVos.size()) {
						indexValueCeil = examResultVos.size()-1;
					}											
				}				

				//BigDecimal indexValueFloorCeil = new BigDecimal(String.valueOf(indexValueCeil));
				
				//前一个段位剩下的
				if(j>0 && remainCount.compareTo(new BigDecimal("0"))>0) {
					if (remainCount.compareTo(indexValueDecimal)>0) {
						int start = j;
						//剩下的比待分配的段位还多
						BigDecimal bigDecimal = indexValueDecimal;
						while( (j+1)<indexValues.size() && remainCount.compareTo(bigDecimal)>0) {
							bigDecimal = indexValueDecimal.add(new BigDecimal(String.valueOf(indexValues.get(j+1))));											
							j++;
						}
						for(int m=start;m<=j;m++) {
							if (m==j) {
								//BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average =  remainCount.divide(remainCountNum,2,RoundingMode.DOWN);
								BigDecimal remain = remainCount.subtract(average.multiply(remainCountNum)); //多余的   
								int first =1;									
								for (String key : mapRemain.get("A"+(m+1)).keySet()) {
									if (map.get("A"+(m+1))!= null && map.get("A"+(m+1)).get(key) != null) {
										//档数
										if (first == 2) {
											map.get("A"+(m+1)).put(key, map.get("A"+(m+1)).get(key).add(average.add(remain)));									
										}else {
											map.get("A"+(m+1)).put(key, map.get("A"+(m+1)).get(key).add(average.add(remain)));									
										}
									}else {																				
											map.get("A"+(m+1)).put(key, average);																																																			
									}
									first++;
								}														
							}else {
								BigDecimal total = new BigDecimal(indexValues.get(m).toString()) ;
								//BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average =  total.divide(remainCountNum,2,RoundingMode.DOWN);
								BigDecimal remain = total.subtract(average.multiply(remainCountNum)); //多余的   
								int first = 1;
								for (String key : mapRemain.get("A"+(m+1)).keySet()) {
									if (map.get("A"+(m+1))!= null && map.get("A"+(m+1)).get(key) != null) {
										//档数
										if (first == 2) {
											map.get("A"+(m+1)).put(key, map.get("A"+(m+1)).get(key).add(average.add(remain)));
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A"+(m+1)).get(key).subtract(average.add(remain)));
											if (mapRemain.get("A"+(m+2)) != null) {
											    mapRemain.get("A"+(m+2)).put(key, mapRemain.get("A"+(m+1)).get(key).subtract(average.add(remain)));
											}else {
												mapRemain.put("A"+(m+2),innerMap);
											}
											
										}else {
											map.get("A"+(m+1)).put(key, map.get("A"+(m+1)).get(key).add(average));
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A"+(m+1)).get(key).subtract(average));
											mapRemain.put("A"+(m+2),innerMap);
										}																			
									}else {										
											map.get("A"+(m+1)).put(key, average);																				
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A"+(m+1)).get(key).subtract(average));
											mapRemain.put("A"+(m+2),innerMap);											
										
									}
									first++;
								}
								remainCount = remainCount.subtract(total);
							}
						}
						
						indexValue = indexValue.add(new BigDecimal(indexValues.get(j).toString()));
						indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
					    indexValueFloor = (int) Math.floor(indexValue.floatValue());
						if (indexValueFloor>=examResultVos.size()) {
							indexValueFloor = examResultVos.size()-1;
						}
						indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
						indexValueCeil  = (int) Math.ceil(indexValue.floatValue());
						if (indexValueCeil>=examResultVos.size()) {
							indexValueCeil = examResultVos.size()-1;
						}	
						lastj = j;
						
					}else {
						for (String key : mapRemain.get("A"+(j+1)).keySet()) {
							if (map.get("A"+(j+1))!= null && map.get("A"+(j+1)).get(key) != null) {
								//档数
								map.get("A"+(j+1)).put(key, map.get("A"+(j+1)).get(key).add(mapRemain.get("A"+(j+1)).get(key)));
							}else {
								map.get("A"+(j+1)).put(key, mapRemain.get("A"+(j+1)).get(key));								
							}
						}
						mapRemain.clear();
					}
					remainIndexValueDecimal =   indexValueDecimal.subtract(remainCount);
					remainCount = remainCount.subtract(remainCount);//归0
				}
								 				
				//6.82   在6后面出现相等  
				if (examResultVos.get(indexValueFloor).getGradeRank() != examResultVos.get(indexValueCeil).getGradeRank()) {
					//小于档位
					if (i<=indexValueFloor) {						
						if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
							//档数
							map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq).add(one));								
						}else {
							map.get("A"+(j+1)).put("level"+classSeq,one);								
						}
						remainIndexValueDecimal = remainIndexValueDecimal.subtract(one); 
					}
					//等于档位
					if (i==indexValueCeil) {
						int count = 1;//相同排名数
						while(examResultVos.get(i).getGradeRank() == examResultVos.get(i+1).getGradeRank()) {
							i++;
							count++;
						}
						BigDecimal total = remainIndexValueDecimal;
						BigDecimal average =  total.divide(new BigDecimal(count),2,RoundingMode.DOWN);
						BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); //多余的
						remainCountNum = new BigDecimal(count);
						for (int m=indexValueCeil;m<=i;m++) {
							classSeq = examResultVos.get(m).getClassSeq();
							//@todo 除不尽的默认加在第一个
							if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
								//档数
								if (m==indexValueCeil) {
									map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq).add(average.add(remain)));								
								}else {
									map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq).add(average));								
								}
							}else {
								map.get("A"+(j+1)).put("level"+classSeq, average);								
							}
							//剩余的
							if (mapRemain.get("A"+(j+2)) != null && mapRemain.get("A"+(j+2)).get("level"+classSeq) != null) {
								//档数
								mapRemain.get("A"+(j+2)).put("level"+classSeq, mapRemain.get("A"+(j+2)).get("level"+classSeq).add(one.subtract(average)));								
								remainCount = remainCount.add(one.subtract(average));
							}else {
								if (mapRemain.get("A"+(j+2)) == null) {
									mapRemain.put("A"+(j+2), new HashMap<String, BigDecimal>());
									mapRemain.get("A"+(j+2)).put("level"+classSeq, one.subtract(average.add(remain)));	
									remainCount = remainCount.add(one.subtract(average.add(remain)));

								}else {
									mapRemain.get("A"+(j+2)).put("level"+classSeq, one.subtract(average));	
									remainCount = remainCount.add(one.subtract(average));

								}								
							}
							
						}
						
						j++;
					}
				}
				//6.82   在6前后面出现相等  
				else {
						int gradeRank = examResultVos.get(indexValueFloor).getGradeRank();
					    /*int m = indexValueFloor;//开始相等
					    int n = indexValueCeil;//结束相等
						while() {
					    	
					    } */
						if (examResultVos.get(i).getGradeRank() != gradeRank ) {
							if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
								//档数
								map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq).add(one));								
							}else {
								map.get("A"+(j+1)).put("level"+classSeq, one);								
							}
							remainIndexValueDecimal = remainIndexValueDecimal.subtract(one);
						}else {
							int count = 1;
							int start = i;//最后一个不相等
							while( (i+1)<examResultVos.size() && examResultVos.get(i).getGradeRank() == examResultVos.get(i+1).getGradeRank()) {
								i++;
								count++;
							}
							remainCountNum = new BigDecimal(count);
							BigDecimal total = remainIndexValueDecimal;
							BigDecimal average =  total.divide(new BigDecimal(count),2,RoundingMode.DOWN);
							BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); //多余的   
							for (int m=start;m<=i;m++) {								
								classSeq = examResultVos.get(m).getClassSeq();
								//@todo 除不尽的默认加在第一个
								if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
									//档数
									if (m==start) {
										map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq).add(average.add(remain)));								
									}else {
										map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq).add(average));								
									}
								}else {
									map.get("A"+(j+1)).put("level"+classSeq, average);								
								}
								//剩余的
								if (mapRemain.get("A"+(j+2)) != null && mapRemain.get("A"+(j+2)).get("level"+classSeq) != null) {
									//档数
									mapRemain.get("A"+(j+2)).put("level"+classSeq, mapRemain.get("A"+(j+2)).get("level"+classSeq).add(one.subtract(average)));								
									remainCount = remainCount.add(one.subtract(average));

								}else {
									if (mapRemain.get("A"+(j+2)) == null) {
										mapRemain.put("A"+(j+2), new HashMap<String, BigDecimal>());
										mapRemain.get("A"+(j+2)).put("level"+classSeq,  one.subtract(average.add(remain)));
										remainCount = remainCount.add(one.subtract(average.add(remain)));
									}else {
										mapRemain.get("A"+(j+2)).put("level"+classSeq, one.subtract(average));	
										remainCount = remainCount.add(one.subtract(average));

									}
								}

							}
							j++;

						}				
				}
				
				
			}	
			for (String	 key : map.keySet()) {
				System.out.print(key+":");
				for (String innerKey : map.get(key).keySet()) {
					System.out.println(innerKey+":"+map.get(key).get(innerKey).floatValue());
				}
			}
			caculateAverageSum(map);
			result.put(subject.getSubjectId(), map);
		}
		return result;
	}
	
	
	
	@Override
	public Map<String, Map<String, Map<String, BigDecimal>>> calculateAverageByFile(MultipartFile file) {
		// TODO Auto-generated method stub
		// subjectName level class   
		Map<String,Map<String, Map<String, BigDecimal>>> result = new LinkedHashMap<String, Map<String,Map<String,BigDecimal>>>();

				Workbook wb =  ReadExcelUtils.ReadExcelByFile(file);				
				try {
					String[] titles = ReadExcelUtils.readExcelTitle(wb);
					for (int i=3;i<titles.length;i++) {
						List<AverageExcelVo> averageExcelVos = getAverageExcelVo(wb,i);
						sortAverageExcelVo(averageExcelVos);
						Map<Integer,List<AverageExcelVo>> levelMap = getLevelMap(averageExcelVos);
						Map<Integer,BigDecimal> levelMapDecimal = getLevelMapDecimal(averageExcelVos);
						Map<Integer,List<AverageExcelVo>> schoolLevelMap = getLevelMapBySchoolName(levelMap,Constants.SCHOOL_NAME);
						Map<Integer,BigDecimal> schoolLevelMapDecimal = getLevelMapDecimalBySchoolName(schoolLevelMap,levelMapDecimal,Constants.SCHOOL_NAME);
						List<String> classCodes = getClassCode(averageExcelVos,Constants.SCHOOL_NAME);
						
						
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
				

				
				return result;
	}
	
	private void calculate(Map<Integer,List<AverageExcelVo>> schoolLevelMap,Map<Integer,BigDecimal> schoolLevelMapDecimal,List<String> classCodes) {
		Map<String, Map<String, BigDecimal>> result = new LinkedHashMap<String, Map<String,BigDecimal>>();
		for (Integer level : schoolLevelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = schoolLevelMap.get(level);			
			for (String classCode : classCodes) {
				
				
				if (averageExcelVos == null || averageExcelVos.size() ==0) {
					//result.put("A"+level, );
				}
			
				
				
				
			}									
		}
		
		
		
		
	}
	
	/**
	 * @return 班级编码
	 */
	private List<String> getClassCode(List<AverageExcelVo> averageExcelVos,String schoolName){
		List<String> result = new ArrayList<String>();
		for (AverageExcelVo vo : averageExcelVos) {
			if (vo.getSchoolName().equalsIgnoreCase(schoolName) && ! result.contains(vo.getClassCode())) {
				result.add(vo.getClassCode());
			}
		}						
		return result;
	}
	
	
	private List<AverageExcelVo> getAverageExcelVo (Workbook wb,int index){
		List<AverageExcelVo> result = new ArrayList<AverageExcelVo>();		           
        Sheet sheet = wb.getSheetAt(0);  
        // 得到总行数  
        int rowNum = sheet.getLastRowNum();  
        Row row = sheet.getRow(0);  
        int colNum = row.getPhysicalNumberOfCells();  
        // 正文内容应该从第二行开始,第一行为表头的标题  
        for (int i = 1; i <= rowNum; i++) {  
            row = sheet.getRow(i);  
            int j = 0; 
            AverageExcelVo vo = new AverageExcelVo();
            vo.setSchoolName(row.getCell(1).getRichStringCellValue().getString());
            vo.setClassCode(row.getCell(2).getRichStringCellValue().getString());   
            vo.setSchoolName(row.getCell(3).getRichStringCellValue().getString()); 
            vo.setSubjectResult(Float.valueOf(String.valueOf(row.getCell(index).getNumericCellValue()))   );              
            result.add(vo); 
        }  
		return result;
	}
	
	/**
	  * @Author : Administrator
	  * @Description : 
	  成绩排序
	*/
	private void sortAverageExcelVo(List<AverageExcelVo> averageExcelVos) {
		Collections.sort(averageExcelVos, new Comparator<AverageExcelVo>() {
			public int compare(AverageExcelVo o1, AverageExcelVo o2) {
				//按照成绩进行降序排列
				if (o1.getSubjectResult() > o2.getSubjectResult()) {
					return -1;
				}
				if (o1.getSubjectResult() == o2.getSubjectResult()) {
					return 0;
				}
				return 1;
			}
		});
		for (int i = 0; i < averageExcelVos.size(); i++) {
			int count = i;
			averageExcelVos.get(i).setRank(i + 1);
			while ((i + 1) <= averageExcelVos.size() && averageExcelVos.get(i).getSubjectResult() == averageExcelVos.get(i + 1).getSubjectResult()) {
				i++;
				averageExcelVos.get(i).setRank(count + 1);
			}
		}
	}
	/**
	  * @Author : Administrator
	  * @Description : 
	  按等级分组
	*/
	private Map<Integer,List<AverageExcelVo>> getLevelMap(List<AverageExcelVo> averageExcelVos){
		List<BigDecimal> level = new ArrayList<BigDecimal>();
		level.add(new BigDecimal("2.5"));
		for (int i = 1; i < 20; i++) {
			level.add(new BigDecimal(i*5));
		}
		level.add(new BigDecimal("97.5"));
		level.add(new BigDecimal("100"));		
		BigDecimal size = new BigDecimal(averageExcelVos.size()) ;//参加考试人数
		Map<Integer,List<AverageExcelVo>> levelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		for (int i = 0; i < level.size(); i++) {
			int previousLevelCount = 0;
			if (i>0){
				//获取前一个段位的人数
				 previousLevelCount = levelMap.get(i-1).size();
			}
			BigDecimal initBigDecimal =  size.multiply(level.get(i)).setScale(2,RoundingMode.HALF_UP);
			int ceil = (int) Math.ceil(initBigDecimal.floatValue());
			int floor = (int) Math.floor(initBigDecimal.floatValue());
			while ((ceil+1)<averageExcelVos.size()&&averageExcelVos.get(ceil).getRank() == averageExcelVos.get(ceil+1).getRank()){
				ceil++;
			}
			levelMap.put(i,averageExcelVos.subList(previousLevelCount,ceil));
		}
		return levelMap;
	}
	/**
	  * @Author : Administrator
	  * @Description : 
	  按等级分组(初始指标)
	*/
	private Map<Integer,BigDecimal> getLevelMapDecimal(List<AverageExcelVo> averageExcelVos){
		List<BigDecimal> level = new ArrayList<BigDecimal>();
		level.add(new BigDecimal("2.5"));
		for (int i = 1; i < 20; i++) {
			level.add(new BigDecimal(i*5));
		}
		level.add(new BigDecimal("97.5"));
		level.add(new BigDecimal("100"));		
		BigDecimal size = new BigDecimal(averageExcelVos.size()) ;//参加考试人数
		Map<Integer,BigDecimal> levelMapDecimal = new LinkedHashMap<Integer, BigDecimal>();
		for (int i = 0; i < level.size(); i++) {			
			BigDecimal initBigDecimal =  size.multiply(level.get(i)).setScale(2,RoundingMode.HALF_UP);			
			levelMapDecimal.put(i,initBigDecimal);
		}
		return levelMapDecimal;
	}
	
	
	/**
	 * @param levelMap
	 * @param schoolName
	 * @return
	 */
	private Map<Integer,List<AverageExcelVo>> getLevelMapBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,String schoolName) {
		Map<Integer, List<AverageExcelVo>> schoolLevelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();				
		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			List<AverageExcelVo> innerList = new ArrayList<AverageExcelVo>();
			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				schoolLevelMap.put(level, innerList);
			}else {
				
				for (int i =0;i<averageExcelVos.size();i++) {
				   if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
					   innerList.add(averageExcelVos.get(i));
				   }
				}
				schoolLevelMap.put(level, innerList);			    
			}									
		}
		return schoolLevelMap;
	}
	
	/**
	 * @param levelMap
	 * @param schoolName
	 * 某一个学校的指标
	 */
	private Map<Integer, BigDecimal> getLevelMapDecimalBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,Map<Integer,BigDecimal> levelMapDecimal,String schoolName) {
		Map<Integer, BigDecimal> schoolLevelMapDecimal = new LinkedHashMap<Integer, BigDecimal>();
		BigDecimal zero = new BigDecimal("0");	
		BigDecimal one = new BigDecimal("1");	
		BigDecimal start = new BigDecimal("0");	
		
		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				schoolLevelMapDecimal.put(level, zero);
			}else {
				//最后一名名次
				int lastRank = averageExcelVos.get(averageExcelVos.size()-1).getRank();
				int first = 0 ;//第一个等于最后一名的,用来计算同名的总人数
				int schoolCount = 0 ;//该学校的同名人数
				for (int i =0;i<averageExcelVos.size();i++) {
					if (averageExcelVos.get(i).getRank() != lastRank) {
						//不等于最后一名则直接加1
						if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
							start = start.add(one);
						}
					}else {
						//等于最后一名需要算出同名的总人数以及某个学校的人数
						if (first == 0) {
							first = i;
						}
						if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
							schoolCount ++ ;
						}												
					}
				}
			    BigDecimal countDecimal = new BigDecimal(averageExcelVos.size()-first);//同分总人数  
			    BigDecimal schoolCountDecimal = new BigDecimal(schoolCount);//该校同分总人数
			    BigDecimal initDecimal = levelMapDecimal.get(level);//初始指标
			    BigDecimal firstDecimal = new BigDecimal(first);
			    // (初始指标-同分第一个)/同分总人数*该校同分总人数
			    BigDecimal bigDecimal = (initDecimal.subtract(firstDecimal)).divide(countDecimal).multiply(schoolCountDecimal).setScale(2, RoundingMode.HALF_UP);
			    schoolLevelMapDecimal.put(level, bigDecimal.add(start));			    								
			}									
		}
		return schoolLevelMapDecimal;
	}
	
	
	
	
	

	//计算累数
	private void caculateAverageSum(Map<String, Map<String, BigDecimal>> map) {
		for (String	key  : map.keySet()) {
			int i = Integer.valueOf(key.substring(1));   
			Map<String, BigDecimal> innerMap = map.get(key);
			int size = innerMap.size();
				//1 --> 22
				BigDecimal allLevel = new BigDecimal("0");
				BigDecimal allLevelSum = new BigDecimal("0");
				for(int j = 1 ;j<=size;j++) {
					//1--> 11
					BigDecimal sum = new BigDecimal("0");
					for(int m=1;m<=i;m++) {
						sum = sum.add(map.get("A"+m).get("level"+j));												
					}
					innerMap.put("levelSum"+j, sum);					
					allLevel = allLevel.add(map.get("A"+i).get("level"+j));					
				}
				
				innerMap.put("allLevel", allLevel);
				if (i==1) {
					innerMap.put("allLevelSum", allLevel);
				}else {
					innerMap.put("allLevelSum", map.get("A"+(i-1)).get("allLevelSum").add(allLevel));
				}
		}				
	}

}
