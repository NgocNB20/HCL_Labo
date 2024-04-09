/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate.impl;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.DeleteCardOutput;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.dto.multipayment.CardDto;
import jp.co.itechh.quad.core.dto.multipayment.HmDeleteCardInput;
import jp.co.itechh.quad.core.dto.multipayment.HmPaymentClientInput;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.DeleteCardLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カード削除通信ロジック実装クラス
 *
 * @author kimura
 */
@Component
public class DeleteCardLogicImpl extends AbstractShopLogic implements DeleteCardLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCardLogicImpl.class);

    /** PGカード決済サービスクライアント */
    private final PaymentClient paymentClient;

    @Autowired
    public DeleteCardLogicImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * 実行メソッド
     *
     * @param cardDto      カードDto
     * @param dbUpdateFlag DB更新フラグ true:カード情報変更後会員情報TBLを更新します false:更新しません ※未使用
     * @return 結果
     */
    @Override
    public DeleteCardOutput execute(CardDto cardDto, boolean dbUpdateFlag) {

        // GMOに会員登録していなければ処理終了
        if (!isRegistedMember(cardDto)) {
            return null;
        }
        HmDeleteCardInput input = new HmDeleteCardInput();
        DeleteCardOutput output = new DeleteCardOutput();
        // 会員ID
        input.setMemberId(cardDto.getPaymentMemberId());
        // カード登録連番モード
        input.setSeqMode(HmPaymentClientInput.SEQ_MODE_LOGICAL);
        // カード登録連番
        input.setCardSeq(HmPaymentClientInput.CARD_SEQ);

        try {
            output = paymentClient.doDeleteCard(input);
        } catch (PaymentException pe) {
            LOGGER.error("例外処理が発生しました", pe);
            // 呼び元でエラーハンドリング実施
            throwMessage(MSGCD_PAYMENT_COM_FAIL, null, pe);
        }

        return output;
    }

    /**
     * 会員が既に登録されているか確認する<br/>
     *
     * @param cardDto カードDto
     * @return true:登録 false:未登録
     */
    protected boolean isRegistedMember(CardDto cardDto) {
        // 決済代行会員IDが未設定の場合処理終了
        if (StringUtil.isEmpty(cardDto.getPaymentMemberId())) {
            return false;
        }

        return true;
    }

}
