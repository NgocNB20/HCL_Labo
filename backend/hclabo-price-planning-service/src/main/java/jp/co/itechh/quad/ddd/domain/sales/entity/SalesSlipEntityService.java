/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.entity;

import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCouponService;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 販売伝票エンティティ ドメインサービス
 */
@Service
public class SalesSlipEntityService {

    /** クーポン値オブジェクトドメインサービス */
    private final ApplyCouponService applyCouponService;

    /**
     * コンストラクタ
     *
     * @param applyCouponService クーポン値オブジェクトドメインサービス
     */
    @Autowired
    public SalesSlipEntityService(ApplyCouponService applyCouponService) {
        this.applyCouponService = applyCouponService;
    }

    /**
     * クーポン適用
     *
     * @param couponCode クーポンコード
     * @param entity 販売伝票
     */
    public void applyCoupon(String couponCode, SalesSlipEntity entity) {

        // アサートチェック
        AssertChecker.assertNotNull("salesSlipEntity is null", entity);

        // チェック
        // 下書き状態でないならエラー
        if (entity.getSalesStatus() != SalesStatus.DRAFT) {
            throw new DomainException("PRICE-PLANNING-SALS0001-E");
        }

        // クーポン適用
        ApplyCoupon applyCoupon = applyCouponService.getApplyCoupon(entity, couponCode);
        if (applyCoupon != null) {
            entity.settingCoupon(applyCoupon);
        }
    }

    /**
     * 販売伝票チェック
     *
     * @param entity 販売伝票
     */
    public void checkSalesSlip(SalesSlipEntity entity) {

        // アサートチェック
        AssertChecker.assertNotNull("salesSlipEntity is null", entity);

        // 商品金額合計チェック
        Integer goodsPriceTotal = entity.getItemPurchasePriceTotal();
        if (goodsPriceTotal <= 0) {
            throw new DomainException("PRICE-PLANNING-IPPT0002-E");
        }

        // クーポン妥当性チェック
        applyCouponService.checkApplyCoupon(entity);
    }

}
