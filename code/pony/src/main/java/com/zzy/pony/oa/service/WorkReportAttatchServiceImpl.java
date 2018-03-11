package com.zzy.pony.oa.service;

import com.zzy.pony.oa.mapper.WorkReportAttachMapper;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.model.WorkReportAttach;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.util.DateTimeUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: wangchao262
 * @Date: 2018-02-27
 * @Description
 */
@Service
@Transactional
public class WorkReportAttatchServiceImpl implements WorkReportAttatchService {

    @Value("${oaReportAttatch.baseUrl}")
    private String attatchPath;
    @Autowired
    private WorkReportAttachMapper workReportAttachMapper;

    @Override
    public void addFile(MultipartFile file, long reportId, int typeId) {
        String childPath = DateTimeUtil.dateToStr(new Date());//子路径
        File childDir = null;
        File localFile = null;
        String fileName ="";
        if (file!=null && !file.isEmpty()) {
            try {
                fileName = childPath+File.separator +file.getOriginalFilename();
                InputStream inputStream = file.getInputStream();
                childDir = new File(attatchPath, childPath);//根路径+子路径
                if (childDir.exists()) {
                    //子文件夹存在
                    localFile = new File(childDir,file.getOriginalFilename());
                    FileOutputStream outputStream = new FileOutputStream(localFile);
                    IOUtils.copy(inputStream, outputStream);
                    inputStream.close();
                    outputStream.close();
                }else {
                    childDir.mkdirs();
                    localFile = new File(childDir,file.getOriginalFilename());
                    FileOutputStream outputStream = new FileOutputStream(localFile);
                    IOUtils.copy(inputStream, outputStream);
                    inputStream.close();
                    outputStream.close();

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WorkReportAttach ta = new WorkReportAttach();
        ta.setCreateTime(new Date());
        ta.setCreateUser(ShiroUtil.getLoginName());
        ta.setTargetId(reportId);
        ta.setTargetType(typeId);
        ta.setFileName(fileName);
        ta.setOriginalName(file.getOriginalFilename());
        ta.setSavePath(childPath);
        workReportAttachMapper.addFile(ta);
    }

    @Override
    public List<WorkReportAttach> findByReportId(long reportId) {
        return workReportAttachMapper.findByReportId(reportId);
    }

    @Override
    public List<WorkReportAttach> findByTypeAndReportId(int type, long reportId) {
        return workReportAttachMapper.findByTypeAndReportId(type,reportId);
    }

    @Override
    public WorkReportAttach findByAttachId(long id) {
        return workReportAttachMapper.findByAttachId(id);
    }
}
