package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.handle.SendThirdDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleThirdDao extends BaseDao<Long, SendThirdHead> {

    List<SendThirdHead> queryBig(@Param("ob") SendThirdHead sendThirdHead, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer queryBigCount(@Param("ob") SendThirdHead sendThirdHead, @Param("offset") Integer offset, @Param("limit") Integer limit);

    void insertBig(SendThirdHead sendThirdHead);

    void editBigBy(SendThirdHead sendThirdHead);

    //下面是子包

    List<SendThirdDetail> querySmall(SendThirdDetail sendThirdDetail);

    void insertSmalls(List<SendThirdDetail> list);

    void deleteSmallByHandleNo(SendThirdDetail sendThirdDetail);

}
