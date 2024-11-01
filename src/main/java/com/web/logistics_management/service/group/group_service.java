package com.web.logistics_management.service.group;



import com.web.logistics_management.service.user.user_model;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class group_service {
    private final group_interface crud;

    // 조건 조회
    public List<group_model> select(String field, String value) {
        try {
            return switch (field) {
                case "organization" -> crud.findByOrganization(value);
                case "master" -> crud.findByMaster(value);
                default -> null;
            };
        } catch (Exception e) {
            throw new RuntimeException("그룹 정보 조회 실패");
        }
    }

    // 그룹 유효성 검증
    public Optional<group_model> Op_group(String group, String master) {
        return crud.findByOrganizationAndMaster(group,master);
    }

    // 그룹 생성
    public group_model insert(group_model group) {
        try {
            return crud.saveAndFlush(group);
        } catch (Exception e) {
            throw new RuntimeException("이미 존재하는 그룹입니다");
        }
    }

    // 그룹 수정 불가

    // 삭제
    @Transactional
    public void delete(String organization, String master) {
        try {
                crud.deleteByOrganizationAndMaster(organization, master);
        } catch (Exception e) {
            throw new RuntimeException("그룹 삭제 권한이 없습니다");
        }
    }































}
