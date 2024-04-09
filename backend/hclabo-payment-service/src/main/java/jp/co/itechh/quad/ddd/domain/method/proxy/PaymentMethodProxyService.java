/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.method.proxy;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dao.shop.settlement.SettlementMethodDao;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;
import jp.co.itechh.quad.core.logic.multipayment.CardBrandGetLogic;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 決済方法 プロキシサービス
 */
@Service
public class PaymentMethodProxyService {

    /** 決済方法Dao */
    private final SettlementMethodDao settlementMethodDao;

    /** カードブランド情報取得Logic */
    private final CardBrandGetLogic cardBrandGetLogic;

    /** コンストラクタ */
    @Autowired
    public PaymentMethodProxyService(SettlementMethodDao settlementMethodDao, CardBrandGetLogic cardBrandGetLogic) {
        this.settlementMethodDao = settlementMethodDao;
        this.cardBrandGetLogic = cardBrandGetLogic;
    }

    /**
     * 選択可能決済方法を取得する
     *
     * @return 決済Dtoリスト
     */
    public List<SettlementDto> getSelectablePaymentMethodList() {

        // 決済方法Dao用検索条件Dtoを作成 ※公開中のみ指定
        SettlementSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(SettlementSearchForDaoConditionDto.class);
        conditionDto.setOpenStatusPC(HTypeOpenDeleteStatus.OPEN);

        // 決済詳細Dtoリストを取得
        List<SettlementDetailsDto> settlementDetailsList =
                        settlementMethodDao.getSearchSettlementDetailsList(conditionDto);
        if (CollectionUtils.isEmpty(settlementDetailsList)) {
            return null;
        }

        List<SettlementDto> settlementList = new ArrayList<>();
        for (SettlementDetailsDto settlementDetails : settlementDetailsList) {

            // 決済Dtoに変換 ※画面側に返す項目のみ
            SettlementDto settlementDto = createSettlementDto(settlementDetails);

            // カードブランド情報をセット ※分割回数取得のために必要
            // TODO 現状は全クレジットカードで結果固定だが、将来ブランド指定で分かれそうなので、ループ内で回している
            if (settlementDetails.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {
                setCardBrandInfo(settlementDto);
            }

            settlementList.add(settlementDto);
        }

        return settlementList;
    }

    /**
     * 決済Dto作成 ※画面側に返す項目のみ利用
     *
     * @param settlementDetailsDto 決済詳細Dto
     * @return 決済Dto
     */
    private SettlementDto createSettlementDto(SettlementDetailsDto settlementDetailsDto) {
        SettlementDto settlementDto = ApplicationContextUtility.getBean(SettlementDto.class);
        settlementDto.setSettlementDetailsDto(settlementDetailsDto);
        return settlementDto;
    }

    /**
     * カードブランド情報の設定
     *
     * @param settlementDto 決済Dto
     */
    private void setCardBrandInfo(SettlementDto settlementDto) {
        List<CardBrandEntity> cardBrandList = cardBrandGetLogic.execute(true);
        settlementDto.setCardBrandEntityList(cardBrandList);
    }

    /**
     * 手数料を取得する
     *
     * @param paymentMethodId             決済方法ID（決済方法SEQ）
     * @param priceForLargeAmountDiscount 計算対象金額（一律手数料高額割引）
     * @param priceForPriceCommission     計算対象金額（金額別手数料）
     * @return 手数料 ※算出不可能な場合、null
     */
    public Integer getCommission(String paymentMethodId,
                                 Integer priceForLargeAmountDiscount,
                                 Integer priceForPriceCommission) {

        // 入力チェック
        AssertChecker.assertNotEmpty("paymentMethodId is empty", paymentMethodId);
        AssertChecker.assertNotNull("priceForLargeAmountDiscount is null", priceForLargeAmountDiscount);
        AssertChecker.assertNotNull("priceForPriceCommission is null", priceForPriceCommission);

        // 決済詳細Dtoを取得
        SettlementDetailsDto settlementDetails =
                        settlementMethodDao.getSettlementDetails(Integer.valueOf(paymentMethodId),
                                                                 BigDecimal.valueOf(priceForPriceCommission)
                                                                );
        if (settlementDetails == null) {
            return null;
        }

        // 金額別手数料対象区間なしの場合、画面にはハイフン表示 ※nullリターン
        if (!checkPriceCommission(settlementDetails)) {
            return null;
        }

        return getCommission(settlementDetails, priceForLargeAmountDiscount);
    }

    /**
     * 金額別手数料対象区間なしチェック
     *
     * @param settlementDetails 決済詳細Dto
     * @return true:対象区間あり or 金額別手数料ではない, false:対象区間なし
     */
    private boolean checkPriceCommission(SettlementDetailsDto settlementDetails) {

        HTypeSettlementMethodCommissionType commissionType = settlementDetails.getSettlementMethodCommissionType();

        // 金額別手数料の場合のみチェック
        if (commissionType != HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN) {
            return true;
        }

        // SQL実行時点で、パラメータのpriceForPriceCommissionに最も近い区間が取得されている
        // この区間が取得されている場合のみ、決済詳細DtoのmaxPriceとcommissionが設定されている
        // 該当区間がない場合、上記2つの値が設定されていない
        return (settlementDetails.getCommission() != null);
    }

    /**
     * 種別に合わせて手数料を取得
     *
     * @param settlementDetails           決済詳細Dto
     * @param priceForLargeAmountDiscount 計算対象金額（一律手数料高額割引）
     * @return 手数料 ※算出不可能な場合、null
     */
    private Integer getCommission(SettlementDetailsDto settlementDetails, Integer priceForLargeAmountDiscount) {

        HTypeSettlementMethodCommissionType commissionType = settlementDetails.getSettlementMethodCommissionType();

        // 金額別手数料
        if (commissionType == HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN) {
            return settlementDetails.getCommission().intValueExact();
        }

        // 一律手数料
        if (commissionType == HTypeSettlementMethodCommissionType.FLAT_YEN) {

            BigDecimal discountPrice = settlementDetails.getLargeAmountDiscountPrice();
            BigDecimal judgePrice = BigDecimal.valueOf(priceForLargeAmountDiscount);

            if (discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) > 0
                && judgePrice.compareTo(discountPrice) >= 0) {
                // 高額割引適用
                BigDecimal discountCommission = settlementDetails.getLargeAmountDiscountCommission();
                return discountCommission == null ? null : discountCommission.intValueExact();
            } else {
                // 通常
                return settlementDetails.getEqualsCommission().intValueExact();
            }
        }

        // その他区分：未対応
        return null;
    }

}
