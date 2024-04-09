/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.common.dto;

import jp.co.itechh.quad.admin.pc.web.admin.goods.category.GoodsModelItem;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * カテゴリ登録商品手動追加Dtoクラス
 *
 **/
@Data
@Component
@Scope("prototype")
public class CategoryGoodsManualAddDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** オプション 手動ソート*/
    private String optionSort;

    /** 商品モデルアイテムリスト*/
    private List<GoodsModelItem> goodsItems;
}
