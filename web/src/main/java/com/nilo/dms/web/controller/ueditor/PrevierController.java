package com.nilo.dms.web.controller.ueditor;

import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/ueditor")
public class PrevierController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 预览
     */
    @RequestMapping(value = "/dialogs/preview/preview.html", method = RequestMethod.GET)
    public String preview(UsernamePasswordToken token, String randomCode, HttpServletRequest request) {


        return "ueditor/preview";
    }

}
