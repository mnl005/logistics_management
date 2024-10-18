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
    // 기타정보
    private String url;
    private String type;
    private Object event;
    private Object error;

    // 메인정보
    private String id_data;
    private String msg;
    private T req_data;
    private V res_data;


    // 요청과 응답 정의
    private Object req_define;
    private Object res_define;

    //
    private String redirect;


}
