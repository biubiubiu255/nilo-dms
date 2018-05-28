package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DictionaryDO;
import com.nilo.dms.dao.dataobject.DictionaryTypeDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ronny on 2017/9/18.
 */
@Repository
public interface DictionaryTypeDao extends BaseDao<Long, DictionaryTypeDO> {

    List<DictionaryTypeDO> queryAll();

    List<DictionaryTypeDO> queryBy(@Param(value="typeList") List<String> typeList,@Param(value = "desc") String desc);
}
