/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.transformer.mailtemplate;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CalculatePriceUtility;
import jp.co.itechh.quad.core.utility.ConvenienceUtility;
import jp.co.itechh.quad.core.utility.MailTemplateDummyMapUtility;
import jp.co.itechh.quad.core.utility.OrderUtility;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注文系メールテンプレート用トランスフォーマ。
 *
 * @author tm27400
 * @author natsume
 * @author Tomo (itec) ダミー値マップ動的差し替え対応
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Component("orderTransformHelper")
public class OrderTransformer implements Transformer {

    /************************************
     ** 注文ステータス 値オブジェクト(enum)の一覧<br//>
     ** jp.co.itechh.quad.ddd.domain.transaction.valueobject
     ************************************/

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTransformer.class);

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 受注業務Utility */
    private final OrderUtility orderUtility;

    /** 金額計算Utility */
    private final CalculatePriceUtility calculatePriceUtility;

    /** コンビニUtility */
    private final ConvenienceUtility convenienceUtility;

    /** DecimalFormat */
    protected Format priceFormat = new DecimalFormat("#,##0");

    /** 預金種別ラベル */
    private static final String ACCOUNTTYPE_LABEL = "普通預金";

    /** コンストラクタ */
    @Autowired
    public OrderTransformer(ConversionUtility conversionUtility,
                            DateUtility dateUtility,
                            OrderUtility orderUtility,
                            CalculatePriceUtility calculatePriceUtility,
                            ConvenienceUtility convenienceUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.orderUtility = orderUtility;
        this.calculatePriceUtility = calculatePriceUtility;
        this.convenienceUtility = convenienceUtility;
    }

    /**
     * ダミープレースホルダを返す
     *
     * @return ダミープレースホルダ
     */
    @Override
    public Map<String, String> getDummyPlaceholderMap() {
        MailTemplateDummyMapUtility mailTemplateDummyMapUtility =
                        ApplicationContextUtility.getBean(MailTemplateDummyMapUtility.class);
        return mailTemplateDummyMapUtility.getDummyValueMap(getResourceName());
    }

    /**
     * 値マップを取得する
     *
     * @param arguments 変換元引数
     * @return 値マップ
     */
    @Override
    public Map<String, String> toValueMap(Object... arguments) {

        // 引数チェック
        this.checkArguments(arguments);

        if (arguments == null) {
            MailTemplateDummyMapUtility mailTemplateDummyMapUtility =
                            ApplicationContextUtility.getBean(MailTemplateDummyMapUtility.class);
            return mailTemplateDummyMapUtility.getDummyValueMap(getResourceName());
        } else if (arguments.length == 0) {
            return Collections.emptyMap();
        }

        // 受注Dtoから各種情報を取得
        OrderReceivedDto value = (OrderReceivedDto) arguments[0];
        MemberInfoEntity memberInfoEntity = value.getMemberInfoDetailsDto().getMemberInfoEntity();
        OrderReceivedResponse orderReceived = value.getOrderReceivedResponse();
        ShippingSlipResponse shippingSlip = value.getShippingSlipResponse();
        SalesSlipResponse salesSlip = value.getSalesSlipResponse();
        BillingSlipResponse billingSlip = value.getBillingSlipResponse();
        PaymentMethodResponse paymentMethod = value.getPaymentMethodResponse();
        ShippingMethodResponse shippingMethod = value.getShippingMethodResponse();
        MulPayBillResponse mulPayBill = value.getMulPayBillResponse();
        PaymentLinkResponse paymentLink = value.getBillingSlipResponse().getPaymentLinkResponse();

        Map<String, String> valueMap = new LinkedHashMap<>();

        // 受注情報
        valueMap.put("O_ORDER_CODE", orderReceived.getOrderCode());
        valueMap.put("O_ORDER_TIME", dateUtility.format(orderReceived.getOrderReceivedDate(), "yyyy年M月d日H時m分"));

        // ご注文主
        valueMap.put("O_MEMBER_INFO_SEQ", Integer.toString(memberInfoEntity.getMemberInfoSeq()));
        valueMap.put("O_ORDER_LAST_NAME", memberInfoEntity.getMemberInfoLastName());
        valueMap.put("O_ORDER_FIRST_NAME", memberInfoEntity.getMemberInfoFirstName());
        valueMap.put("O_ORDER_TEL", memberInfoEntity.getMemberInfoTel());

        // 受注金額
        valueMap.put(
                        "O_ORDER_PRICE", priceFormat.format(new BigDecimal(salesSlip.getBillingAmount() != null ?
                                                                                           salesSlip.getBillingAmount() :
                                                                                           0)));

        // 決済方法
        valueMap.put("O_SETTLEMENTMETHOD_TYPE", paymentMethod.getSettlementMethodType());
        valueMap.put("O_SETTLEMENTMETHOD_DISPLAY_NAME_PC", billingSlip.getPaymentMethodName());

        // 受注配送
        // 配送追跡 URLを表示すれば、「発送内容」 表示する
        valueMap.put("O_DELIVERY_ORDER_DISPLAY_FLAG", "0");
        String deliveryChaseURL = orderUtility.getDeliveryChaseURL(
                        shippingMethod.getDeliveryMethodResponse().getDeliveryChaseURL(),
                        shippingMethod.getDeliveryMethodResponse().getDeliveryChaseURLDisplayPeriod(),
                        shippingSlip.getShipmentStatusConfirmCode(),
                        conversionUtility.toTimestamp(shippingSlip.getCompleteShipmentDate())
                                                                  );
        if (StringUtil.isNotEmpty(deliveryChaseURL)) {
            valueMap.put("O_DELIVERY_ORDER_DISPLAY_FLAG", "1");
        }

        toValueMapDelively(valueMap, shippingSlip, shippingMethod);

        // マルチペイメント決済請求
        if (mulPayBill != null && mulPayBill.getPayType() != null && paymentLink != null && "5".equals(
                        paymentMethod.getSettlementMethodType())) {
            // 決済タイプ
            valueMap.put("O_LINKPAY_TYPE", paymentLink.getPayType());
            if ("cvs".equals(paymentLink.getPaymethod())) {
                // コンビニ
                valueMap.put("O_SETTLEMENTMETHOD_DISPLAY_NAME_PC", "コンビニ");
                valueMap.put("O_CONVENIENCE", mulPayBill.getConvenience());
                valueMap.put("O_CONF_NO", mulPayBill.getConfNo());
                valueMap.put("O_PAYMENT_TIME_LIMIT_DATE",
                             dateUtility.format(paymentLink.getLaterDateLimit(), "yyyy年M月d日")
                            );

                // 該当のコンビニの場合、オンライン決済番号を編集する。
                String receiptNo = CopyUtil.deepCopy(mulPayBill.getReceiptNo());
                if (convenienceUtility.isConveni2(mulPayBill.getConvenience())) {
                    // オンライン決済番号は4桁-7桁に区切る
                    // (WNT25126649 → WNT2-5126649 のように編集する)
                    StringBuilder builder = new StringBuilder();
                    builder.append(receiptNo.substring(0, 4)).append("-").append(receiptNo.substring(4));
                    receiptNo = builder.toString();
                }
                valueMap.put("O_RECEIPT_NO", receiptNo);
            } else if ("payeasy".equals(paymentLink.getPaymethod())) {
                // ペイジー
                valueMap.put("O_SETTLEMENTMETHOD_DISPLAY_NAME_PC", "ペイジー");
                valueMap.put("O_CONF_NO", mulPayBill.getConfNo());
                valueMap.put("O_BKCODE", mulPayBill.getBkCode());
                valueMap.put("O_CUST_ID", mulPayBill.getCustId());
                valueMap.put("O_PAYMENT_TIME_LIMIT_DATE",
                             dateUtility.format(paymentLink.getLaterDateLimit(), "yyyy年M月d日")
                            );
            } else if ("ganb".equals(paymentLink.getPaymethod())) {
                // 銀行振込
                valueMap.put("O_PAY_TYPE", paymentLink.getPayTypeName());
                valueMap.put("O_BANK_NAME", mulPayBill.getBankName());
                valueMap.put("O_BRANCH_NAME", mulPayBill.getBranchName());
                valueMap.put("O_ACCOUNT_TYPE", this.getAccountTypeLabel(mulPayBill.getAccountType()));
                valueMap.put("O_ACCOUNT_NUMBER", mulPayBill.getAccountNumber());
                valueMap.put(
                                "O_TRAN_DATE", dateUtility.format(
                                                TimestampConversionUtil.toTimestamp(
                                                                mulPayBill.getExprireDate(), "yyyyMMdd"), "yyyy年M月d日"));
            }
        }
        return valueMap;
    }

    /**
     * 値マップに配送情報を設定
     *
     * @param valueMap       値マップ
     * @param shippingSlip   配送伝票
     * @param shippingMethod 配送方法
     */
    protected void toValueMapDelively(Map<String, String> valueMap,
                                      ShippingSlipResponse shippingSlip,
                                      ShippingMethodResponse shippingMethod) {

        // TODO-QUAD-1222 ※メルテンの該当htmlの修正も必要
        int receiverCounter = 1;
        valueMap.put(
                        "O_SHIPMENT_STATUS" + LOOP_STR + receiverCounter, EnumTypeUtil.getValue(
                                        shippingSlip.getCompleteShipmentDate() != null ?
                                                        HTypeShipmentStatus.SHIPPED :
                                                        HTypeShipmentStatus.UNSHIPMENT));
        valueMap.put("O_DELIVERY_CODE" + LOOP_STR + receiverCounter, shippingSlip.getShipmentStatusConfirmCode());

        // 配送追跡URLを表示
        valueMap.put("O_DELIVERY_CHASEURL_DISPLAY_FLAG" + LOOP_STR + receiverCounter, "0");
        String deliveryChaseURL = orderUtility.getDeliveryChaseURL(
                        shippingMethod.getDeliveryMethodResponse().getDeliveryChaseURL(),
                        shippingMethod.getDeliveryMethodResponse().getDeliveryChaseURLDisplayPeriod(),
                        shippingSlip.getShipmentStatusConfirmCode(),
                        conversionUtility.toTimestamp(shippingSlip.getCompleteShipmentDate())
                                                                  );
        if (StringUtil.isNotEmpty(deliveryChaseURL)) {
            valueMap.put("O_DELIVERY_CHASEURL_DISPLAY_FLAG" + LOOP_STR + receiverCounter, "1");
            valueMap.put("O_DELIVERY_CHASEURL" + LOOP_STR + receiverCounter, deliveryChaseURL);
        }
        valueMap.put("O_DELIVERY_TOTAL", String.valueOf(receiverCounter));

    }

    /**
     * 引数の有効性を確認する
     *
     * @param arguments 引数
     */
    protected void checkArguments(Object[] arguments) {

        // オブジェクトがない場合はテスト送信用とみなす
        if (arguments == null) {
            return;
        }

        if (arguments.length != 1) {
            RuntimeException e = new IllegalArgumentException("プレースホルダ用値マップに変換できません：引数の数が合いません。");
            LOGGER.warn("引数チェックエラー", e);
            throw e;
        }

        if (!(arguments[0] instanceof OrderReceivedDto)) {
            RuntimeException e = new IllegalArgumentException("プレースホルダ用値マップに変換できません：引数の型が合いません。");
            LOGGER.warn("引数チェックエラー", e);
            throw e;
        }
    }

    /**
     * リソースファイル名<br/>
     *
     * @return リソースファイル名
     */
    @Override
    public String getResourceName() {
        return "OrderTransformHelper";
    }

    /**
     * 振込先口座種別ラベル取得
     *
     * @param accountType 振込先口座種別
     * @return 振込先口座種別ラベル
     */
    protected String getAccountTypeLabel(String accountType) {
        return StringUtils.isNotEmpty(accountType) && "1".equals(accountType) ? ACCOUNTTYPE_LABEL : StringUtils.EMPTY;
    }
}