package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.dao.SysUserMapper;
import com.miller.enums.ResultEnum;
import com.miller.model.SysUser;
import com.miller.param.UserParam;
import com.miller.service.SysUserService;
import com.miller.util.BeanValidator;
import com.miller.util.MD5Util;
import com.miller.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by miller on 2018/7/25
 * @author Miller
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper userMapper;

    public void save(UserParam param) {
        // 1.参数校验
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException(ResultEnum.USER_TELEPHONE_EXIST);
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException(ResultEnum.USER_EMAIL_EXIST);
        }
        SysUser sysUser = param2SysUser(param);
        // 密码加密
        sysUser.setPassword(MD5Util.encrypt(PasswordUtil.randomPassword()));
        // TODO: sendEmail
        userMapper.insertSelective(sysUser);
    }

    public void update(UserParam param) {
        // 1.参数校验
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException(ResultEnum.USER_TELEPHONE_EXIST);
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException(ResultEnum.USER_EMAIL_EXIST);
        }
        // 2. 查询待更新的用户是否存在
        SysUser before = userMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(ResultEnum.USER_NOT_EXIST);
        }
        // 更新后的对象
        SysUser after = param2SysUser(param);
        // 更新
        userMapper.updateByPrimaryKeySelective(after);

    }

    public SysUser findByKeyword(String keyword) {
        return userMapper.findByKeyword(keyword);
    }


    public boolean checkEmailExist(String mail, Integer userId) {
        return userMapper.countByMail(mail, userId) > 0;
    }
    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return userMapper.countByTelephone(telephone,userId) > 0;
    }

    private SysUser param2SysUser(UserParam param) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(param, user);
        user.setOperator("system");
        user.setOperatorIp("127.0.0.1");
        user.setOperatorTime(new Date());
        return user;
    }
}
