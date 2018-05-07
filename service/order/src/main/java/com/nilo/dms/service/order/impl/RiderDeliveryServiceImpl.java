package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.order.NotifyRequest;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.PhoneMessage;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.order.WaybillService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by admin on 2017/10/31.
 */
@Service
public class RiderDeliveryServiceImpl implements RiderDeliveryService {
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
    private UserService userService;

    @Autowired
    WaybillService waybillService;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private DeliveryRouteService deliveryRouteService;


    //插入多个小包
    //参数 riderDeliveryDO：主要是需要大包号，以及操作人
    //参数 String[] smallOrders：小包号order数组
    @Override
    public void addRiderPackDetail(Long merchantId, String handleNo, String[] smallOrders) {
        insertSmalls(merchantId, handleNo, smallOrders);
    }

    //这里是插入一个大包记录，没有别的注意，有什么写入什么
    @Override
    public void addRiderPack(RiderDelivery riderDelivery) {
        RiderDelivery RiderDeliveryCl = new RiderDelivery();
        BeanUtils.copyProperties(riderDelivery, RiderDeliveryCl);
        RiderDeliveryCl.setStatus(HandleRiderStatusEnum.SAVA.getCode());
        handleRiderDao.insertBig(RiderDeliveryCl);
    }

    //大包的status代表0 保存而已、1 分派成功
    //这里是同时写入大包和所有小包
    @Override
    @Transactional
    public void addRiderPackAndDetail(Long merchantId, RiderDelivery riderDelivery, String[] smallOrders) {
        addRiderPack(riderDelivery);
        addRiderPackDetail(merchantId, riderDelivery.getHandleNo(), smallOrders);
    }

    //根据大包号，获取子包list，并且自动渲染driver、操作人名字
    @Override
    public List<RiderDelivery> queryRiderDelivery(String merchantId, RiderDelivery riderDelivery, Pagination page) {
        List<RiderDelivery> list = handleRiderDao.queryRiderDeliveryBig(riderDelivery, page.getOffset(), page.getLimit());
        //page.setTotalCount(commonDao.lastFoundRows());
        page.setTotalCount(handleRiderDao.queryRiderDeliveryBigCount(riderDelivery, page.getOffset(), page.getLimit()));
        Set<Long> userIds = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            userIds.add(Long.parseLong(list.get(i).getRider()));
            userIds.add(Long.parseLong(list.get(i).getHandleBy().toString()));
        }


        //查询出当前大包结果list中所有成员id（主要是快递员ID和操作人ID）
        //然后再查出这些ID对应的个人信息（主要是取名字）

        Long[] userIDArr = new Long[userIds.size()];
        userIds.toArray(userIDArr);
        List<StaffDO> userInfoByUserIds = findUserInfoByUserIds(Long.parseLong(merchantId), userIDArr);

        //String[] userIDArrStr = new String[userIds.size()];
        List<String> userIDList = new ArrayList<String>();
        for (Long e : userIds) {
            userIDList.add(e.toString());
        }

        List<UserInfo> userInfoByUserIdStrs = userService.findUserInfoByUserIds(merchantId, userIDList);

        //这里两个for循环是将list结果中与当前成员表（riderInfoList、opNameInfoList）中对应的ID找到，然后赋值name
        for (int i = 0; i < list.size(); i++) {
            for (StaffDO e : userInfoByUserIds) {
                if (e.getUserId().equals(Long.parseLong(list.get(i).getRider()))) {
                    RiderDelivery riderTemp = list.get(i);
                    riderTemp.setRiderName(e.getNickName());
                    list.set(i, riderTemp);
                }
            }
            for (UserInfo e : userInfoByUserIdStrs) {
                if (e.getUserId().equals(list.get(i).getHandleBy().toString())) {
                    RiderDelivery riderTemp = list.get(i);
                    riderTemp.setHandleByName(e.getName());
                    list.set(i, riderTemp);
                }
            }


        }

