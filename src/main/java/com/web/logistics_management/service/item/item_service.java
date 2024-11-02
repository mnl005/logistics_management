package com.web.logistics_management.service.item;


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
    public List<HashMap<String, String>> select_all(String organization) {
        try {
            // organization이 해당 그룹인 데이터를 조회
            List<item_model> items = crud.selectByOrgnaization(organization);
            // 결과를 저장할 리스트
            List<HashMap<String, String>> organizationAndCodes = new ArrayList<>();
            // 조회된 리스트에서 organization과 code를 개별적으로 추출하여 HashMap에 저장
            for (item_model item : items) {
                HashMap<String, String> map = new HashMap<>();
                map.put("organization", item.getId().getOrganization());
                map.put("item_code", item.getId().getItem_code());
                map.put("name",item.getName());
                map.put("other",item.getOther());
                map.put("image",item.getImage());
                organizationAndCodes.add(map);
            }
            return organizationAndCodes;
        } catch (Exception e) {
            throw new RuntimeException("조회 결과 없음");
        }
    }

    // 일부 조회
    public HashMap<String, String> select(String organization, String item_code){
        try {
            item_model item =  crud.findByOrganizationAndCode(organization,item_code);
            HashMap<String,String> map = new HashMap<>();
            map.put("organization", item.getId().getOrganization());
            map.put("item_code", item.getId().getItem_code());
            map.put("name",item.getName());
            map.put("other",item.getOther());
            map.put("image",item.getImage());
            return map;
        } catch (Exception e) {
            throw new RuntimeException("해당 상품이 존재하지 않습니다");
        }
    }

    // 삽입
    public item_model insert(item_model item) {
        try {
            return crud.saveAndFlush(item);
        } catch (Exception e) {
            throw new RuntimeException("상품 등록 실패");
        }
    }

    // 삭제
    public void delete(String organization, String item_code){
        try {
            crud.deleteByOrganizationAndCode(organization,item_code);
        } catch (Exception e) {
            throw new RuntimeException("상품 삭제 실패");
        }

    }
}
