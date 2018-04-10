package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum HandleRiderStatusEnum implements EnumMessage{
    SAVA(0, "仅保存"),
    SHIP(1, "已经发运"),;
    private Integer code;
    private String desc;

    private static Map<Integer, HandleRiderStatusEnum> map = new HashMap<Integer, HandleRiderStatusEnum>(10);

    static {
        HandleRiderStatusEnum[] enums = HandleRiderStatusEnum.values();
        for (HandleRiderStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private HandleRiderStatusEnum(Integer code, String desc) {
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

    public static HandleRiderStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
