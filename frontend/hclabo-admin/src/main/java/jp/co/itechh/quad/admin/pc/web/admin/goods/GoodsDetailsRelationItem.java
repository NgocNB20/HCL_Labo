/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品管理：商品詳細ページ 関連商品アイテム<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsDetailsRelationItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /************************************
     ** 商品規格項目
     ************************************/
    /**
     * No<br/>
     */
    private Integer relationDspNo;

    /**
     * 全角No<br/>
     */
    private String relationZenkakuNo;

    /**
     * 商品グループコード<br/>
     */
    private String relationGoodsGroupCode;

    /**
     * 商品グループ名<br/>
     */
    private String relationGoodsGroupName;

    /************************************
     ** 関連商品判定
     ************************************/
    /**
     * 関連商品有無<br/>
     *
     * @return true=関連商品グループあり
     */
    public boolean isExist() {
        return (relationDspNo != null);
    }

    /**
     * 関連商品有無<br/>
     *
     * @return true=関連商品グループなし
     */
    public boolean isNotExist() {
        // ※ なぜか必要です・・・
        return !isExist();
    }

}
