/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.utility;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * Beanユーティリティクラス
 *
 * @author negishi
 */
@Component
public class BeanUtility {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtility.class);

    /**
     * 指定オブジェクトの指定フィールドに値を設定する
     *
     * @param target 指定オブジェクト
     * @param fieldName 指定フィールド
     * @param value 設定値
     */
    public void setFieldValue(Object target, String fieldName, Object value) {
        // 第４引数=false（指定フィールドMulstでない）
        setFieldValue(target, fieldName, value, false);
    }

    /**
     * 指定オブジェクトの指定フィールドに値を設定する
     *
     * @param target 指定オブジェクト
     * @param fieldName 指定フィールド
     * @param value 設定値
     * @param fieldMust 指定フィールドが必ず指定オブジェクトに存在していないといけないかどうか...true:存在していないといけない
     */
    public void setFieldValue(Object target, String fieldName, Object value, boolean fieldMust) {
        BeanWrapper bean = new BeanWrapperImpl(target);
        // 書き込み可能プロパティの場合のみ、書き込み実施
        if (bean.isWritableProperty(fieldName)) {
            bean.setPropertyValue(fieldName, value);
        } else {
            // setFieldValueが実施できなかった場合の警告メッセージ
            String warningMessage = "BeanUtility#setFieldValueが正常に行えませんでした。指定した引数が問題ないかご確認ください。 [target]=" + target
                                    + " [fieldName]=" + fieldName + " [value]=" + value;
            if (fieldMust) {
                // targetに存在しないfieldNameが指定されいた場合は、例外を発生させ処理を落とす
                throw new IllegalArgumentException(warningMessage);
            }
            // 上記以外はデバッグログ出力
            LOGGER.debug(String.valueOf(bean));
        }
    }

    /**
     * 指定オブジェクトの指定フィールドの値を取得する
     *
     * @param target 指定オブジェクト
     * @param fieldName 指定フィールド
     * @return 指定されたフィールドの値
     */
    public Object getFieldValue(Object target, String fieldName) {
        // 第３引数=false（指定フィールドMustでない）
        return getFieldValue(target, fieldName, false);
    }

    /**
     * 指定オブジェクトの指定フィールドの値を取得する
     *
     * @param target 指定オブジェクト
     * @param fieldName 指定フィールド
     * @return 指定されたフィールドの値
     * @param fieldMust 指定フィールドが必ず指定オブジェクトに存在していないといけないかどうか...true:存在していないといけない
     */
    public Object getFieldValue(Object target, String fieldName, boolean fieldMust) {
        BeanWrapper bean = new BeanWrapperImpl(target);
        if (bean.isReadableProperty(fieldName)) {
            return bean.getPropertyValue(fieldName);
        } else {
            // setFieldValueが実施できなかった場合の警告メッセージ
            String warningMessage = "BeanUtility#getFieldValueが正常に行えませんでした。指定した引数が問題ないかご確認ください。 [target]=" + target
                                    + " [fieldName]=" + fieldName;
            if (fieldMust) {
                // targetに存在しないfieldNameが指定されいた場合は、例外を発生させ処理を落とす
                throw new IllegalArgumentException(warningMessage);
            }
            // 上記以外はデバッグログ出力
            LOGGER.debug(String.valueOf(bean));
            return null;
        }
    }

    /**
     * Bean項目をクリアする
     * @param clazz Beanクラス
     * @param targetModel クリア対象のモデル
     */
    public void clearBean(Class<?> clazz, Object targetModel) {
        clearBean(clazz, targetModel, null);
    }

    /**
     * Bean項目をクリアする
     * @param clazz Beanクラス
     * @param targetModel クリア対象のモデル
     * @param excludedFields クリア除外対象フィールド
     */
    public void clearBean(Class<?> clazz, Object targetModel, String[] excludedFields) {
        try {
            // クリア用Modelを生成
            Object targetModelForClear = clazz.getDeclaredConstructor().newInstance();

            // 除外対象フィールドがある場合
            if (excludedFields != null) {
                // BeanWrapper生成
                BeanWrapper fromBean = new BeanWrapperImpl(targetModel);
                BeanWrapper toBean = new BeanWrapperImpl(targetModelForClear);
                for (String field : excludedFields) {
                    // 書き込み可能かどうかチェック
                    // ※この判定で、toBeanオブジェクトに存在する項目のみ、セットできる
                    if (toBean.isWritableProperty(field)) {
                        // 後続のモデルクリア処理実行前に、
                        // 除外対象フィールドはあらかじめ値を詰めておく
                        toBean.setPropertyValue(field, fromBean.getPropertyValue(field));
                    }
                }
            }
            // モデルクリア実行
            BeanUtils.copyProperties(targetModel, targetModelForClear);
        } catch (BeansException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}