/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate.impl;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.SaveMemberOutput;
import com.gmo_pg.g_pay.client.output.SearchMemberOutput;
import jp.co.itechh.quad.core.dto.multipayment.CardDto;
import jp.co.itechh.quad.core.dto.multipayment.HmSaveMemberInput;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.SaveMemberLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.SearchMemberLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * マルチペイメント会員登録通信ロジック実装クラス
 *
 * @author kimura
 */
@Component
public class SaveMemberLogicImpl extends AbstractShopLogic implements SaveMemberLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveMemberLogicImpl.class);

    /** ＰＧカード決済サービスクライアント */
    private final PaymentClient paymentClient;

    /** 会員照会Logic */
    private final SearchMemberLogic searchMemberLogic;

    @Autowired
    public SaveMemberLogicImpl(PaymentClient paymentClient, SearchMemberLogic searchMemberLogic) {
        this.paymentClient = paymentClient;
        this.searchMemberLogic = searchMemberLogic;
    }

    /**
     * 実行メソッド<br />
     *
     * @param cardDto カードDto
     * @return 会員登録出力パラメータ
     */
    @Override
    // @RequiresNewTx
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SaveMemberOutput execute(CardDto cardDto) {

        // 既に登録済みの場合は処理しない。
        if (isRegistedMember(cardDto.getPaymentMemberId())) {
            return null;
        }

        SaveMemberOutput res = new SaveMemberOutput();
        HmSaveMemberInput input = new HmSaveMemberInput();

        // 会員ID
        input.setMemberId(cardDto.getPaymentMemberId());
        // 会員名
        input.setMemberName(null);

        try {
            res = paymentClient.doSaveMember(input);
        } catch (PaymentException pe) {
            LOGGER.error("例外処理が発生しました", pe);
            // 呼び元でエラーハンドリング実施
            throwMessage(MSGCD_PAYMENT_COM_FAIL, null, pe);
        }

        return res;
    }

    /**
     * 会員が既に登録されているか確認する<br/>
     *
     * @param paymentMemberId 決済代行会員ID
     * @return true:登録済み false:未登録
     */
    protected boolean isRegistedMember(String paymentMemberId) {
        // 会員照会
        SearchMemberOutput res = searchMemberLogic.execute(paymentMemberId);

        if (res.getMemberList().isEmpty()) {
            return false;
        }
        return true;
    }
}