/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.batch.core.throwable;

/**
 * データ件数が0件だった場合にスローするクラス
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 *
 */
public class NoDataException extends RuntimeException {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ。
     */
    public NoDataException() {
        super();
    }
}
