package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCustomerIdx(Long CustomerIdx);
}
