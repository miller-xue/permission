package com.miller.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miller on 2018/8/2
 */
public class StringUtil {
    /**
     * 字符串切分成List<Integer>
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str) {
        // 把字符串切分成list数组
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
