package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum OptTypeEnum implements EnumMessage{
    CANCEL("cancel","Cancel Order"),
    ARRIVE_SCAN("arrive_scan","ArriveScan"),
    ALLOCATE("allocate", "Allocate"),
    GO_TO_PICK_UP("go_to_pick_up", "Go Pickup"),
    PICK_UP("pick_up", "Pickup"),
    PICK_UP_FAILED("pick_up_failed", "Pickup Failed"),
    LOADING("loading", "Loading"),
    LOADING_CANCEL("loading_cancel", "Cancel Loading"),
    DELIVERY("delivery", "Delivery"),
    SEND("send", "Send"),
    DELAY("delay", "Delay"),
    DETAIN("detain", "Detain"),
    SIGN("receive", "Sign"),
    PACKAGE("package", "Package"),
    UNPACK ("unpack", "Unpack"),
    REFUSE("refuse", "Refuse"),
    PROBLEM("problem", "Problem"),
    ;
    private String code;
    private String desc;

    private static Map<String, OptTypeEnum> map = new HashMap<String, OptTypeEnum>(10);

    static {
        OptTypeEnum[] enums = OptTypeEnum.values();
        for (OptTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }

        enumMaps.put("OptTypeEnum",map);
    }

    private OptTypeEnum(String code, String desc) {
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

    public static OptTypeEnum getEnum(String code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
