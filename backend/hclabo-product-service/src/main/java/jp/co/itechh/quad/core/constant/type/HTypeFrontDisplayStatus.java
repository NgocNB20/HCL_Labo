/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.seasar.doma.Domain;

import java.sql.Timestamp;

/**
 * フロント公開状態
 *
 * @author kimura
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeFrontDisplayStatus implements EnumType {

    /** 非表示 */
    NO_OPEN("非表示", "0"),

    /** 表示中 */
    OPEN("表示中", "1");

    /** doma用ファクトリメソッド */
    public static HTypeFrontDisplayStatus of(String value) {

        HTypeFrontDisplayStatus hType = EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * フロント表示状態の判定
     * <pre>
     * ・公開状態と公開期間から現在の表示状態を判定する
     * ・フロント表示基準日時が設定されている場合は、基準日時を現在として公開状態を判断
     * </pre>
     *
     * @param openStatus
     * @param openStartTime
     * @param openEndTime
     * @param frontDisplayReferenceDate（管理サイトからの場合未設定）
     * @return true = 表示
     */
    public static boolean isDisplay(HTypeOpenDeleteStatus openStatus,
                                    Timestamp openStartTime,
                                    Timestamp openEndTime,
                                    Timestamp frontDisplayReferenceDate) {

        // 公開状態が「公開中」の場合
        if (openStatus == HTypeOpenDeleteStatus.OPEN) {

            // 公開期間未設定の場合
            if (ObjectUtils.isEmpty(openStartTime) && ObjectUtils.isEmpty(openEndTime)) {
                return true;
            } else {

                // 日付関連Helper取得
                DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

                // フロント表示基準日時が設定されている場合
                if (ObjectUtils.isNotEmpty(frontDisplayReferenceDate)) {
                    return dateUtility.isOpen(openStartTime, openEndTime, frontDisplayReferenceDate);
                } else {
                    return dateUtility.isOpen(openStartTime, openEndTime);
                }
            }
        }
        // 公開状態が「非公開」または「削除」の場合
        return false;
    }

    /**
     * フロント表示状態の判定
     * <pre>
     * ・公開状態と公開期間から現在の表示状態を判定する
     * ・フロント表示基準日時が設定されている場合は、基準日時を現在として公開状態を判断
     * </pre>
     *
     * @param openStatus
     * @param openStartTime
     * @param openEndTime
     * @param frontDisplayReferenceDate（管理サイトからの場合未設定）
     * @return true = 表示
     */
    public static boolean isDisplay(HTypeOpenStatus openStatus,
                                    Timestamp openStartTime,
                                    Timestamp openEndTime,
                                    Timestamp frontDisplayReferenceDate) {

        // 公開状態が「公開中」の場合
        if (openStatus == HTypeOpenStatus.OPEN) {

            // 公開期間未設定の場合
            if (ObjectUtils.isEmpty(openStartTime) && ObjectUtils.isEmpty(openEndTime)) {
                return true;
            } else {

                // 日付関連Helper取得
                DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

                // フロント表示基準日時が設定されている場合
                if (ObjectUtils.isNotEmpty(frontDisplayReferenceDate)) {
                    return dateUtility.isOpen(openStartTime, openEndTime, frontDisplayReferenceDate);
                } else {
                    return dateUtility.isOpen(openStartTime, openEndTime);
                }
            }
        }
        // 公開状態が「非公開」または「削除」の場合
        return false;
    }

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}