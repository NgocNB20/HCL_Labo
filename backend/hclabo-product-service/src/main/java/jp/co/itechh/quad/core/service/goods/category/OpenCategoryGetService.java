/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;

import java.sql.Timestamp;

/**
 * 公開中カテゴリ取得
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface OpenCategoryGetService {

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間無し） */
    public static final String MSGCD_OPENSTATUS_NO_OPEN = "PREVIEW-STATUS-001-";

    /** 公開状態が公開以外の場合のエラーメッセージ（公開期間有り） */
    public static final String MSGCD_OPENSTATUS_OUT_OF_TERM = "PREVIEW-STATUS-002-";

    /**
     * カテゴリSEQリストを元にカテゴリDTOのリストを取得する
     *
     * @param categoryId カテゴリID
     * @return カテゴリDtoクラス
     */
    CategoryDto execute(String categoryId);

    /**
     * カテゴリSEQリストを元にカテゴリDTOのリストを取得する
     *
     * @param categoryId                カテゴリID
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリDtoクラス
     */
    CategoryDto execute(String categoryId, Timestamp frontDisplayReferenceDate);

}
