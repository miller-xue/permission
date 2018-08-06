package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.common.RequestHolder;
import com.miller.constant.LogType;
import com.miller.dao.*;
import com.miller.dto.SearchLogDto;
import com.miller.enums.result.LogResult;
import com.miller.enums.result.SysResult;
import com.miller.model.*;
import com.miller.param.SearchLogParam;
import com.miller.service.SysLogService;
import com.miller.service.SysRoleAclService;
import com.miller.service.SysRoleUserService;
import com.miller.util.BeanValidator;
import com.miller.util.IpUtil;
import com.miller.util.JsonMapper;
import com.miller.util.SysUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by miller on 2018/8/5
 * @author Miller
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleAclService sysRoleAclService;

    @Resource
    private SysRoleUserService sysRoleUserService;

    @Override
    public void recover(int id) {
        SysLogWithBLOBs log = sysLogMapper.selectByPrimaryKey(id);
        if (log == null) {
            throw new ParamException(SysResult.PARAM_ERROR);
        }
        switch (log.getType()) {
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeDept == null) {
                    throw new ParamException(1, "待还原的部门不存在");
                }
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException(LogResult.INSERT_AND_DELETE_NOT_RECOVER);
                }
                SysDept afterDept = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysDept>() {
                });
                SysUtil.invokeSetOperate(afterDept);
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeUser == null) {
                    throw new ParamException(1, "待还原的用户不存在");
                }
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException(LogResult.INSERT_AND_DELETE_NOT_RECOVER);
                }
                SysUser afterUser = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysUser>() {
                });
                SysUtil.invokeSetOperate(afterUser);
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeAclModule == null) {
                    throw new ParamException(1, "待还原的权限模块不存在");
                }
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException(LogResult.INSERT_AND_DELETE_NOT_RECOVER);
                }
                SysAclModule afterAclModule = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysAclModule>() {
                });
                SysUtil.invokeSetOperate(afterAclModule);
                sysAclModuleMapper.updateByPrimaryKeySelective(afterAclModule);
                saveAclModuleLog(beforeAclModule, afterAclModule);
                break;
            case LogType.TYPE_ACL:
                SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeAcl == null) {
                    throw new ParamException(1, "待还原的权限不存在");
                }
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException(LogResult.INSERT_AND_DELETE_NOT_RECOVER);
                }
                SysAcl afterAcl = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysAcl>() {
                });
                SysUtil.invokeSetOperate(afterAcl);
                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                if (beforeRole == null) {
                    throw new ParamException(1, "待还原的角色不存在");
                }
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException(LogResult.INSERT_AND_DELETE_NOT_RECOVER);
                }
                SysRole afterRole = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysRole>() {
                });
                SysUtil.invokeSetOperate(afterRole);
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                if (aclRole == null) {
                    throw new ParamException(1, "角色已经不存在了");
                }
                sysRoleAclService.changeRoleAcls(log.getTargetId(),JsonMapper.string2Obj(log.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole userRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                if (userRole == null) {
                    throw new ParamException(1, "角色已经不存在了");
                }
                sysRoleAclService.changeRoleAcls(log.getTargetId(),JsonMapper.string2Obj(log.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            default:;
        }
    }

    @Override
    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        // 新增没有after
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperator(sysLog);
        sysLogMapper.insert(sysLog);
    }

    @Override
    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        // 新增没有after
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperator(sysLog);
        sysLogMapper.insert(sysLog);
    }

    @Override
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        // 新增没有after
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperator(sysLog);
        sysLogMapper.insert(sysLog);
    }

    @Override
    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        // 新增没有after
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperator(sysLog);
        sysLogMapper.insert(sysLog);
    }

    @Override
    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        // 新增没有after
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        setOperator(sysLog);
        sysLogMapper.insert(sysLog);
    }

    private void setOperator(SysLogWithBLOBs log) {
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(new Date());
        log.setStatus(1);
    }

    @Override
    public PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page) {
        BeanValidator.check(page);
        SearchLogDto dto = new SearchLogDto();
        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeg())) {
            dto.setBeforeSeg("%" + param.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getAfterSeg())) {
            dto.setAfterSeg("%" + param.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            dto.setOperator("%" + param.getOperator() + "%");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFormTime())) {
                dto.setFormTime(dateFormat.parse(param.getFormTime()));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                dto.setToTime(dateFormat.parse(param.getToTime()));
            }
        } catch (Exception e) {
            throw new ParamException(SysResult.PARAM_DATE_ERROR);
        }
        int count = sysLogMapper.countBySearchDto(dto);
        if (count > 0) {
            List<SysLogWithBLOBs> sysLogList = sysLogMapper.selectPageListBySearchDto(dto, page);
            return PageResult.<SysLogWithBLOBs>builder().total(count).data(sysLogList).build();
        }
        return PageResult.<SysLogWithBLOBs>builder().build();
    }


}
