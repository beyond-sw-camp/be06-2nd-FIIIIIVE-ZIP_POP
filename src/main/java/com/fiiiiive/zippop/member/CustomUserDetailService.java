package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if(!customerOptional.isEmpty()) {
            System.out.println(customerOptional);
            Customer customer = customerOptional.get();
            return CustomUserDetails.builder()
                    .idx(customer.getIdx())
                    .email(customer.getEmail())
                    .password(customer.getPassword())
                    .role(customer.getRole())
                    .enabled(customer.getEnabled())
                    .build();
        } else{
            Optional<Company> companyOptional = companyRepository.findByEmail(email);
            Company company = companyOptional.get();
            if(companyOptional.isPresent()){
                return CustomUserDetails.builder()
                        .idx(company.getIdx())
                        .email(company.getEmail())
                        .password(company.getPassword())
                        .role(company.getRole())
                        .enabled(company.getEnabled())
                        .build();
            }
            return null;
        }
    }
}