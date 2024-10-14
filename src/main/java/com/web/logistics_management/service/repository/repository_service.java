package com.web.logistics_management.service.repository;

import com.web.logistics_management.service.item.item_interface;
import com.web.logistics_management.service.item.item_model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class repository_service {
    private final repository_interface crud;

    // 그룹 전체 조회
    public List<repository_model> selectByOrganization(String group){
        return crud.findByOrganization(group);
    }

    // 조회
    public List<repository_model> selectGroupLocation(String group, String location){
        return crud.findByOrganizationAndLocation(group,location);
    }

    // 수량 이상 조회
    public List<repository_model> selectQuantityUp(Integer quantity){
        return crud.findQuantityUp(quantity);
    }
    // 수량 이하 조회

    // 물품 코드 조회
    // 빈 로케이션 조회
    // 업데이트

    // 삽입
    public repository_model insert(repository_model re) {
        return crud.saveAndFlush(re);
    }

    // 삭제
    public void delete(String group, String code){
        crud.deleteByOrganizationAndCode(group,code);
    }
}

