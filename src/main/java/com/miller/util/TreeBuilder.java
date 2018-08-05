package com.miller.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.miller.common.BaseTree;
import com.miller.constant.SysConstans;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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

    /**
     * 重写递归TODO 排序递归
     * @param list
     * @param idAttribute
     * @param parentIdAttribute
     * @param comparable
     * @param <T>
     * @return
     */
    public static <T> List<T> makeTreeListByRecursion(List<T> list, String idAttribute,
                                                      String parentIdAttribute,Comparable<T> comparable) {
        if (CollectionUtils.isEmpty(list) || StringUtils.isBlank(idAttribute)
                || StringUtils.isBlank(parentIdAttribute)) {
            return Collections.emptyList();
        }
        // pid: [child1,child2,child3]
        Multimap<Integer, T> listMap = ArrayListMultimap.create();
        // 根节点List
        List<T> rootList = Lists.newArrayList();
        for (T t : list) {
            Integer pid = (Integer) ConvertUtil.invokeGetMethod(t, parentIdAttribute);
            if (pid.equals(SysConstans.ROOT_PARENT_ID)) {
                rootList.add(t);
            }else {
                listMap.put(pid, t);
            }
        }
        //对rootList进行排序
        Collections.sort(rootList, (Comparator<? super T>) comparable);
        transformTree(rootList, idAttribute, listMap);
        listMap.clear();
        return rootList;
    }

    /**
     * 递归给根节点List放入子节点
     * @param rootList
     * @param idAttribute
     * @param listMap
     * @param <T>
     */
    public static  <T> void transformTree(List<T> rootList, String idAttribute
                                  ,Multimap<Integer, T> listMap) {
        for (T t : rootList) {
            Integer id = (Integer) ConvertUtil.invokeGetMethod(t, idAttribute);
            List<T> tempList = (List<T>) listMap.get(id);
            if (CollectionUtils.isNotEmpty(tempList)) {
                // 排序
                List<T> children = (List<T>) ConvertUtil.invokeGetMethod(t, "children");
                if (CollectionUtils.isEmpty(tempList)) {
                    // 给集合赋值
                    ConvertUtil.invokeSetMethod(t, "children", Lists.newArrayList());
                }
                children.addAll(tempList);
                // 设置下一层
                //进入到下一层去处理
                transformTree(tempList,idAttribute,listMap);
            }
        }
    }
}
