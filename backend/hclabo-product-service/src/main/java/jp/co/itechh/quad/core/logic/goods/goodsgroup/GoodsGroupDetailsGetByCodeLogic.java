/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;

import java.sql.Timestamp;

/**
 * 商品グループ詳細取得
 *
 * @author ozaki
 * @version $Revision: 1.5 $
 */
public interface GoodsGroupDetailsGetByCodeLogic {

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間無し） */
    public static final String MSGCD_OPENSTATUS_NO_OPEN = "PREVIEW-STATUS-001-";

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間有り） */
    public static final String MSGCD_OPENSTATUS_OUT_OF_TERM = "PREVIEW-STATUS-002-";

    /**
     * 商品グループ詳細取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode 商品コード
     * @param siteType サイト区分
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループ情報DTO
     */
    GoodsGroupDto execute(Integer shopSeq,
                          String goodsGroupCode,
                          String goodsCode,
                          HTypeSiteType siteType,
                          HTypeOpenDeleteStatus openStatus,
                          Timestamp frontDisplayReferenceDate);

    /**
     *
     * 商品グループ詳細取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode 商品コード
     * @param siteType サイト区分
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @param imageGetFlag 取得フラグ true..全商品規格画像 false..デフォルト商品規格画像
     * @return 商品グループ情報DTO
     */
    GoodsGroupDto execute(Integer shopSeq,
                          String goodsGroupCode,
                          String goodsCode,
                          HTypeSiteType siteType,
                          HTypeOpenDeleteStatus openStatus,
                          Timestamp frontDisplayReferenceDate,
                          Boolean imageGetFlag);

}