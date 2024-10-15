package com.web.logistics_management.service.item;

import com.web.logistics_management.service.inviter.invite_model;
import com.web.logistics_management.service.item.item_model;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface item_interface extends JpaRepository<item_model, String> {


    @Query("SELECT m FROM item m WHERE m.id.organization = :organization AND m.id.code = :code")
    item_model findByOrganizationAndCode(@Param("organization") String organization, @Param("code") String code);

    @Query("SELECT m FROM item m WHERE m.id.organization = :organization")
    List<item_model> selectByOrgnaization(@Param("organization") String organization);

    @Modifying
    @Transactional
    @Query("DELETE FROM item m WHERE m.id.organization = :organization AND m.id.code = :code")
    void deleteByOrganizationAndCode(@Param("organization") String organization, @Param("code") String code);

}