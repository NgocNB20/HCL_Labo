/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.logic.common;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfoShop;

/**
 * ショップ情報作成ロジック(共通情報)<br/>
 *
 * @author natume
 * @author sakai
 * @version $Revision: 1.2 $
 *
 */
public interface CommonInfoShopCreateLogic {

    /**
     * ショップ情報作成処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @return ショップ情報(共通情報)
     */
    CommonInfoShop execute(Integer shopSeq);
}