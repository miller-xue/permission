package com.miller.service;

import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.model.*;
import com.miller.param.SearchLogParam;

import java.util.List;

/**
 * Created by miller on 2018/8/5
 *
 * @author Miller
 */
public interface SysLogService {

    void saveDeptLog(SysDept before, SysDept after);

    void saveUserLog(SysUser before, SysUser after);

    void saveAclModuleLog(SysAclModule before, SysAclModule after);

    void saveAclLog(SysAcl before, SysAcl after);

    void saveRoleLog(SysRole before, SysRole after);

    PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page);

    void recover(int id);
}
