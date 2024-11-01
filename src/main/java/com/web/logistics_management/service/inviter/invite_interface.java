package com.web.logistics_management.service.inviter;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface invite_interface extends JpaRepository<invite_model, String> {


    List<invite_model> findByMaster(String master);

    List<invite_model> findByTarget(String target);

    List<invite_model> findByNum(Integer num);

    @Query("SELECT m FROM invite m WHERE m.master = :master and m.target = :target and m.organization = :organization")
    Optional<invite_model> findByMasterAndTargetAndOrganization(String master, String target, String organization);


    void deleteByNum(Integer num);
}