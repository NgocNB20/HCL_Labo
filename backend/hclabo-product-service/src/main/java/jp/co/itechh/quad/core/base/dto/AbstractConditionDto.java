/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.dto;

import jp.co.itechh.quad.core.web.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 検索条件DTO基底クラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@Data
public abstract class AbstractConditionDto implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ページ情報 */
    private PageInfo pageInfo;

    /**
     * 検索結果総件数取得（long)
     * @return 検索結果総件数
     */
    public long getTotalCountLong() {

        if (this.pageInfo == null || this.pageInfo.getSelectOptions() == null) {
            return 0;
        }

        return this.pageInfo.getSelectOptions().getCount();
    }

    /**
     * 検索結果総件数取得（int)
     * @return 検索結果総件数
     */
    public int getTotalCount() {
        return Math.toIntExact(getTotalCountLong());
    }

    /**
     * オフセット取得
     * @return オフセット
     */
    public int getOffset() {

        if (this.pageInfo == null) {
            return 0;
        }

        return this.pageInfo.getOffset();
    }
}