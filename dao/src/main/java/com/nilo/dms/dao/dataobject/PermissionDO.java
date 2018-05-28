package com.nilo.dms.dao.dataobject;

import com.nilo.dms.common.BaseDo;

/**
 * 权限结构
 */
public class PermissionDO extends BaseDo<Integer> {

    private int pId;

    private String name;

    private String value;

    private int isMenu;

    public int getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(int isMenu) {
        this.isMenu = isMenu;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
