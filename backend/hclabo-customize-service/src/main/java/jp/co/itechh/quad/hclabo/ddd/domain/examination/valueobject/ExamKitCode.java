/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject;

import lombok.Getter;

/**
 * 検査キット番号 値オブジェクト
 */
@Getter
public class ExamKitCode {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ
     * ※ファクトリ、DB取得値設定用
     *
     * @param value 検査キット番号バリュー
     */
    public ExamKitCode(String value) {
        this.value = value;
    }
}
