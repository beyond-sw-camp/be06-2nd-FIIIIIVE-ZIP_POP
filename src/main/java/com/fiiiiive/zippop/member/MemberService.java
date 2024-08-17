package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.member.model.request.EditInfoReq;
import com.fiiiiive.zippop.member.model.request.EditPasswordReq;
import com.fiiiiive.zippop.member.model.request.PostSignupReq;
import com.fiiiiive.zippop.member.model.response.GetProfileRes;
import com.fiiiiive.zippop.member.model.response.PostSignupRes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final JavaMailSender emailSender;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final EmailVerifyRepository emailVerifyRepository;
    private final PasswordEncoder passwordEncoder;

    public PostSignupRes signup(PostSignupReq dto) throws BaseException {
        if(dto.getCrn() != null && Objects.equals(dto.getRole(), "ROLE_COMPANY")){
            if(customerRepository.findByCustomerEmail(dto.getEmail()).isPresent()) {
                throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_REGISTER_AS_CUSTOMER);
            }
            Optional<Company> result = companyRepository.findByCompanyEmail(dto.getEmail());
            if(result.isPresent()){
                Company company = result.get();
                if(!company.getEnabled() && company.getInactive()) {
                    return PostSignupRes.builder()
                            .idx(company.getCompanyIdx())
                            .enabled(company.getEnabled())
                            .role(company.getRole())
                            .email(company.getEmail())
                            .inactive(company.getInactive())
                            .build();
                } else {
                    throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_EXIST);
                }
            } else{
                Company company = Company.builder()
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .name(dto.getName())
                        .crn(dto.getCrn())
                        .role(dto.getRole())
                        .address(dto.getAddress())
                        .phoneNumber(dto.getPhoneNumber())
                        .enabled(false)
                        .inactive(false)
                        .build();
                companyRepository.save(company);
                return PostSignupRes.builder()
                        .idx(company.getCompanyIdx())
                        .role(dto.getRole())
                        .enabled(company.getEnabled())
                        .inactive(company.getInactive())
                        .email(dto.getEmail())
                        .build();
            }
        } else {
            if(companyRepository.findByCompanyEmail(dto.getEmail()).isPresent()) {
                throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_REGISTER_AS_COMPANY);
            }
            Optional<Customer> result = customerRepository.findByCustomerEmail(dto.getEmail());
            if(result.isPresent()){
                Customer customer = result.get();
                if(!customer.getEnabled() && customer.getInactive()){
                    return PostSignupRes.builder()
                            .idx(customer.getCustomerIdx())
                            .role(customer.getRole())
                            .enabled(customer.getEnabled())
                            .inactive(customer.getInactive())
                            .email(customer.getEmail())
                            .build();
                }
                else {
                    throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_EXIST);
                }
            } else {
                Customer customer = Customer.builder()
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .name(dto.getName())
                        .role(dto.getRole())
                        .address(dto.getAddress())
                        .phoneNumber(dto.getPhoneNumber())
                        .point(3000)
                        .enabled(false)
                        .inactive(false)
                        .build();
                customerRepository.save(customer);
                return PostSignupRes.builder()
                        .idx(customer.getCustomerIdx())
                        .role(customer.getRole())
                        .enabled(customer.getEnabled())
                        .inactive(customer.getInactive())
                        .email(customer.getEmail())
                        .build();
            }
        }
    }

    public Boolean activeMember(String email, String role) throws BaseException {
        if(Objects.equals(role, "ROLE_COMPANY")){
            Optional<Company> result = companyRepository.findByCompanyEmail(email);
            if(result.isPresent()){
                Company company = result.get();
                company.setEnabled(true);
                company.setInactive(false);
                companyRepository.save(company);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EMAIL_VERIFY_FAIL);
            }
        } else {
            Optional<Customer> result = customerRepository.findByCustomerEmail(email);
            if(result.isPresent()) {
                Customer customer = result.get();
                customer.setEnabled(true);
                customer.setInactive(false);
                customerRepository.save(customer);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EMAIL_VERIFY_FAIL);
            }
        }
        return true;
    }

    public String sendEmail(PostSignupRes response) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(response.getEmail());
        if(Objects.equals(response.getRole(), "ROLE_COMPANY")){
            if(!response.getEnabled() && !response.getInactive()){
                message.setSubject("ZIPPOP - 기업으로 가입하신걸 환영합니다.");
            } else {
                message.setSubject("ZIPPOP - 기업회원계정 복구 이메일");
            }
        }
        else {
            if(!response.getEnabled() && !response.getInactive()){
                message.setSubject("ZIPPOP - 고객으로 가입하신걸 환영합니다.");
            } else {
                message.setSubject("ZIPPOP - 고객회원계정 복구 이메일");
            }
        }
        String uuid = UUID.randomUUID().toString();
        message.setText("http://localhost:8080/api/v1/member/verify?email="+response.getEmail()+"&role="+response.getRole()+"&uuid="+uuid);
        emailSender.send(message);
        return uuid;
    }

    @Transactional(rollbackFor = Exception.class)
    public void inActiveMenber(CustomUserDetails customUserDetails) throws BaseException {
        String email = customUserDetails.getUsername();
        String role = customUserDetails.getRole();
        Long idx = customUserDetails.getIdx();
        if(Objects.equals(role, "ROLE_COMPANY")){
            Optional<Company> result = companyRepository.findByCompanyEmail(email);
            if(result.isPresent()){
                Company company = result.get();
                company.setEnabled(false);
                company.setInactive(true);
                companyRepository.save(company);
                emailVerifyRepository.deleteByEmail(email);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_INACTIVE_FAIL);
            }
        } else {
            Optional<Customer> result = customerRepository.findByCustomerEmail(customUserDetails.getEmail());
            if(result.isPresent()) {
                Customer customer = result.get();
                customer.setEnabled(false);
                customer.setInactive(true);
                if(customer.getPassword() == null){
                    customerRepository.save(customer);
                } else {
                    customerRepository.save(customer);
                    emailVerifyRepository.deleteByEmail(email);
                }
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_INACTIVE_FAIL);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void editInfo(CustomUserDetails customUserDetails, EditInfoReq dto) throws BaseException {
        String email = customUserDetails.getEmail();
        String role = customUserDetails.getRole();
        if(Objects.equals(role, "ROLE_COMPANY")){
            Optional<Company> result = companyRepository.findByCompanyEmail(email);
            if(result.isPresent()){
                Company company = result.get();
                company.setName(dto.getName());
                company.setAddress(dto.getAddress());
                company.setCrn(dto.getCrn());
                company.setPhoneNumber(dto.getPhoneNumber());
                companyRepository.save(company);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EDIT_INFO_FAIL);
            }
        } else {
            Optional<Customer> result = customerRepository.findByCustomerEmail(customUserDetails.getEmail());
            if(result.isPresent()) {
                Customer customer = result.get();
                customer.setName(dto.getName());
                customer.setAddress(dto.getAddress());
                customer.setPhoneNumber(dto.getPhoneNumber());
                customerRepository.save(customer);
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EDIT_INFO_FAIL);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void editPassword(CustomUserDetails customUserDetails, EditPasswordReq dto) throws BaseException {
        String email = customUserDetails.getEmail();
        String role = customUserDetails.getRole();
        if(Objects.equals(role, "ROLE_COMPANY")){
            Optional<Company> result = companyRepository.findByCompanyEmail(email);
            if(result.isPresent()){
                Company company = result.get();
                if(passwordEncoder.matches(dto.getOriginPassword(), company.getPassword()))
                {
                    company.setPassword(passwordEncoder.encode(dto.getNewPassword()));
                    companyRepository.save(company);
                }
                else {
                    throw new BaseException(BaseResponseMessage.MEMBER_EDIT_PASSWORD_FAIL_PASSWORD_NOT_MATCH);
                }
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EDIT_PASSWORD_FAIL);
            }
        } else {
            Optional<Customer> result = customerRepository.findByCustomerEmail(customUserDetails.getEmail());
            if(result.isPresent()) {
                Customer customer = result.get();
                if(passwordEncoder.matches(dto.getOriginPassword(), customer.getPassword()))
                {
                    customer.setPassword(passwordEncoder.encode(dto.getNewPassword()));
                    customerRepository.save(customer);
                }
                else {
                    throw new BaseException(BaseResponseMessage.MEMBER_EDIT_PASSWORD_FAIL_PASSWORD_NOT_MATCH);
                }
            } else {
                throw new BaseException(BaseResponseMessage.MEMBER_EDIT_PASSWORD_FAIL);
            }
        }
    }

    public GetProfileRes getProfile(CustomUserDetails customUserDetails) throws BaseException {
        if(Objects.equals(customUserDetails.getRole(), "ROLE_CUSTOMER")){
            Customer customer = customerRepository.findById(customUserDetails.getIdx())
            .orElseThrow(() -> new BaseException(BaseResponseMessage.MEMBER_PROFILE_FAIL));
            return GetProfileRes.builder()
                    .name(customer.getName())
                    .point(customer.getPoint())
                    .email(customer.getEmail())
                    .phoneNumber(customer.getPhoneNumber())
                    .address(customer.getAddress())
                    .build();
        } else{
            Company company = companyRepository.findById(customUserDetails.getIdx())
            .orElseThrow(() -> new BaseException(BaseResponseMessage.MEMBER_PROFILE_FAIL));
            return GetProfileRes.builder()
                    .name(company.getName())
                    .crn(company.getCrn())
                    .email(company.getEmail())
                    .phoneNumber(company.getPhoneNumber())
                    .address(company.getAddress())
                    .build();
        }
    }
}
