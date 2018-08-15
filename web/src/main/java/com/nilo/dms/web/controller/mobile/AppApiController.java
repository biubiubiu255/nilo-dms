package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.WaybillLogDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dao.dataobject.WaybillRouteDO;
import com.nilo.dms.dto.common.LoginInfo;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.handle.HandleRefuse;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.dto.order.Loading;
import com.nilo.dms.dto.order.ShipParameter;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.FileService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.web.controller.BaseController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/app_api")
public class AppApiController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private WaybillService waybillService;

    @Autowired
    private WaybillOptService waybillOptService;

    @Autowired
    private WaybillLogDao waybillLogDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private DeliveryRouteService deliveryRouteService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/check.html")
    public String check(String code) {
        Long a = waybillLogDao.getStateByOrderNo(code);
        if (a == null) {
            return toJsonErrorMsg("There is no OrderNo");
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/arrive_scan.html")
    @ResponseBody
    public String arrive_scan(String[] scanedCodes) {
        if (scanedCodes.length > 0) {
            waybillService.arrive(new ArrayList<>(Arrays.asList(scanedCodes)));
        }
        return toJsonTrueMsg();
    }


    //下面是签收
    @RequestMapping(value = "/sign.html")
    @ResponseBody
    public String sign(MultipartFile file, String logisticsNo, String signer, String remark) {

        Principal me = SessionLocal.getPrincipal();
        AssertUtil.isNotNull(file, BizErrorCode.SIGN_PHOTO_EMPTY);
        try {
            fileService.uploadSignImage(me.getMerchantId(), me.getUserId(), logisticsNo, file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        // 签收
        waybillOptService.sign(logisticsNo, signer, remark);
        return toJsonTrueMsg();
    }


    //下面是延迟件
    @RequestMapping(value = "/delay.html", method = RequestMethod.POST)
    @ResponseBody
    public String save(DelayParam param) {
        try {
            waybillOptService.delay(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    //下面是拒收件
    @RequestMapping(value = "/refuse.html", method = RequestMethod.POST)
    @ResponseBody
    public String refuse(HandleRefuse handleRefuse) {
        Principal me = SessionLocal.getPrincipal();

        handleRefuse.setHandleName(me.getUserName());
        handleRefuse.setHandleBy(Long.parseLong(me.getUserId()));
        waybillOptService.refuse(handleRefuse);
        return toJsonTrueMsg();
    }

    //下面是延迟件和拒收件的理由
    @RequestMapping(value = "/refuse_reason.html", method = RequestMethod.GET)
    @ResponseBody
    public String getReason() {
        List list = SystemCodeUtil.getSystemCodeList((String) SessionLocal.getPrincipal().getMerchantId(), Constant.REFUSE_REASON);
        return toJsonTrueData(list);
    }


    //路由查询
    @RequestMapping(value = "/route.html")
    @ResponseBody
    public String route(String logisticsNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        List<WaybillRouteDO> waybillRouteDOS = deliveryRouteService.queryRoute(merchantId, logisticsNo);

        return toJsonTrueData(waybillRouteDOS);

    }

    //修改密码
    @RequestMapping(value = "/modify_password.html", method = RequestMethod.POST)
    @ResponseBody
    public String modify_password(String oldPassword, String newPassword, String againPassword) {
        try {
            AssertUtil.isNotBlank(oldPassword, SysErrorCode.REQUEST_IS_NULL);
            Principal me = SessionLocal.getPrincipal();
            String userId = me.getUserId();
            //校验旧密码
            User user = userService.findByUserId(me.getMerchantId(), userId);
            if (!StringUtils.equals(DigestUtils.sha1Hex(oldPassword), user.getLoginInfo().getPassword())) {
                throw new DMSException(BizErrorCode.OLD_PASSWORD_ERROR);
            }

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(userId);
            loginInfo.setMerchantId(me.getMerchantId());
            loginInfo.setPassword(DigestUtils.sha1Hex(newPassword));
            userService.updateLoginInfo(loginInfo);
        } catch (Exception e) {
            logger.error("changePassword failed. ", e);
            return toJsonErrorMsg("Failed:" + e.getMessage());
        }
        return toJsonTrueMsg();

    }


}