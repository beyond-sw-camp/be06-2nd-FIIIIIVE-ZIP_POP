package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT cp FROM Company cp WHERE cp.email = :companyEmail")
    Optional<Company> findByCompanyEmail(String companyEmail);
}
