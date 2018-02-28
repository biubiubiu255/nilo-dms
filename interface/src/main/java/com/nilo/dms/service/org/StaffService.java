package com.nilo.dms.service.org;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.org.model.Staff;

import java.util.List;

/**
 * Created by ronny on 2017/8/30.
 */
public interface StaffService {

    void addStaff(Staff staff);

    void updateStaff(Staff staff);

    Staff findByStaffId(String companyId, String staffId);

    List<Staff> findBy(String companyId, String departmentId, String staffId, String name, Pagination page);

    List<Staff> findAllStaffBy(String companyId, String departmentId);
}
