package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum UserStatusEnum implements EnumMessage{
    NORMAL(1, "Normal"),
    DISABLED(2, "Disabled"),
    FROZEN(3, "Frozen");
    private Integer code;
    private String desc;

    private static Map<Integer, UserStatusEnum> map = new HashMap<Integer, UserStatusEnum>(10);

    static {
        UserStatusEnum[] enums = UserStatusEnum.values();
        for (UserStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private UserStatusEnum(Integer code, String desc) {
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

    public static UserStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
