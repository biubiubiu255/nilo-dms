package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum AbnormalTypeEnum implements EnumMessage {
    REFUSE("refuse", "Refuse"),
    PROBLEM("problem", "Problem"),;
    private String code;
    private String desc;

    private static Map<String, AbnormalTypeEnum> map = new HashMap<String, AbnormalTypeEnum>(10);

    static {
        AbnormalTypeEnum[] enums = AbnormalTypeEnum.values();
        for (AbnormalTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("AbnormalTypeEnum", map);
    }

    private AbnormalTypeEnum(String code, String desc) {
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

    public static AbnormalTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
