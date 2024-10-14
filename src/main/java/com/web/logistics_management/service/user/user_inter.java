package com.web.logistics_management.service.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface user_inter extends JpaRepository<user_model, String> {


    @Query("SELECT m FROM user m WHERE m.id = :id")
    List<user_model> findByIdAll(String id);
    List<user_model> findByName(String name);
    List<user_model> findByEmail(String email);
    List<user_model> findByPhone(String phone);
    List<user_model> findByOrganization(String value);

    @Query("SELECT m FROM user m WHERE m.email = :email")
    Optional<user_model> findByEmails(String email);

    @Query("SELECT m FROM user m WHERE m.organization = :organization")
    Optional<user_model> check_Organization(String organization);

    @Query("SELECT m FROM user m WHERE m.id = :id OR m.email = :email")
    Optional<user_model> check_id_email(@Param("id") String id, @Param("email") String email);


}
