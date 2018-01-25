package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum AbnormalOrderStatusEnum implements EnumMessage{

    CREATE(1, "Pending"),
    CANCELED(2, "Canceled"),
    COMPLETE(3, "Complete"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, AbnormalOrderStatusEnum> map = new HashMap<Integer, AbnormalOrderStatusEnum>(20);

    static {
        AbnormalOrderStatusEnum[] enums = AbnormalOrderStatusEnum.values();
        for (AbnormalOrderStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("AbnormalOrderStatusEnum", map);

    }

    private AbnormalOrderStatusEnum(Integer code, String desc) {
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

    public static AbnormalOrderStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

}
