package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.DictionaryDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ronny on 2017/9/18.
 */
@Repository
public interface DictionaryDao extends BaseDao<Long, DictionaryDO> {

    List<DictionaryDO> queryAll();

    List<DictionaryDO> queryAllBy(@Param("merchantId")Long merchantId);

    void save(@Param("list")List<DictionaryDO> list);

    List<DictionaryDO> queryBy(@Param("merchantId")Long merchantId, @Param("type")String type);

    DictionaryDO queryByCode(@Param("merchantId")Long merchantId, @Param("type")String type,@Param("code")String code);

    void deleteBy(@Param("merchantId") Long merchantId, @Param("type")String type,@Param("code")String code);

}
