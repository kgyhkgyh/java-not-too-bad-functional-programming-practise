package com.kgyhkgyh.fp.util;

import io.vavr.Function1;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;



/**
 * @author kgyhkgyh
 * @version ValidationUtil.java, v 0.1 2023年09月18日 17:02 kgyhkgyh
 */
public class ValidationUtil {
	
	/**
	 * 初始化Validation，并指定错误类型
	 * @param t
	 * @param eClass
	 * @return
	 * @param <E>
	 * @param <T>
	 */
	public static <E, T> Validation<E, T> init(T t, Class<E> eClass) {
		return Validation.valid(t);
	}
	
	
	/**
	 * 进行Validation的映射处理，如果失败则返回Validation<E>，并可以对错误进行处理
	 * @param func
	 * @param e
	 * @return
	 * @param <T>
	 * @param <R>
	 * @param <E>
	 */
	public static <T, R, E> Function1<T, Validation<E, R>> validMap(Function1<T, R> func, E e, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .toValidation()
					   .mapError(neverMind -> e);
	}
	
	/**
	 * 进行Validation的映射处理，如果失败则返回Validation<E>
	 * @param func
	 * @param e
	 * @return
	 * @param <T>
	 * @param <E>
	 */
	public static <T, E> Function1<T, Validation<E, T>> validMap(Consumer<T> func, E e, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .peek(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .toValidation()
					   .mapError(neverMind -> e);
	}
	
	/**
	 * 进行Validation的映射处理，如果失败不会返回错误，并对错误进行处理
	 * @param func
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <E>
	 */
	public static <T, E> Function1<T, Validation<E, T>> validMap(Consumer<T> func, BiConsumer<T, Throwable> cn) {
		return t -> {
			Try.success(t)
			   .peek(func)
			   .onFailure(throwable -> cn.accept(t, throwable));
			return Validation.valid(t);
		};
		
	}
	
	/**
	 * 进行Validation的映射处理，如果失败会返回错误，并对错误进行处理
	 * @param func
	 * @param eFunc
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <R>
	 * @param <E>
	 */
	public static <T, R, E> Function1<T, Validation<E, R>> validMap(Function1<T, R> func, Function1<Throwable, E> eFunc, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .map(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .toValidation()
					   .mapError(eFunc);
	}
	
	/**
	 * 进行Validation的映射处理，如果失败会返回错误，并对错误进行处理
	 * @param func
	 * @param eFunc
	 * @param cn
	 * @return
	 * @param <T>
	 * @param <E>
	 */
	public static <T, E> Function1<T, Validation<E, T>> validMap(Consumer<T> func, Function1<Throwable, E> eFunc, BiConsumer<T, Throwable> cn) {
		return t -> Try.success(t)
					   .peek(func)
					   .onFailure(throwable -> cn.accept(t, throwable))
					   .toValidation()
					   .mapError(eFunc);
	}
	
}