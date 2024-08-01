/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.fp.util;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * @author 辰熠 zack
 * @version ListUtil.java, v 0.1 2023年09月06日 15:08 辰熠 zack
 */
public class ListUtil {
	
	/**
	 * 将java list转换为vavr list
	 *
	 * @param list
	 * @return
	 */
	public static <T> List<T> toVavrList(java.util.List<T> list) {
		return List.ofAll(list);
	}
	
	/**
	 * 将vavr list转换为java list
	 *
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> java.util.List<T> toJavaList(List<T> list) {
		return list.toJavaList();
	}
	
	/**
	 * 组合list，并返回一个tuple来进行后续处理
	 *
	 * @param list1
	 * @param list2
	 * @param list3
	 * @param <T1>
	 * @param <T2>
	 * @param <T3>
	 * @return
	 */
	public static <T1, T2, T3> List<Tuple3<T1, T2, T3>> zipAll(java.util.List<T1> list1, java.util.List<T2> list2, java.util.List<T3> list3) {
		return toVavrList(list1).zipWithIndex()
								.map(tuple -> {
									Integer index = tuple._2;
									return Tuple.of(tuple._1, list2.size() > index ? list2.get(index) : null, list3.size() > index ? list3.get(index) : null);
								});
	}
	
	/**
	 * 组合list，并返回一个tuple来进行后续处理
	 *
	 * @param list1
	 * @param list2
	 * @param <T1>
	 * @param <T2>
	 * @return
	 */
	public static <T1, T2> List<Tuple2<T1, T2>> zipAll(java.util.List<T1> list1, java.util.List<T2> list2) {
		return toVavrList(list1).zipWithIndex()
								.map(tuple -> {
									Integer index = tuple._2;
									return Tuple.of(tuple._1, list2.size() > index ? list2.get(index) : null);
								});
	}
	
	/**
	 * 创建空list
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> empty() {
		return List.empty();
	}
	
	/**
	 * 创建空list
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> java.util.List<T> javaEmpty() {
		return new ArrayList<>();
	}
	
	/**
	 * 进行列表映射，使用vavr的list对象
	 *
	 * @param func
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> Function1<List<T>, List<R>> listMap(Function1<T, R> func) {
		return list -> list.map(func);
	}
	
	/**
	 * 进行列表映射，使用java的list对象
	 *
	 * @param func
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> Function1<java.util.List<T>, java.util.List<R>> javaListMap(Function1<T, R> func) {
		return list -> List.ofAll(list)
						   .map(func)
						   .toJavaList();
	}
	
	/**
	 * 进行列表映射，使用java的list对象
	 *
	 * @param predicate
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T> Function1<java.util.List<T>, java.util.List<T>> javaListFilter(Predicate<T> predicate) {
		return list -> List.ofAll(list)
						   .filter(predicate)
						   .toJavaList();
	}
	
	/**
	 * 按照index类型进行排序
	 *
	 * @param list
	 * @param indexList
	 * @param indexFn
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> List<T> sort(List<T> list, List<R> indexList, Function1<T, R> indexFn) {
		return indexList.map(id -> list.filter(inter -> indexFn.apply(inter).equals(id))
									   .headOption()
									   .getOrNull());
	}
	
	/**
	 * 按照index类型进行排序
	 *
	 * @param list
	 * @param indexList
	 * @param indexFn
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> List<T> sort(List<T> list, List<R> indexList, Function1<T, R> indexFn, T defaultValue) {
		return indexList.map(id -> list.filter(inter -> indexFn.apply(inter).equals(id))
									   .headOption()
									   .getOrElse(defaultValue));
	}
	
	/**
	 * 按照index类型进行排序
	 *
	 * @param list
	 * @param indexList
	 * @param indexFn
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> java.util.List<T> sort(java.util.List<T> list, java.util.List<R> indexList, Function1<T, R> indexFn) {
		return sort(toVavrList(list), toVavrList(indexList), indexFn).toJavaList();
	}
	
	/**
	 * 按照index类型进行排序
	 *
	 * @param list
	 * @param indexList
	 * @param indexFn
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> java.util.List<T> sort(java.util.List<T> list, java.util.List<R> indexList, Function1<T, R> indexFn, T defaultValue) {
		return sort(toVavrList(list), toVavrList(indexList), indexFn, defaultValue).toJavaList();
	}
	
	public static <T> Function1<java.util.List<T>, Option<T>> head() {
		return list -> List.ofAll(list).headOption();
	}
	
}