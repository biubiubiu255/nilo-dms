package com.nilo.dms.web.controller.system;


import com.nilo.dms.common.enums.ImageStatusEnum;
import com.nilo.dms.common.enums.ImageTypeEnum;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.ImageCut;
import com.nilo.dms.dao.ImageDao;
import com.nilo.dms.dao.dataobject.ImageDO;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/image")
public class ImageController extends BaseController {

    private static final String path = "F:\\ronny_1\\dms_master\\web\\target\\platform-dms\\upload";

    private static final String[] suffixNameAllow = new String[]{".jpg", ".png"};

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageDao imageDao;

    @RequestMapping(value = "/show.html")
    public String getList(Model model, String orderNo, String imageType) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<ImageDO> list = imageDao.findBy(Long.parseLong(merchantId), orderNo, imageType);
        model.addAttribute("list", list);
        return "image/show";
    }

    @ResponseBody
    @RequestMapping(value = "/upload/{imageType}/{orderNo}.html")
    public String upload(MultipartFile file, @PathVariable String orderNo, @PathVariable String imageType) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String fileName = "";
        try {
            if (file != null) {
                fileName = FileUtil.uploadFile(file, path);

                ImageDO imageDO = new ImageDO();
                
                imageDO.setMerchantId(Long.parseLong(me.getMerchantId()));
                imageDO.setOrderNo(orderNo);
                imageDO.setCreatedBy(me.getUserId());
                imageDO.setUpdatedBy(me.getUserId());
                imageDO.setStatus(ImageStatusEnum.NORMAL.getCode());
                imageDO.setImageName(fileName);
                imageDO.setImageType(ImageTypeEnum.getEnum(imageType).getCode());
                imageDao.insert(imageDO);

            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(fileName);
    }

    @RequestMapping(value = "/uploadPhotoPage.html")
    public String getList(Model model, String userId) {
        model.addAttribute("userId", userId);
        return "image/uploadPhoto";
    }

    @ResponseBody
    @RequestMapping("/uploadPhoto.html")
    public String uploadPhoto(@RequestParam("upimage") MultipartFile imageFile, HttpServletRequest request) {

        //校验图片格式
        String suffixName = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
        if (!Arrays.asList(suffixNameAllow).contains(suffixName)) {
            return toJsonErrorMsg("Image not Allowed");
        }

        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String h = request.getParameter("h");
        String w = request.getParameter("w");
        // 页面实际图片宽高
        String pheight = request.getParameter("ph");
        String pweight = request.getParameter("pw");

        // 切图参数
        int imageX = Integer.parseInt(x);
        int imageY = Integer.parseInt(y);
        int imageH = Integer.parseInt(h);
        int imageW = Integer.parseInt(w);
        int srcH = Integer.parseInt(pheight);
        int srcW = Integer.parseInt(pweight);

        String fileName = path + File.separator + IdWorker.getInstance().nextId() + ".jpg";
        try {
            if (imageFile != null) {
                ImageCut.imgCut(imageFile.getInputStream(), imageX, imageY, imageW, imageH, srcW, srcH, fileName);
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(fileName);
    }
}
