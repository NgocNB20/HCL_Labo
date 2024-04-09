/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * サイト種別：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSiteType implements EnumType {

    /** フロントPC */
    FRONT_PC("PC", "0"),

    /** バック */
    BACK("管理画面", "3");

    /**
     * PCサイトパス名
     */
    public static final String FRONT_SITE_PATH = "front";

    /**
     * 携帯サイトパス名
     */
    public static final String MOBILE_SITE_PATH = "mobile";

    /**
     * 管理者画面パス名
     */
    public static final String ADMIN_SITE_PATH = "admin";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HTypeSiteType.class);

    /**
     * フロントか否かの判定
     *
     * @return true=フロント, false=フロント以外
     */
    public boolean isFront() {
        return this == FRONT_PC;
    }

    /**
     * フロントPCか否かの判定
     *
     * @return true=フロントPC, false=フロントPC以外
     */
    public boolean isFrontPC() {
        return this == FRONT_PC;
    }

    /**
     * バックか否かの判定
     *
     * @return true=バック, false=バック以外
     */
    public boolean isBack() {
        return this == BACK;
    }

    /**
     * サイト判定
     * <pre>
     * hTypeSiteType.site.pathの値からサイトを判断
     * </pre>
     * @param url リクエストURL
     * @return サイト区分
     * @see #FRONT_SITE_PATH
     * @see #ADMIN_SITE_PATH
     */
    public static HTypeSiteType getSiteType(String url) {
        String key = "hTypeSiteType.site.path";
        String sitePath = PropertiesUtil.getSystemPropertiesValue(key);

        if (FRONT_SITE_PATH.equals(sitePath)) {
            // フロントPC
            return HTypeSiteType.FRONT_PC;

        } else if (ADMIN_SITE_PATH.equals(sitePath)) {
            // 管理画面
            return HTypeSiteType.BACK;
        } else {
            // デフォルト：フロントPC
            LOGGER.error(String.format("hTypeSiteType.site.pathの値が不正[%s] 呼び出し元でエラーを発生させないためFRONT_PCを返却", sitePath));
            return HTypeSiteType.FRONT_PC;
        }
    }

    /**
     * サイトが複数配送に対応しているか判定する
     *
     * @return true:対応 false:非対応
     */
    public boolean isMultiDeliSite() {
        // 常にfalse（本メソッドは後々削除される想定）
        return false;
    }

    /** doma用ファクトリメソッド */
    public static HTypeSiteType of(String value) {

        HTypeSiteType hType = EnumTypeUtil.getEnumFromValue(HTypeSiteType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}