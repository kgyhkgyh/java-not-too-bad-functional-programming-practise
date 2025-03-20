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
	 * ==================================================================================================
	 *
	 *      ##      ##       ###       ########     ######     ##     ##          ##     ##    ########
	 *      ##      ##      ## ##         ##       ##    ##    ##     ##          ###   ###    ##
	 *      ##  ##  ##     ##   ##        ##       ##          ##     ##          #### ####    ##
	 *      ##  ##  ##    ##     ##       ##       ##          #########          ## ### ##    ######
	 *      ##  ##  ##    #########       ##       ##          ##     ##          ##     ##    ##
	 *      ##  ##  ##    ##     ##       ##       ##    ##    ##     ##          ##     ##    ##
	 *       ###  ###     ##     ##       ##        ######     ##     ##          ##     ##    ########
	 *
	 * ==================================================================================================
	 * 工具类说明：本工具类提供了一些Tuple处理的常用工具方法，帮助函数式变成将横向链路编程，编程纵向，旨在提高代码可读性。
	 * Demo:
	 * Option.of(t)
	 * 		 .map(toTuple(t ->  doSomeThing1(t), t -> doSomeThing2(t))
	 * 		 .map(tupleMap(t1 -> doMoreThing1(t1)), t2 -> doMoreThing2(t2))
	 * 		 .map(tupleMap((t1, t2) -> doFinalThing(t1, t2)))
	 *
	 * 这段代码的意义：
	 *      			   t
	 *        		/   		  \
	 *     doSomeThing1		   doSomeThing2
	 *        /	   					\
	 *   	t1   					t2
	 *   	 |						|
	 *    doMoreThing1   		doMoreThing2
	 *   	 |						|
	 *       t1'  				   t2'
	 *         \  				   /
	 *     		    doFinalThing
	 *
	 * 这样，便将本来横向的代码执行，变成了类似于链路图的纵向执行，便于阅读。
	 * ==================================================================================================
	 */
	
	/**
	 * 构建被Option保护的安全{@link Tuple2}
	 * @param t1
	 * @param t2
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2> Tuple2<Option<T1>, Option<T2>> toTuple(T1 t1, T2 t2) {
		return Tuple.of(Option.of(t1), Option.of(t2));
	}
	
	/**
	 * 构建被Option保护的安全{@link Tuple3}, 并使用{@link  Option}进行保护
	 * @param t1
	 * @param t2
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2, T3> Tuple3<Option<T1>, Option<T2>, Option<T3>> toTuple(T1 t1, T2 t2, T3 t3) {
		return Tuple.of(Option.of(t1), Option.of(t2), Option.of(t3));
	}
	
	/**
	 * 构建普通非安全的{@link Tuple2}
	 * @param t1
	 * @param t2
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2> Tuple2<T1, T2> toUnsafeTuple(T1 t1, T2 t2) {
		return Tuple.of(t1, t2);
	}
	
	/**
	 * 构建普通非安全的{@link Tuple3}
	 * @param t1
	 * @param t2
	 * @param t3
	 * @return
	 * @param <T1>
	 * @param <T2>
	 * @param <T3>
	 */
	public static <T1, T2, T3> Tuple3<T1, T2, T3> toUnsafeTuple(T1 t1, T2 t2, T3 t3) {
		return Tuple.of(t1, t2, t3);
	}
	
	/**
	 * 构建被Option保护的安全{@link Tuple3}, 并使用{@link  Option}进行保护
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
	 * 将一个值分拆为{@link Tuple3}，并使用{@link  Option}进行保护
	 * @param fn1
	 * @param fn2
	 * @param fn3
	 * @return
	 * @param <T>
	 * @param <R1>
	 * @param <R2>
	 * @param <R3>
	 */
	public static <T, R1, R2, R3> Function<T, Tuple3<Option<R1>, Option<R2>, Option<R3>>> toTuple(Function1<T, R1> fn1, Function1<T, R2> fn2, Function1<T, R3> fn3) {
		return  t -> {
			Option<R1> op1 = Option.of(t).flatMap(Function1.lift(fn1));
			Option<R2> op2 = Option.of(t).flatMap(Function1.lift(fn2));
			Option<R3> op3 = Option.of(t).flatMap(Function1.lift(fn3));
			return Tuple.of(op1, op2, op3);
		};
	}
	
	/**
	 * 将一个值分拆为{@link Tuple2}
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
	 * 检查{@link Tuple2}中的所有元素是否都是非空的
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2> Predicate<Tuple2<Option<T1>, Option<T2>>> allSafe() {
		return tuple -> tuple._1.isDefined() && tuple._2.isDefined();
	}
	
	/**
	 * 检查Tuple3中的所有元素是否都是非空的
	 * @return
	 * @param <T1>
	 * @param <T2>
	 */
	public static <T1, T2, T3> Predicate<Tuple3<Option<T1>, Option<T2>, Option<T3>>> all3Safe() {
		return tuple -> tuple._1.isDefined() && tuple._2.isDefined() && tuple._3.isDefined();
	}
	
	/**
	 * 对tuple进行左右映射，不做任何的安全性校验与保护
	 * @param leftFn
	 * @param rightFn
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, R1, R2> Function1<Tuple2<T1, T2>, Tuple2<R1, R2>> unSafeTupleMap(Function1<T1, R1> leftFn, Function1<T2, R2> rightFn) {
		return  tuple -> Tuple.of(leftFn.apply(tuple._1), rightFn.apply(tuple._2));
	}
	
	/**
	 * 对tuple3进行映射，不做任何的安全性校验与保护
	 * @param leftFn
	 * @param rightFn
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, T3, R1, R2, R3> Function1<Tuple3<T1, T2, T3>, Tuple3<R1, R2, R3>> unSafeTupleMap(Function1<T1, R1> leftFn, Function1<T2, R2> midFn, Function1<T3, R3> rightFn) {
		return  tuple -> {
			R1 r1 = leftFn.apply(tuple._1);
			R2 r2 = midFn.apply(tuple._2);
			R3 r3 = rightFn.apply(tuple._3);
			return Tuple.of(r1, r2, r3);
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
	public static <T1, T2, R1, R2> Function1<Tuple2<Option<T1>, Option<T2>>, Tuple2<Option<R1>, Option<R2>>> tupleMap(Function1<T1, R1> leftFn, Function1<T2, R2> rightFn) {
		return  tuple -> {
			Option<R1> leftOp = tuple._1.flatMap(Function1.lift(leftFn));
			Option<R2> rightOp = tuple._2.flatMap(Function1.lift(rightFn));
			return Tuple.of(leftOp, rightOp);
		};
	}
	
	/**
	 * 对tuple进行左右映射，并使用option进行保护
	 * @param fn1
	 * @param fn3
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, T3, R1, R2, R3> Function1<Tuple3<Option<T1>, Option<T2>, Option<T3>>, Tuple3<Option<R1>, Option<R2>, Option<R3>>> tupleMap(Function1<T1, R1> fn1, Function1<T2, R2> fn2, Function1<T3, R3> fn3) {
		return  tuple -> {
			Option<R1> op1 = tuple._1.flatMap(Function1.lift(fn1));
			Option<R2> op2 = tuple._2.flatMap(Function1.lift(fn2));
			Option<R3> op3 = tuple._3.flatMap(Function1.lift(fn3));
			return Tuple.of(op1, op2, op3);
		};
	}
	
	/**
	 * 将非完全的tuple进行左右映射，并使用option进行保护
	 * @param leftFn
	 * @param rightFn
	 * @param <R1>
	 * @param <R2>
	 * @return
	 */
	public static <T1, T2, R1, R2> Function1<Tuple2<T1, T2>, Tuple2<Option<R1>, Option<R2>>> unsafeToSafeTupleMap(Function1<T1, R1> leftFn, Function1<T2, R2> rightFn) {
		return  tuple -> {
			Option<R1> leftOp = Option.of(tuple._1).flatMap(Function1.lift(leftFn));
			Option<R2> rightOp = Option.of(tuple._2).flatMap(Function1.lift(rightFn));
			return Tuple.of(leftOp, rightOp);
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
	public static <T1, T2, R> Function1<Tuple2<Option<T1>, Option<T2>>, Option<R>> tupleMerge(Function2<T1, T2, R> fn) {
		return tuple -> Option.of(tuple)
							  .filter(allSafe())
							  .flatMap(t -> Function2.lift(fn).apply(t._1.get(), t._2.get()));
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
	public static <T1, T2, T3, R> Function1<Tuple3<Option<T1>, Option<T2>, Option<T3>>, Option<R>> tupleMerge(Function3<T1, T2, T3, R> fn) {
		return tuple -> Option.of(tuple)
							  .filter(all3Safe())
							  .flatMap(t -> Function3.lift(fn).apply(t._1.get(), t._2.get(), t._3.get()));
	}
	
	/**
	 * 将普通的Function2函数变成Tuple2入参的函数，不进行任何安全性校验
	 * @param fn
	 * @return
	 * @param <T1>
	 * @param <T2>
	 * @param <R>
	 */
	public static <T1, T2, R> Function1<Tuple2<T1, T2>, R> unsafeTupleMerge(Function2<T1, T2, R> fn) {
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
	public static <T1, T2, T3, R> Function1<Tuple3<T1, T2, T3>, R> unsafeTupleMerge(Function3<T1, T2, T3, R> fn) {
		return tuple -> fn.apply(tuple._1, tuple._2, tuple._3);
	}

}
