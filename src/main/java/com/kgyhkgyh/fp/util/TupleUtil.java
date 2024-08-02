package com.kgyhkgyh.fp.util;

import io.vavr.*;
import io.vavr.control.Option;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author kgyhkgyh
 * @version TupleUtil.java, v 0.1 2023年09月14日 16:18 kgyhkgyh
 */
public class TupleUtil {
	
	/**
	 * 构建被Option保护的安全Tuple
	 * @param t1
	 * @param t2
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2> Tuple2<Option<T1>, Option<T2>> safeTuple(T1 t1, T2 t2) {
		return Tuple.of(Option.of(t1), Option.of(t2));
	}
	
	/**
	 * 检查Tuple2中的所有元素是否都是非空的
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2> Predicate<Tuple2<Option<T1>, Option<T2>>> allSafe() {
		return tuple -> tuple._1.isDefined() && tuple._2.isDefined();
	}
	
	/**
	 * 将一个值分拆为{@link Tuple2}，并使用{@link  Option}进行保护
	 * 左右函数本身具备Option保护能力
	 * @param leftFn
	 * @param rightFn
	 * @param <T>
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T, R1, R2> Function<T, Tuple2<Option<R1>, Option<R2>>> toTupleFnSafe(Function1<T, Option<R1>> leftFn, Function1<T, Option<R2>> rightFn) {
		return  t -> {
			Option<R1> leftOp = Option.of(t).flatMap(leftFn);
			Option<R2> rightOp = Option.of(t).flatMap(rightFn);
			return Tuple.of(leftOp, rightOp);
		};
	}
	
	/**
	 * 将一个值分拆为{@link Tuple2}，并使用{@link  Option}进行保护
	 * @param leftFn
	 * @param rightFn
	 * @param <T>
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T, R1, R2> Function<T, Tuple2<Option<R1>, Option<R2>>> toTuple(Function1<T, R1> leftFn, Function1<T, R2> rightFn) {
		return  t -> {
			Option<R1> leftOp = Option.of(t).flatMap(Function1.lift(leftFn));
			Option<R2> rightOp = Option.of(t).flatMap(Function1.lift(rightFn));
			return Tuple.of(leftOp, rightOp);
		};
	}
	
	/**
	 * 将一个值分拆为{@link Tuple2}，并使用{@link  Option}进行保护
	 * @param leftFn
	 * @param rightFn
	 * @param <T>
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T, R1, R2> Function<T, Tuple2<R1, R2>> toUnsafeTuple(Function1<T, R1> leftFn, Function1<T, R2> rightFn) {
		return  t -> Tuple.of(leftFn.apply(t), rightFn.apply(t));
	}
	
	/**
	 * 对tuple进行左右映射，并使用option进行保护
	 * @param leftFn
	 * @param rightFn
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, R1, R2> Function1<Tuple2<Option<T1>, Option<T2>>, Tuple2<Option<R1>, Option<R2>>> tupleSepMap(Function1<T1, R1> leftFn, Function1<T2, R2> rightFn) {
		return  tuple -> {
			Option<R1> leftOp = tuple._1.flatMap(Function1.lift(leftFn));
			Option<R2> rightOp = tuple._2.flatMap(Function1.lift(rightFn));
			return Tuple.of(leftOp, rightOp);
		};
	}
	
	/**
	 * 对tuple进行左右映射，并使用option进行保护
	 * @param leftFn
	 * @param rightFn
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, R1, R2> Function1<Tuple2<T1, T2>, Tuple2<R1, R2>> unsafeTupleSepMap(Function1<T1, R1> leftFn, Function1<T2, R2> rightFn) {
		return  tuple -> {
			R1 r1 = leftFn.apply(tuple._1);
			R2 r2 = rightFn.apply(tuple._2);
			return Tuple.of(r1, r2);
		};
	}
	
	/**
	 * 将普通的function2函数变成tuple输入，对执行结果进行安全性校验
	 * 执行入参为Tuple<Option<T1>, Option<T2>>的执行函数
	 * @param fn
	 * @param <T1>
	 * @param <T2>
	 * @param <R>
	 * @return
	 */
	public static <T1, T2, R> Function1<Tuple2<Option<T1>, Option<T2>>, Option<R>> toTupleFn(Function2<T1, T2, R> fn) {
		return tuple -> Option.of(tuple)
							  .filter(allSafe())
							  .flatMap(t -> Function2.lift(fn).apply(t._1.get(), t._2.get()));
	}
	
	/**
	 * 将普通的Function2函数变成Tuple2入参的函数，不进行任何安全性校验
	 * @param fn
	 * @return
	 * @param <T1>
	 * @param <T2>
	 * @param <R>
	 */
	public static <T1, T2, R> Function1<Tuple2<T1, T2>, R> toUnsafeTupleFn(Function2<T1, T2, R> fn) {
		return tuple -> fn.apply(tuple._1, tuple._2);
	}
	
	/**
	 * 将普通的Function3函数变成Tuple3入参的函数，不进行任何安全性校验
	 * @param fn
	 * @return
	 * @param <T1>
	 * @param <T2>
	 * @param <R>
	 */
	public static <T1, T2, T3, R> Function1<Tuple3<T1, T2, T3>, R> toUnsafeTupleFn(Function3<T1, T2, T3, R> fn) {
		return tuple -> fn.apply(tuple._1, tuple._2, tuple._3);
	}
	
	/**
	 * 将一个值分拆为{@link Tuple3},不进行任何保护
	 * @param fn1
	 * @param fn2
	 * @param fn3
	 * @return
	 * @param <T>
	 * @param <R1>
	 * @param <R2>
	 * @param <R3>
	 */
	public static <T, R1, R2, R3> Function<T, Tuple3<R1, R2, R3>> toUnsafeTuple(Function1<T, R1> fn1, Function1<T, R2> fn2, Function1<T, R3> fn3) {
		return  t -> Tuple.of(fn1.apply(t), fn2.apply(t), fn3.apply(t));
	}
	
	/**
	 * 对tuple进行左右映射，并使用option进行保护
	 * @param leftFn
	 * @param rightFn
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, T3, R1, R2, R3> Function1<Tuple3<T1, T2, T3>, Tuple3<R1, R2, R3>> unsafeTupleSepMap(Function1<T1, R1> leftFn, Function1<T2, R2> midFn, Function1<T3, R3> rightFn) {
		return  tuple -> {
			R1 r1 = leftFn.apply(tuple._1);
			R2 r2 = midFn.apply(tuple._2);
			R3 r3 = rightFn.apply(tuple._3);
			return Tuple.of(r1, r2, r3);
		};
	}

}