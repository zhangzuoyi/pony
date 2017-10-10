package com.zzy.pony.exam.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.tiles.template.PutAttributeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.exam.dao.AverageIndexDao;
import com.zzy.pony.exam.mapper.AverageIndexMapper;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageIndexRowVo;
import com.zzy.pony.exam.vo.AverageIndexVo;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.vo.ExamResultVo;

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
	public void calculateAverage(int examId, int gradeId) {
		// TODO Auto-generated method stub
		// 获取考试科目
		SchoolYear schoolYear = schoolYearService.getCurrent();
		List<Subject> subjects = subjectService.findByExam(examId);
		List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(),gradeId);
		BigDecimal one = new BigDecimal("1");
		for (Subject subject : subjects) {
			//key section(A1) value(key seq)
			Map<String, Map<String, Float>> map = new LinkedHashMap<String, Map<String,Float>>();
			//前一个段位剩下的
			Map<String, Map<String, Float>> mapRemain = new HashMap<String, Map<String,Float>>();
			BigDecimal remainCount = new BigDecimal(String.valueOf(0)); 
			List<Float> indexValues = new ArrayList<Float>();			
			List<AverageIndexVo> averageIndexVos = averageIndexMapper.findByExamAndGradeAndSubject(examId, gradeId,subject.getSubjectId());
			List<ExamResultVo> examResultVos = examResultMapper.findByExamAndGradeAndSubject(examId, gradeId, subject.getSubjectId());
			for (AverageIndexVo averageIndexVo : averageIndexVos) {
				indexValues.add(averageIndexVo.getIndexValue());
				Map<String, Float> innerMap = new LinkedHashMap<String, Float>();
				for(int i = 0;i<schoolClasses.size();i++) {
					innerMap.put("level"+schoolClasses.get(i).getSeq(), 0f);
				}
				map.put(averageIndexVo.getSection(), innerMap);
			}
			int j=0;//段位控制
			BigDecimal indexValue = new BigDecimal("0") ;
			boolean flag = true;
			
			for(int i=0;i<examResultVos.size();i++) {
				int classSeq = examResultVos.get(i).getClassSeq();
				BigDecimal indexValueDecimal = new BigDecimal("0")  ;
				int indexValueFloor = 0;
				BigDecimal indexValueFloorDecimal = new BigDecimal("0");
				int indexValueCeil =0;
				if (flag) {
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
				}
				flag = false;

				//BigDecimal indexValueFloorCeil = new BigDecimal(String.valueOf(indexValueCeil));
				
				//前一个段位剩下的
				if(j>0 && remainCount.compareTo(new BigDecimal("0"))>0) {
					if (remainCount.compareTo(indexValueDecimal)>0) {
						int start = j;
						//剩下的比待分配的段位还多
						//@todo 多余跨档位处理
						BigDecimal bigDecimal = indexValueDecimal.add(new BigDecimal(String.valueOf(indexValues.get(j+1))));
						while(remainCount.compareTo(bigDecimal)>0) {
							j++;
							bigDecimal = indexValueDecimal.add(new BigDecimal(String.valueOf(indexValues.get(j+1))));											
						}
						for(int m=start;m<j;m++) {
							if (m==j-1) {
								BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average =  remainCount.divide(count,2,RoundingMode.DOWN);
								BigDecimal remain = remainCount.subtract(average.multiply(count)); //多余的   

								for (String key : mapRemain.get("A"+(m+1)).keySet()) {
									if (map.get("A"+(m+1))!= null && map.get("A"+(m+1)).get(key) != null) {
										//档数
										map.get("A"+(m+1)).put(key, map.get("A"+(m+1)).get(key)+average.floatValue());									
									}else {
										map.get("A"+(m+1)).put(key, average.floatValue());										
									}
								}														
							}else {
								BigDecimal total = new BigDecimal(indexValues.get(m).toString()) ;
								BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average =  total.divide(count,2,RoundingMode.DOWN);
								BigDecimal remain = total.subtract(average.multiply(count)); //多余的   
								for (String key : mapRemain.get("A"+(m+1)).keySet()) {
									if (map.get("A"+(m+1))!= null && map.get("A"+(m+1)).get(key) != null) {
										//档数
										map.get("A"+(m+1)).put(key, map.get("A"+(m+1)).get(key)+average.floatValue());
										Map<String, Float> innerMap = new HashMap<String, Float>();
										innerMap.put(key, mapRemain.get("A"+(m+1)).get(key)-average.floatValue());
										mapRemain.put("A"+(m+2),innerMap);
									}else {
										map.get("A"+(m+1)).put(key, average.floatValue());	
										Map<String, Float> innerMap = new HashMap<String, Float>();
										innerMap.put(key, mapRemain.get("A"+(m+1)).get(key)-average.floatValue());
										mapRemain.put("A"+(m+2),innerMap);
									}
									remainCount.subtract(total);
								}
							}
						}
						
					}else {
						for (String key : mapRemain.get("A"+(j+1)).keySet()) {
							if (map.get("A"+(j+1))!= null && map.get("A"+(j+1)).get(key) != null) {
								//档数
								map.get("A"+(j+1)).put(key, map.get("A"+(j+1)).get(key)+mapRemain.get("A"+(j+1)).get(key));
							}else {
								map.get("A"+(j+1)).put(key, mapRemain.get("A"+(j+1)).get(key));								
							}
						}
						mapRemain.clear();
					}
					
				}
				
				BigDecimal remainIndexValueDecimal =   indexValueDecimal.subtract(remainCount);
				
				remainCount = remainCount.subtract(remainCount);//归0


				//6.82   在6后面出现相等  
				if (examResultVos.get(indexValueFloor).getGradeRank() != examResultVos.get(indexValueCeil).getGradeRank()) {
					//小于档位
					if (i<=indexValueFloor) {						
						if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
							//档数
							map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+1);								
						}else {
							map.get("A"+(j+1)).put("level"+classSeq, 1f);								
						}
						remainIndexValueDecimal.subtract(one); 
					}
					//等于档位
					if (i==indexValueCeil) {
						int count = 1;//相同排名数
						while(examResultVos.get(i).getGradeRank() == examResultVos.get(i+1).getGradeRank()) {
							i++;
							count++;
						}
						BigDecimal total = remainIndexValueDecimal;
						BigDecimal average =  total.divide(new BigDecimal(count));
						BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); //多余的   
						for (int m=indexValueCeil;m<=i;m++) {
							classSeq = examResultVos.get(m).getClassSeq();
							//@todo 除不尽的默认加在第一个
							if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
								//档数
								if (m==indexValueCeil) {
									map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+average.add(remain).floatValue());								
								}else {
									map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+average.floatValue());								
								}
							}else {
								map.get("A"+(j+1)).put("level"+classSeq, average.floatValue());								
							}
							//剩余的
							if (mapRemain.get("A"+(j+2)) != null && mapRemain.get("A"+(j+2)).get("level"+classSeq) != null) {
								//档数
								mapRemain.get("A"+(j+2)).put("level"+classSeq, mapRemain.get("A"+(j+2)).get("level"+classSeq)+one.subtract(average).floatValue());								
								remainCount = remainCount.add(one.subtract(average));
							}else {
								if (mapRemain.get("A"+(j+2)) == null) {
									mapRemain.put("A"+(j+2), new HashMap<String, Float>());
									mapRemain.get("A"+(j+2)).put("level"+classSeq, one.subtract(average.add(remain)).floatValue());	
									remainCount = remainCount.add(one.subtract(average.add(remain)));

								}else {
									mapRemain.get("A"+(j+2)).put("level"+classSeq, one.subtract(average).floatValue());	
									remainCount = remainCount.add(one.subtract(average));

								}								
							}
							
						}
						
						j++;
						flag = true;
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
								map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+1);								
							}else {
								map.get("A"+(j+1)).put("level"+classSeq, 1f);								
							}
							remainIndexValueDecimal.subtract(one);
						}else {
							int count = 1;
							int start = i;//最后一个不相等
							while(examResultVos.get(i).getGradeRank() == examResultVos.get(i+1).getGradeRank()) {
								i++;
								count++;
							}
							
							BigDecimal total = remainIndexValueDecimal;
							BigDecimal average =  total.divide(new BigDecimal(count),2,RoundingMode.DOWN);
							BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); //多余的   
							for (int m=start;m<=i;m++) {								
								classSeq = examResultVos.get(m).getClassSeq();
								//@todo 除不尽的默认加在第一个
								if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
									//档数
									if (m==start) {
										map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+average.add(remain).floatValue());								
									}else {
										map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+average.floatValue());								
									}
								}else {
									map.get("A"+(j+1)).put("level"+classSeq, average.floatValue());								
								}
								//剩余的
								if (mapRemain.get("A"+(j+2)) != null && mapRemain.get("A"+(j+2)).get("level"+classSeq) != null) {
									//档数
									mapRemain.get("A"+(j+2)).put("level"+classSeq, mapRemain.get("A"+(j+2)).get("level"+classSeq)+one.subtract(average).floatValue());								
									remainCount = remainCount.add(one.subtract(average));

								}else {
									if (mapRemain.get("A"+(j+2)) == null) {
										mapRemain.put("A"+(j+2), new HashMap<String, Float>());
										mapRemain.get("A"+(j+2)).put("level"+classSeq,  one.subtract(average.add(remain)).floatValue());
										remainCount = remainCount.add(one.subtract(average.add(remain)));
									}else {
										mapRemain.get("A"+(j+2)).put("level"+classSeq, one.subtract(average).floatValue());	
										remainCount = remainCount.add(one.subtract(average));

									}
								}

							}
							j++;
							flag = true;

						}				
				}
				
				
			}	
			for (String	 key : map.keySet()) {
				System.out.print(key+":");
				for (String innerKey : map.get(key).keySet()) {
					System.out.println(innerKey+":"+map.get(key).get(innerKey));
				}
			}
			
		}
		
	}

}
