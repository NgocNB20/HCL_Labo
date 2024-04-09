/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockStatusGetLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockStatusDisplayGetRealStatusLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品規格の在庫状態取得ロジック
 * ※
 *
 * @author hs32101
 *
 */
@Component
public class GoodsStockStatusGetLogicImpl extends AbstractShopLogic implements GoodsStockStatusGetLogic {

    /** リアルタイム在庫状況判定ロジック */
    private final StockStatusDisplayGetRealStatusLogic stockStatusDisplayGetRealStatusLogic;

    @Autowired
    public GoodsStockStatusGetLogicImpl(StockStatusDisplayGetRealStatusLogic stockStatusDisplayGetRealStatusLogic) {
        this.stockStatusDisplayGetRealStatusLogic = stockStatusDisplayGetRealStatusLogic;
    }

    /**
     * 商品規格の在庫状態の設定
     * <pre>
     * 商品の販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 「5:残りわずか」 販売中で、販売可能在庫数が残少表示在庫数より少ない状態。
     * 「10：予約受付中」 予約商品で、販売可能在庫数が1以上の状態。
     * 「4:在庫あり」 販売中で、販売可能在庫数が1以上の状態。
     * 「3:在庫なし」 販売中で、販売可能在庫数が0の商品。入荷する予定ありの商品。
     * 「9:予約受付前」 予約商品で、販売期間開始前の商品
     * 「2:販売前」 販売中で、販売期間開始前の商品
     * 「1:販売期間終了」 販売中で、販売期間終了後の商品
     * 「0:非販売」 非販売の状態
     * </pre>
     *
     * @param goodsDtoList 商品DTOリスト
     * @param shopSeq ショップSEQ
     * @return 規格単位の在庫ステータスMAP＜商品SEQ、在庫状況＞
     */
    @Override
    public Map<Integer, HTypeStockStatusType> execute(List<GoodsDto> goodsDtoList, Integer shopSeq) {

        Map<Integer, HTypeStockStatusType> resultMap = new LinkedHashMap<>();
        for (GoodsDto goodsDto : goodsDtoList) {

            if (goodsDto.getGoodsEntity().getGoodsGroupSeq() == null) {
                continue;
            }

            // コンディション作成
            StockStatusDisplayConditionDto condition = createCondition(goodsDto);

            // 在庫状況をリアルタイムで判定
            HTypeStockStatusType currentStatus = stockStatusDisplayGetRealStatusLogic.execute(condition);

            // MAPに格納
            if (!resultMap.containsKey(goodsDto.getGoodsEntity().getGoodsSeq())) {
                resultMap.put(goodsDto.getGoodsEntity().getGoodsSeq(), currentStatus);
            }
        }
        // 在庫状態MAPを返却
        return resultMap;
    }

    /**
     * 在庫状態表示判定用DTO作成
     *
     * @param goodsDto 商品DTO
     * @return 在庫状態表示判定用DTO
     */
    protected StockStatusDisplayConditionDto createCondition(GoodsDto goodsDto) {

        GoodsEntity goodsEntity = goodsDto.getGoodsEntity();
        StockDto stockDto = goodsDto.getStockDto();

        StockStatusDisplayConditionDto condition =
                        ApplicationContextUtility.getBean(StockStatusDisplayConditionDto.class);

        // 販売可能在庫数
        condition.setSalesPossibleStock(stockDto.getSalesPossibleStock());
        // 残少表示在庫数
        condition.setRemainderFewStock(stockDto.getRemainderFewStock());
        // 在庫管理フラグ
        condition.setStockManagementFlag(goodsEntity.getStockManagementFlag());

        // 販売状態、販売開始日、販売終了日
        condition.setSaleStatus(goodsEntity.getSaleStatusPC());
        condition.setSaleStartTime(goodsEntity.getSaleStartTimePC());
        condition.setSaleEndTime(goodsEntity.getSaleEndTimePC());
        return condition;
    }

}