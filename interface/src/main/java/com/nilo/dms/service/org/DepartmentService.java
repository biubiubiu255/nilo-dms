package com.nilo.dms.service.org;

import com.nilo.dms.dto.common.ZTree;
import com.nilo.dms.dto.org.Department;

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
