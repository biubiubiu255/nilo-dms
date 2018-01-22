package com.nilo.dms.service;


import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.model.UserInfo;

import java.util.List;

public interface UserService {

    void updateUserInfo(UserInfo userInfo);

    void updateLoginInfo(LoginInfo loginInfo);

    void addUser(User user);

    void updateUserRoles(String merchantId, String userId, Long[] roles);

    void updateUserNetwork(String merchantId, String userId, Long[] networks);

    User findByUserId(String merchantId, String userId);

    UserInfo findUserInfoByUserId(String merchantId, String userId);

    List<User> findByUserIds(String merchantId, List<String> userId);

    List<UserInfo> findUserInfoByUserIds(String merchantId, List<String> userId);

    boolean usernameExists(String username);

    User findByUsername(String username);

    List<User> findUserPageBy(String merchantId, String userName, Pagination pagination);

}
