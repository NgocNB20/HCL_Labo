/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockStatusGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupStockStatusGetLogic;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 商品グループの在庫状況取得ロジック
 *
 * @author Kaneko　2013/03/01
 */
@Component
public class GoodsGroupStockStatusGetLogicImpl extends AbstractShopLogic implements GoodsGroupStockStatusGetLogic {

    /** 商品規格の在庫状況取得ロジック */
    private final GoodsStockStatusGetLogic goodsStockStatusGetLogic;

    /** 商品Utility */
    private final GoodsUtility goodsUtility;

    @Autowired
    public GoodsGroupStockStatusGetLogicImpl(GoodsStockStatusGetLogic goodsStockStatusGetLogic,
                                             GoodsUtility goodsUtility) {
        this.goodsStockStatusGetLogic = goodsStockStatusGetLogic;
        this.goodsUtility = goodsUtility;
    }

    /**
     * 商品グループの在庫状況の設定
     * <pre>
     * 商品の販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 商品グループに複数規格が存在する場合は、「HTypeStockStatusTypeのordinal」順の優先度として、商品グループの在庫状態を決定する。
     * 「5:残りわずか」 販売中で、販売可能在庫数が残少表示在庫数より少ない状態。
     * 「10:予約受付中」 予約受付中の状態
     * 「4:在庫あり」 販売中で、販売可能在庫数が1以上の状態。
     * 「3:在庫なし」 販売中で、販売可能在庫数が0の商品。入荷する予定ありの商品。
     * 「9:予約受付前」 公開中で、予約受付開始前
     * 「2:販売前」 販売中で、販売期間開始前の商品
     * 「1:販売期間終了」 販売中で、販売期間終了後の商品
     * 「0:非販売」 非販売の状態
     * </pre>
     *
     * @param goodsDtoList 商品DTOリスト
     * @return 商品DTOリスト内の最大優先度の在庫状態
     */
    @Override
    public HTypeStockStatusType execute(List<GoodsDto> goodsDtoList) {

        // 規格単位の在庫状況を取得
        Integer shopSeq = 1001;
        Map<Integer, HTypeStockStatusType> goodsStockStatusMap =
                        goodsStockStatusGetLogic.execute(goodsDtoList, shopSeq);

        // 商品グループ単位の在庫状況に変換して返す
        HTypeStockStatusType resultType = goodsUtility.convertGoodsGroupStockStatus(goodsDtoList, goodsStockStatusMap);

        return resultType;
    }

}