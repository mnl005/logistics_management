package com.web.logistics_management.function.group;

import com.web.logistics_management.immutable.Dto;
import com.web.logistics_management.immutable.model;
import com.web.logistics_management.service.group.group_model;
import com.web.logistics_management.service.inviter.invite_model;
import com.web.logistics_management.service.inviter.invite_service;
import com.web.logistics_management.service.user.user_service;
import com.web.logistics_management.service.jwt_service;
import com.web.logistics_management.service.group.group_service;
import com.web.logistics_management.service.user_group.user_group_service;
import com.web.logistics_management.service.group.group_model;

import com.web.logistics_management.service.user.user_model;
import com.web.logistics_management.service.user_group.user_organization_model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final group_service group_service;
    private final invite_service invite_service;
    private final user_group_service user_group_service;
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
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();
        // 사용자가 입력한 그룹 이름
        String group_name = req.getV1();


        // 사용자가 입력한 그룹 이름으로 그룹 조회
        List<group_model> group_list = group_service.select("organization", group_name);
        // 사용자가 입력한 그룹 이름과 동일한 이름을 가진 그룹이 없는지
        boolean bool = group_list.isEmpty();

        // 사용자가 입력한 그룹 이름이 사용 가능할 때
        if (bool) {
            // 그룹 정보 생성
            group_model model = new group_model();
            model.setMaster(id);
            model.setOrganization(group_name);
            // 사용자 그룹 정보 변경
            group_service.insert(model);
            // 완료
            dto.setMsg(group_name + " 그룹 생성 완료");
        }
        // 사용자가 입력한 그룹 이름이 이미 사용 중이라면
        else {
            // 완료
            dto.setMsg(group_name + " 은 이미 존재하는 그룹 이름입니다");
        }
        return dto;
    }

    // 기능 : 사용자의 소속 그룹들
    // 받는 데이터 : 없음
    // 보낼 데이터 : group_list(organization)
    public Dto<Object, Object> me(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();


        // 사용자가 소속되어 있는 정보
        List<user_organization_model> groups = user_group_service.select_id(id);

        // 사용자가 그룹에 소속되어있지 않을때
        if (groups.isEmpty()) {
            // 완료
            dto.setMsg("그룹에 소속되지 않았습니다");
            return dto;
        }
        //사용자가 그룹에 소속되어 있을때
        else {
            // 그룹 이름만 추출
            List<Object> group_list = groups
                    .stream()
                    .map(group -> {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("organization", group.getId().getOrganization());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 보낼 데이터 형식 : group_list
            res.put("group_list", group_list);
            // 완료
            dto.setRes_data(res);
            dto.setMsg("소속된 그룹의 개수는 " + group_list.size() + " 개 입니다");
        }


        return dto;
    }


    // 기능 : 소속된 그룹중 한가지 선택해 접속
    // 받는 데이터 : id_data(그룹 이름)
    // 보낼 데이터 : group_info(group_name,group_size,group_master)-(그룹이름, 그룹크기, 그룹관리자),  group_users(그룹구성원(id,profile))
    public Dto<Object, Object> connect(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();
        // 사용자가 접속을 선택한 그룹 이름
        String group_name = dto.getId_data();

        // 사용자가 속한 그룹
        user_organization_model group = user_group_service.select(id, group_name);

        // 사용자가 그룹에 소속되어있지 않을때
        if (Objects.isNull(group)) {
            // 완료
            dto.setMsg(group_name + " 그룹에 소속되지 않았습니다");
            return dto;
        }
        //사용자가 그룹에 소속되어 있을때
        else {
            // 같은 그룹원의 정보 불러오기
            List<user_organization_model> group_list = user_group_service.select_organization(group_name);
            List<Object> list = group_list
                    .stream()
                    .map(in -> {
                        HashMap<String, String> map = new HashMap<>();
                        String in_id = in.getId().getId();
                        String profile = user_service.Op_id(in_id).get().getProfile();
                        map.put("id", in_id);
                        map.put("profile", profile);
                        return map;
                    })
                    .collect(Collectors.toList());

            // 그룹 관리자 정보
            List<group_model> group_model = group_service.select("organization", group_name);
            String master = group_model.get(0).getMaster();

            // 그룹이름, 그룹크기, 그룹관리자
            HashMap<String, String> map = new HashMap<>();
            map.put("group_name", group_name);
            map.put("group_size", Integer.toString(list.size()));
            map.put("group_master", master);

            // 보낼 데이터 형식 : group_info
            res.put("group_info", map);
            // 보낼 데이터 형식 : group_users
            res.put("group_users", list);

            // 선택한 그룹에 접속
            jwt_service.access_groups(group_name, response);

            // 완료
            dto.setRes_data(res);
            dto.setMsg(group_name + " 그룹에 접속되었습니다");

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
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        String me_gorup = connect.getId().getOrganization();


        // 사용자가 입력한 아이디
        String find_id = dto.getId_data();

        // 사용자가 찾는 유저와 내가 접속한 그룹이 일치하는지
        Optional<user_organization_model> Op = user_group_service.OpIdOrganization(find_id, me_gorup);

        // 사용자가 찾는 유저와 사용자가 접속한 그룹이 일치한다면
        if(Op.isPresent()){
            // 유저 정보 찾기
            user_model find_user = user_service.Op_id(find_id).get();
            // 보낼 데이터 형식 : user_info
            res.put("user_info", find_user);
            // 완료
            dto.setRes_data(res);
            dto.setMsg("그룹원 조회 완료");
        }else{
            // 완료
            dto.setMsg("그룹원 조회 실패");
        }

        return dto;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 기능 : 그룹원으로 초대하기
    // 받는 데이터 : v1(초대할 유저 아이디)
    // 보낼 데이터 : 없음
    public Dto<model, Object> invite(Dto<model, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        model req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자 아이디
        String id = me.getId();
        // 그룹 접속정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        // 접속중인 그룹 정보
        String me_gorup = connect.getId().getOrganization();


        // 초대할 유저
        String target = req.getV1();

        // 초대자의 권한 확인
        Optional<group_model> Op = group_service.Op_group(me_gorup, id);

        // 초대할 유저의 정보
        user_organization_model model = user_group_service.select(target, me_gorup);
        boolean bool = Objects.isNull(model);


        // 사용자가 초대할 권한이 없다면
        if (Op.isEmpty()) {
            // 완료
            dto.setMsg("당신은 초대할 권한이 없습니다");
        }
        // 해당 유저가 이미 조직에 소속되었다면
        else if (!bool) {
            // 완료
            dto.setMsg(target + " 유저가 이미 그룹에 소속되어 있습니다");
        }
        // 초대 가능한 상태라면
        else {
            // 초대하기
            invite_model inviteModel = new invite_model();
            inviteModel.setMaster(id);
            inviteModel.setTarget(target);
            inviteModel.setOrganization(me_gorup);
            invite_service.insert(inviteModel);
            // 완료
            dto.setMsg(target + " 님에게 초대를 보냈습니다");
        }


        return dto;
    }

    // 기능 : 초대를 받거나 보낸 리스트 조회
    // 받는 데이터 : 없음
    // 보낼 데이터 : invite_list(num,master,target,organization)
    public Dto<Object, Object> invite_select(Dto<Object, Object> dto, HttpServletRequest request) {

        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();

        // 초대를 받거나 보낸 리스트 조회
        List<invite_model> list = invite_service.masterOrtarget(id, id);
        // 보낼 데이터 형식 : invite_list
        res.put("invite_list", list);
        // 완료
        dto.setMsg(list.size() + " 개의 초대 내역을 불러옵니다");

        // 완료
        dto.setRes_data(res);
        return dto;
    }


    // 기능 : 초대 삭제
    // 받을 데이터 : id_data(삭제할 초대 목록의 분류번호)
    // 보낼 데이터 : invite_list(num,master,target,organization)
    public Dto<Object, Object> cancel(Dto<Object, Object> dto, HttpServletRequest request) {
        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();

        // 삭제할 초대 목록의 분류번호
        String num = dto.getId_data();
        // 삭제할 초대 내역
        invite_model model = invite_service.select("num", num).get(0);
        // 초대 내역의 초대자
        String master = model.getMaster();
        // 초대 내역의 타겟
        String target = model.getTarget();

        // 초대 내역의 타겟과 초대자중 어느 하나라도 일치한다면
        if (id.equals(master) || id.equals(target)) {
            // 초대 목록을 삭제
            invite_service.delete(num);

            // 초대를 받거나 보낸 리스트 조회
            List<invite_model> list = invite_service.masterOrtarget(id, id);
            // 보낼 데이터 형식 : invite_list
            res.put("invite_list", list);
            // 완료
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
    // 보낼 데이터 : invite_list(num,master,target,organization)
    public Dto<Object, Object> accept(Dto<Object, Object> dto, HttpServletRequest request) {
        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();

        // 승락할 초대 목록 분류번호
        String num = dto.getId_data();
        // 유저가 초대받은 내역
        invite_model I_model = invite_service.select("num", num).get(0);


        // 초대 내역의 초대자
        String master = I_model.getMaster();
        // 초대 내역의 타겟 아이디
        String target = I_model.getTarget();
        // 초대 내역의 그룹 이름
        String group = I_model.getOrganization();

        // 초대가 유효한지
        Optional<group_model> Op_gorup = group_service.Op_group(group,master);

        // 초대가 유효하고, 타겟과 나의 아이디가 일치하다면
        if (Op_gorup.isPresent() && target.equals(id)) {
            // 적용할 그룹 정보
            user_organization_model Newmodel = new user_organization_model();
            Newmodel.setIDAndOrganization(target,group);

            // 그룹에 등록
            user_group_service.insert(Newmodel);
            // 초대내역 삭제
            invite_service.delete(num);

            // 초대를 받거나 보낸 리스트 조회
            List<invite_model> list = invite_service.masterOrtarget(id, id);
            // 보낼 데이터 형식 : invite_list
            res.put("invite_list", list);
            // 완료
            dto.setMsg(group + " 그룹에 소속되었습니다");
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
    public Dto<Object, Object> delete(Dto<Object, Object> dto, HttpServletRequest request, HttpServletResponse response) {
        // 보낼 데이터 저장
        HashMap<String, Object> res = new HashMap<>();
        // 받는 데이터
        Object req = dto.getReq_data();
        // 토큰 인증
        user_model me = user_service.Op_id(jwt_service.validations(jwt_service.request_get_token(request))).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 사용자의 아이디
        String id = me.getId();
        // 그룹 접속 정보 인증
        user_organization_model connect = user_group_service.OpIdOrganization(id,jwt_service.validation_group(jwt_service.request_get_group_token(request))).orElseThrow(() -> new RuntimeException("그룹에 접속 후 이용하세요"));
        String me_gorup = connect.getId().getOrganization();



        // 그룹원 목록에서 제거
        user_group_service.delete(id,me_gorup);

        // 사용자가 그룹의 관리자일 경우 그룹 목록에서 그룹을 완전히 삭제
        Optional<group_model> op= group_service.Op_group("connect", id);
        if(op.isPresent()){
            group_service.delete(me_gorup,id);
        }

        // 그룹 접속 종료
        jwt_service.block_group(response);

        // 사용자가 소속되어 있는 정보
        List<user_organization_model> groups = user_group_service.select_id(id);

        // 사용자가 그룹에 소속되어있지 않을때
        if (groups.isEmpty()) {
            // 완료
            dto.setMsg("그룹에 소속되지 않았습니다");
            return dto;
        }
        //사용자가 그룹에 소속되어 있을때
        else {
            // 그룹 이름만 추출
            List<Object> group_list = groups
                    .stream()
                    .map(group -> {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("organization", group.getId().getOrganization());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 보낼 데이터 형식 : group_list
            res.put("group_list", group_list);
            // 완료
            dto.setRes_data(res);
            dto.setMsg("소속된 그룹의 개수는 " + group_list.size() + " 개 입니다");
        }
        return dto;
    }
}
