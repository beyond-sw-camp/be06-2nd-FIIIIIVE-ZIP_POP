package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT cm FROM Customer cm WHERE cm.email = :customerEmail")
    Optional<Customer> findByCustomerEmail(String customerEmail);
}
