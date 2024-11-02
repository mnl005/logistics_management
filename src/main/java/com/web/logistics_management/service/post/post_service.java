package com.web.logistics_management.service.post;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class post_service {
    private final post_interface crud;

    // 조건 조회
    public List<post_model> select_all(String organization) {
        try {
            return crud.selectByOrganization(organization);
        } catch (Exception e) {
            throw new RuntimeException("게시글 조회 실패");
        }
    }

    // 나의 게시글 조회
    public List<post_model> select_me(String organization, String id) {
        try {
            return crud.selectByOrganizationAndId(organization,id);
        } catch (Exception e) {
            throw new RuntimeException("게시글 조회 실패");
        }
    }

    //게시글 작성
    public post_model insert(post_model post_model) {
        try {
            return crud.saveAndFlush(post_model);
        } catch (Exception e) {
            throw new RuntimeException("게시글 작성 실패");
        }
    }

    //게시글 삭제
    public void delete(String organization, String id, String num) {
        try {
            crud.delete(organization,id, Integer.valueOf(num));
        } catch (Exception e) {
            throw new RuntimeException("게시글 삭제 실패");
        }
    }
}
