package com.nilo.dms.service;


import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dto.common.LoginInfo;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.common.UserInfo;

import java.util.List;

public interface UserService {

    void updateUserInfo(UserInfo userInfo);

    void updateLoginInfo(LoginInfo loginInfo);

    void addUser(User user);

    void updateUserRoles(String merchantId, String userId, Long[] roles);

    void updateUserNetwork(String merchantId, String userId, Long[] networks);

    User findByUserId(String merchantId, String userId);

    UserInfo findUserInfoByUserId(String merchantId, String userId);

    //List<User> findByUserIds(String merchantId, List<String> userId);

    boolean usernameExists(String username);

    User findByUsername(String username);

    List<User> findUserPageBy(String merchantId, String userName, Pagination pagination);

    List<UserInfo> findUserInfoByUserIds(String merchantId, List<String> userId);

    //List<User> findByUserIds(String merchantId, List<String> userId);

    List<User> findByUserIds(String merchantId, List<String> userId);

    List<ThirdExpressDO> findUserPageByExpresses(String merchantId, ThirdExpressDO express, Pagination pagination);

    List<ThirdExpressDO> findExpressesAll(Pagination page);

    void addExpress(ThirdExpressDO express);

    void updateExpress(ThirdExpressDO express);

    void deleteExpress(ThirdExpressDO express);

}
