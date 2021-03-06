package com.zzy.pony.exam.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.dao.AverageIndexDao;
import com.zzy.pony.exam.mapper.AverageIndexMapper;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.*;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.ReadExcelUtils;
import com.zzy.pony.vo.ExamResultVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
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

/**
 * @author WANGCHAO262
 *
 */
/**
 * @author WANGCHAO262
 *
 */
/**
 * @author WANGCHAO262
 *
 */
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
	public Map<Integer, Map<String, Map<String, BigDecimal>>> calculateAverage(int examId, int gradeId) {
		// TODO Auto-generated method stub
		// 获取考试科目
		SchoolYear schoolYear = schoolYearService.getCurrent();
		List<Subject> subjects = subjectService.findByExam(examId);
		List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(),
				gradeId);
		BigDecimal one = new BigDecimal("1");
		BigDecimal zero = new BigDecimal("0");
		Map<Integer, Map<String, Map<String, BigDecimal>>> result = new LinkedHashMap<Integer, Map<String, Map<String, BigDecimal>>>();

		for (Subject subject : subjects) {
			// key section(A1) value(key seq)
			Map<String, Map<String, BigDecimal>> map = new LinkedHashMap<String, Map<String, BigDecimal>>();
			// 前一个段位剩下的
			Map<String, Map<String, BigDecimal>> mapRemain = new HashMap<String, Map<String, BigDecimal>>();
			BigDecimal remainCount = new BigDecimal(String.valueOf(0));
			List<Float> indexValues = new ArrayList<Float>();
			List<AverageIndexVo> averageIndexVos = averageIndexMapper.findByExamAndGradeAndSubject(examId, gradeId,
					subject.getSubjectId());
			List<ExamResultVo> examResultVos = examResultMapper.findByExamAndGradeAndSubject(examId, gradeId,
					subject.getSubjectId());
			for (AverageIndexVo averageIndexVo : averageIndexVos) {
				indexValues.add(averageIndexVo.getIndexValue());
				Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
				for (int i = 0; i < schoolClasses.size(); i++) {
					innerMap.put("level" + schoolClasses.get(i).getSeq(), zero);
				}
				map.put(averageIndexVo.getSection(), innerMap);
			}
			int j = 0;// 段位控制
			BigDecimal indexValue = new BigDecimal(indexValues.get(0).toString());
			int lastj = j;
			BigDecimal remainIndexValueDecimal = new BigDecimal(indexValues.get(0).toString());
			BigDecimal remainCountNum = new BigDecimal("0");

			for (int i = 0; i < examResultVos.size(); i++) {
				int classSeq = examResultVos.get(i).getClassSeq();
				BigDecimal indexValueDecimal = new BigDecimal("0");
				int indexValueFloor = 0;
				BigDecimal indexValueFloorDecimal = new BigDecimal("0");

				int indexValueCeil = 0;
				if (lastj != j) {
					indexValue = indexValue.add(new BigDecimal(indexValues.get(j).toString()));
					indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
					indexValueFloor = (int) Math.floor(indexValue.floatValue());
					if (indexValueFloor >= examResultVos.size()) {
						indexValueFloor = examResultVos.size() - 1;
					}
					indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
					indexValueCeil = (int) Math.ceil(indexValue.floatValue());
					if (indexValueCeil >= examResultVos.size()) {
						indexValueCeil = examResultVos.size() - 1;
					}
					lastj = j;
				} else {
					indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
					indexValueFloor = (int) Math.floor(indexValue.floatValue());
					if (indexValueFloor >= examResultVos.size()) {
						indexValueFloor = examResultVos.size() - 1;
					}
					indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
					indexValueCeil = (int) Math.ceil(indexValue.floatValue());
					if (indexValueCeil >= examResultVos.size()) {
						indexValueCeil = examResultVos.size() - 1;
					}
				}

				// BigDecimal indexValueFloorCeil = new
				// BigDecimal(String.valueOf(indexValueCeil));

				// 前一个段位剩下的
				if (j > 0 && remainCount.compareTo(new BigDecimal("0")) > 0) {
					if (remainCount.compareTo(indexValueDecimal) > 0) {
						int start = j;
						// 剩下的比待分配的段位还多
						BigDecimal bigDecimal = indexValueDecimal;
						while ((j + 1) < indexValues.size() && remainCount.compareTo(bigDecimal) > 0) {
							bigDecimal = indexValueDecimal.add(new BigDecimal(String.valueOf(indexValues.get(j + 1))));
							j++;
						}
						for (int m = start; m <= j; m++) {
							if (m == j) {
								// BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average = remainCount.divide(remainCountNum, 2, RoundingMode.DOWN);
								BigDecimal remain = remainCount.subtract(average.multiply(remainCountNum)); // 多余的
								int first = 1;
								for (String key : mapRemain.get("A" + (m + 1)).keySet()) {
									if (map.get("A" + (m + 1)) != null && map.get("A" + (m + 1)).get(key) != null) {
										// 档数
										if (first == 2) {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average.add(remain)));
										} else {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average.add(remain)));
										}
									} else {
										map.get("A" + (m + 1)).put(key, average);
									}
									first++;
								}
							} else {
								BigDecimal total = new BigDecimal(indexValues.get(m).toString());
								// BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average = total.divide(remainCountNum, 2, RoundingMode.DOWN);
								BigDecimal remain = total.subtract(average.multiply(remainCountNum)); // 多余的
								int first = 1;
								for (String key : mapRemain.get("A" + (m + 1)).keySet()) {
									if (map.get("A" + (m + 1)) != null && map.get("A" + (m + 1)).get(key) != null) {
										// 档数
										if (first == 2) {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average.add(remain)));
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A" + (m + 1)).get(key)
													.subtract(average.add(remain)));
											if (mapRemain.get("A" + (m + 2)) != null) {
												mapRemain.get("A" + (m + 2)).put(key, mapRemain.get("A" + (m + 1))
														.get(key).subtract(average.add(remain)));
											} else {
												mapRemain.put("A" + (m + 2), innerMap);
											}

										} else {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average));
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A" + (m + 1)).get(key).subtract(average));
											mapRemain.put("A" + (m + 2), innerMap);
										}
									} else {
										map.get("A" + (m + 1)).put(key, average);
										Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
										innerMap.put(key, mapRemain.get("A" + (m + 1)).get(key).subtract(average));
										mapRemain.put("A" + (m + 2), innerMap);

									}
									first++;
								}
								remainCount = remainCount.subtract(total);
							}
						}

						indexValue = indexValue.add(new BigDecimal(indexValues.get(j).toString()));
						indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
						indexValueFloor = (int) Math.floor(indexValue.floatValue());
						if (indexValueFloor >= examResultVos.size()) {
							indexValueFloor = examResultVos.size() - 1;
						}
						indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
						indexValueCeil = (int) Math.ceil(indexValue.floatValue());
						if (indexValueCeil >= examResultVos.size()) {
							indexValueCeil = examResultVos.size() - 1;
						}
						lastj = j;

					} else {
						for (String key : mapRemain.get("A" + (j + 1)).keySet()) {
							if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get(key) != null) {
								// 档数
								map.get("A" + (j + 1)).put(key,
										map.get("A" + (j + 1)).get(key).add(mapRemain.get("A" + (j + 1)).get(key)));
							} else {
								map.get("A" + (j + 1)).put(key, mapRemain.get("A" + (j + 1)).get(key));
							}
						}
						mapRemain.clear();
					}
					remainIndexValueDecimal = indexValueDecimal.subtract(remainCount);
					remainCount = remainCount.subtract(remainCount);// 归0
				}

				// 6.82 在6后面出现相等
				if (examResultVos.get(indexValueFloor).getGradeRank() != examResultVos.get(indexValueCeil)
						.getGradeRank()) {
					// 小于档位
					if (i <= indexValueFloor) {
						if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get("level" + classSeq) != null) {
							// 档数
							map.get("A" + (j + 1)).put("level" + classSeq,
									map.get("A" + (j + 1)).get("level" + classSeq).add(one));
						} else {
							map.get("A" + (j + 1)).put("level" + classSeq, one);
						}
						remainIndexValueDecimal = remainIndexValueDecimal.subtract(one);
					}
					// 等于档位
					if (i == indexValueCeil) {
						int count = 1;// 相同排名数
						while (examResultVos.get(i).getGradeRank() == examResultVos.get(i + 1).getGradeRank()) {
							i++;
							count++;
						}
						BigDecimal total = remainIndexValueDecimal;
						BigDecimal average = total.divide(new BigDecimal(count), 2, RoundingMode.DOWN);
						BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); // 多余的
						remainCountNum = new BigDecimal(count);
						for (int m = indexValueCeil; m <= i; m++) {
							classSeq = examResultVos.get(m).getClassSeq();
							// @todo 除不尽的默认加在第一个
							if (map.get("A" + (j + 1)) != null
									&& map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								if (m == indexValueCeil) {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average.add(remain)));
								} else {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average));
								}
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, average);
							}
							// 剩余的
							if (mapRemain.get("A" + (j + 2)) != null
									&& mapRemain.get("A" + (j + 2)).get("level" + classSeq) != null) {
								// 档数
								mapRemain.get("A" + (j + 2)).put("level" + classSeq, mapRemain.get("A" + (j + 2))
										.get("level" + classSeq).add(one.subtract(average)));
								remainCount = remainCount.add(one.subtract(average));
							} else {
								if (mapRemain.get("A" + (j + 2)) == null) {
									mapRemain.put("A" + (j + 2), new HashMap<String, BigDecimal>());
									mapRemain.get("A" + (j + 2)).put("level" + classSeq,
											one.subtract(average.add(remain)));
									remainCount = remainCount.add(one.subtract(average.add(remain)));

								} else {
									mapRemain.get("A" + (j + 2)).put("level" + classSeq, one.subtract(average));
									remainCount = remainCount.add(one.subtract(average));

								}
							}

						}

						j++;
					}
				}
				// 6.82 在6前后面出现相等
				else {
					int gradeRank = examResultVos.get(indexValueFloor).getGradeRank();
					/*
					 * int m = indexValueFloor;//开始相等 int n = indexValueCeil;//结束相等 while() {
					 * 
					 * }
					 */
					if (examResultVos.get(i).getGradeRank() != gradeRank) {
						if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get("level" + classSeq) != null) {
							// 档数
							map.get("A" + (j + 1)).put("level" + classSeq,
									map.get("A" + (j + 1)).get("level" + classSeq).add(one));
						} else {
							map.get("A" + (j + 1)).put("level" + classSeq, one);
						}
						remainIndexValueDecimal = remainIndexValueDecimal.subtract(one);
					} else {
						int count = 1;
						int start = i;// 最后一个不相等
						while ((i + 1) < examResultVos.size()
								&& examResultVos.get(i).getGradeRank() == examResultVos.get(i + 1).getGradeRank()) {
							i++;
							count++;
						}
						remainCountNum = new BigDecimal(count);
						BigDecimal total = remainIndexValueDecimal;
						BigDecimal average = total.divide(new BigDecimal(count), 2, RoundingMode.DOWN);
						BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); // 多余的
						for (int m = start; m <= i; m++) {
							classSeq = examResultVos.get(m).getClassSeq();
							// @todo 除不尽的默认加在第一个
							if (map.get("A" + (j + 1)) != null
									&& map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								if (m == start) {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average.add(remain)));
								} else {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average));
								}
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, average);
							}
							// 剩余的
							if (mapRemain.get("A" + (j + 2)) != null
									&& mapRemain.get("A" + (j + 2)).get("level" + classSeq) != null) {
								// 档数
								mapRemain.get("A" + (j + 2)).put("level" + classSeq, mapRemain.get("A" + (j + 2))
										.get("level" + classSeq).add(one.subtract(average)));
								remainCount = remainCount.add(one.subtract(average));

							} else {
								if (mapRemain.get("A" + (j + 2)) == null) {
									mapRemain.put("A" + (j + 2), new HashMap<String, BigDecimal>());
									mapRemain.get("A" + (j + 2)).put("level" + classSeq,
											one.subtract(average.add(remain)));
									remainCount = remainCount.add(one.subtract(average.add(remain)));
								} else {
									mapRemain.get("A" + (j + 2)).put("level" + classSeq, one.subtract(average));
									remainCount = remainCount.add(one.subtract(average));

								}
							}

						}
						j++;

					}
				}

			}
			for (String key : map.keySet()) {
				System.out.print(key + ":");
				for (String innerKey : map.get(key).keySet()) {
					System.out.println(innerKey + ":" + map.get(key).get(innerKey).floatValue());
				}
			}
			caculateAverageSum(map);
			result.put(subject.getSubjectId(), map);
		}
		return result;
	}

   /**
     * @Author : Administrator
     * @Description : 
     
   */
	@Override
	public Map<String, Map<String, Map<String, BigDecimal>>> calculateNewAverage(List<String> subjects,Set<String> schoolClasses,
			Map<String, Map<String, BigDecimal>> subjectLevelMap,Map<String, List<AverageNewVo>> subjectResults,Map<String,Integer> levelWeight) {
		// TODO Auto-generated method stub
        Map<String,Map<String,Map<String,BigDecimal>>> result = new LinkedHashMap<String, Map<String, Map<String, BigDecimal>>>();
		BigDecimal zero = new BigDecimal("0");
		BigDecimal one = new BigDecimal("1");
       List<String> classCodes = new ArrayList<String>(schoolClasses);


        for (String subject:subjects) {
            Map<String, Map<String, BigDecimal>> innerResult = new LinkedHashMap<String, Map<String, BigDecimal>>();
            BigDecimal remainDecimal = new BigDecimal("0");// 上一等级整体遗留的 = 上一等级人数-上一等级指标数
            BigDecimal allCount = new BigDecimal(0);// 人数累加
            BigDecimal allLevelCount = new BigDecimal(0);// 指标累加
            Map<String, BigDecimal> remainDecimalMap = new HashMap<String, BigDecimal>();
            BigDecimal remainCount = new BigDecimal("0");// 上一等级总人数
            Map<String, BigDecimal> remainCountMap = new HashMap<String, BigDecimal>();// 上一等级各个班人数
            Boolean flag = true;

            List<AverageNewVo> averageNewVos = subjectResults.get(subject);
            Map<String,BigDecimal> levelMap = subjectLevelMap.get(subject);
            sortAverageNewVo(averageNewVos);
            List<String> levels = new ArrayList<String>(levelMap.keySet());
            Map<String,List<AverageNewVo>> subjectLevel =  getNewLevelMap(averageNewVos,levelMap);
            for (String level : subjectLevel.keySet()) {
                List<AverageNewVo> averageNewVoList = subjectLevel.get(level);
                BigDecimal levelDecimal = levelMap.get(level);
                allLevelCount = allLevelCount.add(levelDecimal);
                levelDecimal = levelDecimal.subtract(remainDecimal);// 当前可分配 = 初始分配-上一等级遗留
                int last = averageNewVoList.size() - 1;// 最后一个的
                int first = 0;// 第一个的
                int total = averageNewVoList.size();// 总人数
                BigDecimal totalDecimal = new BigDecimal(total);

                //add
                if(allCount.add(totalDecimal).intValue() == averageNewVos.size()){
                    levelDecimal =  new BigDecimal(averageNewVos.size()).subtract(allCount);
                    remainDecimal = BigDecimal.ZERO;
                    allCount = allCount.add(totalDecimal);
                }else{
                    allCount = allCount.add(totalDecimal);
                    remainDecimal = allCount.subtract(allLevelCount);// 留给下一等级的
                }





                Map<String, BigDecimal> remainMap = new HashMap<String, BigDecimal>();
                BigDecimal averageDecimal = zero;
                BigDecimal firstDecimal = zero;
                BigDecimal availabelLevel = zero;
                BigDecimal sameCount = zero;
                BigDecimal lastLevelDecimal = zero;
                for (String classCode : classCodes) {
                    Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
                    BigDecimal count = new BigDecimal("0");
                    BigDecimal classSameCount = new BigDecimal("0");// 该班重名人数
                    if (averageNewVoList == null || averageNewVoList.size() == 0) {
                        // 如果为空，则应该把remainDecimal置值,如果remainDecimal高于可分配的
                        if (levelDecimal.compareTo(zero) >= 0) {
                            if (remainDecimalMap.containsKey(classCode)) {
                                innerMap.put( level, remainDecimalMap.get(classCode));
                                remainDecimalMap.put(classCode, zero);
                            } else {
                                innerMap.put( level, zero);
                            }
                            flag = true;
                        } else {
                            // 上一等级遗留大于当前等级可分配,需要根据人数平均分
                            BigDecimal average = levelMap.get(level).divide(remainCount, RoundingMode.HALF_UP);
                            if (remainCountMap.containsKey(classCode)) {
                                BigDecimal classRemain = average.multiply(remainCountMap.get(classCode)).setScale(2,
                                        RoundingMode.HALF_UP);
                                innerMap.put( level, classRemain);
                                remainDecimalMap.put(classCode, remainDecimalMap.get(classCode).subtract(classRemain));
                            } else {
                                innerMap.put(level, zero);
                            }
                            flag = false;
                        }
                    } else {
                        int lastRank = averageNewVoList.get(last).getGradeRank();
                        for (int i = 0; i < averageNewVoList.size(); i++) {
                            if (averageNewVoList.get(i).getClassName().equalsIgnoreCase(classCode)) {
                                if (averageNewVoList.get(i).getGradeRank() != lastRank) {
                                    count = count.add(one);
                                } else {
                                    classSameCount = classSameCount.add(one);
                                    if (remainMap.containsKey(classCode)) {
                                        remainMap.put(classCode, remainMap.get(classCode).add(one));
                                    } else {
                                        remainMap.put(classCode, one);
                                    }
                                }
                            }
                            if (averageNewVoList.get(i).getGradeRank() == lastRank && first == 0) {
                                first = i;
                            }
                            // 有可能某一个等级全部都是重名的
                            if (averageNewVoList.get(0).getGradeRank() == lastRank) {
                                first = 0;
                            }

                        }
                        firstDecimal = new BigDecimal(first);
                        // 可分配指标 / 总体重名人数 * 该班重名人数
                        availabelLevel = levelDecimal.subtract(firstDecimal);// 可分配指标
                        sameCount = totalDecimal.subtract(firstDecimal);// 总体重名人数
                        averageDecimal = availabelLevel.divide(sameCount, RoundingMode.HALF_UP);// 相同的每个能分到的
                        if (remainDecimalMap.get(classCode) != null) {
                            lastLevelDecimal = remainDecimalMap.get(classCode);
                        } else {
                            lastLevelDecimal = zero;
                        }
                        innerMap.put( level, lastLevelDecimal.add(count).add(averageDecimal.multiply(classSameCount))
                                .setScale(2, RoundingMode.HALF_UP));

                        flag = true;

                    }

                    if (innerResult.containsKey(classCode)) {
                        innerResult.get(classCode).putAll(innerMap);
                    } else {
                        innerResult.put(classCode, innerMap);
                    }
                }
                if (flag) {
                    remainDecimalMap.clear();// 清空上一等级遗留
                    remainCount = sameCount;
                    remainCountMap.clear();
                    // 留给下一等级的
                    for (String key : remainMap.keySet()) {
                        remainDecimalMap.put(key, remainMap.get(key).multiply(one.subtract(averageDecimal)).setScale(2,
                                RoundingMode.HALF_UP));
                        remainCountMap.put(key, remainMap.get(key));
                    }
                }

            }

            caculateNewAverageSum(innerResult,classCodes,levels,levelWeight);// 计算累数以及M值
            result.put(subject, innerResult);

        }




        



		return result;
		/*BigDecimal one = new BigDecimal("1");
		BigDecimal zero = new BigDecimal("0");
		Map<String, Map<String, Map<String, BigDecimal>>> result = new LinkedHashMap<String, Map<String, Map<String, BigDecimal>>>();

		for (String subject : subjects) {
			// key section(A1) value(key seq)
			Map<String, Map<String, BigDecimal>> map = new LinkedHashMap<String, Map<String, BigDecimal>>();
			// 前一个段位剩下的
			Map<String, Map<String, BigDecimal>> mapRemain = new HashMap<String, Map<String, BigDecimal>>();
			BigDecimal remainCount = new BigDecimal(String.valueOf(0));
			List<Float> indexValues = new ArrayList<Float>();		
			Map<String, BigDecimal> levelMap = subjectLevelMap.get(subject);
			List<AverageNewVo> examResultVos = subjectResults.get(subject);
			sortAverageNewVo(examResultVos);
			
			for (String level : levelMap.keySet()) {
				indexValues.add(levelMap.get(level).floatValue());
				Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
				Iterator<String> iterator =  schoolClasses.iterator();
				while(iterator.hasNext()) {
					innerMap.put("level" + iterator.next(), zero);
				}		
				map.put(level, innerMap);
			}
			int j = 0;// 段位控制
			BigDecimal indexValue = new BigDecimal(indexValues.get(0).toString());
			int lastj = j;
			BigDecimal remainIndexValueDecimal = new BigDecimal(indexValues.get(0).toString());
			BigDecimal remainCountNum = new BigDecimal("0");

			for (int i = 0; i < examResultVos.size(); i++) {
				String classSeq = examResultVos.get(i).getClassName();
				BigDecimal indexValueDecimal = new BigDecimal("0");
				int indexValueFloor = 0;
				BigDecimal indexValueFloorDecimal = new BigDecimal("0");

				int indexValueCeil = 0;
				
				if (i == 495) {
					System.out.println("495");
				}
				
				if (lastj != j) {
					indexValue = indexValue.add(new BigDecimal(indexValues.get(j).toString()));
					indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
					indexValueFloor = (int) Math.floor(indexValue.floatValue());
					if (indexValueFloor >= examResultVos.size()) {
						indexValueFloor = examResultVos.size() - 2;
					}
					indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
					indexValueCeil = (int) Math.ceil(indexValue.floatValue());
					if (indexValueCeil >= examResultVos.size()) {
						indexValueCeil = examResultVos.size() - 1;
					}
					lastj = j;
				} else {
					indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
					indexValueFloor = (int) Math.floor(indexValue.floatValue());
					if (indexValueFloor >= examResultVos.size()) {
						indexValueFloor = examResultVos.size() - 2;
					}
					indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
					indexValueCeil = (int) Math.ceil(indexValue.floatValue());
					if (indexValueCeil >= examResultVos.size()) {
						indexValueCeil = examResultVos.size() - 1;
					}
				}

				// BigDecimal indexValueFloorCeil = new
				// BigDecimal(String.valueOf(indexValueCeil));

				// 前一个段位剩下的
				if (j > 0 && remainCount.compareTo(new BigDecimal("0")) > 0) {
					if (remainCount.compareTo(indexValueDecimal) > 0) {
						int start = j;
						// 剩下的比待分配的段位还多
						BigDecimal bigDecimal = indexValueDecimal;
						while ((j + 1) < indexValues.size() && remainCount.compareTo(bigDecimal) > 0) {
							bigDecimal = indexValueDecimal.add(new BigDecimal(String.valueOf(indexValues.get(j + 1))));
							j++;
						}
						for (int m = start; m <= j; m++) {
							if (m == j) {
								// BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average = remainCount.divide(remainCountNum, 2, RoundingMode.DOWN);
								BigDecimal remain = remainCount.subtract(average.multiply(remainCountNum)); // 多余的
								int first = 1;
								for (String key : mapRemain.get("A" + (m + 1)).keySet()) {
									if (map.get("A" + (m + 1)) != null && map.get("A" + (m + 1)).get(key) != null) {
										// 档数
										if (first == 2) {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average.add(remain)));
										} else {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average.add(remain)));
										}
									} else {
										map.get("A" + (m + 1)).put(key, average);
									}
									first++;
								}
							} else {
								BigDecimal total = new BigDecimal(indexValues.get(m).toString());
								// BigDecimal count = new BigDecimal(mapRemain.get("A"+(start+1)).size());
								BigDecimal average = total.divide(remainCountNum, 2, RoundingMode.DOWN);
								BigDecimal remain = total.subtract(average.multiply(remainCountNum)); // 多余的
								int first = 1;
								for (String key : mapRemain.get("A" + (m + 1)).keySet()) {
									if (map.get("A" + (m + 1)) != null && map.get("A" + (m + 1)).get(key) != null) {
										// 档数
										if (first == 2) {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average.add(remain)));
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A" + (m + 1)).get(key)
													.subtract(average.add(remain)));
											if (mapRemain.get("A" + (m + 2)) != null) {
												mapRemain.get("A" + (m + 2)).put(key, mapRemain.get("A" + (m + 1))
														.get(key).subtract(average.add(remain)));
											} else {
												mapRemain.put("A" + (m + 2), innerMap);
											}

										} else {
											map.get("A" + (m + 1)).put(key,
													map.get("A" + (m + 1)).get(key).add(average));
											Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
											innerMap.put(key, mapRemain.get("A" + (m + 1)).get(key).subtract(average));
											mapRemain.put("A" + (m + 2), innerMap);
										}
									} else {
										map.get("A" + (m + 1)).put(key, average);
										Map<String, BigDecimal> innerMap = new HashMap<String, BigDecimal>();
										innerMap.put(key, mapRemain.get("A" + (m + 1)).get(key).subtract(average));
										mapRemain.put("A" + (m + 2), innerMap);

									}
									first++;
								}
								remainCount = remainCount.subtract(total);
							}
						}

						indexValue = indexValue.add(new BigDecimal(indexValues.get(j).toString()));
						indexValueDecimal = new BigDecimal(indexValues.get(j).toString());
						indexValueFloor = (int) Math.floor(indexValue.floatValue());
						if (indexValueFloor >= examResultVos.size()) {
							indexValueFloor = examResultVos.size() - 1;
						}
						indexValueFloorDecimal = new BigDecimal(String.valueOf(indexValueFloor));
						indexValueCeil = (int) Math.ceil(indexValue.floatValue());
						if (indexValueCeil >= examResultVos.size()) {
							indexValueCeil = examResultVos.size() - 1;
						}
						lastj = j;

					} else {
						for (String key : mapRemain.get("A" + (j + 1)).keySet()) {
							if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get(key) != null) {
								// 档数
								map.get("A" + (j + 1)).put(key,
										map.get("A" + (j + 1)).get(key).add(mapRemain.get("A" + (j + 1)).get(key)));
							} else {
								map.get("A" + (j + 1)).put(key, mapRemain.get("A" + (j + 1)).get(key));
							}
						}
						mapRemain.clear();
					}
					remainIndexValueDecimal = indexValueDecimal.subtract(remainCount);
					remainCount = remainCount.subtract(remainCount);// 归0
				}
				
				// 6.82 在6后面出现相等				
				if (examResultVos.get(indexValueFloor).getGradeRank() != examResultVos.get(indexValueCeil)
						.getGradeRank()) {
					// 小于档位
					if (i <= indexValueFloor) {
						if (indexValue.subtract(new BigDecimal(i)).compareTo(one)<0) {
							if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								map.get("A" + (j + 1)).put("level" + classSeq,
										map.get("A" + (j + 1)).get("level" + classSeq).add(indexValue.subtract(new BigDecimal(i))));
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, indexValue.subtract(new BigDecimal(i)));
							}
							remainIndexValueDecimal = remainIndexValueDecimal.subtract(indexValue.subtract(new BigDecimal(i)));
							
						}else {
							if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								map.get("A" + (j + 1)).put("level" + classSeq,
										map.get("A" + (j + 1)).get("level" + classSeq).add(one));
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, one);
							}
							remainIndexValueDecimal = remainIndexValueDecimal.subtract(one);
						}
						
					}
					// 等于档位
					if (i == indexValueCeil) {
						if (i == examResultVos.size()-1 ) {
							if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								map.get("A" + (j + 1)).put("level" + classSeq,
										map.get("A" + (j + 1)).get("level" + classSeq).add(one));
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, one);
							}
							continue;
						}
						int count = 1;// 相同排名数
						while (examResultVos.get(i).getGradeRank() == examResultVos.get(i + 1).getGradeRank()) {
							i++;
							count++;
						}
						BigDecimal total = remainIndexValueDecimal;
						BigDecimal average = total.divide(new BigDecimal(count), 2, RoundingMode.DOWN);
						BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); // 多余的
						remainCountNum = new BigDecimal(count);
						for (int m = indexValueCeil; m <= i; m++) {
							classSeq = examResultVos.get(m).getClassName();
							// @todo 除不尽的默认加在第一个
							if (map.get("A" + (j + 1)) != null
									&& map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								if (m == indexValueCeil) {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average.add(remain)));
								} else {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average));
								}
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, average);
							}
							// 剩余的
							if (mapRemain.get("A" + (j + 2)) != null
									&& mapRemain.get("A" + (j + 2)).get("level" + classSeq) != null) {
								// 档数
								mapRemain.get("A" + (j + 2)).put("level" + classSeq, mapRemain.get("A" + (j + 2))
										.get("level" + classSeq).add(one.subtract(average)));
								remainCount = remainCount.add(one.subtract(average));
							} else {
								if (mapRemain.get("A" + (j + 2)) == null) {
									mapRemain.put("A" + (j + 2), new HashMap<String, BigDecimal>());
									mapRemain.get("A" + (j + 2)).put("level" + classSeq,
											one.subtract(average.add(remain)));
									remainCount = remainCount.add(one.subtract(average.add(remain)));

								} else {
									mapRemain.get("A" + (j + 2)).put("level" + classSeq, one.subtract(average));
									remainCount = remainCount.add(one.subtract(average));

								}
							}

						}

						j++;
					}
				}
				// 6.82 在6前后面出现相等
				else {
					int gradeRank = examResultVos.get(indexValueFloor).getGradeRank();
					*//*
					 * int m = indexValueFloor;//开始相等 int n = indexValueCeil;//结束相等 while() {
					 * 
					 * }
					 *//*
					if (examResultVos.get(i).getGradeRank() != gradeRank) {
						if (map.get("A" + (j + 1)) != null && map.get("A" + (j + 1)).get("level" + classSeq) != null) {
							// 档数
							map.get("A" + (j + 1)).put("level" + classSeq,
									map.get("A" + (j + 1)).get("level" + classSeq).add(one));
						} else {
							map.get("A" + (j + 1)).put("level" + classSeq, one);
						}
						remainIndexValueDecimal = remainIndexValueDecimal.subtract(one);
					} else {
						int count = 1;
						int start = i;// 最后一个不相等
						while((i + 1) < examResultVos.size()
								&& examResultVos.get(i).getGradeRank() == examResultVos.get(i + 1).getGradeRank()) {
							i++;
							count++;
						}
						remainCountNum = new BigDecimal(count);
						BigDecimal total = remainIndexValueDecimal;
						BigDecimal average = total.divide(new BigDecimal(count), 2, RoundingMode.DOWN);
						BigDecimal remain = total.subtract(average.multiply(new BigDecimal(count))); // 多余的
						for (int m = start; m <= i; m++) {
							classSeq = examResultVos.get(m).getClassName();
							// @todo 除不尽的默认加在第一个
							if (map.get("A" + (j + 1)) != null
									&& map.get("A" + (j + 1)).get("level" + classSeq) != null) {
								// 档数
								if (m == start) {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average.add(remain)));
								} else {
									map.get("A" + (j + 1)).put("level" + classSeq,
											map.get("A" + (j + 1)).get("level" + classSeq).add(average));
								}
							} else {
								map.get("A" + (j + 1)).put("level" + classSeq, average);
							}
							// 剩余的
							if (mapRemain.get("A" + (j + 2)) != null
									&& mapRemain.get("A" + (j + 2)).get("level" + classSeq) != null) {
								// 档数
								mapRemain.get("A" + (j + 2)).put("level" + classSeq, mapRemain.get("A" + (j + 2))
										.get("level" + classSeq).add(one.subtract(average)));
								remainCount = remainCount.add(one.subtract(average));

							} else {
								if (mapRemain.get("A" + (j + 2)) == null) {
									mapRemain.put("A" + (j + 2), new HashMap<String, BigDecimal>());
									mapRemain.get("A" + (j + 2)).put("level" + classSeq,
											one.subtract(average.add(remain)));
									remainCount = remainCount.add(one.subtract(average.add(remain)));
								} else {
									mapRemain.get("A" + (j + 2)).put("level" + classSeq, one.subtract(average));
									remainCount = remainCount.add(one.subtract(average));

								}
							}

						}
						j++;

					}
				}

			}
			for (String key : map.keySet()) {
				System.out.print(key + ":");
				for (String innerKey : map.get(key).keySet()) {
					System.out.println(innerKey + ":" + map.get(key).get(innerKey).floatValue());
				}
			}
			caculateNewAverageSum(map);
			result.put(subject, map);
		}
		return result;*/
	}

	@Override
	public Map<String, Map<String, Map<String, BigDecimal>>> calculateAverageByFile(MultipartFile file) {
		// TODO Auto-generated method stub
		// subjectName level class
		Map<String, Map<String, Map<String, BigDecimal>>> result = new LinkedHashMap<String, Map<String, Map<String, BigDecimal>>>();

		Workbook wb = ReadExcelUtils.ReadExcelByFile(file);
		try {
			String[] titles = ReadExcelUtils.readExcelTitle(wb);
			for (int i = 3; i < titles.length; i++) {
				List<AverageExcelVo> averageExcelVos = getAverageExcelVo(wb, i, 0);
				sortAverageExcelVo(averageExcelVos);
				Map<Integer, List<AverageExcelVo>> levelMap = getLevelMap(averageExcelVos);
				Map<Integer, BigDecimal> levelMapDecimal = getLevelMapDecimal(averageExcelVos);
				Map<Integer, List<AverageExcelVo>> schoolLevelMap = getLevelMapBySchoolName(levelMap,
						Constants.SCHOOL_NAME);
				Map<Integer, BigDecimal> schoolLevelMapDecimal = getLevelMapDecimalBySchoolName(levelMap,
						levelMapDecimal, Constants.SCHOOL_NAME);
				List<String> classCodes = getClassCode(averageExcelVos, Constants.SCHOOL_NAME);
				Map<String, Map<String, BigDecimal>> innerMap = calculate(schoolLevelMap, schoolLevelMapDecimal,
						classCodes);
				result.put(titles[i], innerMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Map<String, BigDecimal>> calculate(Map<Integer, List<AverageExcelVo>> schoolLevelMap,
			Map<Integer, BigDecimal> schoolLevelMapDecimal, List<String> classCodes) {
		Map<String, Map<String, BigDecimal>> result = new LinkedHashMap<String, Map<String, BigDecimal>>();
		BigDecimal zero = new BigDecimal("0");
		BigDecimal one = new BigDecimal("1");
		BigDecimal remainDecimal = new BigDecimal("0");// 上一等级整体遗留的 = 上一等级人数-上一等级指标数
		BigDecimal allCount = new BigDecimal(0);// 人数累加
		BigDecimal allLevelCount = new BigDecimal(0);// 指标累加
		Map<String, BigDecimal> remainDecimalMap = new HashMap<String, BigDecimal>();
		BigDecimal remainCount = new BigDecimal("0");// 上一等级总人数
		Map<String, BigDecimal> remainCountMap = new HashMap<String, BigDecimal>();// 上一等级各个班人数
		Boolean flag = true;

		for (Integer level : schoolLevelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = schoolLevelMap.get(level);
			BigDecimal levelDecimal = schoolLevelMapDecimal.get(level);
			allLevelCount = allLevelCount.add(levelDecimal);
			levelDecimal = levelDecimal.subtract(remainDecimal);// 当前可分配 = 初始分配-上一等级遗留
			int last = averageExcelVos.size() - 1;// 最后一个的
			int first = 0;// 第一个的
			int total = averageExcelVos.size();// 总人数
			BigDecimal totalDecimal = new BigDecimal(total);
			allCount = allCount.add(totalDecimal);
			remainDecimal = allCount.subtract(allLevelCount);// 留给下一等级的

			Map<String, BigDecimal> remainMap = new HashMap<String, BigDecimal>();
			BigDecimal averageDecimal = zero;
			BigDecimal firstDecimal = zero;
			BigDecimal availabelLevel = zero;
			BigDecimal sameCount = zero;
			BigDecimal lastLevelDecimal = zero;

			for (String classCode : classCodes) {
				Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
				BigDecimal count = new BigDecimal("0");
				BigDecimal classSameCount = new BigDecimal("0");// 该班重名人数
				if (averageExcelVos == null || averageExcelVos.size() == 0) {
					// 如果为空，则应该把remainDecimal置值,如果remainDecimal高于可分配的
					if (levelDecimal.compareTo(zero) >= 0) {
						if (remainDecimalMap.containsKey(classCode)) {
							innerMap.put("A" + level, remainDecimalMap.get(classCode));
							remainDecimalMap.put(classCode, zero);
						} else {
							innerMap.put("A" + level, zero);
						}
						flag = true;
					} else {
						// 上一等级遗留大于当前等级可分配,需要根据人数平均分
						BigDecimal average = schoolLevelMapDecimal.get(level).divide(remainCount, RoundingMode.HALF_UP);
						if (remainCountMap.containsKey(classCode)) {
							BigDecimal classRemain = average.multiply(remainCountMap.get(classCode)).setScale(2,
									RoundingMode.HALF_UP);
							innerMap.put("A" + level, classRemain);
							remainDecimalMap.put(classCode, remainDecimalMap.get(classCode).subtract(classRemain));
						} else {
							innerMap.put("A" + level, zero);
						}
						flag = false;
					}
				} else {
					int lastRank = averageExcelVos.get(last).getRank();
					for (int i = 0; i < averageExcelVos.size(); i++) {
						if (averageExcelVos.get(i).getClassCode().equalsIgnoreCase(classCode)) {
							if (averageExcelVos.get(i).getRank() != lastRank) {
								count = count.add(one);
							} else {
								classSameCount = classSameCount.add(one);
								if (remainMap.containsKey(classCode)) {
									remainMap.put(classCode, remainMap.get(classCode).add(one));
								} else {
									remainMap.put(classCode, one);
								}
							}
						}
						if (averageExcelVos.get(i).getRank() == lastRank && first == 0) {
							first = i;
						}
						// 有可能某一个等级全部都是重名的
						if (averageExcelVos.get(0).getRank() == lastRank) {
							first = 0;
						}

					}
					firstDecimal = new BigDecimal(first);
					// 可分配指标 / 总体重名人数 * 该班重名人数
					availabelLevel = levelDecimal.subtract(firstDecimal);// 可分配指标
					sameCount = totalDecimal.subtract(firstDecimal);// 总体重名人数
					averageDecimal = availabelLevel.divide(sameCount, RoundingMode.HALF_UP);// 相同的每个能分到的
					if (remainDecimalMap.get(classCode) != null) {
						lastLevelDecimal = remainDecimalMap.get(classCode);
					} else {
						lastLevelDecimal = zero;
					}
					innerMap.put("A" + level, lastLevelDecimal.add(count).add(averageDecimal.multiply(classSameCount))
							.setScale(2, RoundingMode.HALF_UP));

					flag = true;

				}

				if (result.containsKey(classCode)) {
					result.get(classCode).putAll(innerMap);
				} else {
					result.put(classCode, innerMap);
				}
			}
			if (flag) {
				remainDecimalMap.clear();// 清空上一等级遗留
				remainCount = sameCount;
				remainCountMap.clear();
				// 留给下一等级的
				for (String key : remainMap.keySet()) {
					remainDecimalMap.put(key, remainMap.get(key).multiply(one.subtract(averageDecimal)).setScale(2,
							RoundingMode.HALF_UP));
					remainCountMap.put(key, remainMap.get(key));
				}
			}

		}

		calculateSum(result);// 计算累数
		
		return result;
	}

	@Override
	public Map<String, Map<String, BigDecimal>> calculateTopTen(Map<Integer, List<AverageExcelVo>> schoolTopTenLevelMap,
			Map<Integer, List<AverageExcelVo>> schoolLevelMap, Map<Integer, BigDecimal> schoolLevelMapDecimal,
			List<String> classCodes) {
		// TODO Auto-generated method stub
		Map<String, Map<String, BigDecimal>> result = new LinkedHashMap<String, Map<String, BigDecimal>>();
		BigDecimal zero = new BigDecimal("0");
		BigDecimal one = new BigDecimal("1");
		BigDecimal remainDecimal = new BigDecimal("0");// 上一等级整体遗留的 = 上一等级人数-上一等级指标数
		BigDecimal allCount = new BigDecimal(0);// 人数累加
		BigDecimal allLevelCount = new BigDecimal(0);// 指标累加
		Map<String, BigDecimal> remainDecimalMap = new HashMap<String, BigDecimal>();
		BigDecimal remainCount = new BigDecimal("0");// 上一等级总人数
		Map<String, BigDecimal> remainCountMap = new HashMap<String, BigDecimal>();// 上一等级各个班人数
		Boolean flag = true;

		for (Integer level : schoolTopTenLevelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = schoolTopTenLevelMap.get(level);
			BigDecimal levelDecimal = schoolLevelMapDecimal.get(level);
			allLevelCount = allLevelCount.add(levelDecimal);
			levelDecimal = levelDecimal.subtract(remainDecimal);// 当前可分配 = 初始分配-上一等级遗留
			int last = schoolLevelMap.get(level).size() - 1;// 最后一个的
			int first = 0;// 第一个的
			int total = schoolLevelMap.get(level).size();// 总人数
			BigDecimal totalDecimal = new BigDecimal(total);
			allCount = allCount.add(totalDecimal);

			Map<String, BigDecimal> remainMap = new HashMap<String, BigDecimal>();

			BigDecimal averageDecimal = zero;
			BigDecimal firstDecimal = zero;
			BigDecimal availabelLevel = zero;
			BigDecimal sameCount = zero;
			BigDecimal lastLevelDecimal = zero;

			for (String classCode : classCodes) {
				Map<String, BigDecimal> innerMap = new LinkedHashMap<String, BigDecimal>();
				BigDecimal count = new BigDecimal("0");
				BigDecimal classSameCount = new BigDecimal("0");// 该班重名人数
				if (averageExcelVos == null || averageExcelVos.size() == 0) {
					// 如果为空，则应该把remainDecimal置值,如果remainDecimal高于可分配的

					if (levelDecimal.compareTo(zero) >= 0) {
						if (remainDecimalMap.containsKey(classCode)) {
							innerMap.put("A" + level, remainDecimalMap.get(classCode));
							remainDecimalMap.put(classCode, zero);
						} else {
							innerMap.put("A" + level, zero);
						}

						flag = true;

					} else {
						// 上一等级遗留大于当前等级可分配,需要根据人数平均分
						BigDecimal average = schoolLevelMapDecimal.get(level).divide(remainCount, RoundingMode.HALF_UP);
						if (remainCountMap.containsKey(classCode)) {
							BigDecimal classRemain = average.multiply(remainCountMap.get(classCode)).setScale(2,
									RoundingMode.HALF_UP);
							innerMap.put("A" + level, classRemain);
							remainDecimalMap.put(classCode, remainDecimalMap.get(classCode).subtract(classRemain));
						} else {
							innerMap.put("A" + level, zero);
						}
						flag = false;

					}

				} else {
					int lastRank = schoolLevelMap.get(level).get(last).getRank();
					for (int i = 0; i < averageExcelVos.size(); i++) {
						if (averageExcelVos.get(i).getClassCode().equalsIgnoreCase(classCode)) {
							if (averageExcelVos.get(i).getRank() != lastRank) {
								count = count.add(one);
							} else {
								classSameCount = classSameCount.add(one);
								if (remainMap.containsKey(classCode)) {
									remainMap.put(classCode, remainMap.get(classCode).add(one));
								} else {
									remainMap.put(classCode, one);
								}
							}
						}

					}
					for (int i = 0; i < schoolLevelMap.get(level).size(); i++) {

						if (schoolLevelMap.get(level).get(i).getRank() == lastRank && first == 0) {
							first = i;
						}
						// 有可能某一个等级全部都是重名的
						if (schoolLevelMap.get(level).get(0).getRank() == lastRank) {
							first = 0;
						}

					}
					firstDecimal = new BigDecimal(first);
					// 可分配指标 / 总体重名人数 * 该班重名人数
					availabelLevel = levelDecimal.subtract(firstDecimal);// 可分配指标
					sameCount = totalDecimal.subtract(firstDecimal);// 总体重名人数
					averageDecimal = availabelLevel.divide(sameCount, RoundingMode.HALF_UP);// 相同的每个能分到的
					if (remainDecimalMap.get(classCode) != null) {
						lastLevelDecimal = remainDecimalMap.get(classCode);
					} else {
						lastLevelDecimal = zero;
					}
					innerMap.put("A" + level, lastLevelDecimal.add(count).add(averageDecimal.multiply(classSameCount))
							.setScale(2, RoundingMode.HALF_UP));

					flag = true;
				}

				if (result.containsKey(classCode)) {
					result.get(classCode).putAll(innerMap);
				} else {
					result.put(classCode, innerMap);
				}
			}

			remainDecimal = allCount.subtract(allLevelCount);// 留给下一等级的

			if (flag) {
				remainDecimalMap.clear();// 清空上一等级遗留
				remainCount = sameCount;
				remainCountMap.clear();

				// 留给下一等级的
				for (String key : remainMap.keySet()) {
					remainDecimalMap.put(key, remainMap.get(key).multiply(one.subtract(averageDecimal)).setScale(2,
							RoundingMode.HALF_UP));
					remainCountMap.put(key, remainMap.get(key));
				}
			}

		}
		calculateSum(result);// 计算累数
		return result;
	}

	// 计算赋分值
	@Override
	public void calculateAssign(Map<Integer, List<AverageExcelVo>> levelMap) {
		// TODO Auto-generated method stub
		for (Integer level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			for (AverageExcelVo vo : averageExcelVos) {
				vo.setSubjectResultAssign(Constants.ASSIGN_LEVEL.get(level));
			}
		}
	}

	@Override
	public void calculateAssignScore(Map<Integer, List<AverageExcelVo>> levelMap,
			List<AverageAssignExcelVo> averageAssignExcelVos) {
		// TODO Auto-generated method stub
		Map<String, AverageAssignExcelVo> map = new HashMap<String, AverageAssignExcelVo>();
		for (AverageAssignExcelVo vo : averageAssignExcelVos) {
			map.put(vo.getUniqueId(), vo);
		}
		for (Integer level : levelMap.keySet()) {
			List<AverageExcelVo> vos = levelMap.get(level);
			for (AverageExcelVo vo : vos) {
				if (map.containsKey(vo.getUniqueId())) {
					map.get(vo.getUniqueId()).getAssignScore().put(vo.getSubjectName(),
							new BigDecimal(vo.getSubjectResultAssign()));
				}
			}
		}
	}

	/**
	 * @return 班级编码
	 */
	@Override
	public List<String> getClassCode(List<AverageExcelVo> averageExcelVos, String schoolName) {
		List<String> result = new ArrayList<String>();
		for (AverageExcelVo vo : averageExcelVos) {
			if (vo.getSchoolName().equalsIgnoreCase(schoolName) && !result.contains(vo.getClassCode())) {
				result.add(vo.getClassCode());
			}
		}
		Collections.sort(result);
		return result;
	}
	
	@Override
	public Set<String> getSchoolName(List<AverageExcelVo> averageExcelVos) {
		Set<String> result = new TreeSet<String>();
		for (AverageExcelVo vo : averageExcelVos) {
			if (StringUtils.isNotBlank(vo.getSchoolName())) {
				result.add(vo.getSchoolName());
			}
		}
		return result;
	}

	@Override
	public List<AverageExcelVo> getAverageExcelVo(Workbook wb, int index, int schoolIndex) {
		List<AverageExcelVo> result = new ArrayList<AverageExcelVo>();
		Sheet sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		Row headRow = sheet.getRow(0);
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			Row row = sheet.getRow(i);

			if (row.getCell(index) == null || row.getCell(index).getCellType() == Cell.CELL_TYPE_BLANK
					||  Math.abs(row.getCell(index).getNumericCellValue())  <= 0.000001) {
				continue;
			} else {
				AverageExcelVo vo = new AverageExcelVo();
				
				  vo.setSchoolName(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex)).
				  toString());
				  vo.setClassCode(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex +
				  1)).toString());
				  vo.setName(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex +
				  2)).toString()); 
				  /*vo.setUniqueId(vo.getSchoolName() + vo.getClassCode() +
				  vo.getName());*/
				 
				vo.setUniqueId(row.getRowNum() + "");// 用行号来代替唯一Id
				vo.setSubjectResult(Float.valueOf(String.valueOf(row.getCell(index).getNumericCellValue())));
				vo.setSubjectName(ReadExcelUtils.getCellFormatValue(headRow.getCell(index)).toString());
				result.add(vo);
			}
		}
		return result;
	}

	// 不区分学校
	@Override
	public List<AverageAssignExcelVo> getAverageAssignExcelVo(Workbook wb, int schoolIndex, String schoolName) {
		// TODO Auto-generated method stub
		List<AverageAssignExcelVo> result = new ArrayList<AverageAssignExcelVo>();
		Sheet sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		Row headRow = sheet.getRow(0);// 标题
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			Row row = sheet.getRow(i);

			if((row.getCell(schoolIndex) == null || row.getCell(schoolIndex).getCellType() == Cell.CELL_TYPE_BLANK)&&(
                    row.getCell(schoolIndex+2) == null || row.getCell(schoolIndex+2).getCellType() == Cell.CELL_TYPE_BLANK
                    )){
                break;
			}


			// modify 不区分学校 姓名 名字 性别等
			AverageAssignExcelVo vo = new AverageAssignExcelVo();

            vo.setExamineeNo(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex-2)).toString());
            vo.setStudentNo(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex-1)).toString());
			vo.setSchoolName(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex)).toString());
			vo.setClassCode(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex + 1)).toString());
			vo.setName(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex + 2)).toString());
			vo.setSex(ReadExcelUtils.getCellFormatValue(row.getCell(schoolIndex + 3)).toString());
			vo.setUniqueId(row.getRowNum() + "");// 用行号来表示唯一Id
			Map<String, BigDecimal> assignScore = new LinkedHashMap<String, BigDecimal>();
			vo.setAssignScore(assignScore);
			Map<String, BigDecimal> initScore = new LinkedHashMap<String, BigDecimal>();
			for (int j = schoolIndex + 4; j < row.getLastCellNum(); j++) {
				initScore.put(headRow.getCell(j).getStringCellValue(),
						new BigDecimal(row.getCell(j).getNumericCellValue()));
			}
			vo.setInitScore(initScore);
			result.add(vo);


		}
		return result;
	}

	/*
	 * 指定科目计算总分
	 */
	@Override
	public List<AverageExcelVo> getAverageExcelVoSum(Workbook wb, List<String> subjectNames) {
		// TODO Auto-generated method stub

		List<AverageExcelVo> result = new ArrayList<AverageExcelVo>();
		Sheet sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			AverageExcelVo vo = new AverageExcelVo();
			vo.setSchoolName(ReadExcelUtils.getCellFormatValue(row.getCell(0)).toString());
			vo.setClassCode(ReadExcelUtils.getCellFormatValue(row.getCell(1)).toString());
			vo.setName(ReadExcelUtils.getCellFormatValue(row.getCell(2)).toString());
			for (int m = 3; m < colNum; m++) {
				if (subjectNames.contains(sheet.getRow(0).getCell(m).getRichStringCellValue().toString())) {
					vo.setSubjectResultSum(vo.getSubjectResultSum()
							+ Float.valueOf(String.valueOf(row.getCell(m).getNumericCellValue())));
				}

			}

			result.add(vo);
		}
		return result;
	}

	/**
	 * @Author : Administrator
	 * @Description : 成绩排序
	 */
	@Override
	public void sortAverageExcelVo(List<AverageExcelVo> averageExcelVos) {
		Collections.sort(averageExcelVos, new Comparator<AverageExcelVo>() {
			public int compare(AverageExcelVo o1, AverageExcelVo o2) {
				// 按照成绩进行降序排列
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
			while ((i + 1) < averageExcelVos.size()
					&& averageExcelVos.get(i).getSubjectResult() == averageExcelVos.get(i + 1).getSubjectResult()) {
				i++;
				averageExcelVos.get(i).setRank(count + 1);
			}
		}
	}

	@Override
	public void sortAverageExcelVoSum(List<AverageExcelVo> averageExcelVos) {
		// TODO Auto-generated method stub
		Collections.sort(averageExcelVos, new Comparator<AverageExcelVo>() {
			public int compare(AverageExcelVo o1, AverageExcelVo o2) {
				// 按照成绩进行降序排列
				if (o1.getSubjectResultSum() > o2.getSubjectResultSum()) {
					return -1;
				}
				if (o1.getSubjectResultSum() == o2.getSubjectResultSum()) {
					return 0;
				}
				return 1;
			}
		});
		for (int i = 0; i < averageExcelVos.size(); i++) {
			int count = i;
			averageExcelVos.get(i).setRankSum(i + 1);
			while ((i + 1) < averageExcelVos.size() && averageExcelVos.get(i).getSubjectResultSum() == averageExcelVos
					.get(i + 1).getSubjectResultSum()) {
				i++;
				averageExcelVos.get(i).setRankSum(count + 1);
			}
		}
	}
	
	

	@Override
	public void sortAverageNewVo(List<AverageNewVo> averageNewVos) {
		// TODO Auto-generated method stub
		Collections.sort(averageNewVos, new Comparator<AverageNewVo>() {
			public int compare(AverageNewVo o1, AverageNewVo o2) {
				// 按照成绩进行降序排列
				if (o1.getSubjectScore() > o2.getSubjectScore()) {
					return -1;
				}
				if (o1.getSubjectScore() == o2.getSubjectScore()) {
					return 0;
				}
				return 1;
			}
		});
		for (int i = 0; i < averageNewVos.size(); i++) {
			int count = i;
			averageNewVos.get(i).setGradeRank(i + 1);
			while ((i + 1) < averageNewVos.size() && averageNewVos.get(i).getSubjectScore() == averageNewVos
					.get(i + 1).getSubjectScore()) {
				i++;
				averageNewVos.get(i).setGradeRank(count + 1);
			}
		}
	}

	/**
	 * @Author : Administrator
	 * @Description : 按等级分组
	 */
	@Override
	public Map<Integer, List<AverageExcelVo>> getLevelMap(List<AverageExcelVo> averageExcelVos) {
		BigDecimal hundred = new BigDecimal("100");
		BigDecimal size = new BigDecimal(averageExcelVos.size());// 参加考试人数
		Map<Integer, List<AverageExcelVo>> levelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		int previousLevelCount = 0;
		for (int i = 0; i < Constants.AVERAGE_LEVELS.size(); i++) {

			BigDecimal initBigDecimal = size.multiply(Constants.AVERAGE_LEVELS.get(i)).divide(hundred).setScale(2,
					RoundingMode.HALF_UP);
			int ceil = (int) Math.ceil(initBigDecimal.floatValue());
			int floor = (int) Math.floor(initBigDecimal.floatValue());
			while ((floor + 1) < averageExcelVos.size()
					&& averageExcelVos.get(floor).getRank() == averageExcelVos.get(floor + 1).getRank()) {
				floor++;
			}
			if (floor + 1 >= averageExcelVos.size()) {
				levelMap.put(i + 1, averageExcelVos.subList(previousLevelCount, averageExcelVos.size()));
			} else {
				levelMap.put(i + 1, averageExcelVos.subList(previousLevelCount, floor + 1));
			}
			// 获取前一个段位的人数
			previousLevelCount = floor + 1;

		}
		return levelMap;
	}
    private  Map<String, List<AverageNewVo>> getNewLevelMap(List<AverageNewVo> averageNewVos,Map<String,BigDecimal> levelMap) {
        Map<String,List<AverageNewVo>> result = new LinkedHashMap<String, List<AverageNewVo>>();
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal size = new BigDecimal(averageNewVos.size());// 参加考试人数
        BigDecimal count = new BigDecimal("0");
        int previousLevelCount = 0;
        List<String> list = new ArrayList<String>(levelMap.keySet());
        for (int i = 0; i < list.size(); i++) {
            count = count.add(levelMap.get(list.get(i)));
           /* BigDecimal initBigDecimal = size.multiply(count).divide(hundred).setScale(2,
                    RoundingMode.HALF_UP);*/
            BigDecimal initBigDecimal = count;
            int ceil = (int) Math.ceil(initBigDecimal.floatValue());
            int floor = (int) Math.floor(initBigDecimal.floatValue());
            while ((floor + 1) < averageNewVos.size()
                    && averageNewVos.get(floor).getGradeRank() == averageNewVos.get(floor + 1).getGradeRank()) {
                floor++;
            }
            if (floor + 1 >= averageNewVos.size()) {
            	if(previousLevelCount >= averageNewVos.size()){
					result.put(list.get(i), new ArrayList<AverageNewVo>());
				}else{
					result.put(list.get(i), averageNewVos.subList(previousLevelCount, averageNewVos.size()));
				}
            } else {
                result.put(list.get(i), averageNewVos.subList(previousLevelCount, floor + 1));
            }
            // 获取前一个段位的人数
            previousLevelCount = floor + 1;
        }
        return result;
    }

	/*
	 * 按等级分组(总分)
	 */
	@Override
	public Map<Integer, List<AverageExcelVo>> getLevelMapSum(List<AverageExcelVo> averageExcelVos) {
		// TODO Auto-generated method stub
		BigDecimal hundred = new BigDecimal("100");
		BigDecimal size = new BigDecimal(averageExcelVos.size());// 参加考试人数
		Map<Integer, List<AverageExcelVo>> levelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		int previousLevelCount = 0;
		for (int i = 0; i < Constants.AVERAGE_LEVELS.size(); i++) {

			BigDecimal initBigDecimal = size.multiply(Constants.AVERAGE_LEVELS.get(i)).divide(hundred).setScale(2,
					RoundingMode.HALF_UP);
			int ceil = (int) Math.ceil(initBigDecimal.floatValue());
			int floor = (int) Math.floor(initBigDecimal.floatValue());
			while ((floor + 1) < averageExcelVos.size()
					&& averageExcelVos.get(floor).getRankSum() == averageExcelVos.get(floor + 1).getRankSum()) {
				floor++;
			}
			if (floor + 1 >= averageExcelVos.size()) {
				levelMap.put(i + 1, averageExcelVos.subList(previousLevelCount, averageExcelVos.size()));
			} else {
				levelMap.put(i + 1, averageExcelVos.subList(previousLevelCount, floor + 1));
			}
			// 获取前一个段位的人数
			previousLevelCount = floor + 1;

		}
		return levelMap;
	}

	@Override
	public Map<Integer, List<AverageExcelVo>> getLevelAssignMap(List<AverageExcelVo> averageExcelVos) {
		// TODO Auto-generated method stub
		BigDecimal hundred = new BigDecimal("100");
		BigDecimal size = new BigDecimal(averageExcelVos.size());// 参加考试人数
		Map<Integer, List<AverageExcelVo>> levelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		int previousLevelCount = 0;
		for (int i = 0; i < Constants.AVERAGE_ASSIGN_LEVELS.size(); i++) {

			BigDecimal initBigDecimal = size.multiply(Constants.AVERAGE_ASSIGN_LEVELS.get(i)).divide(hundred)
					.setScale(2, RoundingMode.HALF_UP);
			int ceil = (int) Math.ceil(initBigDecimal.floatValue());
			int floor = (int) Math.floor(initBigDecimal.floatValue());
			while ((floor + 1) < averageExcelVos.size()
					&& averageExcelVos.get(floor).getRank() == averageExcelVos.get(floor + 1).getRank()) {
				floor++;
			}
			if (floor + 1 >= averageExcelVos.size()) {
				levelMap.put(i + 1, averageExcelVos.subList(previousLevelCount, averageExcelVos.size()));
			} else {
				levelMap.put(i + 1, averageExcelVos.subList(previousLevelCount, floor + 1));
			}
			// 获取前一个段位的人数
			previousLevelCount = floor + 1;

		}
		return levelMap;
	}

	/**
	 * @Author : Administrator
	 * @Description : 按等级分组(初始指标)
	 */
	@Override
	public Map<Integer, BigDecimal> getLevelMapDecimal(List<AverageExcelVo> averageExcelVos) {
		BigDecimal hundred = new BigDecimal("100");

		BigDecimal size = new BigDecimal(averageExcelVos.size());// 参加考试人数
		Map<Integer, BigDecimal> levelMapDecimal = new LinkedHashMap<Integer, BigDecimal>();
		for (int i = 0; i < Constants.AVERAGE_LEVELS.size(); i++) {
			if (i > 0) {
				BigDecimal initBigDecimal = size
						.multiply(Constants.AVERAGE_LEVELS.get(i).subtract(Constants.AVERAGE_LEVELS.get(i - 1)))
						.divide(hundred, 2, RoundingMode.HALF_UP);
				levelMapDecimal.put(i + 1, initBigDecimal);
			} else {
				BigDecimal initBigDecimal = size.multiply(Constants.AVERAGE_LEVELS.get(i)).divide(hundred, 2,
						RoundingMode.HALF_UP);
				levelMapDecimal.put(i + 1, initBigDecimal);
			}

		}
		return levelMapDecimal;
	}

	/**
	 * @param levelMap
	 * @param schoolName
	 * @return
	 */
	@Override
	public Map<Integer, List<AverageExcelVo>> getLevelMapBySchoolName(Map<Integer, List<AverageExcelVo>> levelMap,
			String schoolName) {
		Map<Integer, List<AverageExcelVo>> schoolLevelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			List<AverageExcelVo> innerList = new ArrayList<AverageExcelVo>();
			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				schoolLevelMap.put(level, innerList);
			} else {

				for (int i = 0; i < averageExcelVos.size(); i++) {
					if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
						innerList.add(averageExcelVos.get(i));
					}
				}
				schoolLevelMap.put(level, innerList);
			}
		}
		return schoolLevelMap;
	}

	@Override
	public Map<Integer, List<AverageExcelVo>> getLevelAssignMapBySchoolName(Map<Integer, List<AverageExcelVo>> levelMap,
			String schoolName) {
		// TODO Auto-generated method stub
		Map<Integer, List<AverageExcelVo>> schoolLevelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			List<AverageExcelVo> innerList = new ArrayList<AverageExcelVo>();
			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				schoolLevelMap.put(level, innerList);
			} else {

				for (int i = 0; i < averageExcelVos.size(); i++) {
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
	 *            某一个学校的指标
	 */
	@Override
	public Map<Integer, BigDecimal> getLevelMapDecimalBySchoolName(Map<Integer, List<AverageExcelVo>> levelMap,
			Map<Integer, BigDecimal> levelMapDecimal, String schoolName) {
		Map<Integer, BigDecimal> schoolLevelMapDecimal = new LinkedHashMap<Integer, BigDecimal>();
		BigDecimal zero = new BigDecimal("0");
		BigDecimal one = new BigDecimal("1");
		BigDecimal start = new BigDecimal("0");
		BigDecimal schoolRemainDecimal = new BigDecimal("0");
		BigDecimal remainDecimal = new BigDecimal("0");
		BigDecimal allCount = new BigDecimal(0);// 人数累加
		BigDecimal allLevelCount = new BigDecimal(0);// 指标累加

		BigDecimal remainCount = new BigDecimal("0");
		BigDecimal remainAllCount = new BigDecimal("0");


		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			allCount = allCount.add(new BigDecimal(averageExcelVos.size()));
			allLevelCount = allLevelCount.add(levelMapDecimal.get(level));

			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				// 如果为空，则应该把remainDecimal置值,如果remainDecimal高于可分配的
				if (remainDecimal.compareTo(levelMapDecimal.get(level)) >= 0) {
					BigDecimal remain = levelMapDecimal.get(level).divide(remainAllCount, RoundingMode.HALF_UP)
							.multiply(remainCount).setScale(2, RoundingMode.HALF_UP);
					schoolLevelMapDecimal.put(level, remain);
					schoolRemainDecimal = schoolRemainDecimal.subtract(remain);
					remainDecimal = remainDecimal.subtract(levelMapDecimal.get(level));
				}else{
					//add 若当前为0且遗留小于可分配的
					BigDecimal remain = levelMapDecimal.get(level).divide(remainAllCount, RoundingMode.HALF_UP)
							.multiply(remainCount).setScale(2, RoundingMode.HALF_UP);
					schoolLevelMapDecimal.put(level, remain);
					schoolRemainDecimal = schoolRemainDecimal.subtract(schoolRemainDecimal);
					remainDecimal = remainDecimal.subtract(remainDecimal);
				}
			} else {
				// 最后一名名次
				int lastRank = averageExcelVos.get(averageExcelVos.size() - 1).getRank();
				int first = 0;// 第一个等于最后一名的,用来计算同名的总人数
				int schoolCount = 0;// 该学校的同名人数
				for (int i = 0; i < averageExcelVos.size(); i++) {
					if (averageExcelVos.get(i).getRank() != lastRank) {
						// 不等于最后一名则直接加1
						if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
							start = start.add(one);
						}
					} else {
						// 等于最后一名需要算出同名的总人数以及某个学校的人数
						if (first == 0) {
							first = i;
						}
						// 所有的都是重名
						if (averageExcelVos.get(0).getRank() == lastRank) {
							first = 0;
						}
						if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
							schoolCount++;
						}
					}
				}
				BigDecimal countDecimal = new BigDecimal(averageExcelVos.size() - first);// 同分总人数
				BigDecimal schoolCountDecimal = new BigDecimal(schoolCount);// 该校同分总人数
				BigDecimal initDecimal = levelMapDecimal.get(level);// 初始指标
				BigDecimal firstDecimal = new BigDecimal(first);
				// (初始指标-同分第一个)/同分总人数*该校同分总人数
				BigDecimal bigDecimal = (initDecimal.subtract(remainDecimal).subtract(firstDecimal))
						.multiply(schoolCountDecimal).divide(countDecimal, 2, RoundingMode.HALF_UP);
				schoolLevelMapDecimal.put(level, bigDecimal.add(start).add(schoolRemainDecimal));
				schoolRemainDecimal = schoolCountDecimal.subtract(bigDecimal);// 该校留给下一个等级的
				remainDecimal = allCount.subtract(allLevelCount);// 整体留给下一个等级的
				remainCount = schoolCountDecimal;//// 该校同分总人数
				remainAllCount = countDecimal;// 同分总人数
			}
			start = zero;// 清0

		}
		return schoolLevelMapDecimal;
	}

	@Override
	public Map<Integer, List<AverageExcelVo>> getTopTenLevelMapSumBySchoolName(
			Map<Integer, List<AverageExcelVo>> levelMap, List<String> classCodes, String schoolName) {
		// TODO Auto-generated method stub
		Map<String, Integer> classMap = new HashMap<String, Integer>();
		Map<String, Integer> classTenMap = new HashMap<String, Integer>();
		int ten = 10;
		for (String string : classCodes) {
			classMap.put(string, 0);
		}
		Map<Integer, List<AverageExcelVo>> schoolLevelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			List<AverageExcelVo> innerList = new ArrayList<AverageExcelVo>();
			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				schoolLevelMap.put(level, innerList);
			} else {
				for (int i = 0; i < averageExcelVos.size(); i++) {

					if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
						if (classMap.get(averageExcelVos.get(i).getClassCode()) < ten) {
							innerList.add(averageExcelVos.get(i));
							classMap.put(averageExcelVos.get(i).getClassCode(),
									classMap.get(averageExcelVos.get(i).getClassCode()) + 1);
						} else if (classMap.get(averageExcelVos.get(i).getClassCode()) == ten) {
							innerList.add(averageExcelVos.get(i));
							classTenMap.put(averageExcelVos.get(i).getClassCode(), averageExcelVos.get(i).getRankSum());
							classMap.put(averageExcelVos.get(i).getClassCode(),
									classMap.get(averageExcelVos.get(i).getClassCode()) + 1);
						} else if (classTenMap.get(averageExcelVos.get(i).getClassCode()) != null && averageExcelVos
								.get(i).getRankSum() == classTenMap.get(averageExcelVos.get(i).getClassCode())) {
							innerList.add(averageExcelVos.get(i));
						}
					}
				}
				schoolLevelMap.put(level, innerList);
			}
		}
		return schoolLevelMap;
	}

	@Override
	public Map<Integer, List<AverageExcelVo>> getTopTenLevelMapBySchoolName(Map<Integer, List<AverageExcelVo>> levelMap,
			List<String> classCodes, String schoolName) {
		// TODO Auto-generated method stub
		Map<String, Integer> classMap = new HashMap<String, Integer>();
		Map<String, Integer> classTenMap = new HashMap<String, Integer>();
		int ten = 10;
		for (String string : classCodes) {
			classMap.put(string, 0);
		}
		Map<Integer, List<AverageExcelVo>> schoolLevelMap = new LinkedHashMap<Integer, List<AverageExcelVo>>();
		for (int level : levelMap.keySet()) {
			List<AverageExcelVo> averageExcelVos = levelMap.get(level);
			List<AverageExcelVo> innerList = new ArrayList<AverageExcelVo>();
			if (averageExcelVos == null || averageExcelVos.size() == 0) {
				schoolLevelMap.put(level, innerList);
			} else {
				for (int i = 0; i < averageExcelVos.size(); i++) {
					if (averageExcelVos.get(i).getSchoolName().equalsIgnoreCase(schoolName)) {
						if (classMap.get(averageExcelVos.get(i).getClassCode()) < ten) {
							innerList.add(averageExcelVos.get(i));
							classMap.put(averageExcelVos.get(i).getClassCode(),
									classMap.get(averageExcelVos.get(i).getClassCode()) + 1);
						} else if (classMap.get(averageExcelVos.get(i).getClassCode()) == ten) {
							innerList.add(averageExcelVos.get(i));
							classTenMap.put(averageExcelVos.get(i).getClassCode(), averageExcelVos.get(i).getRank());
							classMap.put(averageExcelVos.get(i).getClassCode(),
									classMap.get(averageExcelVos.get(i).getClassCode()) + 1);
						} else if (classTenMap.get(averageExcelVos.get(i).getClassCode()) != null && averageExcelVos
								.get(i).getRank() == classTenMap.get(averageExcelVos.get(i).getClassCode())) {
							innerList.add(averageExcelVos.get(i));
						}
					}
				}
				schoolLevelMap.put(level, innerList);
			}
		}
		return schoolLevelMap;
	}

	/**
	 * 计算累数
	 */
	private void calculateSum(Map<String, Map<String, BigDecimal>> map) {

		
		for (String classCode : map.keySet()) {
			Map<String, BigDecimal> innerMap = map.get(classCode);
			BigDecimal classAllLevel = new BigDecimal("0");// 一个班的累数
            BigDecimal M = new BigDecimal("0");

			for (int i = 1; i <= Constants.AVERAGE_LEVELS.size(); i++) {
				classAllLevel = classAllLevel.add(innerMap.get("A" + i));
				innerMap.put("classAllSum" + i, classAllLevel);
                M = M.add(innerMap.get("A" + i).multiply(Constants.LEVEL_WEIGHT.get("A" + i)).setScale(2,RoundingMode.HALF_UP));
			}
			if (classAllLevel.compareTo(new  BigDecimal("0"))<=0 ){
                innerMap.put("M",new BigDecimal("0"));
            }else{
                innerMap.put("M",M.divide(classAllLevel,2,RoundingMode.HALF_UP));
            }
		}
		BigDecimal allLevelSum = new BigDecimal("0");// 所有的累数
		Map<String, BigDecimal> allLevelMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> allLevelSumMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> allMMap = new HashMap<String, BigDecimal>();


		BigDecimal allM = new BigDecimal("0");

		for (int i = 1; i <= Constants.AVERAGE_LEVELS.size(); i++) {
			BigDecimal allLevel = new BigDecimal("0");// 所有的档数
			for (String classCode : map.keySet()) {
				allLevel = allLevel.add(map.get(classCode).get("A" + i));
			}
			allM = allM.add(allLevel.multiply((Constants.LEVEL_WEIGHT.get("A"+i)).setScale(2,RoundingMode.HALF_UP)));
			allLevelSum = allLevelSum.add(allLevel);
			allLevelMap.put("allLevel" + i, allLevel);
			allLevelSumMap.put("allLevelSum" + i, allLevelSum);
		}
		if (allLevelSum.compareTo(new  BigDecimal("0"))<=0 ){
			allMMap.put("allM",new BigDecimal("0"));
		}else{
			allMMap.put("allM",allM.divide(allLevelSum,2,RoundingMode.HALF_UP));
		}
		map.put("allLevel", allLevelMap);
		map.put("allLevelSum", allLevelSumMap);
		map.put("allM", allMMap);


	}

	// 计算累数
	private void caculateAverageSum(Map<String, Map<String, BigDecimal>> map) {
		for (String key : map.keySet()) {
			int i = Integer.valueOf(key.substring(1));
			Map<String, BigDecimal> innerMap = map.get(key);
			int size = innerMap.size();
			// 1 --> 22
			BigDecimal allLevel = new BigDecimal("0");
			BigDecimal allLevelSum = new BigDecimal("0");
			for (int j = 1; j <= size; j++) {
				// 1--> 11
				BigDecimal sum = new BigDecimal("0");
				for (int m = 1; m <= i; m++) {
					sum = sum.add(map.get("A" + m).get("level" + j));
				}
				innerMap.put("levelSum" + j, sum);
				allLevel = allLevel.add(map.get("A" + i).get("level" + j));
			}

			innerMap.put("allLevel", allLevel);
			if (i == 1) {
				innerMap.put("allLevelSum", allLevel);
			} else {
				innerMap.put("allLevelSum", map.get("A" + (i - 1)).get("allLevelSum").add(allLevel));
			}
		}
	}
		// 计算累数
		private void caculateNewAverageSum(Map<String, Map<String, BigDecimal>> map,List<String> classCodes, List<String> levels,Map<String,Integer> levelWeight) {
	    for (int j=0;j<classCodes.size();j++) {
                Map<String, BigDecimal> innerMap = map.get(classCodes.get(j));
                BigDecimal classAllLevel = new BigDecimal("0");// 一个班的累数
                BigDecimal M = new BigDecimal("0");
                for (int i = 1; i <= levels.size(); i++) {
                    classAllLevel = classAllLevel.add(innerMap.get(levels.get(i-1)));
                    innerMap.put("classAllSum" + i, classAllLevel);
                    M = M.add(innerMap.get(levels.get(i-1)).multiply(new BigDecimal(levelWeight.get(levels.get(i-1)))).setScale(2,RoundingMode.HALF_UP));
                }
                if (classAllLevel.compareTo(new  BigDecimal("0"))<=0 ){
                    innerMap.put("M",new BigDecimal("0"));
                }else{
                    innerMap.put("M",M.divide(classAllLevel,2,RoundingMode.HALF_UP));
                }
            }
            BigDecimal allLevelSum = new BigDecimal("0");// 所有的累数
            Map<String, BigDecimal> allLevelMap = new HashMap<String, BigDecimal>();
            Map<String, BigDecimal> allLevelSumMap = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> allMMap = new HashMap<String, BigDecimal>();


			BigDecimal allM = new BigDecimal("0");
			for (int i = 1; i <= levels.size(); i++) {
                BigDecimal allLevel = new BigDecimal("0");// 所有的档数
                for (String classCode : classCodes) {
                    allLevel = allLevel.add(map.get(classCode).get("A" + i));
                }
				allM = allM.add(allLevel.multiply(new BigDecimal(levelWeight.get(levels.get(i-1)))).setScale(2,RoundingMode.HALF_UP));
                allLevelSum = allLevelSum.add(allLevel);
                allLevelMap.put("allLevel" + i, allLevel);
                allLevelSumMap.put("allLevelSum" + i, allLevelSum);
            }
			if (allLevelSum.compareTo(new  BigDecimal("0"))<=0 ){
				allMMap.put("allM",new BigDecimal("0"));
			}else{
				allMMap.put("allM",allM.divide(allLevelSum,2,RoundingMode.HALF_UP));
			}
            map.put("allLevel", allLevelMap);
            map.put("allLevelSum", allLevelSumMap);
			map.put("allM", allMMap);

		}

		@Override
		public void calculateSchoolSum(Map<String, Map<Integer, BigDecimal>> schoolLevelSumMap) {
			// TODO Auto-generated method stub
			for (String schoolName : schoolLevelSumMap.keySet()) {	
				Map<Integer, BigDecimal> innerMap = schoolLevelSumMap.get(schoolName);
				BigDecimal schoolAllSum = new BigDecimal("0");
                BigDecimal M = new BigDecimal("0");
				for (int i = 1; i <= Constants.AVERAGE_LEVELS.size(); i++) {
					schoolAllSum = schoolAllSum.add(innerMap.get(i));
                    M = M.add(innerMap.get(i).multiply(Constants.LEVEL_WEIGHT.get("A"+i)).setScale(2,RoundingMode.HALF_UP));
					innerMap.put(100+i, schoolAllSum);
				}
				
				if (schoolAllSum.compareTo(new  BigDecimal("0"))<=0 ){
                    innerMap.put(999,new BigDecimal("0"));
                }else{
                    innerMap.put(999,M.divide(schoolAllSum,2,RoundingMode.HALF_UP));
                }
				
			}			
			Map<Integer, BigDecimal> allLevelMap = new HashMap<Integer, BigDecimal>();
			Map<Integer, BigDecimal> allLevelMapSum = new HashMap<Integer, BigDecimal>();
			Map<Integer, BigDecimal> allMMap = new HashMap<Integer, BigDecimal>();
			BigDecimal allM = new BigDecimal("0");

			BigDecimal allLevelSum = new BigDecimal("0");
			for (int i = 1; i <=Constants.AVERAGE_LEVELS.size(); i++) {
				BigDecimal allLevel = new BigDecimal("0");
				for (String schoolName : schoolLevelSumMap.keySet()) {
					allLevel = allLevel.add(schoolLevelSumMap.get(schoolName).get(i));
				}

				allM = allM.add(allLevel.multiply((Constants.LEVEL_WEIGHT.get("A"+i)).setScale(2,RoundingMode.HALF_UP)));
				allLevelMap.put(1000+i, allLevel);
				allLevelSum = allLevelSum.add(allLevel);
				allLevelMapSum.put(1000+i, allLevelSum);											
			}

			if (allLevelSum.compareTo(new  BigDecimal("0"))<=0 ){
				allMMap.put(999,new BigDecimal("0"));
			}else{
				allMMap.put(999,allM.divide(allLevelSum,2,RoundingMode.HALF_UP));
			}
			schoolLevelSumMap.put("allLevel", allLevelMap);
			schoolLevelSumMap.put("allLevelSum", allLevelMapSum);
			schoolLevelSumMap.put("allM", allMMap);

		}

	@Override
	public Map<String, BigDecimal> calculateSchoolMap(Map<Integer, BigDecimal> schoolLevelMapDecimal) {
		Map<String,BigDecimal> result = new HashMap<String, BigDecimal>();

		BigDecimal allSum = new BigDecimal("0");
		for (Integer level:
		schoolLevelMapDecimal.keySet()) {
			allSum = allSum.add(schoolLevelMapDecimal.get(level));
			result.put("A"+level,schoolLevelMapDecimal.get(level));
			result.put("allSum"+level,allSum);
		}
		return result;
	}

	@Override
	public void calculateSchoolM(Map<String, Map<String, BigDecimal>> schoolMap) {
		//计算全部档数和累数

        Map<String,BigDecimal> allLevelMap = new HashMap<String, BigDecimal>();
        Map<String,BigDecimal> allLevelSumMap = new HashMap<String, BigDecimal>();

        Set<String> schoolNames = new HashSet<String>();
        schoolNames.addAll(schoolMap.keySet());

        BigDecimal allM = new BigDecimal("0");
        Map<String,BigDecimal> allMMap = new HashMap<String, BigDecimal>();
        for (int i=1 ;i<=22;i++) {
            BigDecimal allLevel = new BigDecimal("0");
            BigDecimal allLevelSum = new BigDecimal("0");
            for (String schoolName :
                    schoolNames) {
                allLevel = allLevel.add(schoolMap.get(schoolName).get("A"+i));
                allLevelSum = allLevelSum.add(schoolMap.get(schoolName).get("allSum"+i));
            }
            allM = allM.add(allLevel.multiply((Constants.LEVEL_WEIGHT.get("A"+i)).setScale(2,RoundingMode.HALF_UP)));
            allLevelMap.put("allLevel"+i,allLevel);
            allLevelSumMap.put("allLevelSum"+i,allLevelSum);
        }

        if (allLevelSumMap.get("allLevelSum22").compareTo(BigDecimal.ZERO)<=0){
            allMMap.put("allM",BigDecimal.ZERO);
        }else{
            allMMap.put("allM",allM.divide(allLevelSumMap.get("allLevelSum22"),2,RoundingMode.HALF_UP));
        }
        schoolMap.put("allLevel",allLevelMap);
        schoolMap.put("allLevelSum",allLevelSumMap);
        schoolMap.put("allM",allMMap);
        //计算M值
        for (String schoolName:
                schoolNames) {
            BigDecimal M = new BigDecimal("0");
            Map<String,BigDecimal> innerMap = schoolMap.get(schoolName);
            for (int i=1 ;i<=22;i++) {
                M = M.add(innerMap.get("A"+i).multiply((Constants.LEVEL_WEIGHT.get("A"+i)).setScale(2,RoundingMode.HALF_UP)));
            }
            if (schoolMap.get(schoolName).get("allSum22").compareTo(BigDecimal.ZERO)<=0){
                innerMap.put("M",BigDecimal.ZERO);
            }else{
                innerMap.put("M",M.divide(schoolMap.get(schoolName).get("allSum22"),2,RoundingMode.HALF_UP));
            }
        }
    }
}
