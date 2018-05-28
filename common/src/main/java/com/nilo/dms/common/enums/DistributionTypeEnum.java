package com.nilo.dms.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public enum DistributionTypeEnum implements EnumMessage{
    NETWORK("0", "Network"),
    SELF_COLLECT("1", "Self Collect"),;
    private String code;
    private String desc;

    private static Map<String, DistributionTypeEnum> map = new HashMap<String, DistributionTypeEnum>(10);

    static {
        DistributionTypeEnum[] enums = DistributionTypeEnum.values();
        for (DistributionTypeEnum e : enums) {
            map.put(e.getCode(), e);
        }
    }

    private DistributionTypeEnum(String code, String desc) {
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

    public static DistributionTypeEnum getEnum(String code) {
        return map.get(code);
    }
    public Map getMap() {
        return map;
    }

}
