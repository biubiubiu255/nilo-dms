package com.nilo.dms.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DictionaryDao;
import com.nilo.dms.dao.dataobject.DictionaryDO;
import com.nilo.dms.dto.system.Dictionary;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.DictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/22.
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public List<Dictionary> queryBy(String merchantId, String type) {

        List<DictionaryDO> queryList = dictionaryDao.queryBy(Long.parseLong(merchantId), type);

        List<Dictionary> list = new ArrayList<>();
        if (queryList == null) return list;
        for (DictionaryDO d : queryList) {
            list.add(convert(d));
        }

        return list;
    }

    @Override
    public Dictionary queryByCode(String merchantId, String type, String code) {
        DictionaryDO dictionaryDO = dictionaryDao.queryByCode(Long.parseLong(merchantId), type, code);

        if (dictionaryDO == null) return null;
        return convert(dictionaryDO);
    }

    @Override
    public void add(Dictionary dictionary) {

        Dictionary dic = queryByCode(dictionary.getMerchantId(), dictionary.getType(), dictionary.getCode());
        if (dic != null) throw new DMSException(BizErrorCode.CODE_EXIST, dictionary.getCode());

        DictionaryDO dictionaryDO = convert(dictionary);
        dictionaryDao.insert(dictionaryDO);

        //刷新缓存
        RedisUtil.sadd("System_code" + dictionary.getMerchantId() + dictionary.getType(), JSON.toJSONString(dictionary));
    }

    @Override
    public void delete(Dictionary dictionary) {
        Dictionary dic = queryByCode(dictionary.getMerchantId(), dictionary.getType(), dictionary.getCode());
        if (dic == null) throw new DMSException(BizErrorCode.CODE_NOT_EXIST, dictionary.getCode());
        dictionaryDao.deleteBy(Long.parseLong(dictionary.getMerchantId()), dictionary.getType(), dictionary.getCode());
        //刷新缓存
        RedisUtil.srem("System_code" + dic.getMerchantId() + dic.getType(), JSON.toJSONString(dic));
    }

    @Override
    public void update(Dictionary dictionary) {

        Dictionary dic = queryByCode(dictionary.getMerchantId(), dictionary.getType(), dictionary.getCode());
        if (dic == null) throw new DMSException(BizErrorCode.CODE_NOT_EXIST, dictionary.getCode());

        DictionaryDO dictionaryDO = convert(dictionary);
        dictionaryDao.update(dictionaryDO);
        //刷新缓存
        RedisUtil.srem("System_code" + dic.getMerchantId() + dic.getType(), JSON.toJSONString(dic));
        RedisUtil.sadd("System_code" + dictionary.getMerchantId() + dictionary.getType(), JSON.toJSONString(dictionary));

    }

    @Override
    public List<Dictionary> queryAllBy(String merchantId) {
        List<DictionaryDO> queryList = dictionaryDao.queryAllBy(Long.parseLong(merchantId));
        List<Dictionary> list = new ArrayList<>();
        if (queryList != null) {
            for (DictionaryDO d : queryList) {
                list.add(convert(d));
            }
        }
        return list;
    }

    @Override
    public List<Dictionary> queryAll() {
        List<DictionaryDO> queryList = dictionaryDao.queryAll();
        List<Dictionary> list = new ArrayList<>();
        if (queryList != null) {
            for (DictionaryDO d : queryList) {
                list.add(convert(d));
            }
        }
        return list;
    }

    private Dictionary convert(DictionaryDO dictionaryDo) {
        if (dictionaryDo == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId("" + dictionaryDo.getId());
        dictionary.setMerchantId("" + dictionaryDo.getMerchantId());
        dictionary.setCode(dictionaryDo.getCode());
        dictionary.setType(dictionaryDo.getType());
        dictionary.setValue(dictionaryDo.getValue());
        dictionary.setRemark(dictionaryDo.getRemark());
        dictionary.setShowOrder(dictionaryDo.getShowOrder());
        return dictionary;
    }

    private DictionaryDO convert(Dictionary d) {

        if (d == null) {
            return null;
        }

        DictionaryDO dictionaryDO = new DictionaryDO();
        dictionaryDO.setValue(d.getValue());
        dictionaryDO.setType(d.getType());
        dictionaryDO.setCode(d.getCode());
        dictionaryDO.setMerchantId(Long.parseLong(d.getMerchantId()));
        dictionaryDO.setRemark(d.getRemark());
        dictionaryDO.setShowOrder(d.getShowOrder());
        return dictionaryDO;

    }
}
