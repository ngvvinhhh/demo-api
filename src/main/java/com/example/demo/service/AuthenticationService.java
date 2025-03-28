package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Enum.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.model.*;
import com.example.demo.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    List<Account> accounts = new ArrayList<>();
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;



    public RegisterResponse register(RegisterRequest registerRequest) {
        try {
            // Kiểm tra email đã tồn tại hay chưa
            if (accountRepository.existsByEmail(registerRequest.getEmail())) {
                throw new DuplicateEntity("Email already exists!");
            }

            Account account = modelMapper.map(registerRequest, Account.class);
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            account.setRole(Role.CUSTOMER);
            account.setCreateAt(LocalDateTime.now());
            Account newAccount = accountRepository.save(account);

            return modelMapper.map(newAccount, RegisterResponse.class);
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntity("Duplicate entry: " + e.getMessage());
        }
    }



    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));

            Account account = (Account) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse;
        }catch (Exception e) {
            throw new EntityNotFoundException("Username or Password invalid!!!");
        }
    }

    @GetMapping("account")
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findAccountByEmail(email);
    }

    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }

    public ProfileResponse getProfile() {
        Account currentAccount = getCurrentAccount();
        return modelMapper.map(currentAccount, ProfileResponse.class);
    }

    public ProfileResponse updateProfile(ProfileRequest updateProfileRequest) {
        Account currentAccount = getCurrentAccount();

        if (updateProfileRequest.getName() != null) {
            currentAccount.setName(updateProfileRequest.getName());
        }
        if (updateProfileRequest.getEmail() != null) {
            currentAccount.setEmail(updateProfileRequest.getEmail());
        }
        if (updateProfileRequest.getAddress() != null) {
            currentAccount.setAddress(updateProfileRequest.getAddress());
        }
        if (updateProfileRequest.getGender() != null) {
            currentAccount.setGender(updateProfileRequest.getGender());
        }
        if (updateProfileRequest.getPhone() != null) {
            currentAccount.setPhone(updateProfileRequest.getPhone());
        }

        Account updatedAccount = accountRepository.save(currentAccount);

        return modelMapper.map(updatedAccount, ProfileResponse.class);
    }

}
