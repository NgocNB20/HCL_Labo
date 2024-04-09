/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 受注詳細モデル<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDetailsModel extends AbstractOrderDetailsModel {

    /** 受注詳細共通モデル */
    private OrderDetailsCommonModel orderDetailsCommonModel;

    /**
     * キャンセル受注の更新モードかどうか<br/>
     * true..更新モード
     */
    private boolean canceledOrderUpdate;

    /** メモ（入力） */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 2000, message = "{HTextAreaValidator.LENGTH_detail}")
    private String editMemo;

    /**
     * 検索条件保持判定用<br />
     * 遷移元パッケージを格納<br />
     * チケット対応#2743対応
     */
    private String from;

    private String md = "list";

    /** メール送信済みフラグ */
    private boolean mailSentFlag;

    /**
     * @param mailSentFlag the mailSentFlag to set
     */
    public void setMailSentFlag(boolean mailSentFlag) {
        if (mailSentFlag) {
            this.mailSentFlag = mailSentFlag;
        }
    }

    /**
     * 現在時刻がGMO即時払いキャンセル期限内
     *
     * @return 現在時刻がGMO即時払いキャンセル期限内: true、 else false
     */
    public boolean isPassGmoCancelDeadline() {
        if (this.orderDetailsCommonModel != null && this.orderDetailsCommonModel.billingSlipResponse != null
            && this.orderDetailsCommonModel.billingSlipResponse.getPaymentLinkResponse() != null
            && this.orderDetailsCommonModel.billingSlipResponse.getPaymentLinkResponse().getCancelLimit() != null) {
            Date currentDate = new Date(System.currentTimeMillis());
            Date cancelLimit =
                            this.orderDetailsCommonModel.billingSlipResponse.getPaymentLinkResponse().getCancelLimit();

            int result = cancelLimit.compareTo(currentDate);
            if (result < 0) {
                return true;
            }
        }
        return false;
    }
}