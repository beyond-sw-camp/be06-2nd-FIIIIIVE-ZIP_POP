package com.fiiiiive.zippop.orders;

import com.fiiiiive.zippop.orders.model.CompanyOrders;
import com.fiiiiive.zippop.orders.model.CustomerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyOrdersRepository extends JpaRepository<CompanyOrders, Long> { }