package com.fiiiiive.zippop.orders;

import com.fiiiiive.zippop.orders.model.CompanyOrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyOrdersDetailRepository extends JpaRepository<CompanyOrdersDetail, Long> { }
