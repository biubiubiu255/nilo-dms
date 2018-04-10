package com.nilo.dms.service.impl;

import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.MenuDao;
import com.nilo.dms.dao.dataobject.MenuDO;
import com.nilo.dms.dto.common.Menu;
import com.nilo.dms.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronny on 2017/9/14.
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> getWebMenu(String userId) {

        if (StringUtil.isEmpty(userId)) {
            return null;
        }
        List<MenuDO> menuDOs = menuDao.queryBy(Long.parseLong(userId));
        List<Menu> menuList = convert(menuDOs);
        return menuList;
    }

    private List<Menu> convert(List<MenuDO> list) {

        List<Menu> menuList = new ArrayList<>();

        if (list == null) return menuList;

        Menu header = new Menu();
        // 获取 header
        for (MenuDO m : list) {

            //构建菜单明细
            if (m.getParentId() == 0) {
                Menu menu = new Menu();
                menu.setId("" + m.getId());
                menu.setIcon("fa fa-laptop");
                menu.setText(m.getText());
                menu.setUrl(m.getUrl());
                menu.setTargetType(null);

                //查找children菜单
                menu.setChildren(getChildrenMenu(m.getId(), list));
                menuList.add(menu);
            }

        }
        return menuList;
    }


    private List<Menu> getChildrenMenu(Long parentId, List<MenuDO> list) {
        List<Menu> childrenList = new ArrayList<>();
        for (MenuDO m : list) {
            if (m.getParentId().compareTo(parentId) == 0) {
                Menu menu = new Menu();
                menu.setId("" + m.getId());
                menu.setText(m.getText());
                menu.setIcon("fa fa-circle-o");
                menu.setUrl(m.getUrl());
                menu.setTargetType("iframe-tab");
                menu.setChildren(getChildrenMenu(m.getId(), list));
                childrenList.add(menu);
            }
        }
        return childrenList;
    }

}
