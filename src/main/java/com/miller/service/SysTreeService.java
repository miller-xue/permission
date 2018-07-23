package com.miller.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.miller.dao.SysDeptMapper;
import com.miller.dto.DeptLevelDto;
import com.miller.model.SysDept;
import com.miller.util.LevelUtil;
import com.miller.util.TreeBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by miller on 2018/7/22
 * @author Miller
 * 树的泛型方法已经实现,TreeBuilder
 */
@Deprecated
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper deptMapper;

    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = deptMapper.getAllDept();

        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return  TreeBuilder.makeTreeList(dtoList, "id", "parentId");

    }


    public List<DeptLevelDto> deptList2Tree(List<DeptLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }
        // level -> key [dept1, dept2...........]
        Multimap<String, DeptLevelDto> levelDtoMap = ArrayListMultimap.create();


        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto : dtoList) {
            levelDtoMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 对root进行排序 按照seq从小到大排序
        Collections.sort(rootList, deptLevelDtoComparator);

        transformDeptTree(rootList, LevelUtil.ROOT, levelDtoMap);

        return rootList;
    }

    public void transformDeptTree(List<DeptLevelDto> dtoList, String level, Multimap<String, DeptLevelDto> levelDtoMap) {
        for (DeptLevelDto dto : dtoList) {
            // 遍历该层的每个元素
            String nextLevel = LevelUtil.caculateLevel(level, dto.getId());

            // 处理当前的层级

            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDtoMap.get(nextLevel);

            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList, deptLevelDtoComparator);
                //设置下一层
                dto.setChildren(tempDeptList);

                // 进入到下一层去处理
                transformDeptTree(tempDeptList, nextLevel, levelDtoMap);
            }
        }
    }

    public  Comparator<DeptLevelDto> deptLevelDtoComparator = new Comparator<DeptLevelDto>() {
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
