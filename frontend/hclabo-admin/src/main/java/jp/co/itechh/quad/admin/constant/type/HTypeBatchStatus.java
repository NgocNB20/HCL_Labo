/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バッチタスク状態：列挙型
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeBatchStatus implements EnumType {

    /** 正常終了（旧HIT-MALLでは「NORMAL_TERMINATION」） */
    COMPLETED("正常終了", "COMPLETED"),
    /** 異常終了（旧HIT-MALLでは「ABNORMAL_TERMINATION」） */
    FAILED("異常終了", "FAILED");

    /** doma用ファクトリメソッド */
    public static HTypeBatchStatus of(String value) {

        HTypeBatchStatus hType = EnumTypeUtil.getEnumFromValue(HTypeBatchStatus.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
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