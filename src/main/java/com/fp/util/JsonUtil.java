package com.fp.util;

import com.alibaba.fastjson.JSON;
import io.vavr.Function1;
import io.vavr.Function2;

import java.util.List;

/**
 * @author chen yi (zack)
 * @date 2022/3/28
 */
public class JsonUtil {

    /**
     * 创建jsonParse的偏函数
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Function1<String, T> jsonParseFn(Class<T> clazz) {
        Function2<String, Class<T>, T> parseObjectFn = Function2.of(JSON::parseObject);
        return parseObjectFn.reversed().curried().apply(clazz);
    }

    /**
     * 创建jsonParseArray的偏函数
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Function1<String, List<T>> jsonArrParseFn(Class<T> clazz) {
        Function2<String, Class<T>, List<T>> parseObjectFn = Function2.of(JSON::parseArray);
        return parseObjectFn.reversed().curried().apply(clazz);
    }

}
