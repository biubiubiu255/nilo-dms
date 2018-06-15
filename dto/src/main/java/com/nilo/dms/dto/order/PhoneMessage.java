package com.nilo.dms.dto.order;

/**
 * Created by admin on 2017/10/18.
 */
public class PhoneMessage {

    private String msgType;

    private String phoneNum;

    private String waybill;

    private String content;

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PhoneMessage{" +
                "msgType='" + msgType + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
