package com.nilo.dms.service;


import com.nilo.dms.dto.common.Menu;

import java.util.List;

/**
 * Created by ronny on 2017/9/14.
 */
public interface MenuService {

    List<Menu> getWebMenu(String userId);
}
