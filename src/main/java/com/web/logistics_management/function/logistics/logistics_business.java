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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();
            // v1 : 상품의 식별코드
            String code = req.getV1();
            // v2 : 상품 이름
            String name = req.getV2();
            // v3 : 상품 이미지
            String image = req.getV3();

            // 삼품 정보 설정
            item_model item = new item_model();
            item.setOrganizationAndCode(group, code);
            item.setName(name);
            item.setImage(image);

            // 상품등록
            item_service.insert(item);

            // 완료
            dto.setMsg("상품등록완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("상품등록실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 상품 전체 정보 조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : 모든 아이템 정보
    public Dto<Object, Object> item_info_all(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 유저 그룹으로 모든 상품 조회
            List<HashMap<String, String>> list = item_service.selectGroup(group);

            // 보낼 데이터 형식 : item_info
            res.put("item_info", list);

            // 완료
            dto.setRes_data(res);
            dto.setMsg("상품 리스트 조회 완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("상품 리스트 조회 실패");
            dto.setJs("none");
        }

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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();
            // v1 : 상품 코드
            String code = req.getV1();

            //상품 조회
            HashMap<String, String> item = item_service.selectGroupCode(group, code);

            // 보낼 데이터 형식 : item_info
            res.put("item_info", item);

            // 완료
            dto.setRes_data(res);
            dto.setMsg("상품 리스트 조회 완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("상품 리스트 조회 실패");
            dto.setJs("none");
        }

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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();
            // v1 : 상품 코드
            String code = req.getV1();

            //상품 삭제
            item_service.deleteGroupCode(group, code);

            // 완료
            dto.setMsg("상품 삭제 완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("상품 삭제 실패");
            dto.setJs("none");
        }

        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 기능 : 로케이션 전체조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : 로케이션 전체 데이터
    public Dto<model, Object> location_info_all(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            //  보낼 데이터 형식 : location_info
            res.put("location_info", repository_service.selectByOrganization(group));

            // 조회된 로케이션 개수
            int num = repository_service.selectByOrganization(group).size();

            // 완료
            dto.setRes_data(res);
            dto.setMsg(num + " 개의 로케이션 정보가 조회되었습니다");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("로케이션 조회 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 로케이션 특정 조회
    // 받는 데이터 : v1(로케이션정보)
    // 보낼 데이터 : 특정 로케이션 정보
    public Dto<model, Object> location_info(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 보낼 데이터 형식 : location_info
            res.put("location_info", repository_service.selectGroupLocation(group, req.getV1()));


            // 완료
            dto.setRes_data(res);
            dto.setMsg("로케이션 조회 완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("로케이션 조회 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 로케이션 수량 이상 조회
    // 받는 데이터 : v1(수량)
    // 보낼 데이터 : 수량 이상 로케이션들 정보
    public Dto<model, Object> location_info_up(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 보낼 데이터 형식 : location_info
            res.put("location_info", repository_service.selectQuantityUp(group, Integer.parseInt(req.getV1())));

            // 완료
            dto.setRes_data(res);
            dto.setMsg(req.getV1() + " 개 이상의 상품을 가진 로케이션에 대한 정보가 조회되었습니다");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("로케이션 조회 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 로케이션 수량 이하 조회
    // 받는 데이터 : v1(수량)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_info_down(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 보낼 데이터 형식 : location_info
            res.put("location_info", repository_service.selectQuantityDown(group, Integer.parseInt(req.getV1())));

            // 완료
            dto.setRes_data(res);
            dto.setMsg(req.getV1() + " 개 이하의 상품을 가진 로케이션에 대한 정보가 조회되었습니다");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("로케이션 조회 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 로케이션 수량 없음 조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_info_no(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 보낼 데이터 형식 : location_info
            res.put("location_info", repository_service.selectQuantityZero(group));

            // 완료
            dto.setRes_data(res);
            dto.setMsg("상품이 0개인 수량을 가지는 로케이션에 대해 조회가 되었습니다");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("로케이션 조회 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 로케이션 상품 코드로 조회
    // 받는 데이터 : v1(상품코드)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_info_code(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 보낼 데이터 형식 : location_info
            res.put("location_info", repository_service.selectCode(group, req.getV1()));

            // 완료
            dto.setRes_data(res);
            dto.setMsg("로케이션 상품 코드로 조회 완료");
            dto.setJs("none");
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("로케이션 조회 실패");
            dto.setJs("none");
        }

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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 상품코드를 업데이트할지, 수량을 업데이트 할지
            String type = req.getType();
            // 로케이션 위치 정보
            String location = req.getV1();
            // 상품코드 또는 수량
            String codeOrquantity = req.getV2();

            // 로케이션 정보 불러와 해당 로케이션의 상품 수량을 파악
            HashMap<String, String> map = repository_service.selectGroupLocation(group, location);
            System.out.println(map);
            int i = Integer.parseInt(map.get("quantity"));

            // 로케이션에 상품이 남은 상태에서 상품 수량 변경을 시도 한다면
            if (type.equals("code") && i >= 1) {
                // 완료
                dto.setMsg("로케이션에 " + i + " 개의 상품이 남아있어서 상품 정보 변경이 불가합니다");
                dto.setJs("none");
            }
            // 로케이션에 상품의 수량이 0개 이거나 바꾸려는 정보가 상품코드가 아닌 물건의 개수라면
            else {
                // 로케이션 업데이트
                repository_service.update(group, location, type, codeOrquantity);
                // 완료
                dto.setMsg("로케이션 정보 업데이트가 완료되었습니다");
                dto.setJs("none");
            }

        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("로케이션 정보 업데이트 실패");
            dto.setJs("none");
        }

        return dto;
    }

    // 기능 : 로케이션 신규 등록
    // 받는 데이터 : v1(로케이션 번호), v2(상품코드), v3(갯수)
    // 보낼 데이터 : 없음
    public Dto<model, Object> location_insert(Dto<model, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 모델의 값을 유저가 정의한 값으로 설정
            repository_model model = new repository_model();
            model.setOrganizationAndLocation(group, req.getV1());
            model.setCode(req.getV2());
            model.setQuantity(Integer.valueOf(req.getV3()));

            // 이미 등록된 로케이션인지 확인
            try{
                HashMap<String,String> map = repository_service.selectGroupLocation(group,req.getV1());
                // 완료
                dto.setMsg("이미 등록되어 있는 로케이션입다");
                dto.setJs("none");
            }
            catch(Exception e){
                // 로케이션 등록
                repository_service.insert(model);
                // 완료
                dto.setMsg("로케이션 등록 완료");
                dto.setJs("none");
            }
        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("로케이션 등록 실패");
            dto.setJs("none");
        }

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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 유저의 그룹 이름
            String group = me.get().getOrganization();

            // 로케이션에 남아있는 상품 갯수 확인
            HashMap<String,String> map =  repository_service.selectGroupLocation(group,req.getV1());
            int i = Integer.parseInt(map.get("quantity"));

            // 만약 로케이션에 상품이 남아있다면 로케이션 삭제 불가
            if(i >= 1){
                // 완료
                dto.setMsg("로케이션에 물건이" + i + " 개 남아 해당 로케이션 정보를 삭제할 수 없습니다");
                dto.setJs("none");
            }
            // 상품이 남아있지 않다면
            else{
                // 로케이션 삭제
                repository_service.delete(group, req.getV1());
                // 완료
                dto.setMsg("해당 로케이션 정보가 삭제되었습니다");
                dto.setJs("none");
            }

        }
        // 인증 정보가 유효하지 않다면
        else {

            // 완료
            dto.setMsg("로케이션 삭제 실패");
            dto.setJs("none");
        }

        return dto;
    }


}
