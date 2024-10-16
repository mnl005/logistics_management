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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 사용자가 입력한 그룹 이름
        String group_name = req.getV1();

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 사용자의 정보
            user_model user = me.get();

            // 사용자가 속한 그룹이 없는지
            boolean bool1 = Objects.isNull(user.getOrganization());
            // 사용자가 입력한 그룹 이름과 동일한 이름을 가진 그룹이 없는지
            boolean bool2 = user_service.select("organization", group_name).isEmpty();

            // 사용자가 속한 그룹이 없고, 사용자가 입력한 그룹 이름이 사용 가능할 때 사용자의 그룹 정보를 변경
            if (bool1 && bool2) {
                // 완료
                user_service.update(user.getId(), "organization", group_name);
                dto.setMsg(group_name + " 그룹 생성 완료");
            }
            // 사용자가 이미 그룹에 속해 있다면
            else if (!bool1) {
                // 완료
                dto.setMsg("이미 " + user.getOrganization() + " 그룹에 속해 있습니다");
            }
            // 사용자가 입력한 그룹 이름이 중복된다면
            else {
                // 완료
                dto.setMsg(group_name + " 은 이미 존재하는 그룹 이름입니다");
            }
        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("인증실패");
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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 사용자의 정보
            user_model user = me.get();

            // 사용자가 그룹에 소속되어있지 않을때
            if (Objects.isNull(user.getOrganization())) {
                // 완료
                dto.setMsg("그룹에 소속되지 않았습니다");
                return dto;
            }
            //사용자가 그룹에 소속되어 있을때
            else {
                // 같은 그룹원의 정보 불러오기
                List<Object> list = user_service.select("organization", user.getOrganization())
                        .stream()
                        .map(users -> {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", users.getId());
                            map.put("profile", users.getProfile());
                            return map;
                        })
                        .collect(Collectors.toList());

                // 그룹 정보 불러오기
                HashMap<String,String> map = new HashMap<>();
                map.put("group_name",user.getOrganization());
                map.put("group_size",Integer.toString(list.size()));

                // 보낼 데이터 형식 : group_info
                res.put("group_info",map );
                // 보낼 데이터 형식 : group_users
                res.put("group_users", list);

                // 완료
                dto.setRes_data(res);
                dto.setMsg("나의 그룹정보 조회 완료");

            }
        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("인증실패");
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
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 사용자가 입력한 아이디
            String find_id = dto.getId_data();

            // 사용자가 입력한 아이디에 해당하는 유저의 정보
            Optional<user_model> find_user = user_service.findById(find_id);

            // 해당 아이디를 가진 유저가 존재한다면
            if (find_user.isPresent()) {
                // 찾는 유저의 그룹명
                String find_organization = find_user.get().getOrganization();
                // 사용자의 그룹명
                String me_organization = me.get().getOrganization();

                // 나와 찾는 유저의 그룹명이 일치한다면
                if (find_organization.equals(me_organization)) {
                    // 보낼 데이터 형식 : user_info
                    res.put("user_info", find_user.get());
                    // 완료
                    dto.setRes_data(res);
                    dto.setMsg("그룹원 조회 완료");
                } else {
                    // 완료
                    dto.setMsg("그룹원 조회 실패");
                }
            }
            // 해당 아이디를 가진 유저가 존재하지 않다면
            else {
                // 완료
                dto.setMsg("그룹원 조회 실패");
            }

        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("인증실패");
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
        Object req = dto.getReq_data();
        // jwt 토큰 검증 및 유저 정보 가져오기
        Optional<user_model> me = jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById);
        // 초대할 유저
        String target = dto.getReq_data().getV1();

        // 인증 정보가 유효하다면
        if (me.isPresent()) {

            // 초대할 유저의 정보
            Optional<user_model> find_user = user_service.findById(target);

            // 사용자가 조직에 소속되지 않았다면
            if (me.get().getOrganization() == null) {
                // 완료
                dto.setMsg("당신은 그룹에 소속되어 있지 않습니다");
            }
            // 해당 유저가 존재하지 않는다면
            else if (find_user.isEmpty()) {
                // 완료
                dto.setMsg("해당 유저가 존재하지 않습니다");
            }
            // 해당 유저가 이미 조직에 소속되었다면
            else if (!(find_user.get().getOrganization() == null)) {
                // 완료
                dto.setMsg("해당 유저가 이미 그룹에 소속되어 있습니다");
            }
            // 초대하기
            else {
                invite_service.insert(me.get().getId(), target);
                // 완료
                dto.setMsg("초대 완료");
            }
        }
        // 인증 정보가 유효하지 않다면
        else {
            // 완료
            dto.setMsg("인증실패");
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

        // jwt 토큰 검증 및 유저 정보 가져오기
        jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById)
                .map(me -> {

                    if (me.getOrganization() == null) {
                        // 보낼 데이터 형식 : invite_list
                        res.put("invite_list", invite_service.select("target", me.getId()));
                        dto.setMsg("초대 받은 내역을 불러옵니다");
                    } else {
                        // 보낼 데이터 형식 : invite_list
                        res.put("invite_list", invite_service.select("inviter", me.getId()));
                        dto.setMsg("초대한 내역을 불러옵니다");
                    }
                    return 1;
                })
                .orElseGet(() -> {
                    // 완료
                    dto.setMsg("실패");
                    return null;
                });
        // 완료
        dto.setReq_data(res);
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

        // 삭제할 초대 목록
        String data_id = dto.getId_data();

        // jwt 토큰 검증 및 유저 정보 가져오기
        jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById)
                .map(me -> {
                    invite_model model = invite_service.select("num", data_id).get(0);

                    // 유저의 아이디와 초대목록의 초대 대상이나 초대한 대상이 일치하다면(유저와 관련이 있다면)
                    if (me.getId().equals(model.getTarget()) || me.getId().equals(model.getInviter())) {
                        // 초대 목록을 삭제
                        invite_service.delete(data_id);
                        dto.setMsg("초대 하거나 초대 받은 해당 내역을 삭제하였습니다");
                    } else {
                        dto.setMsg("잘못된 접근입니다");
                    }
                    return 1;
                })
                .orElseGet(() -> {
                    // 완료
                    dto.setMsg("인증실패");
                    return null;
                });
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
        // 승락할 초대 목록 분류번호
        String data_id = dto.getId_data();

        jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById)
                .map(me -> {
                    // 유저가 초대받은 내역을 불러온다
                    invite_model I_model = invite_service.select("num", data_id).get(0);
                    // 초대자의 아이디를 불러온다
                    String inviter = I_model.getInviter();
                    // 불러온 아이디로 초대자의 정보를 불러온다
                    user_model U_model = user_service.findById(inviter).get();

                    // 유저가 초대 받은게 맞고, 유저가 속한 그룹이 없고, 초대자가 그룹에 속해있을때
                    if (me.getId().equals(I_model.getTarget()) && me.getOrganization() == null && U_model.getOrganization() != null) {
                        // 유저를 그룹에 등록
                        user_service.update(me.getId(), "organization", U_model.getOrganization());
                        // 초대내역 삭제
                        invite_service.delete(data_id);
                        // 완료
                        dto.setMsg(U_model.getOrganization() + " 그룹에 소속되었습니다");
                    }
                    // 그렇지 않은 경우
                    else {
                        dto.setMsg("이미 그룹에 소속되어 있거나 초대자가 그룹에 속해있지 않습니다");
                    }
                    return 1;
                })
                .orElseGet(() -> {
                    // 완료
                    dto.setMsg("인증실패");
                    return null;
                });
        // 완료
        dto.setReq_data(res);
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

        jwt_service.validations(jwt_service.request_get_token(request))
                .flatMap(user_service::findById)
                .map(me -> {
                    // 완료
                    user_service.update(me.getId(), "organization", null);
                    dto.setMsg("조직 탈퇴 완료");
                    return 1;
                })
                .orElseGet(() -> {
                    // 완료
                    dto.setMsg("인증실패");
                    return null;
                });
        dto.setReq_data(res);
        return dto;
    }
}
