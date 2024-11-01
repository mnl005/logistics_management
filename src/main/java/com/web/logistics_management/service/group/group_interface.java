package com.web.logistics_management.service.group;

import com.web.logistics_management.service.user.user_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface group_interface extends JpaRepository<group_model, String> {

    @Query("SELECT m FROM organization m WHERE m.organization = :organization")
    List<group_model> findByOrganization(String organization);

    @Query("SELECT m FROM organization m WHERE m.master = :master")
    List<group_model> findByMaster(String master);

    @Query("SELECT m FROM organization m WHERE m.organization = :organization and m.master = :master")
    Optional<group_model> findByOrganizationAndMaster(String organization, String master);

    @Modifying
    @Query("DELETE FROM organization m WHERE m.organization = :organization AND m.master = :master")
    void deleteByOrganizationAndMaster(String organization, String master);
}