package com.nilo.dms.dto.system;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum SocketMessageTypeEnum {
    CONNECT("connect", "connect"),
    MESSAGE("message", "message"),;
    private String code;
    private String value;

    private static Map<String, SocketMessageTypeEnum> map = new HashMap<String, SocketMessageTypeEnum>(10);

    static {
        SocketMessageTypeEnum[] enums = SocketMessageTypeEnum.values();
        for (SocketMessageTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private SocketMessageTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public static SocketMessageTypeEnum getEnum(String code) {
        return map.get(code);
    }
}
