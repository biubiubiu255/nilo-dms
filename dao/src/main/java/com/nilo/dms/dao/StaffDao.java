package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DepartmentDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffDao extends BaseDao<Long, StaffDO> {

    List<StaffDO> queryBy(@Param("companyId") Long companyId, @Param("departmentId") String departmentId, @Param("staffId") String staffId, @Param("name") String name,@Param("offset")int offset, @Param("limit")int limit);

    StaffDO queryByStaffId(@Param("companyId") Long companyId, @Param("staffId") String staffId);

    List<StaffDO> queryAllBy(@Param("companyId") Long companyId, @Param("departmentId") String departmentId );

    List<StaffDO> queryAllRider(@Param("companyId") Long companyId);
}
