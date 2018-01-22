package com.nilo.dms.web.controller.api.model;

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

    private String merchantId;

    private Long timestamp;

    private String op;

    public OPEnum getOp() {
        return OPEnum.getEnum(this.op);
    }

    public void setOp(String op) {
        this.op = op;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void checkParam() {
        AssertUtil.isNotBlank(merchantId, BizErrorCode.MERCHANT_ID_EMPTY);
        AssertUtil.isNotBlank(data, BizErrorCode.DATA_EMPTY);
        AssertUtil.isNotBlank(sign, BizErrorCode.SIGN_EMPTY);
        AssertUtil.isNotBlank(op, BizErrorCode.OP_EMPTY);
        AssertUtil.isNotNull(timestamp, BizErrorCode.TIMESTAMP_EMPTY);
        AssertUtil.isNotNull(getOp(), BizErrorCode.OP_NOT_EXIST);

        //时间戳 10分钟内
        Long currentTime = DateUtil.getSysTimeStamp();
        Long diff = Math.abs(currentTime - this.timestamp);
        if (diff > 10 * 60) {
            throw new DMSException(BizErrorCode.TIMESTAMP_ERROR);
        }

        //校验sign
        String merchantId = this.merchantId;
        MerchantConfig merchantConfig = SystemConfig.getMerchantConfig(merchantId);
        boolean check = checkSign(merchantConfig.getKey(), data, sign);
        AssertUtil.isTrue(check, BizErrorCode.SING_ERROR);

    }

    private boolean checkSign(String key, String data, String sign) {
        String checkSign = DigestUtils.md5Hex(key + data + key);
        return StringUtil.equalsIgnoreCase(checkSign, sign);
    }
}
