package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum ScheduleStatusEnum implements EnumMessage{
    NORMAL(1, "Normal"),
    DISABLE(2, "Disable"),;
    private Integer code;
    private String desc;

    private static Map<Integer, ScheduleStatusEnum> map = new HashMap<Integer, ScheduleStatusEnum>(10);

    static {
        ScheduleStatusEnum[] enums = ScheduleStatusEnum.values();
        for (ScheduleStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private ScheduleStatusEnum(Integer code, String desc) {
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

    public static ScheduleStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
