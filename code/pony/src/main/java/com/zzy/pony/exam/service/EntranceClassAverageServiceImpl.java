package com.zzy.pony.exam.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.vo.EntranceExcelVo;
import com.zzy.pony.util.ReadExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-05-11
 * @Description
 */
@Service
@Transactional
public class EntranceClassAverageServiceImpl implements EntranceClassAverageService {

    private static Logger logger = LoggerFactory.getLogger(EntranceClassAverageServiceImpl.class);


    @Override
    public List<EntranceExcelVo> getEntranceExcelVo(Workbook wb) throws Exception {
        if (wb == null) {
            return null;
        }
        List<EntranceExcelVo> result = new ArrayList<EntranceExcelVo>();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row headRow = sheet.getRow(0);
        Row row = null;
        //考生存在空的成绩或者0分成绩均不记录整体统计范围
        boolean absent = false;
        // 正文内容应该从第二行开始,第一行为表头的标题
        try {
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                int colNum = row.getPhysicalNumberOfCells();
                EntranceExcelVo vo = new EntranceExcelVo();
                vo.setSubjectResultMap(new HashMap<String, BigDecimal>());
                vo.setSubjectRankMap(new HashMap<String, Integer>());
                vo.setLevelMap(new HashMap<String, String>());
                for (int j = 0; j < colNum; j++) {
                    if (row.getCell(j) == null || Cell.CELL_TYPE_BLANK == row.getCell(j).getCellType() || (Cell.CELL_TYPE_NUMERIC == row.getCell(j).getCellType() && row.getCell(j).getNumericCellValue() <= 0.00001f)) {
                        absent = true;
                        break;
                    } else {
                        switch (j) {
                            case 0:
                                vo.setClassName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString());
                                break;
                            case 1:
                                vo.setStudentName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString());
                                break;
                            case 2:
                                vo.setStudentNo(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString());
                                break;
                            //成绩
                            default:
                                vo.getSubjectResultMap().put(headRow.getCell(j).toString(), new BigDecimal(row.getCell(j).getNumericCellValue()));
                        }
                    }
                    vo.setTotalResult(BigDecimal.ZERO);
                }
                if (absent) {
                    absent = false;
                } else {
                    result.add(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("excel解析出错");
        }

        return result;    }

    @Override
    public void sort(List<EntranceExcelVo> vos, String subject) {
        if ("total".equals(subject)) {
            //总成绩
            for (EntranceExcelVo vo :
                    vos) {
                vo.setSubjectResult(vo.getTotalResult());
            }
            Collections.sort(vos);
            for (int i = 0; i < vos.size(); i++) {
                int j = i + 1;
                if (i == vos.size() - 1 && vos.get(i - 1).getSubjectResult().equals(vos.get(i).getSubjectResult())) {
                    //边界
                    vos.get(i).setTotalRank(vos.get(i - 1).getTotalRank());
                } else {
                    vos.get(i).setTotalRank(j);
                }
                while (i < (vos.size() - 1) && vos.get(i + 1).getSubjectResult().equals(vos.get(i).getSubjectResult())) {
                    i++;
                    vos.get(i).setTotalRank(j);
                }
            }
        } else {
            for (EntranceExcelVo vo :
                    vos) {
                vo.setSubjectResult(vo.getSubjectResultMap().get(subject));
                vo.setTotalResult(vo.getTotalResult().add(vo.getSubjectResultMap().get(subject)));
            }
            Collections.sort(vos);
            for (int i = 0; i < vos.size(); i++) {
                int j = i + 1;
                if (i == vos.size() - 1 && vos.get(i - 1).getSubjectResult().equals(vos.get(i).getSubjectResult())) {
                    //边界
                    vos.get(i).getSubjectRankMap().put(subject, vos.get(i - 1).getSubjectRankMap().get(subject));
                } else {
                    vos.get(i).getSubjectRankMap().put(subject, j);
                }
                while (i < (vos.size() - 1) && vos.get(i + 1).getSubjectResult().equals(vos.get(i).getSubjectResult())) {
                    i++;
                    vos.get(i).getSubjectRankMap().put(subject, j);
                }
            }
        }
    }

    @Override
    public void calculate(List<EntranceExcelVo> vos, String subject) {
        // A 0.15 B 0.3 C 0.3 E 0.2 F 0.5
         getLevelMap(vos, subject);

    }

    @Override
    public List<String> getClassName(List<EntranceExcelVo> vos) {
        HashSet<String> set = new HashSet<String>();
        for (EntranceExcelVo vo :
                vos) {
            if(StringUtils.isNotBlank(vo.getClassName())){
                set.add(vo.getClassName());
            }
        }
        List<String> result = new ArrayList<String>(set);
        Collections.sort(result);
        return result;     }

    private void getLevelMap(List<EntranceExcelVo> vos, String subject) {

        BigDecimal total = new BigDecimal(vos.size());
        BigDecimal levelA = total.multiply(Constants.ENTRANCE_LEVEL_A).setScale(2, RoundingMode.HALF_UP);
        int levelAFloor = levelA.intValue() + 1;
        BigDecimal levelB = total.multiply(Constants.ENTRANCE_LEVEL_B).setScale(2, RoundingMode.HALF_UP);
        int levelBFloor = levelB.intValue() + 1;
        BigDecimal levelC = total.multiply(Constants.ENTRANCE_LEVEL_C).setScale(2, RoundingMode.HALF_UP);
        int levelCFloor = levelC.intValue() + 1;
        BigDecimal levelD = total.multiply(Constants.ENTRANCE_LEVEL_D).setScale(2, RoundingMode.HALF_UP);
        int levelDFloor = levelD.intValue() + 1;
        BigDecimal levelE = total.multiply(Constants.ENTRANCE_LEVEL_E).setScale(2, RoundingMode.HALF_UP);
        int levelEFloor = levelE.intValue();
        for (EntranceExcelVo vo :
                vos) {
            if ("total".equals(subject)) {
                if (vo.getTotalRank() <= levelAFloor) {
                    vo.getLevelMap().put(subject,"A");
                } else if (vo.getTotalRank() <= levelBFloor) {
                    vo.getLevelMap().put(subject,"B");
                } else if (vo.getTotalRank() <= levelCFloor) {
                    vo.getLevelMap().put(subject,"C");
                } else if (vo.getTotalRank() <= levelDFloor) {
                    vo.getLevelMap().put(subject,"D");
                } else if (vo.getTotalRank() <= levelEFloor) {
                    vo.getLevelMap().put(subject,"E");
                }
            } else {
                if (vo.getSubjectRankMap().get(subject) <= levelAFloor) {
                    vo.getLevelMap().put(subject,"A");
                } else if (vo.getSubjectRankMap().get(subject) <= levelBFloor) {
                    vo.getLevelMap().put(subject,"B");
                } else if (vo.getSubjectRankMap().get(subject) <= levelCFloor) {
                    vo.getLevelMap().put(subject,"C");
                } else if (vo.getSubjectRankMap().get(subject) <= levelDFloor) {
                    vo.getLevelMap().put(subject,"D");
                } else if (vo.getSubjectRankMap().get(subject) <= levelEFloor) {
                    vo.getLevelMap().put(subject,"E");
                }
            }
        }

    }
}
