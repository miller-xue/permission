package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.miller.Exception.ParamException;
import com.miller.common.RequestHolder;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysRoleAclMapper;
import com.miller.dao.SysRoleMapper;
import com.miller.dto.AclDto;
import com.miller.dto.AclModuleLevelDto;
import com.miller.enums.result.RoleResult;
import com.miller.model.SysAcl;
import com.miller.model.SysRole;
import com.miller.param.RoleParam;
import com.miller.service.SysCoreService;
import com.miller.service.SysLogService;
import com.miller.service.SysRoleService;
import com.miller.util.BeanValidator;
import com.miller.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysCoreService coreService;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public void save(RoleParam param) {
        // 1.参数校验
        BeanValidator.check(param);
        // 2.参数转对象
        SysRole role = param2Role(param);
        // 3.判断全局角色名是否重复
        if (checkExist(role.getName(), role.getId())) {
            throw new ParamException(RoleResult.ROLE_NAME_EXIST);
        }
        // 4.保存
        sysRoleMapper.insertSelective(role);
        sysLogService.saveRoleLog(null, role);
    }


    @Override
    public void update(RoleParam param) {
        // 1.参数校验
        BeanValidator.check(param);
        // 2.查询未修改前对象,判断是否存在.不存在抛出异常
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(RoleResult.ROLE_NOT_EXIST);
        }
        // 3.参数转对象
        SysRole after = param2Role(param);
        // 4.根据before 和 after 判断是否修改了名称
        if (!before.getName().equals(after.getName())) {
            // 4.1 修改了名称判断是否唯一 否抛出异常
            if (checkExist(after.getName(), after.getId())) {
                throw new ParamException(RoleResult.ROLE_NAME_EXIST);
            }
        }
        // 5.更新
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);
    }

    @Override
    public List<SysRole> getAll() {
        return sysRoleMapper.selectAll();
    }


    @Override
    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1.当前用户已分配的权限点
        List<SysAcl> userAclList = coreService.getCurrentUserAclList();
        // 2.当前角色分配的权限点
        List<SysAcl> roleAclList = coreService.getRoleAclList(roleId);
        // 3.当前系统所有的权限点
        List<SysAcl> allAclList = sysAclMapper.selectAll();

        // 用户权限id set
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        // 角色权限id set
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());


        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl sysAcl : allAclList) {
            AclDto temp = AclDto.adapt(sysAcl);
            if (userAclIdSet.contains(sysAcl.getId())) {
                temp.setHasAcl(true);
            }
            if (roleAclIdSet.contains(sysAcl.getId())) {
                temp.setChecked(true);
            }
            aclDtoList.add(temp);
        }
        return coreService.aclListToTree(aclDtoList);
    }

    @Override
    public List<SysRole> getRoleListByUserId(int userId) {
        // 根据用户查询用户所属角色列表
        return sysRoleMapper.selectListByUserId(userId);
    }

    @Override
    public List<SysRole> getRoleListByAclId(int aclId) {
        return sysRoleMapper.selectListByAclId(aclId);
    }

    /**
     * 拼接AclModule acl 拼接成树
     * @param aclDtoList
     * @return

    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleList = AclModuleLevelDto.adaptList(sysAclModuleMapper.getAllAclModule());


        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclListWithOrder(aclModuleList, moduleIdAclMap);

        return TreeBuilder.makeTreeList(aclModuleList, "id", "parentId");
    }
     */
    /**
     * 权限模块绑定权限
     * @param aclModuleList
     * @param moduleIdAclMap

    public void bindAclListWithOrder(List<AclModuleLevelDto> aclModuleList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if(CollectionUtils.isEmpty(aclModuleList)){
            return;
        }
        for (AclModuleLevelDto aclModuleLevelDto : aclModuleList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(aclModuleLevelDto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, aclSqlCompartor);
                aclModuleLevelDto.setAclList(aclDtoList);
            }
        }
    }
     */

    /**
     * 判断名称是否重复
     * @param name
     * @param id
     * @return
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

    /**
     * 参数转对象
     * @param param
     * @return
     */
    public SysRole param2Role(RoleParam param) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(param, sysRole);
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperatorTime(new Date());
        return sysRole;
    }

    /*public Comparator<AclDto> aclSqlCompartor = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };*/
}
