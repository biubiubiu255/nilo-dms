package com.nilo.dms.web;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.nilo.dms.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronny on 2017/8/22.
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        logger.error("Request url:{}", request.getRequestURL(), ex);

        String method = request.getMethod();
        ModelAndView mv = new ModelAndView();

        if (StringUtil.equalsIgnoreCase(method, RequestMethod.POST.name())) {
            // 获取request请求方式
        /*  使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常   */
            FastJsonJsonView view = new FastJsonJsonView();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", false);
            result.put("msg", ex.getMessage());
            view.setAttributesMap(result);
            mv.setView(view);
        }

        if (StringUtil.equalsIgnoreCase(method, RequestMethod.GET.name())) {
            mv.addObject("msg", ex.getMessage());
            mv.setViewName("error");
        }
        return mv;
    }
}
