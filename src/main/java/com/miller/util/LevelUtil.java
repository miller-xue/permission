package com.miller.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by miller on 2018/7/22
 * @author Miller
 */
public class LevelUtil {
    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    /**
     * 根据父level 和父id 生成子level-
     * @param parentLevel
     * @param parentId
     * @return
     */
    public static String caculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        }
        return StringUtils.join(parentLevel, SEPARATOR, parentId);
    }
}
