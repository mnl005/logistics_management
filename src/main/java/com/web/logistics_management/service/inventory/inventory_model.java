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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer num;

    private String organization;

    private String location_code;

    private String item_code;

    private Integer quantity;

    private String updated_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InventoryStatus status = InventoryStatus.NORMAL;


}

