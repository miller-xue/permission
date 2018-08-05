package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.common.RequestHolder;
import com.miller.constant.SysConstans;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysAclModuleMapper;
import com.miller.dto.AclModuleLevelDto;
import com.miller.enums.result.AclModuleResult;
import com.miller.model.SysAclModule;
import com.miller.param.AclModuleParam;
import com.miller.service.SysAclModuleService;
import com.miller.service.SysLogService;
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

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public void save(AclModuleParam param) throws ParamException {
        // 1.必填参数校验
        BeanValidator.check(param);
        // 2.对象转换
        SysAclModule sysAclModule = param2Model(param);
        // 3.判断父节点是否存在 && 计算level level == 上级level . 上级id
        if (!param.getParentId().equals(SysConstans.ROOT_PARENT_ID)) {
            SysAclModule  parentModule = sysAclModuleMapper.selectByPrimaryKey(param.getParentId());
            if (parentModule == null) {
                throw new ParamException(AclModuleResult.PARENT_NOT_EXIST);
            }
            sysAclModule.setLevel(LevelUtil.caculateLevel(parentModule.getLevel(), parentModule.getId()));
        }else {
            sysAclModule.setLevel(LevelUtil.caculateLevel(getLevel(sysAclModule.getParentId()), sysAclModule.getParentId()));
        }
        // 4.判断父节点下名称是否唯一
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException(AclModuleResult.ACL_MODULE_NAME_EXIST);
        }


        sysAclModuleMapper.insertSelective(sysAclModule);
        sysLogService.saveAclModuleLog(null, sysAclModule);
    }

    @Override
    public List<AclModuleLevelDto> aclModuleTree() {
        // 1.查询所有权限模块
        List<SysAclModule> allAclModule = sysAclModuleMapper.getAllAclModule();
        // 2.转换成DTO
        List<AclModuleLevelDto> dtoList = AclModuleLevelDto.adaptList(allAclModule);
        // 3.使用工具拼装成树
        return TreeBuilder.makeTreeList(dtoList,"id","parentId");
    }

    @Override
    @Transactional
    public void update(AclModuleParam param) throws ParamException {
        // 1.必填参数校验
        BeanValidator.check(param);
        // 2.判断被修改的对象是否存在
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(AclModuleResult.ACL_MODULE_NOT_EXIST);
        }

        SysAclModule after = param2Model(param);
        // 3.没有修改部门
//        if (after.getParentId().equals(before.getParentId())) {
//            sysAclModuleMapper.updateByPrimaryKeySelective(after);
//            return;
//        }

        // 3.当前id父节点不能是自己
        if (after.getParentId().equals(after.getId())) {
            throw new ParamException(AclModuleResult.PARENT_ID_NOT_EQUALS_ID);
        }
        // 3.检查父节点是否存在
        if (!after.getParentId().equals(SysConstans.ROOT_PARENT_ID)) {
            SysAclModule afterParent = sysAclModuleMapper.selectByPrimaryKey(after.getParentId());
            // 判断父节点是否存在
            if (afterParent == null) {
                throw new ParamException(AclModuleResult.PARENT_NOT_EXIST);
            }
            // 子节点不能为当前的父节点
            if (afterParent.getLevel().indexOf(before.getLevel()) == 0 && afterParent.getLevel().length() > before.getLevel().length()) {
                throw new ParamException(AclModuleResult.PARENT_NOT_CHILD);
            }
            after.setLevel(LevelUtil.caculateLevel(afterParent.getLevel(), afterParent.getId()));
        }else {
            after.setLevel(LevelUtil.caculateLevel(getLevel(after.getParentId()),after.getParentId()));
        }

        // 判断同父节点下名称是否唯一
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException(AclModuleResult.ACL_MODULE_NAME_EXIST);
        }

        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);
    }



    /**
     * 修改自己和子节点的level
     * @param before
     * @param after
     */
    private void updateWithChild(SysAclModule before, SysAclModule after) {
        // 修改前level
        String oldLevelPrefix = before.getLevel();
        // 修改后的level
        String newLevelPrefix = after.getLevel();

        // 查询出所有修改对象的子节点，不包括自己
        List<SysAclModule> childList = sysAclModuleMapper.getChildAclModuleListByLevel(oldLevelPrefix + before.getId());
        if (CollectionUtils.isNotEmpty(childList)) {
            for (SysAclModule aclModule : childList) {
                String level = aclModule.getLevel();
                if (level.indexOf(oldLevelPrefix) == 0) {
                    level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                    aclModule.setLevel(level);
                }
            }
            sysAclModuleMapper.batchUpdateLevel(childList);
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public void delete(int aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        // 1.删除的对象是否为空
        if (sysAclModule == null) {
            throw new ParamException(AclModuleResult.ACL_MODULE_NOT_EXIST);
        }
        // 2.删除的对象是否有子节点
        if (sysAclModuleMapper.countByParentId(aclModuleId) > 0) {
            throw new ParamException(AclModuleResult.ACL_MODULE_HAS_CHILD);
        }
        // 3.删除的对象下是否有acl
        if (sysAclMapper.countByAclModuleId(aclModuleId) > 0) {
            throw new ParamException(AclModuleResult.ACL_MODULE_HAS_ACL);
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

    /**
     * 获得指定对象的level
     * @param aclModuleId
     * @return
     */
    private String getLevel(Integer aclModuleId) {
        // 父节点无level
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
