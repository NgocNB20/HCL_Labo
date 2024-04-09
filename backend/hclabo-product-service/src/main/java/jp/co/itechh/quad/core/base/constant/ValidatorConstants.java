/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.constant;

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

    /** 商品説明の最大文字数 */
    public static final int LENGTH_GOODS_NOTE_MAXIMUM = 4000;

    /** 商品_受注連携設定の最大文字数 */
    public static final int LENGTH_GOODS_ORDERSETTING_MAXIMUM = 4000;

    /** 商品グループコード（商品管理番号）の正規表現 */
    protected static final String REGEX_GOODS_GROUP_CODE_BASE = "[a-zA-Z0-9-]";

    /** + */
    protected static final String REGEX_PLUS = "+";

    /** 商品グループコード（商品管理番号）の正規表現（画面用）*/
    public static final String REGEX_GOODS_GROUP_CODE = REGEX_GOODS_GROUP_CODE_BASE + REGEX_PLUS;

}
