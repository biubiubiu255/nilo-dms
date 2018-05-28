package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum BizFeeConfigStatusEnum implements EnumMessage{

    NORMAL(1, "Normal"),
    DELETE(2, "Delete"),
    HISTORY(3, "history"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, BizFeeConfigStatusEnum> map = new HashMap<Integer, BizFeeConfigStatusEnum>(20);

    static {
        BizFeeConfigStatusEnum[] enums = BizFeeConfigStatusEnum.values();
        for (BizFeeConfigStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("BizFeeConfigStatusEnum", map);

    }

    private BizFeeConfigStatusEnum(Integer code, String desc) {
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

    public static BizFeeConfigStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

}
