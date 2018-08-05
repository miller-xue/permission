package com.miller.service.impl;

import com.google.common.base.Joiner;
import com.miller.common.RedisPool;
import com.miller.constant.CatchKeyConstants;
import com.miller.service.SysCatchService;
import com.miller.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * Created by miller on 2018/8/5
 * TODO 整合的略微不好
 */
@Service
@Slf4j
public class SysCatchServiceImpl implements SysCatchService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;


    @Override
    public void saveCatche(String toSaveValue, int timeoutSeconds, CatchKeyConstants prefix) {
        saveCatche(toSaveValue, timeoutSeconds, prefix, null);
    }

    @Override
    public void saveCatche(String toSaveValue, int timeoutSeconds, CatchKeyConstants prefix, String... keys) {
        if (toSaveValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String catchKey = generateCatchKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(catchKey, timeoutSeconds, toSaveValue);
        } catch (Exception e) {
            log.error("save catch exception, prefix:{}, keys:{}", prefix, JsonMapper.obj2String(keys));

        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public String getFromCache(CatchKeyConstants prefix, String... keys) {
        String catchKey = generateCatchKey(prefix, keys);
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(catchKey);
        } catch (Exception e) {
            log.error("get catch exception, prefix:{}, keys:{}", prefix, JsonMapper.obj2String(keys), e);
            return null;
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    private String generateCatchKey(CatchKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);

        }
        return key;
    }
}
