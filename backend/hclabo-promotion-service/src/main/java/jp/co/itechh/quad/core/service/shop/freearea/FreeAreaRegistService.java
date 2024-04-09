/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア登録サービス
 *
 * @author shibuya
 * @version $Revision: 1.2 $
 *
 */
public interface FreeAreaRegistService {

    /* メッセージ SSF0004 */
    /**
     * フリーエリア登録失敗エラー
     * <code>MSGCD_FREEAREA_REGIST_FAIL</code>
     */
    String MSGCD_FREEAREA_REGIST_FAIL = "SSF000401";

    /**
     * フリーエリア登録
     *
     * @param freeAreaEntity フリーエリアエンティティ
     * @return 処理件数
     */
    int execute(FreeAreaEntity freeAreaEntity);
}
