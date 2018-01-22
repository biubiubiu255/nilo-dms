package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum MerchantStatusEnum implements EnumMessage{
    NORMAL(1, "Normal"),
    DISABLED(2, "Disabled");
    private Integer code;
    private String desc;

    private static Map<Integer, MerchantStatusEnum> map = new HashMap<Integer, MerchantStatusEnum>(10);

    static {
        MerchantStatusEnum[] enums = MerchantStatusEnum.values();
        for (MerchantStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private MerchantStatusEnum(Integer code, String desc) {
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

    public static MerchantStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
