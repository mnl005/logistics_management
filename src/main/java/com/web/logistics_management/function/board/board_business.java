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
        // jwt 토큰 검증 및 유저 정보 불러오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);


        // 유저 정보 유효시
        if (me.isPresent()) {

            // 유저의 소속 그룹명 불러오기
            String group_name = me.get().getOrganization();

            // 유저가 그룹에 소속되지 않은 경우 ???

            // 그룹에 속한 유저들의 아이디 리스트 조회
            List<String> id_list = user_service.select("organization", group_name)
                    .stream()
                    .map(user_model::getId)
                    .toList();

            // 각 유저 아이디로 게시판 정보를 조회하여 리스트에 저장
            List<board_model> boardList = id_list
                    .stream()
                    .map(board_service::selectById)
                    .flatMap(List::stream)
                    .toList();

            // 보낼 데이터 형식 : board_list
            res.put("board_list", boardList);
            // 그룹 게시글의 수량 조회
            String size = String.valueOf(boardList.size());

            //완료
            dto.setRes_data(res);
            dto.setMsg(size + " 개의 게시글이 조회되었습니다");
        }
        else {
            // 완료
            dto.setMsg("인증실패");
        }

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
        // jwt 토큰 검증 및 유저 정보 불러오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 유저 인증 정보가 유효시
        if(me.isPresent()){

            // 유저의 아이디 불러오기
            String id = me.get().getId();

            // 유저의 게시글 불러오기
            List<board_model> list = board_service.selectById(id);

            // 게시글 사이즈 조회
            String size = String.valueOf(list.size());

            // 데이터 형식 : board_list
            res.put("board_list", list);

            // 완료
            dto.setRes_data(res);
            dto.setMsg(size + " 개의 게시글이 조회되었습니다");
        }
        // 유저 인증 정보 유효하지 않을때
        else{
            //완료
            dto.setMsg("인증실패");
        }

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
        // jwt 토큰 검증 및 유저 정보 불러오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 사용자 인증 정보가 유효하다면
        if (me.isPresent()) {
            // 사용자 아이디 확인
            String id = me.get().getId();
            // 게시글 등록자를 사용자의 아이디로 등록
            req.setId(id);
            // 게시글 삽입
            board_service.insert(req);

            // 완료
            dto.setMsg("게시글 작성 성공");
        }
        // 사용자 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("게시글 작성 실패");
        }
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
        // jwt 토큰 검증 및 유저 정보 불러오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 사용자 인증 정보가 유효하다면
        if (me.isPresent()) {
            // 사용자의 아이디 조회
            String id = me.get().getId();
            // 사용자의 아이디와 게시글 고유키로 게시글 삭제
            board_service.delete(id, board_id);

            // 완료
            dto.setMsg(board_id + " 번 게시글이 삭제 되었습니다");
        }
        // 사용자 인증 정보가 유효하지 않다면
        else {
            dto.setMsg("인증실패");
        }

        return dto;
    }
}
