package com.web.logistics_management.service.inviter;

import jakarta.transaction.Transactional;

import java.util.List;

import com.web.logistics_management.service.inviter.invite_interface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class invite_service {

    private final invite_interface crud;

    // 목록 조회
    public List<invite_model> select(String field, String value) {
        try {
            return switch (field) {
                case "inviter" -> crud.findByInviter(value);
                case "target" -> crud.findByTarget(value);
                case "num" -> crud.findByNum(Integer.valueOf(value));
                default -> null;
            };
        } catch (Exception e) {
            throw new RuntimeException("초대 내역 조회 실패");
        }
    }

    // 목록 생성
    public invite_model insert(String inviter, String target) {
        List<invite_model> model = crud.findByInviterAndTarget(inviter, target);
        invite_model save = new invite_model();
        save.setInviter(inviter);
        save.setTarget(target);
        if (model.isEmpty()) {
            return crud.saveAndFlush(save);
        } else {
            throw new RuntimeException("이미 초대를 보냈습니다");
        }
    }

    // 목록 삭제
    @Transactional
    public void delete(String num) {
        try {
            crud.deleteByNum(Integer.valueOf(num));
        } catch (Exception e) {
            throw new RuntimeException("초대 내역 삭제 실패");
        }
    }
}
