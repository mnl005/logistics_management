package com.web.logistics_management.service.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "inventory")
public class inventory_model {

    @EmbeddedId
    private InventoryId id;

    private Integer quantity;

    private String updated_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryStatus status = InventoryStatus.NORMAL;

    public void setOrganizationAndCode(String newOrganization, String newLocationCode,String newItemCode) {
        this.id = new InventoryId(newOrganization, newLocationCode,newItemCode);
    }


    // 복합키 클래스
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InventoryId implements Serializable {
        private String organization;
        private String location_code;
        private String item_code;
    }
}

