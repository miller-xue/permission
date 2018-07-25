package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.dao.SysUserMapper;
import com.miller.enums.ResultEnum;
import com.miller.model.SysUser;
import com.miller.param.UserParam;
import com.miller.service.SysUserService;
import com.miller.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by miller on 2018/7/25
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper userMapper;

    public void save(UserParam param) {
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException(ResultEnum.USER_TELEPHONE_EXIST);
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException(ResultEnum.USER_EMAIL_EXIST);
        }
        SysUser sysUser = param2SysUser(param);

        // TODO: sendEmail
        userMapper.insertSelective(sysUser);
    }


    public boolean checkEmailExist(String mail, Integer userId) {
        return false;
    }
    public boolean checkTelephoneExist(String mail, Integer userId) {
        return false;
    }

    private SysUser param2SysUser(UserParam param) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(param, user);
        String password = "123456";
        user.setPassword(password);
        user.setOperator("system");
        user.setOperatorIp("127.0.0.1");
        user.setOperatorTime(new Date());
        return user;
    }
}
