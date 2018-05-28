package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

public class DeliverAgendaDO extends BaseDo<Integer>{

    private Integer deliveredMonthNum;
    private Integer deliveredDayNum;
    private Integer taskDayNum;
    private Integer delayDayNum;

    public Integer getDeliveredMonthNum() {
        return deliveredMonthNum;
    }

    public void setDeliveredMonthNum(Integer deliveredMonthNum) {
        this.deliveredMonthNum = deliveredMonthNum;
    }

    public Integer getDeliveredDayNum() {
        return deliveredDayNum;
    }

    public void setDeliveredDayNum(Integer deliveredDayNum) {
        this.deliveredDayNum = deliveredDayNum;
    }

    public Integer getTaskDayNum() {
        return taskDayNum;
    }

    public void setTaskDayNum(Integer taskDayNum) {
        this.taskDayNum = taskDayNum;
    }

    public Integer getDelayDayNum() {
        return delayDayNum;
    }

    public void setDelayDayNum(Integer delayDayNum) {
        this.delayDayNum = delayDayNum;
    }

    @Override
    public String toString() {
        return "DeliverAgendaDO{" +
                "deliveredMonthNum=" + deliveredMonthNum +
                ", deliveredDayNum=" + deliveredDayNum +
                ", taskDayNum=" + taskDayNum +
                ", delayDayNum=" + delayDayNum +
                '}';
    }
}
