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

    // 소속중인 그룹 리스트 불러오기
    @PostMapping("/me")
    public ResponseEntity<JsonNode> me(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.me(dto,request);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 그룹 연결
    @PostMapping("/connect")
    public ResponseEntity<JsonNode> connect(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = group_business.connect(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 그룹 정보 불러오기
    @PostMapping("/info")
    public ResponseEntity<JsonNode> group_info(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = group_business.group_info(dto,request,response);
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

    // 초대하거나 초대받은 목록조회
    @PostMapping("/invite_select")
    public ResponseEntity<JsonNode> invite_select(@RequestBody Dto<Object,Object> dto, HttpServletRequest request){
        dto = group_business.invite_select(dto,request);
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
