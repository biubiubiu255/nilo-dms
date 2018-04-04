package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.DeliveryOrderDao;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.RiderDeliveryDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.RiderDeliveryService;
import com.nilo.dms.service.system.SystemConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by admin on 2017/10/31.
 */
@Service
public class RiderDeliveryServiceImpl implements RiderDeliveryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HandleRiderDao handleRiderDao;

    @Autowired
    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonDao commonDao;

    //插入多个小包
    //参数 riderDeliveryDO：主要是需要大包号，以及操作人
    //参数 String[] smallOrders：小包号order数组
    @Override
    public void addRiderPackDetail(Long merchantId , RiderDeliveryDO riderDeliveryDO, String[] smallOrders) {

        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        List<DeliveryOrderDO> list  = deliveryOrderDao.queryByOrderNos(merchantId, Arrays.asList(smallOrders));
        RiderDeliverySmallDO riderDeliverySmallDO = new RiderDeliverySmallDO();
        for (DeliveryOrderDO e : list){
            org.springframework.beans.BeanUtils.copyProperties(e, riderDeliverySmallDO);
            riderDeliverySmallDO.setHandleBy(riderDeliverySmallDO.getHandleBy());
            riderDeliverySmallDO.setRider_handle_no(riderDeliveryDO.getHandleNo());
            riderDeliverySmallDO.setHandleBy(Long.valueOf(riderDeliveryDO.getHandleBy()));
            riderDeliverySmallDO.setVersion(riderDeliveryDO.getVersion());
            handleRiderDao.insertSmall(riderDeliverySmallDO);
            //System.out.println("包裹信息 = " + riderDeliverySmallDO.toString());
        }
    }

    //这里是插入一个大包记录，没有别的注意，有什么写入什么
    @Override
    public void addRiderPack(RiderDeliveryDO riderDeliveryDO) {
        if(riderDeliveryDO.getStatus()==null){
            riderDeliveryDO.setStatus(0);
        }
        handleRiderDao.insertBig(riderDeliveryDO);
    }

    //大包的status代表0 保存而已、1 分派成功
    //这里是同时写入大包和所有小包
    @Override
    public void addRiderPackAndDetail(Long merchantId, RiderDeliveryDO riderDeliveryDO, String[] smallOrders) {
        riderDeliveryDO.setVersion(UUID.randomUUID().toString());
        riderDeliveryDO.setStatus(1);
        addRiderPack(riderDeliveryDO);
        addRiderPackDetail(merchantId, riderDeliveryDO, smallOrders);
    }

    @Override
    public List<RiderDeliveryDO> queryRiderDelivery(String merchantId , RiderDeliveryDO riderDeliveryDO, Pagination page) {
        List<RiderDeliveryDO> list = handleRiderDao.queryRiderDeliveryBig(riderDeliveryDO);
        page.setTotalCount(commonDao.lastFoundRows());
        List<String> riders = new ArrayList<String>();
        List<String> opName = new ArrayList<String>();
        for (int i=0;i<list.size();i++){
            riders.add(list.get(i).getRider());
            opName.add(list.get(i).getHandleBy().toString());
        }
        //查询出当前大包结果list中所有成员id（主要是快递员ID和操作人ID）
        //然后再查出这些ID对应的个人信息（主要是取名字）
        List<UserInfo> riderInfoList  = userService.findUserInfoByUserIds(merchantId, riders);
        List<UserInfo> opNameInfoList = userService.findUserInfoByUserIds(merchantId, opName);

        //这里两个for循环是将list结果中与当前成员表（riderInfoList、opNameInfoList）中对应的ID找到，然后赋值name
        for (int i=0;i<list.size();i++){
            for (UserInfo e : riderInfoList){
                if (e.getUserId().equals(list.get(i).getRider())){
                    RiderDeliveryDO riderTemp = list.get(i);
                    riderTemp.setRider(e.getName());
                    list.set(i, riderTemp);
                }
            }
            for (UserInfo e : opNameInfoList){
                if (e.getUserId().equals(list.get(i).getHandleBy().toString())){
                    RiderDeliveryDO riderTemp = list.get(i);
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
    public List<RiderDeliverySmallDO> queryRiderDeliveryDetail(String merchantId , RiderDeliverySmallDO riderDeliverySmallDO, Pagination page) {
        List<RiderDeliverySmallDO> list = handleRiderDao.queryDeliverySmall(riderDeliverySmallDO);
        return list;
    }


}
