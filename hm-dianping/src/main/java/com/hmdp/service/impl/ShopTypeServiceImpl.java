package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * service's implement class
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryList() {
        String key = CACHE_SHOP_TYPE;

        String typeJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(typeJson)) {
            List<ShopType> shopTypeList = JSONUtil.toList(typeJson, ShopType.class);
            return Result.ok(shopTypeList);
        }

        List<ShopType> shopTypeList = query().orderByAsc("sort").list();
        if (shopTypeList == null) {
            return Result.fail("The shop's message doesn't exist.");
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopTypeList));
        return Result.ok(shopTypeList);
    }
}
