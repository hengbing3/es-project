package com.christer.project.util;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-09-04 22:10
 * Description:
 * 函数式接口，拷贝数据回调接口
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {

    /**
     * 定义默认回调方法
     * @param t
     * @param s
     */
    void callBack(S t, T s);
}