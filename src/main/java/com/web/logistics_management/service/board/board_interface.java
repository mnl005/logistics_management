package com.web.logistics_management.service.board;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface board_interface extends JpaRepository<board_model, String> {

    @Query("SELECT m FROM board m WHERE m.id = :id")
    List<board_model> selectById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM board m WHERE m.id = :id AND m.num = :num")
    void deleteByIdNum(@Param("id") String id, @Param("num") Integer num);
}
