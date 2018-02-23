package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.ImageStatusEnum;
import com.nilo.dms.common.enums.ImageTypeEnum;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.dao.ImageDao;
import com.nilo.dms.dao.dataobject.ImageDO;
import com.nilo.dms.service.FileService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.SignForOrderParam;
import com.nilo.dms.web.controller.BaseController;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/mobile/rider/sign")
public class SignScanController extends BaseController {
	@Autowired
	private RiderOptService riderOptService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private FileService fileService;
	
    @Value("#{configProperties['temp_photo_file_path']}")
	private static final String path = "";

	private static final String[] suffixNameAllow = new String[] { ".jpg", ".png" };

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageDao imageDao;

	// customers 客户
	@RequestMapping(value = "/sign.html")
	public String customers() {

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
				System.out.print("【========================】" + path);

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

		if (orderNo == null) {
			return toJsonErrorMsg("错误信息");
		}
		Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
		String merchantId = me.getMerchantId();
		DeliveryOrder deliveryOrder = orderService.queryByOrderNo(merchantId, orderNo);

		//model.addAttribute("deliveryOrder", deliveryOrder);
		if(deliveryOrder==null) {
			return toJsonErrorMsg("orderNo is error");
		}
		return toJsonTrueData(deliveryOrder);

	}

}
