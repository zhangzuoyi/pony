package com.zzy.pony.evaluation.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.evaluation.dao.OutcomeAttachDao;
import com.zzy.pony.evaluation.dao.OutcomeDao;
import com.zzy.pony.evaluation.dao.OutcomeValueDao;
import com.zzy.pony.evaluation.mapper.OutcomeMapper;
import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.model.OutcomeAttach;
import com.zzy.pony.evaluation.model.OutcomeValue;
import com.zzy.pony.evaluation.vo.OutcomeVo;
import com.zzy.pony.util.DateTimeUtil;
@Service
@Transactional
public class OutcomeServiceImpl implements OutcomeService {
	@Autowired
	private OutcomeMapper mapper;
	@Autowired
	private OutcomeDao dao;
	@Autowired
	private OutcomeValueDao valueDao;
	@Autowired
	private OutcomeAttachDao attachDao;
	@Value("${outcomeAttach.baseDir}")
	private String attachBaseDir;

	@Override
	public List<OutcomeVo> findByTeacher(Integer teacherId) {
		return mapper.findByTeacher(teacherId);
	}

	@Override
	public void add(Outcome outcome) {
		setScore(outcome);
		dao.save(outcome);
	}
	
	private void setScore(Outcome outcome){
		OutcomeValue ov=valueDao.findByCategoryAndLevel1AndLevel2(outcome.getCategory(), outcome.getLevel1(), outcome.getLevel2());
		if(ov != null){
			outcome.setScore(ov.getScore());
		}
	}

	@Override
	public void saveAttach(MultipartFile file, Long outcomeId) {
		
		if (file!=null && !file.isEmpty()) {
			try {
				String childPath = DateTimeUtil.dateToStr(new Date());//子路径
				String oldFileName=file.getOriginalFilename();
				String newFileName=UUID.randomUUID().toString()+oldFileName;
				String savePath = childPath+File.separator +newFileName;
				InputStream inputStream = file.getInputStream();
				File childDir = new File(attachBaseDir, childPath);//根路径+子路径
				if ( ! childDir.exists()) {
					childDir.mkdirs();
				}
				File localFile = new File(attachBaseDir,savePath);
				FileOutputStream outputStream = new FileOutputStream(localFile);
				IOUtils.copy(inputStream, outputStream);
				inputStream.close();
				outputStream.close();
				
				Outcome oc=dao.findOne(outcomeId);
				OutcomeAttach attach=new OutcomeAttach();
				attach.setOldFileName(oldFileName);
				attach.setOutcome(oc);
				attach.setSavePath(savePath);
				attachDao.save(attach);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
	}

	@Override
	public List<OutcomeAttach> findAttach(Long outcomeId) {
		Outcome outcome=new Outcome();
		outcome.setOutcomeId(outcomeId);
		return attachDao.findByOutcome(outcome);
	}

	@Override
	public byte[] getAttachContent(OutcomeAttach attach) {
		File localFile = new File(attachBaseDir,attach.getSavePath());
		try {
			FileInputStream in = new FileInputStream(localFile);
			byte[] result=IOUtils.toByteArray(in);
			in.close();
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(Outcome outcome) {
		Outcome old=dao.findOne(outcome.getOutcomeId());
		old.setCategory(outcome.getCategory());
		old.setDescription(outcome.getDescription());
		old.setLevel1(outcome.getLevel1());
		old.setLevel2(outcome.getLevel2());
		old.setOccurDate(outcome.getOccurDate());
		setScore(old);
		dao.save(old);
	}

	@Override
	public void deleteAttach(Long attachId) {
		OutcomeAttach attach=attachDao.findOne(attachId);
		File localFile = new File(attachBaseDir,attach.getSavePath());
		localFile.delete();
		attachDao.delete(attach);
	}

	@Override
	public List<OutcomeVo> findAll() {
		return mapper.findAll();
	}

	@Override
	public void check(Long outcomeId,String loginName) {
		Outcome outcome=dao.findOne(outcomeId);
		outcome.setStatus(Outcome.STATUS_CHECKED);
		outcome.setCheckTime(new Date());
		outcome.setCheckUser(loginName);
		dao.save(outcome);
	}

	@Override
	public void delete(Long outcomeId) {
		Outcome outcome=dao.findOne(outcomeId);
		if(outcome.getAttaches() !=null && outcome.getAttaches().size()>0) {
			for(OutcomeAttach oa : outcome.getAttaches()) {
				deleteAttach(oa.getId());
			}
		}
		dao.delete(outcome);
	}

}
