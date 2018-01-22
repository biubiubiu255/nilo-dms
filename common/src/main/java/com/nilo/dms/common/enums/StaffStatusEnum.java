package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum StaffStatusEnum {
    TRAINEE(1,"Trainee"),
    REGULAR (2, "Regular"),
    RESIGNED(3, "Resigned");
    private Integer code;
    private String desc;

    private static Map<Integer, StaffStatusEnum> map = new HashMap<Integer, StaffStatusEnum>(10);

    static {
        StaffStatusEnum[] enums = StaffStatusEnum.values();
        for (StaffStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private StaffStatusEnum(Integer code, String desc) {
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

    public static StaffStatusEnum getEnum(Integer code) {
        return map.get(code);
    }
    public Map getMap() {
        return map;
    }

}
