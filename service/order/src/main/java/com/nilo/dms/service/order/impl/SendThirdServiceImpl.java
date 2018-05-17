package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderReceiverDao;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.HandleThirdDao;
import com.nilo.dms.dao.WaybillDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import com.nilo.dms.dao.dataobject.RiderDelivery;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.PhoneMessage;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.SystemConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
@Service
public class SendThirdServiceImpl implements SendThirdService {

    @Autowired
    private HandleThirdDao handleThirdDao;

    @Autowired
    private WaybillService waybillService;
    @Autowired
    @Qualifier("phoneSMSProducer")
    private AbstractMQProducer phoneSMSProducer;
    @Autowired
    private HandleRiderDao handleRiderDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;
    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @Override
    public void insertSmallAll(Long merchantId, String handleNo, String[] smallOrders) {
        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        if (handleNo == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        List<Waybill> list = waybillService.queryByOrderNos(merchantId.toString(), Arrays.asList(smallOrders));
        List<SendThirdDetail> dataList = new ArrayList<SendThirdDetail>();
        for (Waybill e : list) {
            SendThirdDetail sendThirdDetail = new SendThirdDetail();
            org.springframework.beans.BeanUtils.copyProperties(e, sendThirdDetail);
            sendThirdDetail.setMerchantId(merchantId);
            sendThirdDetail.setThirdHandleNo(handleNo);
            sendThirdDetail.setOrderNo(e.getOrderNo());
            sendThirdDetail.setMerchantId(merchantId);
            dataList.add(sendThirdDetail);
        }
        handleThirdDao.insertSmalls(dataList);
    }

    //这里是插入一个大包记录，没有别的注意，有什么写入什么
    @Override
    public void insertBig(SendThirdHead sendThirdHead) {
        SendThirdHead sendThirdHeadCl = new SendThirdHead();
        BeanUtils.copyProperties(sendThirdHead, sendThirdHeadCl);
        sendThirdHeadCl.setStatus(HandleRiderStatusEnum.SAVA.getCode());
        handleThirdDao.insertBig(sendThirdHeadCl);
    }

    //大包的status代表0 保存而已、1 分派成功
    //这里是同时写入大包和所有小包
    @Override
    @Transactional
    public void insertBigAndSmall(Long merchantId, SendThirdHead sendThirdHead, String[] smallOrders) {

        AssertUtil.isNotBlank(sendThirdHead.getThirdExpressCode(), BizErrorCode.THIRD_EXPRESS_EMPTY);
        AssertUtil.isNotBlank(sendThirdHead.getDriver(), BizErrorCode.THIRD_DRIVER_EMPTY);
        AssertUtil.isNotNull(smallOrders, BizErrorCode.WAYBILL_EMPTY);
        AssertUtil.isFalse(smallOrders.length == 0, BizErrorCode.WAYBILL_EMPTY);

        Principal principal = SessionLocal.getPrincipal();

        sendThirdHead.setMerchantId(Long.parseLong(principal.getMerchantId()));
        sendThirdHead.setHandleNo(SystemConfig.getNextSerialNo(merchantId.toString(), SerialTypeEnum.LOADING_NO.getCode()));

        insertBig(sendThirdHead);
        insertSmallAll(merchantId, sendThirdHead.getHandleNo(), smallOrders);
    }

    //根据大包号，获取子包list，并且自动渲染driver、操作人名字
    @Override
    public List<SendThirdHead> queryHead(SendThirdHead head, Pagination page) {

        List<SendThirdHead> list = handleThirdDao.queryBig(head, page.getOffset(), page.getLimit());
        page.setTotalCount(handleThirdDao.queryBigCount(head));

        return list;
    }

    //查询大包下的子包信息，也就是查询到件后，分派很多小包
    //这里的查询是自定义查询，传入的是大包DO，其实也就是需要大包号即可，即可查询到所有小包
    @Override
    public SendThirdHead queryDetailsByHandleNo(String handleNo) {
        Principal principal = SessionLocal.getPrincipal();

        SendThirdHead head = handleThirdDao.queryBigByHandleNo(Long.parseLong(principal.getMerchantId()), handleNo);
        if (head == null) {
            return null;
        }
        List<SendThirdDetail> detailsList = handleThirdDao.querySmall(Long.parseLong(principal.getMerchantId()), handleNo);
        head.setList(detailsList);

        return head;
    }

    //查询一个装车单里所有的小包，并且返回的是总表类型的list<DeliveryOrderDO>
    @Override
    public List<Waybill> querySmallsPlus(String merchantId, String handleNo) {
        //新建一个以装车单号为查询条件的DO，返回的即时该装车单里所有的小包
        SendThirdDetail sendThirdDetail = new SendThirdDetail();
        sendThirdDetail.setThirdHandleNo(handleNo);
        List<SendThirdDetail> list = handleThirdDao.querySmall(Long.parseLong(merchantId), handleNo);

        List<String> smallOrders = new ArrayList<String>();
        for (SendThirdDetail e : list) {
            smallOrders.add(e.getOrderNo());
        }
        List<Waybill> resList = waybillService.queryByOrderNos(merchantId, smallOrders);
        return resList;
    }

    @Override
    @Transactional
    public void edit(SendThirdHead sendThirdHead, String[] smallOrders) {

        AssertUtil.isNotNull(sendThirdHead, BizErrorCode.HandleNO_NOT_EXIST);
        AssertUtil.isNotBlank(sendThirdHead.getHandleNo(), BizErrorCode.HandleNO_NOT_EXIST);

        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());
        //查看装车单号是否存在
        SendThirdHead query = handleThirdDao.queryBigByHandleNo(merchantId, sendThirdHead.getHandleNo());
        if (query == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }

        handleThirdDao.editBigBy(sendThirdHead);
        handleThirdDao.deleteSmallByHandleNo(merchantId, sendThirdHead.getHandleNo());
        //插入小包
        insertSmallAll(merchantId, sendThirdHead.getHandleNo(), smallOrders);
    }

