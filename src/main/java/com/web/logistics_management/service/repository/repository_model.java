package com.web.logistics_management.service.repository;

import com.web.logistics_management.service.item.item_model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "repository")
public class repository_model {

    @EmbeddedId
    private ItemId id;  // 복합 키

    private String code;
    private Integer quantity;

    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code",this.code);
        map.put("quantity", String.valueOf(this.quantity));
        map.put("organization", this.id.getOrganization());
        map.put("location", this.id.getLocation());
        return map;
    }

    public void setOrganizationAndLocation(String newOrganization, String newLocation) {
        this.id = new repository_model.ItemId(newOrganization, newLocation);  // 새로운 ItemId 객체로 설정
    }


    // 복합키 클래스
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemId implements Serializable {
        private String organization;
        private String location;
    }


}