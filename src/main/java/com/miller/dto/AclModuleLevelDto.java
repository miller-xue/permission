package com.miller.dto;

import com.google.common.collect.Lists;
import com.miller.common.BaseTree;
import com.miller.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miller on 2018/7/28
 * @author Miller
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto  extends BaseTree<AclModuleLevelDto> {

    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private Integer status;

    private String remark;

    private List<AclDto> aclList = new ArrayList<AclDto>();

    public static AclModuleLevelDto adapt(SysAclModule dept) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }

    public static List<AclModuleLevelDto> adaptList(List<SysAclModule> sysAclModuleList) {
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule sysAclModule : sysAclModuleList) {
            dtoList.add(adapt(sysAclModule));
        }
        return dtoList;
    }
}