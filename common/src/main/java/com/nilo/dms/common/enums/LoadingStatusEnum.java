package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum LoadingStatusEnum implements EnumMessage{
    CREATE(1, "Create"),
    LOADING(2, "Loading"),
    SHIP(3, "Ship"),
    CANCEL(4, "Cancel");
    private Integer code;
    private String desc;

    private static Map<Integer, LoadingStatusEnum> map = new HashMap<Integer, LoadingStatusEnum>(10);

    static {
        LoadingStatusEnum[] enums = LoadingStatusEnum.values();
        for (LoadingStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private LoadingStatusEnum(Integer code, String desc) {
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

    public static LoadingStatusEnum getEnum(Integer code) {
        return map.get(code);
    }
    public Map getMap() {
        return map;
    }
}
