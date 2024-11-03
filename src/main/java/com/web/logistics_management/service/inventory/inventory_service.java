package com.web.logistics_management.service.inventory;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.web.logistics_management.service.inventory.InventoryStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class inventory_service {

    private final inventory_interface crud;

    // 전체 조회
    public List<HashMap<String, String>> select_all(String organization) {
        try {
            List<inventory_model> inventories = crud.findByOrganization(organization);
            List<HashMap<String, String>> result = new ArrayList<>();

            for (inventory_model inventory : inventories) {
                HashMap<String, String> map = new HashMap<>();
                map.put("num", inventory.getNum().toString());
                map.put("organization", inventory.getOrganization());
                map.put("location_code", inventory.getLocation_code());
                map.put("item_code", inventory.getItem_code());
                map.put("quantity", inventory.getQuantity().toString());
                map.put("updated_date", inventory.getUpdated_date());
                map.put("status", inventory.getStatus().name()); // Enum을 문자열로 변환
                result.add(map);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("물류 데이터 전체 조회 실패", e);
        }
    }

    // num 으로 조회
    public inventory_model select_num(Integer num) {
        try {
            return crud.findByNum(num);
        } catch (Exception e) {
            throw new RuntimeException("해당 인벤토리 정보가 존재하지 않습니다");
        }
    }

    // 조건 조회
    public List<HashMap<String, String>> select(String organization, String field, String value) {
        try {
            List<inventory_model> inventories = switch (field) {
                case "location_code" -> crud.findByOrganizationAndLocationCode(organization, value);
                case "item_code" -> crud.findByOrganizaitonAndItemCode(organization, value);
                case "quantity" -> crud.findByOrganizationAndQuantity(organization, Integer.valueOf(value));
                case "status" -> crud.findByOrganizationAndStatus(organization, InventoryStatus.valueOf(value));
                default -> throw new IllegalArgumentException("잘못된 필드: " + field);
            };

            List<HashMap<String, String>> result = new ArrayList<>();

            for (inventory_model inventory : inventories) {
                HashMap<String, String> map = new HashMap<>();
                map.put("num", inventory.getNum().toString());
                map.put("organization", inventory.getOrganization());
                map.put("location_code", inventory.getLocation_code());
                map.put("item_code", inventory.getItem_code());
                map.put("quantity", inventory.getQuantity().toString());
                map.put("updated_date", inventory.getUpdated_date());
                map.put("status", inventory.getStatus().name());
                result.add(map);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("일치하는 데이터가 없습니다");
        }
    }


    // 삽입
    public inventory_model insert(inventory_model inventory) {
        try {
            return crud.saveAndFlush(inventory);
        } catch (Exception e) {
            throw new RuntimeException("이미 존재하는 정보입니다");
        }
    }

    // 삭제
    @Transactional
    public inventory_model update(String organization, String location_code, String item_code, String field, String value) {
        try {
            Optional<inventory_model> op = crud.findByOrganizationAndLocationCodeAndItemCode(organization, location_code, item_code);
            if (op.isPresent()) {
                inventory_model model = op.get();

                switch (field) {
                    case "quantity":
                        model.setQuantity(Integer.valueOf(value));
                        break;
                    case "status":
                        switch (value) {
                            case "NORMAL" -> model.setStatus(InventoryStatus.NORMAL);
                            case "DISCREPANCY" -> model.setStatus(InventoryStatus.DISCREPANCY);
                            case "DAMAGED" -> model.setStatus(InventoryStatus.DAMAGED);
                        }
                        break;
                    default:
                        break;
                }
                return crud.saveAndFlush(model);
            } else {
                throw new Error("해당 정보가 존재하지 않습니다");
            }

        } catch (Exception e) {
            throw new RuntimeException("물류 데이터 업데이트 실패");
        }
    }

    // 삭제
    @Transactional
    public void delete(String organization, String location_code, String item_code) {
        try {
            crud.delete(organization,location_code,item_code);
        } catch (Exception e) {
            throw new RuntimeException("삭제 실패");
        }
    }


}
