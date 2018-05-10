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
public interface EntranceAverageService {


    List<EntranceExcelVo> getEntranceExcelVo(Workbook wb) throws Exception  ;
    /**
     * Author: WANGCHAO262
     * Date  : 2018-05-07
     * Description: 计算总分以及排名
     */
    void sort(List<EntranceExcelVo> vos,String subject);

    List<Map<String,Map<String,BigDecimal>>> calculate(List<EntranceExcelVo> vos, String subject);

    List<String> getSchoolName(List<EntranceExcelVo> vos);
    List<String> getClassName(List<EntranceExcelVo> vos,String schoolName);


    /**
     * Author: WANGCHAO262
     * Date  : 2018-05-10
     * Description: 计算某个学校的均量值
     */
    List<Map<String,Map<String,BigDecimal>>> calculateSchool( List<EntranceExcelVo> vos,String subject,String schoolName);

}
