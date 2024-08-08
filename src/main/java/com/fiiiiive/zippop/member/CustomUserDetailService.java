package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> resultCustomer = customerRepository.findByCustomerEmail(email);
        if (resultCustomer.isPresent()) {
            Customer customer = resultCustomer.get();
            return CustomUserDetails.builder()
                    .idx(customer.getCustomerIdx())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .password(customer.getPassword())
                    .role(customer.getRole())
                    .enabled(customer.getEnabled())
                    .build();
        } else {
            Optional<Company> resultCompany = companyRepository.findByCompanyEmail(email);
            if (resultCompany.isPresent()) {
                Company company = resultCompany.get();
                return CustomUserDetails.builder()
                        .idx(company.getCompanyIdx())
                        .name(company.getName())
                        .email(company.getEmail())
                        .password(company.getPassword())
                        .role(company.getRole())
                        .enabled(company.getEnabled())
                        .build();
            } else {
                return null;
            }
        }
    }
}