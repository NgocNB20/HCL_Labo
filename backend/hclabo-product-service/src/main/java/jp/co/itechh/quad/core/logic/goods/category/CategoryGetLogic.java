/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;

import java.sql.Timestamp;

/**
 * カテゴリ情報取得
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
public interface CategoryGetLogic {

    /**
     * カテゴリ情報を取得する。
     *
     * @param shopSeq    ショップSEQ
     * @param categoryId カテゴリID
     * @return カテゴリエンティティ
     */
    CategoryEntity execute(Integer shopSeq, String categoryId);

    /**
     * カテゴリ情報を取得する。
     *
     * @param shopSeq    ショップSEQ
     * @param categoryId カテゴリID
     * @param openStatus 公開状態
     * @return カテゴリエンティティ
     */
    CategoryEntity execute(Integer shopSeq, String categoryId, HTypeOpenStatus openStatus);

    /**
     * カテゴリ情報を取得する。
     *
     * @param shopSeq                   ショップSEQ
     * @param categoryId                カテゴリID
     * @param openStatus                公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリエンティティ
     */
    CategoryEntity execute(Integer shopSeq,
                           String categoryId,
                           HTypeOpenStatus openStatus,
                           Timestamp frontDisplayReferenceDate);

    /**
     * カテゴリ情報を取得する。
     * 公開のみカテゴリ情報を取得する。
     *
     * @param shopSeq                   ショップSEQ
     * @param categoryId                カテゴリID
     * @param openStatus                公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリエンティティ
     */
    CategoryEntity getCategoryOpen(Integer shopSeq,
                                   String categoryId,
                                   HTypeOpenStatus openStatus,
                                   Timestamp frontDisplayReferenceDate);
}