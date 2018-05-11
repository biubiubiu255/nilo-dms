package com.nilo.dms.service.org.impl;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.SerialTypeEnum;
import com.nilo.dms.common.enums.StaffStatusEnum;
import com.nilo.dms.common.enums.UserStatusEnum;
import com.nilo.dms.common.enums.UserTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.CommonDao;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.UserInfoDO;
import com.nilo.dms.dto.common.LoginInfo;
import com.nilo.dms.dto.common.User;
import com.nilo.dms.dto.common.UserInfo;
import com.nilo.dms.dto.org.Department;
import com.nilo.dms.dto.org.Staff;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.org.DepartmentService;
import com.nilo.dms.service.org.StaffService;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.service.system.SystemConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/29.
 */
@Service
public class StaffServiceImpl implements StaffService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void addStaff(Staff staff) {

        String merchantId = staff.getMerchantId();
        if (StringUtil.isEmpty(staff.getStaffId())) {
            String staffID = SystemConfig.getNextSerialNo(merchantId, SerialTypeEnum.STAFF_ID.getCode());
            staff.setStaffId(staffID);
        }
        //查询staffID是否已经存在
        StaffDO queryStaff = staffDao.queryByStaffId(staff.getStaffId());
        if (queryStaff != null) {
            throw new DMSException(BizErrorCode.STAFF_EXIST, staff.getStaffId());
        }
        //查询staffID是否在用户表中存在
        User queryUser = userService.findByUsername(staff.getStaffId());
        if (queryUser != null) {
            throw new DMSException(BizErrorCode.STAFF_EXIST, staff.getStaffId());
        }

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {
                try {
                    //添加用户信息
                    UserInfo userInfo = new UserInfo();
                    userInfo.setName(staff.getRealName());
                    userInfo.setMerchantId(merchantId);
                    userInfo.setEmail(staff.getEmail());
                    userInfo.setPhone(staff.getPhone());

                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setMerchantId(merchantId);
                    loginInfo.setStatus(UserStatusEnum.NORMAL);
                    loginInfo.setUserName(staff.getStaffId());
                    loginInfo.setUserType(UserTypeEnum.NORMAL);
                    loginInfo.setPassword(DigestUtils.sha1Hex("12345678"));
                    User user = new User();
                    user.setMerchantId(merchantId);
                    user.setLoginInfo(loginInfo);
                    user.setUserInfo(userInfo);
                    userService.addUser(user);

                    //添加员工信息
                    staff.setStatus(StaffStatusEnum.REGULAR);
                    StaffDO staffDO = convert(staff);
                    staffDO.setUserId(Long.parseLong(user.getUserId()));
                    staffDao.insert(staffDO);

                } catch (Exception e) {
                    logger.error("addStaff Failed. Data:{}", staff, e);
                    transactionStatus.setRollbackOnly();
                    throw e;
                }
                return null;
            }
        });

    }

    @Override
    @Transactional
    public void updateStaff(Staff staff) {

        StaffDO query = staffDao.queryByStaffId(staff.getStaffId());
        if (query == null) throw new DMSException(BizErrorCode.STAFF_NOT_EXIST);

        StaffDO staffDO = convert(staff);
        staffDao.update(staffDO);

        UserInfo userInfo = new UserInfo();
        userInfo.setMerchantId(staff.getMerchantId());
        userInfo.setUserId("" + query.getUserId());
        userInfo.setName(staff.getRealName());
        userInfo.setPhone(staff.getPhone());
        userInfo.setEmail(staff.getEmail());
        userService.updateUserInfo(userInfo);
    }

    @Override
    public Staff findByStaffId(String companyId, String staffId) {

        StaffDO staffDO = staffDao.queryByStaffId(staffId);
        return convert(staffDO);
    }

    @Override
    public List<Staff> findBy(String companyId, String departmentId, String staffId, String name, Pagination page) {

        List<StaffDO> queryList = staffDao.queryBy(Long.parseLong(companyId), departmentId, staffId, name, page.getOffset(), page.getLimit());
        Long size = commonDao.lastFoundRows();
        page.setTotalCount(size);
        List<Staff> list = new ArrayList<>();
        if (queryList != null) {
            for (StaffDO s : queryList) {
                list.add(convert(s));
            }
        }
        return list;
    }

    @Override
    public List<Staff> findAllStaffBy(String companyId, String departmentId) {
        List<StaffDO> queryList = staffDao.queryAllBy(Long.parseLong(companyId), departmentId);
        List<Staff> list = new ArrayList<>();
        if (queryList != null) {
            for (StaffDO s : queryList) {
                list.add(convert(s));
            }
        }
        return list;
    }

    private Staff convert(StaffDO staffDO) {
        if (staffDO == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setMerchantId("" + staffDO.getMerchantId());
        staff.setCompanyId("" + staffDO.getCompanyId());
        staff.setUserId("" + staffDO.getUserId());
        if (StringUtil.isNotEmpty(staffDO.getDepartmentId())) {
            Department department = departmentService.queryById("" + staffDO.getCompanyId(), staffDO.getDepartmentId());
            staff.setDepartmentName(department.getDepartmentName());
            staff.setDepartmentId(department.getDepartmentId());
        }

        String jobDesc = SystemCodeUtil.getCodeVal("" + staffDO.getMerchantId(), Constant.STAFF_JOB, staffDO.getJob());
        staff.setJobDesc(jobDesc);

        staff.setBirthday(staffDO.getBirthday());
        staff.setEmail(staffDO.getEmail());
        staff.setEmployTime(staffDO.getEmployTime());
        staff.setIdCard(staffDO.getIdCard());
        staff.setJob(staffDO.getJob());
        staff.setNickName(staffDO.getNickName());
        staff.setPhone(staffDO.getPhone());
        staff.setAddress(staffDO.getAddress());
        staff.setRealName(staffDO.getRealName());
        staff.setSex(staffDO.getSex());
        staff.setStaffId(staffDO.getStaffId());
        staff.setTitle(staffDO.getTitle());
        staff.setTitleLevel(staffDO.getTitleLevel());
        staff.setTitleTime(staffDO.getTitleTime());
        staff.setOutsource(staffDO.getOutsource());

        staff.setIsRider(staffDO.getIsRider() == 1);
        staff.setStatus(StaffStatusEnum.getEnum(staffDO.getStatus()));

        staff.setRegularTime(staffDO.getRegularTime());
        staff.setResignedTime(staffDO.getResignedTime());

        return staff;

    }

    private StaffDO convert(Staff staff) {
        StaffDO staffDO = new StaffDO();
        staffDO.setMerchantId(Long.parseLong(staff.getMerchantId()));
        if (StringUtil.isNotEmpty(staff.getCompanyId())) {
            staffDO.setCompanyId(Long.parseLong(staff.getCompanyId()));
        }
        if (staff.isRider() != null) {
            staffDO.setIsRider(staff.isRider() ? 1 : 0);
        }
        if (StringUtil.isNotEmpty(staff.getUserId())) {
            staffDO.setUserId(Long.parseLong(staff.getUserId()));
        }
        staffDO.setBirthday(staff.getBirthday());
        staffDO.setEmail(staff.getEmail());
        staffDO.setEmployTime(staff.getEmployTime());

        staffDO.setRegularTime(staff.getRegularTime());
        staffDO.setResignedTime(staff.getResignedTime());

        staffDO.setIdCard(staff.getIdCard());
        staffDO.setJob(staff.getJob());
        staffDO.setNickName(staff.getNickName());
        staffDO.setPhone(staff.getPhone());
        staffDO.setRealName(staff.getRealName());
        staffDO.setSex(staff.getSex());
        staffDO.setStaffId(staff.getStaffId());
        staffDO.setTitle(staff.getTitle());
        staffDO.setTitleLevel(staff.getTitleLevel());
        staffDO.setTitleTime(staff.getTitleTime());
        staffDO.setAddress(staff.getAddress());
        staffDO.setOutsource(staff.getOutsource());
        if (staff.getStatus() != null) {
            staffDO.setStatus(staff.getStatus().getCode());
        }
        staffDO.setDepartmentId(staff.getDepartmentId());
        return staffDO;

    }
}
