/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.stereotype.Component;

/**
 * アプリケーションコンテキストのユーティリティクラス
 * 作成日：2021/02/25
 *
 * SingletonコンポーネントのAutowiredより前にsetApplicationContextをCALLしたいがために
 * LoadTimeWeaverAwareをインプリメントする
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Component
public class ApplicationContextUtility implements ApplicationContextAware, LoadTimeWeaverAware {

    /**
     * アプリケーションコンテキスト
     */
    private static ApplicationContext context;

    /**
     * アプリケーションコンテキストにDIされるBeanの取得
     * （クラスタイプから取得）
     *
     * @param clazz
     * @param <T>
     * @return 対象Bean
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return context.getBean(clazz);
    }

    /**
     * アプリケーションコンテキストにDIされるBeanの取得
     * （Bean-Name＆クラスタイプから取得）
     *
     * @param clazz
     * @param <T>
     * @param beanName
     * @return 対象Bean
     */
    public static <T> T getBeanByName(String beanName, Class<T> clazz) throws BeansException {
        return context.getBean(beanName, clazz);
    }

    /**
     * アプリケーションコンテキストのセット
     * （ApplicationContextAwareをインプリメントするため）
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * ロード時ウィービングサポートのセット
     * （LoadTimeWeaverAwareをインプリメントするため）
     *
     * @param loadTimeWeaver
     */
    @Override
    public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
        // 特に処理不要
    }

}

