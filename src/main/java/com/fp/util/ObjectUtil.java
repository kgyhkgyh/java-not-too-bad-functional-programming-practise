package com.fp.util;

import com.google.common.base.Objects;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.BeanUtils;

import java.util.function.*;

import static com.fp.util.FunctionUtil.not;
import static com.fp.util.FunctionUtil.toPredicate;


/**
 * @author chen yi (zack)
 * @date 2022/3/28
 */
public class ObjectUtil {

    /**
     * 创建Object.equals的偏函数
     *
     * @param obj
     * @return
     */
    public static Predicate<Object> equalsFn(Object obj) {
        return Option.of(obj)
                     .map(Function2.of(Objects::equal).curried())
                     .map(FunctionUtil::toPredicate)
                     .getOrElse(other -> false);
    }

    /**
     * 创建Object.equals的偏函数，对传入值进行提前处理
     *
     * @param obj
     * @return
     */
    public static <T, R> Predicate<T> equalsFn(R obj, Function<T, R> fun) {
        Function1<T, Boolean> f = t -> Option.of(t)
                                             .map(fun)
                                             .map(r -> Objects.equal(r, obj))
                                             .getOrElse(false);

        return toPredicate(f);
    }


    /**
     * 将source拷贝到target的Function
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T,R> Function1<T,R> copyFn(Class<R> clazz) {
        Function2<T, Class<R>, R> function2 = Function2.of(ObjectUtil::copyAndReturn);
        return function2.reversed().curried().apply(clazz);
    }

    /**
     *
     * @param source
     * @param targetClazz
     * @param <T>
     * @param <R>
     * @return
     */
    private static <T,R> R copyAndReturn(T source, Class<R> targetClazz){
        R r = Try.of(targetClazz::newInstance)
                 .getOrElseThrow((Supplier<RuntimeException>) RuntimeException::new);
        BeanUtils.copyProperties(source, r);
        return r;
    }

    /**
     * 将一个对象完成consumer操作后返回，实例：
     * Option.of(object)
     *       .map(setProp(Obj::setValue, value))
     * 等同于
     * object.setValue(value)
     * @param consumer
     * @param value
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> Function<T, T> setProp(BiConsumer<T, U> consumer, U value) {
        return t -> {
            try {
                Option.of(value)
                      .peek(v -> consumer.accept(t, v));
            } catch (Throwable throwable) {
                throw throwable;
            }
            return t;
        };
    }
    
    /**
     * 将一个对象完成consumer操作后返回，实例：
     * Option.of(object)
     *       .map(setProp(Obj::setValue, value))
     * 等同于
     * object.setValue(value)
     * @param consumer
     * @param value
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> Function<T, T> setProp(BiConsumer<T, U> consumer, Option<U> value) {
        return t -> {
            try {
                value.peek(v -> consumer.accept(t, v));
            } catch (Throwable throwable) {
                throw throwable;
            }
            return t;
        };
    }

    /**
     * 将一个对象完成consumer操作后返回，实例：
     * Option.of(value)
     *       .peek(doConsumeFn(Obj::setValue, obj))
     * 等同于
     * obj.setValue(value)
     * @param consumer
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> Consumer<U> setObjProp(T obj, BiConsumer<T, U> consumer) {
        return value -> {
            try {
                consumer.accept(obj, value);
            } catch (Throwable throwable) {
                throw throwable;
            }
        };
    }
    
    /**
     * 获取一个对象的属性，返回对应的Option
     * @param obj
     * @param mapper
     * @return
     * @param <T>
     * @param <U>
     */
    public static <T, U> Option<U> getProp(T obj, Function<T, U> mapper) {
        return Option.of(obj).map(mapper);
    }
    
    /**
     * 校验predicate，如果不通过则会执行后续的consumer进行处理
     * @param predicate
     * @param consumer
     * @return
     * @param <T>
     */
    public static <T> Predicate<T> check(Predicate<T> predicate, Consumer<T> consumer) {
        return t -> {
            Option.of(t)
                  .filter(not(predicate))
                  .peek(consumer);
            return predicate.test(t);
        };
    }
    
    /**
     * 对象自同态
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T identity(T t) {
        return t;
    }

}
