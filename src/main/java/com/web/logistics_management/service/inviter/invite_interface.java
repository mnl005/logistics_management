package com.web.logistics_management.service.inviter;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface invite_interface extends JpaRepository<invite_model, String> {


    List<invite_model> findByInviter(String inviter);

    List<invite_model> findByTarget(String target);

    List<invite_model> findByNum(Integer target);

    List<invite_model> findByInviterAndTarget(String inviter, String target);


    void deleteByNum(Integer num);

    void deleteByInviter(String inviter);

    void deleteByTarget(String inviter);
}