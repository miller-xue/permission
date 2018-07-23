package com.miller.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.miller.Exception.ParamException;
import com.miller.dao.SysDeptMapper;
import com.miller.dto.DeptLevelDto;
import com.miller.enums.ResultEnum;
import com.miller.model.SysDept;
import com.miller.param.DeptParam;
import com.miller.service.SysDeptService;
import com.miller.util.BeanValidator;
import com.miller.util.LevelUtil;
import com.miller.util.TreeBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miller on 2018/7/22
 * @author Miller
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper mapper;

    public void save(DeptParam param) throws ParamException {
        // 1.参数校验
        BeanValidator.check(param);
        // 2.检查部门名称是否重复
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException(ResultEnum.DEPT_NAME_EXITS);
        }
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(param, dept);
        dept.setLevel(LevelUtil.caculateLevel(getLevel(param.getParentId()), dept.getParentId()));

        //TODO
        dept.setOperator("system");
        dept.setOperatorIp("127.0.0.1");
        dept.setOperatorTime(new Date());

        mapper.insertSelective(dept);
    }

    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = mapper.getAllDept();

        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return  TreeBuilder.makeTreeList(dtoList, "id", "parentId");

    }

    @Transactional(rollbackFor = Exception.class)
    public void update(DeptParam param) throws ParamException {
        // 1.参数校验
        BeanValidator.check(param);

        // 1.校验当前部门是否存在
        SysDept before = mapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");

        // 2.检查部门名称是否重复
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException(ResultEnum.DEPT_NAME_EXITS);
        }


        // 更新之后的值
        SysDept after = new SysDept();
        BeanUtils.copyProperties(param, after);
        after.setLevel(LevelUtil.caculateLevel(getLevel(after.getParentId()), after.getParentId()));
        after.setOperator("system");
        after.setOperatorIp("127.0.0.1");
        after.setOperatorTime(new Date());
        updateWithChild(before, after);
    }


    private void updateWithChild(SysDept before, SysDept after) {

        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            List<SysDept> childList = mapper.getChildDeptListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(childList)) {
                for (SysDept dept : childList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                mapper.batchUpdateLevel(childList);
            }
        }

        mapper.updateByPrimaryKey(after);
    }


    /**
     * 判断是否存在
     * @param parentId
     * @param deptName
     * @param deptId
     * @return
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return mapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    private String getLevel(Integer deptId) {
        SysDept dept = mapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }
}
