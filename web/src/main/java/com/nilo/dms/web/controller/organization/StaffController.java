package com.nilo.dms.web.controller.organization;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.StaffStatusEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.OutsourceDao;
import com.nilo.dms.dao.dataobject.OutsourceDO;
import com.nilo.dms.dto.org.Department;
import com.nilo.dms.dto.org.Staff;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.org.DepartmentService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private OutsourceDao outsourceDao;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String listGet(Model model) {
        Principal me = SessionLocal.getPrincipal();
        List<Department> list = departmentService.queryAll(me.getCompanyId());
        model.addAttribute("departmentList", list);

        return "staff/list";
    }

    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    @ResponseBody
    public String list(Model model, String departmentId, String staffId, String name) {
        Principal me = SessionLocal.getPrincipal();
        Pagination page = getPage();
        List<Staff> list = staffService.findBy(me.getCompanyId(), departmentId, staffId, name, page);
        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/editStaffPage.html", method = RequestMethod.GET)
    public String editStaffPage(Model model, String staffId) {
        Principal me = SessionLocal.getPrincipal();
        String companyId = me.getCompanyId();
        Staff staff = staffService.findByStaffId(companyId, staffId);
        List<Department> list = departmentService.queryAll(me.getCompanyId());
        List<OutsourceDO> outsourceList = outsourceDao.findAll(me.getMerchantId());

        model.addAttribute("departmentList", list);
        model.addAttribute("staff", staff);
        model.addAttribute("outsourceList", outsourceList);

        return "staff/edit";
    }

    @RequestMapping(value = "/addStaffPage.html", method = RequestMethod.GET)
    public String addStaffPage(Model model) {
        Principal me = SessionLocal.getPrincipal();
        List<Department> list = departmentService.queryAll(me.getCompanyId());
        model.addAttribute("departmentList", list);
        List<OutsourceDO> outsourceList = outsourceDao.findAll(me.getMerchantId());
        model.addAttribute("outsourceList", outsourceList);

        return "staff/add";
    }

    @RequestMapping(value = "/editStaff.html", method = RequestMethod.POST)
    @ResponseBody
    public String editStaff(Model model, Staff staff, String employTimeDate, Integer staffStatus, Integer isRiderCode) {
        try {
            Principal me = SessionLocal.getPrincipal();
            if (StringUtil.isEmpty(employTimeDate)) {
                throw new RuntimeException("Employ Time is Empty.");
            }
            String companyId = me.getCompanyId();
            staff.setCompanyId(companyId);
            Long employTime = DateUtil.parse(employTimeDate, "yyyy-MM-dd");
            staff.setEmployTime(employTime);
            StaffStatusEnum statusEnum = StaffStatusEnum.getEnum(staffStatus);
            staff.setStatus(statusEnum);
            staff.setIsRider(isRiderCode != null);
            staff.setMerchantId(me.getMerchantId());
            staffService.updateStaff(staff);
        } catch (Exception e) {
            logger.error("editStaff Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }


    @RequestMapping(value = "/addStaff.html", method = RequestMethod.POST)
    @ResponseBody
    public String addStaff(Model model, Staff staff, String employTimeDate, Integer staffStatus, Integer isRiderCode) {
        try {
            if (StringUtil.isEmpty(employTimeDate)) {
                throw new RuntimeException("Employ Time is Empty.");
            }
            Principal me = SessionLocal.getPrincipal();
            staff.setCompanyId(me.getCompanyId());
            staff.setMerchantId(me.getMerchantId());
            Long employTime = DateUtil.parse(employTimeDate, "yyyy-MM-dd");
            staff.setEmployTime(employTime);
            StaffStatusEnum statusEnum = StaffStatusEnum.getEnum(staffStatus);
            staff.setStatus(statusEnum);
            staff.setIsRider(isRiderCode != null);
            staffService.addStaff(staff);
        } catch (Exception e) {
            logger.error("editStaff Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/regular.html", method = RequestMethod.POST)
    @ResponseBody
    public String regular(String staffId) {
        try {
            Principal me = SessionLocal.getPrincipal();

            Staff staff = new Staff();
            staff.setMerchantId(me.getMerchantId());
            staff.setStatus(StaffStatusEnum.REGULAR);
            staff.setStaffId(staffId);
            staff.setRegularTime(DateUtil.getSysTimeStamp());
            staffService.updateStaff(staff);
        } catch (Exception e) {
            logger.error("editStaff Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/resigned.html", method = RequestMethod.POST)
    @ResponseBody
    public String resigned(String staffId) {
        try {
            Principal me = SessionLocal.getPrincipal();

            Staff staff = new Staff();
            staff.setMerchantId(me.getMerchantId());
            staff.setStatus(StaffStatusEnum.RESIGNED);
            staff.setStaffId(staffId);
            staff.setResignedTime(DateUtil.getSysTimeStamp());
            staffService.updateStaff(staff);
        } catch (Exception e) {
            logger.error("editStaff Failed. ", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
