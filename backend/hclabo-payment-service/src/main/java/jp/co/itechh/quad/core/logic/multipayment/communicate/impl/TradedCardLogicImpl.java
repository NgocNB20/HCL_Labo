/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate.impl;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.TradedCardOutput;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.core.dto.multipayment.HmPaymentClientInput;
import jp.co.itechh.quad.core.dto.multipayment.HmTradedCardInput;
import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.CardBrandGetLogic;
import jp.co.itechh.quad.core.logic.multipayment.CardBrandGetMaxCardBrandSeqLogic;
import jp.co.itechh.quad.core.logic.multipayment.CardBrandRegistLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.TradedCardLogic;
import jp.co.itechh.quad.core.utility.MulPayUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 決済後カード登録通信ロジック実装クラス
 *
 * @author kimura
 */
@Component
public class TradedCardLogicImpl extends AbstractShopLogic implements TradedCardLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(TradedCardLogicImpl.class);

    /** ＰＧカード決済サービスクライアント */
    private final PaymentClient paymentClient;

    /** カードブランド情報取得ロジック */
    private final CardBrandGetLogic cardBrandGetLogic;

    /** MAXカードブランドSEQ取得ロジック */
    private final CardBrandGetMaxCardBrandSeqLogic cardBrandGetMaxCardBrandSeqLogic;

    /** カードブランド情報登録ロジック */
    private final CardBrandRegistLogic cardBrandRegistLogic;

    /** マルペイUtility取得 */
    private final MulPayUtility mulPayUtility;

    @Autowired
    public TradedCardLogicImpl(PaymentClient paymentClient,
                               CardBrandGetLogic cardBrandGetLogic,
                               CardBrandGetMaxCardBrandSeqLogic cardBrandGetMaxCardBrandSeqLogic,
                               CardBrandRegistLogic cardBrandRegistLogic,
                               MulPayUtility mulPayUtility) {
        this.paymentClient = paymentClient;
        this.cardBrandGetLogic = cardBrandGetLogic;
        this.cardBrandGetMaxCardBrandSeqLogic = cardBrandGetMaxCardBrandSeqLogic;
        this.cardBrandRegistLogic = cardBrandRegistLogic;
        this.mulPayUtility = mulPayUtility;
    }

    /**
     * 実行メソッド
     *
     * @param memberInfoSeq 会員SEQ
     * @param orderId       受注番号
     * @return カード参照出力パラメータ
     */
    @Override
    public TradedCardOutput execute(Integer memberInfoSeq, String orderId) {

        // 決済代行会社会員IDの取得
        String paymentMemberId = this.mulPayUtility.createPaymentMemberId(memberInfoSeq);

        TradedCardOutput res = new TradedCardOutput();

        HmTradedCardInput input = new HmTradedCardInput();

        // 会員 ID
        input.setMemberId(paymentMemberId);

        // オーダーID
        input.setOrderId(orderId);

        // カード登録連番モード
        input.setSeqMode(HmPaymentClientInput.SEQ_MODE_LOGICAL);

        // 名義人
        input.setHolderName(null);

        try {
            res = this.paymentClient.doTradedCard(input);
        } catch (PaymentException pe) {
            LOGGER.error("例外処理が発生しました", pe);
            // 呼び元でエラーハンドリング実施
            throwMessage(MSGCD_PAYMENT_COM_FAIL, null, pe);
        }

        if (!res.isErrorOccurred()) {

            // カードブランド登録
            registCardBrand(res.getForward());

        }

        return res;
    }

    /**
     * カードブランド登録
     * GMOからの戻り値：カード会社コードがカードブランドに登録されていない場合、
     * 不明カード会社としてダミーデータを登録する
     *
     * @param forward GMOから帰ってきたクレジットカード会社コード
     */
    protected void registCardBrand(String forward) {
        // クレジットカード会社コード取得
        String cardBrandCode = forward;

        // クレジットカード会社コードからカードブランド情報を取得
        CardBrandEntity cardBrandEntity = this.cardBrandGetLogic.execute(cardBrandCode);
        // カードブランド情報登録済みの場合は処理終了
        if (cardBrandEntity != null) {
            return;
        }

        // カードブランドエンティティを作成
        cardBrandEntity = getComponent(CardBrandEntity.class);

        // MAXカードブランドSEQ取得
        int cardBrandSeq = this.cardBrandGetMaxCardBrandSeqLogic.execute();

        // カードブランドSEQ
        cardBrandEntity.setCardBrandSeq(++cardBrandSeq);
        // クレジットカード会社コード
        cardBrandEntity.setCardBrandCode(cardBrandCode);
        // カードブランド名
        String cardBrandName = PropertiesUtil.getLabelPropertiesValue("auto.regist.cardbrandname");
        // カードブランド名よりクレジットカード会社コードの桁数が大きくなる可能性があるため
        // カードブランドSEQを付与
        cardBrandEntity.setCardBrandName(cardBrandName + cardBrandSeq);
        // カードブランド表示名PC
        cardBrandEntity.setCardBrandDisplayPc(cardBrandName + cardBrandSeq);
        // 全角、半角の変換Helper取得
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

        // カードブランド情報登録ロジック実行
        this.cardBrandRegistLogic.execute(cardBrandEntity);
    }
}