package com.zzy.pony.crm.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.zzy.pony.crm.dao.CustomerDao;
import com.zzy.pony.crm.model.Customer;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerDao dao;

	@Override
	public List<Customer> findAll() {
		return dao.findAll();
	}

	@Override
	public void add(Customer customer, String user) {
		customer.setCreateTime(new Date());
		customer.setCreateUser(user);
		customer.setUpdateTime(new Date());
		customer.setUpdateUser(user);
		dao.save(customer);
	}

	@Override
	public void update(Customer customer, String user) {
		Customer old=dao.findOne(customer.getId());
		old.setUpdateTime(new Date());
		old.setUpdateUser(user);
		old.setAddr(customer.getAddr());
		old.setAreaCode(customer.getAreaCode());
		old.setCityCode(customer.getCityCode());
		old.setComments(customer.getComments());
		old.setCountyCode(customer.getCountyCode());
		old.setIndustryId(customer.getIndustryId());
		old.setLevel(customer.getLevel());
		old.setManager(customer.getManager());
		old.setName(customer.getName());
		old.setPhone(customer.getPhone());
		old.setProvinceCode(customer.getProvinceCode());
		old.setStatus(customer.getStatus());
		old.setType(customer.getType());
		old.setWebsite(customer.getWebsite());
		dao.save(customer);
		
	}

	@Override
	public void delete(long id) {
		dao.delete(id);
		
	}

	@Override
	public Page<Customer> findPage(int currentPage, int pageSize) {
		Sort sort = new Sort(Direction.DESC, "id");
		Pageable pageable = new PageRequest(currentPage-1,pageSize, sort);
		return dao.findAll(pageable);
	}

}
