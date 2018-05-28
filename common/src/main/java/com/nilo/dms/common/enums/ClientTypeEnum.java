package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum ClientTypeEnum implements EnumMessage{
    API("1", "api"),
    DMS_ADD("2", "dms add"),
    DMS_IMPORT("3", "dms import"),
    ;
    private String code;
    private String desc;

    private static Map<String, ClientTypeEnum> map = new HashMap<String, ClientTypeEnum>(10);

    static {
        ClientTypeEnum[] enums = ClientTypeEnum.values();
        for (ClientTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("ClientTypeEnum", map);

    }

    private ClientTypeEnum(String code, String desc) {
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

    public static ClientTypeEnum getEnum(String code) {
        return map.get(code);
    }

}
