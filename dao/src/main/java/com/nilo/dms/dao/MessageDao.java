package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dao.dataobject.MessageDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao extends BaseDao<Long, MessageDO> {

    List<MessageDO> queryBy(@Param("status")List<Integer> status,@Param("receiver")String receiver);

    List<MessageDO> queryAllCreated();

}
