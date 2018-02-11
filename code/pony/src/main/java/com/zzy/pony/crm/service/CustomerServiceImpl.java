package com.zzy.pony.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.crm.dao.CustomerDao;
import com.zzy.pony.crm.model.Customer;
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerDao dao;

	@Override
	public List<Customer> findAll() {
		return dao.findAll();
	}

}
