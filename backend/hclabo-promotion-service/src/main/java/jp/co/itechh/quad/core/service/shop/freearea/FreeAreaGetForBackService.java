/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア取得(管理機能用)
 *
 * @author shibuya
 * @version $Revision: 1.2 $
 *
 */
public interface FreeAreaGetForBackService {

    /* メッセージ SSF0003 */
    /**
     * フリーエリア取得失敗エラー
     * <code>MSGCD_FREEAREA_GET_FAIL</code>
     */
    String MSGCD_FREEAREA_GET_FAIL = "SSF000301";

    /**
     * フリーエリア取得(管理機能用)
     *
     * @param freeAreaSeq フリーエリアSEQ
     * @return フリーエリアエンティティ
     */
    FreeAreaEntity execute(Integer freeAreaSeq);
}
