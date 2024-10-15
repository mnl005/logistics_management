package com.web.logistics_management.service.repository;

import com.web.logistics_management.service.item.item_model;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface repository_interface extends JpaRepository<repository_model, String> {
    @Query("SELECT m FROM repository m WHERE m.id.organization = :organization")
    List<repository_model> findByOrganization(@Param("organization") String organization);

    @Query("SELECT m FROM repository m WHERE m.id.organization = :organization AND m.id.location = :location")
    repository_model findByOrganizationAndLocation(@Param("organization") String organization, @Param("location") String location);

    @Query("SELECT m FROM repository m WHERE m.id.organization = :organization AND m.quantity >= :quantity")
    List<repository_model> findQuantityUp(@Param("organization") String organization, @Param("quantity") Integer quantity);

    @Query("SELECT m FROM repository m WHERE m.id.organization = :organization AND m.quantity <= :quantity")
    List<repository_model> findQuantityDown(@Param("organization") String organization, @Param("quantity") Integer quantity);

    @Query("SELECT m FROM repository m WHERE m.id.organization = :organization AND m.quantity = 0")
    List<repository_model> findQuantityZero(@Param("organization") String organization);

    @Query("SELECT m FROM repository m WHERE m.id.organization = :organization AND m.code = :code")
    List<repository_model> findByCode(@Param("organization") String organization, @Param("code") String code);



    @Modifying
    @Transactional
    @Query("DELETE FROM repository m WHERE m.id.organization = :organization AND m.id.location = :location")
    void deleteByOrganizationAndCode(@Param("organization") String organization, @Param("location") String location);


}

