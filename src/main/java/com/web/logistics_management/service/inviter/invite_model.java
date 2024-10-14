package com.web.logistics_management.service.inviter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "invite")
public class invite_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer num;

    private String inviter;

    private String target;

}
