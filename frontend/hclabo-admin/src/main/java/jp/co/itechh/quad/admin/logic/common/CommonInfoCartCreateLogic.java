/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.logic.common;

import java.math.BigDecimal;

/**
 * カート情報作成Logic(共通情報)<br/>
 *
 * @author sk39909
 */
public interface CommonInfoCartCreateLogic {

    /**
     * カート情報作成処理<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param accessUid 端末識別番号
     * @return カート合計数量
     */
    BigDecimal execute(Integer memberInfoSeq, String accessUid);
}
