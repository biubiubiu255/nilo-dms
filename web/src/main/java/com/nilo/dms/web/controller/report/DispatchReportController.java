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
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/20.
 */
@Controller
@RequestMapping("/report")
public class DispatchReportController extends BaseController {

    @Autowired
    private TaskService taskService;


    @RequestMapping(value = "/dispatchReport.html", method = RequestMethod.GET)
    public String list(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("list", getRiderList(merchantId));
        return "report/dispatch";
    }

    @ResponseBody
    @RequestMapping(value = "/dispatchList.html")
    public String getOrderList(String rider, String fromTime, String toTime) {

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
        parameter.setTaskType(TaskTypeEnum.DISPATCH.getCode());
        List<Integer> status = new ArrayList<>();
        status.add(TaskStatusEnum.COMPLETE.getCode());
        status.add(TaskStatusEnum.PROCESS.getCode());
        parameter.setStatus(status);

        parameter.setFromTime(fromTimeL);
        parameter.setToTime(toTimeL);
        List<Task> list = taskService.queryTask(parameter, pagination);
        return toPaginationLayUIData(pagination, list);
    }
}
