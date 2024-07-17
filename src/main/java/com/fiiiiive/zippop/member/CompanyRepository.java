package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);
    public Optional<Company> findByEmail(String email);
    public Optional<Company> findByIdx(Long idx);
}
