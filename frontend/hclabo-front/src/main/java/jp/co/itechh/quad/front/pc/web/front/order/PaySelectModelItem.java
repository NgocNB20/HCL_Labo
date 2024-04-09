/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.constant.type.HTypeBillType;
import jp.co.itechh.quad.front.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import lombok.Data;

import java.io.Serializable;

import static jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType.CREDIT;

/**
 * 決済方法選択画面アイテム Model
 *
 * @author kimura
 */
@Data
public class PaySelectModelItem implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** 決済方法値 */
    private String settlementMethodValue;

    /** 決済方法名 */
    private String settlementMethodLabel;

    /** 決済タイプ */
    private HTypeSettlementMethodType settlementMethodType;

    /** 決済タイプ名 */
    private String settlementMethodTypeName;

    /** 請求タイプ */
    private HTypeBillType billType;

    /** 決済方法説明文PC */
    private String settlementNotePC;

    /** ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableInstallment;

    /** ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableRevolving;

    /** カード番号（動的バリデータ） */
    @HCHankaku
    private String cardNumber;

    /** 有効期限（月）（動的バリデータ） */
    private String expirationDateMonth;

    /** 有効期限（年）（動的バリデータ） */
    private String expirationDateYear;

    /** 支払区分 */
    private String paymentType;

    /** 分割回数（動的バリデータ） */
    private String dividedNumber;

    /**
     * セキュリティコード（動的バリデータ）
     */
    @HCHankaku
    private String securityCode;

    /** カード保存フラグ */
    private boolean saveFlg;

    /**
     * コンディション<br />
     * クレジットかどうか
     *
     * @return true..クレジット / false..クレジットではない
     */
    public boolean isCreditType() {
        if (this.settlementMethodType == null) {
            return CREDIT == getSettlementMethodType();
        }
        return CREDIT == this.settlementMethodType;
    }

    /**
     * 分割可能の決済かどうか判断<br/>
     *
     * @return true 分割可
     */
    public boolean isPossibleInstallment() {

        return HTypeEffectiveFlag.VALID == getEnableInstallment();
    }

    /**
     * リボ払い可能の決済かどうか判断<br/>
     *
     * @return true リボ払い可
     */
    public boolean isPossibleRevolving() {

        return HTypeEffectiveFlag.VALID == getEnableRevolving();
    }
}
