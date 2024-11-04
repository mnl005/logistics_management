package com.web.logistics_management.function.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.user.user_model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class user_controller {

    private final ObjectMapper mapper = new ObjectMapper();
    private final user_business user_business;

    // 유저 정보 확인
    @PostMapping("/refresh")
    public ResponseEntity<JsonNode> refresh(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.refresh(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 유저 정보 확인
    @PostMapping("/me")
    public ResponseEntity<JsonNode> me(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.user_info(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 로그인 1단계
    //v1 : 이메일
    @PostMapping("/login1")
    public ResponseEntity<JsonNode> login1(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.login1(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 로그인 2단계
    //v1 : 인증코드
    @PostMapping("/login2")
    public ResponseEntity<JsonNode> login2(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.login2(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 회원가입 1단계
    // user_mode : 등록할 회원 정보
    @PostMapping("/join1")
    public ResponseEntity<JsonNode> join1(@RequestBody Dto<user_model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.join1(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 회원가입 2단계
    //v1 : 인증코드
    @PostMapping("/join2")
    public ResponseEntity<JsonNode> join2(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.join2(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<JsonNode> logout(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.logout(dto,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }


    // 회원탈퇴
    @PostMapping("/delete")
    public ResponseEntity<JsonNode> delete(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.user_delete(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 회원정보 업데이트
    @PostMapping("/update")
    public ResponseEntity<JsonNode> update(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.user_update(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

}
