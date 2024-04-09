/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.goods.goods;

import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.dto.goods.stock.StockDto;
import jp.co.itechh.quad.front.entity.goods.goods.GoodsEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.3 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品エンティティ */
    private GoodsEntity goodsEntity;

    /** 在庫DTO */
    private StockDto stockDto;

    /** 削除フラグ */
    private String deleteFlg;

    /** 在庫状態PC */
    private HTypeStockStatusType stockStatusPc;

    /** フロント表示在庫状態　※プレビュー日時を加味して判定したステータス */
    private HTypeStockStatusType frontDisplayStockStatus;
}
