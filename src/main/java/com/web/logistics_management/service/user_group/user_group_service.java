package com.web.logistics_management.service.user_group;

import com.web.logistics_management.service.user.user_model;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class user_group_service {
    private final user_group_interface crud;


    public Optional<user_organization_model> OpIdOrganization(String id,String organization) {
        try {
            return crud.OpIdOrganization(id,organization);
        } catch (Exception e) {
            throw new RuntimeException("그룹 인증 오류");
        }
    }

    // 아이디로 조회
    public List<user_organization_model> select_id(String id) {
        try {
            return crud.findByIdAll(id);
        } catch (Exception e) {
            throw new RuntimeException("소속 그룹 찾는 중 에러발생");
        }
    }


    // 그룹 이름으로 조회
    public List<user_organization_model> select_organization(String organization) {
        try {
            return crud.findbyOrganizationAll(organization);
        } catch (Exception e) {
            throw new RuntimeException("소속 그룹 찾는 중 에러발생");
        }
    }

    // 아이디와 그룹 이름으로 조회
    public user_organization_model select(String id, String organization) {
        try {
            return crud.findByidAndOrganization(id,organization);
        } catch (Exception e) {
            throw new RuntimeException("소속 그룹 찾는 중 에러발생");
        }
    }

    // 그룹 들어가기
    public user_organization_model insert(user_organization_model model) {
        try {
            return crud.saveAndFlush(model);
        } catch (Exception e) {
            throw new RuntimeException("이미 해당 그룹에 소속되어 있습니다");
        }
    }

    // 그룹 탈퇴
    // 삭제
    @Transactional
    public void delete(String id, String organization) {
        try {
            crud.deleteByOrganizationAndMaster(id,organization);
        } catch (Exception e) {
            throw new RuntimeException("그룹 탈퇴 실패");
        }
    }

}
