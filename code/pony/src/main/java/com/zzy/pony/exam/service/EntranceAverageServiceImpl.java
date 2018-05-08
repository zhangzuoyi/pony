package com.zzy.pony.exam.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.vo.EntranceExcelVo;
import com.zzy.pony.model.Subject;
import com.zzy.pony.util.ReadExcelUtils;
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
 * @Date: 2018-05-07
 * @Description
 */
@Service
@Transactional
public class EntranceAverageServiceImpl implements EntranceAverageService {

    private static Logger logger = LoggerFactory.getLogger(EntranceAverageServiceImpl.class);

    @Override
    public List<EntranceExcelVo> getEntranceExcelVo(Workbook wb) throws Exception  {
        if(wb == null){
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
                for(int j = 0; j< colNum ; j++){
                    if (row.getCell(j) == null ||  Cell.CELL_TYPE_BLANK == row.getCell(j).getCellType()   ||(Cell.CELL_TYPE_NUMERIC == row.getCell(j).getCellType() && row.getCell(j).getNumericCellValue() <= 0.00001f   )) {
                        absent = true;
                        break;
                    } else {
                        switch (j){
                            case 0 : vo.setSchoolName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            case 1 : vo.setClassName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            case 2 : vo.setStudentName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            case 3 : vo.setStudentNo(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            //成绩
                            default: vo.getSubjectResultMap().put(headRow.getCell(j).toString(),new BigDecimal(row.getCell(j).getNumericCellValue()));
                        }
                    }
                    vo.setTotalResult(BigDecimal.ZERO);
                }
                if (absent){
                    absent = false;
                }else{
                    result.add(vo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("excel解析出错");
        }

        return result;
    }

    @Override
    public void sort(List<EntranceExcelVo> vos,String subject) {
        if ("total".equals(subject)){
            //总成绩
            for (EntranceExcelVo vo:
                    vos) {
                vo.setSubjectResult(vo.getTotalResult());
            }
            Collections.sort(vos);
            for (int i = 0 ;i<vos.size();i++){
                int j = i+1;
                if (i == vos.size()-1 &&  vos.get(i-1).getSubjectResult().equals(vos.get(i).getSubjectResult())){
                    //边界
                    vos.get(i).setTotalRank(vos.get(i-1).getTotalRank());
                }else{
                    vos.get(i).setTotalRank(j);
                }
                while (i < (vos.size()-1) &&  vos.get(i+1).getSubjectResult().equals(vos.get(i).getSubjectResult())){
                    i++;
                    vos.get(i).setTotalRank(j);
                }
            }
        }else{
            for (EntranceExcelVo vo:
                    vos) {
                vo.setSubjectResult(vo.getSubjectResultMap().get(subject));
                vo.setTotalResult(vo.getTotalResult().add(vo.getSubjectResultMap().get(subject)));
            }
            Collections.sort(vos);
            for (int i = 0 ;i<vos.size();i++){
                int j = i+1;
                if (i == vos.size()-1 &&  vos.get(i-1).getSubjectResult().equals(vos.get(i).getSubjectResult())){
                    //边界
                    vos.get(i).getSubjectRankMap().put(subject,vos.get(i-1).getSubjectRankMap().get(subject));
                }else{
                    vos.get(i).getSubjectRankMap().put(subject,j);
                }
                while (i < (vos.size()-1) &&  vos.get(i+1).getSubjectResult().equals(vos.get(i).getSubjectResult())){
                    i++;
                    vos.get(i).getSubjectRankMap().put(subject,j);
                }
            }
        }


    }


    @Override
    public List<Map<String,Map<String,BigDecimal>>> calculate(List<EntranceExcelVo> vos, String subject) {
        // A 0.15 B 0.3 C 0.3 E 0.2 F 0.5
        List<EntranceExcelVo> levelAVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelBVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelCVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelDVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelEVos = new ArrayList<EntranceExcelVo>();
        BigDecimal total  = new BigDecimal(vos.size());
        BigDecimal levelA =  total.multiply(Constants.ENTRANCE_LEVEL_A).setScale(2, RoundingMode.HALF_UP);
        int levelAFloor =  levelA.intValue()+1;
        BigDecimal levelB =  total.multiply(Constants.ENTRANCE_LEVEL_B).setScale(2, RoundingMode.HALF_UP);
        int levelBFloor =  levelB.intValue()+1;
        BigDecimal levelC =  total.multiply(Constants.ENTRANCE_LEVEL_C).setScale(2, RoundingMode.HALF_UP);
        int levelCFloor =  levelC.intValue()+1;
        BigDecimal levelD =  total.multiply(Constants.ENTRANCE_LEVEL_D).setScale(2, RoundingMode.HALF_UP);
        int levelDFloor =  levelD.intValue()+1;
        BigDecimal levelE =  total.multiply(Constants.ENTRANCE_LEVEL_E).setScale(2, RoundingMode.HALF_UP);
        int levelEFloor =  levelE.intValue();
        for (EntranceExcelVo vo:
        vos) {
            if ("total".equals(subject)){
                if(vo.getTotalRank()<=levelAFloor){
                    levelAVos.add(vo);
                }else if (vo.getTotalRank()<=levelBFloor){
                    levelBVos.add(vo);
                }else if (vo.getTotalRank()<=levelCFloor){
                    levelCVos.add(vo);
                }else if (vo.getTotalRank()<=levelDFloor){
                    levelDVos.add(vo);
                }else if (vo.getTotalRank()<=levelEFloor){
                    levelEVos.add(vo);
                }
            }else{
                if(vo.getSubjectRankMap().get(subject)<=levelAFloor){
                    levelAVos.add(vo);
                }else if (vo.getSubjectRankMap().get(subject)<=levelBFloor){
                    levelBVos.add(vo);
                }else if (vo.getSubjectRankMap().get(subject)<=levelCFloor){
                    levelCVos.add(vo);
                }else if (vo.getSubjectRankMap().get(subject)<=levelDFloor){
                    levelDVos.add(vo);
                }else if (vo.getSubjectRankMap().get(subject)<=levelEFloor){
                    levelEVos.add(vo);
                }
            }
        }
        Map<String,Map<String,BigDecimal>> levelAResult =  calculateLevel(levelAVos,subject,levelA,new HashMap<String, BigDecimal>());
        Map<String,Map<String,BigDecimal>> levelBResult =  calculateLevel(levelBVos,subject,levelB.subtract(levelA),levelAResult.get("remain"));
        Map<String,Map<String,BigDecimal>> levelCResult =  calculateLevel(levelCVos,subject,levelC.subtract(levelA).subtract(levelB),levelBResult.get("remain"));
        Map<String,Map<String,BigDecimal>> levelDResult =  calculateLevel(levelDVos,subject,levelD.subtract(levelA).subtract(levelB).subtract(levelC),levelCResult.get("remain"));
        Map<String,Map<String,BigDecimal>> levelEResult =  calculateLevel(levelEVos,subject,levelE.subtract(levelA).subtract(levelB).subtract(levelC).subtract(levelD),levelDResult.get("remain"));
        List<Map<String,Map<String,BigDecimal>>> list = new ArrayList<Map<String, Map<String, BigDecimal>>>();
        list.add(levelAResult);
        list.add(levelBResult);
        list.add(levelCResult);
        list.add(levelDResult);
        list.add(levelEResult);
        calculateAcc(list);
        return list;
    }

    /**
     * Author: WANGCHAO262
     * Date  : 2018-05-08
     * Description: lastMap 上一等级遗留
     */
    private Map<String,Map<String,BigDecimal>> calculateLevel(List<EntranceExcelVo> vos, String subject,BigDecimal level, Map<String,BigDecimal> lastMap){
        Map<String,Map<String,BigDecimal>> result = new HashMap<String, Map<String, BigDecimal>>();
        int levelFloor = level.intValue();
        //最后一名同名次人数
        int lastRank = 0;
        if ("total".equals(subject)){
            lastRank =    vos.get(vos.size()-1).getTotalRank();
        }else{
            lastRank =    vos.get(vos.size()-1).getSubjectRankMap().get(subject);
        }
        int sameCount = 0;
        Map<String,Integer> sameMap = new HashMap<String, Integer>();
        for (EntranceExcelVo vo:
             vos) {
            if ("total".equals(subject)){
                if (vo.getTotalRank() < lastRank){
                    if (lastMap.containsKey(vo.getSchoolName())){
                        lastMap.put(vo.getSchoolName(),lastMap.get(vo.getSchoolName()).add(BigDecimal.ONE));
                    }else{
                        lastMap.put(vo.getSchoolName(),BigDecimal.ONE);
                    }
                }else if (vo.getTotalRank() == lastRank){
                    sameCount ++ ;
                    if (sameMap.containsKey(vo.getSchoolName())){
                        sameMap.put(vo.getSchoolName(),sameMap.get(vo.getSchoolName()).intValue()+1);
                    }else{
                        sameMap.put(vo.getSchoolName(),1);
                    }
                }
            }else{
                if (vo.getSubjectRankMap().get(subject) < lastRank){
                    if (lastMap.containsKey(vo.getSchoolName())){
                        lastMap.put(vo.getSchoolName(),lastMap.get(vo.getSchoolName()).add(BigDecimal.ONE));
                    }else{
                        lastMap.put(vo.getSchoolName(),BigDecimal.ONE);
                    }
                }else if (vo.getSubjectRankMap().get(subject) == lastRank){
                    sameCount ++ ;
                    if (sameMap.containsKey(vo.getSchoolName())){
                        sameMap.put(vo.getSchoolName(),sameMap.get(vo.getSchoolName()).intValue()+1);
                    }else{
                        sameMap.put(vo.getSchoolName(),1);
                    }
                }

            }
        }
        //相同名次处理
        Map<String,BigDecimal> remainMap = new HashMap<String, BigDecimal>();
        BigDecimal bg =  level.subtract(new BigDecimal(vos.size())).add(new BigDecimal(sameCount));
        BigDecimal average = bg.divide(new BigDecimal(sameCount),RoundingMode.HALF_UP);
        BigDecimal averageRemain = BigDecimal.ONE.subtract(average);
        for (String schoolName:
        sameMap.keySet()) {
            BigDecimal tmp =  average.multiply(new BigDecimal(sameMap.get(schoolName)));
            if (lastMap.containsKey(schoolName)){
                lastMap.put(schoolName,lastMap.get(schoolName).add(tmp));
            }else{
                lastMap.put(schoolName,tmp);
            }
            remainMap.put(schoolName,averageRemain.multiply(new BigDecimal(sameMap.get(schoolName))));
        }
        //当前level
        result.put("current",lastMap);
        //遗留给下一level
        result.put("remain",remainMap);
        return result;
    }

    /**
     * Author: WANGCHAO262
     * Date  : 2018-05-08
     * Description: 计算档数和累数
     */
    private void calculateAcc(List<Map<String,Map<String,BigDecimal>>> list){

        BigDecimal accBg = BigDecimal.ZERO;
        for (Map<String,Map<String,BigDecimal>> map:
        list) {
           Map<String,BigDecimal> innerMap =   map.get("current");
           BigDecimal innerAccBg = BigDecimal.ZERO;
            for (String schoolName:
            innerMap.keySet()) {
                innerAccBg = innerAccBg.add(innerMap.get(schoolName));
            }
            accBg = accBg.add(innerAccBg);
            innerMap.put("acc",innerAccBg);
            innerMap.put("accSum",accBg);
        }
    }




}
