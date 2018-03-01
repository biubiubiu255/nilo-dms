package com.nilo.dms.service.order.impl;

import static com.nilo.dms.common.Constant.IS_PACKAGE;

import java.text.MessageFormat;
import java.util.*;

import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.*;
import com.nilo.dms.service.order.model.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.CreateDeliveryRequestStatusEnum;
import com.nilo.dms.common.enums.DeliveryOrderPaidTypeEnum;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.enums.ServiceTypeEnum;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryOrderDao;
import com.nilo.dms.dao.DeliveryOrderGoodsDao;
import com.nilo.dms.dao.DeliveryOrderReceiverDao;
import com.nilo.dms.dao.DeliveryOrderRequestDao;
import com.nilo.dms.dao.DeliveryOrderSenderDao;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderGoodsDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderReceiverDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderRequestDO;
import com.nilo.dms.dao.dataobject.DeliveryOrderSenderDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.UserNetworkDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.AbstractOrderOpt;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.CreateDeliverOrderMessage;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.DeliveryOrderParameter;
import com.nilo.dms.service.order.model.GoodsInfo;
import com.nilo.dms.service.order.model.NotifyRequest;
import com.nilo.dms.service.order.model.OrderOptRequest;
import com.nilo.dms.service.order.model.PackageRequest;
import com.nilo.dms.service.order.model.PhoneMessage;
import com.nilo.dms.service.order.model.ReceiverInfo;
import com.nilo.dms.service.order.model.SenderInfo;
import com.nilo.dms.service.order.model.Task;
import com.nilo.dms.service.order.model.UnpackRequest;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import com.nilo.dms.service.system.model.InterfaceConfig;
import com.nilo.dms.service.system.model.MerchantConfig;
import com.nilo.dms.service.system.model.OrderHandleConfig;
import com.nilo.dms.service.system.model.SMSConfig;

/**
 * Created by ronny on 2017/9/15.
 */
