package com.web.logistics_management.service.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class user_service {

    private final user_interface crud;



    @PersistenceContext
    private EntityManager entityManager;

    // 전체 조회
    public List<user_model> select_all() {
        try {
            return crud.findAll();
        } catch (Exception e) {
            throw new RuntimeException("사용자 전체조회 실패");
        }
    }

    // 조건 조회
    public List<user_model> select(String field, String value) {
        try {
            return switch (field) {
                case "id" -> crud.findByIdAll(value);
                case "email" -> crud.findByEmail(value);
                case "phone" -> crud.findByPhone(value);
                case "name" -> crud.findByName(value);
                default -> null;
            };
        } catch (Exception e) {
            throw new RuntimeException("사용자 조회 실패");
        }
    }

    // 멤버 생성
    public user_model insert(user_model user) {
        try {
            return crud.saveAndFlush(user);
        } catch (Exception e) {
            throw new RuntimeException("이미 존재하는 아이디 또는 이메일");
        }
    }

    // 수정
    @Transactional
    public user_model update(String id, String field, String value) {
        try {
            Optional<user_model> op_user = crud.findById(id);
            if (op_user.isPresent()) {
                user_model user = op_user.get();
                switch(field){
                    case "name":
                        user.setName(value);
                        break;
                    case "phone":
                        user.setPhone(value);
                        break;
                    case "profile":
                        user.setProfile(value);
                        break;
                    default:
                        break;
                }
                return crud.saveAndFlush(user);
            } else {
                throw new Error("해당 ID의 멤버가 없습니다: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보 업데이트 실패");
        }

    }

    // 삭제
    @Transactional
    public void delete(String id) {
        try {
            crud.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("사용자 삭제 실패");
        }
    }


    //--------------------------------------------------------------------------------------------------

    // 인증시 사용
    public Optional<user_model> Op_id(String id){
        try {
            return crud.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("사용자 조회 실패");
        }
    }


    //아이디 또는 이메일 중복검증
    public Optional<user_model> check_id_email(String id, String email) {
        try {
            return crud.check_id_email(id,email);
        } catch (Exception e) {
            throw new RuntimeException("아이디와 이메일 검증 실패");
        }
    }



}

