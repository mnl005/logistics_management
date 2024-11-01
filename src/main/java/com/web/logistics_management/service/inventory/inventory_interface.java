package com.web.logistics_management.service.inventory;

import com.web.logistics_management.service.user.user_model;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface inventory_interface extends JpaRepository<inventory_model, String> {

    @Query("SELECT m FROM inventory m WHERE m.id.organization = :organization")
    List<inventory_model> findByOrganization(String organization);

    @Query("SELECT m FROM inventory m WHERE m.id.organization = :organization and m.id.location_code = :location_code")
    List<inventory_model> findByOrganizationAndLocationCode(String organization, String location_code);

    @Query("SELECT m FROM inventory m WHERE m.id.organization = :organization and m.id.item_code = :item_code")
    List<inventory_model> findByOrganizaitonAndItemCode(String organization, String item_code);

    @Query("SELECT m FROM inventory m WHERE m.id.organization = :organization and m.quantity = :quantity")
    List<inventory_model> findByOrganizationAndQuantity(String organization, Integer quantity);

    @Query("SELECT m FROM inventory m WHERE m.id.organization = :organization and m.status = :status")
    List<inventory_model> findByOrganizationAndStatus(String organization, InventoryStatus status);

    @Query("SELECT m FROM inventory m WHERE m.id.organization = :organization and m.id.location_code = :location_code and m.id.item_code = :item_code")
    Optional<inventory_model> findByOrganizationAndLocationCodeAndItemCode(String organization, String location_code, String item_code);


    @Modifying
    @Transactional
    @Query("DELETE FROM inventory m WHERE m.id.organization = :organization AND m.id.location_code = :location_code AND m.id.item_code = :item_code")
    void delete(@Param("organization") String organization, @Param("location_code") String location_code ,@Param("item_code") String item_code);

}
