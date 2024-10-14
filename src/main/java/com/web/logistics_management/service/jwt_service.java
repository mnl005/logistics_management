package com.web.logistics_management.service;


import com.web.logistics_management.service.user.user_inter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.io.Encoders;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;



@RequiredArgsConstructor
@Service
public class jwt_service {

    private final user_inter crud_user;


    private final SecretKey key = Jwts.SIG.HS256.key().build();


    String secretString = Encoders.BASE64.encode(key.getEncoded());

    //권한 부여
    public void access(String id, HttpServletResponse response) {
        String token = issue(id);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(-1);
        cookie.setDomain("localhost");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void block(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(-1);
        cookie.setDomain("localhost");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //단기 토큰
    public String short_token(String id) {
        return Jwts.builder()
                .signWith(key)
                .claim("id", id) // 유저 아이디
                .issuedAt(new Date())  //발행 날짜
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) //2분 유효
                .compact();//키로 서명
    }

    //토큰 발급
    public String issue(String id) {
        return Jwts.builder()
                .claim("id", id) // 유저 아이디
                .issuedAt(new Date())  //발행 날짜
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24L)) //하루 유효
                .signWith(key).compact();//키로 서명
    }

    //토큰 검증
    public Optional<String> validations(String token) {
        Jwt<?, ?> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parse(token);

        Claims claims = (Claims) claimsJws.getPayload();

        System.out.println("Subject (id): " + claims.get("id"));
        System.out.println("Issued At: " + claims.getIssuedAt());
        System.out.println("Expiration: " + claims.getExpiration());

        return Optional.ofNullable(claims.get("id", String.class));
    }

    // request 로부터 토큰 불러옴
    public String request_get_token(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return token = cookie.getValue();
                }
            }
        }
        return null;
    }


}