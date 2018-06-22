package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.NotifyRequest;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.PhoneMessage;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.mq.producer.AbstractMQProducer;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.SendMessageService;
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
    private SendMessageService sendMessageService;
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
        page.setTotalCount(handleRiderDao.queryRiderDeliveryBigCount(riderDelivery));

        Set<Long> userIDList = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            userIDList.add(Long.parseLong(list.get(i).getRider()));
        }
        if(userIDList.size()>0){
            List<StaffDO> staff = staffDao.findstaffByIDs(userIDList.toArray(new Long[userIDList.size()]));
            //这里两个for循环是将list结果中与当前成员表（riderInfoList、opNameInfoList）中对应的ID找到，然后赋值name
            for (RiderDelivery r : list) {
                for (StaffDO s : staff) {
                    if (StringUtil.equals(s.getUserId().toString(), r.getRider())) {
                        r.setRiderName(s.getStaffId() + "-" + s.getRealName());
                    }
                }
            }
        }

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


        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        //这里因为查询是自定义，也就是以有参数，就有什么查询条件查询，所有统为list，这里只需要查一个大包，也只有一条结果，但还是得取get(0)
        List<RiderDelivery> tempList = handleRiderDao.queryRiderDeliveryBig(riderDelivery, 0, 1);
        if (tempList.size() == 0) {
            throw new DMSException(BizErrorCode.LOADING_EMPTY);
        }
        riderDelivery = tempList.get(0);

        handleRiderDao.deleteSmallsByHandleNo(riderDelivery.getMerchantId(), riderDelivery.getHandleNo());

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
        request.setOptType(OptTypeEnum.DELIVERY);
        request.setOrderNo(orderNos);
        request.setRider(riderDelivery.getRider());

        waybillService.handleOpt(request);

        handleRiderDao.upBigStatus(handleNo, HandleRiderStatusEnum.SHIP.getCode());
        handleRiderDao.upSmallStatus(handleNo, HandleRiderStatusEnum.SHIP.getCode());

        UserInfo userInfo = userService.findUserInfoByUserId("" + riderDelivery.getMerchantId(), "" + riderDelivery.getRider());
        //发送短信
        for (RiderDeliverySmallDO d : riderDeliverySmallDOS) {
            Long merchantId=riderDelivery.getMerchantId();
            WaybillDO w = waybillDao.queryByOrderNo(merchantId, d.getOrderNo());
            DeliveryOrderReceiverDO r = deliveryOrderReceiverDao.queryByOrderNo(merchantId, d.getOrderNo());
            sendMessageService.sendMessage(w.getOrderNo(),SendMessageService.RIDER_DELIVERY,r.getContactNumber(),w.getReferenceNo(),userInfo.getName(),userInfo.getPhone());
        }
    }

    @Override
    public List<StaffDO> findUserInfoByUserIds(Long merchantId, Long[] userIDs) {
        return staffDao.findstaffByIDs(userIDs);
    }

    @Override
    public void deleteHandleAndSmalls(String handleNo) {
        AssertUtil.isNotNull(handleNo, BizErrorCode.HandleNO_NOT_EXIST);
        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());
        RiderDelivery riderDelivery = new RiderDelivery();
        riderDelivery.setMerchantId(merchantId);
        riderDelivery.setHandleNo(handleNo);
        List<RiderDelivery> riderDeliveries = queryRiderDelivery(merchantId.toString(), riderDelivery, new Pagination());
        AssertUtil.isTrue(!riderDeliveries.isEmpty(), BizErrorCode.HandleNO_NOT_EXIST);
        handleRiderDao.deleteHandleBy(merchantId, handleNo);
        handleRiderDao.deleteSmallsByHandleNo(merchantId, handleNo);
    }

}
