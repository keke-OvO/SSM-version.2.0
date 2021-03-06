package com.kk.service.impl;


import com.kk.dao.AdminMapper;
import com.kk.pojo.Admin;
import com.kk.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin select(Admin ad) {
        return adminMapper.select(ad);
    }

    @Override
    public Admin check(Map map) {
        return adminMapper.check(map);
    }

    @Override
    public void altpwd(Admin ad) {
        adminMapper.altpwd(ad);
    }
}
