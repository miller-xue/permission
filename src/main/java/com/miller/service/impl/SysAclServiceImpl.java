package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.common.RequestHolder;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysAclModuleMapper;
import com.miller.enums.ResultEnum;
import com.miller.model.SysAcl;
import com.miller.model.SysAclModule;
import com.miller.param.AclParam;
import com.miller.service.SysAclModuleService;
import com.miller.service.SysAclService;
import com.miller.util.BeanValidator;
import com.miller.util.IpUtil;
import com.mysql.fabric.xmlrpc.base.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by miller on 2018/7/29
 *
 * @author Miller
 */
@Service
public class SysAclServiceImpl implements SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    SysAclModuleMapper moduleMapper;

    public void save(AclParam param) {
        BeanValidator.check(param);
        SysAclModule sysAclModule = moduleMapper.selectByPrimaryKey(param.getAclModuleId());
        if (sysAclModule == null) {
            throw new ParamException(ResultEnum.ACL_MODULE_NOT_EXIST);
        }

        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException(ResultEnum.ACL_NAME_EXIST);
        }
        SysAcl sysAcl = param2Model(param);
        sysAcl.setCode(gererateCode());
        sysAclMapper.insertSelective(sysAcl);
    }

    public void update(AclParam param) {
        BeanValidator.check(param);
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(ResultEnum.ACL_NOT_EXIST);
        }

        SysAclModule sysAclModule = moduleMapper.selectByPrimaryKey(param.getAclModuleId());
        if (sysAclModule == null) {
            throw new ParamException(ResultEnum.ACL_MODULE_NOT_EXIST);
        }
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException(ResultEnum.ACL_NAME_EXIST);
        }
        SysAcl after = param2Model(param);

        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }


    private SysAcl param2Model(AclParam param){
        SysAcl sysAcl = new SysAcl();
        BeanUtils.copyProperties(param, sysAcl);
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAcl.setOperatorTime(new Date());
        return sysAcl;
    }

    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }


    public String gererateCode() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date()) + "_" + ((int)Math.random() * 100);
    }
}
