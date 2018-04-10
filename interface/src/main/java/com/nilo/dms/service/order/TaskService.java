package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.Task;
import com.nilo.dms.dto.order.TaskParameter;

import java.util.List;

/**
 * Created by admin on 2017/10/23.
 */
public interface TaskService {

    String addTask(Task task);

    void transferTask(String taskId, String from, String to, String remark);

    void updateTask(Task task);

    List<Task> queryTask(TaskParameter parameter, Pagination pagination);

    Task queryTaskByTypeAndOrderNo(String merchantId, String taskType, String orderNo);
}
