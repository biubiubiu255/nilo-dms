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
import com.nilo.dms.dao.HandleThirdDao;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.SendThirdService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (sendThirdHead.getStatus() == null) {
            sendThirdHead.setStatus(HandleRiderStatusEnum.SAVA.getCode());
        }
        handleThirdDao.insertBig(sendThirdHead);
    }

    //大包的status代表0 保存而已、1 分派成功
    //这里是同时写入大包和所有小包
    @Override
    @Transactional
    public void insertBigAndSmall(Long merchantId, SendThirdHead sendThirdHead, String[] smallOrders) {

        AssertUtil.isNotBlank(sendThirdHead.getThirdExpressCode(), BizErrorCode.THIRD_EXPRESS_EMPTY);
        AssertUtil.isNotBlank(sendThirdHead.getDriver(), BizErrorCode.THIRD_DRIVER_EMPTY);
        AssertUtil.isNotNull(smallOrders, BizErrorCode.WAYBILL_EMPTY);
        AssertUtil.isTrue(smallOrders.length == 0, BizErrorCode.WAYBILL_EMPTY);

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
    public void editSmall(Long merchantId, String handleNo, String[] smallOrders) {
        //查看装车单号是否存在
        SendThirdHead query = handleThirdDao.queryBigByHandleNo(merchantId, handleNo);
        if (query == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        handleThirdDao.deleteSmallByHandleNo(merchantId, handleNo);
        //插入小包
        insertSmallAll(merchantId, handleNo, smallOrders);
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
    }

    @Override
    public void editBig(SendThirdHead sendThirdHead) {
        if (sendThirdHead.getHandleNo() == null) {
            throw new DMSException(BizErrorCode.HandleNO_NOT_EXIST);
        }
        handleThirdDao.editBigBy(sendThirdHead);
    }

}
