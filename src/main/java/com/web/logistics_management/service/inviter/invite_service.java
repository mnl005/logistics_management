package com.web.logistics_management.service.inviter;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import com.web.logistics_management.service.inviter.invite_interface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class invite_service {

    private final invite_interface crud;

    // 초대 조회
    public List<invite_model> select(String field, String value) {
        try {
            return switch (field) {
                case "master" -> crud.findByMaster(value);
                case "target" -> crud.findByTarget(value);
                case "num" -> crud.findByNum(Integer.valueOf(value));
                default -> null;
            };
        } catch (Exception e) {
            throw new RuntimeException("초대 내역 조회 실패");
        }
    }


    // 초대 하거나 받은 리스트 조회
    public List<invite_model> masterOrtarget(String master, String target) {
        try {
            return crud.masterOrtarget(master, target);


        } catch (Exception e) {
            throw new RuntimeException("조회 실패");
        }
    }

    // 초대 생성
    public invite_model insert(invite_model model) {
        try{
            return crud.saveAndFlush(model);
        }catch (Exception e) {
            throw new RuntimeException("이미 초대를 보냈습니다");
        }
    }

    // 초대 삭제
    @Transactional
    public void delete(String num) {
        try {
            crud.deleteByNum(Integer.valueOf(num));
        } catch (Exception e) {
            throw new RuntimeException("초대 내역 삭제 실패");
        }
    }
}
