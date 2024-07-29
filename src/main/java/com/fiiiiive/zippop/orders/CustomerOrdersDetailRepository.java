package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.orders.model.CompanyOrdersDetail;
import com.fiiiiive.zippop.orders.model.CustomerOrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrdersDetailRepository extends JpaRepository<CustomerOrdersDetail, Long> { }