package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/9/15.
 */
public enum TaskStatusEnum implements EnumMessage{

    CREATE(1, "Create"),
    PROCESS(2, "Process"),
    COMPLETE(3, "Complete"),
    ;
    private Integer code;
    private String desc;

    private static Map<Integer, TaskStatusEnum> map = new HashMap<Integer, TaskStatusEnum>(20);

    static {
        TaskStatusEnum[] enums = TaskStatusEnum.values();
        for (TaskStatusEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private TaskStatusEnum(Integer code, String desc) {
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

    public static TaskStatusEnum getEnum(Integer code) {
        return map.get(code);
    }

    public Map getMap() {
        return map;
    }
}
