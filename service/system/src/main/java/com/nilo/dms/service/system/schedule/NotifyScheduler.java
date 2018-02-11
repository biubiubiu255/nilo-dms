package com.nilo.dms.service.system.schedule;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.enums.NotifyStatusEnum;
import com.nilo.dms.common.utils.HttpUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.NotifyDao;
import com.nilo.dms.dao.dataobject.NotifyDO;
import com.nilo.dms.service.system.SpringContext;
import com.nilo.dms.service.system.model.NotifyResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NotifyScheduler implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private NotifyDao notifyDao = SpringContext.getBean("notifyDao", NotifyDao.class);

    private static final AtomicInteger processing = new AtomicInteger(
            0);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        if (processing.compareAndSet(0, 1)) {
            try {
                List<NotifyDO> list = notifyDao.queryRetryList(NotifyStatusEnum.RETRY.getCode());
                for (NotifyDO notify : list) {
                    Map<String, String> params = new HashMap<>();
                    params.put("op", notify.getBizType());
                    params.put("sign", notify.getSign());
                    params.put("app_key","dms");
                    params.put("data", notify.getParam());
                    String response = "";
                    try {
                        response = HttpUtil.post(notify.getUrl(), params);
                        NotifyResponse notifyResponse = JSON.parseObject(response, NotifyResponse.class);
                        if (!notifyResponse.getResult()) {
                            throw new IllegalStateException("NotifyMerchant failed." + notifyResponse);
                        }
                        NotifyDO update = new NotifyDO();
                        update.setNotifyId(notify.getNotifyId());
                        update.setStatus(NotifyStatusEnum.SUCCESS.getCode());
                        update.setNum(notify.getNum() + 1);
                        update.setResult(response);
                        notifyDao.update(update);

                    } catch (Exception e) {
                        NotifyDO update = new NotifyDO();
                        update.setNotifyId(notify.getNotifyId());
                        update.setStatus(NotifyStatusEnum.Failed.getCode());
                        update.setNum(notify.getNum() + 1);
                        String htmlEncode = HtmlUtils.htmlEscape(response);

                        update.setResult(StringUtil.isEmpty(response) ? e.getMessage() : htmlEncode);
                        notifyDao.update(update);
                    }

                }
            } catch (Exception ex) {
                logger.error("queryRetryList failed." + ex.getMessage());
            } finally {
                processing.set(0);
            }
        }else{
            System.out.println("=============================");
        }
    }


}
