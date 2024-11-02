package com.web.logistics_management.function.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.immutable.Dto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.logistics_management.service.post.post_model;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class post_controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final post_business post_business;

    // 같은 그룹의 게시물을 불러온다
    @PostMapping("/group")
    public ResponseEntity<JsonNode> select_all(@RequestBody Dto<Object, Object> dto, HttpServletRequest request){
        dto = post_business.group(dto, request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 유저의 게시글을 불러온다
    @PostMapping("/me")
    public ResponseEntity<JsonNode> me(@RequestBody Dto<Object, Object> dto, HttpServletRequest request){
        dto = post_business.me(dto, request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 게시글 등록
    @PostMapping("/insert")
    public ResponseEntity<JsonNode> insert(@RequestBody Dto<post_model, Object> dto, HttpServletRequest request){
        dto = post_business.insert(dto, request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 유저의 게시글 삭제
    @PostMapping("/delete")
    public ResponseEntity<JsonNode> delete(@RequestBody Dto<Object, Object> dto, HttpServletRequest request){
        dto = post_business.delete(dto, request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

}
