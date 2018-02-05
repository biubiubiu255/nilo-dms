package com.nilo.dms.service.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.UserStatusEnum;
import com.nilo.dms.common.enums.UserTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.IdWorker;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dao.dataobject.UserLoginDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserLoginDao userLoginDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserNetworkDao userNetworkDao;

    @Autowired
    private ThirdExpressDao ThirdExpressDao;


    @Override
    public void updateUserInfo(UserInfo userInfo) {

        AssertUtil.isNotBlank(userInfo.getUserId(), BizErrorCode.USER_ID_ILLEGAL);
        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(userInfo.getMerchantId()), Long.parseLong(userInfo.getUserId()));
        if (userInfoDO == null) {
            throw new DMSException(BizErrorCode.USER_ID_ILLEGAL);
        }
        UserInfoDO update = convert(userInfo);
        userInfoDao.update(update);
    }

    @Override
    public void updateLoginInfo(LoginInfo loginInfo) {
        AssertUtil.isNotBlank(loginInfo.getUserId(), BizErrorCode.USER_ID_ILLEGAL);
        UserLoginDO loginDO = userLoginDao.findByUserId(Long.parseLong(loginInfo.getMerchantId()), Long.parseLong(loginInfo.getUserId()));
        AssertUtil.isNotNull(loginDO, BizErrorCode.USER_ID_ILLEGAL);

        UserLoginDO update = convert(loginInfo);
        userLoginDao.update(update);
    }

    /**
     * 新增用户信息
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public void addUser(User user) {

        //校验
        AssertUtil.isNotNull(user, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotNull(user.getLoginInfo(), SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotNull(user.getUserInfo(), SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotBlank(user.getMerchantId(), BizErrorCode.MERCHANT_ID_EMPTY);
        AssertUtil.isNotBlank(user.getLoginInfo().getPassword(), BizErrorCode.PASSWORD_EMPTY);
        AssertUtil.isNotBlank(user.getLoginInfo().getUserName(), BizErrorCode.USER_NAME_EMPTY);
        AssertUtil.isNotNull(user.getLoginInfo().getUserType(), BizErrorCode.USER_TYPE_EMPTY);
        AssertUtil.isNotBlank(user.getUserInfo().getName(), BizErrorCode.NAME_EMPTY);
        AssertUtil.isNotBlank(user.getUserInfo().getEmail(), BizErrorCode.EMAIL_EMPTY);
        AssertUtil.isNotBlank(user.getUserInfo().getPhone(), BizErrorCode.PHONE_EMPTY);

        //判断userName是否存在
        AssertUtil.isFalse(usernameExists(user.getLoginInfo().getUserName()), BizErrorCode.USER_NAME_EXIST);


        //生成新的userId
        Long userId = IdWorker.getInstance().nextId();
        user.setUserId("" + userId);

        UserInfo userInfo = user.getUserInfo();
        userInfo.setMerchantId(user.getMerchantId());
        userInfo.setUserId("" + userId);
        UserInfoDO insert = convert(userInfo);
        userInfoDao.insert(insert);

        LoginInfo loginInfo = user.getLoginInfo();
        loginInfo.setStatus(UserStatusEnum.NORMAL);
        loginInfo.setMerchantId(user.getMerchantId());
        loginInfo.setUserId("" + userId);
        UserLoginDO insert1 = convert(loginInfo);
        userLoginDao.insert(insert1);

    }

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roles
     */
    @Override
    @Transactional
    public void updateUserRoles(String merchantId, String userId, Long[] roles) {

        AssertUtil.isNotBlank(userId, BizErrorCode.USER_ID_ILLEGAL);
        Long userIdLong = Long.parseLong(userId);
        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(merchantId), Long.parseLong(userId));
        AssertUtil.isNotNull(userInfoDO, BizErrorCode.USER_ID_ILLEGAL);

        // 先删后增
        roleDao.deleteAll(userIdLong);
        if (roles != null && roles.length > 0) {
            roleDao.insertAll(userIdLong, roles);
        }

    }

    @Override
    @Transactional
    public void updateUserNetwork(String merchantId, String userId, Long[] networks) {
        AssertUtil.isNotBlank(userId, BizErrorCode.USER_ID_ILLEGAL);
        Long userIdLong = Long.parseLong(userId);
        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(merchantId), Long.parseLong(userId));
        AssertUtil.isNotNull(userInfoDO, BizErrorCode.USER_ID_ILLEGAL);
        userNetworkDao.deleteAll(userIdLong);
        if (networks != null && networks.length > 0) {
            userNetworkDao.insertAll(userIdLong, networks);
        }
    }

    public boolean usernameExists(String username) {
        UserLoginDO userLoginDO = userLoginDao.findByUserName(username);
        if (userLoginDO == null) {
            return false;
        }
        return true;
    }

    public User findByUsername(String username) {
        UserLoginDO userLoginDO = userLoginDao.findByUserName(username);
        if (userLoginDO == null) {
            return null;
        }
        UserInfoDO userInfoDO = userInfoDao.queryByUserId(userLoginDO.getMerchantId(), userLoginDO.getUserId());
        User user = new User();
        user.setMerchantId("" + userInfoDO.getMerchantId());
        user.setUserId("" + userInfoDO.getId());

        user.setUserInfo(convert(userInfoDO));
        user.setLoginInfo(convert(userLoginDO));
        return user;
    }

    @Override
    public List<User> findUserPageBy(String merchantId, String userName, Pagination pagination) {

        List<User> userList = new ArrayList<>();
        Long count = userLoginDao.findCountBy(Long.parseLong(merchantId), userName);
        if (count == 0) {
            return userList;
        }
        pagination.setTotalCount(count);
        List<UserLoginDO> list = userLoginDao.findBy(Long.parseLong(merchantId), userName, pagination.getOffset(), pagination.getLimit());

        List<Long> userIds = new ArrayList<>();
        for (UserLoginDO loginDO : list) {
            userIds.add(loginDO.getUserId());
        }
        List<UserInfoDO> userInfoList = userInfoDao.queryByUserIds(Long.parseLong(merchantId), userIds);

        for (UserLoginDO loginDO : list) {
            User user = new User();
            user.setMerchantId("" + loginDO.getMerchantId());
            user.setUserId("" + loginDO.getUserId());
            for (UserInfoDO infoDO : userInfoList) {
                if (infoDO.getId().compareTo(loginDO.getUserId()) == 0) {
                    user.setUserInfo(convert(infoDO));
                    break;
                }
            }

            user.setLoginInfo(convert(loginDO));
            userList.add(user);
        }

        return userList;
    }

    @Override
    public User findByUserId(String merchantId, String userId) {

        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(merchantId), Long.parseLong(userId));
        if (userInfoDO == null) return null;

        UserLoginDO loginDO = userLoginDao.findByUserId(Long.parseLong(merchantId), Long.parseLong(userId));

        User user = new User();
        user.setMerchantId("" + userInfoDO.getMerchantId());
        user.setUserId("" + userInfoDO.getId());

        user.setUserInfo(convert(userInfoDO));
        user.setLoginInfo(convert(loginDO));
        return user;
    }


    @Override
    public UserInfo findUserInfoByUserId(String merchantId, String userId) {
        UserInfoDO userInfoDO = userInfoDao.queryByUserId(Long.parseLong(merchantId), Long.parseLong(userId));

        if (userInfoDO == null) return null;

        return convert(userInfoDO);
    }

    @Override
    public List<User> findByUserIds(String merchantId, List<String> userId) {

        List<Long> userIds = new ArrayList<>();
        if (userId == null || userId.size() == 0) {
            return null;
        }
        for (String s : userId) {
            if (StringUtil.isNotEmpty(s)) {
                userIds.add(Long.parseLong(s));
            }
        }
        List<UserInfoDO> userInfoList = userInfoDao.queryByUserIds(Long.parseLong(merchantId), userIds);
        List<UserLoginDO> userLoginDOList = userLoginDao.findByUserIds(Long.parseLong(merchantId), userIds);

        List<User> userList = new ArrayList<>();

        for (UserInfoDO i : userInfoList) {
            User user = new User();
            user.setUserId("" + i.getId());
            user.setMerchantId("" + i.getMerchantId());

            user.setUserInfo(convert(i));
            for (UserLoginDO l : userLoginDOList) {
                if (i.getId().compareTo(l.getUserId()) == 0) {
                    user.setLoginInfo(convert(l));
                    break;
                }
            }
            userList.add(user);
        }

        return userList;

    }

    @Override
    public List<UserInfo> findUserInfoByUserIds(String merchantId, List<String> userId) {
        List<Long> userIds = new ArrayList<>();
        if (userId == null || userId.size() == 0) {
            return null;
        }
        for (String s : userId) {
            if (StringUtil.isNotEmpty(s)) {
                userIds.add(Long.parseLong(s));
            }
        }
        List<UserInfoDO> userInfoList = userInfoDao.queryByUserIds(Long.parseLong(merchantId), userIds);

        List<UserInfo> userList = new ArrayList<>();

        for (UserInfoDO i : userInfoList) {
            UserInfo user = convert(i);
            user.setUserId("" + i.getId());
            user.setMerchantId("" + i.getMerchantId());
            userList.add(user);
        }

        return userList;
    }

    private UserInfo convert(UserInfoDO userInfoDO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setMerchantId("" + userInfoDO.getMerchantId());
        userInfo.setUserId("" + userInfoDO.getId());
        userInfo.setName(userInfoDO.getName());
        userInfo.setEmail(userInfoDO.getEmail());
        userInfo.setPhone(userInfoDO.getPhone());
        userInfo.setCreatedTime(userInfoDO.getCreatedTime());
        return userInfo;
    }

    private UserInfoDO convert(UserInfo userInfo) {
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setMerchantId(Long.parseLong(userInfo.getMerchantId()));
        userInfoDO.setId(Long.parseLong(userInfo.getUserId()));
        userInfoDO.setName(userInfo.getName());
        userInfoDO.setEmail(userInfo.getEmail());
        userInfoDO.setPhone(userInfo.getPhone());

        return userInfoDO;
    }

    private LoginInfo convert(UserLoginDO loginDO) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setMerchantId("" + loginDO.getMerchantId());
        loginInfo.setUserId("" + loginDO.getUserId());
        loginInfo.setPassword(loginDO.getPassword());
        loginInfo.setUserName(loginDO.getUserName());
        loginInfo.setStatus(UserStatusEnum.getEnum(loginDO.getStatus()));
        loginInfo.setUserType(UserTypeEnum.getEnum(loginDO.getUserType()));
        return loginInfo;
    }

    private UserLoginDO convert(LoginInfo loginInfo) {
        UserLoginDO loginDO = new UserLoginDO();
        loginDO.setMerchantId(Long.parseLong(loginInfo.getMerchantId()));
        loginDO.setUserId(Long.parseLong(loginInfo.getUserId()));
        loginDO.setPassword(loginInfo.getPassword());
        loginDO.setUserName(loginInfo.getUserName());
        if (loginInfo.getStatus() != null) {
            loginDO.setStatus(loginInfo.getStatus().getCode());
        }
        if (loginInfo.getUserType() != null) {
            loginDO.setUserType(loginInfo.getUserType().getCode());
        }
        return loginDO;
    }

    @Override
    public List<ThirdExpressDO> findUserPageByExpresses(String merchantId,
                                                        ThirdExpressDO express, Pagination pagination) {

        List<ThirdExpressDO> list = new ArrayList<ThirdExpressDO>();

        list = ThirdExpressDao.findByExpress(express);

        pagination.setTotalCount(list.size());

        return list;
    }

    @Override
    public void addExpress(ThirdExpressDO express) {

        ThirdExpressDao.addExpress(express);
    }

    @Override
    public void updateExpress(ThirdExpressDO express) {
        ThirdExpressDao.updateExpress(express);

    }

    @Override
    public void deleteExpress(ThirdExpressDO express) {
        ThirdExpressDao.deleteExpress(express);

    }

    @Override
    public List<ThirdExpressDO> findExpressesAll(Pagination page) {

        List<ThirdExpressDO> list = new ArrayList<ThirdExpressDO>();

        list = ThirdExpressDao.findByMerchantIdAll();

        page.setTotalCount(list.size());

        return list;
    }


}
