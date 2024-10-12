package com.web.logistics_management.function.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.logistics_management.immutable.Dto;
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
@RequestMapping("/user")
public class user_con {

    private final ObjectMapper mapper = new ObjectMapper();
    private final user_business user_business;

    @PostMapping("/me")
    public ResponseEntity<JsonNode> user_info(@RequestBody Dto<Object,Object> dto, HttpServletRequest request, HttpServletResponse response){
        dto = user_business.user_info(dto,request,response);
        return ResponseEntity.ok(mapper.valueToTree(dto));
    }
}
