package com.web.logistics_management.function.logistics;

import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.item.item_model;
import com.web.logistics_management.service.repository.repository_model;
import com.web.logistics_management.service.user.user_model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class logistics_business {
    private final com.web.logistics_management.service.user.user_service user_service;
    private final com.web.logistics_management.service.item.item_service item_service;
    private final com.web.logistics_management.service.repository.repository_service repository_service;
    private final com.web.logistics_management.service.jwt_service jwt_service;

    // 기능 : 상품 등록
    // 받는 데이터 : v1(상품식별코드), v2(상품이름), image(상품이미지)
    // 보낼 데이터 : 없음
    public Dto<model, Object> item_insert(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 그룹 이름
        String group = me.getOrganization();
        // v1 : 상품의 식별코드
        String code = req.getV1();
        // v2 : 상품 이름
        String name = req.getV2();
        // image : 상품 이미지
        String image = req.getImage();

        // 상품 정보 설정
        item_model item = new item_model();
        item.setOrganizationAndCode(group, code);
        item.setName(name);
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
    public Dto<Object, Object> item_info_all(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 그룹 이름
        String group = me.getOrganization();

        // 모든 상품 조회
        List<HashMap<String, String>> list = item_service.selectGroup(group);

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
    public Dto<model, Object> item_info(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자의 그룹 이름
        String group = me.getOrganization();
        // v1 : 상품 코드
        String code = req.getV1();

        //상품 조회
        HashMap<String, String> item = item_service.selectGroupCode(group, code);

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
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자의 그룹 이름
        String group = me.getOrganization();
        // v1 : 상품 코드
        String code = req.getV1();

        //상품 삭제
        item_service.deleteGroupCode(group, code);

        // 완료
        dto.setMsg("상품 삭제 완료");


        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 기능 : 로케이션 전체조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : location_info(location,code,quantity,)
    public Dto<model, Object> location_info_all(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자의 그룹 이름
        String group = me.getOrganization();

        // 로케이션 전체조회
        List<HashMap<String, String>> list = repository_service.selectByOrganization(group);

        //  보낼 데이터 형식 : location_info
        res.put("location_info", list);

        // 조회된 로케이션 개수
        int num = list.size();

        // 완료
        dto.setRes_data(res);
        dto.setMsg(num + " 개의 로케이션 정보가 조회되었습니다");


        return dto;
    }

    // 기능 : 로케이션 특정 조회
    // 받는 데이터 : v1(로케이션 위치정보)
    // 보낼 데이터 : location_info(location,code,quantity)
    public Dto<model, Object> location_info(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자의 그룹 이름
        String group = me.getOrganization();
        // 로케이션 위치정보
        String location = req.getV1();

        // 로케이션 조회
        HashMap<String, String> map = repository_service.selectGroupLocation(group, location);

        // 보낼 데이터 형식 : location_info
        res.put("location_info", map);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("로케이션 조회 완료");


        return dto;
    }

    // 기능 : 로케이션 수량 이상 조회
    // 받는 데이터 : v1(수량)
    // 보낼 데이터 : location_info(location,code,quantity)
    public Dto<model, Object> location_info_up(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자의 그룹 이름
        String group = me.getOrganization();
        // 이상 조회 값
        int i = Integer.parseInt(req.getV1());

        // 이상값 조회
        List<HashMap<String, String>> list = repository_service.selectQuantityUp(group, i);

        // 보낼 데이터 형식 : location_info
        res.put("location_info", list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(i + " 개 이상의 상품을 가진 로케이션에 대해 조회되었습니다");


        return dto;
    }

    // 기능 : 로케이션 수량 이하 조회
    // 받는 데이터 : v1(수량)
    // 보낼 데이터 : location_info(organization,location,code,quantity)
    public Dto<model, Object> location_info_down(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자의 그룹 이름
        String group = me.getOrganization();
        // 이하 조회 값
        int i = Integer.parseInt(req.getV1());

        // 이하 조회
        List<HashMap<String, String>> list = repository_service.selectQuantityDown(group, i);

        // 보낼 데이터 형식 : location_info
        res.put("location_info", list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg(req.getV1() + " 개 이하의 상품을 가진 로케이션에 대해 조회되었습니다");


        return dto;
    }

    // 기능 : 로케이션 수량 없음 조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : location_info(organization,location,code,quantity)
    public Dto<model, Object> location_info_no(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 유저의 그룹 이름
        String group = me.getOrganization();

        // 수량이 0인 로케이션 조회
        List<HashMap<String, String>> list = repository_service.selectQuantityZero(group);

        // 보낼 데이터 형식 : location_info
        res.put("location_info", list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("상품이 0개인 수량을 가지는 로케이션에 대해 조회가 되었습니다");


        return dto;
    }

    // 기능 : 로케이션 상품 코드로 조회
    // 받는 데이터 : v1(상품코드)
    // 보낼 데이터 : location_info(organization,location,code,quantity)
    public Dto<model, Object> location_info_code(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 유저의 그룹 이름
        String group = me.getOrganization();
        // 상품코드
        String code = req.getV1();

        // 상품코드 가지는 로케이션 조회
        List<HashMap<String, String>> list = repository_service.selectCode(group, code);

        // 보낼 데이터 형식 : location_info
        res.put("location_info", list);

        // 완료
        dto.setRes_data(res);
        dto.setMsg("로케이션 상품 코드로 조회 완료");


        return dto;
    }

    // 기능 : 로케이션 업데이트
    // 받는 데이터 : type(상품코드 | 수량), v1(로케이션번호), v2(상품코드) 또는 v2(수량)- 수량이 0이 아니라면 상품 코드 변경 불가
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_update(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 유저의 그룹 이름
        String group = me.getOrganization();
        // 상품코드를 업데이트할지, 수량을 업데이트 할지
        String type = req.getType();
        // 로케이션 위치 정보
        String location = req.getV1();
        // 상품코드 또는 수량
        String codeOrquantity = req.getV2();

        // 업데이트할 로케이션
        HashMap<String, String> map = repository_service.selectGroupLocation(group, location);
        // 업데이트할 로케이션의 상품수량
        int i = Integer.parseInt(map.get("quantity"));

        // 상품수량 존재시 로케이션에 할당된 상품 바꿀때
        if (type.equals("code") && i >= 1) {
            // 완료
            dto.setMsg("로케이션에 " + i + " 개의 상품이 남아있어서 상품 정보 변경이 불가합니다");
        }
        // 로케이션에 상품의 수량이 0개 이거나, 바꾸려는 정보가 상품코드가 아닌 물건의 개수라면
        else {
            // 로케이션 업데이트
            repository_service.update(group, location, type, codeOrquantity);
            // 완료
            dto.setMsg("로케이션 정보 업데이트가 완료되었습니다");
        }


        return dto;
    }

    // 기능 : 로케이션 신규 등록
    // 받는 데이터 : v1(로케이션 분류번호), v2(상품코드), v3(갯수)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_insert(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 시용자 그룹 이름
        String group = me.getOrganization();
        // 로케이션 분류번호
        String location = req.getV1();
        // 상품코드
        String code = req.getV2();
        // 상품수량
        int quantity = Integer.valueOf(req.getV3());

        // 로케이션 정보세팅
        repository_model model = new repository_model();
        model.setOrganizationAndLocation(group, location);
        model.setCode(code);
        model.setQuantity(quantity);

        // 로케이션 등록
        repository_service.insert(model);

        // 완료
        dto.setMsg("로케이션 등록 완료");

        return dto;
    }

    // 기능 : 로케이션 삭제
    // 받는 데이터 : v1(로케이션 번호)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_delete(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 그룹 이름
        String group = me.getOrganization();
        // 로케이션 번호
        String location = req.getV1();

        // 로케이션에 정보
        HashMap<String, String> map = repository_service.selectGroupLocation(group, location);
        // 상품수량
        int i = Integer.parseInt(map.get("quantity"));

        // 상품수량이 0이 아닐때
        if (i >= 1) {
            // 완료
            dto.setMsg("로케이션에 물건이" + i + " 개 남아 해당 로케이션 정보를 삭제할 수 없습니다");
        }
        // 상품이 남아있지 않다면
        else {
            // 로케이션 삭제
            repository_service.delete(group, req.getV1());
            // 완료
            dto.setMsg("해당 로케이션 정보가 삭제되었습니다");
        }


        return dto;
    }


}
