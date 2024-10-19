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

    private final user_inter crud;



    @PersistenceContext
    private EntityManager entityManager;

    // 모두 조회
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
                case "organization" -> crud.findByOrganization(value);
                default -> null;
            };
        } catch (Exception e) {
            throw new RuntimeException("사용자 조회 실패");
        }
    }

    // 멤버 생성
    public user_model insert(user_model user) {
        try {
            Optional<user_model> existingUser = crud.findById(user.getId());
            Optional<user_model> existingEmail = crud.findByEmails(user.getEmail());
            if (existingUser.isPresent() || existingEmail.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 아이디 또는 이메일");
            }
            return crud.saveAndFlush(user);
        } catch (Exception e) {
            throw new RuntimeException("신규 유저 등록 실패");
        }

    }

    // ID로 멤버 갱신
    @Transactional
    public user_model update(String id, String field, String value) {
        try {
            Optional<user_model> existingMember = crud.findById(id);
            if (existingMember.isPresent()) {
                user_model user_to_update = existingMember.get();
                switch(field){
                    case "name":
                        user_to_update.setName(value);
                        break;
                    case "organization":
                        user_to_update.setOrganization(value);
                        break;
                    case "phone":
                        user_to_update.setPhone(value);
                        break;
                    case "profile":
                        user_to_update.setProfile(value);
                        break;
                    default:
                        break;
                }
                return crud.saveAndFlush(user_to_update);
            } else {
                throw new Error("해당 ID의 멤버가 없습니다: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보 업데이트 실패");
        }

    }



    // ID로 멤버 삭제
    @Transactional
    public void delete(String id) {
        try {
            crud.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("사용자 삭제 실패");
        }
    }

    public Optional<user_model> findById(String id){
        try {
            return crud.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("사용자 조회 실패");
        }
    }
    public Optional<user_model> findByEmail(String email){
        try {
            return crud.findByEmails(email);

        } catch (Exception e) {
            throw new RuntimeException("사용자 조회 실패");
        }
    }
    public Optional<user_model> check_Organization(String organization){
        try {
            return crud.check_Organization(organization);

        } catch (Exception e) {
            throw new RuntimeException("그룹 체크 실패");
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

