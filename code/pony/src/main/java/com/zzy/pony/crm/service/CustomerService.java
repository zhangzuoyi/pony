package com.zzy.pony.crm.service;

import java.util.List;

import com.zzy.pony.crm.model.Customer;

public interface CustomerService {
	List<Customer> findAll();
	void add(Customer customer, String user);
}
