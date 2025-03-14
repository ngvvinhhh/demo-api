package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Autowired
    AccountRepository accountRepository;

    public final String SERECT_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407m";

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SERECT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Account account) {
        String token = Jwts.builder()
                .subject(account.getId()+"")
                .issuedAt(new Date(System.currentTimeMillis())) // 10:30
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    public Account getAccountByToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String idString = claims.getSubject();
        long id = Long.parseLong(idString);

        return accountRepository.findAccountById(id);
    }
}
