package com.web.logistics_management.service.location;

import com.web.logistics_management.service.item.item_model;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface location_interface extends JpaRepository<location_model, String> {
    @Query("SELECT m FROM location m WHERE m.id.organization = :organization AND m.id.location_code = :location_code")
    location_model findByOrganizationAndLocation_code(@Param("organization") String organization, @Param("location_code") String location_code);

    @Query("SELECT m FROM location m WHERE m.id.organization = :organization")
    List<location_model> findByOrgnaization(@Param("organization") String organization);

    @Modifying
    @Transactional
    @Query("DELETE FROM location m WHERE m.id.organization = :organization AND m.id.location_code = :location_code")
    void deleteByOrganizationAndLocation_code(@Param("organization") String organization, @Param("location_code") String location_code);


}

