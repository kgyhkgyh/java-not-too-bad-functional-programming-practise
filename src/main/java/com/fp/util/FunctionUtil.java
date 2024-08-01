package com.fp.util;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.lang3.BooleanUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author chen yi (zack)
 * @date 2021/12/6
 */
public class FunctionUtil {
    
    /**
     * 对Function2函数进行柯里化
     * @param fn
     * @param value
     * @param <T1>
     * @param <T2>
     * @param <R>
     * @return
     */
    public static <T1, T2, R> Function1<T2, R> curry(Function2<T1, T2, R> fn, T1 value) {
        return Function2.of(fn).curried().apply(value);
    }
    
    /**
     * 对Consumer2函数进行柯里化
     * @param consumer
     * @param t1
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> Consumer<T2> curryCn(BiConsumer<T1, T2> consumer, T1 t1) {
        return t2 -> consumer.accept(t1, t2);
    }

    /**
     * 对Function2函数进行反向柯里化
     * @param fn
     * @param value
     * @param <T1>
     * @param <T2>
     * @param <R>
     * @return
     */
    public static <T1, T2, R> Function1<T1, R> curryRvs(Function2<T1, T2, R> fn, T2 value) {
        return Function2.of(fn).reversed().curried().apply(value);
    }
    
    /**
     * 对Consumer2函数进行反向柯里化
     * @param consumer
     * @param t2
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> Consumer<T1> curryRvsCn(BiConsumer<T1, T2> consumer, T2 t2) {
        return t1 -> consumer.accept(t1, t2);
    }
    
    
    /**
     * 对Function3函数执行降维
     * @param fn
     * @param value
     * @param <T1>
     * @param <T2>
     * @param <R>
     * @return
     */
    public static <T1, T2, T3, R> Function2<T2, T3, R> apply(Function3<T1, T2, T3, R> fn, T1 value) {
        return Function3.of(fn).apply(value);
    }

    /**
     * 将返回boolean的Function转换为Predicate
     *
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> toPredicate(Function1<T, Boolean> fn) {
        return expression -> Try.of(() -> fn.apply(expression))
                                .toOption()
                                .getOrElse(false);
    }

    /**
     * 对断言进行反向逻辑
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return t -> !predicate.test(t);
    }

    /**
     * 对boolean函数进行反向逻辑
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> Function1<T, Boolean> not(Function<T, Boolean> fn) {
        return andThen(fn::apply, BooleanUtils::isFalse);
    }
    
    /**
     * 组合两个函数，形成管道调用
     * @param fn1
     * @param fn2
     * @param <T>
     * @param <U>
     * @param <R>
     * @return
     */
    public static <T, U, R> Function1<T, R> andThen(Function1<T, U> fn1, Function1<U, R> fn2) {
        return fn1.andThen(fn2);
    }
    
    /**
     * 组合两个函数，形成管道调用
     * @param fn1
     * @param fn2
     * @param <T>
     * @param <U>
     * @param <R>
     * @return
     */
    public static <T, U, R, W> Function1<T, W> andThen(Function1<T, U> fn1, Function1<U, R> fn2, Function1<R, W> fn3) {
        return fn1.andThen(fn2).andThen(fn3);
    }
    
    /**
     * 组合两个函数，形成管道调用
     * @param fn1
     * @param fn2
     * @param <T>
     * @param <U>
     * @param <R>
     * @return
     */
    public static <T, U, R> Function1<T, R> compose(Function1<U, R> fn1, Function1<T, U> fn2) {
        return fn1.compose(fn2);
    }
    
    /**
     * 组合两个Function1变成一个Function2
     * @param fn1
     * @param fn2
     * @param combineFn
     * @param <T1>
     * @param <T2>
     * @param <R1>
     * @param <R2>
     * @param <R>
     * @return
     */
    public static <T1, T2, R1, R2, R> Function2<T1, T2, Option<R>> combineFn(Function1<T1, R1> fn1, Function1<T2, R2> fn2, Function2<R1, R2, R> combineFn) {
        return (t1, t2) -> {
            R1 r1 = fn1.apply(t1);
            R2 r2 = fn2.apply(t2);
            return Option.of(combineFn.apply(r1, r2));
        };
    }
    
    /**
     * 抛弃函数的返回值，将函数变成consumer
     * @param fun
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Consumer<T> toConsumer(Function1<T, R> fun) {
        return fun::apply;
    }
    
    /**
     * 将BiConsumer变成Function2,用于函数式变换 todo
     * @param consumer
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> Function2<T1, T2, Void> toFunction(BiConsumer<T1, T2> consumer) {
        return (t1, t2) -> {
            consumer.accept(t1, t2);
            return null;
        };
    }
    
    /**
     * 执行Option的ap操作
     *
     * @param fnOp
     * @param fnOp
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Function1<T, Option<R>> optionAp(Option<Function1<T, R>> fnOp) {
        return t -> (Option<R>) fnOp.map(fn -> fn.apply(t)).flatMap(Option::of)
                                    .orElse(Option.none());
    }
    
    /**
     * 将一个对象完成consumer操作后返回
     * 示例：consumer(a, b) 等同于
     *      Option.of(a)
     *            .peek(doBiConsumer(b))
     * @param consumer
     * @param val
     * @return
     * @param <R>
     * @param <V>
     */
    public static <R, V> Consumer<R> doBiConsumer(BiConsumer<R, V> consumer, V val) {
        return (t) -> consumer.accept(t, val);
    }
    
}
