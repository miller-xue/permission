package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.dao.SysDeptMapper;
import com.miller.enums.ResultEnum;
import com.miller.model.SysDept;
import com.miller.param.DeptParam;
import com.miller.service.SysDeptService;
import com.miller.util.BeanValidator;
import com.miller.util.LevelUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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

    /**
     * 判断是否存在
     * @param parentId
     * @param deptName
     * @param deptId
     * @return
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        //TODO
        return true;
    }

    private String getLevel(Integer deptId) {
        SysDept dept = mapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }
}
