package com.nilo.dms.service.org.impl;

import com.nilo.dms.common.enums.DepartmentStatusEnum;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.dao.CompanyDao;
import com.nilo.dms.dao.DepartmentDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DepartmentDO;
import com.nilo.dms.service.model.ZTree;
import com.nilo.dms.service.org.DepartmentService;
import com.nilo.dms.service.org.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/29.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CompanyDao companyDao;

    @Override
    public void addDepartment(Department department) {

        //设置部门ID
        department.setStatus(DepartmentStatusEnum.NORMAL);
        department.setDepartmentId("" + IdWorker.getInstance().nextId());
        DepartmentDO departmentDO = convert(department);
        departmentDao.insert(departmentDO);
    }

    @Override
    public void updateDepartment(Department department) {
        DepartmentDO departmentDO = convert(department);
        departmentDao.update(departmentDO);
    }

    @Override
    public List<ZTree> getTree(String companyId) {
        List<ZTree> list = new ArrayList<>();

        //查询公司信息
        CompanyDO companyDO = companyDao.queryById(Long.parseLong(companyId));
        ZTree head = new ZTree();
        head.setId(companyId);
        head.setpId("0");
        head.setName(companyDO.getCompanyName());
        head.setValue(companyId);
        head.setOpen(true);
        head.setIsParent(true);
        list.add(head);


        List<DepartmentDO> doList = departmentDao.queryByCompanyId(Long.parseLong(companyId));
        for (DepartmentDO d : doList) {
            ZTree t = new ZTree();
            t.setName(d.getDepartmentName());
            t.setValue("" + d.getDepartmentId());
            t.setId("" + d.getDepartmentId());
            t.setpId("" + d.getParentDepartmentId());
            t.setOpen(true);
            t.setIsParent(true);
            list.add(t);
        }

        return list;
    }

    @Override
    public Department queryById(String companyId, String departmentId) {

        DepartmentDO departmentDO = departmentDao.queryByDepartmentId(Long.parseLong(companyId), Long.parseLong(departmentId));
        Department department = convert(departmentDO);

        return department;
    }

    @Override
    public List<Department> queryAll(String companyId) {
        List<DepartmentDO> doList = departmentDao.queryByCompanyId(Long.parseLong(companyId));
        List<Department> list = new ArrayList<>();
        for (DepartmentDO d : doList) {
            list.add(convert(d));
        }
        return list;
    }

    private Department convert(DepartmentDO departmentDO) {

        if (departmentDO == null) {
            return null;
        }
        Department department = new Department();
        department.setCompanyId("" + departmentDO.getCompanyId());
        department.setDesc(departmentDO.getDescription());
        department.setDepartmentName(departmentDO.getDepartmentName());
        department.setDepartmentId("" + departmentDO.getDepartmentId());
        department.setType(departmentDO.getType());
        department.setStatus(DepartmentStatusEnum.getEnum(departmentDO.getStatus()));

        if (departmentDO.getParentDepartmentId() != departmentDO.getCompanyId()) {
            DepartmentDO parentDep = departmentDao.queryByDepartmentId(departmentDO.getCompanyId(), departmentDO.getParentDepartmentId());
            department.setParentDepartment(convert(parentDep));
        }
        department.setLeaderId(departmentDO.getLeader());
        return department;

    }

    private DepartmentDO convert(Department department) {

        if (department == null) {
            return null;
        }
        DepartmentDO departmentDO = new DepartmentDO();
        departmentDO.setCompanyId(Long.parseLong(department.getCompanyId()));
        departmentDO.setDescription(department.getDesc());
        departmentDO.setDepartmentName(department.getDepartmentName());
        departmentDO.setDepartmentId(Long.parseLong(department.getDepartmentId()));
        departmentDO.setType(department.getType());

        departmentDO.setLeader(department.getLeaderId());

        if (department.getStatus() != null) {
            departmentDO.setStatus(department.getStatus().getCode());
        }
        if (department.getParentDepartment() != null) {
            departmentDO.setParentDepartmentId(Long.parseLong(department.getParentDepartment().getDepartmentId()));
        }
        return departmentDO;

    }
}
