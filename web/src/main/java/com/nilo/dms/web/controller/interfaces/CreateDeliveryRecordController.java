package com.nilo.dms.web.controller.interfaces;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.CreateDeliveryRequestStatusEnum;
import com.nilo.dms.common.enums.NotifyStatusEnum;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.DeliveryOrderRequestDao;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderRequestDO;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.system.NotifyService;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/interface/createDelivery")
public class CreateDeliveryRecordController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotifyDao notifyDao;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private DeliveryOrderRequestDao orderRequestDao;

    @Autowired
    private NotifyService notifyService;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "interface/create_delivery/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getUserList(String orderNo, String data, String sign) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination pagination = getPage();

        List<DeliveryOrderRequestDO> requestDOList = orderRequestDao.queryBy(Long.parseLong(merchantId), orderNo, data, sign, pagination.getOffset(), pagination.getLimit());
        pagination.setTotalCount(commonDao.lastFoundRows());

        List<CreateDelivery> list = new ArrayList<>();

        if (requestDOList != null) {
            for (DeliveryOrderRequestDO requestDO : requestDOList) {

                CreateDelivery d = new CreateDelivery();
                d.setOrderNo(requestDO.getOrderNo());
                d.setSign(requestDO.getSign());
                d.setData(requestDO.getData());
                d.setCreateStatus(CreateDeliveryRequestStatusEnum.getEnum(requestDO.getStatus()).getDesc());
                d.setCreateMsg(requestDO.getMsg());
                d.setCreatedTime(requestDO.getCreatedTime());
                List<NotifyDO> notifyList = notifyDao.findBy(Long.parseLong(merchantId), requestDO.getOrderNo(), "create_order_notify", null, pagination.getOffset(), pagination.getLimit());

                if (notifyList != null && notifyList.size() > 0) {
                    NotifyDO notifyDO = notifyList.get(0);
                    d.setNotifyStatus(NotifyStatusEnum.getEnum(notifyDO.getStatus()).getDesc());
                    d.setNotifyMsg(notifyDO.getResult());
                    d.setNotifyTime(notifyDO.getUpdatedTime());
                }
                list.add(d);
            }
        }
        return toPaginationLayUIData(pagination, list);
    }

    @ResponseBody
    @RequestMapping(value = "/retryNotify.html")
    public String retryNotify(String notifyId) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            notifyService.retryNotify(merchantId, notifyId);
        } catch (Exception e) {
            log.error("retryNotify failed. notifyId:{}", notifyId, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    private static class CreateDelivery {
        private String orderNo;

        private String referenceNo;

        private String createStatus;

        private String notifyStatus;

        private String sign;

        private String data;

        private String createMsg;

        private String notifyMsg;

        private Long createdTime;

        private Long notifyTime;

        public Long getNotifyTime() {
            return notifyTime;
        }

        public void setNotifyTime(Long notifyTime) {
            this.notifyTime = notifyTime;
        }

        public Long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Long createdTime) {
            this.createdTime = createdTime;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getReferenceNo() {
            return referenceNo;
        }

        public void setReferenceNo(String referenceNo) {
            this.referenceNo = referenceNo;
        }

        public String getCreateStatus() {
            return createStatus;
        }

        public void setCreateStatus(String createStatus) {
            this.createStatus = createStatus;
        }

        public String getNotifyStatus() {
            return notifyStatus;
        }

        public void setNotifyStatus(String notifyStatus) {
            this.notifyStatus = notifyStatus;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getCreateMsg() {
            return createMsg;
        }

        public void setCreateMsg(String createMsg) {
            this.createMsg = createMsg;
        }

        public String getNotifyMsg() {
            return notifyMsg;
        }

        public void setNotifyMsg(String notifyMsg) {
            this.notifyMsg = notifyMsg;
        }
    }
}
