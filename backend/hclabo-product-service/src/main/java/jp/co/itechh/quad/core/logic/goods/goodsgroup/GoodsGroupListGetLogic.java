/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;

import java.util.List;

/**
 * 商品グループリスト取得
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupListGetLogic {

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間無し） */
    public static final String MSGCD_OPENSTATUS_NO_OPEN = "PREVIEW-STATUS-001-";

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間有り） */
    public static final String MSGCD_OPENSTATUS_OUT_OF_TERM = "PREVIEW-STATUS-002-";

    /**
     * 商品グループリスト取得
     * 検索条件をもとに商品グループ情報リストを取得する。
     *
     * @param conditionDto 商品グループDao用検索条件DTO
     * @return 商品グループリスト
     */
    List<GoodsGroupDto> execute(GoodsGroupSearchForDaoConditionDto conditionDto);

    /**
     * 商品グループSEQのリストを取得する
     *
     * @param goodGroupSeq                      商品グループSEQ
     * @param categoryConditionEntity           カテゴリ条件エンティティクラス
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティクラスのリスト
     * @return 商品グループSEQのリスト
     */
    List<Integer> execute(Integer goodGroupSeq,
                          CategoryConditionEntity categoryConditionEntity,
                          List<CategoryConditionDetailEntity> categoryConditionDetailEntityList);
}