        //System.out.println("本次测试 = " + list.toString());
        return list;
    }

    //查询大包下的子包信息，也就是查询到件后，分派很多小包
    //这里的查询是自定义查询，传入的是大包DO，其实也就是需要大包号即可，即可查询到所有小包
    @Override
    public List<RiderDeliverySmallDO> queryRiderDeliveryDetail(String merchantId, RiderDeliverySmallDO riderDeliverySmallDO, Pagination page) {
        List<RiderDeliverySmallDO> list = handleRiderDao.queryDeliverySmall(riderDeliverySmallDO);
        return list;
    }

    //查询大包下的子包信息，也就是查询到件后，分派很多小包
    //这里的查询是自定义查询，传入的是大包DO，其实也就是需要大包号即可，即可查询到所有小包，这里的小包信息更全，也就是总表的信息
    @Override
    public List<WaybillDO> queryRiderDeliveryDetailPlus(String merchantId, RiderDelivery riderDelivery, Pagination page) {
        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        riderDelivery.setHandleNo(riderDelivery.getHandleNo());
        riderDeliverySmallDO.setHandleNo(riderDelivery.getHandleNo());
        List<RiderDeliverySmallDO> list = handleRiderDao.queryDeliverySmall(riderDeliverySmallDO);
        List<String> smallOrders = new ArrayList<String>();
        for (RiderDeliverySmallDO e : list) {
            smallOrders.add(e.getOrderNo());
        }
        List<WaybillDO> resList = waybillDao.queryByOrderNos(Long.valueOf(merchantId), smallOrders);
        return resList;
    }

    @Override
    @Transactional
    public void editRiderPackAndDetail(RiderDelivery riderDelivery, String[] smallOrders) {

        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        //这里因为查询是自定义，也就是以有参数，就有什么查询条件查询，所有统为list，这里只需要查一个大包，也只有一条结果，但还是得取get(0)
        List<RiderDelivery> tempList = handleRiderDao.queryRiderDeliveryBig(riderDelivery, 0, 1);
        if (tempList.size() == 0) {
            throw new DMSException(BizErrorCode.LOADING_EMPTY);
        }
        riderDelivery = tempList.get(0);

        riderDeliverySmallDO.setHandleNo(riderDelivery.getHandleNo());
        handleRiderDao.deleteSmallByHandleNo(riderDeliverySmallDO);
        insertSmalls(riderDelivery.getMerchantId(), riderDelivery.getHandleNo(), smallOrders);

        if (riderDelivery.getHandleNo() == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        handleRiderDao.upBigBy(riderDelivery);
    }

    @Override
    public void insertSmalls(Long merchantId, String handleNo, String[] smallOrders) {

        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        if (handleNo == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        List<WaybillDO> list = waybillDao.queryByOrderNos(merchantId, Arrays.asList(smallOrders));
        List<RiderDeliverySmallDO> datas = new ArrayList<RiderDeliverySmallDO>();

        for (WaybillDO e : list) {
            RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
            org.springframework.beans.BeanUtils.copyProperties(e, riderDeliverySmallDO);
            riderDeliverySmallDO.setHandleNo(handleNo);
            datas.add(riderDeliverySmallDO);
            //System.out.println("包裹信息 = " + riderDeliverySmallDO.toString());
        }
        handleRiderDao.insertSmalls(datas);

    }

    @Override
    public void ship(String handleNo) {

        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setHandleNo(handleNo);
        List<RiderDelivery> riderDeliveries = handleRiderDao.queryRiderDeliveryBig(riderDelivery, 0, 1);
        if (riderDeliveries.size() == 0) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        } else {
            riderDelivery = riderDeliveries.get(0);
        }

        if (riderDelivery.getStatus() == null) {
            throw new IllegalArgumentException("Loading No." + handleNo + " Ship Failed.");
        }
        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        riderDeliverySmallDO.setHandleNo(riderDelivery.getHandleNo());
        List<RiderDeliverySmallDO> riderDeliverySmallDOS = handleRiderDao.queryDeliverySmall(riderDeliverySmallDO);
        if (riderDeliverySmallDOS == null || riderDeliverySmallDOS.size() == 0) {
            throw new DMSException(BizErrorCode.LOADING_EMPTY);
        }

        List<String> orderNos = new ArrayList<>();
        for (RiderDeliverySmallDO d : riderDeliverySmallDOS) {
            orderNos.add(d.getOrderNo());
        }

        OrderOptRequest request = new OrderOptRequest();
        request.setOptType(OptTypeEnum.SEND);
        request.setOrderNo(orderNos);
        waybillService.handleOpt(request);

        handleRiderDao.upBigStatus(handleNo, HandleRiderStatusEnum.SHIP.getCode());
        handleRiderDao.upSmallStatus(handleNo, HandleRiderStatusEnum.SHIP.getCode());

        UserInfo userInfo = userService.findUserInfoByUserId("" + riderDelivery.getMerchantId(), "" + riderDelivery.getHandleBy());
        //发送短信
        for (RiderDeliverySmallDO d : riderDeliverySmallDOS) {
            WaybillDO w = waybillDao.queryByOrderNo(riderDelivery.getMerchantId(), d.getOrderNo());
            DeliveryOrderReceiverDO r = deliveryOrderReceiverDao.queryByOrderNo(riderDelivery.getMerchantId(), d.getOrderNo());
            //送货上门
            if (StringUtil.equals(w.getChannel(), "1")) {
                String content = "Dear customer, your order " + d.getOrderNo() + " has been dispatched today. Your total order amount is Ksh." + w.getNeedPayAmount() + ".Kindly call " + userInfo.getName() + " " + userInfo.getPhone() + " to notify you the time of delivery. Please keep your phone on. Thank you.";
                PhoneMessage message = new PhoneMessage();
                message.setMerchantId("" + riderDelivery.getMerchantId());
                message.setContent(content);
                message.setPhoneNum(r.getContactNumber());
                message.setMsgType(OptTypeEnum.DELIVERY.getCode());
                try {
                    phoneSMSProducer.sendMessage(message);
                } catch (Exception e) {

                }
            }
        }
        deliveryRouteService.addKiliRoute(orderNos, "P30");
    }

    @Override
    public List<StaffDO> findUserInfoByUserIds(Long merchantId, Long[] userIDs) {
        return staffDao.findstaffByIDs(userIDs);
    }

}
