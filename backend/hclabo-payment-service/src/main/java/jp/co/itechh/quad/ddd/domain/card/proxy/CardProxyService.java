/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.card.proxy;

import com.gmo_pg.g_pay.client.output.DeleteCardOutput;
import com.gmo_pg.g_pay.client.output.SearchCardOutput;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.multipayment.CardDto;
import jp.co.itechh.quad.core.logic.multipayment.communicate.DeleteCardLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.SaveMemberLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.SearchCardLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.TradedCardLogic;
import jp.co.itechh.quad.core.utility.MulPayUtility;
import jp.co.itechh.quad.ddd.domain.card.valueobject.CreditExpirationDate;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * クレジットカードプロキシサービス
 */
@Service
public class CardProxyService {

    /** カード参照ロジック */
    private final SearchCardLogic searchCardLogic;

    /** カード削除ロジック */
    public DeleteCardLogic deleteCardLogic;

    /** 決済後カード登録ロジック */
    private TradedCardLogic tradedCardLogic;

    /** 会員登録ロジック */
    private SaveMemberLogic saveMemberLogic;

    /** マルチペイメントHelper */
    private final MulPayUtility mulPayUtility;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public CardProxyService(SearchCardLogic searchCardLogic,
                            DeleteCardLogic deleteCardLogic,
                            TradedCardLogic tradedCardLogic,
                            SaveMemberLogic saveMemberLogic,
                            MulPayUtility mulPayUtility,
                            ConversionUtility conversionUtility) {
        this.searchCardLogic = searchCardLogic;
        this.deleteCardLogic = deleteCardLogic;
        this.tradedCardLogic = tradedCardLogic;
        this.saveMemberLogic = saveMemberLogic;
        this.mulPayUtility = mulPayUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 登録済みカード情報を取得する<br/>
     * ※フェーズ1のSearchCardLogicを流用
     *
     * @param customerId 顧客ID
     * @return SearchCardOutput  カード情報照会結果
     */
    public SearchCardOutput getCardInfo(String customerId) {

        // 顧客IDのチェック
        AssertChecker.assertNotEmpty("customerId is empty", customerId);

        // カード情報を参照する
        SearchCardOutput resultCard = this.searchCardLogic.execute(
                        this.mulPayUtility.createPaymentMemberId(this.conversionUtility.toInteger(customerId)));

        // 照会結果にカード情報が存在しない場合、処理をスキップ
        if (resultCard == null || CollectionUtil.isEmpty(resultCard.getCardList())) {
            return null;
        }

        return resultCard;
    }

    /**
     * 登録済みカード情報を削除する<br/>
     * ※フェーズ1のDeleteCardLogicを流用
     *
     * @param customerId 顧客ID
     */
    public void deleteCardInfo(String customerId) {

        // 顧客IDのチェック
        AssertChecker.assertNotEmpty("customerId is empty", customerId);

        // カード情報が登録されているか確認し、存在しない場合は処理をスキップする
        if (getCardInfo(customerId) == null) {
            return;
        }

        // カード情報を作成する
        CardDto cardDto = toCardDtoForDelete(customerId);

        // カード削除処理
        DeleteCardOutput result = deleteCardLogic.execute(cardDto, true);

        if (result == null) {
            throw new DomainException("PAYMENT_CPRS0001-E");
        }
        if (result.isErrorOccurred()) {
            throw new DomainException("PAYMENT_CPRS0001-E");
        }
    }

    /**
     * 決済後カード登録更新<br/>
     * ※フェーズ1のDeleteCardLogic、SaveMemberLogic、TradedCardLogicを流用<br/>
     * カード番号では登録不可。カード番号＋有効期限で生成されたトークンで登録
     * <p>
     * TODO 各種フェーズ１のGMOロジックは、呼び出し元でエラーハンドリングをしている。このあたり注文フロー（取引）の途中なのでエラー検討が必要
     *
     * @param customerId          顧客ID
     * @param expirationDate      有効期限
     * @param token               トークン
     * @param registCreditFlag    登録済みカードフラグ
     * @param useRegistedCardFlag 登録済みカード使用フラグ
     * @param orderCode           受注番号
     */
    public void saveTradedCardInfo(String customerId,
                                   CreditExpirationDate expirationDate,
                                   String token,
                                   boolean registCreditFlag,
                                   boolean useRegistedCardFlag,
                                   String orderCode) {

        // 顧客IDのチェック
        AssertChecker.assertNotEmpty("customerId is empty", customerId);
        // トークンのチェック
        AssertChecker.assertNotEmpty("token is empty", token);
        // 取引IDのチェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);

        // カード情報を作成する
        CardDto cardDto = toCardDtoForSave(customerId, expirationDate, token, registCreditFlag);

        // カード情報を取得する
        SearchCardOutput resultCard = getCardInfo(customerId);

        // カード登録状態のチェック
        // 登録済みカードが存在するが、「今回は使用しない」かつ「別のカードを登録」する場合
        // 登録済みカードを削除する
        if (resultCard != null && !useRegistedCardFlag && registCreditFlag) {
            // cardDtoの「会員SEQ」「決済代行会員ID」のみ利用
            this.deleteCardLogic.execute(cardDto, true);
        }

        // 登録済みカードが存在せず、カードを登録する場合
        // マルチペイメント会員登録を行う（呼び出し先で会員照会チェックも実行）
        if (resultCard == null && registCreditFlag) {
            this.saveMemberLogic.execute(cardDto);
        }
        // 今回は「登録済みカードを使用しない」かつ「カードを登録」する場合
        // 決済で利用したカードを新たに登録する
        if (!useRegistedCardFlag && registCreditFlag) {
            this.tradedCardLogic.execute(this.conversionUtility.toInteger(customerId), orderCode);
        }
    }

    /**
     * カード入力情報を登録更新用のカードDtoに変換<br/>
     *
     * @param customerId       顧客ID
     * @param expirationDate   有効期限
     * @param token            トークン
     * @param registCreditFlag 登録済みカードフラグ
     * @return CardDto カード情報
     */
    private CardDto toCardDtoForSave(String customerId,
                                     CreditExpirationDate expirationDate,
                                     String token,
                                     boolean registCreditFlag) {

        CardDto cardDto = ApplicationContextUtility.getBean(CardDto.class);

        // 会員SEQ
        cardDto.setMemberInfoSeq(this.conversionUtility.toInteger(customerId));
        // 決済代行会員ID
        cardDto.setPaymentMemberId(
                        this.mulPayUtility.createPaymentMemberId(this.conversionUtility.toInteger(customerId)));
        // 有効期限
        cardDto.setExpirationDate(expirationDate.getExpirationYear() + expirationDate.getExpirationMonth());
        // トークン
        cardDto.setToken(token);
        // クレジットカード情報保持状態を移送
        cardDto.setRegistCredit(registCreditFlag);

        return cardDto;
    }

    /**
     * カード入力情報を削除用のカードDtoに変換<br/>
     *
     * @param customerId 顧客ID
     * @return CardDto カード情報
     */
    private CardDto toCardDtoForDelete(String customerId) {

        CardDto cardDto = ApplicationContextUtility.getBean(CardDto.class);

        // 会員SEQ
        cardDto.setMemberInfoSeq(this.conversionUtility.toInteger(customerId));
        // 決済代行会員ID
        cardDto.setPaymentMemberId(
                        this.mulPayUtility.createPaymentMemberId(this.conversionUtility.toInteger(customerId)));

        return cardDto;
    }

}