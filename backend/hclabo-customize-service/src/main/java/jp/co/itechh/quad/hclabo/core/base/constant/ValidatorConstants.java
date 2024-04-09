/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.base.constant;

/**
 * 入力規制の情報を集約したクラス
 * <pre>
 * 散乱している全ての情報を集約するには工数がかかってしまうため
 * 案件毎に変わりそうな物を抜粋して集約
 * </pre>
 *
 * @author negishi
 *
 */
public class ValidatorConstants {

    /**
     * チェックスタイル対応（コンストラクタがなければ警告される）
     */
    protected ValidatorConstants() {
    }

    /* ━━━━━━━━━━━━ 共通化の一時情報のためprotected ━━━━━━━━━━━━ */

    protected static final String REGEX_PLUS = "+";

    /** 受注コードの正規表現 */
    protected static final String REGEX_EXAM_KIT_CODE_BASE = "[A-Z0-9]";

    /* ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ */

    /** 検査キット番号コードの正規表現 */
    public static final String REGEX_EXAM_KIT_CODE = REGEX_EXAM_KIT_CODE_BASE + REGEX_PLUS;

    /** 検査キット番号コードの最大文字数 */
    public static final int LENGTH_EXAM_KIT_CODE_MAXIMUM = 20;

}
