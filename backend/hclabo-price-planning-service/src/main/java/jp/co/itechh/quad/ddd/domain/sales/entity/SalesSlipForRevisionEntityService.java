/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.entity;

import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCouponService;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.salesslip.presentation.api.param.WarningContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 改訂用販売伝票エンティティ ドメインサービス
 */
@Service
public class SalesSlipForRevisionEntityService {

    /**
     * 処理件数マップのキー ワーニングメッセージ
     * <code>WARNING_MESSAGE</code>
     */
    private static final String WARNING_MESSAGE = "WarningMessage";

    /** クーポン値オブジェクトドメインサービス */
    private final ApplyCouponService applyCouponService;

    /** 受注アダプター */
    private final IOrderReceivedAdapter orderReceivedAdapter;

    /**
     * コンストラクタ
     *
     * @param applyCouponService クーポン値オブジェクトドメインサービス
     * @param orderReceivedAdapter 受注アダプター
     */
    @Autowired
    public SalesSlipForRevisionEntityService(ApplyCouponService applyCouponService,
                                             IOrderReceivedAdapter orderReceivedAdapter) {
        this.applyCouponService = applyCouponService;
        this.orderReceivedAdapter = orderReceivedAdapter;
    }

    /**
     * 改訂用販売伝票チェック
     *
     * @param entity 改訂用販売伝票
     * @return warningMessageForResponse
     */
    public Map<String, List<WarningContent>> checkSalesSlipForRevision(SalesSlipForRevisionEntity entity) {

        // アサートチェック
        AssertChecker.assertNotNull("salesSlipForRevisionEntity is null", entity);

        // 商品金額合計チェック
        Integer goodsPriceTotal = entity.getItemPurchasePriceTotal();
        if (goodsPriceTotal <= 0) {
            throw new DomainException("PRICE-PLANNING-IPPT0002-E");
        }

        // クーポン妥当性チェック
        AppLevelFacesMessage warnMessage = applyCouponService.checkApplyCoupon(entity);

        // APIレスポンス用の警告メッセージ設定
        return settingWarningMessage(warnMessage);
    }

    /**
     * 改訂前手数料/送料適用フラグ設定
     *
     * @param originCommissionApplyFlag 改訂前手数料適用フラグ
     * @param originCarriageApplyFlag 改訂前送料適用フラグ
     * @param salesSlipForRevisionEntity 改訂用販売伝票
     */
    public void settingOriginCommissionAndCarriageApplyFlag(boolean originCommissionApplyFlag,
                                                            boolean originCarriageApplyFlag,
                                                            SalesSlipForRevisionEntity salesSlipForRevisionEntity) {

        // チェック
        OrderReceived orderReceived = orderReceivedAdapter.get(salesSlipForRevisionEntity.getTransactionId());
        // 受注情報が存在しない場合はエラー
        if (orderReceived == null) {
            throw new DomainException(
                            "PRICE-PLANNING-ORDS0001-E", new String[] {salesSlipForRevisionEntity.getTransactionId()});
        }

        salesSlipForRevisionEntity.setOriginCommissionApplyFlag(originCommissionApplyFlag);
        salesSlipForRevisionEntity.setOriginCarriageApplyFlag(originCarriageApplyFlag);
    }

    /**
     * APIレスポンス用の警告メッセージ設定
     *
     * @param warnMessage 警告メッセージ
     * @return warningMessageForResponse
     */
    private Map<String, List<WarningContent>> settingWarningMessage(AppLevelFacesMessage warnMessage) {

        Map<String, List<WarningContent>> warningMessageMap = new HashMap<>();

        if (warnMessage == null) {
            return warningMessageMap;
        }

        List<WarningContent> warningContentList = new ArrayList<>();

        WarningContent warningContent = new WarningContent();
        warningContent.setCode(warnMessage.getMessageCode());
        warningContent.setMessage(warnMessage.getMessage());

        warningContentList.add(warningContent);
        warningMessageMap.put(WARNING_MESSAGE, warningContentList);

        return warningMessageMap;
    }

}