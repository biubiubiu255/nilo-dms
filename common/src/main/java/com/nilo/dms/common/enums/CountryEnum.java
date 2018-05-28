package com.nilo.dms.common.enums;

import com.nilo.dms.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ronny on 2017/9/19.
 */
public enum CountryEnum implements EnumMessage {
    Kenya("Kenya", "KE", "KES", "KSh", "+03:00", "254"),
    Nigeria("Nigeria", "NG", "NGN", "₦", "+01:00", "234"),
    Uganda("Uganda", "UG", "UGX", "UGX", "+03:00", "256"),
    China("China", "CN", "CNY", "¥", "+08:00", "86"),;

    private String desc;
    private String code;
    private String currencyCode;
    private String currencyUnit;
    private String timeZoneCode;
    private String areaCode;

    private CountryEnum(String desc, String code, String currencyCode, String currencyUnit, String timeZoneCode, String areaCode) {
        this.desc = desc;
        this.code = code;
        this.currencyCode = currencyCode;
        this.currencyUnit = currencyUnit;
        this.timeZoneCode = timeZoneCode;
        this.areaCode = areaCode;
    }

    private static Map<String, CountryEnum> map = new HashMap<String, CountryEnum>(10);

    static {
        CountryEnum[] enums = CountryEnum.values();
        for (CountryEnum e : enums) {
            map.put(e.getCode(), e);
        }
        enumMaps.put("CountryEnum", map);

    }

    public static CountryEnum getEnum(String code) {
        return map.get(code);
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getCurrencyUnit() {
        return this.currencyUnit;
    }

    public String getTimeZoneCode() {
        return this.timeZoneCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }
}
