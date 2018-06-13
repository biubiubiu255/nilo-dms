package com.nilo.dms.web.controller.order;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.CellData;
import com.nilo.dms.common.ExcelData;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.*;
import com.nilo.dms.dao.HandleRiderDao;
import com.nilo.dms.dao.HandleThirdDao;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.*;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/tests")
public class TestMeController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HandleRiderDao handleRiderDao;

    @Autowired
    private HandleThirdDao handleThirdDao;

    @ResponseBody
    @RequestMapping(value = "/s.html")
    public String list(String orderNo) {
        UserInfoDO userInfoDO = handleRiderDao.queryUserInfoBySmallNo(orderNo);

        return toJsonTrueData(userInfoDO);

    }

    @ResponseBody
    @RequestMapping(value = "/std.html")
    public String storde(String orderNo) {
        SendThirdHead sendThirdHead = handleThirdDao.queryHandleBySmallNo(1L, orderNo);
        return toJsonTrueData(sendThirdHead);

    }


}
