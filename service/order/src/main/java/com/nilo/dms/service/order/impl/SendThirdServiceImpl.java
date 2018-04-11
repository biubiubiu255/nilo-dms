package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.HandleRiderStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.HandleThirdDao;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserService userService;


    //插入多个小包
    //参数 riderDeliveryDO：主要是需要大包号，以及操作人
    //参数 String[] smallOrders：小包号order数组
    @Override
    public void insertSmallAll(Long merchantId , String handleNo, String[] smallOrders) {
        //以小包DO的order，调用deliveryOrderDao接口查询多条该订单的信息
        //然后把查询到的list中每条信息，比如weight等合并到小包list的每条信息中，这里用的是bean合并，因为常用字段都一样
        if(handleNo==null){
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        List<Waybill> list  = waybillService.queryByOrderNos(merchantId.toString(), Arrays.asList(smallOrders));
        List<SendThirdDetail> dataList = new ArrayList<SendThirdDetail>();
        for (Waybill e : list){
            SendThirdDetail sendThirdDetail = new SendThirdDetail();
            org.springframework.beans.BeanUtils.copyProperties(e, sendThirdDetail);
            sendThirdDetail.setThirdHandleNo(handleNo);
            sendThirdDetail.setOrderNo(e.getOrderNo());
            dataList.add(sendThirdDetail);
            //System.out.println("包裹信息 = " + riderDeliverySmallDO.toString());
        }
        handleThirdDao.insertSmalls(dataList);
    }

    //这里是插入一个大包记录，没有别的注意，有什么写入什么
    @Override
    public void insertBig(SendThirdHead sendThirdHead) {
        if(sendThirdHead.getStatus()==null){
            sendThirdHead.setStatus(HandleRiderStatusEnum.SAVA.getCode());
        }
        handleThirdDao.insertBig(sendThirdHead);
    }

    //大包的status代表0 保存而已、1 分派成功
    //这里是同时写入大包和所有小包
    @Override
    @Transactional
    public void insertBigAndSmall(Long merchantId, SendThirdHead sendThirdHead, String[] smallOrders) {
        if(sendThirdHead.getHandleNo()==null || sendThirdHead.equals("")){
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        insertBig(sendThirdHead);
        insertSmallAll(merchantId, sendThirdHead.getHandleNo(), smallOrders);
    }

    //根据大包号，获取子包list，并且自动渲染driver、操作人名字
    @Override
    public List<SendThirdHead> queryBigs(Long merchantId , SendThirdHead sendThirdHead, Pagination page) {
        List<SendThirdHead> list = handleThirdDao.queryBig(sendThirdHead, page.getOffset(), page.getLimit());
        //page.setTotalCount(commonDao.lastFoundRows());
        page.setTotalCount(handleThirdDao.queryBigCount(sendThirdHead, page.getOffset(), page.getLimit()));
        Set<String> userIds = new HashSet<>();
        for (int i=0;i<list.size();i++){
            userIds.add(list.get(i).getHandleBy().toString());
        }
        //查询出当前大包结果list中所有成员id（主要是快递员ID和操作人ID）
        //然后再查出这些ID对应的个人信息（主要是取名字）
        List<UserInfo> riderInfoList  = userService.findUserInfoByUserIds(merchantId.toString(), new ArrayList<>(userIds));

        //这里两个for循环是将list结果中与当前成员表（riderInfoList、opNameInfoList）中对应的ID找到，然后赋值name
        for (int i=0;i<list.size();i++){
            for (UserInfo e : riderInfoList){
                if (e.getUserId().equals(list.get(i).getHandleBy().toString())){
                    SendThirdHead tempSend = list.get(i);
                    tempSend.setHandleByName(e.getName());
                    list.set(i, tempSend);
                }
            }
        }
        //System.out.println("本次测试 = " + list.toString());
        return list;
    }

    //查询大包下的子包信息，也就是查询到件后，分派很多小包
    //这里的查询是自定义查询，传入的是大包DO，其实也就是需要大包号即可，即可查询到所有小包
    @Override
    public List<SendThirdDetail> querySmalls(String merchantId , SendThirdDetail sendThirdDetail, Pagination page) {
        List<SendThirdDetail> list = handleThirdDao.querySmall(sendThirdDetail);
        return list;
    }

    //查询一个装车单里所有的小包，并且返回的是总表类型的list<DeliveryOrderDO>
    @Override
    public List<Waybill> querySmallsPlus(String merchantId, String handleNo, Pagination page) {
        //新建一个以装车单号为查询条件的DO，返回的即时该装车单里所有的小包
        SendThirdDetail sendThirdDetail = new SendThirdDetail();
        sendThirdDetail.setThirdHandleNo(handleNo);
        List<SendThirdDetail> list = handleThirdDao.querySmall(sendThirdDetail);

        List<String> smallOrders = new ArrayList<String>();
        for (SendThirdDetail e : list){
            smallOrders.add(e.getOrderNo());
        }
        List<Waybill> resList  = waybillService.queryByOrderNos(merchantId, smallOrders);
        return resList;
    }

    @Override
    @Transactional
    public void editSmall(Long merchantId, String handleNo, String[] smallOrders) {
        SendThirdHead sendThirdHead = new SendThirdHead();
        sendThirdHead.setHandleNo(handleNo);

        //查看装车单号是否存在
        List<SendThirdHead> tempList = queryBigs(merchantId, sendThirdHead, new Pagination());
        if (tempList.size()==0){
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }

        SendThirdDetail sendThirdDetail = new SendThirdDetail();
        sendThirdDetail.setThirdHandleNo(handleNo);
        handleThirdDao.deleteSmallByHandleNo(sendThirdDetail);

        insertSmallAll(merchantId, handleNo, smallOrders);
    }

    @Override
    public void editBig(SendThirdHead sendThirdHead) {
        if(sendThirdHead.getHandleNo()==null){
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        handleThirdDao.editBigBy(sendThirdHead);
    }

}
