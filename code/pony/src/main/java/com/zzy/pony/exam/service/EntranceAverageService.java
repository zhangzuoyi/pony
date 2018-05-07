package com.zzy.pony.exam.service;

import com.zzy.pony.exam.vo.EntranceExcelVo;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-05-07
 * @Description
 */
public interface EntranceAverageService {


    List<EntranceExcelVo> getEntranceExcelVo(Workbook wb)  ;
    /**
     * Author: WANGCHAO262
     * Date  : 2018-05-07
     * Description: 计算总分以及排名
     */
    void sort(List<EntranceExcelVo> vos,List<String> subjects);

    void calculate(List<EntranceExcelVo> vos,String subject,List<String> schoolNames);

}
