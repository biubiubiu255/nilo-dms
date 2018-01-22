package com.nilo.dms.web.controller;

import com.nilo.dms.web.MyCaptchaEngineImpl;
import io.leopard.web.captcha.CaptchaView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 验证码服务
 * </p>
 *
 * @author hubin
 * @Date 2016-01-06
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController extends BaseController {

    /**
     * 生成图片
     */
    @ResponseBody
    @RequestMapping("/image.html")
    public CaptchaView image() {
        return new CaptchaView(160, 60, MyCaptchaEngineImpl.class);
    }

}
