package com.zzy.pony.crm.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zzy.pony.crm.model.Customer;

public interface CustomerService {
	List<Customer> findAll();
	void add(Customer customer, String user);
	void update(Customer customer, String user);
	void delete(long id);
	Page<Customer> findPage(int currentPage, int pageSize);
}
