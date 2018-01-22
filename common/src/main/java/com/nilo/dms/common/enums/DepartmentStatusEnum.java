package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum DepartmentStatusEnum implements  EnumMessage{
    NORMAL(1, "Normal"),
    DISABLED(2, "Disabled");
    private Integer code;
    private String desc;

    private static Map<Integer, DepartmentStatusEnum> map = new HashMap<Integer, DepartmentStatusEnum>(10);

    static {
        DepartmentStatusEnum[] enums = DepartmentStatusEnum.values();
        for (DepartmentStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DepartmentStatusEnum(Integer code, String desc) {
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

    public static DepartmentStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
