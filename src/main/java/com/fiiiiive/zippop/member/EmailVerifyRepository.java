package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.EmailVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerifyRepository extends JpaRepository<EmailVerify, Long> {
    Optional<EmailVerify> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM EmailVerify ev WHERE ev.email = :email")
    int deleteByEmail(String email);
}
