package com.nilo.dms.service.system;


import com.nilo.dms.dto.system.Dictionary;

import java.util.List;

/**
 * Created by admin on 2017/9/22.
 */
public interface DictionaryService {

    List<Dictionary> queryBy(String merchantId, String type);

    Dictionary queryByCode(String merchantId, String type, String code);

    void add(Dictionary dictionary);

    void delete(Dictionary dictionary);

    void update(Dictionary dictionary);

    List<Dictionary> queryAllBy(String merchantId);

    List<Dictionary> queryAll();

}
