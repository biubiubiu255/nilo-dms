package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.*;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.DeliveryOrderGoodsDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import com.nilo.dms.dao.dataobject.ThirdPushDo;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.PhoneMessage;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.dto.system.InterfaceConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SendMessageService;
import com.nilo.dms.service.system.SystemConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.*;

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
    private SendMessageService sendMessageService;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;
    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;

    @Autowired
    @Qualifier("sendThirdPushProducer")
    private AbstractMQProducer sendThirdPushProducer;

    @Override
    public void insertSmallAll(Long merchantId, String handleNo, String[] smallOrders) {
        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        if (handleNo == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        if (smallOrders == null || smallOrders.length == 0) {
            return;
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
        sendThirdHead.setHandleNo(getHandleNo(merchantId));

        insertBig(sendThirdHead);
        insertSmallAll(merchantId, sendThirdHead.getHandleNo(), smallOrders);
    }

    private String getHandleNo(Long merchantId) {

        String handleNo = SystemConfig.getNextSerialNo(merchantId.toString(), SerialTypeEnum.LOADING_NO.getCode());
        SendThirdHead head = handleThirdDao.queryBigByHandleNo(merchantId, handleNo);
        if (head != null) {
            handleNo = getHandleNo(merchantId);
        }
        return handleNo;
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
    public SendThirdHead queryLoadingBySmallNo(String merchantId, String orderNo) {
        return handleThirdDao.queryHandleBySmallNo(Long.parseLong(merchantId), orderNo);
    }

    @Override
    @Transactional
    public void editSmalls(SendThirdHead sendThirdHead, String[] smallOrders) {

        AssertUtil.isNotNull(sendThirdHead, BizErrorCode.HandleNO_NOT_EXIST);
        AssertUtil.isNotBlank(sendThirdHead.getHandleNo(), BizErrorCode.HandleNO_NOT_EXIST);

        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());
        //查看装车单号是否存在
        SendThirdHead query = handleThirdDao.queryBigByHandleNo(merchantId, sendThirdHead.getHandleNo());
        if (query == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }

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
        request.setRemark(head.getThirdExpressCode());
        waybillService.handleOpt(request);
        SendThirdHead update = new SendThirdHead();
        update.setHandleNo(handleNo);
        update.setMerchantId(Long.parseLong(principal.getMerchantId()));
        update.setStatus(1);

        handleThirdDao.editBigBy(update);

        SendThirdDetail sendThirdDetail = new SendThirdDetail();
        sendThirdDetail.setMerchantId(Long.parseLong(principal.getMerchantId()));
        sendThirdDetail.setThirdHandleNo(handleNo);
        sendThirdDetail.setStatus(HandleRiderStatusEnum.SHIP.getCode());
        handleThirdDao.editAllSmallbyHandleNo(sendThirdDetail);

        //第三方对接
/*        if(head.getType().equals("waybill")){

            List<String> orders = PickUtil.recombineList(detailsList, "orderNo", String.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orders", orderNos);
            map.put("expressCode", head.getThirdExpressCode());
            map.put("merchantId", principal.getMerchantId());
            try {
                sendThirdPushProducer.sendMessage(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        for (SendThirdDetail d : detailsList) {

            //发送短信
            WaybillDO w = waybillDao.queryByOrderNo(Long.parseLong(principal.getMerchantId()), d.getOrderNo());
            DeliveryOrderReceiverDO r = deliveryOrderReceiverDao.queryByOrderNo(w.getMerchantId(), w.getOrderNo());
            if (!StringUtil.equals(w.getOrderType(), "PG")) {
                if (StringUtil.equals(w.getChannel(), "1")) {
                    sendMessageService.sendMessage(w.getOrderNo(), SendMessageService.THIRD_SELF_COLLECT, r.getContactNumber(), w.getReferenceNo(), head.getThirdExpressCode(), w.getStop(), r.getAddress());
                } else {
                    sendMessageService.sendMessage(w.getOrderNo(), SendMessageService.THIRD_DOORSTEP, r.getContactNumber(), w.getReferenceNo(), head.getThirdExpressCode());
                }
            }
        }
    }

    @Override
    public void deleteHandle(String handleNo) {
        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());
        SendThirdHead sendThirdHead = handleThirdDao.queryBigByHandleNo(merchantId, handleNo);
        AssertUtil.isNotNull(sendThirdHead, BizErrorCode.HandleNO_NOT_EXIST);
        handleThirdDao.deleteById(sendThirdHead.getId().longValue());
        handleThirdDao.deleteSmallByHandleNo(merchantId, handleNo);
    }


}

