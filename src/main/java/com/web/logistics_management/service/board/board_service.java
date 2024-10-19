package com.web.logistics_management.service.board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class board_service {
    private final board_interface crud;

    // 조건 조회
    public List<board_model> selectById(String id) {
        try {
            return crud.selectById(id);
        } catch (Exception e) {
            throw new RuntimeException("게시글 조회 실패");
        }
    }

    //게시글 작성
    public board_model insert(board_model board_model) {
        try {
            return crud.saveAndFlush(board_model);
        } catch (Exception e) {
            throw new RuntimeException("게시글 작성 실패");
        }
    }

    //게시글 삭제
    public void delete(String id, String num) {
        try {
            crud.deleteByIdNum(id, Integer.valueOf(num));
        } catch (Exception e) {
            throw new RuntimeException("게시글 삭제 실패");
        }
    }
}
