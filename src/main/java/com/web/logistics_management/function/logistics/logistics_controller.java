package com.web.logistics_management.function.logistics;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.function.user.user_business;
import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.email_service;
import com.web.logistics_management.service.item.item_model;
import com.web.logistics_management.service.jwt_service;
import com.web.logistics_management.service.user.user_service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.web.logistics_management.function.logistics.logistics_business;

@RequiredArgsConstructor
@RestController
@RequestMapping("/logistics")
public class logistics_controller {

    private final ObjectMapper mapper = new ObjectMapper();
    private final logistics_business logistics_business;

    // 아이템 등록
    @PostMapping("/item/insert")
    public ResponseEntity<JsonNode> item_insert(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.item_insert(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 아이템 전체 조회
    @PostMapping("/item/select_all")
    public ResponseEntity<JsonNode> item_info_all(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.item_info_all(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 아이템 일부 조회
    @PostMapping("/item/select")
    public ResponseEntity<JsonNode> item_info(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.item_info(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 아이템 삭제
    @PostMapping("/item/delete")
    public ResponseEntity<JsonNode> item_delete(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.item_delete(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }



    // 로케이션 전체 조회
    @PostMapping("/location/select_all")
    public ResponseEntity<JsonNode> location_info_all(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_info_all(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 특정 조회
    @PostMapping("/location/select")
    public ResponseEntity<JsonNode> location_info(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_info(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 수량 이상 조회
    @PostMapping("/location/up")
    public ResponseEntity<JsonNode> location_info_up(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_info_up(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 수량 이하 조회
    @PostMapping("/location/down")
    public ResponseEntity<JsonNode> location_info_down(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_info_down(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 수량 없음 조회
    @PostMapping("/location/zero")
    public ResponseEntity<JsonNode> location_info_no(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_info_no(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 상품 코드 조회
    @PostMapping("/location/code")
    public ResponseEntity<JsonNode> location_info_code(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_info_code(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 업데이트
    @PostMapping("/location/update")
    public ResponseEntity<JsonNode> location_update(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_update(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 신규 삽입
    @PostMapping("/location/insert")
    public ResponseEntity<JsonNode> location_insert(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_insert(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
    // 로케이션 삭제
    @PostMapping("/location/delete")
    public ResponseEntity<JsonNode> location_delete(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_delete(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

}
