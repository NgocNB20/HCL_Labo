/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.bill;

import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGmoReleaseFlag;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * 受注請求クラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderBillEntity implements Serializable {

    /**
     * シリアル
     */
    private static final long serialVersionUID = 6532166004490690858L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注請求連番（必須） */
    private Integer orderBillVersionNo;

    /** 請求状態（必須） */
    private HTypeBillStatus billStatus = HTypeBillStatus.BILL_NO_CLAIM;

    /** 請求日時（必須） */
    private Timestamp billTime;

    /** 請求金額（必須） */
    private BigDecimal billPrice = new BigDecimal(0);

    /** 決済方法SEQ（必須） */
    private Integer settlementMethodSeq;

    /** クレジットカード会社コード */
    private String creditCompanyCode;

    /** クレジットカード会社 */
    private String creditCompany;

    /** 決済手数料（必須） */
    private BigDecimal settlementCommission = new BigDecimal(0);

    /** 異常フラグ（必須） */
    private HTypeEmergencyFlag emergencyFlag = HTypeEmergencyFlag.OFF;

    /** GMO連携解除フラグ */
    private HTypeGmoReleaseFlag gmoReleaseFlag = HTypeGmoReleaseFlag.NORMAL;

    /** 支払期限日 */
    private Timestamp paymentTimeLimitDate;

    /** キャンセル可能日 */
    private Timestamp cancelableDate;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** 入金日時 */
    private Date moneyReceiptTime;

    // テーブル項目外追加フィールド

    /** クレジットカードブランド名PC */
    private String cardBrandDisplayPC;

    /**
     * @param paymentTimeLimitDate the paymentTimeLimitDate to set
     */
    public void setPaymentTimeLimitDate(Timestamp paymentTimeLimitDate) {
        this.paymentTimeLimitDate = paymentTimeLimitDate;

        // 時分秒を切り捨てる
        if (this.paymentTimeLimitDate != null) {
            this.paymentTimeLimitDate =
                            new Timestamp(DateUtils.truncate(this.paymentTimeLimitDate, Calendar.DATE).getTime());
        }
    }

    /**
     * @param cancelableDate the cancelableDate to set
     */
    public void setCancelableDate(Timestamp cancelableDate) {
        this.cancelableDate = cancelableDate;
        // 時分秒を切り捨てる
        if (this.cancelableDate != null) {
            this.cancelableDate = new Timestamp(DateUtils.truncate(this.cancelableDate, Calendar.DATE).getTime());
        }
    }
}