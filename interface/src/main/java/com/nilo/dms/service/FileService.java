package com.nilo.dms.service;


import com.nilo.dms.service.model.Menu;

import java.util.List;

/**
 * Created by ronny on 2017/9/14.
 */
public interface FileService {

    void uploadSignImage(String merchantId,String uploadBy ,String orderNo, byte[] b) throws Exception;

    void uploadProblemImage(String merchantId,String uploadBy ,String orderNo, byte[] b) throws Exception;

}
