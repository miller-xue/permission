package com.miller.dto;

import com.google.common.collect.Lists;
import com.miller.common.BaseTree;
import com.miller.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by miller on 2018/7/22
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends BaseTree<DeptLevelDto> {

    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String remark;

    private String operator;

    private Date operateTime;

    private String operateIp;

    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, deptLevelDto);
        return deptLevelDto;
    }

    public static List<DeptLevelDto> adaptList(List<SysDept> depts) {
        List<DeptLevelDto> result = Lists.newArrayList();
        for (SysDept dept : depts) {
            result.add(adapt(dept));
        }
        return result;
    }

}
