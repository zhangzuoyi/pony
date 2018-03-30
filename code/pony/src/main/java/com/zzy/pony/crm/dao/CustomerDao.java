package com.zzy.pony.crm.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.crm.model.Customer;

public interface CustomerDao extends JpaRepository<Customer,Long>{
	Page<Customer> findAll(Pageable pageable);
}
