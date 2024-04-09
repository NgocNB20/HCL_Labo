/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import com.gmo_pg.g_pay.client.output.SearchCardOutput;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeDividedNumber;
import jp.co.itechh.quad.core.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGmoPaymentCancelStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.core.constant.type.HTypePaymentType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dao.linkpay.SettlementMethodLinkDao;
import jp.co.itechh.quad.core.dao.shop.settlement.SettlementMethodDao;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPaymentService;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPaymentService;
import jp.co.itechh.quad.ddd.domain.card.proxy.CardProxyService;
import jp.co.itechh.quad.ddd.domain.card.valueobject.CreditExpirationDate;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.OrderPaymentDao;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * 注文決済ドメインサービス
 */
@Service
public class OrderPaymentEntityService {

    /** 決済方法Dao */
    private final SettlementMethodDao settlementMethodDao;

    /** クレジットカード決済 値オブジェクト ドメインサービス */
    private final CreditPaymentService creditPaymentService;

    /** カードプロキシサービス */
    private final CardProxyService cardProxyService;

    /** 決済方法取得ロジック */
    private final SettlementMethodGetLogic settlementMethodGetLogic;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 注文決済Daoクラス */
    private final OrderPaymentDao orderPaymentDao;

    /** リンク決済個別決済手段 マスタDao */
    private final SettlementMethodLinkDao settlementMethodLinkDao;

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** 配送アダプター */
    private final IShippingAdapter shippingAdapter;

    /** コンストラクタ */
    @Autowired
    public OrderPaymentEntityService(SettlementMethodDao settlementMethodDao,
                                     CreditPaymentService creditPaymentService,
                                     CardProxyService cardProxyService,
                                     SettlementMethodGetLogic settlementMethodGetLogic,
                                     ConversionUtility conversionUtility,
                                     OrderPaymentDao orderPaymentDao,
                                     SettlementMethodLinkDao settlementMethodLinkDao,
                                     DateUtility dateUtility,
                                     IShippingAdapter shippingAdapter) {
        this.settlementMethodDao = settlementMethodDao;
        this.creditPaymentService = creditPaymentService;
        this.cardProxyService = cardProxyService;
        this.settlementMethodGetLogic = settlementMethodGetLogic;
        this.conversionUtility = conversionUtility;
        this.orderPaymentDao = orderPaymentDao;
        this.settlementMethodLinkDao = settlementMethodLinkDao;
        this.dateUtility = dateUtility;
        this.shippingAdapter = shippingAdapter;
    }

