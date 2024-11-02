package com.web.logistics_management.service.post;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface post_interface extends JpaRepository<post_model, String> {


    @Query("SELECT m FROM post m WHERE m.organization = :organization")
    List<post_model> selectByOrganization(@Param("organization") String organization);


    @Query("SELECT m FROM post m WHERE m.organization = :organization and m.id = :id" )
    List<post_model> selectByOrganizationAndId(@Param("organization") String organization, @Param("id") String id);


    @Modifying
    @Transactional
    @Query("DELETE FROM post m WHERE m.organization = :organization and m.id = :id and m.num = :num")
    void delete(@Param("organization") String organization, @Param("id") String id, @Param("num") Integer num);
}
