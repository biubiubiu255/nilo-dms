package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum MessageStatusEnum {
    CREATE(1, "Create"),
    VIEWED(2, "Viewed");
    private Integer code;
    private String desc;

    private static Map<Integer, MessageStatusEnum> map = new HashMap<Integer, MessageStatusEnum>(10);

    static {
        MessageStatusEnum[] enums = MessageStatusEnum.values();
        for (MessageStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private MessageStatusEnum(Integer code, String desc) {
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

    public static MessageStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

}
