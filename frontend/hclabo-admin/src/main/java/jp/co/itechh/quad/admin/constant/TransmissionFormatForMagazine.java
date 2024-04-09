/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.admin.constant;

import jp.co.itechh.quad.admin.constant.type.HTypeMagazineType;

/**
 * メールテンプレート 送信形式入力値
 * <pre>
 * リファクタリング目的で作成
 * 以下、リテラル値をハードコーディングしているコードが対象
 * if (page.getEditingVersion() == 2) {
 *     mailDto.setHtmlMail(true);
 * } else if (page.getEditingVersion() == 3) {
 *     mailDto.setMobileMail(true);
 * } else {
 *     mailDto.setHtmlMail(false);
 *     mailDto.setMobileMail(false);
 * }
 * </pre>
 * <pre>
 * {@link HTypeMagazineType}を使用できれば良かったのだが、値が異なるため不可
 * HTypeMagazineTypeはTEXTが0、HTMLが1、モバイルが2
 *  送信形式入力値はTEXTが1、HTMLが2、モバイルが3
 * </pre>
 * @author kaneda
 */
public class TransmissionFormatForMagazine {

    /** 隠蔽コンストラクタ */
    private TransmissionFormatForMagazine() {
    }

    /** PC-テキスト */
    public static final int INT_PC_TEXT = 1;
    /** PC-HMTL */
    public static final int INT_PC_HTML = 2;

    /** PC-テキスト-文字コード */
    public static final String CHARSET_PC_TEXT = "ISO-2022-JP";
    /** PC-HMTL-文字コード */
    public static final String CHARSET_PC_HTML = "ISO-2022-JP";
}