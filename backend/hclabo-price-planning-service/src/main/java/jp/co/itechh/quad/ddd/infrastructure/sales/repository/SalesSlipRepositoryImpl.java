/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.sales.repository;

import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipId;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesStatus;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.AdjustmentAmountDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.ItemPurchasePriceSubTotalDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.SalesSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.SalesSlipDbEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 販売伝票リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class SalesSlipRepositoryImpl implements ISalesSlipRepository {

    /** 販売伝票Daoクラス */
    private final SalesSlipDao salesSlipDao;

    /** 商品購入価格小計Daoクラス */
    private final ItemPurchasePriceSubTotalDao itemPurchasePriceSubTotalDao;

    /** 調整金額Daoクラス */
    private final AdjustmentAmountDao adjustmentAmountDao;

    /** 販売伝票リポジトリHelperクラス */
    private final SalesSlipRepositoryHelper salesSlipRepositoryHelper;

    /** クーポンDAO */
    private final CouponDao couponDao;

    /**
     * コンストラクタ
     *
     * @param salesSlipDao                 販売伝票Daoクラス
     * @param itemPurchasePriceSubTotalDao 商品購入価格小計Daoクラス
     * @param adjustmentAmountDao          クーポンDaoクラス
     * @param salesSlipRepositoryHelper    販売伝票リポジトリHelperクラス
     * @param couponDao                    クーポンDAO
     */
    @Autowired
    public SalesSlipRepositoryImpl(SalesSlipDao salesSlipDao,
                                   ItemPurchasePriceSubTotalDao itemPurchasePriceSubTotalDao,
                                   AdjustmentAmountDao adjustmentAmountDao,
                                   SalesSlipRepositoryHelper salesSlipRepositoryHelper,
                                   CouponDao couponDao) {
        this.salesSlipDao = salesSlipDao;
        this.itemPurchasePriceSubTotalDao = itemPurchasePriceSubTotalDao;
        this.adjustmentAmountDao = adjustmentAmountDao;
        this.salesSlipRepositoryHelper = salesSlipRepositoryHelper;
        this.couponDao = couponDao;
    }

    @Override
    public void save(SalesSlipEntity salesSlipEntity) {

        SalesSlipDbEntity salesSlipDbEntity = salesSlipRepositoryHelper.toSalesSlipDbEntity(salesSlipEntity);
        salesSlipDao.insert(salesSlipDbEntity);

        List<ItemPurchasePriceSubTotalDbEntity> itemPurchasePriceSubTotalDbEntity =
                        salesSlipRepositoryHelper.toItemPurchasePriceSubTotalDbEntityList(salesSlipEntity);
        itemPurchasePriceSubTotalDbEntity.forEach(e -> e.setSalesSlipId(salesSlipDbEntity.getSalesSlipId()));
        itemPurchasePriceSubTotalDbEntity.forEach(item -> {
            itemPurchasePriceSubTotalDao.insert(item);
        });

        // 調整金額
        List<AdjustmentAmountDbEntity> adjustmentAmountDbEntityList =
                        salesSlipRepositoryHelper.toAdjustmentAmountDbEntityList(salesSlipEntity);
        adjustmentAmountDbEntityList.forEach(adjustmentAmountDbEntity -> {
            adjustmentAmountDao.insert(adjustmentAmountDbEntity);
        });
    }

    @Override
    public int update(SalesSlipEntity salesSlipEntity) {

        SalesSlipDbEntity salesSlipDbEntity = this.salesSlipRepositoryHelper.toSalesSlipDbEntity(salesSlipEntity);
        int result = this.salesSlipDao.update(salesSlipDbEntity);

        List<ItemPurchasePriceSubTotalDbEntity> itemPurchasePriceSubTotalRequest =
                        this.salesSlipRepositoryHelper.toItemPurchasePriceSubTotalDbEntityList(salesSlipEntity);

        // 販売伝票IDにより分割した商品購入価格小計を削除する
        itemPurchasePriceSubTotalDao.deleteBySalesSlipId(salesSlipDbEntity.getSalesSlipId());

        for (ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotal : itemPurchasePriceSubTotalRequest) {
            itemPurchasePriceSubTotal.setSalesSlipId(salesSlipEntity.getSalesSlipId().getValue());
            // 商品購入価格小計に登録する
            this.itemPurchasePriceSubTotalDao.insert(itemPurchasePriceSubTotal);
        }

        return result;
    }

    @Override
    public SalesSlipEntity get(SalesSlipId salesSlipId) {

        // TODO このメソッド利用箇所あるか？検索ではひっかからない
        //      仮に利用箇所があるとして、下のgetByTransactionIdとクーポンのところの細かいセット仕様が微妙に違うなど差異がある
        //      共通部分はメソッド化するなど整理したほうが良い

        SalesSlipDbEntity salesSlipDbEntity = salesSlipDao.getBySalesSlipId(salesSlipId.getValue());

        if (ObjectUtils.isEmpty(salesSlipDbEntity)) {
            return null;
        }

        List<ItemPurchasePriceSubTotalDbEntity> itemPurchasePriceSubTotalDbEntityList =
                        itemPurchasePriceSubTotalDao.getBySalesSlipId(salesSlipId.getValue());

        List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotals =
                        salesSlipRepositoryHelper.toItemPurchasePriceSubTotalEntityList(
                                        itemPurchasePriceSubTotalDbEntityList);

        CouponPaymentPrice couponPaymentPrice = new CouponPaymentPrice(salesSlipDbEntity.getCouponPaymentPrice());

        CouponEntity couponDbEntity = new CouponEntity();

        if (salesSlipDbEntity.getCouponCode() != null) {
            // TODO リポジトリでマスタ値検索？
            // どれだけ重複したクーポンコードがあっても最新の1件しか取得しない
            couponDbEntity = couponDao.getCouponByCouponCode(salesSlipDbEntity.getCouponCode());
        }
        ApplyCoupon coupon = new ApplyCoupon(salesSlipDbEntity.getCouponCode(), couponDbEntity.getCouponSeq(),
                                             couponDbEntity.getCouponVersionNo(), salesSlipDbEntity.getCouponName(),
                                             couponPaymentPrice, salesSlipDbEntity.isCouponUseFlag()
        );

        List<AdjustmentAmountDbEntity> adjustmentAmountDbEntityList =
                        adjustmentAmountDao.getBySalesSlipId(salesSlipDbEntity.getSalesSlipId());

        List<AdjustmentAmount> adjustmentAmountList =
                        salesSlipRepositoryHelper.toAdjustmentAmountList(adjustmentAmountDbEntityList);

        SalesPriceConsumptionTax salesPriceConsumptionTax =
                        new SalesPriceConsumptionTax(salesSlipDbEntity.getStandardTaxTargetPrice(),
                                                     salesSlipDbEntity.getReducedTaxTargetPrice(),
                                                     salesSlipDbEntity.getStandardTax(),
                                                     salesSlipDbEntity.getReducedTax()
                        );

        return new SalesSlipEntity(salesSlipId, SalesStatus.DRAFT, coupon, salesSlipDbEntity.getBillingAmount(),
                                   itemPurchasePriceSubTotals, salesSlipDbEntity.getItemPurchasePriceTotal(),
                                   salesSlipDbEntity.getCarriage(), salesSlipDbEntity.getCommission(),
                                   salesPriceConsumptionTax, adjustmentAmountList, salesSlipDbEntity.getTransactionId(),
                                   salesSlipDbEntity.getCustomerId(), salesSlipDbEntity.getOrderCode(),
                                   salesSlipDbEntity.getRegistDate(), salesSlipDbEntity.getSalesOpenDate()
        );
    }

    @Override
    public SalesSlipEntity getByTransactionId(String transactionId) {

        SalesSlipDbEntity salesSlipDbEntity = salesSlipDao.getByTransactionId(transactionId);

        if (ObjectUtils.isEmpty(salesSlipDbEntity)) {
            return null;
        }

        ApplyCoupon coupon;
        String couponCode = salesSlipDbEntity.getCouponCode();
        CouponPaymentPrice couponPaymentPrice = new CouponPaymentPrice(salesSlipDbEntity.getCouponPaymentPrice());
        boolean couponUseFlag = salesSlipDbEntity.isCouponUseFlag();
        if (couponCode == null) {
            coupon = new ApplyCoupon(null, null, null, null, couponPaymentPrice, couponUseFlag);
        } else {
            // 販売伝票が保持している世代のクーポン情報を取得（最新ではない）
            Integer couponSeq = salesSlipDbEntity.getCouponSeq();
            Integer couponVersionNo = salesSlipDbEntity.getCouponVersionNo();
            CouponEntity dbEntity = couponDao.getCouponByCouponVersionNo(couponSeq, couponVersionNo);
            coupon = new ApplyCoupon(couponCode, couponSeq, couponVersionNo, dbEntity.getCouponName(),
                                     couponPaymentPrice, couponUseFlag
            );
        }

        List<ItemPurchasePriceSubTotalDbEntity> itemPurchasePriceSubTotalDbEntityList =
                        itemPurchasePriceSubTotalDao.getBySalesSlipId(salesSlipDbEntity.getSalesSlipId());

        List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotals =
                        salesSlipRepositoryHelper.toItemPurchasePriceSubTotalEntityList(
                                        itemPurchasePriceSubTotalDbEntityList);

        List<AdjustmentAmountDbEntity> adjustmentAmountDbEntityList =
                        adjustmentAmountDao.getBySalesSlipId(salesSlipDbEntity.getSalesSlipId());

        List<AdjustmentAmount> adjustmentAmountList =
                        salesSlipRepositoryHelper.toAdjustmentAmountList(adjustmentAmountDbEntityList);

        SalesPriceConsumptionTax salesPriceConsumptionTax =
                        new SalesPriceConsumptionTax(salesSlipDbEntity.getStandardTaxTargetPrice(),
                                                     salesSlipDbEntity.getReducedTaxTargetPrice(),
                                                     salesSlipDbEntity.getStandardTax(),
                                                     salesSlipDbEntity.getReducedTax()
                        );

        return new SalesSlipEntity(new SalesSlipId(salesSlipDbEntity.getSalesSlipId()),
                                   EnumTypeUtil.getEnum(SalesStatus.class, salesSlipDbEntity.getSalesStatus()), coupon,
                                   salesSlipDbEntity.getBillingAmount(), itemPurchasePriceSubTotals,
                                   salesSlipDbEntity.getItemPurchasePriceTotal(), salesSlipDbEntity.getCarriage(),
                                   salesSlipDbEntity.getCommission(), salesPriceConsumptionTax, adjustmentAmountList,
                                   salesSlipDbEntity.getTransactionId(), salesSlipDbEntity.getCustomerId(),
                                   salesSlipDbEntity.getOrderCode(), salesSlipDbEntity.getRegistDate(),
                                   salesSlipDbEntity.getSalesOpenDate()
        );
    }

    @Override
    public Integer getCouponCountByCustomerId(String customerId, Integer couponSeq, Timestamp couponStartTime) {
        return salesSlipDao.getCouponCountByCustomerId(customerId, couponSeq, couponStartTime);
    }

    @Override
    public int deleteUnnecessaryByTransactionId(String transactionId) {

        itemPurchasePriceSubTotalDao.deleteItemPurchasePriceSubTotal(transactionId);
        adjustmentAmountDao.deleteAdjustmentAmount(transactionId);

        return salesSlipDao.deleteSaleSlip(transactionId);
    }

}