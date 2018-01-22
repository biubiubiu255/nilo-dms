package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum DelayStatusEnum implements EnumMessage{

    CREATE(1, "Create"),
    DETAIN(2, "Detain"),
    CANCELED(3, "Canceled"),
    COMPLETE(4, "Complete"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, DelayStatusEnum> map = new HashMap<Integer, DelayStatusEnum>(20);

    static {
        DelayStatusEnum[] enums = DelayStatusEnum.values();
        for (DelayStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("DelayStatusEnum", map);

    }

    private DelayStatusEnum(Integer code, String desc) {
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

    public static DelayStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

}
