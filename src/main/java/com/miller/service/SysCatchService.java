package com.miller.service;

import com.miller.constant.CatchKeyConstants;

/**
 * Created by miller on 2018/8/5
 */
public interface SysCatchService {

    void saveCatche(String toSaveValue, int timeoutSeconds, CatchKeyConstants prefix);

    void saveCatche(String toSaveValue, int timeoutSeconds, CatchKeyConstants prefix, String... keys);

    String getFromCache(CatchKeyConstants prefix, String... keys);
}
