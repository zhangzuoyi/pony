package com.zzy.pony.tiku.controller;

import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.tiku.model.Zujuan;
import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.service.ZujuanQuestionService;
import com.zzy.pony.tiku.service.ZujuanService;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;
import com.zzy.pony.tiku.vo.ZujuanVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
@Controller
@RequestMapping(value = "/tiku/zujuan")
public class ZujuanController {

    @Autowired
    private ZujuanService zujuanService;
    @Autowired
    private ZujuanQuestionService zujuanQuestionService;

    @RequestMapping(value = "main")
    public String main() {
        return "tiku/zujuan/main";
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public Page<ZujuanVo> list(@RequestBody ConditionVo cv) {
        cv.setLoginName(ShiroUtil.getLoginName());
        return zujuanService.list(cv);

    }

    @RequestMapping(value = "listQuestion", method = RequestMethod.GET)
    @ResponseBody
    public List<ZujuanQuestionVo> listQuestion(@RequestParam(value = "zujuanId") long zujuanId) {
        return zujuanQuestionService.listQuestion(zujuanId);
    }


    @RequestMapping(value = "add")
    @ResponseBody
    public void add(@RequestParam(value = "name") String name,
                    @RequestParam(value = "gradeId") String gradeId,
                    @RequestParam(value = "subjectId") String subjectId,
                    @RequestParam(value = "questionIds[]") List<Long> questionIds) {

        //get 中文乱码 tomcat server.xml connector  URIEncoding="UTF-8"
        zujuanService.addBatch(name, gradeId, subjectId, questionIds);


    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public void delete(@RequestParam(value = "id") long id) {
        zujuanQuestionService.deleteByZujuanId(id);
        zujuanService.deleteById(id);
    }

    @RequestMapping(value = "questionUpdate",method = RequestMethod.POST)
    @ResponseBody
    public void questionUpdate(@RequestBody ZujuanQuestionVo vo) {

        ZujuanQuestion old = zujuanQuestionService.findById(vo.getId());
        old.setScore(vo.getScore());
        //old.setSeq(vo.getSeq());
        zujuanQuestionService.update(old);
    }
    @RequestMapping(value = "questionDelete")
    @ResponseBody
    public void questionDelete(@RequestParam(value="id") long  id) {

        zujuanQuestionService.deleteById(id);
    }


}
