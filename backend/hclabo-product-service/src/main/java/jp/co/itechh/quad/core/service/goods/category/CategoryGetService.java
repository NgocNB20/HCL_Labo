/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;

import java.sql.Timestamp;

/**
 * カテゴリ取得
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
public interface CategoryGetService {

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間無し） */
    public static final String MSGCD_OPENSTATUS_NO_OPEN = "PREVIEW-STATUS-001-";

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間有り） */
    public static final String MSGCD_OPENSTATUS_OUT_OF_TERM = "PREVIEW-STATUS-002-";

    /**
     * カテゴリの取得
     *
     * @param categoryId カテゴリId
     * @return カテゴリ情報エンティティ
     */
    CategoryDto execute(String categoryId);

    /**
     * カテゴリの取得
     *
     * @param openStatus 公開状態
     * @param categoryId カテゴリId
     * @return カテゴリ情報エンティティ
     */
    CategoryDto execute(HTypeOpenStatus openStatus, String categoryId, Timestamp frontDisplayReferenceDate);

    /**
     * カテゴリの取得
     *
     * @param categoryId                カテゴリId
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリ情報エンティティ
     */
    CategoryDto execute(String categoryId, HTypeOpenStatus openStatus, Timestamp frontDisplayReferenceDate);

}
