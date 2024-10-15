package com.web.logistics_management.immutable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dto<T,V> {

    // 메인정보
    private T req_data;
    private V res_data;
    private String id_data;

    // 제어 정보
    private String msg;
    private String js;

    // 기타정보
    private String url;
    private String type;

    // 이벤트나 에러시
    private Object event;
    private Object error;

    // 요청과 응답 정의
    private Object req_define;
    private Object res_define;


}
