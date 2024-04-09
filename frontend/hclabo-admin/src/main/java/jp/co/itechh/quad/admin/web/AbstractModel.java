/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.web;

import jp.co.itechh.quad.admin.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import lombok.Data;

/**
 * 共通親モデル
 * @author yt23807
 */
@Data
public abstract class AbstractModel {

    /** デフォルト最大表示件数 */
    private static final int PAGE_DEFAULT_LIMIT_MODEL = 100;

    /** ページ情報 */
    private PageInfo pageInfo;

    /** POST後のF5押下の防止対策フラグ */
    private boolean reloadFlag;

    /**
     * 共通情報の取得<br/>
     * @return 共通情報
     */
    public CommonInfo getCommonInfo() {
        return ApplicationContextUtility.getBean(CommonInfo.class);
    }

    /**
     * デフォルト最大表示件数の取得
     *
     * @return デフォルト最大表示件数
     */
    public int getPageDefaultLimitModel() {
        return PAGE_DEFAULT_LIMIT_MODEL;
    }
}
