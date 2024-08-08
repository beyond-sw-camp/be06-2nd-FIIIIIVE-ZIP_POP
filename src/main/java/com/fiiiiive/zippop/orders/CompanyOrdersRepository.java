package com.fiiiiive.zippop.orders;

import com.fiiiiive.zippop.orders.model.CompanyOrders;
import com.fiiiiive.zippop.orders.model.CustomerOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyOrdersRepository extends JpaRepository<CompanyOrders, Long> {
    @Query("SELECT co FROM CompanyOrders co JOIN FETCH co.company c WHERE c.companyIdx = :companyIdx")
    Optional<List<CompanyOrders>> findByCompanyIdx(Long companyIdx);
}