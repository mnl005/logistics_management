package com.web.logistics_management.function.group;

import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.inviter.invite_model;
import com.web.logistics_management.service.inviter.invite_service;
import com.web.logistics_management.service.user.user_service;
import com.web.logistics_management.service.jwt_service;

import com.web.logistics_management.service.user.user_model;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class group_business {
    private final user_service user_service;
    private final invite_service invite_service;
    private final jwt_service jwt_service;

    // 기능 :  그룹 생성
    // 받는 데이터 : v1(생성할그룹이름)
    // 보낼 데이터 : 없음
    public Dto<model, Object> create(Dto<model, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자가 입력한 그룹 이름
        String group_name = req.getV1();
        // 사용자의 아이디
        String id = me.getId();
        // 사용자가 속한 그룹
        String group = me.getName();

        // 사용자가 속한 그룹이 없는지
        boolean bool1 = Objects.isNull(group);
        // 사용자가 입력한 그룹 이름과 동일한 이름을 가진 그룹이 없는지
        boolean bool2 = user_service.select("organization", group_name).isEmpty();

        // 사용자가 속한 그룹이 없고, 사용자가 입력한 그룹 이름이 사용 가능할 때 사용자의 그룹 정보를 변경
        if (bool1 && bool2) {
            // 사용자 그룹 정보 변경
            user_service.update(id, "organization", group_name);
            // 완료
            dto.setMsg(group_name + " 그룹 생성 완료");
        }
        // 사용자가 이미 그룹에 속해 있다면
        else if (!bool1) {
            // 완료
            dto.setMsg("이미 " + group + " 그룹에 속해 있습니다");
        }
        // 사용자가 입력한 그룹 이름이 중복된다면
        else {
            // 완료
            dto.setMsg(group_name + " 은 이미 존재하는 그룹 이름입니다");
        }
        return dto;
    }


    // 기능 : 사용자의 그룹 정보
    // 받는 데이터 : 없음
    // 보낼 데이터 : group_name(그룹이름), group_users(그룹구성원(id,profile)), group_size(그룹구성원수)
    public Dto<Object, Object> group_info(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 그룹 정보
        String group = me.getOrganization();

        // 사용자가 그룹에 소속되어있지 않을때
        if (Objects.isNull(group)) {
            // 완료
            dto.setMsg("그룹에 소속되지 않았습니다");
            return dto;
        }
        //사용자가 그룹에 소속되어 있을때
        else {
            // 같은 그룹원의 정보 불러오기
            List<Object> list = user_service.select("organization", group)
                    .stream()
                    .map(users -> {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", users.getId());
                        map.put("profile", users.getProfile());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 그룹이름, 그룹크기
            HashMap<String, String> map = new HashMap<>();
            map.put("group_name", group);
            map.put("group_size", Integer.toString(list.size()));

            // 보낼 데이터 형식 : group_info
            res.put("group_info", map);
            // 보낼 데이터 형식 : group_users
            res.put("group_users", list);

            // 완료
            dto.setRes_data(res);
            dto.setMsg("나의 그룹정보 조회 완료");

        }


        return dto;
    }


    // 기능 : 그룹 구성원 정보 상세보기
    // 받는 데이터 : id_data(상세보기할 그룹원의 아이디)
    // 보낼 데이터 : user_info(id,organization,emmail,name,phone,profile)
    public Dto<Object, Object> select(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자가 입력한 아이디
        String find_id = dto.getId_data();
        // 사용자가 입력한 아이디에 해당하는 유저의 정보
        user_model find_user = user_service.findById(find_id).get();

        // 찾는 유저의 그룹명
        String find_organization = find_user.getOrganization();
        // 사용자의 그룹명
        String me_organization = me.getOrganization();

        // 나와 찾는 유저의 그룹명이 일치한다면
        if (find_organization.equals(me_organization)) {
            // 보낼 데이터 형식 : user_info
            res.put("user_info", find_user);
            // 완료
            dto.setRes_data(res);
            dto.setMsg("그룹원 조회 완료");
        } else {
            // 완료
            dto.setMsg("그룹원 조회 실패");
        }

        return dto;
    }


    // 기능 : 그룹원으로 초대하기
    // 받는 데이터 : v1(초대할 유저 아이디)
    // 보낼 데이터 : 없음
    public Dto<model, Object> invite(Dto<model, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 초대할 유저
        String target = req.getV1();
        // 초대할 유저의 정보
        user_model find_user = user_service.findById(target).get();

        // 사용자가 조직에 소속되지 않았다면
        if (me.getOrganization() == null) {
            // 완료
            dto.setMsg("당신은 그룹에 소속되어 있지 않습니다");
        }
        // 해당 유저가 이미 조직에 소속되었다면
        else if (find_user.getOrganization() != null) {
            // 완료
            dto.setMsg("해당 유저가 이미 그룹에 소속되어 있습니다");
        }
        // 초대 가능한 상태라면
        else {
            // 초대하기
            invite_service.insert(me.getId(), target);
            // 완료
            dto.setMsg("초대 완료");
        }


        return dto;
    }

    // 기능 : 초대 목록 확인
    // 받는 데이터 : 없음
    // 보낼 데이터 : invite_list(num,inviter,target)
    public Dto<Object, Object> invite_list(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 사용자가 그룹에 소속되지 않았다면
        if (me.getOrganization() == null) {

            // 사용자가 타겟인 초대 받은 내역 불러오기
            List<invite_model> list = invite_service.select("target", me.getId());
            // 보낼 데이터 형식 : invite_list
            res.put("invite_list", list);
            // 완료
            dto.setMsg("초대 받은 내역을 불러옵니다");

        }
        // 사용자가 그룹에 소속된 상태라면
        else {

            // 사용자가 초대자인 초대 내역 불러오기
            List<invite_model> list = invite_service.select("inviter", me.getId());
            // 보낼 데이터 형식 : invite_list
            res.put("invite_list", list);
            // 완료
            dto.setMsg("초대한 내역을 불러옵니다");

        }


        // 완료
        dto.setRes_data(res);
        return dto;
    }

    // 기능 : 초대 삭제
    // 받을 데이터 : id_data(삭제할 초대 목록의 분류번호)
    // 보낼 데이터 : 없음
    public Dto<Object, Object> cancel(Dto<Object, Object> dto, HttpServletRequest request) {
        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 삭제할 초대 목록의 분류번호
        String data_id = dto.getId_data();
        // 삭제할 초대 내역
        invite_model model = invite_service.select("num", data_id).get(0);
        // 사용자의 아이디
        String id = me.getId();
        // 초대 내역의 초대자
        String inviter = model.getInviter();
        // 초대 내역의 타겟
        String target = model.getTarget();

        // 초대 내역의 타겟과 초대자중 어느 하나라도 일치한다면
        if (id.equals(target) || id.equals(inviter)) {
            // 초대 목록을 삭제
            invite_service.delete(data_id);
            dto.setMsg("초대 하거나 초대 받은 해당 내역을 삭제하였습니다");
        }
        // 잘못된 접근시
        else {
            dto.setMsg("잘못된 접근입니다");
        }
        return dto;
    }

    // 기능 : 초대승락
    // 받을 데이터 : id_data(수락할 초대 목록의 분류번호)
    // 보낼 데이터 : 없음
    public Dto<Object, Object> accept(Dto<Object, Object> dto, HttpServletRequest request) {
        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 승락할 초대 목록 분류번호
        String data_id = dto.getId_data();
        // 유저가 초대받은 내역
        invite_model I_model = invite_service.select("num", data_id).get(0);


        // 사용자의 아이디
        String id = me.getId();
        // 사용자의 그룹
        String group = me.getOrganization();
        // 초대 내역의 초대자
        String inviter = I_model.getInviter();
        // 초대 내역의 타겟 아이디
        String target = I_model.getTarget();

        // 초대자가 실존하는지
        user_model inviter_model = user_service.findById(inviter).get();
        // 초대자의 실제 그룹
        String inviter_group = inviter_model.getOrganization();

        // 사용자가 초대 받은게 맞고, 사용자가 속한 그룹이 없고, 초대자가 그룹에 속해있을때
        if (id.equals(target) && group == null && inviter_group != null) {

            // 사용자를 그룹에 등록
            user_service.update(id, "organization", inviter_group);
            // 초대내역 삭제
            invite_service.delete(data_id);
            // 완료
            dto.setMsg(inviter_group + " 그룹에 소속되었습니다");
        }
        // 그렇지 않은 경우
        else {
            // 완료
            dto.setMsg("이미 그룹에 소속되어 있거나 초대자가 그룹에 속해있지 않습니다");
        }
        return dto;
    }

    // 기능 : 그룹 나가기
    // 받을 데이터 : 없음
    // 보낼 데이터 : 없음
    public Dto<Object, Object> delete(Dto<Object, Object> dto, HttpServletRequest request) {
        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.findById(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 아이디
        String id = me.getId();

        // 유저의 그룹정보 삭제
        user_service.update(id, "organization", null);

        // 완료
        dto.setMsg("조직 탈퇴 완료");
        dto.setRedirect("/");
        return dto;
    }
}
