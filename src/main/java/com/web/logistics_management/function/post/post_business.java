package com.web.logistics_management.function.post;


import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.service.post.post_model;
import com.web.logistics_management.service.user.user_model;
import com.web.logistics_management.service.user_group.user_group_service;
import com.web.logistics_management.service.user_group.user_organization_model;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.web.logistics_management.service.jwt_service;
import com.web.logistics_management.service.post.post_service;
import com.web.logistics_management.service.user.user_service;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class post_business {
    private final jwt_service jwt_service;
    private final user_group_service user_group_service;
    private final post_service post_service;
    private final user_service user_service;


    // 기능 : 그룹의 게시물 불러오기
    // 받는 데이터 : 없음
    // 보낼 데이터 : board_list(id,created_date,title,content,image)
    public Dto<Object, Object> group(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        String me_group = connect.getId().getOrganization();


        // 그룹 구성원들 게시판 리스트
        List<post_model> boardList = post_service.select_all(me_group);

        // 보낼 데이터 형식 : board_list
        res.put("board_list", boardList);
        // 그룹 게시글의 수량
        String size = String.valueOf(boardList.size());

        //완료
        dto.setRes_data(res);
        dto.setMsg(size + " 개의 게시글이 조회되었습니다");


        return dto;
    }


    // 기능 : 유저의 게시물 불러오기
    // 받는 데이터 : 없음
    // 보낼 데이터 : board_list(num,id,created_date,title,content,image)
    public Dto<Object, Object> me(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        String me_group = connect.getId().getOrganization();

        // 사용자 게시글
        List<post_model> list = post_service.select_me(me_group,id);

        // 게시글 사이즈
        String size = String.valueOf(list.size());

        // 데이터 형식 : board_list
        res.put("board_list", list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(size + " 개의 게시글이 조회되었습니다");


        return dto;
    }

    // 기능 : 게시글 등록
    // 받는 데이터 : board_model(title,content,image)
    // 보낼 데이터 : board_list(num,id,created_date,title,content,image)
    public Dto<post_model, Object> insert(Dto<post_model, Object> dto, HttpServletRequest request) {
        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        post_model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        String me_group = connect.getId().getOrganization();


        // 게시글 등록자를 사용자의 아이디로 설정
        req.setId(id);
        req.setOrganization(me_group);
        // 게시글 삽입
        post_service.insert(req);

        // 사용자 게시글
        List<post_model> list = post_service.select_me(me_group,id);

        // 보낼 데이터 형식 board_list
        res.put("board_list",list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("게시글 작성 성공");

        return dto;
    }

    // 기능 : 게시글 삭제
    // 받는 데이터 : id_data(게시글 분류넘버)
    // 보낼 데이터 : board_list(num,id,created_date,title,content,image)
    public Dto<Object, Object> delete(Dto<Object, Object> dto, HttpServletRequest request) {
        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 삭제할 게시글의 분류번호
        String board_id = dto.getId_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        String me_group = connect.getId().getOrganization();


        // 사용자의 아이디와 게시글 고유키로 게시글 삭제
        post_service.delete(me_group,id, board_id);

        // 사용자 게시글
        List<post_model> list = post_service.select_me(me_group,id);

        // 보낼 데이터 형식 : board_list
        res.put("board_list",list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("게시글이 삭제 되었습니다");


        return dto;
    }
}
