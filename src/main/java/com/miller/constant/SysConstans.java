package com.miller.constant;

/**
 * Created by miller on 2018/7/21
 */
public interface SysConstans {

    /**
     * 异常错误key
     */
    String EXCEPTION_MESSAGE_KEY = "msg";

    String EXCEPTION_CODE_KEY = "code";

    /**
     * ModelAndView 返回 json字符串的 viewname 是springmvc.xml中 <bean>转换器中的id
     */
    String JSON_VIEW_NAME = "jsonView";


    /**
     * 父节点的根节点
     */
    Integer ROOT_PARENT_ID = 0;

    /**
     * 有效
     */
    Integer STATUS_EFFECTIVE = 1;

    /**
     * 无效
     */
    Integer STATUS_INVALID = 0;
}
