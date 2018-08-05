package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.common.RequestHolder;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysAclModuleMapper;
import com.miller.enums.result.AclModuleResult;
import com.miller.enums.result.AclResult;
import com.miller.model.SysAcl;
import com.miller.model.SysAclModule;
import com.miller.model.SysLog;
import com.miller.param.AclParam;
import com.miller.service.SysAclService;
import com.miller.service.SysLogService;
import com.miller.util.BeanValidator;
import com.miller.util.IpUtil;
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


    @Resource
    private SysLogService sysLogService;

    @Override
    public void save(AclParam param) {
        // 1.参数校验
        BeanValidator.check(param);
        // 2.校验权限模块是否未空
        SysAclModule sysAclModule = moduleMapper.selectByPrimaryKey(param.getAclModuleId());
        if (sysAclModule == null) {
            throw new ParamException(AclModuleResult.ACL_MODULE_NOT_EXIST);
        }
        // 3.同一权限模块下权限名唯一
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException(AclResult.ACL_NAME_EXIST);
        }
        SysAcl sysAcl = param2Model(param);
        // 补充编码数据
        sysAcl.setCode(gererateCode());

        sysAclMapper.insertSelective(sysAcl);
        sysLogService.saveAclLog(null, sysAcl);
    }

    @Override
    public void update(AclParam param) {
        // 1.参数校验
        BeanValidator.check(param);
        // 2.判断被修改的对象是否未空
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(AclResult.ACL_NOT_EXIST);
        }
        // 3.权限模块是否为空
        SysAclModule sysAclModule = moduleMapper.selectByPrimaryKey(param.getAclModuleId());
        if (sysAclModule == null) {
            throw new ParamException(AclModuleResult.ACL_MODULE_NOT_EXIST);
        }
        // 4.同一权限模块下权限名唯一
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException(AclResult.ACL_NAME_EXIST);
        }
        SysAcl after = param2Model(param);
        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before, after);
    }

    @Override
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery) {
        // 1.参数校验
        BeanValidator.check(pageQuery);
        // 2.查询总数
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        // 3.总数大于0开始分页查询数据库
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