@Service
public class OrderServiceImpl extends AbstractOrderOpt implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryRouteService deliveryRouteService;
    @Autowired
    private OrderOptLogService orderOptLogService;
    @Autowired
    private DeliveryOrderGoodsDao deliveryOrderGoodsDao;
    @Autowired
    private DeliveryOrderDao deliveryOrderDao;
    @Autowired
    private DeliveryOrderReceiverDao deliveryOrderReceiverDao;
    @Autowired
    private DeliveryOrderSenderDao deliveryOrderSenderDao;
    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private DeliveryOrderRequestDao deliveryOrderRequestDao;

    @Autowired
    private UserNetworkDao userNetworkDao;
    @Autowired
    @Qualifier("notifyMerchantProducer")
    private AbstractMQProducer notifyMerchantProducer;

    @Autowired
    @Qualifier("phoneSMSProducer")
    private AbstractMQProducer phoneSMSProducer;

    @Autowired
    @Qualifier("createDeliveryOrderProducer")
    private AbstractMQProducer createDeliveryOrderProducer;

    public String addCreateDeliveryOrderRequest(String merchantId, String data, String sign) {

        try {

            DeliveryOrder order = JSON.parseObject(data, DeliveryOrder.class);
            String orderNo = order.getOrderNo();

            DeliveryOrder query = queryByOrderNo(merchantId, orderNo);
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
    public String createDeliveryOrder(final DeliveryOrder data) {
        // 数据校验
        verifyDeliveryOrderParam(data);

        String orderNo = transactionTemplate.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus transactionStatus) {
                String orderNo = "";
                Long merchant = Long.parseLong(data.getMerchantId());
                try {
                    // 1、保存订单信息
                    data.setStatus(DeliveryOrderStatusEnum.CREATE);
                    DeliveryOrderDO orderHeader = convert(data);
                    // 获取订单号
                    orderNo = SystemConfig.getNextSerialNo(data.getMerchantId(),
                            SerialTypeEnum.DELIVERY_ORDER_NO.getCode());
                    orderHeader.setOrderNo(orderNo);
                    deliveryOrderDao.insert(orderHeader);

                    // 2、保存订单商品明细信息
                    for (GoodsInfo g : data.getGoodsInfoList()) {
                        DeliveryOrderGoodsDO goods = new DeliveryOrderGoodsDO();
                        goods.setMerchantId(merchant);
                        goods.setOrderNo(orderNo);
                        goods.setGoodsDesc(g.getGoodsDesc());
                        goods.setQty(g.getQty());
                        goods.setUnitPrice(g.getUnitPrice());
                        goods.setGoodsId(g.getGoodsId());
                        goods.setQuality(g.getQuality());
                        deliveryOrderGoodsDao.insert(goods);
                    }

                    // 3、保存收货人信息
                    ReceiverInfo receiverInfo = data.getReceiverInfo();
                    DeliveryOrderReceiverDO r = new DeliveryOrderReceiverDO();
                    r.setOrderNo(orderNo);
                    r.setMerchantId(merchant);
                    r.setAddress(receiverInfo.getReceiverAddress());
                    r.setArea(receiverInfo.getReceiverArea());
                    r.setCity(receiverInfo.getReceiverCity());
                    r.setContactNumber(receiverInfo.getReceiverPhone());
                    r.setCountry(receiverInfo.getReceiverCountry());
                    r.setName(receiverInfo.getReceiverName());
                    r.setProvince(receiverInfo.getReceiverProvince());
                    deliveryOrderReceiverDao.insert(r);

                    // 3、保存寄件人信息
                    SenderInfo senderInfo = data.getSenderInfo();
                    DeliveryOrderSenderDO s = new DeliveryOrderSenderDO();
                    s.setMerchantId(merchant);
                    s.setOrderNo(orderNo);
                    s.setAddress(senderInfo.getSenderAddress());
                    s.setArea(senderInfo.getSenderArea());
                    s.setCity(senderInfo.getSenderCity());
                    s.setContactNumber(senderInfo.getSenderPhone());
                    s.setCountry(senderInfo.getSenderCountry());
                    s.setName(senderInfo.getSenderName());
                    s.setProvince(senderInfo.getSenderProvince());
                    deliveryOrderSenderDao.insert(s);
                } catch (Exception e) {
                    logger.error("createDeliveryOrder Failed. Data:{}", data, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return orderNo;
            }
        });

        return orderNo;
    }

    @Override
    public List<DeliveryOrder> queryDeliveryOrderBy(DeliveryOrderParameter parameter, Pagination pagination) {

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
        map.put("offset", pagination.getOffset());
        map.put("limit", pagination.getLimit());

        // 查询记录
        List<DeliveryOrderDO> queryList = deliveryOrderDao.queryDeliveryOrderListBy(map);
        Long count = deliveryOrderDao.queryCountBy(map);
        pagination.setTotalCount(count == null ? 0 : count);

        return batchQuery(queryList, Long.parseLong(parameter.getMerchantId()));
    }

    private List<DeliveryOrder> batchQuery(List<DeliveryOrderDO> deliveryOrderDOs, Long merchantId) {

        List<DeliveryOrder> list = new ArrayList<>();
        // 构建订单号集合
        List<String> orderNos = new ArrayList<>();
        for (DeliveryOrderDO o : deliveryOrderDOs) {
            orderNos.add(o.getOrderNo());
        }
        List<DeliveryOrderSenderDO> senderDO = deliveryOrderSenderDao.queryByOrderNos(merchantId, orderNos);
        List<DeliveryOrderReceiverDO> receiverDO = deliveryOrderReceiverDao.queryByOrderNos(merchantId, orderNos);
        for (DeliveryOrderDO o : deliveryOrderDOs) {
            DeliveryOrder order = convert(o);
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
            list.add(order);
        }
        return list;
    }

    @Override
    public DeliveryOrder queryByOrderNo(String merchantId, String orderNo) {
        DeliveryOrderDO orderDO = deliveryOrderDao.queryByOrderNo(Long.parseLong(merchantId), orderNo);
        if (orderDO == null) {
            return null;
        }
        DeliveryOrder order = convert(orderDO);
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
    public List<DeliveryOrder> queryByOrderNos(String merchantId, List<String> orderNos) {
        if (orderNos == null || orderNos.size() == 0) {
            return null;
        }
        List<DeliveryOrderDO> orderDOs = deliveryOrderDao.queryByOrderNos(Long.parseLong(merchantId), orderNos);
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

                    OrderHandleConfig handleConfig = SystemConfig.getOrderHandleConfig(optRequest.getMerchantId(),
                            optRequest.getOptType().getCode());

                    for (String orderNo : optRequest.getOrderNo()) {
                        DeliveryOrderDO orderDO = deliveryOrderDao
                                .queryByOrderNo(Long.parseLong(optRequest.getMerchantId()), orderNo);
                        if (handleConfig.getUpdateStatus() != null) {
                            // 更新订单状态
                            updateDeliveryOrderStatus(optRequest, orderNo, handleConfig);
                            // 通知KiliBoss订单状态变更
                            notifyStatusUpdate(optRequest, orderNo, orderDO.getReferenceNo(),
                                    DeliveryOrderStatusEnum.getEnum(handleConfig.getUpdateStatus()));
                        }
                        // 短信消息
                        sendPhoneSMS(optRequest.getMerchantId(), optRequest.getOptType().getCode(), orderDO);
                    }
                    // 记录物流轨迹
                    deliveryRouteService.addRoute(optRequest);

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
    @Transactional
    public void arrive(String merchantId, String scanNo, String networkId, String arriveBy) {
        List<WaybillScanDetailsDO> scanDetailList = waybillScanDetailsDao.queryByScanNo(scanNo);
        if (scanDetailList == null || scanDetailList.size() == 0) throw new DMSException(BizErrorCode.ARRIVE_EMPTY);


        List<String> orderNos = new ArrayList<>();
        for (WaybillScanDetailsDO details : scanDetailList) {
            orderNos.add(details.getOrderNo());
        }
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(arriveBy);
        optRequest.setOptType(OptTypeEnum.ARRIVE_SCAN);
        optRequest.setOrderNo(orderNos);
        optRequest.setNetworkId(networkId);
        handleOpt(optRequest);

        // 更新重量
        for (WaybillScanDetailsDO details : scanDetailList) {
            if (details.getWeight() == null)
                continue;
            DeliveryOrderDO orderDO = new DeliveryOrderDO();
            orderDO.setOrderNo(details.getOrderNo());
            orderDO.setWeight(details.getWeight());
            orderDO.setMerchantId(Long.parseLong(merchantId));

            deliveryOrderDao.update(orderDO);
        }

        this.addNetworkTask(orderNos, arriveBy, merchantId);

    }

    @Override
    @Transactional
    public void print(String merchantId, List<String> orderNos) {
        for (String o : orderNos) {
            DeliveryOrderDO orderDO = deliveryOrderDao.queryByOrderNo(Long.parseLong(merchantId), o);
            if (orderDO == null) {
                throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, o);
            }
            DeliveryOrderDO update = new DeliveryOrderDO();
            update.setOrderNo(o);
            update.setMerchantId(Long.parseLong(merchantId));
            update.setPrintTimes(orderDO.getPrintTimes() + 1);
            deliveryOrderDao.update(update);
        }
    }

    @Override
    @Transactional
    public void waybillNoListArrive(List<String> waybillNos, String arriveBy, String merchantId) {
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(arriveBy);
        optRequest.setOptType(OptTypeEnum.ARRIVE_SCAN);
        optRequest.setOrderNo(waybillNos);
        handleOpt(optRequest);

        this.addNetworkTask(waybillNos, arriveBy, merchantId);

    }


    /**
     * 根据运单号list、收件人更新网点自提任务
     *
     * @param waybillNos
     * @param arriveBy
     * @param merchantId
     */
    private void addNetworkTask(List<String> waybillNos, String arriveBy, String merchantId) {

        List<UserNetworkDO> userNetworkDOList = userNetworkDao.queryByUserId(Long.parseLong(arriveBy)); // 网点到件的运单为自提，添加网点任务
        if (userNetworkDOList == null || userNetworkDOList.size() == 0) {
            return;
        }
        for (String waybillNo : waybillNos) {
            DeliveryOrderDO orderDO = deliveryOrderDao.queryByOrderNo(Long.parseLong(merchantId), waybillNo);
            if (StringUtil.equalsIgnoreCase(orderDO.getChannel(), "Y")
                    && !StringUtil.equalsIgnoreCase(orderDO.getIsPackage(), "Y")) {
                Task task = new Task();
                task.setMerchantId(merchantId);
                task.setStatus(TaskStatusEnum.CREATE);
                task.setCreatedBy(arriveBy);
                task.setOrderNo(waybillNo);
                task.setHandledBy("" + userNetworkDOList.get(0).getDistributionNetworkId());
                task.setTaskType(TaskTypeEnum.SELF_DELIVERY);
                taskService.addTask(task);
            }
        }

    }

    @Override
    @Transactional
    public String addPackage(PackageRequest packageRequest) {
        String orderNo = "";
        Long merchant = Long.parseLong(packageRequest.getMerchantId());

        // 判断是否允许打包
        for (String o : packageRequest.getOrderNos()) {
            DeliveryOrder deliveryOrder = queryByOrderNo(packageRequest.getMerchantId(), o);
            if (StringUtil.isNotEmpty(deliveryOrder.getParentNo()) || deliveryOrder.getStatus() != DeliveryOrderStatusEnum.ARRIVED) {
                throw new DMSException(BizErrorCode.PACKAGE_NOT_ALLOW, o);
            }
        }

        // 1、保存订单信息
        DeliveryOrderDO orderHeader = new DeliveryOrderDO();
        orderHeader.setMerchantId(merchant);
        orderHeader.setHigh(packageRequest.getHigh());
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
        deliveryOrderDao.insert(orderHeader);

        // 发件网点信息
        DistributionNetworkDO networkDO = distributionNetworkDao.queryById(new Long(packageRequest.getNetworkId()));
        DeliveryOrderReceiverDO r = new DeliveryOrderReceiverDO();
        r.setOrderNo(orderNo);
        r.setMerchantId(merchant);
        r.setAddress(networkDO.getAddress());
        r.setArea(networkDO.getArea());
        r.setCity(networkDO.getCity());
        r.setContactNumber(networkDO.getContactName());
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
        s.setContactNumber(receiverNetwork.getContactName());
        s.setCountry(receiverNetwork.getCountry());
        s.setName(receiverNetwork.getContactName());
        s.setProvince(receiverNetwork.getProvince());
        deliveryOrderSenderDao.insert(s);

        // 关联包裹与子运单
        for (String o : packageRequest.getOrderNos()) {
            DeliveryOrderDO update = new DeliveryOrderDO();
            update.setMerchantId(merchant);
            update.setOrderNo(o);
            update.setParentNo(orderNo);
            deliveryOrderDao.update(update);
        }

        // 添加操作日志
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOrderNo(packageRequest.getOrderNos());
        optRequest.setOptType(OptTypeEnum.PACKAGE);
        optRequest.setOptBy(packageRequest.getOptBy());
        optRequest.setNetworkId("" + packageRequest.getNetworkId());
        optRequest.setMerchantId(packageRequest.getMerchantId());
        orderOptLogService.addOptLog(optRequest);
        return orderNo;
    }

    @Override
    @Transactional
    public void unpack(UnpackRequest unpackRequest) {


        Iterator<String> iterator = unpackRequest.getOrderNos().iterator();
        // 判断是否允许拆包
        for (; iterator.hasNext(); ) {
            String orderNo = iterator.next();
            DeliveryOrder deliveryOrder = queryByOrderNo(unpackRequest.getMerchantId(), orderNo);
            if (StringUtil.isEmpty(deliveryOrder.getParentNo()) && !deliveryOrder.isPackage()) {
                throw new DMSException(BizErrorCode.PACKAGE_NOT_ALLOW, orderNo);
            }
            if (deliveryOrder.isPackage()) {
                OrderOptRequest optRequest = new OrderOptRequest();
                optRequest.setMerchantId(unpackRequest.getMerchantId());
                optRequest.setOptBy(unpackRequest.getOptBy());
                optRequest.setOptType(OptTypeEnum.RECEIVE);
                optRequest.setOrderNo(Arrays.asList(new String[]{orderNo}));
                optRequest.setNetworkId("" + unpackRequest.getNetworkId());
                handleOpt(optRequest);
                iterator.remove();
            }
        }
        //1、到件
        waybillNoListArrive(unpackRequest.getOrderNos(), unpackRequest.getOptBy(), unpackRequest.getMerchantId());

        // 2、添加操作日志
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setOrderNo(unpackRequest.getOrderNos());
        optRequest.setOptType(OptTypeEnum.UNPACK);
        optRequest.setOptBy(unpackRequest.getOptBy());
        optRequest.setNetworkId("" + unpackRequest.getNetworkId());
        optRequest.setMerchantId(unpackRequest.getMerchantId());
        orderOptLogService.addOptLog(optRequest);

        for (String o : unpackRequest.getOrderNos()) {
            //3、清除 大包号
            DeliveryOrderDO update = new DeliveryOrderDO();
            update.setMerchantId(Long.parseLong(unpackRequest.getMerchantId()));
            update.setOrderNo(o);
            update.setParentNo("");
            deliveryOrderDao.update(update);
        }
    }

    @Override
    public List<DeliveryOrder> queryByPackageNo(String merchantNo, String packageNo) {
        List<DeliveryOrderDO> queryList = deliveryOrderDao.queryByPackageNo(Long.parseLong(merchantNo), packageNo);

        List<DeliveryOrder> list = new ArrayList<>();
        if (queryList == null)
            return list;
        for (DeliveryOrderDO d : queryList) {
            list.add(convert(d));
        }
        return list;
    }

    private void updateDeliveryOrderStatus(OrderOptRequest optRequest, String orderNo, OrderHandleConfig handleConfig) {
        long affected = 0;
        // 更新订单信息，循环10次，10次未更新成功，则跳出
        for (int i = 0; i < 10; i++) {
            DeliveryOrderDO query = deliveryOrderDao.queryByOrderNo(Long.parseLong(optRequest.getMerchantId()),
                    orderNo);
            DeliveryOrderDO update = new DeliveryOrderDO();
            update.setMerchantId(query.getMerchantId());
            update.setOrderNo(orderNo);
            update.setStatus(handleConfig.getUpdateStatus());
            update.setVersion(query.getVersion());
            // 更新订单信息
            affected = deliveryOrderDao.update(update);
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

    private void sendPhoneSMS(String merchantId, String optType, DeliveryOrderDO query) {
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

    /**
     * 通知Kiliboss状态变更
     *
     * @param orderNo
     * @param referenceNo
     * @param status
     */
    private void notifyStatusUpdate(OrderOptRequest optRequest, String orderNo, String referenceNo,
                                    DeliveryOrderStatusEnum status) {
        String convertResult = StatusConvert.convert(status);
        if (convertResult == null) return;

        try {
            NotifyRequest notify = new NotifyRequest();

            MerchantConfig merchantConfig = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + optRequest.getMerchantId()),
                    MerchantConfig.class);
            InterfaceConfig interfaceConfig = JSON.parseObject(
                    RedisUtil.hget(Constant.INTERFACE_CONF + optRequest.getMerchantId(), "update_status"), InterfaceConfig.class);
            if (interfaceConfig == null) {
                return;
            }
            notify.setOrderNo(orderNo);
            notify.setReferenceNo(referenceNo);
            notify.setMerchantId(optRequest.getMerchantId());
            notify.setMethod(interfaceConfig.getOp());
            notify.setUrl(interfaceConfig.getUrl());
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("waybill_number", orderNo);
            dataMap.put("status", convertResult);
            UserInfo userInfo = userService.findUserInfoByUserId(optRequest.getMerchantId(), optRequest.getOptBy());
            dataMap.put("opt_by", userInfo.getName());
            dataMap.put("network", optRequest.getNetworkId());
            dataMap.put("remark", optRequest.getRemark());
            String data = JSON.toJSONString(dataMap);
            notify.setData(data);
            notify.setSign(createSign(merchantConfig.getKey(), data));
            notifyMerchantProducer.sendMessage(notify);
        } catch (Exception e) {
            logger.error("Send Message Failed. orderNo:{}", orderNo, e);
        }
    }

    private static class StatusConvert {
        private static Map<DeliveryOrderStatusEnum, String> convertRelation = new HashMap<>();

        static {

            convertRelation.put(DeliveryOrderStatusEnum.DELIVERY, "180");
            convertRelation.put(DeliveryOrderStatusEnum.SEND, "185");
            convertRelation.put(DeliveryOrderStatusEnum.PICK_UP, "210");
            convertRelation.put(DeliveryOrderStatusEnum.RECEIVED, "190");
            convertRelation.put(DeliveryOrderStatusEnum.PROBLEM, "197");
        }

        public static String convert(DeliveryOrderStatusEnum status) {
            if (convertRelation.containsKey(status)) {
                return convertRelation.get(status);
            } else {
                return null;
            }
        }
    }


    private String createSign(String key, String data) {
        return new String(DigestUtils.md5Hex(key + data + key).toUpperCase());
    }

    private DeliveryOrder convert(DeliveryOrderDO d) {
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderNo(d.getOrderNo());
        deliveryOrder.setReferenceNo(d.getReferenceNo());
        deliveryOrder.setCreatedTime(d.getCreatedTime());
        deliveryOrder.setUpdatedTime(d.getUpdatedTime());
        deliveryOrder.setCountry(d.getCountry());
        deliveryOrder.setMerchantId("" + d.getMerchantId());
        deliveryOrder.setOrderTime(d.getOrderTime());
        deliveryOrder.setOrderType(d.getOrderType());
        deliveryOrder.setOrderPlatform(d.getOrderPlatform());
        deliveryOrder.setTotalPrice(d.getTotalPrice());
        deliveryOrder.setNeedPayAmount(d.getNeedPayAmount());
        deliveryOrder.setStatus(DeliveryOrderStatusEnum.getEnum(d.getStatus()));
        deliveryOrder.setServiceType(ServiceTypeEnum.getEnum(d.getServiceType()));
        deliveryOrder.setWeight(d.getWeight());
        deliveryOrder.setGoodsType(d.getGoodsType());
        String orderTypeDesc = SystemCodeUtil.getCodeVal("" + d.getMerchantId(), Constant.DELIVERY_ORDER_TYPE,
                d.getOrderType());
        deliveryOrder.setOrderTypeDesc(orderTypeDesc);

        deliveryOrder.setWarehouseId(d.getWarehouseId());
        deliveryOrder.setStop(d.getStop());
        deliveryOrder.setStopId(d.getStopId());
        deliveryOrder.setChannel(d.getChannel());
        deliveryOrder.setChannelStation(d.getChannelStation());
        deliveryOrder.setOrderCategory(d.getOrderCategory());
        deliveryOrder.setCarrierId(d.getCarrierId());
        deliveryOrder.setCarrierName(d.getCarrierName());
        deliveryOrder.setRelationOrderNo(d.getRelationOrderNo());
        deliveryOrder.setDeliveryFee(d.getDeliveryFee());
        deliveryOrder.setIsCod(d.getIsCod());
        deliveryOrder.setNotes(d.getNotes());
        deliveryOrder.setRemark(d.getRemark());
        deliveryOrder.setNeedPayAmount(d.getNeedPayAmount());
        deliveryOrder.setPaidType(DeliveryOrderPaidTypeEnum.getEnum(d.getStatus()));

        deliveryOrder.setParentNo(d.getParentNo());
        deliveryOrder.setLength(d.getLength());
        deliveryOrder.setWidth(d.getWidth());
        deliveryOrder.setHigh(d.getHigh());
        deliveryOrder.setNetworkId(d.getNetworkId());
        deliveryOrder.setNextNetworkId(d.getNextNetworkId());

        if (d.getNetworkId() != null) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + d.getMerchantId(), "" + d.getNetworkId()), DistributionNetworkDO.class);
            deliveryOrder.setNetworkDesc(networkDO.getName());
        }
        if (d.getNextNetworkId() != null) {
            DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + d.getMerchantId(), "" + d.getNextNetworkId()), DistributionNetworkDO.class);
            deliveryOrder.setNextNetworkDesc(networkDO.getName());
        }
        deliveryOrder.setPackage(StringUtil.equalsIgnoreCase(d.getIsPackage(), Constant.IS_PACKAGE));
        deliveryOrder.setCreatedBy(d.getCreatedBy());
        deliveryOrder.setPrintTimes(d.getPrintTimes());
        return deliveryOrder;
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

    private DeliveryOrderDO convert(DeliveryOrder d) {

        DeliveryOrderDO orderHeader = new DeliveryOrderDO();
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
        orderHeader.setServiceType(d.getServiceType().getCode());
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

    private void verifyDeliveryOrderParam(DeliveryOrder data) {

        AssertUtil.isNotNull(data, SysErrorCode.REQUEST_IS_NULL);

        AssertUtil.isNotBlank(data.getReferenceNo(), BizErrorCode.REFERENCE_NO_EMPTY);
        AssertUtil.isNotBlank(data.getOrderType(), BizErrorCode.ORDER_TYPE_EMPTY);

        AssertUtil.isNotBlank(data.getGoodsType(), BizErrorCode.GOODS_TYPE_EMPTY);

        AssertUtil.isNotNull(data.getReceiverInfo(), BizErrorCode.RECEIVER_EMPTY);

        AssertUtil.isNotBlank(data.getReceiverInfo().getReceiverName(), BizErrorCode.RECEIVE_NAME_EMPTY);
        AssertUtil.isNotBlank(data.getReceiverInfo().getReceiverPhone(), BizErrorCode.RECEIVE_PHONE_EMPTY);
        AssertUtil.isNotBlank(data.getReceiverInfo().getReceiverAddress(), BizErrorCode.RECEIVE_ADDRESS_EMPTY);
    }

	@Override
	public long updatePaidType(DeliveryOrderDO deliveryOrderDO) {

		return deliveryOrderDao.update(deliveryOrderDO);

	}
}
