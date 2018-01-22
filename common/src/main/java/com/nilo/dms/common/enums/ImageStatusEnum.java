package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum ImageStatusEnum implements EnumMessage{

    NORMAL(1, "Normal"),
    DELETE(2, "Delete"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, ImageStatusEnum> map = new HashMap<Integer, ImageStatusEnum>(20);

    static {
        ImageStatusEnum[] enums = ImageStatusEnum.values();
        for (ImageStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private ImageStatusEnum(Integer code, String desc) {
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

    public static ImageStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
