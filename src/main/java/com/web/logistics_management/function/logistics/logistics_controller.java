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
    @PostMapping("/item/select")
    public ResponseEntity<JsonNode> item_select(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.item_select(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 아이템 삭제
    @PostMapping("/item/delete")
    public ResponseEntity<JsonNode> item_delete(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.item_delete(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

//////////////////////////////////////////////////////////////////////////////////////////////////

    // 로케이션 전체 조회
    @PostMapping("/location/select")
    public ResponseEntity<JsonNode> location_select(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_select(dto,request,response);
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
    public ResponseEntity<JsonNode> location_delete(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.location_delete(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    // 인벤토리 전체조회
    @PostMapping("/inventory/select_all")
    public ResponseEntity<JsonNode> inventory_select_all(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.inventory_select_all(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 인벤토리 일부조
    @PostMapping("/inventory/select")
    public ResponseEntity<JsonNode> inventory_select(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.inventory_select(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 인벤토리 생성
    @PostMapping("/inventory/insert")
    public ResponseEntity<JsonNode> inventory_insert(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.inventory_insert(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 인벤토리 수정
    @PostMapping("/inventory/update")
    public ResponseEntity<JsonNode> inventory_update(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.inventory_update(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }

    // 인벤토리 삭제
    @PostMapping("/inventory/delete")
    public ResponseEntity<JsonNode> inventory_delete(@RequestBody Dto<model,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = logistics_business.inventory_delete(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
}
