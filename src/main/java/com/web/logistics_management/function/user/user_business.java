package com.web.logistics_management.function.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.service.user_ser.user_model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

import com.web.logistics_management.service.user_ser.user_service;
import com.web.logistics_management.service.jwt_service;


@RequiredArgsConstructor
@Component
public class user_business {

    private final user_service user_service;
    private final jwt_service jwt_service;
//    private final Email_service email_service;

    //회원가입시 유저정보 임시저장
    private user_model user_ob = new user_model();


    private final ObjectMapper mapper = new ObjectMapper();


    // 기능 : 유저 정보 불러오기
    // 받는 데이터 : 없음
    // 보낼 데이터 : user_info
    public Dto<Object, Object> user_info(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {


        //테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증
        jwt_service.access("user1", response);


        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        dto.setMsg("테스트완료");
        // jwt 토큰 검증 및 유저 정보 불러오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);


        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            user_model user = me.get();
            String id = user.getId();

            // 보낼 데이터 형식 : user_info
            res.put("user_info", user_service.findById(id).get());

            // 데이터 보내기
            dto.setRes_data(res);
            dto.setMsg("내 정보 조회");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {
            dto.setMsg("인증실패");
            dto.setJs("none");
        }
        return dto;
    }

}
