package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.PaidTypeEnum;
import com.nilo.dms.service.FileService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.order.model.Waybill;
import com.nilo.dms.service.order.model.SignForOrderParam;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mobile/rider/sign")
public class SignScanController extends BaseController {
    @Autowired
    private RiderOptService riderOptService;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private FileService fileService;

    @Value("#{configProperties['temp_photo_file_path']}")
    private static final String path = "";

    private static final String[] suffixNameAllow = new String[]{".jpg", ".png"};

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/toSign.html")
    public String toSign(Model model) {
        boolean isPaid = false;


        String orderNo = getRequest().getParameter("logisticsNo");
        //如果转输了运单号
        if (orderNo != null) {
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            String merchantId = me.getMerchantId();
            Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);
            if (deliveryOrder != null) {

                String receiverName = deliveryOrder.getReceiverInfo().getReceiverName();
                //运单类型为cod类型
                if ("1".equals(deliveryOrder.getIsCod())) {
                    //有代收货款
                    if (deliveryOrder.getNeedPayAmount() != null && deliveryOrder.getNeedPayAmount() > 0) {

                        double amount = deliveryOrder.getNeedPayAmount();
                        model.addAttribute("amount", amount);
                        //代收货款已收
                        if (deliveryOrder.getAlreadyPaid() != null && deliveryOrder.getAlreadyPaid() > 0) {
                            model.addAttribute("already", deliveryOrder.getAlreadyPaid());
                            isPaid = true;
                            //未收代收货款
                        } else {
                            model.addAttribute("already", 0);
                        }
                        //代收货款为0
                    } else {
                        model.addAttribute("amount", 0);
                        model.addAttribute("already", 0);
                    }
                    model.addAttribute("isCod", 1);
                    //运单不是代收货款类型
                } else {
                    isPaid = true;
                }
                model.addAttribute("receiverName", receiverName);

                //已支付的类型，如果是在线支付，需要AlreadyPaid有值才能签收，其他支付类型，认为已收款。可以签收
                if (deliveryOrder.getPaidType() != null) {
                    model.addAttribute("paidType", deliveryOrder.getPaidType().getDesc());
                    if (deliveryOrder.getPaidType().getCode() != PaidTypeEnum.ONLINE.getCode()) {
                        isPaid = true;
                    }
                }
            }
            model.addAttribute("isPaid", isPaid);
            model.addAttribute("logisticsNo", orderNo);
        }
        return "mobile/rider/sign/sign";
    }

    @RequestMapping(value = "/save.html")
    @ResponseBody
    public String save(MultipartFile file, String logisticsNo, String signer, String remark) {
        SignForOrderParam param = new SignForOrderParam();
        param.setOrderNo(logisticsNo);
        param.setSignBy(signer);
        param.setRemark(remark);
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        // 写入图片
        try {
            if (file != null) {
                fileService.uploadSignImage(me.getMerchantId(), me.getUserId(), logisticsNo, file.getBytes());
                // imageDO.setImageType(ImageTypeEnum.getEnum().getCode());
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        // 写入数据
        try {
            param.setMerchantId(me.getMerchantId());
            param.setOptBy(me.getUserId());
            param.setOrderNo(logisticsNo);
            param.setRemark(remark);
            param.setSignBy(signer);
            riderOptService.signForOrder(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getDetail.html", method = RequestMethod.POST)
    public String getDetail(Model model, String orderNo) {
        Map<String, String> map = new HashMap<String, String>();
        boolean isPaid = false;
        if (orderNo == null) {
            return toJsonErrorMsg("错误信息");
        }
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        String merchantId = me.getMerchantId();
        Waybill deliveryOrder = waybillService.queryByOrderNo(merchantId, orderNo);

        if (deliveryOrder == null) {
            return toJsonErrorMsg("orderNo is error");
        }
        String receiverName = deliveryOrder.getReceiverInfo().getReceiverName();
        if (deliveryOrder != null) {

            //String receiverName = deliveryOrder.getReceiverInfo().getReceiverName();
            //运单类型为cod类型
            if ("1".equals(deliveryOrder.getIsCod())) {
                //有代收货款
                if (deliveryOrder.getNeedPayAmount() != null && deliveryOrder.getNeedPayAmount() > 0) {

                    double amount = deliveryOrder.getNeedPayAmount();
                    map.put("amount", amount + "");
                    //代收货款已收
                    if (deliveryOrder.getAlreadyPaid() != null && deliveryOrder.getAlreadyPaid() > 0) {
                        model.addAttribute("already", deliveryOrder.getAlreadyPaid());
                        isPaid = true;
                        //未收代收货款
                    } else {
                        map.put("already", "0");
                    }
                    //代收货款为0
                } else {
                    map.put("amount", "0");
                    map.put("already", "0");
                }
                map.put("isCod", "1");
            }
            map.put("receiverName", receiverName);

            //已支付的类型，如果是在线支付，需要AlreadyPaid有值才能签收，其他支付类型，认为已收款。可以签收
            if (deliveryOrder.getPaidType() != null) {
                map.put("paidType", deliveryOrder.getPaidType().getDesc());
                if (deliveryOrder.getPaidType().getCode() != PaidTypeEnum.ONLINE.getCode()) {
                    isPaid = true;
                }
            }
        }
        map.put("isPaid", isPaid + "");

        return toJsonTrueData(map);

    }

}
