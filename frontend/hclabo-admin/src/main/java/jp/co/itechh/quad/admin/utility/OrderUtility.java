/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import org.springframework.stereotype.Component;

/**
 * 受注業務ユーティリティクラス
 *
 * @author negishi
 * @author Kaneko (itec) 2012/08/20 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class OrderUtility {

    /** 無料決済SEQ（設定値） */
    protected static final String FREE_SETTLEMENT_METHOD_SEQ = "free.settlement.method.seq";

    public OrderUtility() {
    }

    /**
     * 無料配送の決済方法 SEQを取得する。
     *
     * @return 無料決済方法SEQ
     */
    public Integer getFreeSettlementMethodSeq() {
        return PropertiesUtil.getSystemPropertiesValueToInt(FREE_SETTLEMENT_METHOD_SEQ);
    }
}