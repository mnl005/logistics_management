package com.web.logistics_management.immutable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
    @AfterReturning(value = "within( com.web.logistics_management.function..*_controller)", returning = "result")
    public void afterLog(Object result) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 줄바꿈을 추가하도록 설정
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            String jsonResult = objectMapper.writeValueAsString(result);

            logger.info("출력 <<<<< \n{}", jsonResult);
        } catch (Exception e) {
            logger.error("출력을 JSON으로 변환하는 중 에러 발생: {}", e.getMessage());
        }
    }

    @AfterThrowing(value = "within(com.web.logistics_management.function..*)", throwing = "ex")
    public void logError(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.error("에러메소드 : {} \n 에러클래스 : {} \n 에러메시지 : {}", methodName, className, ex.getStackTrace());
    }

}