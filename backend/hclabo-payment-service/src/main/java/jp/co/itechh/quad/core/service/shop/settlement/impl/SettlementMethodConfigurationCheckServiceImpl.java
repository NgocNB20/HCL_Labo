/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement.impl;

import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodCheckLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodConfigurationCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 決済方法設定チェックサービス
 *
 * 設定可能な決済方法であるかをチェックします。
 *
 * @author YAMAGUCHI
 *
 */
@Service
public class SettlementMethodConfigurationCheckServiceImpl extends AbstractShopService
                implements SettlementMethodConfigurationCheckService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementMethodConfigurationCheckServiceImpl.class);

    /**
     * 決済方法チェックロジック
     */
    private final SettlementMethodCheckLogic settlementMethodCheckLogic;

    @Autowired
    public SettlementMethodConfigurationCheckServiceImpl(SettlementMethodCheckLogic settlementMethodCheckLogic) {
        this.settlementMethodCheckLogic = settlementMethodCheckLogic;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodDto 決済方法DTO
     */
    @Override
    public void execute(SettlementMethodDto settlementMethodDto) {

        // 決済方法エンティティ取得
        SettlementMethodEntity settlementMethodEntity = getSettlementMethodEntity(settlementMethodDto);

        // 手数料設定チェック
        HTypeSettlementMethodPriceCommissionFlag settlementMethodPriceCommissionFlag =
                        settlementMethodEntity.getSettlementMethodPriceCommissionFlag();
        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT == settlementMethodPriceCommissionFlag) {
            // 金額別手数料チェック
            checkByCommissionTypeForEachAmount(settlementMethodDto);
        } else {
            BigDecimal commission = settlementMethodEntity.getEqualsCommission();
            // 手数料入力チェック
            checkCommission(commission);

            if (settlementMethodEntity.getSettlementMethodType() != HTypeSettlementMethodType.LINK_PAYMENT) {
                BigDecimal maxPrice = settlementMethodEntity.getMaxPurchasedPrice();
                HTypeSettlementMethodCommissionType commissionType =
                                settlementMethodEntity.getSettlementMethodCommissionType();

                // 利用可能最大金額チェック
                checkMaxPrice(maxPrice);

                // 一律手数料種別毎チェック
                checkByCommissionTypeForFlat(maxPrice, commission, commissionType);
            }
        }

        // 決済方法チェック
        checkSettlementMethod(settlementMethodEntity);

        if (hasErrorMessage()) {
            throwMessage();
        }

    }

    /**
     * 決済方法エンティティ取得
     *
     * @param settlementMethodDto 決済方法DTO
     * @param customParams 案件用引数
     * @return 決済方法エンティティ
     */
    protected SettlementMethodEntity getSettlementMethodEntity(SettlementMethodDto settlementMethodDto,
                                                               Object... customParams) {
        SettlementMethodEntity settlementMethodEntity = settlementMethodDto.getSettlementMethodEntity();
        settlementMethodEntity.setShopSeq(1001);
        return settlementMethodEntity;
    }

    /**
     * 金額別手数料チェック
     *
     * @param settlementMethodDto 決済方法DTO
     * @param customParams 案件用引数
     */
    protected void checkByCommissionTypeForEachAmount(SettlementMethodDto settlementMethodDto, Object... customParams) {
        List<SettlementMethodPriceCommissionEntity> list =
                        settlementMethodDto.getSettlementMethodPriceCommissionEntityList();
        if (list == null || list.isEmpty()) {
            this.addErrorMessage(MSGCD_COMMISSION_NO_SET);
        }
    }

    /**
     * 手数料チェック
     *
     * @param commission 手数料
     * @param customParams 案件用引数
     */
    protected void checkCommission(BigDecimal commission, Object... customParams) {
        if (commission == null) {
            this.addErrorMessage(MSGCD_EQUALS_COMMISSION_NO_SET);
        }
    }

    /**
     * 利用可能最大金額チェック
     *
     * @param maxPrice 利用可能最大金額
     * @param customParams 案件用引数
     */
    protected void checkMaxPrice(BigDecimal maxPrice, Object... customParams) {
        if (maxPrice == null) {
            this.addErrorMessage(MSGCD_MAX_PURCHASED_PRICE_NO_SET);
        } else if (BigDecimal.ZERO.compareTo(maxPrice) >= 0) {
            this.addErrorMessage(MSGCD_MAX_PURCHASED_PRICE_ZERO);
        }
    }

    /**
     * 一律手数料種別毎のチェック
     *
     * @param maxPrice 利用可能最大金額
     * @param commission 手数料
     * @param commissionType 手数料種別
     * @param customParams 案件用引数
     */
    protected void checkByCommissionTypeForFlat(BigDecimal maxPrice,
                                                BigDecimal commission,
                                                HTypeSettlementMethodCommissionType commissionType,
                                                Object... customParams) {
        // 手数料種別＝一律(円)の場合のチェック
        if (HTypeSettlementMethodCommissionType.FLAT_YEN.equals(commissionType) && commission != null
            && maxPrice != null && commission.compareTo(maxPrice) >= 0) {
            this.addErrorMessage(MSGCD_COMMISSION_ORVER_MAX_PURCHASED_PRICE);
        }
        // 手数料種別＝一律(％)の場合のチェック
        // if
        // (HTypeSettlementMethodCommissionType.FLAT_PERCENTAGE.equals(commissionType)
        // && commission != null && commission.compareTo(new BigDecimal(100)) >=
        // 0) {
        // this.addErrorMessage(MSGCD_COMMISSION_ORVER_MAX_PERCENTAGE_PRICE);
        // }
    }

    /**
     * 決済方法チェック
     *
     * @param settlementMethodEntity 決済方法エンティティ
     * @param customParams 案件用引数
     */
    protected void checkSettlementMethod(SettlementMethodEntity settlementMethodEntity, Object... customParams) {
        try {
            settlementMethodCheckLogic.execute(settlementMethodEntity);
        } catch (AppLevelListException e) {
            LOGGER.error("例外処理が発生しました", e);
            this.addErrorMessage(e);
        }
    }

}