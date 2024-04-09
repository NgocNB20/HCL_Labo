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
 * 商品管理：商品詳細ページ 商品インフォメーションアイコンアイテム<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsDetailsInformationIconItem implements Serializable {

    /**
     * serialVersionUID<br/>
     */
    private static final long serialVersionUID = 1L;

    // 商品インフォメーションアイコン情報
    /**
     * アイコンSEQ
     */
    private Integer iconSeq;

    /**
     * アイコン名
     */
    private String iconName;

    /**
     * カラーコード
     */
    private String colorCode;

    /**
     * チェックボックス
     */
    private boolean checkBoxPc;

    /************************************
     ** 条件判定
     ************************************/

    /**
     * チェックボックスのチェック判定<br/>
     *
     * @return true=チェックされている場合
     */
    public boolean isCheckedPcIcon() {
        return checkBoxPc;
    }

}
