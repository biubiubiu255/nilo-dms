package com.nilo.dms.web.controller;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.utils.FileUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.AreaDao;
import com.nilo.dms.dao.dataobject.AreaDO;
import com.nilo.dms.dao.dataobject.MenuDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/22.
 */
@Controller
public class AddressBuild {

    private static final String path = "F:\\ronny_1";

    @Autowired
    private AreaDao areaDao;

    @RequestMapping(value = "/buildAddress.html")
    public String test1(String countryCode) {

        if (StringUtil.isEmpty(countryCode)) {
            return "empty";
        }

        List<AreaDO> list = areaDao.findAll(countryCode);

        List<Address> addressList = new ArrayList<>();
        for (AreaDO m : list) {
            if (m.getParentId() == 0) {
                Address address = new Address();
                address.setCode("" + m.getAreaId());
                address.setName(m.getName());
                address.setChilds(getChildrenAddress(m.getParentId(), list));
                addressList.add(address);
            }

        }
        try {
            FileUtil.saveTextFile(JSON.toJSONString(addressList), path, countryCode + ".json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    static class Address {

        private String name;

        private String code;

        private List<Address> childs;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<Address> getChilds() {
            return childs;
        }

        public void setChilds(List<Address> childs) {
            this.childs = childs;
        }
    }


    private List<Address> getChildrenAddress(Long parentId, List<AreaDO> list) {
        List<Address> childrenList = new ArrayList<>();
        for (AreaDO m : list) {
            if (m.getParentId().compareTo(parentId) == 0) {
                Address address = new Address();
                address.setName(m.getName());
                address.setCode("" + m.getAreaId());
                address.setChilds(getChildrenAddress(m.getAreaId(), list));
                childrenList.add(address);
            }
        }
        return childrenList;
    }
}
