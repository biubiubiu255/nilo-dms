package com.nilo.dms.service.order.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.PickUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.DeliveryRouteMessage;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.consumer.AbstractMQConsumer;
import com.nilo.dms.service.mq.model.ConsumerDesc;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.service.org.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */
@ConsumerDesc(topic = "delivery_order_route", group = "route_group", filterExpression = "route")
public class RouteConsumer extends AbstractMQConsumer {

    private static Logger logger = LoggerFactory.getLogger(RouteConsumer.class);

    @Autowired
    private DeliveryOrderRouteDao deliveryOrderRouteDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private HandleThirdDao handleThirdDao;

    @Autowired
    private WaybillDao waybillDao;

    @Autowired
    private DeliveryOrderReceiverDao orderReceiverDao;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Autowired
    private HandleSignDao handleSignDao;


    @Override
    public void handleMessage(MessageExt messageExt, Object obj) throws Exception {


        DeliveryRouteMessage message = null;
        try {
            message = (DeliveryRouteMessage) obj;

            OptTypeEnum optTypeEnum = OptTypeEnum.getEnum(message.getOptType());

            for (String orderNo : message.getOrderNo()) {
                //DeliveryOrderRouteDO orderRouteDO = new DeliveryOrderRouteDO();
                WaybillRouteDO routeInfo = new WaybillRouteDO();
                routeInfo.setMerchantId(Integer.parseInt(message.getMerchantId()));
                routeInfo.setOpt(message.getOptType());
                routeInfo.setOptByName(message.getOptName());
                routeInfo.setOrderNo(orderNo);
                //载入当前的orderNo的网点信息和收件人信息
                DistributionNetworkDO headNetwork = distributionNetworkDao.queryById(Long.parseLong(message.getNetworkId()));
                routeInfo.setOptNetwork(headNetwork.getName());
                routeInfo.setCreatedTime(DateUtil.getSysTimeStamp().intValue());
                routeInfo.setUpdatedTime(DateUtil.getSysTimeStamp().intValue());

                switch (optTypeEnum) {
                    case ARRIVE_SCAN: {
                        break;
                    }
                    case DELIVERY: {
                        //UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(message.getMerchantId()), Long.parseLong(message.getRider()));
                        List<StaffDO> staffDOS = staffDao.findstaffByIDs(new Long[]{Long.parseLong(message.getRider())});
                        if (!staffDOS.isEmpty()) {
                            routeInfo.setRider(staffDOS.get(0).getRealName() + "-" + staffDOS.get(0).getStaffId());
                            routeInfo.setRiderPhone(staffDOS.get(0).getPhone());
                        }
                        break;
                    }
                    case SEND: {
                        SendThirdHead bigDO = handleThirdDao.queryHandleBySmallNo(Long.parseLong(message.getMerchantId()), routeInfo.getOrderNo());
                        //handleThirdDao.
                        routeInfo.setNextStation(bigDO.getNextStation());
                        routeInfo.setDriver(bigDO.getDriver());
                        routeInfo.setExpressName(bigDO.getThirdExpressCode());
                        routeInfo.setOptByName(bigDO.getHandleName());
                        routeInfo.setCreatedTime(DateUtil.getSysTimeStamp().intValue());

                        if (bigDO.getNextStation() == null) {
                            WaybillDO waybillDO = waybillDao.queryByOrderNo(Long.parseLong(message.getMerchantId()), routeInfo.getOrderNo());
                            if (waybillDO.getNextNetworkId()!=null){
                                DistributionNetworkDO networkDO = distributionNetworkDao.queryById(waybillDO.getNextNetworkId().longValue());
                                routeInfo.setNextStation(networkDO.getName());
                            }
                        } else {
                            routeInfo.setNextStation(bigDO.getNextStation());
                        }


                        List<WaybillDO> smallBags = waybillDao.queryByPackageNo(routeInfo.getMerchantId().longValue(), routeInfo.getOrderNo());
                        //判断当前是大包，则写入小包轨迹，大包正常往下执行
                        if (smallBags.isEmpty()) {
                            break;
                        }
                        List<String> sonOrders = PickUtil.recombineList(smallBags, "orderNo", String.class);
                        for (String smallNo : sonOrders) {
                            //这里是一个个的小包进行遍历，收件人信息
                            routeInfo.setOrderNo(smallNo);
                            deliveryOrderRouteDao.insert(routeInfo);
                        }
                        routeInfo.setOrderNo(orderNo);
                        //routeInfo.set
                        break;
                    }
                    case SIGN: {
                        HandleSignDO handleSignDO = handleSignDao.queryByNo(Long.parseLong(message.getMerchantId()), orderNo);
                        routeInfo.setSigner(handleSignDO.getSigner());
                        break;
                    }
                    default:
                        break;
                }
                deliveryOrderRouteDao.insert(routeInfo);
            }
        } catch (Exception e) {
            //不进行重复消费
            logger.error("RouteConsume Failed. RouteMessage:{}", message, e);
        }
    }

}
