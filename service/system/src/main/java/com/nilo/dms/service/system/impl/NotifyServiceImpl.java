package com.nilo.dms.service.system.impl;

import com.nilo.dms.common.enums.NotifyStatusEnum;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.system.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/11/7.
 */
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private NotifyDao notifyDao;

    @Override
    public void retryNotify(String merchantId, String notifyId) {

        NotifyDO notifyDO = new NotifyDO();
        notifyDO.setNotifyId(notifyId);
        notifyDO.setStatus(NotifyStatusEnum.RETRY.getCode());
        notifyDao.update(notifyDO);
    }
}
