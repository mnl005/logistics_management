package com.web.logistics_management.service.location;

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
@Entity(name = "location")
public class location_model {

    @EmbeddedId
    private LocatoinID id;  // 복합 키

    public void setOrganizationAndLocation(String newOrganization, String newLocation) {
        this.id = new location_model.LocatoinID(newOrganization, newLocation);
    }


    // 복합키 클래스
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LocatoinID implements Serializable {
        private String organization;
        private String location_code;
    }

}