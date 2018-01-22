package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum SMSSendStatusEnum implements EnumMessage {
    SUCCESS(1, "Success"),
    FAILED(2, "Failed");
    private Integer code;
    private String desc;

    private static Map<Integer, SMSSendStatusEnum> map = new HashMap<Integer, SMSSendStatusEnum>(10);

    static {
        SMSSendStatusEnum[] enums = SMSSendStatusEnum.values();
        for (SMSSendStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private SMSSendStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SMSSendStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }


}
