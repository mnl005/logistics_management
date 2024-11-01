package com.web.logistics_management.service;



import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


    private final SecretKey key = Jwts.SIG.HS256.key().build();
    String secretString = Encoders.BASE64.encode(key.getEncoded());

    @Value("${cookie.domain}")
    private String COOKIE_DOMAIN;

    //권한 부여
    public void access(String id, HttpServletResponse response) {
        try{
            String token = issue(id);
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(-1);
            cookie.setDomain(COOKIE_DOMAIN); // 배포시 수정
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch (Exception e){
            throw new RuntimeException("접근허용 실패");
        }
    }

    //그룹 접속 권한 부여
    public void access_groups(String group, HttpServletResponse response) {
        try{
            String token = issue_group(group);
            Cookie cookie = new Cookie("group_token", token);
            cookie.setMaxAge(-1);
            cookie.setDomain(COOKIE_DOMAIN); // 배포시 수정
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch (Exception e){
            throw new RuntimeException("접근허용 실패");
        }
    }

    public void block(HttpServletResponse response) {
        try{
            Cookie cookie = new Cookie("token", "");
            cookie.setMaxAge(-1);
            cookie.setDomain(COOKIE_DOMAIN);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch(Exception e){
            throw new RuntimeException("토큰만료 실패");
        }
    }

    public void block_group(HttpServletResponse response) {
        try{
            Cookie cookie = new Cookie("group_token", "");
            cookie.setMaxAge(-1);
            cookie.setDomain(COOKIE_DOMAIN);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch(Exception e){
            throw new RuntimeException("토큰만료 실패");
        }
    }

    //단기 토큰
    public String short_token(String id) {
        try{
            return Jwts.builder()
                    .signWith(key)
                    .claim("id", id) // 유저 아이디
                    .issuedAt(new Date())  //발행 날짜
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) //2분 유효
                    .compact();//키로 서명
        }catch (Exception e){
            throw new RuntimeException("단기토큰 발급실패");
        }
    }

    //토큰 발급
    public String issue(String id) {
        try{
            return Jwts.builder()
                    .claim("id", id) // 유저 아이디
                    .issuedAt(new Date())  //발행 날짜
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24L)) //하루 유효
                    .signWith(key).compact();//키로 서명
        }catch (Exception e){
            throw new RuntimeException("토큰발급실패");
        }
    }

    // 그룹 접속
    public String issue_group(String group) {
        try{
            return Jwts.builder()
                    .claim("group", group) // 유저 소속 그룹
                    .issuedAt(new Date())  //발행 날짜
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24L)) //하루 유효
                    .signWith(key).compact();//키로 서명
        }catch (Exception e){
            throw new RuntimeException("토큰발급실패");
        }
    }

    //토큰 검증
    public String validations(String token){
        try{
            Jwt<?, ?> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);

            Claims claims = (Claims) claimsJws.getPayload();
            return claims.get("id", String.class);
        }catch (Exception e){
            throw new RuntimeException("인증실패");
        }
    }

    // 그룹 토큰 검증
    public String validation_group(String token){
        try{
            Jwt<?, ?> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);

            Claims claims = (Claims) claimsJws.getPayload();
            return claims.get("group", String.class);
        }catch (Exception e){
            throw new RuntimeException("그룹에 접속 후 이용해주세요");
        }
    }


    // request 로부터 토큰 불러옴
    public String request_get_token(HttpServletRequest request) {
        try{
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
        }catch (Exception e){
            throw new RuntimeException("토큰 불러오기 실패");
        }
    }

    // request 로부터 그룹토큰 불러옴
    public String request_get_group_token(HttpServletRequest request) {
        try{
            Cookie[] cookies = request.getCookies();
            String token = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("group_token")) {
                        return token = cookie.getValue();
                    }
                }
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException("토큰 불러오기 실패");
        }
    }


}