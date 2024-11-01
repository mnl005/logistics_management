package com.web.logistics_management.service.location;

import com.web.logistics_management.service.item.item_model;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class location_service {
    private final location_interface crud;

    // 그룹 전체 로케이션 조회
    public List<HashMap<String, String>> select_all(String organization) {
        try {
            List<location_model> locations = crud.findByOrgnaization(organization);
            List<HashMap<String, String>> result = new ArrayList<>();

            for (location_model location : locations) {
                HashMap<String, String> map = new HashMap<>();
                map.put("organization", location.getId().getOrganization());
                map.put("location_code", location.getId().getLocation_code());
                result.add(map);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("로케이션 전체 조회 실패");
        }
    }


    // 삽입
    public location_model insert(location_model location) {
        try {
            return crud.saveAndFlush(location);
        } catch (Exception e) {
            throw new RuntimeException("로케이션 등록 실패");
        }
    }

    // 삭제
    public void delete(String organization, String location_code){
        try {
            crud.deleteByOrganizationAndLocation_code(organization,location_code);
        } catch (Exception e) {
            throw new RuntimeException(location_code + " 로케이션 정보가 존재하지 않습니다");
        }

    }


}

