package com.web.logistics_management.service.repository;

import com.web.logistics_management.service.item.item_interface;
import com.web.logistics_management.service.item.item_model;
import com.web.logistics_management.service.user.user_model;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class repository_service {
    private final repository_interface crud;

    // 그룹 전체 조회
    public List<HashMap<String, String>> selectByOrganization(String group) {

        List<repository_model> list = crud.findByOrganization(group);
        List<HashMap<String, String>> lmap = new ArrayList<>();

        for (repository_model re : list) {
            lmap.add(re.toMap());
        }
        return lmap;
    }

    // 조회
    public HashMap<String, String> selectGroupLocation(String group, String location) {
        return crud.findByOrganizationAndLocation(group, location).toMap();
    }

    // 수량 이상 조회
    public List<HashMap<String, String>> selectQuantityUp(String group,Integer quantity) {
        List<HashMap<String, String>> lmap = new ArrayList<>();
        List<repository_model> list = crud.findQuantityUp(group, quantity);
        for (repository_model re : list) {
            lmap.add(re.toMap());
        }
        return lmap;
    }

    // 수량 이하 조회
    public List<HashMap<String, String>> selectQuantityDown(String group, Integer quantity) {
        List<HashMap<String, String>> lmap = new ArrayList<>();
        List<repository_model> list = crud.findQuantityDown(group, quantity);
        for (repository_model re : list) {
            lmap.add(re.toMap());
        }
        return lmap;
    }

    // 빈 로케이션 조회
    public List<HashMap<String, String>> selectQuantityZero(String group) {
        List<HashMap<String, String>> lmap = new ArrayList<>();
        List<repository_model> list = crud.findQuantityZero(group);
        for (repository_model re : list) {
            lmap.add(re.toMap());
        }
        return lmap;
    }

    // 물품 코드 조회
    public List<HashMap<String, String>> selectCode(String group, String code) {
        List<HashMap<String, String>> lmap = new ArrayList<>();
        List<repository_model> list = crud.findByCode(group, code);
        for (repository_model re : list) {
            lmap.add(re.toMap());
        }
        return lmap;
    }

    // 업데이트
    @Transactional
    public repository_model update(String group, String location, String field, String value) {
        repository_model repository_update = crud.findByOrganizationAndLocation(group, location);

        switch (field) {
            case "code":
                repository_update.setCode(value);
                break;
            case "quantity":
                repository_update.setQuantity(Integer.valueOf(value));
                break;
            default:
                break;
        }
        return crud.saveAndFlush(repository_update);

    }

    // 삽입
    public HashMap<String, String> insert(repository_model re) {
        return crud.saveAndFlush(re).toMap();
    }

    // 삭제
    public void delete(String group, String location) {
        crud.deleteByOrganizationAndCode(group, location);
    }
}

