package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.EmailVerify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerifyRepository extends JpaRepository<EmailVerify, Long> {
    Optional<EmailVerify> findByEmail(String email);
}
