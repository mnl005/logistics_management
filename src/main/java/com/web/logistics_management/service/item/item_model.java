package com.web.logistics_management.service.item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "item")
public class item_model {

    @EmbeddedId
    private ItemId id;  // 복합 키

    private String name;

    private String image;

    public void setOrganizationAndCode(String newOrganization, String newCode) {
        this.id = new ItemId(newOrganization, newCode);  // 새로운 ItemId 객체로 설정
    }

    // 복합키 클래스
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemId implements Serializable {
        private String organization;
        private String code;
    }
}