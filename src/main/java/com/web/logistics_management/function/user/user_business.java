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
        jwt_service.access("user1", response);


        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 불러오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);


        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            user_model user = me.get();
            String id = user.getId();

            // 보낼 데이터 형식 : user_info
            res.put("user_info", user_service.findById(id).get());

            // 완료
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


    // 기능 : 로그인 1단계
    // 받는 데이터 : 이메일
    // 보낼 데이터 : 인증코드
    // 과정 : 이메일 받아 해당 이메일로 딘기토큰 전송
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
        dto.setRes_data(res);
        dto.setMsg(req.getV1() + " 으로 인증코드를 발송했습니다");
        dto.setJs("none");

        return dto;
    }

    // 기능 : 로그인 2단계
    // 받는 데이터 : 인증코드
    // 보낼 데이터 : 인증완료 메시지, 유저정보
    // 과정 : 토큰 받아 인증 후 로그인처리
    public Dto<model, Object> login2(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();

        // 사용자가 입력한 토큰 토큰 가져오기
        Optional<String> token = jwt_service.validations(req.getV1());

        //인증 정보가 유효하다면
        if (token.isPresent()) {

            //이메일로 아이디 찾아 해당 아이디로 장기 토큰 발급
            user_model user = user_service.findByEmail(token.get()).get();
            String id = user.getId();
            jwt_service.access(id, response);

            // 보낼 데이터 형식 : user_info
            res.put("user_info", user);

            // 완료
            dto.setRes_data(res);
            dto.setMsg(id + " 님 환영합니다");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("이메일 인증 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 회원가입 1단계
    // 받는 데이터 : user_model
    // 보낼 데이터 : 이메일 코드전송 메시지
    // 과정 : 유저정보 임시저장 -> 중복 이메일 검증 -> 이메일로 단기토큰 발송
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
            dto.setJs("none");
        } else {
            // 단기 토큰 발급
            String token = jwt_service.short_token(user_ob.getId());
            //인증코드 이메일로 발송
            email_service.sendEmail(user_ob.getEmail(), "인증요청", token);

            // 완료
            dto.setMsg(user_ob.getEmail() + " 로 인증코드가 전송되었습니다");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 회원가입 2단계
    // 받는 데이터 : 인증코드
    // 보낼 데이터 : user_info
    // 과정 : 입력토큰 유효한지 확인 -> 유효하다면 -> DB에 회원정보 저장 -> 저장된 회원정보 클라이언트에 전달
    public Dto<model, Object> join2(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();


        // 입력한 토큰이 유효하다면
        if (jwt_service.validations(req.getV1()).isPresent()) {

            // 조직 정보 초기 셋팅
            user_ob.setOrganization(null);

            //유저 정보 저장
            user_model user = user_service.insert(user_ob);

            //로그인처리
            jwt_service.access(user.getId(), response);

            // 보낼 데이터 형식 : user_info
            res.put("user_info", user);

            // 완료
            dto.setRes_data(res);
            dto.setMsg(user.getId() + " 님의 회원가입을 환영합니다");
            dto.setJs("none");

        }
        // 입력한 토큰이 유효하지 않다면
        else {
            // 완료
            dto.setMsg(user_ob.getId() + " 님이 회원가입에 실패했습니다 다시 시도해 주세요");
            dto.setJs("none");
        }

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
        dto.setJs("none");
        return dto;
    }


    // 기능 : 유저 회원탈퇴
    // 받는 데이터 : 없음
    // 보낼 데이터 : 없음
    // 과정 : 인증정보 유효시 -> 회원탈퇴처리
    public Dto<Object, Object> user_delete(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            user_model user = me.get();
            String id = user.getId();

            // 완료
            user_service.delete(id);
            dto.setMsg("탈퇴완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("탈퇴실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : name, phone, profile 업데이트
    // 받는 데이터 : v1,v2
    // 보낼 데이터 : user_info
    // 과정 : v1이 null이 아닐때 -> 유저 정보의 v1필드를 v2로 (v1이 null이라면 이미지 필드를 v2로) 업데이트 -> 업데이트된 유저 정보 전송
    public Dto<model, Object> user_update(Dto<model, Object> dto, HttpServletRequest request) {
        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            user_model user = me.get();
            String id = user.getId();

            // 내보낼 데이터 형식 : user_info
            switch (req.getV1()) {
                case "profile" ->  // v1이 없을때 프로필 이미지를 v2로
                        res.put("user_info", user_service.update(id, "profile", req.getV2()));
                case "name" ->  // v1 이 name 일때 이름을 v2으로
                        res.put("user_info", user_service.update(id, "name", req.getV2()));
                case "phone" ->  // v1이 phone 일때 전화번호를 v2으로
                        res.put("user_info", user_service.update(id, "phone", req.getV2()));
            }

            // 데이터 보내기
            dto.setRes_data(res);
            dto.setMsg("내정보 수정완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {
            dto.setMsg("내정보 수정실패");
            dto.setJs("none");
        }
        return dto;
    }


}
