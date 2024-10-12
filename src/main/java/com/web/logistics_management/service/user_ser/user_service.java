package com.web.logistics_management.service.user_ser;

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
        return crud.findAll();
    }

    // 조건 조회
    public List<user_model> select(String field, String value) {
//        System.out.println(passwordEncoder.encode(value));
        return switch (field) {
            case "id" -> crud.findByIdAll(value);
            case "email" -> crud.findByEmail(value);
            case "phone" -> crud.findByPhone(value);
            case "name" -> crud.findByName(value);
            case "organization" -> crud.findByOrganization(value);
            default -> null;
        };
    }

    // 멤버 생성
    public user_model insert(user_model user) {
        Optional<user_model> existingUser = crud.findById(user.getId());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디");
        }
        return crud.saveAndFlush(user);
    }

    // ID로 멤버 갱신
    @Transactional
    public user_model update(String id, String field, String value) {
        Optional<user_model> existingMember = crud.findById(id);
        System.out.println(id);
        System.out.println(field);
        System.out.println(value);

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
    }



    // ID로 멤버 삭제
    @Transactional
    public void delete(String id) {
        crud.deleteById(id);
    }

    public Optional<user_model> findById(String id){
        return crud.findById(id);
    }
    public Optional<user_model> findByEmail(String email){
        return crud.findByEmails(email);
    }
    public Optional<user_model> check_Organization(String organization){
        return crud.check_Organization(organization);
    }


    //아이디 또는 이메일 중복검증
    public Optional<user_model> check_id_email(String id, String email) {
        return crud.check_id_email(id,email);
    }



}

