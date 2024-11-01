package com.web.logistics_management.service.user_group;

import com.web.logistics_management.service.inventory.inventory_model;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user_organization")
public class user_organization_model {

    @EmbeddedId
    private UOID id;

    public void setIDAndOrganization(String newID, String newOrganization) {
        this.id = new UOID(newID, newOrganization);
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UOID implements Serializable {
        private String id;
        private String organization;
    }


}