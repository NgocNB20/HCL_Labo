/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品グループDtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class GoodsGroupDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループエンティティ */
    private GoodsGroupEntity goodsGroupEntity;

    /** 商品グループ在庫状態更新バッチ実行時点での在庫状況 */
    private StockStatusDisplayEntity batchUpdateStockStatus;

    /** 商品データ取得時点での在庫状況 */
    private StockStatusDisplayEntity realTimeStockStatus;

    /** 商品グループ表示エンティティ */
    private GoodsGroupDisplayEntity goodsGroupDisplayEntity;

    /** 商品グループ画像エンティティリスト */
    private List<GoodsGroupImageEntity> goodsGroupImageEntityList;

    /** 商品DTOリスト */
    private List<GoodsDto> goodsDtoList;

    /** カテゴリ登録商品エンティティリスト */
    private List<CategoryGoodsEntity> categoryGoodsEntityList;

    /** アイコン詳細DTOリスト */
    private List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList;

    /**税率*/
    private BigDecimal taxRate;
}
