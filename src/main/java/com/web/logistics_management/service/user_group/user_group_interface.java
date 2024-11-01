package com.web.logistics_management.service.user_group;


import com.web.logistics_management.service.user.user_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface user_group_interface extends JpaRepository<user_organization_model, String> {
    @Query("SELECT m FROM user_organization m WHERE m.id.id = :id")
    List<user_organization_model> findByIdAll(String id);

    @Query("SELECT m FROM user_organization m WHERE m.id.organization = :organization")
    List<user_organization_model> findbyOrganizationAll(String organization);

    @Query("SELECT m FROM user_organization m WHERE m.id.id = :id AND m.id.organization = :organization")
    user_organization_model findByidAndOrganization(String id, String organization);

    @Query("SELECT m FROM user_organization m WHERE m.id.id = :id AND m.id.organization = :organization")
    Optional<user_organization_model> OpIdOrganization(String id, String organization);

    @Modifying
    @Query("DELETE FROM user_organization m WHERE m.id.id = :id AND m.id.organization = :organization")
    void deleteByOrganizationAndMaster( String id, String organization);

}
