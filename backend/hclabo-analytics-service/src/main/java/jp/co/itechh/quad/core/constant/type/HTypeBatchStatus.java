/*
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バッチタスク状態：列挙型
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */

@Getter
@AllArgsConstructor
public enum HTypeBatchStatus implements EnumType {

    /**
     * 正常終了（旧HIT-MALLでは「NORMAL_TERMINATION」）
     */
    COMPLETED("正常終了", "COMPLETED"),
    /**
     * 異常終了（旧HIT-MALLでは「ABNORMAL_TERMINATION」）
     */
    FAILED("異常終了", "FAILED");

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
