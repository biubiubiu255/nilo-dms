package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum TaskTypeEnum implements EnumMessage{
    PICK_UP("pick_up", "pick_up."),
    SELF_DELIVERY("self_delivery", "self_delivery."),
    DISPATCH("dispatch", "DISPATCH."),
    SEND("send", "Send"),
    ;
    private String code;
    private String desc;

    private static Map<String, TaskTypeEnum> map = new HashMap<String, TaskTypeEnum>(20);

    static {
        TaskTypeEnum[] enums = TaskTypeEnum.values();
        for (TaskTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private TaskTypeEnum(String code, String desc) {
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

    public static TaskTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
