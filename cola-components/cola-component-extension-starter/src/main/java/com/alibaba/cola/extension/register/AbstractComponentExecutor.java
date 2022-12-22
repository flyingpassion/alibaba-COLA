package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fulan.zjf
 * @date 2017/12/21
 */
public abstract class AbstractComponentExecutor {

    /**
     * Multiple Execute extension with Response
     *
     * @param targetClz
     * @param bizScenario
     * @param exeFunction
     * @param <R>         Response Type
     * @param <T>         Parameter Type
     * @return
     */
    public <R, T> List<R> executeMultiple(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        List<T> componentMultiple = locateComponentMultiple(targetClz, bizScenario);
        return componentMultiple.stream().map(exeFunction::apply).collect(Collectors.toList());
    }

    /**
     * Execute extension with Response
     *
     * @param targetClz
     * @param bizScenario
     * @param exeFunction
     * @param <R>
     * @param <T>
     * @return
     */
    public <R, T> R execute(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        T component = locateComponent(targetClz, bizScenario);
        return exeFunction.apply(component);
    }

    public <R, T> R execute(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction) {
        return execute((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    /**
     * Multiple Execute extension without Response
     *
     * @param targetClz
     * @param context
     * @param exeFunction
     * @param <T>
     */
    public <T> void executeVoidMultiple(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
        List<T> componentMultiple = locateComponentMultiple(targetClz, context);
        componentMultiple.forEach(exeFunction);
    }

    /**
     * Execute extension without Response
     *
     * @param targetClz
     * @param context
     * @param exeFunction
     * @param <T>         Parameter Type
     */
    public <T> void executeVoid(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
        T component = locateComponent(targetClz, context);
        exeFunction.accept(component);
    }

    public <T> void executeVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction) {
        executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    /**
     * 获取任意一个组件
     *
     * @param targetClz
     * @param context
     * @param <C>
     * @return
     */
    protected abstract <C> C locateComponent(Class<C> targetClz, BizScenario context);

    /**
     * 获取多个组件
     *
     * @param targetClz
     * @param context
     * @param <C>
     * @return
     */
    protected abstract <C> List<C> locateComponentMultiple(Class<C> targetClz, BizScenario context);
}
