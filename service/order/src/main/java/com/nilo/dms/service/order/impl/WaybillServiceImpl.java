package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.*;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.dto.system.OrderHandleConfig;
import com.nilo.dms.dto.system.SMSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

import static com.nilo.dms.common.Constant.IS_PACKAGE;

/**
 * Created by ronny on 2017/9/15.
 */
@Service
public class WaybillServiceImpl extends AbstractOrderOpt implements WaybillService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private DeliveryRouteService deliveryRouteService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private WaybillLogService waybillLogService;
    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;
    @Autowired
    private WaybillDao waybillDao;
    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;
    @Autowired
    private DeliveryOrderSenderDao deliveryOrderSenderDao;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private DeliveryOrderRequestDao deliveryOrderRequestDao;

    @Autowired
    @Qualifier("phoneSMSProducer")
    private AbstractMQProducer phoneSMSProducer;

    @Autowired
    @Qualifier("createDeliveryOrderProducer")
    private AbstractMQProducer createDeliveryOrderProducer;

    public String createWaybillRequest(String merchantId, String data, String sign) {

        try {

            Waybill order = JSON.parseObject(data, Waybill.class);
            String orderNo = order.getOrderNo();

            Waybill query = queryByOrderNo(merchantId, orderNo);
            if (query != null) return orderNo;

            // 校验订单参数
            verifyDeliveryOrderParam(order);

            DeliveryOrderRequestDO requestDO = new DeliveryOrderRequestDO();
            requestDO.setOrderNo(orderNo);
            requestDO.setData(data);
            requestDO.setMerchantId(Long.parseLong(merchantId));
            requestDO.setStatus(CreateDeliveryRequestStatusEnum.CREATE.getCode());
            requestDO.setSign(sign);

            deliveryOrderRequestDao.insert(requestDO);
            CreateDeliverOrderMessage message = new CreateDeliverOrderMessage();
            message.setRequestId(requestDO.getId());
            message.setOrderNo(orderNo);
            message.setOptBy(merchantId);
            message.setMerchantId(merchantId);
            createDeliveryOrderProducer.sendMessage(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateWaybill(WaybillHeader header) {

        AssertUtil.isNotNull(header, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotNull(header.getMerchantId(), BizErrorCode.MERCHANT_ID_EMPTY);
        AssertUtil.isNotNull(header.getOrderNo(), BizErrorCode.ORDER_NO_EMPTY);
        //校验是否存在
        WaybillDO query = waybillDao.queryByOrderNo(Long.parseLong(header.getMerchantId()), header.getOrderNo());
        if (query == null) {
            throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, header.getOrderNo());
        }
        //更新运单信息(暂只更新父包裹ID,状态，网点，重量、长宽高，支付类型,打印次数，已支付金额)
        WaybillDO update = new WaybillDO();
        update.setMerchantId(Long.parseLong(header.getMerchantId()));
        update.setOrderNo(header.getOrderNo());
        update.setParentNo(header.getParentNo());
        update.setWeight(header.getWeight());
        update.setLength(header.getLen());
        update.setWidth(header.getWidth());
        update.setHeight(header.getHeight());
        update.setNetworkId(header.getNetworkId());
        update.setNextNetworkId(header.getNextNetworkId());
        if (header.getPaidType() != null) {
            update.setPaidType(header.getPaidType().getCode());
        }
        header.setPrintTimes(header.getPrintTimes());
        header.setAlreadyPaid(header.getAlreadyPaid());
        waybillDao.update(update);

    }

    @Override
    public List<Waybill> queryWaybillBy(WaybillParameter parameter, Pagination pagination) {

        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", parameter.getOrderNo());
        map.put("referenceNo", parameter.getReferenceNo());
        map.put("merchantId", parameter.getMerchantId());
        map.put("orderType", parameter.getOrderType());
        map.put("status", parameter.getStatus());
        map.put("isPackage", parameter.getIsPackage());

        if (StringUtil.isEmpty(parameter.getFromCreatedTime()) || StringUtil.isEmpty(parameter.getToCreatedTime())) {
            if (StringUtil.isEmpty(parameter.getFromCreatedTime())) {
                parameter.setFromCreatedTime(parameter.getToCreatedTime());
            } else {
                parameter.setToCreatedTime(parameter.getFromCreatedTime());
            }
        }
        if (StringUtil.isNotEmpty(parameter.getFromCreatedTime())) {
            Long fromTime = DateUtil.parse(parameter.getFromCreatedTime(), "yyyy-MM-dd");
            map.put("fromCreatedTime", fromTime);
        }
        if (StringUtil.isNotEmpty(parameter.getToCreatedTime())) {
            Long toTime = DateUtil.parse(parameter.getToCreatedTime(), "yyyy-MM-dd") + 24 * 60 * 60 - 1;
            map.put("toCreatedTime", toTime);
        }
        map.put("nextStation", parameter.getNextStation());
        map.put("orderPlatform", parameter.getPlatform());
        map.put("offset", pagination.getOffset());
        map.put("limit", pagination.getLimit());

        // 查询记录
        Long count = waybillDao.queryCountBy(map);
        List<WaybillDO> queryList = waybillDao.queryDeliveryOrderListBy(map);
        pagination.setTotalCount(count == null ? 0 : count);

        return batchQuery(queryList, Long.parseLong(parameter.getMerchantId()));
    }

    private List<Waybill> batchQuery(List<WaybillDO> waybillDOs, Long merchantId) {

        List<Waybill> list = new ArrayList<>();
        // 构建订单号集合
        List<String> orderNos = new ArrayList<>();
        for (WaybillDO o : waybillDOs) {
            orderNos.add(o.getOrderNo());
        }
        List<DeliveryOrderSenderDO> senderDO = deliveryOrderSenderDao.queryByOrderNos(merchantId, orderNos);
        List<DeliveryOrderReceiverDO> receiverDO = deliveryOrderReceiverDao.queryByOrderNos(merchantId, orderNos);
        List<DeliveryOrderGoodsDO> goodsDO = deliveryOrderGoodsDao.queryByOrderNos(merchantId, orderNos);

        for (WaybillDO o : waybillDOs) {
            Waybill order = convert(o);
            for (DeliveryOrderSenderDO s : senderDO) {
                if (StringUtil.equals(o.getOrderNo(), s.getOrderNo())) {
                    order.setSenderInfo(convert(s));
                    break;
                }
            }
            for (DeliveryOrderReceiverDO r : receiverDO) {
                if (StringUtil.equals(o.getOrderNo(), r.getOrderNo())) {
                    order.setReceiverInfo(convert(r));
                    break;
                }
            }
            List<DeliveryOrderGoodsDO> goodsList = new ArrayList<>();
            for (DeliveryOrderGoodsDO r : goodsDO) {
                if (StringUtil.equals(o.getOrderNo(), r.getOrderNo())) {
                    goodsList.add(r);
                }
            }
            order.setGoodsInfoList(convert(goodsList));
            list.add(order);
        }
        return list;
    }

    @Override
    public Waybill queryByOrderNo(String merchantId, String orderNo) {
        WaybillDO orderDO = waybillDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
        if (orderDO == null) {
            return null;
        }
        Waybill order = convert(orderDO);
        Long merchantIdL = Long.parseLong(merchantId);
        DeliveryOrderSenderDO senderDO = deliveryOrderSenderDao.queryByOrderNo(merchantIdL, order.getOrderNo());
        DeliveryOrderReceiverDO receiverDO = deliveryOrderReceiverDao.queryByOrderNo(merchantIdL, order.getOrderNo());
        List<DeliveryOrderGoodsDO> queryGoodsList = deliveryOrderGoodsDao.queryByOrderNo(merchantIdL,
                order.getOrderNo());
        order.setGoodsInfoList(convert(queryGoodsList));
        order.setSenderInfo(convert(senderDO));
        order.setReceiverInfo(convert(receiverDO));
        return order;
    }

    @Override
    public List<Waybill> queryByOrderNos(String merchantId, List<String> orderNos) {
        if (orderNos == null || orderNos.size() == 0) {
            return null;
        }
        List<WaybillDO> orderDOs = waybillDao.queryByOrderNos(Long.parseLong(merchantId), orderNos);
        if (orderDOs == null) {
            return null;
        }
        return batchQuery(orderDOs, Long.parseLong(merchantId));
    }

    @Override
    public void handleOpt(OrderOptRequest optRequest) {

        // 校验操作参数
        checkOtpParam(optRequest);
        // 校验操作类型
        checkOptType(optRequest);
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {

                    String merchantId = SessionLocal.getPrincipal().getMerchantId();

                    OrderHandleConfig handleConfig = SystemConfig.getOrderHandleConfig(merchantId,
                            optRequest.getOptType().getCode());
                    for (String orderNo : optRequest.getOrderNo()) {
                        WaybillDO orderDO = waybillDao
                                .queryByOrderNo(Long.parseLong(merchantId), orderNo);
                        if (handleConfig.getUpdateStatus() != null) {
                            // 更新订单状态
                            updateDeliveryOrderStatus(optRequest, orderNo, handleConfig);
                        }
                        // 短信消息
                        sendPhoneSMS(merchantId, optRequest.getOptType().getCode(), orderDO);
                    }
                    //通知上游系统状态变更
                    notifyService.updateStatus(optRequest);
                    // 记录物流轨迹
                    deliveryRouteService.addRoute(optRequest);
                    // 添加操作记录
                    waybillLogService.addOptLog(optRequest);
                } catch (Exception e) {
                    logger.error("handleOpt Failed. Data:{}", optRequest, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });
    }

    @Override
    public void arrive(List<String> waybillNos) {

        Principal principal = SessionLocal.getPrincipal();
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOptType(OptTypeEnum.ARRIVE_SCAN);
        optRequest.setOrderNo(waybillNos);
        handleOpt(optRequest);
    }

    @Override
    @Transactional
    public void print(String merchantId, List<String> orderNos) {
        for (String o : orderNos) {
            WaybillDO orderDO = waybillDao.queryByOrderNo(Long.parseLong(merchantId), o);
            if (orderDO == null) {
                throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, o);
            }
            WaybillDO update = new WaybillDO();
            update.setOrderNo(o);
            update.setMerchantId(Long.parseLong(merchantId));
            update.setPrintTimes(orderDO.getPrintTimes() + 1);
            waybillDao.update(update);
        }
    }

    @Override
    @Transactional
    public String addPackage(PackageRequest packageRequest) {
        String orderNo = "";
        Long merchant = Long.parseLong(packageRequest.getMerchantId());

        // 判断是否允许打包
        for (String o : packageRequest.getOrderNos()) {
            Waybill waybill = queryByOrderNo(packageRequest.getMerchantId(), o);
            if (StringUtil.isNotEmpty(waybill.getParentNo()) ) {
                throw new DMSException(BizErrorCode.PACKAGE_ALREADY_PACKAGE, o);
            }
        }

        // 1、保存订单信息
        WaybillDO orderHeader = new WaybillDO();
        orderHeader.setMerchantId(merchant);
        orderHeader.setHeight(packageRequest.getHigh());
        orderHeader.setWidth(packageRequest.getWidth());
        orderHeader.setWeight(packageRequest.getWeight());
        orderHeader.setLength(packageRequest.getLength());
        orderHeader.setIsPackage(IS_PACKAGE);
        orderHeader.setOrderType("PG");
        orderHeader.setStatus(DeliveryOrderStatusEnum.ARRIVED.getCode());
        orderHeader.setNextNetworkId(packageRequest.getNextNetworkId());
        orderHeader.setNetworkId(packageRequest.getNetworkId());
        // 获取订单号
        orderNo = SystemConfig.getNextSerialNo(packageRequest.getMerchantId(),
                SerialTypeEnum.DELIVERY_ORDER_NO.getCode());
        orderHeader.setOrderNo(orderNo);
        orderHeader.setCreatedBy(packageRequest.getOptBy());
        waybillDao.insert(orderHeader);

        // 发件网点信息
        DistributionNetworkDO networkDO = distributionNetworkDao.queryById(new Long(packageRequest.getNetworkId()));
        DeliveryOrderReceiverDO r = new DeliveryOrderReceiverDO();
        r.setOrderNo(orderNo);
        r.setMerchantId(merchant);
        r.setAddress(networkDO.getAddress());
        r.setArea(networkDO.getArea());
        r.setCity(networkDO.getCity());
        r.setContactNumber(networkDO.getContactPhone());
        r.setCountry(networkDO.getCountry());
        r.setName(networkDO.getContactName());
        r.setProvince(networkDO.getProvince());
        deliveryOrderReceiverDao.insert(r);

        // 3、保存收件网点信息
        DistributionNetworkDO receiverNetwork = distributionNetworkDao
                .queryById(new Long(packageRequest.getNextNetworkId()));
        DeliveryOrderSenderDO s = new DeliveryOrderSenderDO();
        s.setMerchantId(merchant);
        s.setOrderNo(orderNo);
        s.setAddress(receiverNetwork.getAddress());
        s.setArea(receiverNetwork.getArea());
        s.setCity(receiverNetwork.getCity());
        s.setContactNumber(receiverNetwork.getContactPhone());
        s.setCountry(receiverNetwork.getCountry());
        s.setName(receiverNetwork.getContactName());
        s.setProvince(receiverNetwork.getProvince());
        deliveryOrderSenderDao.insert(s);

        // 关联包裹与子运单
        for (String o : packageRequest.getOrderNos()) {
            WaybillDO update = new WaybillDO();
            update.setMerchantId(merchant);
            update.setOrderNo(o);
            update.setParentNo(orderNo);
            waybillDao.update(update);
        }

        // 添加操作日志
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOrderNo(packageRequest.getOrderNos());
        optRequest.setOptType(OptTypeEnum.PACKAGE);
        waybillLogService.addOptLog(optRequest);
        return orderNo;
    }

    @Override
    @Transactional
    public void unpack(UnpackRequest unpackRequest) {

        Iterator<String> iterator = unpackRequest.getOrderNos().iterator();
        // 判断是否允许拆包
        for (; iterator.hasNext(); ) {
            String orderNo = iterator.next();
            Waybill waybill = queryByOrderNo(unpackRequest.getMerchantId(), orderNo);
            if (StringUtil.isEmpty(waybill.getParentNo()) && !waybill.isPackage()) {
                throw new DMSException(BizErrorCode.UNPACK_NOT_ALLOW, orderNo);
            }
            if (waybill.isPackage()) {
                OrderOptRequest optRequest = new OrderOptRequest();
                optRequest.setOptType(OptTypeEnum.SIGN);
                optRequest.setOrderNo(Arrays.asList(new String[]{orderNo}));
                handleOpt(optRequest);
                iterator.remove();
            }
        }

        //小包到件
        arrive(unpackRequest.getOrderNos());

        for (String o : unpackRequest.getOrderNos()) {
            //3、清除 大包号
            WaybillDO update = new WaybillDO();
            update.setMerchantId(Long.parseLong(unpackRequest.getMerchantId()));
            update.setOrderNo(o);
            update.setParentNo("");
            waybillDao.update(update);
        }
    }

    @Override
    public List<Waybill> queryByPackageNo(String merchantNo, String packageNo) {
        List<WaybillDO> queryList = waybillDao.queryByPackageNo(Long.parseLong(merchantNo), packageNo);

        List<Waybill> list = new ArrayList<>();
        if (queryList == null)
            return list;
        for (WaybillDO d : queryList) {
            list.add(convert(d));
        }
        return list;
    }

    private void updateDeliveryOrderStatus(OrderOptRequest optRequest, String orderNo, OrderHandleConfig handleConfig) {
        long affected = 0;
        // 更新订单信息，循环10次，10次未更新成功，则跳出
        for (int i = 0; i < 10; i++) {
            WaybillDO query = waybillDao.queryByOrderNo(Long.parseLong(SessionLocal.getPrincipal().getMerchantId()),
                    orderNo);
            WaybillDO update = new WaybillDO();
            update.setMerchantId(query.getMerchantId());
            update.setOrderNo(orderNo);
            update.setStatus(handleConfig.getUpdateStatus());
            update.setVersion(query.getVersion());
            // 更新订单信息
            affected = waybillDao.update(update);
            if (affected > 0) {
                // 记录操作日志
                break;
            }
            Thread.yield();
        }
        // 判断是否更新成功
        if (affected == 0) {
            throw new DMSException(SysErrorCode.DB_EXCEPTION);
        }
    }

    private void sendPhoneSMS(String merchantId, String optType, WaybillDO query) {
        try {
            SMSConfig smsConfig = JSON.parseObject(RedisUtil.hget(Constant.SMS_CONF + merchantId, optType),
                    SMSConfig.class);

            if (smsConfig == null) {
                return;
            }

            String content = smsConfig.getContent();
            // 查询手机号码
            DeliveryOrderReceiverDO receiverDO = deliveryOrderReceiverDao.queryByOrderNo(query.getMerchantId(),
                    query.getOrderNo());
            PhoneMessage message = new PhoneMessage();
            message.setPhoneNum(receiverDO.getContactNumber());
            message.setContent(content);
            message.setMerchantId(merchantId);
            message.setMsgType(optType);
            phoneSMSProducer.sendMessage(message);
        } catch (Exception e) {
            logger.error("Send Phone Message Failed.", e);
        }
    }


    private Waybill convert(WaybillDO d) {
        Waybill waybill = new Waybill();
        waybill.setOrderNo(d.getOrderNo());
        waybill.setReferenceNo(d.getReferenceNo());
        waybill.setCreatedTime(d.getCreatedTime());
        waybill.setUpdatedTime(d.getUpdatedTime());
        waybill.setCountry(d.getCountry());
        waybill.setMerchantId("" + d.getMerchantId());
        waybill.setOrderTime(d.getOrderTime());
        waybill.setOrderType(d.getOrderType());
        waybill.setOrderPlatform(d.getOrderPlatform());
        waybill.setTotalPrice(d.getTotalPrice());
        waybill.setNeedPayAmount(d.getNeedPayAmount());
        waybill.setAlreadyPaid(d.getAlreadyPaid());
        waybill.setStatus(DeliveryOrderStatusEnum.getEnum(d.getStatus()));
        waybill.setWeight(d.getWeight());
        waybill.setGoodsType(d.getGoodsType());
        String orderTypeDesc = SystemCodeUtil.getCodeVal("" + d.getMerchantId(), Constant.DELIVERY_ORDER_TYPE,
                d.getOrderType());
        waybill.setOrderTypeDesc(orderTypeDesc);

        waybill.setWarehouseId(d.getWarehouseId());
        waybill.setStop(d.getStop());
        waybill.setStopId(d.getStopId());
        waybill.setChannel(d.getChannel());
        waybill.setChannelStation(d.getChannelStation());
        waybill.setOrderCategory(d.getOrderCategory());
        waybill.setCarrierId(d.getCarrierId());
        waybill.setCarrierName(d.getCarrierName());
        waybill.setRelationOrderNo(d.getRelationOrderNo());
        waybill.setDeliveryFee(d.getDeliveryFee());
        waybill.setIsCod(d.getIsCod());
        waybill.setNotes(d.getNotes());
        waybill.setRemark(d.getRemark());
        waybill.setNeedPayAmount(d.getNeedPayAmount());
        waybill.setPaidType(PaidTypeEnum.getEnum(d.getPaidType()));

        waybill.setParentNo(d.getParentNo());
        waybill.setLen(d.getLength());
        waybill.setWidth(d.getWidth());
        waybill.setHeight(d.getHeight());
        waybill.setNetworkId(d.getNetworkId());
        waybill.setNextNetworkId(d.getNextNetworkId());
        if (d.getNetworkId() != null) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + d.getMerchantId(), "" + d.getNetworkId()), DistributionNetworkDO.class);
            waybill.setNetworkDesc(networkDO.getName());
        }
        if (d.getNextNetworkId() != null) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + d.getMerchantId(), "" + d.getNextNetworkId()), DistributionNetworkDO.class);
            waybill.setNextNetworkDesc(networkDO.getName());
        }
        waybill.setPackage(StringUtil.equalsIgnoreCase(d.getIsPackage(), Constant.IS_PACKAGE));
        waybill.setCreatedBy(d.getCreatedBy());
        waybill.setPrintTimes(d.getPrintTimes());
        return waybill;
    }

    private SenderInfo convert(DeliveryOrderSenderDO s) {
        if (s == null) {
            return null;
        }
        SenderInfo sender = new SenderInfo();
        sender.setSenderAddress(s.getAddress());
        sender.setSenderArea(s.getArea());
        sender.setSenderCity(s.getCity());
        sender.setSenderPhone(s.getContactNumber());
        sender.setSenderCountry(s.getCountry());
        sender.setSenderName(s.getName());
        sender.setSenderProvince(s.getProvince());
        return sender;
    }

    private ReceiverInfo convert(DeliveryOrderReceiverDO d) {
        if (d == null) {
            return null;
        }
        ReceiverInfo receiverInfo = new ReceiverInfo();
        receiverInfo.setReceiverAddress(d.getAddress());
        receiverInfo.setReceiverArea(d.getArea());
        receiverInfo.setReceiverCity(d.getCity());
        receiverInfo.setReceiverPhone(d.getContactNumber());
        receiverInfo.setReceiverCountry(d.getCountry());
        receiverInfo.setReceiverName(d.getName());
        receiverInfo.setReceiverProvince(d.getProvince());
        return receiverInfo;
    }

    private List<GoodsInfo> convert(List<DeliveryOrderGoodsDO> goodsList) {
        if (goodsList == null) {
            return null;
        }
        List<GoodsInfo> list = new ArrayList<>();

        for (DeliveryOrderGoodsDO g : goodsList) {
            GoodsInfo goods = new GoodsInfo();
            goods.setGoodsDesc(g.getGoodsDesc());
            goods.setGoodsId(g.getGoodsId());
            goods.setQty(g.getQty());
            goods.setQuality(g.getQuality());
            goods.setUnitPrice(g.getUnitPrice());
            list.add(goods);
        }
        return list;
    }

    private WaybillDO convert(Waybill d) {

        WaybillDO orderHeader = new WaybillDO();
        orderHeader.setCountry(d.getCountry());
        orderHeader.setMerchantId(Long.parseLong(d.getMerchantId()));
        orderHeader.setOrderPlatform(d.getOrderPlatform());
        orderHeader.setOrderTime(d.getOrderTime());
        orderHeader.setOrderType(d.getOrderType());
        orderHeader.setReferenceNo(d.getReferenceNo());
        orderHeader.setStatus(d.getStatus().getCode());
        orderHeader.setWeight(d.getWeight());
        orderHeader.setGoodsType(d.getGoodsType());
        orderHeader.setTotalPrice(d.getTotalPrice());
        orderHeader.setWarehouseId(d.getWarehouseId());
        orderHeader.setStop(d.getStop());
        orderHeader.setStopId(d.getStopId());
        orderHeader.setChannel(d.getChannel());
        orderHeader.setChannelStation(d.getChannelStation());
        orderHeader.setOrderCategory(d.getOrderCategory());
        orderHeader.setCarrierId(d.getCarrierId());
        orderHeader.setCarrierName(d.getCarrierName());
        orderHeader.setRelationOrderNo(d.getRelationOrderNo());
        orderHeader.setDeliveryFee(d.getDeliveryFee());
        orderHeader.setIsCod(d.getIsCod());
        orderHeader.setNotes(d.getNotes());
        orderHeader.setRemark(d.getRemark());

        orderHeader.setIsPackage(d.isPackage() ? Constant.IS_PACKAGE : "");
        return orderHeader;
    }

    private void verifyDeliveryOrderParam(Waybill data) {
        AssertUtil.isNotNull(data, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotBlank(data.getReferenceNo(), BizErrorCode.REFERENCE_NO_EMPTY);
        AssertUtil.isNotBlank(data.getOrderType(), BizErrorCode.ORDER_TYPE_EMPTY);
        AssertUtil.isNotBlank(data.getGoodsType(), BizErrorCode.GOODS_TYPE_EMPTY);
        AssertUtil.isNotNull(data.getReceiverInfo(), BizErrorCode.RECEIVER_EMPTY);
        AssertUtil.isNotBlank(data.getReceiverInfo().getReceiverName(), BizErrorCode.RECEIVE_NAME_EMPTY);
        AssertUtil.isNotBlank(data.getReceiverInfo().getReceiverPhone(), BizErrorCode.RECEIVE_PHONE_EMPTY);
        AssertUtil.isNotBlank(data.getReceiverInfo().getReceiverAddress(), BizErrorCode.RECEIVE_ADDRESS_EMPTY);
    }

}
