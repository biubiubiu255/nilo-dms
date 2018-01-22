package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum NotifyStatusEnum implements EnumMessage{
    CREATE(0, "Create"),
    SUCCESS(1, "Success"),
    Failed(2, "Failed"),
    RETRY(3, "Retry"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, NotifyStatusEnum> map = new HashMap<Integer, NotifyStatusEnum>(10);

    static {
        NotifyStatusEnum[] enums = NotifyStatusEnum.values();
        for (NotifyStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private NotifyStatusEnum(Integer code, String desc) {
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

    public static NotifyStatusEnum getEnum(Integer code) {
        return map.get(code);
    }
    public Map getMap() {
        return map;
    }

}
