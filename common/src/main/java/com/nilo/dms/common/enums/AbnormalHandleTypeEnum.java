package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum AbnormalHandleTypeEnum implements EnumMessage {
    RETURN("RT", "return"),
    RESEND("RS", "resend"),;
    private String code;
    private String desc;

    private static Map<String, AbnormalHandleTypeEnum> map = new HashMap<String, AbnormalHandleTypeEnum>(10);

    static {
        AbnormalHandleTypeEnum[] enums = AbnormalHandleTypeEnum.values();
        for (AbnormalHandleTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("AbnormalHandleTypeEnum", map);
    }

    private AbnormalHandleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static AbnormalHandleTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
