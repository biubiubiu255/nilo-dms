package com.nilo.dms.service.system;


import com.nilo.dms.service.system.model.SerialNumberRule;

import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
public interface SerialNumberService {

    List<SerialNumberRule> findAll();

    List<SerialNumberRule> findAllBy(String merchantId);

    List<SerialNumberRule> findBy(String merchantId, List<String> serialType);

    void updateSerialNumberRule(SerialNumberRule rule);
}
