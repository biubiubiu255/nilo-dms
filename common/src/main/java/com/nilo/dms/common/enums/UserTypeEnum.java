package com.nilo.dms.common.enums;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum UserTypeEnum implements EnumMessage{
    NORMAL(0, "normal"),
    ADMIN(1, "admin"),;
    private Integer code;
    private String desc;

    private static Map<Integer, UserTypeEnum> map = new HashMap<Integer, UserTypeEnum>(10);

    static {
        UserTypeEnum[] enums = UserTypeEnum.values();
        for (UserTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("UserTypeEnum", map);
    }

    private UserTypeEnum(Integer code, String desc) {
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

    public static UserTypeEnum getEnum(Integer code) {
        return map.get(code);
    }

}
