package com.web.logistics_management.function.group;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class group_controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final group_business group_business;

    // 그룹 생성
    @PostMapping("/create")
    public ResponseEntity<JsonNode> create(@RequestBody Dto<model,Object> dto, HttpServletRequest request){
        dto = group_business.create(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 그룹 접속
    @PostMapping("/me_groups")
    public ResponseEntity<JsonNode> me_groups(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.me_groups(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 나의 그룹 정보
    @PostMapping("/group_connect")
    public ResponseEntity<JsonNode> group_connect(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = group_business.group_connect(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 그룹원 정보 상세보기
    @PostMapping("/select")
    public ResponseEntity<JsonNode> select(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.select(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }


    // 초대하기
    @PostMapping("/invite")
    public ResponseEntity<JsonNode> invite(@RequestBody Dto<model,Object> dto, HttpServletRequest request){
        dto = group_business.invite(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 초대한 목록
    @PostMapping("/master_list")
    public ResponseEntity<JsonNode> master_list(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.master_list(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 초대 받은 목록
    @PostMapping("/target_list")
    public ResponseEntity<JsonNode> target_list(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.target_list(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 초대취소
    @PostMapping("/cancel")
    public ResponseEntity<JsonNode> cancel(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.cancel(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 초대승락
    @PostMapping("/accept")
    public ResponseEntity<JsonNode> accept(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.accept(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 그룹 나가기
    @PostMapping("/delete")
    public ResponseEntity<JsonNode> delete(@RequestBody Dto<Object,Object> dto, HttpServletRequest request,HttpServletResponse response){
        dto = group_business.delete(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
}
