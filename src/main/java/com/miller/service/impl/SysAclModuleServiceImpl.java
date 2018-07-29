package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.common.RequestHolder;
import com.miller.constant.SysConstans;
import com.miller.dao.SysAclModuleMapper;
import com.miller.dto.AclModuleLevelDto;
import com.miller.dto.DeptLevelDto;
import com.miller.enums.ResultEnum;
import com.miller.model.SysAclModule;
import com.miller.param.AclModuleParam;
import com.miller.service.SysAclModuleService;
import com.miller.util.BeanValidator;
import com.miller.util.IpUtil;
import com.miller.util.LevelUtil;
import com.miller.util.TreeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miller on 2018/7/28
 * 权限模块服务层-
 * @author Miller
 */
@Service
@Slf4j
public class SysAclModuleServiceImpl implements SysAclModuleService {


    @Resource
    private SysAclModuleMapper sysAclModuleMapper;


    public void save(AclModuleParam param) throws ParamException {
        BeanValidator.check(param);

        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException(ResultEnum.ACL_MODULE_NAME_EXIST);
        }
        SysAclModule sysAclModule = param2Model(param);
        SysAclModule parentModule = null;
        // 判断父节点是否存在
        if (!param.getParentId().equals(SysConstans.ROOT_PARENT_ID)) {
            parentModule = sysAclModuleMapper.selectByPrimaryKey(param.getParentId());
            if (parentModule == null) {
                throw new ParamException(ResultEnum.PARENT_NOT_EXIST);
            }
            sysAclModule.setLevel(LevelUtil.caculateLevel(parentModule.getLevel(), parentModule.getId()));
        }else {
            sysAclModule.setLevel(LevelUtil.caculateLevel(getLevel(sysAclModule.getParentId()), sysAclModule.getParentId()));
        }

        // 计算level level == 上级level . 上级id
        sysAclModuleMapper.insertSelective(sysAclModule);
    }

    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> allAclModule = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = AclModuleLevelDto.adaptList(allAclModule);

        return TreeBuilder.makeTreeList(dtoList,"id","parentId");
    }

    @Transactional
    public void update(AclModuleParam param) throws ParamException {
        BeanValidator.check(param);
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(ResultEnum.ACL_MODULE_NOT_EXIST);
        }
        SysAclModule after = param2Model(param);
        // 没有修改部门
        if (after.getParentId().equals(before.getParentId())) {
            sysAclModuleMapper.updateByPrimaryKeySelective(after);
            return;
        }
        // 当前id父节点不能是自己
        if (after.getParentId().equals(after.getId())) {
            throw new ParamException(ResultEnum.PARENT_ID_NOT_EQUALS_ID);
        }
        // 检查父节点是否存在
        SysAclModule afterParent = null;
        if (!after.getParentId().equals(SysConstans.ROOT_PARENT_ID)) {
            afterParent = sysAclModuleMapper.selectByPrimaryKey(after.getParentId());
            // 判断父节点是否存在
            if (afterParent == null) {
                throw new ParamException(ResultEnum.PARENT_NOT_EXIST);
            }
            // 子节点不能为当前的父节点
            if (afterParent.getLevel().indexOf(before.getLevel()) == 0 && afterParent.getLevel().length() > before.getLevel().length()) {
                throw new ParamException(ResultEnum.PARENT_NOT_CHILD);
            }
            after.setLevel(LevelUtil.caculateLevel(afterParent.getLevel(), afterParent.getId()));
        }else {
            after.setLevel(LevelUtil.caculateLevel(getLevel(after.getParentId()),after.getParentId()));
        }

        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException(ResultEnum.ACL_MODULE_NAME_EXIST);
        }

        updateWithChild(before, after);
    }

    /**
     * 修改自己和子节点的level
     * @param before
     * @param after
     */
    private void updateWithChild(SysAclModule before, SysAclModule after) {
        String oldLevelPrefix = before.getLevel();
        String newLevelPrefix = after.getLevel();
        List<SysAclModule> childList = sysAclModuleMapper.getChildAclModuleListByLevel(oldLevelPrefix);
        if (CollectionUtils.isNotEmpty(childList)) {
            for (SysAclModule aclModule : childList) {
                String level = aclModule.getLevel();
                if (level.indexOf(oldLevelPrefix) == 0) {
                    level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                    aclModule.setLevel(level);
                }
            }

        }

        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }


    /**
     * 获得指定对象的level
     * @param aclModuleId
     * @return
     */
    public String getLevel(Integer aclModuleId) {
        if (aclModuleId.equals(SysConstans.ROOT_PARENT_ID)) {
            return null;
        }
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }


    /**
     * 判断同级别下是否存在相同name
     * @param parentId
     * @param aclModuleName
     * @param aclModuleId
     * @return
     */
    public boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    /**
     * 参数转 model
     * @param param
     * @return
     */
    public SysAclModule param2Model(AclModuleParam param) {
        SysAclModule sysAclModule = new SysAclModule();
        BeanUtils.copyProperties(param, sysAclModule);

        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperatorTime(new Date());
        return sysAclModule;
    }

}
