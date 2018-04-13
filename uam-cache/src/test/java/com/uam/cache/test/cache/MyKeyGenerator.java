package com.uam.cache.test.cache;

import com.uam.cache.constants.CacheScope;
import com.uam.cache.parser.IKeyGenerator;
import com.uam.cache.parser.IUserKeyGenerator;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 * @date 2017-05-22 14:05
 */
public class MyKeyGenerator extends IKeyGenerator {
    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return null;
    }

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        return "myKey_"+arguments[0];
    }
}
