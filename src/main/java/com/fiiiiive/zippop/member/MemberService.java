package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.member.model.request.PostSignupReq;
import com.fiiiiive.zippop.member.model.response.PostSignupRes;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final JavaMailSender emailSender;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public PostSignupRes signup(PostSignupReq request) throws BaseException {
        if(request.getCrn() != null && Objects.equals(request.getRole(), "ROLE_COMPANY")){
            if(customerRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_REGISTER_AS_CUSTOMER);
            }
            Company company = Company.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .crn(request.getCrn())
                    .role(request.getRole())
                    .enabled(false)
                    .build();
            companyRepository.save(company);
            return PostSignupRes.builder()
                    .idx(company.getCompanyIdx())
                    .role(request.getRole())
                    .email(request.getEmail())
                    .build();
        } else {
            if(companyRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_REGISTER_AS_COMPANY);
            }
            Customer customer = Customer.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .role(request.getRole())
                    .point(3000)
                    .enabled(false)
                    .build();
            customerRepository.save(customer);
            return PostSignupRes.builder()
                    .idx(customer.getCustomerIdx())
                    .role(request.getRole())
                    .email(request.getEmail())
                    .build();
        }
    }

    public Boolean activeMember(String email, String role) throws Exception, BaseException {
        if(Objects.equals(role, "ROLE_COMPANY")){
            Optional<Company> result = companyRepository.findByEmail(email);
            if(result.isPresent()){
                Company company = result.get();
                company.setEnabled(true);
                companyRepository.save(company);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EMAIL_VERIFY_FAIL);
            }
        } else {
            Optional<Customer> result = customerRepository.findByEmail(email);
            if(result.isPresent()) {
                Customer customer = result.get();
                customer.setEnabled(true);
                customerRepository.save(customer);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EMAIL_VERIFY_FAIL);
            }
        }
        return true;
    }

    public String sendEmail(PostSignupReq dto) throws MailException, Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getEmail());
        if(Objects.equals(dto.getRole(), "ROLE_COMPANY")){ message.setSubject("ZIPPOP에 기업으로 가입하신걸 환영합니다."); }
        else { message.setSubject("ZIPPOP에 회원으로 가입하신걸 환영합니다."); }
        String uuid = UUID.randomUUID().toString();
        message.setText("http://localhost:8080/api/v1/member/verify?email="+dto.getEmail()+"&role="+dto.getRole()+"&uuid="+uuid);
        emailSender.send(message);
        return uuid;
    }
}
