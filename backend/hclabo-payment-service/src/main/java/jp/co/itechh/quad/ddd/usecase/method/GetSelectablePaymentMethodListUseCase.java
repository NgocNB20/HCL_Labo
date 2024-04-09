/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.method;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.method.proxy.PaymentMethodProxyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 選択可能決済方法一覧取得 ユースケース
 */
@Service
public class GetSelectablePaymentMethodListUseCase {

    /** 決済方法プロキシサービス */
    private final PaymentMethodProxyService paymentMethodProxyService;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 分割文字（カンマ：,） */
    public static final String CHAR_COMMA = ",";

    /** コンストラクタ */
    @Autowired
    public GetSelectablePaymentMethodListUseCase(PaymentMethodProxyService paymentMethodProxyService,
                                                 ConversionUtility conversionUtility) {
        this.paymentMethodProxyService = paymentMethodProxyService;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 選択可能決済方法一覧を取得する
     *
     * @param transactionId 取引ID
     * @return 存在する ... 選択可能決済方法一覧取得ユースケースDtoリスト / 存在しない ... null
     */
    public List<GetSelectablePaymentMethodListUseCaseDto> getSelectablePaymentMethodList(String transactionId) {
        return toUseCaseDtoList(paymentMethodProxyService.getSelectablePaymentMethodList());
    }

    /**
     * ユースケースDtoリストへの変換処理
     *
     * @param settlementDtoList 決済Dtoリスト
     * @return 選択可能決済方法一覧取得ユースケースDtoリスト
     */
    private List<GetSelectablePaymentMethodListUseCaseDto> toUseCaseDtoList(List<SettlementDto> settlementDtoList) {

        if (CollectionUtils.isEmpty(settlementDtoList)) {
            return null;
        }

        List<GetSelectablePaymentMethodListUseCaseDto> returnList = new ArrayList<>();

        for (SettlementDto settlementDto : settlementDtoList) {

            GetSelectablePaymentMethodListUseCaseDto dto = new GetSelectablePaymentMethodListUseCaseDto();
            SettlementDetailsDto detailsDto = settlementDto.getSettlementDetailsDto();

            dto.setPaymentMethodId(conversionUtility.toString(detailsDto.getSettlementMethodSeq()));
            dto.setPaymentMethodName(detailsDto.getSettlementMethodName());
            dto.setPaymentMethodNote(StringUtils.defaultString(detailsDto.getSettlementNotePC()));
            dto.setBillingType(EnumTypeUtil.getValue(detailsDto.getBillType()));
            dto.setSettlementMethodType(EnumTypeUtil.getValue(detailsDto.getSettlementMethodType()));
            dto.setEnableRevolvingFlag(detailsDto.getEnableRevolving() == HTypeEffectiveFlag.VALID);
            dto.setEnableInstallmentFlag(detailsDto.getEnableInstallment() == HTypeEffectiveFlag.VALID);
            dto.setInstallmentCounts(createDividedNumberItems(settlementDto.getCardBrandEntityList()));

            returnList.add(dto);
        }

        return returnList;
    }

    /**
     * お支払い回数プルダウンを作成
     *
     * @param cardBrandEntityList カードブランド
     * @return クレジット選択可能分割回数
     */
    private String createDividedNumberItems(List<CardBrandEntity> cardBrandEntityList) {

        if (CollectionUtils.isEmpty(cardBrandEntityList)) {
            return null;
        }

        // 分割回数の値を数値形式でソート
        Map<Integer, String> sortMap = new TreeMap<>();
        for (CardBrandEntity cardBrandEntity : cardBrandEntityList) {
            if (cardBrandEntity.getInstallmentCounts() == null) {
                continue;
            }
            String[] installmentCounts = cardBrandEntity.getInstallmentCounts().split(CHAR_COMMA);
            for (String installmentCount : installmentCounts) {
                sortMap.put(Integer.parseInt(installmentCount), installmentCount);
            }
        }

        Map<String, String> dividedNumberMap = new LinkedHashMap<>();
        for (String value : sortMap.values()) {
            dividedNumberMap.put(value, value);
        }

        StringBuilder installmentCounts = new StringBuilder();
        for (String installmentCount : dividedNumberMap.values()) {
            installmentCounts.append(installmentCount);
            if (installmentCounts.length() != dividedNumberMap.size()) {
                installmentCounts.append(CHAR_COMMA);
            }
        }

        return installmentCounts.toString();
    }

}
