package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.DepartmentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends BaseDao<Long, DepartmentDO> {

    List<DepartmentDO> queryByCompanyId(Long companyId);

    DepartmentDO queryByDepartmentId(@Param("companyId") Long companyId,@Param("departmentId") Long departmentId);
}
