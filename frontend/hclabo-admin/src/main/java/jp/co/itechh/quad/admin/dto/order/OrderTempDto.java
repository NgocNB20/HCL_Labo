/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.dto.cart.CartDto;
import jp.co.itechh.quad.admin.dto.shop.questionnaire.QuestionnaireReplyDisplayDto;
import jp.co.itechh.quad.admin.entity.shop.questionnaire.QuestionnaireEntity;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 受注一時情報Dtoクラス
 *
 * @author DtoGenerator
 *
 */
@Data
@Component
@Scope("prototype")
public class OrderTempDto implements Serializable {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTempDto.class);

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** クレジットカード番号 */
    private String cardNo;

    /** カード有効期限(YYMM) */
    private String expire;

    /** セキュリティーコード */
    private String securityCode;

    /** お支払い区分 */
    private String method;

    /** 分割回数 */
    private Integer payTimes;

    /** 本人認証パスワード入力画面URL(カード決済の通信Output項目) */
    private String acsUrl;

    /** 本人認証サービス要求電文(カード決済の通信Output項目) */
    private String paReq;

    /** 決済代行会社会員ID */
    private String paymentMemberId;

    /** この決済で登録済みカードを使用するならtrue */
    private boolean useRegistCardFlg;

    /** カード保存フラグ(今回のカード情報を保存するを選択した場合) */
    private boolean saveFlg;

    /** 登録されたカードがあるならtrue */
    private boolean registCredit;

    /** カート情報 */
    private CartDto cartDto;

    /** 全額クーポン支払フラグ */
    private boolean canUseCouponSettlementFlg;

    /** トークン */
    private String token;

    /**
     * @return カード有効期限(YYMM)のうち、年(YY)部分
     */
    public String getExpireYear() {
        try {
            return expire.substring(0, 2);
        } catch (RuntimeException e) {
            LOGGER.error("例外処理が発生しました", e);
        }
        return null;
    }

    /**
     * @return カード有効期限(YYMM)のうち、月(MM)部分
     */
    public String getExpireMonth() {
        try {
            return expire.substring(2);
        } catch (RuntimeException e) {
            LOGGER.error("例外処理が発生しました", e);
        }
        return null;
    }

    /** true = アンケート取得済 */
    private boolean questionnaireReadFlag;

    /** アンケート */
    private QuestionnaireEntity questionnaireEntity;

    /** アンケート回答画面表示用DTO */
    private List<QuestionnaireReplyDisplayDto> questionnaireReplyDisplayDtoList;

    /** 別のカードを使うボタン押下フラグ */
    private boolean preCreditInformationFlag;
}