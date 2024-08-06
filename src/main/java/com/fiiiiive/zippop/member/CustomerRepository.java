package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
<<<<<<< Updated upstream
    boolean existsByEmail(String email);
    public Optional<Customer> findByEmail(String email);
=======
    @Query("SELECT cm FROM Customer cm WHERE cm.email = :customerEmail")
    Optional<Customer> findByCustomerEmail(String customerEmail);
    Optional<Customer> findByCustomerIdx(Long idx);
>>>>>>> Stashed changes
}
