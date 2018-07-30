package com.miller.dto;

import com.miller.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miller on 2018/7/30
 *
 * @author Miller
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    /**
     * 是否有权限操作
     */
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl sysAcl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(sysAcl, aclDto);
        return aclDto;
    }
}
