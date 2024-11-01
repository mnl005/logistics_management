package com.web.logistics_management.function.logistics;

import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.inventory.inventory_model;
import com.web.logistics_management.service.item.item_model;
import com.web.logistics_management.service.location.location_model;
import com.web.logistics_management.service.user.user_model;
import com.web.logistics_management.service.user_group.user_organization_model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Component
public class logistics_business {
    private final com.web.logistics_management.service.user.user_service user_service;
    private final com.web.logistics_management.service.item.item_service item_service;
    private final com.web.logistics_management.service.location.location_service location_service;
    private final com.web.logistics_management.service.jwt_service jwt_service;
    private final com.web.logistics_management.service.user_group.user_group_service user_group_service;
    private final com.web.logistics_management.service.inventory.inventory_service inventory_service;


    // 기능 : 상품 등록
    // 받는 데이터 : v1(상품식별코드), v2(상품이름),v3(기타표기사항), image(상품이미지)
    // 보낼 데이터 : 없음
    public Dto<model, Object> item_insert(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // v1 : 상품의 식별코드
        String code = req.getV1();
        // v2 : 상품 이름
        String name = req.getV2();
        // v3 : 기타표기사항
        String other = req.getV3();
        // image : 상품 이미지
        String image = req.getImage();

        // 상품 정보 설정
        item_model item = new item_model();
        item.setOrganizationAndCode(me_group, code);
        item.setName(name);
        item.setOther(other);
        item.setImage(image);

        // 상품등록
        item_service.insert(item);

        // 완료
        dto.setMsg("상품등록완료");


        return dto;
    }

    // 기능 : 상품 전체 정보 조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : item_info(organization,code,name,image)
    public Dto<Object, Object> item_select_all(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // 모든 상품 조회
        List<HashMap<String, String>> list = item_service.select_all(me_group);

        // 보낼 데이터 형식 : item_info
        res.put("item_info", list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("전체 상품 리스트 조회 완료");


        return dto;
    }

    // 기능 : 상품 일부 조회
    // 받는 데이터 : v1(상품코드)
    // 보낼 데이터 : 특정 아이템 정보
    public Dto<model, Object> item_select(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // v1 : 상품 코드
        String code = req.getV1();

        //상품 조회
        HashMap<String, String> item = item_service.select(me_group, code);

        // 보낼 데이터 형식 : item_info
        res.put("item_info", item);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("상품 리스트 조회 완료");


        return dto;
    }

    // 기능 : 상품 삭제
    // 받는 데이터 : v1(상품코드)
    // 보낼 데이터 : 없음
    public Dto<model, Object> item_delete(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // v1 : 상품 코드
        String code = req.getV1();

        //상품 삭제
        item_service.delete(me_group, code);

        // 완료
        dto.setMsg("상품 삭제 완료");


        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 기능 : 로케이션 전체조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : location_info(location_code)
    public Dto<model, Object> location_select_all(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();

        // 로케이션 전체조회
        List<HashMap<String, String>> list = location_service.select_all(me_group);

        //  보낼 데이터 형식 : location_info
        res.put("location_info", list);

        // 조회된 로케이션 개수
        int num = list.size();

        // 완료
        dto.setRes_data(res);
        dto.setMsg(num + " 개의 로케이션 정보가 조회되었습니다");


        return dto;
    }

    // 기능 : 로케이션 신규등록
    // 받는 데이터 : v1(location_code)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_insert(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 받은 로케이션 등록 코드
        String location_code = req.getV1();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_gorup = connect.getId().getOrganization();

        // 등록할 로케이션 정보
        location_model model = new location_model();
        model.setOrganizationAndLocation(me_gorup,location_code);

        // 로케이션 정보 등록
        location_service.insert(model);

        // 완료
        dto.setMsg(location_code + " 로케이션 등록 완료");
        return dto;
    }

    // 기능 : 로케이션 삭제
    // 받는 데이터 : v1(location_code)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_delete(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 받은 로케이션 등록 코드
        String location_code = req.getV1();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // 로케이션 정보 등록
        location_service.delete(me_group, id);

        // 완료
        dto.setMsg(location_code + " 로케이션 삭제 완료");
        return dto;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 기능 : 인벤토리 전체 조회
    // 받는 데이터 : v1(location_code)
    // 보낼 데이터 : 없음
    public Dto<model, Object> inventory_select_all(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // 인벤토리 전체 조회
        List<HashMap<String, String>> list = inventory_service.select_all(me_group);
        res.put("inventory_list",list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(list.size() + " 개의 인벤토리가 조회 되었습니다");
        return dto;
    }

    // 기능 : 특정 인벤토리 조회
    // 받는 데이터 : v1(location_code or item_code or quantity or status), v2(조회할 값)
    // 보낼 데이터 : 없음
    public Dto<model, Object> inventory_select(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // 인벤토리 조건 조회
        List<HashMap<String, String>> list = inventory_service.select(me_group, req.getV1(),req.getV2());
        res.put("inventory_list",list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(req.getV1() + " 을 기준으로 " + list.size() + " 개의 인벤토리가 조회 되었습니다");
        return dto;
    }

    // 기능 : 인벤토리 생성
    // 받는 데이터 : v1(location_code),v2(item_code),v3(quantiy)
    // 보낼 데이터 : 없음
    public Dto<model, Object> inventory_insert(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();


        // 인벤토리 정보 생성
        inventory_model inventory_model = new inventory_model();
        inventory_model.setOrganizationAndCode(me_group,req.getV1(),req.getV2());
        inventory_model.setQuantity(Integer.valueOf(req.getV3()));

        // 정보 등록
        inventory_service.insert(inventory_model);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("인벤토리 생성 완료");
        return dto;
    }

    // 기능 : 인벤토리 수정
    // 받는 데이터 : v1(location_code),v2(item_code),v3(quantity,status),v4(value)
    // 보낼 데이터 : 없음
    public Dto<model, Object> inventory_update(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 받은 로케이션 등록 코드
        String location_code = req.getV1();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();

        // 인벤토리 정보 업데이트
        inventory_service.update(me_group,req.getV1(),req.getV2(),req.getV3(),req.getV4());

        // 완료
        dto.setRes_data(res);
        dto.setMsg("인벤토리 생성 완료");
        return dto;
    }

    // 기능 : 인벤토리 삭제
    // 받는 데이터 : v1(location_code),v2(item_code)
    // 보낼 데이터 : 없음
    public Dto<model, Object> inventory_delete(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 받은 로케이션 등록 코드
        String location_code = req.getV1();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 사용자가 접속한 그룹 이름
        String me_group = connect.getId().getOrganization();

        // 인벤토리 정보 삭제
        inventory_service.delete(me_group,req.getV1(),req.getV2());

        // 완료
        dto.setRes_data(res);
        dto.setMsg("인벤토리 삭제 완료");
        return dto;
    }

}
