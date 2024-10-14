package com.web.logistics_management.service.item;


import com.web.logistics_management.service.board.board_interface;
import com.web.logistics_management.service.board.board_model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class item_service {
    private final item_interface crud;

    // 조회
    public List<item_model> selectGroupCode(String group, String code){
        return crud.findByOrganizationAndCode(group,code);
    }

    // 삽입
    public item_model insert(item_model item) {
        return crud.saveAndFlush(item);
    }

    // 삭제
    public void deleteGroupCode(String group, String code){
        crud.deleteByOrganizationAndCode(group,code);
    }
}
