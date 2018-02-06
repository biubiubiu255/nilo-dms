package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/mobile/send")
public class SendScanController extends BaseController {
    @Autowired
    private ThirdExpressDao thirdExpressDao;
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    @Autowired
    private ThirdDriverDao thirdDriverDao;
    @Autowired
    private StaffDao staffDao;

    @RequestMapping(value = "/scan.html")
    public String toPage(Model model, HttpServletRequest request) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("riderList", getRiderList(merchantId));

        //第三方快递公司及自提点
        List<ThirdExpressDO> expressDOList = thirdExpressDao.findByMerchantId(Long.parseLong(merchantId));
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));

        List<NextStation> list = new ArrayList<>();
        for(ThirdExpressDO e : expressDOList){
            NextStation s = new NextStation();
            s.setCode(e.getExpressCode());
            s.setName(e.getExpressName());
            s.setType("T");
            list.add(s);
        }
        for(DistributionNetworkDO n:networkDOList){
            NextStation s = new NextStation();
            s.setCode(""+n.getId());
            s.setName(n.getName());
            s.setType("N");
            list.add(s);
        }
        model.addAttribute("nextStation",list);
        model.addAttribute("thirdCarrier",expressDOList);
        return "mobile/network/send_scan/sendScan";
    }
    @RequestMapping(value = "/getDriver.html")
    @ResponseBody
    public String getDriver(String code) {
        List<ThirdDriverDO> thirdDriver = thirdDriverDao.findByExpressCode(code);
        List<Driver> list = new ArrayList<>();
        for(ThirdDriverDO d : thirdDriver){
            Driver driver = new Driver();
            driver.setCode(d.getDriverId());
            driver.setName(d.getDriverName());
            list.add(driver);
        }
        if(isInteger(code)) {
            List<StaffDO> staffList = staffDao.queryNetworkStaff(Long.parseLong(code));
            for(StaffDO s : staffList){
                Driver driver = new Driver();
                driver.setCode(""+s.getUserId());
                driver.setName(s.getStaffId());
                list.add(driver);
            }
        }
        return toJsonTrueData(list);
    }
    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String[] arr) {
        for(int i = 0; i < arr.length; i ++) {
            System.out.println(arr[i]);
        }
        return "true";
    }
    private  boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static class Driver{
        private String code;
        private String name;

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    public static class NextStation{
        private String code;
        private String name;
        private String type;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
