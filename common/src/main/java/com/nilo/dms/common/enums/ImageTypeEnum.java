package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum ImageTypeEnum implements EnumMessage{
    ABNORMAL("abnormal", "Abnormal"),
    RECEIVE("receive", "receive"),
    ;
    private String code;
    private String desc;

    private static Map<String, ImageTypeEnum> map = new HashMap<String, ImageTypeEnum>(10);

    static {
        ImageTypeEnum[] enums = ImageTypeEnum.values();
        for (ImageTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private ImageTypeEnum(String code, String desc) {
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

    public static ImageTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
