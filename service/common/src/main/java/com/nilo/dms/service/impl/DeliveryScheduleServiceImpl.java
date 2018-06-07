package com.nilo.dms.service.impl;

import com.nilo.dms.common.enums.ImageStatusEnum;
import com.nilo.dms.common.enums.ImageTypeEnum;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.ImageDao;
import com.nilo.dms.dao.dataobject.ImageDO;
import com.nilo.dms.service.FileService;
import com.nilo.dms.service.system.DeliveryScheduleService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 2018/2/23.
 */
@Service
public class DeliveryScheduleServiceImpl implements DeliveryScheduleService {

    @Override
    public void syncDeliveryDeatil() {
        System.out.println("本次测试 = 正在测试哈哈哈哈哈哈哈");
    }
}
