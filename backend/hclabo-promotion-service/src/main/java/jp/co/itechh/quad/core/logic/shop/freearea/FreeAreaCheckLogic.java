/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリアデータチェック
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface FreeAreaCheckLogic {

    /* メッセージ LSF0005 */
    /**
     * フリーエリア重複エラー
     * <code>MSGCD_DUPLICATE_FREEAREA</code>
     */
    public static final String MSGCD_DUPLICATE_FREEAREA = "LSF000501";

    /**
     *
     * フリーエリアデータチェック
     *
     * @param freeAreaEntity フリーエリアエンティティ
     */
    void execute(FreeAreaEntity freeAreaEntity);

}
