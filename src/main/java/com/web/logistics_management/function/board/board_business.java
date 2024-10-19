package com.web.logistics_management.function.board;


import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.board.board_model;
import com.web.logistics_management.service.user.user_model;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.web.logistics_management.service.jwt_service;
import com.web.logistics_management.service.board.board_service;
import com.web.logistics_management.service.user.user_service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class board_business {
    private final jwt_service jwt_service;
    private final board_service board_service;
    private final user_service user_service;


    // 기능 : 그룹의 게시물 불러오기
    // 받는 데이터 : 없음
    // 보낼 데이터 : board_list(num,id,created_date,title,content,image)
    public Dto<Object, Object> group(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 임시
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 그룹이름
        String group = me.getOrganization();

        // 그룹 구성원들 아이디
        List<String> id_list = user_service.select("organization", group)
                .stream()
                .map(user_model::getId)
                .toList();

        // 그룹 구성원들 게시판 리스트
        List<board_model> boardList = id_list
                .stream()
                .map(board_service::selectById)
                .flatMap(List::stream)
                .toList();

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
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자 아이디
        String id = me.getId();

        // 사용자 게시글
        List<board_model> list = board_service.selectById(id);

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
    // 받는 데이터 : board_model(num,id,title,create_date,content,image)
    // 보낼 데이터 : 없음
    public Dto<board_model, Object> insert(Dto<board_model, Object> dto, HttpServletRequest request) {
        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        board_model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 아이디
        String id = me.getId();
        // 게시글 등록자를 사용자의 아이디로 강제설정
        req.setId(id);
        // 게시글 삽입
        board_service.insert(req);

        // 완료
        dto.setMsg("게시글 작성 성공");

        return dto;
    }

    // 기능 : 게시글 삭제
    // 받는 데이터 : id_data(게시글 분류넘버)
    // 보낼 데이터 : 없음
    public Dto<model, Object> delete(Dto<model, Object> dto, HttpServletRequest request) {
        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 삭제할 게시글의 분류번호
        String board_id = dto.getId_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 아이디 조회
        String id = me.getId();
        // 사용자의 아이디와 게시글 고유키로 게시글 삭제
        board_service.delete(id, board_id);

        // 완료
        dto.setMsg("게시글이 삭제 되었습니다");


        return dto;
    }
}
