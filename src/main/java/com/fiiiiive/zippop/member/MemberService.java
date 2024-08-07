package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.member.model.request.EditInfoReq;
import com.fiiiiive.zippop.member.model.request.EditPasswordReq;
import com.fiiiiive.zippop.member.model.request.PostSignupReq;
import com.fiiiiive.zippop.member.model.response.GetPointRes;
import com.fiiiiive.zippop.member.model.response.GetProfileRes;
import com.fiiiiive.zippop.member.model.response.PostSignupRes;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
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

    public PostSignupRes signup(PostSignupReq request) throws BaseException {
        if(request.getCrn() != null && Objects.equals(request.getRole(), "ROLE_COMPANY")){
            if(customerRepository.findByCustomerEmail(request.getEmail()).isPresent()) {
                throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_REGISTER_AS_CUSTOMER);
            }
            Optional<Company> result = companyRepository.findByCompanyEmail(request.getEmail());
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
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .crn(request.getCrn())
                        .role(request.getRole())
                        .enabled(false)
                        .inactive(false)
                        .build();
                companyRepository.save(company);
                return PostSignupRes.builder()
                        .idx(company.getCompanyIdx())
                        .role(request.getRole())
                        .enabled(company.getEnabled())
                        .inactive(company.getInactive())
                        .email(request.getEmail())
                        .build();
            }
        } else {
            if(companyRepository.findByCompanyEmail(request.getEmail()).isPresent()) {
                throw new BaseException(BaseResponseMessage.MEMBER_REGISTER_FAIL_ALREADY_REGISTER_AS_COMPANY);
            }
            Optional<Customer> result = customerRepository.findByCustomerEmail(request.getEmail());
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
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .role(request.getRole())
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
    public GetPointRes getUserPoints(Long customerIdx) {
        Customer customer = customerRepository.findById(customerIdx).orElseThrow(() -> new RuntimeException("User not found"));
        return GetPointRes.builder()
                .customer_idx(customer.getCustomerIdx())
                .point(customer.getPoint())
                .build();
    }
    public GetProfileRes getProfile(CustomUserDetails customUserDetails) {

        return new GetProfileRes(customUserDetails.getUsername(), customUserDetails.getEmail());
    }

}
