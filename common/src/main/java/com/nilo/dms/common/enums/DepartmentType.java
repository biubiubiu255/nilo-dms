package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/10/17.
 */
public enum DepartmentType implements EnumMessage {

    Allocating_Center_One("allocating_center_1", "allocating_center_1"),;

    private String code;
    private String desc;

    private static Map<String, DepartmentType> map = new HashMap<String, DepartmentType>(10);

    static {
        DepartmentType[] enums = DepartmentType.values();
        for (DepartmentType e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DepartmentType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static DepartmentType getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }

}
