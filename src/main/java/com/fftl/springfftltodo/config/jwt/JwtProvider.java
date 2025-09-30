package com.fftl.springfftltodo.config.jwt;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/*
jwt 토큰의 생성과 검증을 담당합니다.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret.appKey}")
    private String secretKey;

    @Value("${jwt.access}")
    private Long accessExpiration; // 10분

    @Value("${jwt.refresh}")
    private Long refreshExpiration; // 1주일

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createRefreshToken(int memberId, String username){
        return generateToken(memberId, username, refreshExpiration);
    }

    public String createAccessToken(int memberId, String username){
        return generateToken(memberId, username, accessExpiration);
    }

    public String generateToken(int memberId, String username, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(Integer.toString(memberId))
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public int getMemberIdFromToken(String token) {
        Jws<Claims> parse = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        return Integer.parseInt(parse.getPayload().getSubject()) ;
    }

    //내가 사용했던 secretKey를 이용하여
    //이 토큰이 유효한 상태의 토큰인지 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }
}