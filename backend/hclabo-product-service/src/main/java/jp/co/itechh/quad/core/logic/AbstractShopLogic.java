/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic;

import jp.co.itechh.quad.core.base.logic.AbstractLogic;
import org.springframework.stereotype.Component;

/**
 * ショップ用ロジック基底クラス
 *
 * @author ueshima
 * @author tm27400
 * @version $Revision: 1.1 $
 */
@Component
public abstract class AbstractShopLogic extends AbstractLogic {

    /**
     * 商品在庫管理フラグ不一致エラー
     * <code>MSGCD_LOGISTIC_SYNC_MANAGEMENT_FLAG_FAIL</code>
     */
    public static final String MSGCD_LOGISTIC_SYNC_MANAGEMENT_FLAG_FAIL = "GOODS-STOCK-001-E";

}