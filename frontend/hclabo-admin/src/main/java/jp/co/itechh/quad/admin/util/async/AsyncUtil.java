/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.util.async;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.utility.AsyncUtility;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

/**
 * 非同期処理を起動させるユーティリティクラス
 *
 */
public class AsyncUtil {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtil.class);

    /**
     * SCOPED_TARGET_NAME_PREFIX
     */
    private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";

    /**
     * 非同期処理を起動させるユーティリティクラス
     *
     * @param execClazz  Execクラス
     * @param methodName  実行するメソッド名
     * @param args  メソッドの引数
     * @param argsClass  メソッドの引数のクラス
     */
    public static void asyncService(Class<?> execClazz, String methodName, Object[] args, Class<?>[] argsClass) {
        try {
            Object execInstance;
            ConfigurableListableBeanFactory beanFactory =
                            ((AbstractApplicationContext) ApplicationContextUtility.getApplicationContext()).getBeanFactory();

            String[] sessionScopeComponentList = beanFactory.getBeanNamesForAnnotation(SessionScope.class);
            Optional<String> beanName = Arrays.stream(sessionScopeComponentList)
                                              .filter(sessionScopeComponent -> sessionScopeComponent.equalsIgnoreCase(
                                                              SCOPED_TARGET_NAME_PREFIX + execClazz.getSimpleName()))
                                              .findFirst();
            if (beanName.isPresent()) {
                execInstance = copyObject(ApplicationContextUtility.getBeanByName(beanName.get(), execClazz));
            } else {
                execInstance = ApplicationContextUtility.getBean(execClazz);
            }

            AsyncUtility asyncUtility = ApplicationContextUtility.getBean(AsyncUtility.class);
            asyncUtility.asyncExecute(execInstance, methodName, args, argsClass);
        } catch (Exception e) {
            LOGGER.error("非同期処理を行うインスタンスの生成に失敗しました。", e);
        }
    }

    /**
     * 新しくインスタンスを生成し、コピー
     *
     * @param inputObject コピー元オブジェクト
     * @return インスタンス化されたオブジェクト
     */
    private static Object copyObject(Object inputObject)
                    throws InvocationTargetException, InstantiationException, IllegalAccessException,
                    NoSuchMethodException {
        try {
            Object copyObject = inputObject.getClass().getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(copyObject, inputObject);
            return copyObject;
        } catch (Exception e) {
            LOGGER.error("非同期処理が開始できません。実行するインスタンスのコピーに失敗しました。");
            throw e;
        }
    }

}