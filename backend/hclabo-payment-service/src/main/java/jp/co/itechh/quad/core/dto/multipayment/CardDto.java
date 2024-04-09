/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.multipayment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * カード預かり情報登録更新Dto
 *
 * @author s_tsuru
 */
@Data
@Component
@Scope("prototype")
public class CardDto implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 会員SEQ
     */
    private Integer memberInfoSeq;

    /**
     * 決済代行会員ID
     */
    private String paymentMemberId;

    /**
     * クレジットカード番号
     */
    private String cardNumber;

    /**
     * 有効期限：YYMM
     */
    private String expirationDate;

    /**
     * セキュリティーコード
     */
    private String securityCode;

    /**
     * 受注SEQ
     */
    private Integer orderSeq;

    /**
     * 受注コード
     */
    private String orderCode;

    /**
     * 受注履歴連番
     */
    private Integer orderVersionNo;

    /**
     * 登録されたカードがあるならtrue
     */
    private boolean registCredit;

    /**
     * この決済で登録済みカードを使用するならtrue
     */
    private boolean useRegistCardFlg;

    /**
     * トークン
     */
    private String token;

}
