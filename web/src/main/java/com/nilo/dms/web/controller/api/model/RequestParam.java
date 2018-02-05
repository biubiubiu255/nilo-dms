package com.nilo.dms.web.controller.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.service.system.model.MerchantConfig;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by admin on 2017/9/28.
 */
public class RequestParam {

    private String data;

    private String sign;

    private String app_key;

    private Long timestamp;

    private String method;

    private String app_id;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public MethodEnum getMethod() {
        return MethodEnum.getEnum(this.method);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void checkParam() {
        AssertUtil.isNotBlank(app_key, BizErrorCode.MERCHANT_ID_EMPTY);
        AssertUtil.isNotBlank(data, BizErrorCode.DATA_EMPTY);
        AssertUtil.isNotBlank(sign, BizErrorCode.SIGN_EMPTY);
        AssertUtil.isNotBlank(method, BizErrorCode.Method_EMPTY);
        AssertUtil.isNotNull(timestamp, BizErrorCode.TIMESTAMP_EMPTY);
        AssertUtil.isNotNull(getMethod(), BizErrorCode.METHOD_NOT_EXIST);

        //时间戳 10分钟内
        Long currentTime = DateUtil.getSysTimeStamp();
        Long diff = Math.abs(currentTime - this.timestamp);
        if (diff > 10 * 60) {
            throw new DMSException(BizErrorCode.TIMESTAMP_ERROR);
        }

        //校验sign
        MerchantConfig merchantConfig = SystemConfig.getMerchantConfigByCode(this.app_key);
        if(merchantConfig== null) throw new DMSException(BizErrorCode.APP_KEY_NOT_EXIST,this.app_key);
        this.app_id = merchantConfig.getMerchantId();
        boolean check = checkSign(merchantConfig.getKey(), data, sign);
        AssertUtil.isTrue(check, BizErrorCode.SING_ERROR);

    }

    private boolean checkSign(String key, String data, String sign) {
        String checkSign = DigestUtils.md5Hex(key + data + key);
        return StringUtil.equalsIgnoreCase(checkSign, sign);
    }
}
