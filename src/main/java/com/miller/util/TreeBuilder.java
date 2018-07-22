package com.miller.util;

import com.google.common.collect.Lists;
import com.miller.common.BaseTree;
import com.miller.constant.SysConstans;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miller on 2018/7/22
 */
public class TreeBuilder {

    public static <T extends BaseTree> List<T> makeTreeList(List<T> list, String idAttribute,
                                                            String parentIdAttribute) {
        if (CollectionUtils.isEmpty(list) || StringUtils.isBlank(idAttribute)
                || StringUtils.isBlank(parentIdAttribute)) {
            return Collections.emptyList();
        }
        Map<Integer, T> map = new LinkedHashMap<Integer, T>();
        for (T vo : list) {
            map.put((Integer) ConvertUtil.invokeGetMethod(vo, idAttribute), vo);
        }

        List<T> treeNodeList = Lists.newArrayList();
        T treeNode;
        T parentNode;
        for (Map.Entry<Integer, T> entry : map.entrySet()) {
            treeNode = entry.getValue();
            Integer pid = (Integer) ConvertUtil.invokeGetMethod(treeNode, parentIdAttribute);
            if (pid == null || pid.equals(SysConstans.ROOT_PARENT_ID)) {
                treeNodeList.add(treeNode);
            }else {
                parentNode = map.get(pid);
                if (parentNode != null) {
                    parentNode.getChildren().add(treeNode);
                }else {
                    treeNodeList.add(treeNode);
                }
            }
        }
        map.clear();
        return treeNodeList;
    }
}
