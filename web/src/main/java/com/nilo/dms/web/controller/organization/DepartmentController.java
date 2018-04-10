package com.nilo.dms.web.controller.organization;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.DepartmentStatusEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.model.ZTree;
import com.nilo.dms.service.org.DepartmentService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.service.org.model.Department;
import com.nilo.dms.service.org.model.Staff;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StaffService staffService;

    @RequestMapping(value = "/details.html", method = RequestMethod.GET)
    public String listGet(Model model) {
        return "department/details";
    }

    @ResponseBody
    @RequestMapping(value = "/departmentTree.html", method = RequestMethod.POST)
    public String updateCompany() {
        Principal me = SessionLocal.getPrincipal();
        List<ZTree> tree = departmentService.getTree(me.getCompanyId());
        return toJsonTrueData(tree);
    }

    @RequestMapping(value = "/addDepartmentPage.html", method = RequestMethod.GET)
    public String addDepartmentPage(Model model, String departmentId) {
        Principal me = SessionLocal.getPrincipal();
        String companyId = me.getCompanyId();
        List<Staff> list = staffService.findAllStaffBy(companyId, null);
        model.addAttribute("list", list);
        if (StringUtil.isNotEmpty(departmentId) && !StringUtil.equals(companyId, departmentId)) {
            Department parentDep = departmentService.queryById(companyId, departmentId);
            model.addAttribute("parentDepName", parentDep.getDepartmentName());
            model.addAttribute("parentDepId", parentDep.getDepartmentId());
        } else {
            model.addAttribute("parentDepId", me.getCompanyId());
            model.addAttribute("parentDepName", me.getCompanyName());
        }
        return "department/addDepartment";
    }

    @RequestMapping(value = "/editDepartmentPage.html", method = RequestMethod.GET)
    public String editDepartmentPage(Model model, String departmentId) {
        Principal me = SessionLocal.getPrincipal();
        String companyId = me.getCompanyId();
        List<Staff> list = staffService.findAllStaffBy(companyId, null);
        model.addAttribute("list", list);
        if (StringUtil.isNotEmpty(departmentId) && !StringUtil.equals(companyId, departmentId)) {
            Department department = departmentService.queryById(companyId, departmentId);
            model.addAttribute("department", department);
        } else {
            throw new IllegalArgumentException("departmentId illegal.");
        }
        return "department/editDepartment";
    }

    @RequestMapping(value = "/addDepartment.html", method = RequestMethod.POST)
    @ResponseBody
    public String addDepartment(Model model, String leaderId, String desc, String parentDepartmentId, String departmentName) {
        Principal me = SessionLocal.getPrincipal();
        String companyId = me.getCompanyId();
        try {
            Department addDept = new Department();
            addDept.setCompanyId(companyId);
            addDept.setLeaderId(leaderId);
            addDept.setDepartmentName(departmentName);
            addDept.setDesc(desc);
            Department parentDep = new Department();
            parentDep.setDepartmentId(parentDepartmentId);
            addDept.setParentDepartment(parentDep);
            departmentService.addDepartment(addDept);
        } catch (Exception e) {
            logger.error("Add department Failed.", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/editDepartment.html", method = RequestMethod.POST)
    @ResponseBody
    public String editDepartment(Model model, String departmentId, String departmentName, String desc, String leaderId) {
        Principal me = SessionLocal.getPrincipal();
        String companyId = me.getCompanyId();
        try {

            Department updateDep = new Department();
            updateDep.setCompanyId(companyId);
            updateDep.setDepartmentId(departmentId);
            updateDep.setDepartmentName(departmentName);
            updateDep.setDesc(desc);
            updateDep.setLeaderId(leaderId);
            departmentService.updateDepartment(updateDep);
        } catch (Exception e) {
            logger.error("Edit Department Failed.", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/delDepartment.html", method = RequestMethod.POST)
    @ResponseBody
    public String delDepartment(Model model, String departmentId) {
        Principal me = SessionLocal.getPrincipal();
        String companyId = me.getCompanyId();
        try {

            Department updateDep = new Department();
            updateDep.setCompanyId(companyId);
            updateDep.setDepartmentId(departmentId);
            updateDep.setStatus(DepartmentStatusEnum.DISABLED);
            departmentService.updateDepartment(updateDep);
        } catch (Exception e) {
            logger.error("Edit department Failed.", e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
