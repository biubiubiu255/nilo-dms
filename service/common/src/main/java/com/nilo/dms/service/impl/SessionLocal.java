package com.nilo.dms.service.impl;

import com.nilo.dms.common.Principal;
import com.nilo.dms.service.model.User;

/**
 * Created by admin on 2018/4/10.
 */
public class SessionLocal {
    private static ThreadLocal<Principal> local = new ThreadLocal<Principal>();

    public static void setPrincipal(Principal principal) {
        local.set(principal);
    }

    public static Principal getPrincipal() {
        return local.get();
    }
}
