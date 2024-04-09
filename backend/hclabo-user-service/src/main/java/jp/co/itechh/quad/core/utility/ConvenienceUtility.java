/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import org.springframework.stereotype.Component;

/**
 * ConvenienceUtility
 * <P>
 * コンビニに関連するヘルパークラス
 *
 * @author my33302
 *
 */
@Component
public class ConvenienceUtility {

    /** 隠蔽コンストラクタ  */
    public ConvenienceUtility() {
    }

    /** コンビニ種別：ローソン */
    public static final String CONVENIENCE_LAWSON = "00001";

    /** コンビニ種別：ローソン(新) */
    public static final String CONVENIENCE_LAWSON_NEW = "10001";

    /** コンビニ種別：ファミリーマート */
    public static final String CONVENIENCE_FAMILYMART = "00002";

    /** コンビニ種別：ファミリーマート(新) */
    public static final String CONVENIENCE_FAMILYMART_NEW = "10002";

    /** コンビニ種別：サークルＫサンクス */
    public static final String CONVENIENCE_CIRCLEKSUNKUS = "00003";

    /** コンビニ種別：サークルＫサンクス(新) */
    public static final String CONVENIENCE_CIRCLEKSUNKUS_NEW = "10003";

    /** コンビニ種別：ミニストップ */
    public static final String CONVENIENCE_MINISTOP = "00005";

    /** コンビニ種別：ミニストップ (新)*/
    public static final String CONVENIENCE_MINISTOP_NEW = "10005";

    /** コンビニ種別：デイリーヤマザキ */
    public static final String CONVENIENCE_DAILYYAMAZAKI = "00006";

    /** コンビニ種別：セブンイレブン */
    public static final String CONVENIENCE_SEVENELEVEN = "00007";

    /** コンビニ種別：セイコーマート */
    public static final String CONVENIENCE_SEICOMART = "00008";

    /** コンビニ種別：セイコーマート(新) */
    public static final String CONVENIENCE_SEICOMART_NEW = "10008";

    /** コンビニ種別：スリーエフ */
    public static final String CONVENIENCE_THREEF = "00009";

    /**
     * 決済方法がコンビニの表示パターン１（ローソン・ファミリーマート・サークルＫサンクス・ミニストップ）
     * @param convenience コンビニ種別
     * @return コンビニ決済（ローソン・ファミリーマート・サークルＫサンクス・ミニストップ）の場合true
     */
    public boolean isConveni1(String convenience) {
        // 編集前コンビニ⇒そのままの場合
        if (CONVENIENCE_LAWSON.equals(convenience) || CONVENIENCE_LAWSON_NEW.equals(convenience)
            || CONVENIENCE_FAMILYMART.equals(convenience) || CONVENIENCE_CIRCLEKSUNKUS.equals(convenience)
            || CONVENIENCE_MINISTOP.equals(convenience) || CONVENIENCE_MINISTOP_NEW.equals(convenience)) {
            return true;
        }
        return false;
    }

    /**
     * 決済方法がコンビニの表示パターン2（デイリーヤマザキ・セイコーマート・スリーエフ）
     * @param convenience コンビニ種別
     * @return コンビニ決済（デイリーヤマザキ・セイコーマート・スリーエフ）の場合true
     */
    public boolean isConveni2(String convenience) {
        if (CONVENIENCE_DAILYYAMAZAKI.equals(convenience) || CONVENIENCE_SEICOMART.equals(convenience)
            || CONVENIENCE_THREEF.equals(convenience)) {
            return true;
        }
        return false;
    }

    /**
     * 決済方法がコンビニの表示パターン3（セブンイレブン）
     * @param convenience コンビニ種別
     * @return コンビニ決済（セブンイレブン）の場合true
     */
    public boolean isConveni3(String convenience) {
        if (CONVENIENCE_SEVENELEVEN.equals(convenience)) {
            return true;
        }
        return false;
    }

    /**
     * 決済方法がコンビニの表示パターン4（ファミリーマート(新)）
     * @param convenience コンビニ種別
     * @return コンビニ決済（ファミリーマート(新)）の場合true
     */
    public boolean isConveni4(String convenience) {
        if (CONVENIENCE_FAMILYMART_NEW.equals(convenience)) {
            return true;
        }
        return false;
    }

    /**
     * 決済方法がコンビニの表示パターン5（サークルKサンクス(新)、セイコマート(新)）
     * @param convenience コンビニ種別
     * @return コンビニ決済（サークルKサンクス(新)、セイコマート(新)）の場合true
     */
    public boolean isConveni5(String convenience) {
        if (CONVENIENCE_CIRCLEKSUNKUS_NEW.equals(convenience) || CONVENIENCE_SEICOMART_NEW.equals(convenience)) {
            return true;
        }
        return false;
    }

}