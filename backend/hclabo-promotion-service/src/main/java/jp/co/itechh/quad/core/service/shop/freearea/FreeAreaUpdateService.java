/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア更新サービス
 *
 * @author shibuya
 * @version $Revision: 1.2 $
 *
 */
public interface FreeAreaUpdateService {

    /* メッセージ SSF0005 */
    /**
     * フリーエリア更新失敗エラー
     * <code>MSGCD_FREEAREA_UPDATE_FAIL</code>
     */
    String MSGCD_FREEAREA_UPDATE_FAIL = "SSF000501";

    /**
     * フリーエリア更新
     *
     * @param freeAreaEntity フリーエリアエンティティ
     * @return 処理件数
     */
    int execute(FreeAreaEntity freeAreaEntity);
}
