package com.nilo.dms.service.org;

import com.nilo.dms.service.model.ZTree;
import com.nilo.dms.service.org.model.Department;

import java.util.List;

/**
 * Created by ronny on 2017/8/30.
 */
public interface DepartmentService {

    void addDepartment(Department department);

    void updateDepartment(Department department);

    List<ZTree> getTree(String companyId);

    Department queryById(String companyId, String departmentId);

    List<Department> queryAll(String companyId);
}
