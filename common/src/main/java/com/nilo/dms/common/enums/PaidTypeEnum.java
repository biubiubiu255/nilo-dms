package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;


public enum PaidTypeEnum implements EnumMessage{

	CASH(0, "Cash"),
	ONLINE(1, "Online"),
	ONLINE_PROBLEM(2, "Online Problem"),
   ;

    private Integer code;
    private String desc;

    private static Map<Integer, PaidTypeEnum> map = new HashMap<Integer, PaidTypeEnum>(20);

    static {
        PaidTypeEnum[] enums = PaidTypeEnum.values();
        for (PaidTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private PaidTypeEnum(Integer code, String desc) {
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

    public static PaidTypeEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