    @Override
    public void ship(String handleNo) {
        Principal principal = SessionLocal.getPrincipal();
        SendThirdHead head = handleThirdDao.queryBigByHandleNo(Long.parseLong(principal.getMerchantId()), handleNo);
        if (head == null || head.getStatus() == 1) {
            throw new IllegalArgumentException("Loading No." + handleNo + " Ship Failed.");
        }
        List<SendThirdDetail> detailsList = handleThirdDao.querySmall(Long.parseLong(principal.getMerchantId()), handleNo);
        if (detailsList == null || detailsList.size() == 0) {
            throw new DMSException(BizErrorCode.LOADING_EMPTY);
        }

        List<String> orderNos = new ArrayList<>();
        for (SendThirdDetail d : detailsList) {
            orderNos.add(d.getOrderNo());
        }

        OrderOptRequest request = new OrderOptRequest();
        request.setOptType(OptTypeEnum.SEND);
        request.setOrderNo(orderNos);
        waybillService.handleOpt(request);
        SendThirdHead update = new SendThirdHead();
        update.setHandleNo(handleNo);
        update.setMerchantId(Long.parseLong(principal.getMerchantId()));
        update.setStatus(1);
        handleThirdDao.editBigBy(update);


        for (SendThirdDetail d : detailsList) {
            //发送短信
            WaybillDO w = waybillDao.queryByOrderNo(Long.parseLong(principal.getMerchantId()), d.getOrderNo());
            if(StringUtil.equals(w.getOrderType(),"PK")){
                continue;
            }

            DeliveryOrderReceiverDO r = deliveryOrderReceiverDao.queryByOrderNo(Long.parseLong(principal.getMerchantId()), d.getOrderNo());
            //送货上门
            String content = "";
            if (StringUtil.equals(w.getChannel(), "1")) {
                content = "Dear customer, your order " + w.getReferenceNo() + " has been dispatched today, the next station is " + w.getStop() + ",and you should pick up it in 1-5 business days at " + r.getAddress() + ". Any question kindly contact us through Social Media and Live Chat.";
            } else {
                content = "Dear customer, your order " + w.getReferenceNo() + " has been dispatched today, the next station is " + w.getStop() + ". Your total order amount is Ksh." + w.getNeedPayAmount() + ". The courier service provider will contact you before delivery. Please keep your phone on. Thank you.";
            }
            PhoneMessage message = new PhoneMessage();
            message.setMerchantId("" + d.getMerchantId());
            message.setContent(content);
            message.setPhoneNum(r.getContactNumber());
            message.setMsgType(OptTypeEnum.DELIVERY.getCode());
            try {
                phoneSMSProducer.sendMessage(message);
            } catch (Exception e) {
            }
        }

        deliveryRouteService.addKiliRoute(orderNos, "P30");

    }
}

