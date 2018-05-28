package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum CustomerTypeEnum implements EnumMessage {

    PERSON("person", "Person"),
    EB("eb", "Electronic Business"),
    NOT_EB("no_eb", "Not Electronic Business"),;

    private String code;
    private String desc;

    private static Map<String, CustomerTypeEnum> map = new HashMap<String, CustomerTypeEnum>(10);

    static {
        CustomerTypeEnum[] enums = CustomerTypeEnum.values();
        for (CustomerTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("CustomerTypeEnum", map);

    }

    private CustomerTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static CustomerTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
