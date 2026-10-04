package com.novablog.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final Duration validity;

    public JwtService(@Value("${novablog.jwt.secret}") String secret,
                      @Value("${novablog.jwt.access-token-minutes:120}") long minutes) {
        byte[] raw = secret.matches("^[A-Za-z0-9+/=]+$") && secret.length() >= 43
                ? Decoders.BASE64.decode(secret) : secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(raw);
        this.validity = Duration.ofMinutes(minutes);
    }

    public String createToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder().subject(username).issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(validity))).signWith(key).compact();
    }

    public String getUsername(String token) {
        return parse(token).getPayload().getSubject();
    }

    public boolean isValid(String token) {
        try {
            return parse(token).getPayload().getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public long getExpiresInSeconds() {
        return validity.toSeconds();
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }
}
