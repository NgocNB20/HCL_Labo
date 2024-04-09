/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;

import java.util.Date;

/**
 * 公開商品グループ詳細情報取得
 * 代表商品SEQを元に、同じ代表商品を持つ公開中の商品情報のリストを取得する。
 *
 * @author ozaki
 * @version $Revision: 1.3 $
 */
public interface OpenGoodsGroupDetailsGetService {

    /**
     * 公開商品グループ詳細情報取得
     * 代表商品SEQを元に、同じ代表商品を持つ公開中の商品情報のリストを取得する。
     *
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode      商品コード
     * @param siteType       サイト種別：列挙型
     * @param openStatus     削除状態付き公開状態
     * @param openStatus     削除状態付き公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループ情報DTO
     */
    GoodsGroupDto execute(String goodsGroupCode,
                          String goodsCode,
                          HTypeSiteType siteType,
                          HTypeOpenDeleteStatus openStatus,
                          Date frontDisplayReferenceDate);
}