    /**
     * デフォルト決済方法を設定する<br>
     * 公開決済方法マスタの1番目の決済方法を取得して設定
     *
     * @param customerId
     * @param billingSlipEntity
     * @param orderPaymentEntity
     */
    public void setDefaultPaymentMethod(String customerId,
                                        BillingSlipEntity billingSlipEntity,
                                        OrderPaymentEntity orderPaymentEntity) {

        // 配送伝票を取得する
        ShippingSlip shippingSlip = this.shippingAdapter.getShippingSlip(billingSlipEntity.getTransactionId());
        // 配送伝票が存在する、かつ配送方法IDが設定されている場合
        if (shippingSlip != null && StringUtils.isBlank(shippingSlip.getShippingMethodId())) {
            return;
        }

        /* 公開決済方法マスタ一覧取得 */
        // 決済方法取得検索条件
        SettlementSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(SettlementSearchForDaoConditionDto.class);
        // 公開中を指定
        conditionDto.setOpenStatusPC(HTypeOpenDeleteStatus.OPEN);
        // 決済詳細Dtoリスト取得
        List<SettlementDetailsDto> settlementDetailsDtoList =
                        settlementMethodDao.getSearchSettlementDetailsList(conditionDto);
        if (CollectionUtils.isEmpty(settlementDetailsDtoList)) {
            return;
        }

        // 公開決済方法の1番目を取得
        SettlementDetailsDto firstSettlementDto = settlementDetailsDtoList.get(0);

        if (firstSettlementDto == null) {
            return;
        }

        // デフォルト決済方法設定不可能フラグ
        boolean isUnableSetPaymentMethod = false;

        SettingPaymentCreditParam creditParam = null;
        // クレジット決済の場合
        if (firstSettlementDto.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {

            creditParam = new SettingPaymentCreditParam();

            // 顧客登録カード情報を取得
            SearchCardOutput searchCardOutput = this.cardProxyService.getCardInfo(customerId);

            if (searchCardOutput != null && !CollectionUtil.isEmpty(searchCardOutput.getCardList())) {
                // 登録済みカードが存在する場合

                // カード情報の照会結果から、カード情報を取得
                SearchCardOutput.CardInfo cardInfo = (SearchCardOutput.CardInfo) searchCardOutput.getCardList().get(0);

                // クレジット有効期限（年）4桁 ※cardInfoは西暦後ろ2桁のみ保持
                String expirationYear =
                                PropertiesUtil.getSystemPropertiesValue("expiration.date.year") + cardInfo.getExpire()
                                                                                                          .substring(
                                                                                                                          0,
                                                                                                                          2
                                                                                                                    );
                // クレジット有効期限（月）2桁
                String expirationMonth = cardInfo.getExpire().substring(2);

                // 決済方法設定処理用パラメータ設定
                creditParam.setPaymentToken(null);
                creditParam.setMaskedCardNo(cardInfo.getCardNo());
                creditParam.setExpirationYear(expirationYear);
                creditParam.setExpirationMonth(expirationMonth);
                creditParam.setPaymentType("1");
                creditParam.setDividedNumber(null);
                creditParam.setRegistCardFlag(false);
                creditParam.setUseRegistedCardFlag(true);

            } else {
                // 登録済みカードが存在しない場合
                isUnableSetPaymentMethod = true;
            }

        } else {
            // その他決済方法の場合
            isUnableSetPaymentMethod = true;

        }

        // デフォルト決済方法設定可能な場合
        if (!isUnableSetPaymentMethod) {
            // 決済方法を設定する
            settingPaymentMethod(firstSettlementDto.getSettlementMethodSeq().toString(), creditParam, billingSlipEntity,
                                 orderPaymentEntity
                                );
        }

    }

    /**
     * 決済方法を設定
     *
     * @param paymentMethodId
     * @param creditParam
     * @param billingSlipEntity
     * @param orderPaymentEntity
     */
    public void settingPaymentMethod(String paymentMethodId,
                                     SettingPaymentCreditParam creditParam,
                                     BillingSlipEntity billingSlipEntity,
                                     OrderPaymentEntity orderPaymentEntity) {

        // 決済方法情報を取得する
        SettlementMethodEntity settlementMethodDbEntity =
                        settlementMethodGetLogic.execute(conversionUtility.toInteger(paymentMethodId));
        // 決済方法が存在しない場合はエラー
        if (settlementMethodDbEntity == null) {
            throw new DomainException("PAYMENT_ODPS0001-E", new String[] {paymentMethodId});
        }

        // 決済方法ID設定
        orderPaymentEntity.setPaymentMethodId(paymentMethodId);
        // 決済方法名設定
        orderPaymentEntity.setPaymentMethodName(settlementMethodDbEntity.getSettlementMethodName());
        // 請求種別設定
        orderPaymentEntity.setSettlementMethodType(settlementMethodDbEntity.getSettlementMethodType());

        // 請求伝票に請求種別を設定
        billingSlipEntity.settingBillingType(settlementMethodDbEntity.getBillType());

        // クレジット決済の場合
        if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {
            // チェック
            AssertChecker.assertNotNull("creditParam is null", creditParam);

            // 3Dセキュア有効フラグ
            boolean enable3dSecureFlag = settlementMethodDbEntity.getEnable3dSecure() == HTypeEffectiveFlag.VALID;
            // クレジット有効期限
            CreditExpirationDate expirationDate =
                            CreditExpirationDate.createCreditExpirationDate(creditParam.getExpirationYear(),
                                                                            creditParam.getExpirationMonth()
                                                                           );

            // 決済依頼(クレジット) 値オブジェクトを生成して、エンティティに設定する
            CreditPayment creditPayment =
                            new CreditPayment(creditParam.getPaymentToken(), creditParam.getMaskedCardNo(),
                                              expirationDate, EnumTypeUtil.getEnumFromValue(HTypePaymentType.class,
                                                                                            creditParam.getPaymentType()
                                                                                           ),
                                              billingSlipEntity.getBillingType(),
                                              EnumTypeUtil.getEnumFromValue(HTypeDividedNumber.class,
                                                                            creditParam.getDividedNumber()
                                                                           ), enable3dSecureFlag,
                                              creditParam.isRegistCardFlag(), creditParam.isUseRegistedCardFlag()
                            );
            orderPaymentEntity.setPaymentRequest(creditPayment);

        } else if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.DISCOUNT) {
            // 全額クーポン決済の場合
        } else if (orderPaymentEntity.getSettlementMethodType() == HTypeSettlementMethodType.LINK_PAYMENT) {
            // リンク決済決済の場合
            LinkPayment linkPayment =
                            new LinkPayment(HTypeGmoPaymentCancelStatus.UNDECIDED, null, null, null, null, null, null);
            orderPaymentEntity.setPaymentRequest(linkPayment);
        } else {

            throw new DomainException("PAYMENT_ODPS0002-E");
        }
    }

