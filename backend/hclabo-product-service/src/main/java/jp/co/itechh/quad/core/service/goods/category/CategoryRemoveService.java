/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

/**
 * カテゴリ削除
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
public interface CategoryRemoveService {

    /**
     * カテゴリ画像削除失敗エラー
     * <code>MSGCD_CATEGORYIMAGE_DELETE_FAIL</code>
     */
    public static final String MSGCD_CATEGORYIMAGE_DELETE_FAIL = "SGC001111";

    /**
     * カテゴリの削除
     *
     * @param categoryId カテゴリID
     * @return 件数
     */
    int execute(String categoryId);

}
