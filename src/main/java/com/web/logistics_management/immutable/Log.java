package com.web.logistics_management.immutable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class Log {

    //비공용 로그
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Before(value = "within(com.web.logistics_management.function..*)")
//    public void beforeLog(JoinPoint joinPoint) {
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        String methodName = joinPoint.getSignature().getName();
//        logger.info("메소드 ==== \n{}.{}()", className, methodName);
//        try {
//            Object[] args = joinPoint.getArgs();
//            Object firstArg = args[0];
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            String jsonInput = objectMapper.writeValueAsString(firstArg);
//            logger.info("입력 >>>>> \n{}", jsonInput);
//        }
//        catch (Exception e) {
//            logger.error("입력을 JSON으로 변환하는 중 에러 발생: {}", e.getMessage());
//        }
//    }


    //    @AfterReturning(value = "within(com.web.logistics_management.function..*)", returning = "result")

    public static Object processData(Object data) {
        // 데이터가 리스트일 경우
        if (data instanceof List<?>) {
            List<Object> list = (List<Object>) data;
            for (int i = 0; i < list.size(); i++) {
                // 재귀 호출로 내부 요소까지 처리
                list.set(i, processData(list.get(i)));
            }
            return list;
        }
        // 데이터가 맵일 경우
        else if (data instanceof Map<?, ?>) {
            Map<Object, Object> map = (Map<Object, Object>) data;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                // 재귀 호출로 값 부분 처리
                map.put(entry.getKey(), processData(entry.getValue()));
            }
            return map;
        }
        // 데이터가 배열일 경우
        else if (data.getClass().isArray()) {
            Object[] array = (Object[]) data;
            for (int i = 0; i < array.length; i++) {
                // 배열의 각 요소 처리
                array[i] = processData(array[i]);
            }
            return array;
        }
        // 데이터가 문자열일 경우
        else if (data instanceof String) {
            String stringData = (String) data;
            if (stringData.length() > 100) {
                return "많은값";
            }
            return stringData;
        }
        // 다른 데이터 타입은 그대로 반환
        else {
            return data;
        }
    }

    @AfterReturning(value = "within( com.web.logistics_management.function..*_controller)", returning = "result")
    public void afterLog(Object result) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();

            HashMap<String, Object> map1 = new HashMap<>();
            HashMap<String, Object> map2 = new HashMap<>();
            map1 = objectMapper.convertValue(body, new TypeReference<HashMap<String, Object>>() {});

            map2.put("url",map1.get("url"));
            map2.put("type",map1.get("type"));
            map2.put("event",map1.get("event"));
            map2.put("error",map1.get("error"));
            map2.put("id_data",map1.get("id_data"));
            map2.put("msg",map1.get("msg"));
            map2.put("redirect",map1.get("redirect"));
            map2.put("req_define",map1.get("req_define"));
            map2.put("res_define",map1.get("res_define"));



            if(map1.get("req_data") != null){
                map2.put("req_data",processData(map1.get("req_data")));
            }
            else{
                map2.put("req_data",null);
            }

            if(map1.get("res_data") != null){
                map2.put("res_data",processData(map1.get("res_data")));
            }
            else{
                map2.put("res_data",null);
            }



            // 줄바꿈을 추가하도록 설정
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            String jsonResult = objectMapper.writeValueAsString(map2);

            logger.info("""
                            \n--------------------------------------------------\s
                            \n- url : {}\s
                            - type : {}\s
                            - event : {}\s
                            - error : {}\s
                            - id_data : {}\s
                            - msg : {}\s
                            - redirect : {}\s
                            - req_define : {}\s
                            - res_define : {}\s
                            - req_data : {}\s
                            - res_data : {}\s
                            """,
                    map2.get("url"),
                    map2.get("type"),
                    map2.get("event"),
                    map2.get("error"),
                    map2.get("id_data"),
                    map2.get("msg"),
                    map2.get("redirect"),
                    map2.get("req_define"),
                    map2.get("res_define"),
                    map2.get("req_data"),
                    map2.get("res_data")
            );

        } catch (Exception e) {
            logger.error("출력을 JSON으로 변환하는 중 에러 발생: {}", e.getMessage());
        }
    }

    @AfterThrowing(value = "within(com.web.logistics_management.function..*_business)", throwing = "ex")
    public void logError(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.error("\n 에러메소드 : {} \n 에러클래스 : {} \n 에러메시지 : {}", methodName, className, ex.getMessage());
    }

}