    /**
     * 注文決済トランザクションデータ最新化
     *
     * @param orderPaymentEntity
     */
    public void modernizeOrderPayment(OrderPaymentEntity orderPaymentEntity) {

        // 決済方法情報を取得する
        SettlementMethodEntity settlementMethodDbEntity = settlementMethodGetLogic.execute(
                        conversionUtility.toInteger(orderPaymentEntity.getPaymentMethodId()));
        // 決済方法が存在しない場合はスキップ
        if (ObjectUtils.isEmpty(settlementMethodDbEntity)) {
            return;
        }
        // 最新化決済方法名設定
        orderPaymentEntity.setPaymentMethodName(settlementMethodDbEntity.getSettlementMethodName());
    }

    /**
     * 対象受注番号一覧を取得
     *
     * @return 対象受注番号一覧
     */
    public List<TargetSalesOrderPaymentDto> getTargetSalesOrderNumberList() {
        return orderPaymentDao.getTargetSalesOrderNumberList();
    }

    /**
     * 対象受注の注文決済に結果反映
     *
     * @param orderCode              受注番号
     * @param gmoPaymentCancelStatus GMO決済キャンセル状態
     * @return 処理結果
     */
    public int updateGmoPaymentCancelStatus(String orderCode, HTypeGmoPaymentCancelStatus gmoPaymentCancelStatus) {
        return orderPaymentDao.updateGmoPaymentCancelStatus(orderCode, gmoPaymentCancelStatus);
    }

    /**
     * リンク決済結果を注文決済へ登録する
     *
     * @param mulPayBillEntity
     * @param billingSlipEntity
     */
    public void setLinkPayResToOrderPayment(MulPayBillEntity mulPayBillEntity, BillingSlipEntity billingSlipEntity) {

        // リンク決済でない場合
        if (HTypeSettlementMethodType.LINK_PAYMENT != billingSlipEntity.getOrderPaymentEntity()
                                                                       .getSettlementMethodType()) {
            throw new DomainException("PAYMENT_LINK0002-E");
        }

        // リンク決済個別決済手段 情報取得
        SettlementMethodLinkEntity settlementMethodLink =
                        settlementMethodLinkDao.getSettlementMethodLinkByPayMethod(mulPayBillEntity.getPayMethod());

        // 決済手段名
        String payTypeName = ObjectUtils.isNotEmpty(settlementMethodLink) ?
                        settlementMethodLink.getPayTypeName() :
                        PropertiesUtil.getSystemPropertiesValue("linkpay.method.other");

        // GMO即時払いキャンセル期限日
        Timestamp cancelLimit = null;
        // GMO後日払い支払期限日
        Timestamp laterDateLimit = null;
        if (HTypePaymentLinkType.IMMEDIATEPAYMENT == LinkPaymentService.getHTypePaymentLinkType(
                        mulPayBillEntity.getPayMethod()) && ObjectUtils.isNotEmpty(settlementMethodLink)) {
            cancelLimit = getCancelLimit(settlementMethodLink);
        } else {
            if ("ganb".equals(mulPayBillEntity.getPayMethod())) {
                Timestamp tmpLaterDateLimit =
                                dateUtility.toTimestampValue(mulPayBillEntity.getExpireDate(), "yyyyMMdd");
                laterDateLimit = dateUtility.getEndOfDate(tmpLaterDateLimit);
            } else {
                laterDateLimit = dateUtility.toTimestampValue(mulPayBillEntity.getPaymentTerm(), "yyyyMMddHHmmss");
            }
        }

        // リンク決済(決済依頼IF) 値オブジェクト生成して設定
        LinkPayment linkPayment =
                        new LinkPayment(HTypeGmoPaymentCancelStatus.UNDECIDED, mulPayBillEntity.getPayMethod(),
                                        mulPayBillEntity.getPayType(), payTypeName,
                                        LinkPaymentService.getHTypePaymentLinkType(mulPayBillEntity.getPayMethod()),
                                        cancelLimit, laterDateLimit
                        );
        OrderPaymentEntity orderPaymentEntity = billingSlipEntity.getOrderPaymentEntity();
        orderPaymentEntity.setPaymentRequest(linkPayment);
    }

    /**
     * GMOキャンセル期限取得
     *
     * @param entity リンク決済個別決済手段
     * @return 指定された分を加算(減算)したTimestamp
     */
    private Timestamp getCancelLimit(SettlementMethodLinkEntity entity) {

        Timestamp limitDate = null;

        if (entity.getCancelLimitDay() != null) {
            // 日数指定の期限の場合
            limitDate = dateUtility.getAdditionalDate(entity.getCancelLimitDay());
            return dateUtility.getEndOfDate(limitDate);
        } else if (entity.getCancelLimitMonth() != null) {
            // 月指定の場合
            limitDate = dateUtility.getAmountTimestamp(entity.getCancelLimitMonth(), true,
                                                       new Timestamp(System.currentTimeMillis()), Calendar.MONTH
                                                      );
            return dateUtility.getEndOfMonth(-entity.getCancelLimitMonth(), new Timestamp(System.currentTimeMillis()));
        }

        return null;
    }
}