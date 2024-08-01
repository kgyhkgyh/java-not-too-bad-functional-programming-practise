/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.fp.util;

import com.google.common.collect.Lists;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.SneakyThrows;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author 辰熠 zack
 * @version TryUtil.java, v 0.1 2023年09月11日 14:55 辰熠 zack
 */
public class TryUtil {
	
	/**
	 * 安全执行func函数，如果失败会使用consumer进行信息处理，最终返回Option<R>
	 * @param func
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R> Function1<T, Option<R>> objFnTry(Function1<T, R> func, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .toOption();
	}
	
	/**
	 * 非安全执行函数func，如果出现异常则抛出一个其他指定异常
	 * @param func
	 * @param e
	 * @return
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R> Function1<T, Option<R>> objFnTry(Function1<T, R> func, Exception e) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwException(e))
					   .toOption();
	}
	
	/**
	 * 非安全执行函数func，如果出现异常则抛出一个其他指定异常
	 * @param func
	 * @param eFunc
	 * @return
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R, E extends Exception> Function1<T, Option<R>> objFnTry(Function1<T, R> func, Function1<Throwable, E> eFunc) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwException(eFunc))
					   .toOption();
	}
	
	/**
	 * 安全执行返回list的func函数，如果失败会使用consumer进行信息处理，最终返回vavr的list结果Option<List<R>>
	 * @param func
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R> Function1<T, Option<io.vavr.collection.List<R>>> listFnTry(Function1<T, List<R>> func, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .filter(list -> list != null)
					   .orElse(Try.of(() -> Lists.newArrayList()))
					   .map(ListUtil::toVavrList)
					   .toOption();
	}
	
	/**
	 * 安全执行返回list的func函数，如果失败会使用consumer进行信息处理，最终返回vavr的list结果Option<List<R>>
	 * @param func
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R> Function1<T, Option<io.vavr.collection.List<R>>> vavrListFnTry(Function1<T, io.vavr.collection.List<R>> func, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .filter(list -> list != null && !list.isEmpty())
					   .toOption();
	}
	
	/**
	 * 非安全执行函数func，如果函数执行失败，则返回一个默认值。
	 * @param func
	 * @param defaultValue
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <R>
	 */
	public static <T, R> Function1<T, R> objFnTryOrDefault(Function1<T, R> func, R defaultValue, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .getOrElse(defaultValue);
	}
	
	/**
	 * 抛出指定异常
	 * @param e
	 * @return
	 * @param <T>
	 * @param <E>
	 */
	@SneakyThrows
	public static <T, E extends Exception> Consumer<T> throwException(E e) {
		return neverMind -> throwConsumer(e);
	}
	
	/**
	 * 抛出指定异常
	 * @param func
	 * @return
	 * @param <E>
	 */
	@SneakyThrows
	public static <E extends Exception> Consumer<Throwable> throwException(Function1<Throwable, E> func) {
		return t -> {
			E e = func.apply(t);
			throwConsumer(e);
		};
	}
	
	@SneakyThrows
	private static void throwConsumer(Exception e){
		throw e;
	}
	
	
}