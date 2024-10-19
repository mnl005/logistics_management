package com.web.logistics_management.immutable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class Err{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonNode> handleException(Exception ex) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode obj = mapper.createObjectNode();
        Throwable cause = ex.getCause();

        if (cause != null) {
            StackTraceElement[] stackTrace = cause.getStackTrace();
//            log.error(ex.getMessage());
//            log.error(Arrays.toString(ex.getStackTrace()));

        } else {
//            log.error("에러 메시지: " + ex.getMessage());
        }

        return ResponseEntity.
                ok(obj.set("err_msg", mapper.valueToTree(ex.getMessage())));
    }
}