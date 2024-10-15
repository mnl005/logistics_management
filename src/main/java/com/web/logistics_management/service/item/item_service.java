package com.web.logistics_management.service.item;


import com.web.logistics_management.service.board.board_interface;
import com.web.logistics_management.service.board.board_model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class item_service {
    private final item_interface crud;

    // 전체 조회
    public List<HashMap<String, String>> selectGroup(String group) {
        // organization이 해당 그룹인 데이터를 조회
        List<item_model> items = crud.selectByOrgnaization(group);

        // 결과를 저장할 리스트
        List<HashMap<String, String>> organizationAndCodes = new ArrayList<>();

        // 조회된 리스트에서 organization과 code를 개별적으로 추출하여 HashMap에 저장
        for (item_model item : items) {
            HashMap<String, String> map = new HashMap<>();
            map.put("organization", item.getId().getOrganization());
            map.put("code", item.getId().getCode());
            map.put("name",item.getName());
            map.put("image",item.getImage());
            organizationAndCodes.add(map);
        }

        return organizationAndCodes;
    }

    // 일부 조회
    public HashMap<String, String> selectGroupCode(String group, String code){
        item_model item =  crud.findByOrganizationAndCode(group,code);
        HashMap<String,String> map = new HashMap<>();
        map.put("organization", item.getId().getOrganization());
        map.put("code", item.getId().getCode());
        map.put("name",item.getName());
        map.put("image",item.getImage());
        return map;
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
