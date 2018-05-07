package com.zzy.pony.exam.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.vo.EntranceExcelVo;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<EntranceExcelVo> getEntranceExcelVo(Workbook wb)  {
        if(wb == null){
            return null;
        }
        List<EntranceExcelVo> result = new ArrayList<EntranceExcelVo>();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row headRow = sheet.getRow(0);
        Row row = null;
        // 正文内容应该从第二行开始,第一行为表头的标题
        try {
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                int colNum = row.getPhysicalNumberOfCells();
                EntranceExcelVo vo = new EntranceExcelVo();
                for(int j = 0; j<= colNum ; j++){
                    if (row.getCell(j) == null || row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK
                            ||  Math.abs(row.getCell(j).getNumericCellValue())  <= 0.000001) {
                        continue;
                    } else {
                        switch (j){
                            case 0 : vo.setSchoolName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            case 1 : vo.setClassName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            case 2 : vo.setStudentName(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            case 3 : vo.setStudentNo(ReadExcelUtils.getCellFormatValue(row.getCell(j)).toString()); break;
                            //成绩
                            default: vo.getSubjectResultMap().put(headRow.getCell(j).toString(),new BigDecimal(row.getCell(j).getNumericCellValue()));
                        }
                        vo.setTotalResult(BigDecimal.ZERO);
                    }
                }
                result.add(vo);
            }
        }catch (Exception e){
            logger.error("excel解析出错");
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void sort(List<EntranceExcelVo> vos,List<String> subjects) {
        for (String subject :
        subjects) {
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
    }


    @Override
    public void calculate(List<EntranceExcelVo> vos, String subject,List<String> schoolNames) {
        // A 0.15 B 0.3 C 0.3 E 0.2 F 0.5
        List<EntranceExcelVo> levelAVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelBVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelCVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelDVos = new ArrayList<EntranceExcelVo>();
        List<EntranceExcelVo> levelEVos = new ArrayList<EntranceExcelVo>();
        BigDecimal total  = new BigDecimal(vos.size());
        BigDecimal levelA =  total.multiply(Constants.ENTRANCE_LEVEL_A).setScale(2, RoundingMode.HALF_UP);
        int levelAFloor =  levelA.intValue();
        BigDecimal levelB =  total.multiply(Constants.ENTRANCE_LEVEL_B).setScale(2, RoundingMode.HALF_UP);
        int levelBFloor =  levelB.intValue();
        BigDecimal levelC =  total.multiply(Constants.ENTRANCE_LEVEL_C).setScale(2, RoundingMode.HALF_UP);
        int levelCFloor =  levelC.intValue();
        BigDecimal levelD =  total.multiply(Constants.ENTRANCE_LEVEL_D).setScale(2, RoundingMode.HALF_UP);
        int levelDFloor =  levelD.intValue();
        BigDecimal levelE =  total.multiply(Constants.ENTRANCE_LEVEL_E).setScale(2, RoundingMode.HALF_UP);
        int levelEFloor =  levelE.intValue();
        for (EntranceExcelVo vo:
        vos) {
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

    private void calculateLevel(List<EntranceExcelVo> vos,int levelFloor,List<String> schoolNames){
        //上一阶段遗留处理逻辑
        for (EntranceExcelVo vo:
             vos) {

        }


    }


}
