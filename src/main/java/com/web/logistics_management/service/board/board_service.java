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
        return crud.selectById(id);
    }

    //게시글 작성
    public board_model insert(board_model board_model) {
        return crud.saveAndFlush(board_model);
    }

    //게시글 삭제
    public void delete(String id, String num) {
        crud.deleteByIdNum(id, Integer.valueOf(num));
    }
}
