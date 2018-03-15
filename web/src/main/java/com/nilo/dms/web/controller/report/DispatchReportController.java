package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.common.enums.TaskTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.order.TaskService;
import com.nilo.dms.service.order.model.Task;
import com.nilo.dms.service.order.model.TaskParameter;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/11/20.
 */
@Controller
@RequestMapping("/report/dispatch")
public class DispatchReportController extends BaseController {

    @Autowired
    private TaskService taskService;


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("list", getRiderList(merchantId));
        return "report/dispatch";
    }


    @RequestMapping(value = "/list.html")
    public String getOrderList(Model model, String rider, String fromTime, String toTime) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        if (StringUtil.isEmpty(fromTime) || StringUtil.isEmpty(fromTime)) {
            if (StringUtil.isEmpty(fromTime)) {
                fromTime = toTime;
            } else {
                toTime = fromTime;
            }
        }
        Long fromTimeL = 0l;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeL = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        Long toTimeL = 0l;
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeL = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }

        Pagination pagination = getPage();
        TaskParameter parameter = new TaskParameter();
        parameter.setMerchantId(merchantId);
        parameter.setHandledBy(rider);
        parameter.setTaskType(Arrays.asList(TaskTypeEnum.DELIVERY.getCode()));
        List<Integer> status = new ArrayList<>();
        status.add(TaskStatusEnum.COMPLETE.getCode());
        status.add(TaskStatusEnum.PROCESS.getCode());
        parameter.setStatus(status);

        parameter.setFromTime(fromTimeL);
        parameter.setToTime(toTimeL);
        List<Task> list = taskService.queryTask(parameter, pagination);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        System.out.println(" = " + list.size());
        // 动态指定报表模板url
        model.addAttribute("url", "/WEB-INF/jasper/report/dispatch.jasper");
        model.addAttribute("format", "pdf"); // 报表格式
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView"; // 对应jasper-defs.xml中的bean id
    }
}
