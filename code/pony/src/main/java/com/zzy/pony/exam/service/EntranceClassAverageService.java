package com.zzy.pony.exam.service;

import com.zzy.pony.exam.vo.EntranceExcelVo;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-05-07
 * @Description
 */
public interface EntranceClassAverageService {


    List<EntranceExcelVo> getEntranceExcelVo(Workbook wb) throws Exception  ;
    /**
     * Author: WANGCHAO262
     * Date  : 2018-05-07
     * Description: 计算总分以及排名
     */
    void sort(List<EntranceExcelVo> vos, String subject);

    void calculate(List<EntranceExcelVo> vos, String subject);

    List<String> getClassName(List<EntranceExcelVo> vos);


  
}
