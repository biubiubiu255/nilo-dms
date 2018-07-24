package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum ThirdPushOrderStatusEnum implements EnumMessage{

    CREATE(0, "创建订单"),
    PUSH_SUCC(1, "推送完成"),
    CALL_BACK_SUCC(2, "回调成功"),
    PUSH_FAIL(-1, "推送失败"),
    CALL_BACK_FAIL(-2, "回调失败"),
    SIGN_COMPLATED(-3, "签收成功");

    private Integer code;
    private String desc;

    private static Map<Integer, ThirdPushOrderStatusEnum> map = new HashMap<Integer, ThirdPushOrderStatusEnum>(20);

    static {
        ThirdPushOrderStatusEnum[] enums = ThirdPushOrderStatusEnum.values();
        for (ThirdPushOrderStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private ThirdPushOrderStatusEnum(Integer code, String desc) {
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

    public static ThirdPushOrderStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
