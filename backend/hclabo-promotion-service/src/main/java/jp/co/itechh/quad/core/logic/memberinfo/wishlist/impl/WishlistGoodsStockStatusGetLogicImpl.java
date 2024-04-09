/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockStatusDisplayGetRealStatusLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistGoodsStockStatusGetLogic;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * お気に入り商品の在庫状態取得ロジック
 *
 * @author Kaneko　2013/03/01
 */
@Component
public class WishlistGoodsStockStatusGetLogicImpl extends AbstractShopLogic
                implements WishlistGoodsStockStatusGetLogic {

    /** リアルタイム在庫状況判定ロジック */
    private StockStatusDisplayGetRealStatusLogic stockStatusDisplayGetRealStatusLogic;

    /**
     * コンストラクタ
     *
     * @param stockStatusDisplayGetRealStatusLogic
     */
    @Autowired
    public WishlistGoodsStockStatusGetLogicImpl(StockStatusDisplayGetRealStatusLogic stockStatusDisplayGetRealStatusLogic) {
        this.stockStatusDisplayGetRealStatusLogic = stockStatusDisplayGetRealStatusLogic;

    }

    /**
     * お気に入り商品の在庫状態の設定。
     * <pre>
     * 商品の公開状態、公開期間、販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     * @param wishlistDtoList お気に入り商品DTOリスト
     * @return お気に入り商品DTOリスト内の最大優先度の在庫状態
     */
    @Override
    public List<WishlistDto> execute(List<WishlistDto> wishlistDtoList) {

        for (WishlistDto wishlistDto : wishlistDtoList) {

            GoodsDetailsDto goodsDetailsDto = wishlistDto.getGoodsDetailsDto();

            HTypeStockStatusType currentStatus = getCurrentStockStatus(goodsDetailsDto);

            wishlistDto.setStockStatus(EnumTypeUtil.getValue(currentStatus));

        }
        // 在庫状態を返却
        return wishlistDtoList;
    }

    /**
     * リアルタイムの在庫状況を取得
     *
     * @param goodsDetailsDto 商品詳細DTO
     * @return リアルタイムの在庫状況
     */
    protected HTypeStockStatusType getCurrentStockStatus(GoodsDetailsDto goodsDetailsDto) {

        StockStatusDisplayConditionDto condition =
                        ApplicationContextUtility.getBean(StockStatusDisplayConditionDto.class);

        // 販売可能在庫数
        condition.setSalesPossibleStock(goodsDetailsDto.getSalesPossibleStock());
        // 残少表示在庫数
        condition.setRemainderFewStock(goodsDetailsDto.getRemainderFewStock());
        // 在庫管理フラグ
        condition.setStockManagementFlag(goodsDetailsDto.getStockManagementFlag());
        // 公開状態
        HTypeOpenDeleteStatus openStatus = null;
        // 公開開始日
        Timestamp openStartTime = null;
        // 公開終了日
        Timestamp openEndTime = null;
        // 販売状態、販売開始日、販売終了日
        condition.setSaleStatus(goodsDetailsDto.getSaleStatusPC());
        condition.setSaleStartTime(goodsDetailsDto.getSaleStartTimePC());
        condition.setSaleEndTime(goodsDetailsDto.getSaleEndTimePC());

        openStatus = goodsDetailsDto.getGoodsOpenStatusPC();
        openStartTime = goodsDetailsDto.getOpenStartTimePC();
        openEndTime = goodsDetailsDto.getOpenEndTimePC();

        // 在庫状態を決定
        // お気に入り商品リストでは公開状態も考慮する
        HTypeStockStatusType currentStatus =
                        stockStatusDisplayGetRealStatusLogic.execute(condition, openStatus, openStartTime, openEndTime);
        return currentStatus;
    }

}