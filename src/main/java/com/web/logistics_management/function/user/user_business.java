package com.web.logistics_management.function.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.user.user_model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

import com.web.logistics_management.service.user.user_service;
import com.web.logistics_management.service.jwt_service;
import com.web.logistics_management.service.email_service;


@RequiredArgsConstructor
@Component
public class user_business {

    private final user_service user_service;
    private final jwt_service jwt_service;
    private final email_service email_service;

    //회원가입시 유저정보 임시저장
    private user_model user_ob = new user_model();


    private final ObjectMapper mapper = new ObjectMapper();


    // 기능 : 유저 정보 불러오기
    // 받는 데이터 : 없음
    // 보낼 데이터 : user_info
    public Dto<Object, Object> user_info(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        //테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증테스트용임시인증
//        jwt_service.access("mnl005", response);

        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // jwt 인증
        //user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).get();
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 보낼 데이터 형식 : user_info
        res.put("user_info", me);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("내 정보 조회");

        return dto;
    }


    // 기능 : 로그인 1단계
    // 받는 데이터 : v1(이메일)
    // 보낼 데이터 : 인증코드
    public Dto<model, Object> login1(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();

        // 단기 토큰 발급
        String token = jwt_service.short_token(req.getV1());

        //인증코드 이메일로 발송
        email_service.sendEmail(req.getV1(), "로그인 인증요청", token);


        // 완료
        dto.setMsg(req.getV1() + " 으로 인증코드를 발송했습니다");

        return dto;
    }

    // 기능 : 로그인 2단계
    // 받는 데이터 : v1(인증코드)
    // 보낼 데이터 : 인증완료 메시지, 유저정보
    public Dto<model, Object> login2(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();

        // 사용자가 입력한 토큰 토큰 가져오기
        String token = req.getV1();

        // 토큰인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 인증 정보로 장기 토큰 발급
        String id = me.getId();
        jwt_service.access(id, response);

        // 보낼 데이터 형식 : user_info
        res.put("user_info", me);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(id + " 님 환영합니다");

        return dto;
    }

    // 기능 : 회원가입 1단계
    // 받는 데이터 : user_model(id,email,phone,name,profile)
    // 보낼 데이터 : 이메일 코드전송 메시지
    public Dto<user_model, Object> join1(Dto<user_model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        user_model req = dto.getReq_data();
        //유저 정보 임시 저장
        user_ob = req;

        // 중복되는 아이디나 이메일이 있다면
        if (user_service.check_id_email(user_ob.getId(), user_ob.getEmail()).isPresent()) {
            // 완료
            dto.setMsg("아이디 또는 이메일이 중복됩니다");
        } else {
            // 단기 토큰 발급
            String token = jwt_service.short_token(user_ob.getId());

            //인증코드 이메일로 발송
            email_service.sendEmail(user_ob.getEmail(), "인증요청", token);

            // 완료
            dto.setMsg(user_ob.getEmail() + " 로 인증코드가 전송되었습니다");
        }

        return dto;
    }

    // 기능 : 회원가입 2단계
    // 받는 데이터 : v1(인증코드)
    // 보낼 데이터 : user_info
    public Dto<model, Object> join2(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 입력한 토큰
        String token = req.getV1();
        // 입력한 토큰 검증
        String id = jwt_service.validations(token);


        // 유저 정보 초기 셋팅
        user_ob.setOrganization(null);
        user_ob.setId(id);
        //유저 정보 저장
        user_model user = user_service.insert(user_ob);
        //로그인처리
        jwt_service.access(id, response);

        // 보낼 데이터 형식 : user_info
        res.put("user_info", user);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(user.getId() + " 님의 회원가입을 환영합니다");

        return dto;
    }

    // 기능 :  로그아웃
    // 받는 데이터 : 없음
    // 보낼 데이터 : 없음
    public Dto<Object, Object> logout(Dto<Object, Object> dto, HttpServletResponse response) {

        // 토큰만료
        jwt_service.block(response);

        //완료
        dto.setMsg("로그아웃되었습니다");
        dto.setRedirect("/");
        return dto;
    }


    // 기능 : 유저 회원탈퇴
    // 받는 데이터 : 없음
    // 보낼 데이터 : 없음
    public Dto<Object, Object> user_delete(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 아이디
        String id = me.getId();
        // 사용자 탈퇴
        user_service.delete(id);

        // 완료
        dto.setMsg("탈퇴완료");
        dto.setRedirect("/");
        return dto;
    }

    // 기능 : name, phone, profile 업데이트
    // 받는 데이터 : v1(profile or name or phone),v2(이름 or 전화번호), image(프로필이미지)
    // 보낼 데이터 : user_info
    public Dto<model, Object> user_update(Dto<model, Object> dto, HttpServletRequest request) {
        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 아이디
        String id = me.getId();

        // 유저 정보 수정
        // 내보낼 데이터 형식 : user_info
        switch (req.getV1()) {
            case "profile" ->  // v1이 profile 일때 프로필 이미지를 image로
                    res.put("user_info", user_service.update(id, "profile", req.getImage()));
            case "name" ->  // v1 이 name 일때 이름을 v2으로
                    res.put("user_info", user_service.update(id, "name", req.getV2()));
            case "phone" ->  // v1이 phone 일때 전화번호를 v2으로
                    res.put("user_info", user_service.update(id, "phone", req.getV2()));
        }

        // 완료
        dto.setRes_data(res);
        dto.setMsg("내정보 수정완료");

        return dto;
    }


}
