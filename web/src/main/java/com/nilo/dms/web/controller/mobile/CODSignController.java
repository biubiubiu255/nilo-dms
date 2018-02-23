package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.ImageStatusEnum;
import com.nilo.dms.common.enums.ImageTypeEnum;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.dao.ImageDao;
import com.nilo.dms.dao.dataobject.ImageDO;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.DeliveryOrder;
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


@Controller
@RequestMapping("/mobile/rider/COD")
public class CODSignController extends BaseController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/sign.html")
    public String sign() {
        return "mobile/rider/COD/dshkqs";
    }

    @Autowired
    private RiderOptService riderOptService;

    @Value("#{configProperties['temp_photo_file_path']}")
    private static final String path = "";

    private static final String[] suffixNameAllow = new String[] { ".jpg", ".png" };

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageDao imageDao;

    @RequestMapping(value = "/save.html")
    @ResponseBody

    public String save(MultipartFile file, String logisticsNo, String signer, String idNo, String danxuan) {
        System.out.println(logisticsNo);
        System.out.println(signer);
        System.out.println(idNo);
        System.out.println(danxuan);
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getDetail.html", method = RequestMethod.POST)
    public String getDetail(Model model, String orderNo) {

        if (orderNo == null) {
            return toJsonErrorMsg("错误信息");
        }
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        String merchantId = me.getMerchantId();
        DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);

        //model.addAttribute("deliveryOrder", deliveryOrder);
        System.out.println(deliveryOrder);
        if(deliveryOrder==null) {
            return toJsonErrorMsg("orderNo is error");
        }
        String receiverName = deliveryOrder.getReceiverInfo().getReceiverName();
        return toJsonTrueData(receiverName);

    }
}
