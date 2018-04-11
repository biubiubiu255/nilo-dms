package com.nilo.dms.web.controller;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.TaskStatusEnum;
import com.nilo.dms.dto.common.Menu;
import com.nilo.dms.service.MenuService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/dashboard.html")
    public String dashboard(Model model) {

        List<Menu> list = (List<Menu>) getSessionAttr("userMenu");
        Principal me = SessionLocal.getPrincipal();
        if (list == null) {
            //根据权限过滤菜单
            list = menuService.getWebMenu(me.getUserId());
            setSessionAttr("userMenu", list);
        }

        TaskParameter parameter = new TaskParameter();
        parameter.setHandledBy(me.getUserId());
        parameter.setMerchantId(me.getMerchantId());
        List<Integer> status = new ArrayList<>();
        status.add(TaskStatusEnum.CREATE.getCode());
        status.add(TaskStatusEnum.PROCESS.getCode());
        parameter.setStatus(status);
        Pagination pagination = new Pagination(0, 100);
        List<Task> taskList = taskService.queryTask(parameter, pagination);

        model.addAttribute("menu", JSON.toJSONString(list));
        model.addAttribute("taskList", taskList);
        model.addAttribute("taskCount", taskList.size());
        return "dashboard";
    }

}
