package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderStatusDdd;
import jp.co.itechh.quad.core.constant.type.HTypeYouPackType;
import jp.co.itechh.quad.ddd.domain.customize.adapter.model.ExamKit;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentMethod;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import jp.co.itechh.quad.ddd.infrastructure.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderProduct;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.SalesSlipProductInformation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注文登録更新用Helperクラス
 */
@Component
public class OrderRegistUpdateUsecaseHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * 全角、半角の変換を行うヘルパークラス
     */
    private final ZenHanConversionUtility zenHanConversionUtility;

    /**
     * 0～9歳
     */
    private static final String AGE_0TO9 = "00";

    /**
     * 10～19歳
     */
    private static final String AGE_10TO19 = "01";

    /**
     * 20～29歳
     */
    private static final String AGE_20TO29 = "02";

    /**
     * 30～39歳
     */
    private static final String AGE_30TO39 = "03";

    /**
     * 40～49歳
     */
    private static final String AGE_40TO49 = "04";

    /**
     * 50～59歳
     */
    private static final String AGE_50TO59 = "05";

    /**
     * 60～69歳
     */
    private static final String AGE_60TO69 = "06";

    /**
     * 70～79歳
     */
    private static final String AGE_70TO79 = "07";

    /**
     * 80～89歳
     */
    private static final String AGE_80TO89 = "08";

    /**
     * 90～99歳
     */
    private static final String AGE_90TO99 = "09";

    /**
     * 未回答・その他
     */
    private static final String UNKNOWN = "10";

    /**
     * 前請求
     */
    private static final String PRE_CLAIM = "0";

    /**
     * 未請求 0
     */
    private static final String BILL_NO_CLAIM = "0";

    /**
     * 請求済み 1
     */
    private static final String BILL_CLAIM = "1";

    /**
     * 正常
     */
    private static final String OFF = "0";

    /**
     * 異常
     */
    private static final String ON = "1";

    /**
     * 未出荷
     */
    private static final String UNSHIPMENT = "0";

    /**
     * 出荷済み
     */
    private static final String SHIPPED = "1";

    /**
     * リピート種別 2
     */
    private static final Integer REPEAT_PURCHASE_TYPE_2 = 2;

    /**
     * リピート種別 1
     */
    private static final Integer REPEAT_PURCHASE_TYPE_1 = 1;

    /**
     * 入金待ち
     */
    private static final String PAYMENT_CONFIRMING = "PAYMENT_CONFIRMING";

    /**
     * 商品準備中
     */
    private static final String ITEM_PREPARING = "ITEM_PREPARING";

    /**
     * 出荷完了
     */
    private static final String SHIPMENT_COMPLETION = "SHIPMENT_COMPLETION";

    /**
     * キャンセル
     */
    private static final String CANCEL = "CANCEL";

    /**
     * 請求決済エラー
     */
    private static final String PAYMENT_ERROR = "PAYMENT_ERROR";

    /**
     * キャンセル以外
     */
    private static final String OTHER = "OTHER";

    /**
     * 全角スペース
     * <code>EM_SPACE</code>
     */
    protected static final String EM_SPACE = "　";

    /**
     * 全角スペース
     * <code>EM_SPACE</code>
     */
    protected static final String EN_SPACE = " ";

    /**
     * Decimalフォーマット
     */
    private final DecimalFormat decimalFormat;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public OrderRegistUpdateUsecaseHelper(ConversionUtility conversionUtility,
                                          ZenHanConversionUtility zenHanConversionUtility) {
        this.conversionUtility = conversionUtility;
        this.zenHanConversionUtility = zenHanConversionUtility;
        this.decimalFormat = new DecimalFormat("##0.00");
    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param orderReceived         受注
     */
    public void settingOrderSearchQueryModelByOrderReceived(OrderSearchQueryModel orderSearchQueryModel,
                                                            OrderReceived orderReceived) {

        orderSearchQueryModel.setOrderCode(orderReceived.getOrderCode());
        orderSearchQueryModel.setOrderTime(orderReceived.getOrderReceivedDate());

        if (StringUtils.isNotEmpty(orderReceived.getOrderStatus())) {
            switch (orderReceived.getOrderStatus().toUpperCase()) {
                case PAYMENT_CONFIRMING:
                    orderSearchQueryModel.setOrderStatus(HTypeOrderStatusDdd.PAYMENT_CONFIRMING.getValue());
                    break;
                case ITEM_PREPARING:
                    orderSearchQueryModel.setOrderStatus(HTypeOrderStatusDdd.ITEM_PREPARING.getValue());
                    break;
                case SHIPMENT_COMPLETION:
                    orderSearchQueryModel.setOrderStatus(HTypeOrderStatusDdd.SHIPMENT_COMPLETION.getValue());
                    break;
                case CANCEL:
                    orderSearchQueryModel.setOrderStatus(HTypeOrderStatusDdd.CANCEL.getValue());
                    break;
                case PAYMENT_ERROR:
                    orderSearchQueryModel.setOrderStatus(HTypeOrderStatusDdd.PAYMENT_ERROR.getValue());
                    break;
                case OTHER:
                    orderSearchQueryModel.setOrderStatus(HTypeOrderStatusDdd.OTHER.getValue());
                    break;
                default:
                    orderSearchQueryModel.setOrderStatus(orderReceived.getOrderStatus());
                    break;
            }
        }

        orderSearchQueryModel.setProcessTime(orderReceived.getProcessTime());

        if (Boolean.TRUE.equals(orderReceived.getShippedFlag())) {
            orderSearchQueryModel.setShipmentStatus(SHIPPED);
        } else {
            orderSearchQueryModel.setShipmentStatus(UNSHIPMENT);
        }

        if (OrderStatus.CANCEL.name().equals(orderReceived.getOrderStatus())) {
            orderSearchQueryModel.setCancelTime(orderReceived.getCancelDate());
        }

        orderSearchQueryModel.setPaymentStatus(
                        OrderRegistUpdateUseCase.PAYMENT_STATUS_DATA_MAP.get(orderReceived.getPaymentStatusDetail()));
        orderSearchQueryModel.setMemo(orderReceived.getAdminMemo());
        orderSearchQueryModel.setCustomerId(orderReceived.getCustomerId());
        orderSearchQueryModel.setNoveltyPresentJudgmentStatus(orderReceived.getNoveltyPresentJudgmentStatus());
    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param shippingSlip          配送伝票
     */
    public void settingOrderSearchQueryModelByShippingSlip(OrderSearchQueryModel orderSearchQueryModel,
                                                           ShippingSlip shippingSlip) {

        orderSearchQueryModel.setShippingMethodId(shippingSlip.getShippingMethodId());
        orderSearchQueryModel.setShipmentDate(shippingSlip.getCompleteShipmentDate());
        orderSearchQueryModel.setShippingMethodName(shippingSlip.getShippingMethodName());
        orderSearchQueryModel.setReceiverDate(shippingSlip.getReceiverDate());
        orderSearchQueryModel.setReceiverTimeZone(shippingSlip.getReceiverTimeZone());
        orderSearchQueryModel.setInvoiceAttachmentFlag(shippingSlip.isInvoiceNecessaryFlag() ? ON : OFF);
        orderSearchQueryModel.setDeliveryStatusConfirmationNo(shippingSlip.getShipmentStatusConfirmCode());

    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param salesSlip             販売伝票
     */
    public void settingOrderSearchQueryModelBySalesSlip(OrderSearchQueryModel orderSearchQueryModel,
                                                        SalesSlip salesSlip) {

        orderSearchQueryModel.setOrderPrice(salesSlip.getBillingAmount());
        orderSearchQueryModel.setSettlementCommission(salesSlip.getCommission());
        orderSearchQueryModel.setOrderDeliveryCarriage(salesSlip.getCarriage());

        int totalAdjustmentAmount =
                        salesSlip.getAdjustmentAmountList().stream().mapToInt(AdjustmentAmount::getAdjustPrice).sum();
        orderSearchQueryModel.setTotalAdjustmentAmount(totalAdjustmentAmount);

        int taxPrice = salesSlip.getStandardTax() + salesSlip.getReducedTax();

        orderSearchQueryModel.setTaxPrice(taxPrice);
        orderSearchQueryModel.setStandardTaxTargetPrice(salesSlip.getStandardTaxTargetPrice());
        orderSearchQueryModel.setStandardTaxPrice(salesSlip.getStandardTax());
        orderSearchQueryModel.setReducedTaxTargetPrice(salesSlip.getReducedTaxTargetPrice());
        orderSearchQueryModel.setReducedTaxPrice(salesSlip.getReducedTax());
        orderSearchQueryModel.setCouponPaymentAmount(salesSlip.getCouponPaymentPrice());
        orderSearchQueryModel.setCouponName(salesSlip.getCouponName());

    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param coupon                クーポン
     */
    public void settingOrderSearchQueryModelByCoupon(OrderSearchQueryModel orderSearchQueryModel, Coupon coupon) {

        orderSearchQueryModel.setCouponId(coupon.getCouponId());

    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param orderAddressBook      住所録
     */
    public void settingOrderSearchQueryModelByOrderAddressBook(OrderSearchQueryModel orderSearchQueryModel,
                                                               AddressBook orderAddressBook) {

        orderSearchQueryModel.setBillingLastName((orderAddressBook.getLastName()));
        orderSearchQueryModel.setBillingFirstName(orderAddressBook.getFirstName());
        orderSearchQueryModel.setBillingLastKana(orderAddressBook.getLastKana());
        orderSearchQueryModel.setBillingFirstKana(orderAddressBook.getFirstKana());
        orderSearchQueryModel.setBillingZipCode(orderAddressBook.getZipCode());
        orderSearchQueryModel.setBillingPrefecture(orderAddressBook.getPrefecture());
        orderSearchQueryModel.setBillingAddress1(orderAddressBook.getAddress1());
        orderSearchQueryModel.setBillingAddress2(orderAddressBook.getAddress2());
        orderSearchQueryModel.setBillingAddress3(orderAddressBook.getAddress3());
        orderSearchQueryModel.setBillingTel(orderAddressBook.getTel());
        orderSearchQueryModel.setDeliveryNote(orderAddressBook.getShippingMemo());

    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param receiverAddressBook   住所録
     */
    public void settingOrderSearchQueryModelByReceiverAddressBook(OrderSearchQueryModel orderSearchQueryModel,
                                                                  AddressBook receiverAddressBook) {

        orderSearchQueryModel.setShippingLastName(receiverAddressBook.getLastName());
        orderSearchQueryModel.setShippingFirstName(receiverAddressBook.getFirstName());
        orderSearchQueryModel.setShippingLastKana(receiverAddressBook.getLastKana());
        orderSearchQueryModel.setShippingFirstKana(receiverAddressBook.getFirstKana());
        orderSearchQueryModel.setShippingZipCode(receiverAddressBook.getZipCode());
        orderSearchQueryModel.setShippingPrefecture(receiverAddressBook.getPrefecture());
        orderSearchQueryModel.setShippingAddress1(receiverAddressBook.getAddress1());
        orderSearchQueryModel.setShippingAddress2(receiverAddressBook.getAddress2());
        orderSearchQueryModel.setShippingAddress3(receiverAddressBook.getAddress3());
        orderSearchQueryModel.setShippingTel(receiverAddressBook.getTel());
        orderSearchQueryModel.setDeliveryNote(receiverAddressBook.getShippingMemo());

    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param customer              会員
     */
    public void settingOrderSearchQueryModelByCustomer(OrderSearchQueryModel orderSearchQueryModel, Customer customer) {

        orderSearchQueryModel.setCustomerLastName(customer.getCustomerLastName());
        orderSearchQueryModel.setCustomerFirstName(customer.getCustomerFirstName());
        orderSearchQueryModel.setCustomerLastKana(customer.getCustomerLastKana());
        orderSearchQueryModel.setCustomerFirstKana(customer.getCustomerFirstKana());
        orderSearchQueryModel.setCustomerTel(customer.getCustomerPhoneNumber());
        orderSearchQueryModel.setCustomerMail(customer.getCustomerMail());
        orderSearchQueryModel.setCustomerBirthday(customer.getCustomerBirthday());
        orderSearchQueryModel.setCustomerSex(customer.getCustomerSex());

        String customerAgeType = getCustomerAgeType(customer.getCustomerBirthday());
        orderSearchQueryModel.setCustomerAgeType(customerAgeType);

    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param paymentMethod         決済方法
     */
    public void settingOrderSearchQueryModelByPaymentMethod(OrderSearchQueryModel orderSearchQueryModel,
                                                            PaymentMethod paymentMethod) {

        orderSearchQueryModel.setPaymentMethodId(paymentMethod.getPaymentMethodId());
    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     * @param billingSlip           請求伝票
     */
    public void settingOrderSearchQueryModelByBillingSlip(OrderSearchQueryModel orderSearchQueryModel,
                                                          BillingSlip billingSlip) {

        orderSearchQueryModel.setPaymentMethodName(billingSlip.getPaymentMethodName());
        orderSearchQueryModel.setLinkPaymentMethodName(billingSlip.getLinkPaymentMethodName());

        if (billingSlip.getMoneyReceiptTime() != null) {
            Timestamp receiptDteTimeString = new Timestamp(billingSlip.getMoneyReceiptTime().getTime());
            orderSearchQueryModel.setPaymentDateAndTime(receiptDteTimeString);
        }

        if (billingSlip.getLaterDateLimit() != null) {
            orderSearchQueryModel.setPaymentTimeLimitDate(billingSlip.getLaterDateLimit());
        }
    }

    /**
     * 受注検索クエリモデル設定
     *
     * @param orderReceived         受注
     * @param orderReceivedCount    受注件数
     * @param paymentMethod         決済方法
     * @param orderSearchQueryModel 受注検索クエリーモデル
     */
    public void settingOrderSearchQueryModelByNecessaryConditions(OrderReceived orderReceived,
                                                                  OrderReceivedCount orderReceivedCount,
                                                                  PaymentMethod paymentMethod,
                                                                  OrderSearchQueryModel orderSearchQueryModel) {
        if (orderReceivedCount.getOrderReceivedCount() > 1) {
            orderSearchQueryModel.setRepeatPurchaseType(REPEAT_PURCHASE_TYPE_2);

        } else {
            orderSearchQueryModel.setRepeatPurchaseType(REPEAT_PURCHASE_TYPE_1);

        }

        if (PRE_CLAIM.equals(paymentMethod.getBillType()) || Boolean.TRUE.equals(orderReceived.getShippedFlag())) {
            orderSearchQueryModel.setBillStatus(BILL_CLAIM);
        } else {
            orderSearchQueryModel.setBillStatus(BILL_NO_CLAIM);
        }

        if (PAYMENT_ERROR.equals(orderReceived.getOrderStatus())) {
            orderSearchQueryModel.setEmergencyFlag(ON);
        } else {
            orderSearchQueryModel.setEmergencyFlag(OFF);
        }
    }

    /**
     * 受注商品リスト取得
     *
     * @param orderSlip          注文票
     * @param productDetailsList 商品詳細リスト
     * @param salesSlip          販売伝票
     * @param examKitList        検査キットリスト
     * @return 受注商品リスト
     */
    public List<OrderProduct> getOrderProductList(OrderSlip orderSlip,
                                                  List<ProductDetails> productDetailsList,
                                                  SalesSlip salesSlip,
                                                  List<ExamKit> examKitList) {

        List<OrderProduct> orderProductList = new ArrayList<>();

        // 注文票と販売伝票と商品マスタをもとに組み立てつつ、注文票の連番と一致するインサート用の受注商品リストを生成
        Map<Integer, SalesSlipProductInformation> salesSlipProductInformationMap = new HashMap<>();

        Map<Integer, ProductDetails> productDetailsInformationMap = new HashMap<>();

        productDetailsList.forEach(item -> {
            Integer itemId = item.getGoodsSeq();
            productDetailsInformationMap.put(itemId, item);
        });

        salesSlip.getItemPriceList().forEach(item -> {

            Integer salesItemSeq = item.getSalesItemSeq();

            SalesSlipProductInformation salesSlipProductInformation = new SalesSlipProductInformation();

            salesSlipProductInformation.setGoodsPrice(item.getItemUnitPrice());
            salesSlipProductInformation.setGoodsCount(item.getItemCount());
            salesSlipProductInformation.setSummaryPrice(item.getItemPriceSubTotal());
            salesSlipProductInformation.setGoodsPriceTotal(salesSlip.getGoodsPriceTotal());
            salesSlipProductInformation.setItemTaxRate(item.getItemTaxRate());

            salesSlipProductInformationMap.put(salesItemSeq, salesSlipProductInformation);
        });

        Map<String, ExamKit> examKitCodeMap = new HashMap<>();
        examKitList.forEach(examKit -> examKitCodeMap.put(examKit.getOrderItemId(), examKit));

        orderSlip.getItemList().forEach(item -> {

            Integer orderItemSeq = item.getOrderItemSeq();
            Integer itemId = Integer.parseInt(item.getItemId());

            OrderProduct orderProduct = new OrderProduct();

            if (!ObjectUtils.isEmpty(productDetailsInformationMap.get(itemId))) {

                ProductDetails productDetails = productDetailsInformationMap.get(itemId);

                orderProduct.setGoodsGroupCode(productDetails.getGoodsGroupCode());
                orderProduct.setGoodsCode(productDetails.getGoodsCode());
                orderProduct.setSaleStartTime(productDetails.getSaleStartTime());
                orderProduct.setOrderSetting1(productDetails.getOrderSetting1());
                orderProduct.setOrderSetting2(productDetails.getOrderSetting2());
                orderProduct.setOrderSetting3(productDetails.getOrderSetting3());
                orderProduct.setOrderSetting4(productDetails.getOrderSetting4());
                orderProduct.setOrderSetting5(productDetails.getOrderSetting5());
                orderProduct.setOrderSetting6(productDetails.getOrderSetting6());
                orderProduct.setOrderSetting7(productDetails.getOrderSetting7());
                orderProduct.setOrderSetting8(productDetails.getOrderSetting8());
                orderProduct.setOrderSetting9(productDetails.getOrderSetting9());
                orderProduct.setOrderSetting10(productDetails.getOrderSetting10());
            }

            orderProduct.setGoodsGroupName(item.getItemName());
            orderProduct.setUnitValue1(item.getUnitValue1());
            orderProduct.setUnitValue2(item.getUnitValue2());
            orderProduct.setJanCode(item.getJanCode());
            orderProduct.setNoveltyGoodsType(item.getNoveltyGoodsType());
            orderProduct.setOrderItemId(item.getOrderItemId());

            ExamKit examKit = examKitCodeMap.get(item.getOrderItemId());
            if (examKit != null) {
                orderProduct.setExamKitCode(examKit.getExamKitCode());
                orderProduct.setExamStatus(examKit.getExamStatus());
                orderProduct.setSpecimenCode(examKit.getSpecimenCode());
                orderProduct.setReceptionDate(examKit.getReceptionDate());
                orderProduct.setSpecimenComment(examKit.getSpecimenComment());
                orderProduct.setExamResultsPdf(examKit.getExamResultsPdf());
            }

            if (!ObjectUtils.isEmpty(salesSlipProductInformationMap.get(orderItemSeq))) {

                SalesSlipProductInformation salesSlipProductInformation =
                                salesSlipProductInformationMap.get(orderItemSeq);

                orderProduct.setGoodsPrice(salesSlipProductInformation.getGoodsPrice());
                orderProduct.setTaxRate(
                                new BigDecimal(decimalFormat.format(salesSlipProductInformation.getItemTaxRate())));
                orderProduct.setGoodsCount(salesSlipProductInformation.getGoodsCount());
                orderProduct.setSummaryPrice(salesSlipProductInformation.getSummaryPrice());
            }

            orderProductList.add(orderProduct);
        });

        return orderProductList;
    }

    /**
     * お客様年代取得
     *
     * @param birthday お客様生年月日
     * @return string
     */
    public String getCustomerAgeType(Date birthday) {

        if (birthday == null) {
            return UNKNOWN;
        }

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 現在日付を取得
        int currentDate = Integer.parseInt(dateUtility.getCurrentYmd());
        // 生年月日をフォーマット（yyyyMMdd）
        int birthdayDate = Integer.parseInt(dateUtility.formatYmd(conversionUtility.toTimestamp(birthday)));
        // 年齢を算出
        int age = (currentDate - birthdayDate) / 10000;

        if (age < 10) {
            return AGE_0TO9;
        } else if (age < 20) {
            return AGE_10TO19;
        } else if (age < 30) {
            return AGE_20TO29;
        } else if (age < 40) {
            return AGE_30TO39;
        } else if (age < 50) {
            return AGE_40TO49;
        } else if (age < 60) {
            return AGE_50TO59;
        } else if (age < 70) {
            return AGE_60TO69;
        } else if (age < 80) {
            return AGE_70TO79;
        } else if (age < 90) {
            return AGE_80TO89;
        } else if (age < 100) {
            return AGE_90TO99;
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 検索用氏名
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     */
    public void searchNameEmUc(OrderSearchQueryModel orderSearchQueryModel) {
        String searchNameEmUc = "";
        if (orderSearchQueryModel.getBillingLastName() != null) {
            searchNameEmUc += orderSearchQueryModel.getBillingLastName();
        }

        if (orderSearchQueryModel.getBillingFirstName() != null) {
            searchNameEmUc += orderSearchQueryModel.getBillingFirstName();
        }

        if (orderSearchQueryModel.getBillingLastKana() != null) {
            if (StringUtils.isNotEmpty(searchNameEmUc)) {
                searchNameEmUc += EM_SPACE;
            }
            searchNameEmUc += orderSearchQueryModel.getBillingLastKana();
        }

        if (orderSearchQueryModel.getBillingFirstKana() != null) {
            searchNameEmUc += orderSearchQueryModel.getBillingFirstKana();
        }

        if (orderSearchQueryModel.getShippingLastName() != null) {
            if (StringUtils.isNotEmpty(searchNameEmUc)) {
                searchNameEmUc += EM_SPACE;
            }
            searchNameEmUc += orderSearchQueryModel.getShippingLastName();
        }

        if (orderSearchQueryModel.getShippingFirstName() != null) {
            searchNameEmUc += orderSearchQueryModel.getShippingFirstName();
        }

        if (orderSearchQueryModel.getShippingLastKana() != null) {
            if (StringUtils.isNotEmpty(searchNameEmUc)) {
                searchNameEmUc += EM_SPACE;
            }
            searchNameEmUc += orderSearchQueryModel.getShippingLastKana();
        }

        if (orderSearchQueryModel.getShippingFirstKana() != null) {
            searchNameEmUc += orderSearchQueryModel.getShippingFirstKana();
        }

        if (orderSearchQueryModel.getCustomerLastName() != null) {
            if (StringUtils.isNotEmpty(searchNameEmUc)) {
                searchNameEmUc += EM_SPACE;
            }
            searchNameEmUc += orderSearchQueryModel.getCustomerLastName();
        }

        if (orderSearchQueryModel.getCustomerFirstName() != null) {
            searchNameEmUc += orderSearchQueryModel.getCustomerFirstName();
        }

        if (orderSearchQueryModel.getCustomerLastKana() != null) {
            if (StringUtils.isNotEmpty(searchNameEmUc)) {
                searchNameEmUc += EM_SPACE;
            }
            searchNameEmUc += orderSearchQueryModel.getCustomerLastKana();
        }

        if (orderSearchQueryModel.getCustomerFirstKana() != null) {
            searchNameEmUc += orderSearchQueryModel.getCustomerFirstKana();
        }

        // 全角変換
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
        searchNameEmUc = zenHanConversionUtility.toZenkaku(searchNameEmUc);
        // 大文字変換
        searchNameEmUc = StringUtils.upperCase(searchNameEmUc);

        orderSearchQueryModel.setSearchNameEmUc(searchNameEmUc);
    }

    /**
     * 検索用電話番号
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     */
    public void searchTelEn(OrderSearchQueryModel orderSearchQueryModel) {
        String searchTelEn = "";
        if (orderSearchQueryModel.getBillingTel() != null) {
            searchTelEn += orderSearchQueryModel.getBillingTel();
        }

        if (orderSearchQueryModel.getShippingTel() != null) {
            if (StringUtils.isNotEmpty(searchTelEn)) {
                searchTelEn += EN_SPACE;
            }
            searchTelEn += orderSearchQueryModel.getShippingTel();
        }

        if (orderSearchQueryModel.getCustomerTel() != null) {
            if (StringUtils.isNotEmpty(searchTelEn)) {
                searchTelEn += EN_SPACE;
            }
            searchTelEn += orderSearchQueryModel.getCustomerTel();
        }
        // 半角変換
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
        searchTelEn = zenHanConversionUtility.toHankaku(searchTelEn);

        orderSearchQueryModel.setSearchTelEn(searchTelEn);
    }

    /**
     * ゆうプリR用郵送種別判定
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     */
    public void youPackJudgement(OrderSearchQueryModel orderSearchQueryModel) {

        // 注文された製品の数を計算する
        // ※ノベルティ商品は個数計算に含めない
        int numberOfOrderProduct = orderSearchQueryModel.getOrderProductList()
                                                        .stream()
                                                        .filter(item -> item.getNoveltyGoodsType()
                                                                            .equals(HTypeNoveltyGoodsType.NORMAL_GOODS.getValue()))
                                                        .mapToInt(OrderProduct::getGoodsCount)
                                                        .sum();
        if (numberOfOrderProduct <= 1) {
            // 1個以下
            orderSearchQueryModel.setYouPackType(Integer.valueOf(HTypeYouPackType.YOU_PACKET.getValue()));
        } else {
            // 複数個
            orderSearchQueryModel.setYouPackType(Integer.valueOf(HTypeYouPackType.YOU_PACK.getValue()));
        }

    }
}