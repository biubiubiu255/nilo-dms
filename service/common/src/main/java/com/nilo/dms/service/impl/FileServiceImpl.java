package com.nilo.dms.service.impl;

import com.nilo.dms.common.enums.ImageStatusEnum;
import com.nilo.dms.common.enums.ImageTypeEnum;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.ImageDao;
import com.nilo.dms.dao.dataobject.ImageDO;
import com.nilo.dms.service.FileService;
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
public class FileServiceImpl implements FileService {
    @Value("#{configProperties['s3SecretKey']}")
    private String s3SecretKey;
    @Value("#{configProperties['s3AppKeyId']}")
    private String s3AppKeyId;
    @Value("#{configProperties['s3Bucket']}")
    private String s3Bucket;
    @Value("#{configProperties['s3Url']}")
    private String s3Url;
    @Autowired
    private ImageDao imageDao;

    @Override
    public void uploadSignImage(String merchantId, String uploadBy, String orderNo, byte[] b) throws Exception {
        String imageName = uploadS3(b, "dms/sign");
        ImageDO imageDO = new ImageDO();
        imageDO.setMerchantId(Long.parseLong(merchantId));
        imageDO.setOrderNo(orderNo);
        imageDO.setCreatedBy(uploadBy);
        imageDO.setStatus(ImageStatusEnum.NORMAL.getCode());
        imageDO.setImageName("dms/sign/" + imageName);
        imageDO.setImageType(ImageTypeEnum.RECEIVE.getCode());
        imageDao.insert(imageDO);

    }

    @Override
    public void uploadProblemImage(String merchantId, String uploadBy, String orderNo, byte[] b) throws Exception {

        String imageName = uploadS3(b, "dms/problem");
        ImageDO imageDO = new ImageDO();
        imageDO.setMerchantId(Long.parseLong(merchantId));
        imageDO.setOrderNo(orderNo);
        imageDO.setCreatedBy(uploadBy);
        imageDO.setStatus(ImageStatusEnum.NORMAL.getCode());
        imageDO.setImageName("dms/problem/" + imageName);
        imageDO.setImageType(ImageTypeEnum.PROBLEM.getCode());
        imageDao.insert(imageDO);
    }

    private String uploadS3(byte[] b, String s3Path) throws Exception {

        String fileName = IdWorker.getInstance().nextId() + ".jpg";

        String url = s3Url + "/" + s3Path + "/" + fileName;
        SimpleDateFormat df = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
        Date date = new Date();
        String dateString = df.format(date);

        String contentType = "multipart/form-data";
        String sign = "PUT\n\n" + contentType + "\n\n" + "x-amz-acl:public-read\n" + "x-amz-date:" + dateString + "\n"
                + "/" + s3Bucket + "/" + s3Path + "/" + fileName;
        Mac hmac = Mac.getInstance("HmacSHA1");
        hmac.init(new SecretKeySpec(
                s3SecretKey.getBytes("UTF-8"), "HmacSHA1"));
        String signature = (new BASE64Encoder()).encode(
                hmac.doFinal(sign.getBytes("UTF-8")))
                .replaceAll("\n", "");

        String authAWS = "AWS " + s3AppKeyId + ":" + signature;

        HttpClient client = new HttpClient();
        //登录
        PutMethod putMethod = new PutMethod(url);

        putMethod.addRequestHeader("Content-Type", contentType);
        putMethod.addRequestHeader("x-amz-acl", "public-read");
        putMethod.addRequestHeader("x-amz-date", dateString);
        putMethod.addRequestHeader("Authorization", authAWS);
        putMethod.setRequestEntity(new ByteArrayRequestEntity(b));
        client.executeMethod(putMethod);
        String response = putMethod.getResponseBodyAsString();
        //释放连接
        putMethod.releaseConnection();
        //判断是否返回成功
        if (!StringUtil.isEmpty(response)) {
            throw new RuntimeException("upload to s3 failed.");
        }
        return fileName;
    }
}
