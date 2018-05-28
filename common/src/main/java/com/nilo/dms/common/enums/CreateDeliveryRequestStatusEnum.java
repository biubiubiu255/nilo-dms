package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum CreateDeliveryRequestStatusEnum implements EnumMessage{

    CREATE(1, "Create"),
    SUCCESS(2, "Success"),
    FAILED(3, "Failed"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, CreateDeliveryRequestStatusEnum> map = new HashMap<Integer, CreateDeliveryRequestStatusEnum>(20);

    static {
        CreateDeliveryRequestStatusEnum[] enums = CreateDeliveryRequestStatusEnum.values();
        for (CreateDeliveryRequestStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }

        enumMaps.put("CreateDeliveryRequestStatusEnum", map);
    }

    private CreateDeliveryRequestStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static CreateDeliveryRequestStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

}
