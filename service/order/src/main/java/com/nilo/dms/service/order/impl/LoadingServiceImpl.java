package com.nilo.dms.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.*;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.LoadingDao;
import com.nilo.dms.dao.LoadingDetailsDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.LoadingDO;
import com.nilo.dms.dao.dataobject.LoadingDetailsDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.service.order.model.LoadingDetails;
import com.nilo.dms.service.order.model.OrderOptRequest;
import com.nilo.dms.service.order.model.Task;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by admin on 2017/10/31.
 */
@Service
public class LoadingServiceImpl implements LoadingService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private LoadingDao loadingDao;

    @Autowired
    private LoadingDetailsDao loadingDetailsDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Override
    public String addLoading(Loading loading) {

        //校验 loading 信息
        AssertUtil.isNotBlank(loading.getRider(), BizErrorCode.RIDER_IS_EMPTY);


        String loadingNo = SystemConfig.getNextSerialNo(loading.getMerchantId(), SerialTypeEnum.LOADING_NO.getCode());

        //1、
        loading.setStatus(LoadingStatusEnum.CREATE);
        loading.setLoadingNo(loadingNo);
        LoadingDO loadingDO = convert(loading);
        loadingDO.setLoadingFromTime(DateUtil.getSysTimeStamp());
        loadingDao.insert(loadingDO);
        //2、
        if (loading.getDetailsList() != null) {
            for (LoadingDetails details : loading.getDetailsList()) {
                LoadingDetailsDO detailsDO = convert(details);
                loadingDetailsDao.insert(detailsDO);
            }
        }

        return loadingNo;
    }

    @Override
    public List<Loading> queryBy(String merchantId, String loadingNo, Integer status, Pagination pagination) {

        List<Loading> list = new ArrayList<>();
        List<LoadingDO> loadingDOList = loadingDao.queryListBy(Long.parseLong(merchantId), loadingNo, status, pagination.getOffset(), pagination.getLimit());
        Long count = commonDao.lastFoundRows();
        if (count == null || count == 0) return list;
        pagination.setTotalCount(count);

        Set<String> userIds = new HashSet<>();
        List<String> loadingNos = new ArrayList<>();
        for (LoadingDO d : loadingDOList) {
            userIds.add(d.getRider());
            userIds.add("" + d.getLoadingBy());
            loadingNos.add(d.getLoadingNo());
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.addAll(userIds);

        List<UserInfo> users = userService.findUserInfoByUserIds(merchantId, userIdList);
        List<LoadingDetailsDO> detailsDOs = loadingDetailsDao.queryByLoadingNos(loadingNos);

        for (LoadingDO d : loadingDOList) {
            Loading loading = convert(d);
            List<LoadingDetails> detailsList = new ArrayList<>();
            for (LoadingDetailsDO detailsDO : detailsDOs) {
                if (StringUtil.equals(detailsDO.getLoadingNo(), d.getLoadingNo())) {
                    LoadingDetails details = convert(detailsDO);
                    detailsList.add(details);
                }
            }
            loading.setDetailsList(detailsList);

            for (UserInfo u : users) {
                if (StringUtil.equals(u.getUserId(), d.getRider())) {
                    loading.setRiderName(u.getName());
                }
                if (StringUtil.equals(u.getUserId(), "" + d.getLoadingBy())) {
                    loading.setLoadingName(u.getName());
                }
            }
            list.add(loading);
        }

        return list;
    }


    @Override
    @Transactional
    public Loading queryByLoadingNo(String merchantId, String loadingNo) {

        LoadingDO loadingDO = loadingDao.queryByLoadingNo(Long.parseLong(merchantId), loadingNo);
        if (loadingDO == null) {
            return null;
        }

        Loading loading = convert(loadingDO);
        UserInfo riderInfo = userService.findUserInfoByUserId(merchantId, loading.getRider());
        UserInfo loadingInfo = userService.findUserInfoByUserId(merchantId, loading.getLoadingBy());

        loading.setRiderName(riderInfo == null ? "" : riderInfo.getName());
        loading.setLoadingName(loadingInfo == null ? "" : loadingInfo.getName());

        List<LoadingDetailsDO> detailsDOList = loadingDetailsDao.queryByLoadingNo(loadingDO.getLoadingNo());
        List<LoadingDetails> detailsList = new ArrayList<>();
        int inx = 0;
        for (LoadingDetailsDO detailsDO : detailsDOList) {
            inx++;
            LoadingDetails details = convert(detailsDO);
            details.setNum(inx);
            details.setDeliveryOrder(orderService.queryByOrderNo(merchantId, details.getOrderNo()));
            detailsList.add(details);
        }
        loading.setDetailsList(detailsList);
        return loading;
    }

    @Override
    @Transactional
    public void loadingScan(String merchantId, String loadingNo, String orderNo, String optBy) {

        LoadingDO loadingDO = loadingDao.queryByLoadingNo(Long.parseLong(merchantId), loadingNo);
        if (loadingDO == null) {
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST, loadingNo);
        }
        if (loadingDO.getStatus() == LoadingStatusEnum.SHIP.getCode()) {
            throw new DMSException(BizErrorCode.LOADING_STATUS_LIMITED, loadingNo);
        }

        User optUser = userService.findByUserId(merchantId, optBy);

        //修改订单信息
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(optBy);
        optRequest.setOptType(OptTypeEnum.LOADING);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        //参数
        Map<String, String> args = new HashMap<>();
        args.put("0", optUser.getUserInfo().getName());
        optRequest.setParams(args);
        //loading不更新订单状态
        //orderService.handleOpt(optRequest);

        //添加订单到发运明细中
        LoadingDetailsDO detailsDO = new LoadingDetailsDO();
        detailsDO.setStatus(LoadingStatusEnum.CREATE.getCode());
        detailsDO.setOrderNo(orderNo);
        detailsDO.setLoadingBy(Long.parseLong(optBy));
        detailsDO.setLoadingNo(loadingNo);
        loadingDetailsDao.insert(detailsDO);

        if (loadingDO.getStatus() == LoadingStatusEnum.CREATE.getCode()) {
            // 更新发运状态
            LoadingDO update = new LoadingDO();
            update.setId(loadingDO.getId());
            update.setLoadingNo(loadingDO.getLoadingNo());
            update.setVersion(loadingDO.getVersion());
            update.setStatus(LoadingStatusEnum.LOADING.getCode());
            loadingDao.update(update);
        }
    }

    @Override
    @Transactional
    public void deleteLoadingDetails(String merchantId, String loadingNo, String orderNo, String optBy) {

        LoadingDO loadingDO = loadingDao.queryByLoadingNo(Long.parseLong(merchantId), loadingNo);
        if (loadingDO == null) {
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST, loadingNo);
        }
        if (!(loadingDO.getStatus() == LoadingStatusEnum.CREATE.getCode() || loadingDO.getStatus() == LoadingStatusEnum.LOADING.getCode())) {
            throw new DMSException(BizErrorCode.LOADING_STATUS_LIMITED, loadingNo);
        }

        List<LoadingDetailsDO> detailsDO = loadingDetailsDao.queryByLoadingNo(loadingNo);
        if (detailsDO == null || detailsDO.size() == 0) {
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST);
        }
        boolean exist = false;
        for (LoadingDetailsDO d : detailsDO) {
            if (d.getOrderNo().equals(orderNo)) {
                exist = true;
            }
        }
        if (!exist) {
            throw new DMSException(BizErrorCode.ORDER_NOT_EXIST, orderNo);
        }

        User optUser = userService.findByUserId(merchantId, optBy);


        //修改订单状态
        OrderOptRequest optRequest = new OrderOptRequest();
        optRequest.setMerchantId(merchantId);
        optRequest.setOptBy(optBy);
        optRequest.setOptType(OptTypeEnum.LOADING_CANCEL);
        List<String> orderNoList = new ArrayList<>();
        orderNoList.add(orderNo);
        optRequest.setOrderNo(orderNoList);
        //参数
        Map<String, String> args = new HashMap<>();
        args.put("0", optUser.getUserInfo().getName());
        optRequest.setParams(args);

        //loading 不更新状态
        //orderService.handleOpt(optRequest);

        loadingDetailsDao.deleteBy(loadingNo, orderNo);

    }

    @Override
    @Transactional
    public void ship(String merchantId, String loadingNo, String optBy) {

        LoadingDO loadingDO = loadingDao.queryByLoadingNo(Long.parseLong(merchantId), loadingNo);

        //快递员信息
        User rider = userService.findByUserId(merchantId, loadingDO.getRider());

        if (loadingDO == null) {
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST, loadingNo);
        }
        if (loadingDO.getStatus() != LoadingStatusEnum.LOADING.getCode()) {
            throw new DMSException(BizErrorCode.LOADING_STATUS_LIMITED, loadingNo);
        }
        List<LoadingDetailsDO> detailsDO = loadingDetailsDao.queryByLoadingNo(loadingNo);
        if (detailsDO == null || detailsDO.size() == 0) {
            throw new DMSException(BizErrorCode.LOADING_EMPTY, loadingNo);
        }

        for (LoadingDetailsDO details : detailsDO) {

            //修改订单状态为发运
            OrderOptRequest optRequest = new OrderOptRequest();
            optRequest.setMerchantId(merchantId);
            optRequest.setOptBy(optBy);
            Map<String, String> args = new HashMap<>();
            if(StringUtil.isNotEmpty(loadingDO.getNextStation())) {
                //参数
                DistributionNetworkDO networkDO = JSON.parseObject(RedisUtil.hget(Constant.NETWORK_INFO + merchantId, "" + loadingDO.getNextStation()), DistributionNetworkDO.class);
                args.put("0", networkDO.getName());
                optRequest.setParams(args);

                optRequest.setOptType(OptTypeEnum.SEND);
            }else{
                //参数
                args.put("0", rider.getUserInfo().getName());
                args.put("1", rider.getUserInfo().getPhone());
                optRequest.setParams(args);
                optRequest.setOptType(OptTypeEnum.DELIVERY);
            }
            List<String> orderNoList = new ArrayList<>();
            orderNoList.add(details.getOrderNo());
            optRequest.setOrderNo(orderNoList);



            orderService.handleOpt(optRequest);

            //添加发运任务
            Task task = new Task();
            task.setMerchantId(merchantId);
            task.setStatus(TaskStatusEnum.CREATE);
            task.setCreatedBy(optBy);
            task.setOrderNo(details.getOrderNo());
            task.setHandledBy(loadingDO.getRider());
            if (StringUtil.isNotEmpty(loadingDO.getNextStation())) {
                task.setTaskType(TaskTypeEnum.SEND);
            } else {
                task.setTaskType(TaskTypeEnum.DISPATCH);
            }
            taskService.addTask(task);
        }
        // 更新发运状态
        LoadingDO update = new LoadingDO();
        update.setId(loadingDO.getId());
        update.setLoadingNo(loadingDO.getLoadingNo());
        update.setVersion(loadingDO.getVersion());
        update.setStatus(LoadingStatusEnum.SHIP.getCode());
        update.setLoadingToTime(DateUtil.getSysTimeStamp());
        loadingDao.update(update);
    }

    @Override
    public void deleteLoading(String merchantId, String loadingNo, String optBy) {
        LoadingDO loadingDO = loadingDao.queryByLoadingNo(Long.parseLong(merchantId), loadingNo);
        if (loadingDO == null) {
            throw new DMSException(BizErrorCode.LOADING_NOT_EXIST, loadingNo);
        }
        if (!(loadingDO.getStatus() == LoadingStatusEnum.CREATE.getCode() || loadingDO.getStatus() == LoadingStatusEnum.LOADING.getCode())) {
            throw new DMSException(BizErrorCode.LOADING_STATUS_LIMITED, loadingNo);
        }
        // 更新发运状态
        LoadingDO update = new LoadingDO();
        update.setLoadingNo(loadingNo);
        update.setStatus(LoadingStatusEnum.CANCEL.getCode());
        loadingDao.update(update);
    }

    private LoadingDO convert(Loading l) {
        if (l == null) {
            return null;
        }
        LoadingDO loading = new LoadingDO();
        loading.setStatus(l.getStatus().getCode());
        loading.setCarrier(l.getCarrier());
        loading.setRider(l.getRider());
        loading.setLoadingBy(Long.parseLong(l.getLoadingBy()));
        loading.setLoadingFromTime(l.getLoadingFromTime());
        loading.setLoadingNo(l.getLoadingNo());
        loading.setLoadingToTime(l.getLoadingToTime());
        loading.setMerchantId(Long.parseLong(l.getMerchantId()));
        loading.setRemark(l.getRemark());
        loading.setTruckNo(l.getTruckNo());
        loading.setTruckType(l.getTruckType());

        loading.setNextStation(l.getNextStation());

        return loading;
    }

    private Loading convert(LoadingDO l) {
        if (l == null) {
            return null;
        }
        Loading loading = new Loading();
        loading.setStatus(LoadingStatusEnum.getEnum(l.getStatus()));
        loading.setCarrier(l.getCarrier());
        loading.setRider(l.getRider());
        loading.setLoadingBy("" + l.getLoadingBy());
        loading.setLoadingFromTime(l.getLoadingFromTime());
        loading.setLoadingNo("" + l.getLoadingNo());
        loading.setLoadingToTime(l.getLoadingToTime());
        loading.setMerchantId("" + l.getMerchantId());
        loading.setRemark(l.getRemark());
        loading.setTruckNo(l.getTruckNo());
        loading.setTruckType(l.getTruckType());
        loading.setNextStation(l.getNextStation());

        return loading;
    }

    private LoadingDetails convert(LoadingDetailsDO detailsDO) {
        if (detailsDO == null) {
            return null;
        }
        LoadingDetails details = new LoadingDetails();
        details.setLoadingNo("" + detailsDO.getLoadingNo());
        details.setLoadingBy(detailsDO.getLoadingBy());
        details.setOrderNo(detailsDO.getOrderNo());
        details.setStatus(detailsDO.getStatus());
        return details;
    }

    private LoadingDetailsDO convert(LoadingDetails details) {
        if (details == null) {
            return null;
        }
        LoadingDetailsDO detailsDO = new LoadingDetailsDO();
        detailsDO.setLoadingNo(details.getLoadingNo());
        detailsDO.setLoadingBy(details.getLoadingBy());
        detailsDO.setOrderNo(details.getOrderNo());
        detailsDO.setStatus(details.getStatus());
        return detailsDO;
    }
}
