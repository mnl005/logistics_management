package com.web.logistics_management.service.repository;

import com.web.logistics_management.service.item.item_model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "repository")
public class repository_model {

    @EmbeddedId
    private ItemId id;  // 복합 키

    private String code;
    private String quantity;

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