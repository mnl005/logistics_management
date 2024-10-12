package com.web.logistics_management.service.user_ser;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user")
public class user_model {

    @Id
    private String id;

    private String organization;

    @Email
    private String email;

    private String name;

    private String phone;

    private String profile;


}
