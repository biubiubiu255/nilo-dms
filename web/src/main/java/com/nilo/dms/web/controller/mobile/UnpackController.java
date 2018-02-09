package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.PackageController.NextStation;
import com.nilo.dms.web.controller.order.UnPackageController.UnpackInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/mobile/network/unpackage")
public class UnpackController extends BaseController {

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/unpack.html")
    public String toPackage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
        List<NextStation> list = new ArrayList<>();

        for (DistributionNetworkDO n : networkDOList) {
            NextStation s = new NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            list.add(s);
        }

        model.addAttribute("nextStation", list);

        return "mobile/network/unpackage/unpack";

    }

    @ResponseBody
    @RequestMapping(value = "/scanList.html")
    public String scanList(String scanNo, String packageNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<UnpackInfo> list = new ArrayList<>();
        Pagination pagination = new Pagination(0, 100);

        if (StringUtil.isEmpty(scanNo)) return toPaginationLayUIData(pagination, list);

        List<WaybillScanDetailsDO> scanDetailsDOList = waybillScanDetailsDao.queryByScanNo(scanNo);
        List<DeliveryOrder> orderList = orderService.queryByPackageNo(merchantId, packageNo);
        if (orderList == null) throw new DMSException(BizErrorCode.PACKAGE_NO_ERROR);
        for (DeliveryOrder o : orderList) {
            UnpackInfo i = new UnpackInfo();
            i.setOrderNo(o.getOrderNo());
            i.setOrderType(o.getOrderType());
            i.setReferenceNo(o.getReferenceNo());
            i.setWeight(o.getWeight());
            for (WaybillScanDetailsDO d : scanDetailsDOList) {
                if (StringUtil.equals(d.getOrderNo(), o.getOrderNo())) {
                    i.setArrived(true);
                    break;
                }
            }
            list.add(i);
        }

        pagination.setTotalCount(list.size());
        return toPaginationLayUIData(pagination, list);

    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "/save.html")
    public String save(@RequestParam("logisticsNo") String logisticsNo) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        String arriveByString = me.getUserId();
        String merchantId = me.getMerchantId();
        orderService.waybillNoListArrive(Arrays.asList(new String[]{logisticsNo}), merchantId, me.getUserId());
        return toJsonTrueMsg();
    }

}
