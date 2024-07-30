package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.EmailVerify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailVerifyRepository emailVerifyRepository;
    public Boolean isExist(String email, String uuid) throws BaseException {
        Optional<EmailVerify> result = emailVerifyRepository.findByEmail(email);
        if (result.isPresent()) {
            EmailVerify emailVerify = result.get();
            if (emailVerify.getUuid().equals(uuid)) { return true; }
        } else { throw new BaseException(BaseResponseMessage.MEMBER_EMAIL_VERIFY_FAIL); }
        return false;
    }

    public void save(String email, String uuid) throws BaseException{
        EmailVerify emailVerify = EmailVerify.builder()
                .email(email)
                .uuid(uuid)
                .build();
        emailVerifyRepository.save(emailVerify);
    }
}
