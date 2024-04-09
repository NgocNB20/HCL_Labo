package jp.co.itechh.quad.ddd.usecase.reports;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.AccessDeviceUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.CopyUtil;
import jp.co.itechh.quad.core.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderSalesProcessingStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOrderSalesStatus;
import jp.co.itechh.quad.core.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.TransactionForRevision;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.MulpayBill;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductCategory;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDisplays;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.MailMagazineSubscriptionFlag;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSlipProductInformation;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.SalesSlipProductInformation;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ProductReportsInformation;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsBilling;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsCoupon;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsCustomer;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsDiscount;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsPayment;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsPrice;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsProduct;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsProductItem;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsShipping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 集計用販売データ登録更新用Helperクラス
 */
@Component
public class ReportRegistUseCaseHelper {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRegistUseCaseHelper.class);

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * アクセスデバイスの解析用Utility
     */
    private final AccessDeviceUtility accessDeviceUtility;

    /**
     * カンマ区切り
     */
    private final String COMMA_DELIMITER = ", ";

    /**
     * キャンセル
     */
    private final String CANCEL_STATUS = "CANCEL";

    /**
     * コンストラクタ
     *
     * @param conversionUtility   変換ユーティリティクラス
     * @param accessDeviceUtility アクセスデバイスの解析用Utility
     */
    @Autowired
    public ReportRegistUseCaseHelper(ConversionUtility conversionUtility, AccessDeviceUtility accessDeviceUtility) {
        this.conversionUtility = conversionUtility;
        this.accessDeviceUtility = accessDeviceUtility;
    }

    /**
     * 販売データクエリモデル設定 : 会員情報
     *
     * @param reportsQueryModel            集計用販売データ
     * @param previousReportsQueryModel    以前の集計販売データ
     * @param customer                     会員
     * @param customerAddress              会員の住所録
     * @param orderReceivedCount           受注件数
     * @param mailMagazineSubscriptionFlag メールマガジン購読フラグ
     */
    public void settingCustomerInfoReportsQueryModel(ReportsQueryModel reportsQueryModel,
                                                     ReportsQueryModel previousReportsQueryModel,
                                                     Customer customer,
                                                     AddressBook customerAddress,
                                                     OrderReceivedCount orderReceivedCount,
                                                     MailMagazineSubscriptionFlag mailMagazineSubscriptionFlag) {
        ReportsCustomer reportsCustomer = new ReportsCustomer();
        reportsCustomer.setCustomerId(customer.getCustomerId());
        reportsCustomer.setCustomerName(
                        customer.getCustomerLastName().concat(" ").concat(customer.getCustomerFirstName()));
        reportsCustomer.setCustomerMail(customer.getCustomerMail());
        reportsCustomer.setCustomerBirthday(conversionUtility.toYmd(customer.getCustomerBirthday()));
        reportsCustomer.setCustomerSex(customer.getCustomerSex());
        reportsCustomer.setCustomerAge(this.calcCustomerAge(customer.getCustomerBirthday()));
        reportsCustomer.setCustomerZipCode(customerAddress.getZipCode());
        reportsCustomer.setCustomerPrefecture(customerAddress.getPrefecture());
        reportsCustomer.setCustomerAddress1(customerAddress.getAddress1());
        reportsCustomer.setCustomerAddress2(customerAddress.getAddress2());
        reportsCustomer.setCustomerAddress3(customerAddress.getAddress3());

        if (previousReportsQueryModel != null) {
            reportsCustomer.setRepeatPurchaseType(previousReportsQueryModel.getCustomer().getRepeatPurchaseType());
            reportsCustomer.setMagazineSubscribeType(
                            previousReportsQueryModel.getCustomer().getMagazineSubscribeType());
        } else {
            if (!ObjectUtils.isEmpty(orderReceivedCount)) {
                if (orderReceivedCount.getOrderReceivedCount() == 1) {
                    reportsCustomer.setRepeatPurchaseType(HTypeRepeatPurchaseType.MEMBER_FIRST.getValue());
                } else if (orderReceivedCount.getOrderReceivedCount() >= 2) {
                    reportsCustomer.setRepeatPurchaseType(HTypeRepeatPurchaseType.MEMBER_REPEATER.getValue());
                }
            }

            reportsCustomer.setMagazineSubscribeType(mailMagazineSubscriptionFlag.getMailMagazine().getValue());
        }

        reportsQueryModel.setCustomer(reportsCustomer);
    }

    /**
     * 販売データクエリモデル設定 : 共通情報
     *
     * @param reportsQueryModel      集計用販売データ
     * @param orderReceived          受注
     * @param transactionRevisionId  取引ID
     * @param transactionForRevision 改訂用取引
     * @param orderSlip              注文票
     * @param isRecentlyShipped      注文が最近出荷
     * @param shippingSlip           配送伝票
     */
    public void settingCommonInfoQueryModel(ReportsQueryModel reportsQueryModel,
                                            OrderReceived orderReceived,
                                            String transactionRevisionId,
                                            TransactionForRevision transactionForRevision,
                                            OrderSlip orderSlip,
                                            Boolean isRecentlyShipped,
                                            ShippingSlip shippingSlip) {

        // プロセスステータスの決定
        if (StringUtils.isEmpty(transactionRevisionId) || isRecentlyShipped) {
            reportsQueryModel.setProcessStatus(HTypeOrderSalesProcessingStatus.NEW.getValue());
        } else if (CANCEL_STATUS.equalsIgnoreCase(orderReceived.getOrderStatus())) {
            reportsQueryModel.setProcessStatus(HTypeOrderSalesProcessingStatus.CANCEL.getValue());
        } else {
            reportsQueryModel.setProcessStatus(HTypeOrderSalesProcessingStatus.UPDATE.getValue());
        }

        // 処理時間の決定
        if (!ObjectUtils.isEmpty(transactionForRevision) && !isRecentlyShipped) {
            // 注文変更
            reportsQueryModel.setProcessTime(transactionForRevision.getProcessTime());
        } else if (!ObjectUtils.isEmpty(transactionForRevision) && isRecentlyShipped) {
            // 出荷登録
            if (shippingSlip.getCompleteShipmentDate() != null) {
                reportsQueryModel.setProcessTime(shippingSlip.getCompleteShipmentDate());
            } else {
                reportsQueryModel.setProcessTime(transactionForRevision.getProcessTime());
            }

        } else {
            // 新規注文
            reportsQueryModel.setProcessTime(orderReceived.getProcessTime());
        }
        reportsQueryModel.setOrderCode(orderReceived.getOrderCode());

        if (StringUtils.isNotEmpty(orderSlip.getUserAgent())) {
            HTypeDeviceType deviceType = accessDeviceUtility.getDeviceType(orderSlip.getUserAgent());
            reportsQueryModel.setOrderDevice(EnumTypeUtil.getValue(deviceType));
        }
    }

    /**
     * 販売データクエリモデル設定 : 取引
     *
     * @param reportsQueryModel      受注検索クエリーモデル
     * @param transactionForRevision 改訂用取引
     */
    public void settingReportsQueryModelByTransaction(ReportsQueryModel reportsQueryModel,
                                                      TransactionForRevision transactionForRevision) {
        // 新規注文の場合
        if (ObjectUtils.isEmpty(transactionForRevision)) {
            reportsQueryModel.setOrderStatus(HTypeOrderSalesStatus.ORDER.getValue());
            return;
        }

        if (Boolean.TRUE.equals(transactionForRevision.getShippedFlag())) {
            reportsQueryModel.setOrderStatus(HTypeOrderSalesStatus.SALES.getValue());
        } else if (Boolean.FALSE.equals(transactionForRevision.getShippedFlag())) {
            reportsQueryModel.setOrderStatus(HTypeOrderSalesStatus.ORDER.getValue());
        }
    }

    /**
     * 販売データクエリモデル設定 : 配送先情報
     *
     * @param reportsQueryModel 集計用販売データ
     * @param shippingSlip      配送伝票
     * @param shippingAddress   発送先住所
     */
    public void settingShippingReportsQueryModel(ReportsQueryModel reportsQueryModel,
                                                 ShippingSlip shippingSlip,
                                                 AddressBook shippingAddress) {
        ReportsShipping shipping = new ReportsShipping();

        shipping.setShippingMethodId(shippingSlip.getShippingMethodId());
        shipping.setShippingMethodName(shippingSlip.getShippingMethodName());
        shipping.setShippingName(shippingAddress.getLastName().concat(" ").concat(shippingAddress.getFirstName()));
        shipping.setShippingZipCode(shippingAddress.getZipCode());
        shipping.setShippingPrefecture(shippingAddress.getPrefecture());
        shipping.setShippingAddress1(shippingAddress.getAddress1());
        shipping.setShippingAddress2(shippingAddress.getAddress2());
        shipping.setShippingAddress3(shippingAddress.getAddress3());
        shipping.setShippingAddressId(shippingSlip.getShippingAddressId());

        reportsQueryModel.setShipping(shipping);
    }

    /**
     * 販売データクエリモデル設定 : 請求先情報
     *
     * @param reportsQueryModel 集計用販売データ
     * @param billingSlip       請求伝票
     * @param billingAddress    請求先住所
     */
    public void settingBillingReportsQueryModel(ReportsQueryModel reportsQueryModel,
                                                BillingSlip billingSlip,
                                                AddressBook billingAddress) {
        ReportsBilling billing = new ReportsBilling();

        billing.setBillingName(billingAddress.getLastName().concat(" ").concat(billingAddress.getFirstName()));
        billing.setBillingZipCode(billingAddress.getZipCode());
        billing.setBillingPrefecture(billingAddress.getPrefecture());
        billing.setBillingAddress1(billingAddress.getAddress1());
        billing.setBillingAddress2(billingAddress.getAddress2());
        billing.setBillingAddress3(billingAddress.getAddress3());
        billing.setBillingAddressId(billingSlip.getBillingAddressId());

        reportsQueryModel.setBilling(billing);
    }

    /**
     * 販売データクエリモデル設定 : 決済情報
     *
     * @param reportsQueryModel 集計用販売データ
     * @param mulpayBill        マルチペイメント
     */
    public void settingPaymentReportsQueryModel(ReportsQueryModel reportsQueryModel,
                                                BillingSlip billingSlip,
                                                MulpayBill mulpayBill) {
        // 集計用リンク決済ID
        String linkPaymentId = PropertiesUtil.getSystemPropertiesValue("aggregation.pay.id.linkpayment");

        ReportsPayment payment = new ReportsPayment();

        payment.setPaymentMethodId(billingSlip.getPaymentMethodId());
        payment.setPaymentMethodName(billingSlip.getPaymentMethodName());
        payment.setPayMethod(billingSlip.getLinkPayMethod());
        if (billingSlip.getPayType() != null) {
            try {
                payment.setPayType(Integer.valueOf(billingSlip.getPayType()));
                payment.setAggregationPayId(Integer.valueOf(linkPaymentId + billingSlip.getPayType()));
                payment.setAggregationPayName(billingSlip.getLinkPaymentMethodName());
            } catch (NumberFormatException ignored) {
                // 集計データの登録時にpayTypeの変換に失敗したため、リンク決済以外の決済方法として登録
                LOGGER.error("集計データの登録時にpayTypeの変換に失敗したため、リンク決済以外の決済方法として登録");
                payment.setAggregationPayId(billingSlip.getPaymentMethodId());
                payment.setAggregationPayName(billingSlip.getPaymentMethodName());
            }
        } else {
            payment.setAggregationPayId(billingSlip.getPaymentMethodId());
            payment.setAggregationPayName(billingSlip.getPaymentMethodName());
        }
        payment.setPayTypeName(billingSlip.getLinkPaymentMethodName());
        payment.setForward(mulpayBill.getForward());
        payment.setCvsCode(mulpayBill.getConvenience());

        reportsQueryModel.setPayment(payment);
    }

    /**
     * 販売データクエリモデル設定 : クーポン
     *
     * @param reportsQueryModel 集計用販売データ
     * @param couponSlips       クーポン
     * @param salesSlip         販売伝票
     */
    public void settingCouponReportsQueryModel(ReportsQueryModel reportsQueryModel,
                                               Coupon couponSlips,
                                               SalesSlip salesSlip) {
        ReportsCoupon coupon = new ReportsCoupon();

        coupon.setCouponName(couponSlips.getCouponName());
        coupon.setCouponSeq(couponSlips.getCouponSeq());
        coupon.setCouponVersionNo(salesSlip.getCouponVersionNo());

        reportsQueryModel.setDiscount(new ReportsDiscount());
        reportsQueryModel.getDiscount().setCoupon(coupon);
    }

    /**
     * 販売データクエリモデル設定 : 金額情報
     *
     * @param reportsQueryModel       マルチペイメント
     * @param salesSlip               販売伝票
     * @param salesSlipBeforeRevision 改訂前の売上伝票
     * @param salesSlipBeforeRevision 改訂前の売上伝票
     * @param isShippedRecently       注文が最近出荷
     */
    public void settingPriceReportsQueryModel(ReportsQueryModel reportsQueryModel,
                                              SalesSlip salesSlip,
                                              SalesSlip salesSlipBeforeRevision,
                                              Boolean isShippedRecently) {
        ReportsPrice orderPrice = new ReportsPrice();

        if (!ObjectUtils.isEmpty(salesSlipBeforeRevision) && !isShippedRecently) {
            orderPrice.setItemSalesPriceTotal(
                            salesSlip.getGoodsPriceTotal() - salesSlipBeforeRevision.getGoodsPriceTotal());
            orderPrice.setCarriage(salesSlip.getCarriage() - salesSlipBeforeRevision.getCarriage());
            orderPrice.setCommission(salesSlip.getCommission() - salesSlipBeforeRevision.getCommission());

            int totalAdjustmentAmount = salesSlip.getAdjustmentAmountList()
                                                 .stream()
                                                 .mapToInt(AdjustmentAmount::getAdjustPrice)
                                                 .sum();
            int beforeTotalAdjustmentAmount = salesSlipBeforeRevision.getAdjustmentAmountList()
                                                                     .stream()
                                                                     .mapToInt(AdjustmentAmount::getAdjustPrice)
                                                                     .sum();
            orderPrice.setOtherPrice(totalAdjustmentAmount - beforeTotalAdjustmentAmount);

            int taxPrice = salesSlip.getStandardTax() + salesSlip.getReducedTax();
            int beforeTaxPrice = salesSlipBeforeRevision.getStandardTax() + salesSlipBeforeRevision.getReducedTax();
            orderPrice.setTax(taxPrice - beforeTaxPrice);
            orderPrice.setCouponPaymentPrice(this.settingCouponPrice(salesSlip, salesSlipBeforeRevision));
            orderPrice.setOrderPrice(salesSlip.getBillingAmount() - salesSlipBeforeRevision.getBillingAmount());
        } else {
            orderPrice.setItemSalesPriceTotal(salesSlip.getGoodsPriceTotal());
            orderPrice.setCarriage(salesSlip.getCarriage());
            orderPrice.setCommission(salesSlip.getCommission());

            int totalAdjustmentAmount = salesSlip.getAdjustmentAmountList()
                                                 .stream()
                                                 .mapToInt(AdjustmentAmount::getAdjustPrice)
                                                 .sum();
            orderPrice.setOtherPrice(totalAdjustmentAmount);

            int taxPrice = salesSlip.getStandardTax() + salesSlip.getReducedTax();
            orderPrice.setTax(taxPrice);

            orderPrice.setCouponPaymentPrice(this.settingCouponPrice(salesSlip));
            orderPrice.setOrderPrice(salesSlip.getBillingAmount());
        }

        reportsQueryModel.setPrice(orderPrice);
    }

    /**
     * 販売データクエリモデル設定 : 注文商品リスト情報
     *
     * @param reportsQueryModel 集計用販売データ
     * @param orderProductList  受注商品一覧
     */
    public void settingProductReportsItemsQueryModel(ReportsQueryModel reportsQueryModel,
                                                     List<ReportsProduct> orderProductList) {
        List<ReportsProductItem> reportsProductItems = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(orderProductList)) {
            orderProductList.forEach(item -> {
                ReportsProductItem productItem = new ReportsProductItem();

                productItem.setGoodsGroupCode(item.getGoodsGroupCode());
                productItem.setGoodsCode(item.getGoodsCode());
                productItem.setGoodsName(item.getGoodsGroupName());
                productItem.setUnitValue1(item.getUnitValue1());
                productItem.setUnitValue2(item.getUnitValue2());
                productItem.setUnitTitle1(item.getUnitTitle1());
                productItem.setUnitTitle2(item.getUnitTitle2());
                productItem.setJanCode(item.getJanCode());
                productItem.setNoveltyGoodsType(item.getNoveltyGoodsType());
                productItem.setUnitPrice(item.getGoodsPrice());
                productItem.setTaxRate(item.getTaxRate().intValue());
                productItem.setSalesCount(item.getSalesCount());
                productItem.setCancelCount(item.getCancelCount());
                productItem.setCategoryId(item.getCategoryId());
                productItem.setGoodsTag(item.getGoodsTag());
                productItem.setCategoryName(item.getCategoryName());
                productItem.setIconId(item.getIconId());
                productItem.setIconName(item.getIconName());

                reportsProductItems.add(productItem);
            });
        }

        reportsQueryModel.setOrderItemList(reportsProductItems);
    }

    /**
     * 受注商品リスト取得
     *
     * @param orderSlip           注文票
     * @param productDetailsList  商品詳細リスト
     * @param salesSlip           販売伝票
     * @param productDisplaysList 商品展示一覧
     * @param allCategoryList     全カテゴリ一覧
     * @return 受注商品リスト
     */
    public List<ReportsProduct> getOrderProductList(OrderSlip orderSlip,
                                                    List<ProductDetails> productDetailsList,
                                                    SalesSlip salesSlip,
                                                    List<ProductDisplays> productDisplaysList,
                                                    List<ProductCategory> allCategoryList) {

        List<ReportsProduct> reportsProductList = new ArrayList<>();

        Map<String, SalesSlipProductInformation> salesSlipProductInformationMap = new HashMap<>();

        List<ProductReportsInformation> productDisplayInformationList = new ArrayList<>();

        // 商品の販売情報を取得する
        productDetailsList.forEach(item -> {
            ProductReportsInformation productDisplayInformation = new ProductReportsInformation();

            productDisplayInformation.setGoodsSeq(String.valueOf(item.getGoodsSeq()));
            productDisplayInformation.setGoodsCode(item.getGoodsCode());
            productDisplayInformation.setGoodsGroupCode(item.getGoodsGroupCode());

            ProductDisplays productDisplay = productDisplaysList.stream()
                                                                .filter(displayItem -> StringUtils.isNotEmpty(
                                                                                displayItem.getGoodsGroupCode())
                                                                                       && displayItem.getGoodsGroupCode()
                                                                                                     .equals(item.getGoodsGroupCode()))
                                                                .findFirst()
                                                                .orElse(null);
            if (!ObjectUtils.isEmpty(productDisplay)) {
                productDisplayInformation.setIconList(productDisplay.getIconList());
                productDisplayInformation.setCategoryList(allCategoryList.stream()
                                                                         .filter(category -> productDisplay.getCategorySeqList()
                                                                                                           .contains(category.getCategorySeq()))
                                                                         .collect(Collectors.toList()));
                productDisplayInformation.setGoodsTagList(productDisplay.getGoodsTagList());
            }

            productDisplayInformationList.add(productDisplayInformation);

        });

        // 販売ドキュメントから製品販売情報を取得する
        salesSlip.getItemPriceList().forEach(item -> {

            String itemId = item.getItemId().concat(String.valueOf(item.getSalesItemSeq()));

            SalesSlipProductInformation salesSlipProductInformation = new SalesSlipProductInformation();

            salesSlipProductInformation.setGoodsPrice(item.getItemUnitPrice());
            salesSlipProductInformation.setSummaryPrice(item.getItemPriceSubTotal());
            salesSlipProductInformation.setGoodsPriceTotal(salesSlip.getGoodsPriceTotal());
            salesSlipProductInformation.setItemTaxRate(item.getItemTaxRate());

            salesSlipProductInformationMap.put(itemId, salesSlipProductInformation);
        });

        // 注文伝票から商品の販売情報を取得する
        orderSlip.getItemList().forEach(item -> {
            ReportsProduct reportsProduct = new ReportsProduct();

            // データを収集します
            reportsProduct.setGoodsSeq(item.getItemId());
            reportsProduct.setGoodsGroupName(item.getItemName());
            reportsProduct.setUnitValue1(item.getUnitValue1());
            reportsProduct.setUnitValue2(item.getUnitValue2());
            reportsProduct.setUnitTitle1(item.getUnitTitle1());
            reportsProduct.setUnitTitle2(item.getUnitTitle2());
            reportsProduct.setJanCode(item.getJanCode());
            reportsProduct.setNoveltyGoodsType(item.getNoveltyGoodsType());
            reportsProduct.setSalesCount(item.getItemCount());
            reportsProduct.setCancelCount(0);
            String itemMapping = item.getItemId().concat(String.valueOf(item.getOrderItemSeq()));

            if (!ObjectUtils.isEmpty(salesSlipProductInformationMap.get(itemMapping))) {

                SalesSlipProductInformation salesSlipProductInformation =
                                salesSlipProductInformationMap.get(itemMapping);

                reportsProduct.setGoodsPrice(salesSlipProductInformation.getGoodsPrice());
                reportsProduct.setTaxRate(salesSlipProductInformation.getItemTaxRate());
                reportsProduct.setGoodsPriceTotal(salesSlip.getGoodsPriceTotal());
            }

            ProductReportsInformation productDisplayInformation = productDisplayInformationList.stream()
                                                                                               .filter(displayItem -> StringUtils.isNotEmpty(
                                                                                                               displayItem.getGoodsSeq())
                                                                                                                      && displayItem.getGoodsSeq()
                                                                                                                                    .equals(item.getItemId()))
                                                                                               .findFirst()
                                                                                               .orElse(null);

            if (!ObjectUtils.isEmpty(productDisplayInformation)) {

                reportsProduct.setGoodsGroupCode(productDisplayInformation.getGoodsGroupCode());
                reportsProduct.setGoodsCode(productDisplayInformation.getGoodsCode());

                if (CollectionUtils.isNotEmpty(productDisplayInformation.getCategoryList())) {
                    reportsProduct.setCategoryId(productDisplayInformation.getCategoryList()
                                                                          .stream()
                                                                          .map(category -> String.valueOf(
                                                                                          category.getCategoryId()))
                                                                          .collect(Collectors.joining(
                                                                                          COMMA_DELIMITER)));
                    reportsProduct.setCategoryName(productDisplayInformation.getCategoryList()
                                                                            .stream()
                                                                            .map(category -> String.valueOf(
                                                                                            category.getCategoryName()))
                                                                            .collect(Collectors.joining(
                                                                                            COMMA_DELIMITER)));
                }

                if (CollectionUtils.isNotEmpty(productDisplayInformation.getGoodsTagList())) {
                    reportsProduct.setGoodsTag(productDisplayInformation.getGoodsTagList()
                                                                        .stream()
                                                                        .collect(Collectors.joining(COMMA_DELIMITER)));
                }

                if (CollectionUtils.isNotEmpty(productDisplayInformation.getIconList())) {
                    reportsProduct.setIconId(productDisplayInformation.getIconList()
                                                                      .stream()
                                                                      .map(icon -> String.valueOf(icon.getIconId()))
                                                                      .collect(Collectors.joining(COMMA_DELIMITER)));
                    reportsProduct.setIconName(productDisplayInformation.getIconList()
                                                                        .stream()
                                                                        .map(icon -> String.valueOf(icon.getIconName()))
                                                                        .collect(Collectors.joining(COMMA_DELIMITER)));
                }
            }

            reportsProductList.add(reportsProduct);
        });

        return reportsProductList;
    }

    /**
     * 注文変更時のデータ収集処理
     *
     * @param preReportsQueryModel
     * @param reportsProductAfterRevisionList
     * @param orderSlipBeforeRevision
     * @param salesSlipBeforeRevision
     * @param productSalesBeforeList
     * @param orderReceived
     * @param isShippedRecently
     * @return 受注商品リスト
     */
    protected List<ReportsProduct> executeForRevisionOrder(ReportsQueryModel preReportsQueryModel,
                                                           List<ReportsProduct> reportsProductAfterRevisionList,
                                                           OrderSlip orderSlipBeforeRevision,
                                                           SalesSlip salesSlipBeforeRevision,
                                                           List<ProductDetails> productSalesBeforeList,
                                                           OrderReceived orderReceived,
                                                           Boolean isShippedRecently) {

        Map<String, ReportsProduct> reportsAfterRevisionOrderMap = new HashMap<>();
        List<OrderSlipProductInformation> orderSlipBeforeRevisionInformationList = new ArrayList<>();

        // (1) 設定データ修正前の受注販売品
        orderSlipBeforeRevision.getItemList().forEach(item -> {
            OrderSlipProductInformation orderSlipProductInformation = new OrderSlipProductInformation();

            orderSlipProductInformation.setOrderItemSeq(item.getOrderItemSeq());
            orderSlipProductInformation.setGoodsGroupName(item.getItemName());
            orderSlipProductInformation.setUnitValue1(item.getUnitValue1());
            orderSlipProductInformation.setUnitValue2(item.getUnitValue2());
            orderSlipProductInformation.setUnitTitle1(item.getUnitTitle1());
            orderSlipProductInformation.setUnitTitle2(item.getUnitTitle2());
            orderSlipProductInformation.setItemCount(item.getItemCount());
            orderSlipProductInformation.setGoodsSeq(item.getItemId());
            orderSlipProductInformation.setJanCode(item.getJanCode());

            orderSlipBeforeRevisionInformationList.add(orderSlipProductInformation);
        });

        salesSlipBeforeRevision.getItemPriceList().forEach(item -> {
            orderSlipBeforeRevisionInformationList.forEach(o -> {
                if ((StringUtils.isNotEmpty(o.getGoodsSeq()) && o.getGoodsSeq().equals(item.getItemId())) && (
                                o.getOrderItemSeq() != null && o.getOrderItemSeq().equals(item.getSalesItemSeq()))) {
                    o.setUnitPrice(item.getItemUnitPrice());
                }
            });
        });

        productSalesBeforeList.forEach(item -> {
            orderSlipBeforeRevisionInformationList.forEach(o -> {
                if (Integer.valueOf(o.getGoodsSeq()).equals(item.getGoodsSeq())) {
                    o.setGoodsGroupCode(item.getGoodsGroupCode());
                }
            });
        });

        orderSlipBeforeRevisionInformationList.forEach(o -> {
            ReportsProductItem beforeReportsProductItem = preReportsQueryModel.getOrderItemList()
                                                                              .stream()
                                                                              .filter(item -> StringUtils.isNotEmpty(
                                                                                              item.getGoodsGroupCode())
                                                                                              && item.getGoodsGroupCode()
                                                                                                     .equals(o.getGoodsGroupCode()))
                                                                              .findFirst()
                                                                              .orElse(null);

            if (beforeReportsProductItem != null) {
                o.setCategoryId(beforeReportsProductItem.getCategoryId());
                o.setCategoryName(beforeReportsProductItem.getCategoryName());
                o.setGoodsTag(beforeReportsProductItem.getGoodsTag());
                o.setIconId(beforeReportsProductItem.getIconId());
                o.setIconName(beforeReportsProductItem.getIconName());
            }
        });

        // (2)
        // 受注販売改定商品マップの作成
        Map<String, ReportsProduct> uniqueReportsProductMap =
                        this.getUniqueOrderSalesItem(reportsProductAfterRevisionList);
        // 改訂前の受注販売商品マップの作成
        Map<String, OrderSlipProductInformation> uniqueOrderSlipBeforeProductMap =
                        this.getUniqueOrderSlipItemBeforeRevision(orderSlipBeforeRevisionInformationList);

        // (3) 売上枚数判定
        uniqueReportsProductMap.forEach((key, value) -> {
            OrderSlipProductInformation orderSlipProductInformation = uniqueOrderSlipBeforeProductMap.get(key);

            if (!ObjectUtils.isEmpty(orderSlipProductInformation) && !CANCEL_STATUS.equals(
                            orderReceived.getOrderStatus()) && !isShippedRecently) {
                //====================
                //  改訂オーダー用
                //====================
                int quantityDiff = value.getSalesCount() - orderSlipProductInformation.getItemCount();

                if (quantityDiff >= 0) {
                    value.setSalesCount(quantityDiff);
                } else {
                    value.setSalesCount(0);
                    value.setCancelCount(Math.abs(quantityDiff));
                }
                value.setCategoryId(orderSlipProductInformation.getCategoryId());
                value.setCategoryName(orderSlipProductInformation.getCategoryName());
                value.setIconId(orderSlipProductInformation.getIconId());
                value.setIconName(orderSlipProductInformation.getIconName());
                value.setGoodsTag(orderSlipProductInformation.getGoodsTag());

            } else if (CANCEL_STATUS.equals(orderReceived.getOrderStatus())) {
                //====================
                // 注文キャンセルの場合
                //====================
                List<ReportsProduct> reportsCancelAddedList = reportsAfterRevisionOrderMap.values()
                                                                                          .stream()
                                                                                          .filter(item -> StringUtils.isNotEmpty(
                                                                                                          item.getGoodsSeq())
                                                                                                          && item.getGoodsSeq()
                                                                                                                 .equals(value.getGoodsSeq()))
                                                                                          .collect(Collectors.toList());
                if (reportsCancelAddedList.size() > 0) {
                    return;
                }

                List<OrderSlipProductInformation> orderProductBeforeCancelList =
                                uniqueOrderSlipBeforeProductMap.values()
                                                               .stream()
                                                               .filter(item -> StringUtils.isNotEmpty(
                                                                               item.getGoodsSeq()) && item.getGoodsSeq()
                                                                                                          .equals(value.getGoodsSeq()))
                                                               .collect(Collectors.toList());

                orderProductBeforeCancelList.forEach(orderCancel -> {
                    ReportsProduct reportsProductCancel = CopyUtil.deepCopy(value);

                    String uniqueCancelKey = orderCancel.getGoodsSeq()
                                                        .concat(orderCancel.getJanCode() == null ?
                                                                                "null" :
                                                                                orderCancel.getJanCode())
                                                        .concat(orderCancel.getGoodsGroupName())
                                                        .concat(orderCancel.getUnitTitle1() == null ?
                                                                                "null" :
                                                                                orderCancel.getUnitTitle1())
                                                        .concat(orderCancel.getUnitTitle2() == null ?
                                                                                "null" :
                                                                                orderCancel.getUnitTitle2())
                                                        .concat(orderCancel.getUnitValue1() == null ?
                                                                                "null" :
                                                                                orderCancel.getUnitValue1())
                                                        .concat(orderCancel.getUnitValue2() == null ?
                                                                                "null" :
                                                                                orderCancel.getUnitValue2())
                                                        .concat(String.valueOf(orderCancel.getUnitPrice()));
                    reportsProductCancel.setCategoryId(orderCancel.getCategoryId());
                    reportsProductCancel.setCategoryName(orderCancel.getCategoryName());
                    reportsProductCancel.setIconId(orderCancel.getIconId());
                    reportsProductCancel.setIconName(orderCancel.getIconName());
                    reportsProductCancel.setGoodsTag(orderCancel.getGoodsTag());
                    reportsProductCancel.setGoodsPrice(orderCancel.getUnitPrice());
                    reportsProductCancel.setGoodsGroupName(orderCancel.getGoodsGroupName());
                    reportsProductCancel.setJanCode(orderCancel.getJanCode());
                    reportsProductCancel.setUnitTitle1(orderCancel.getUnitTitle1());
                    reportsProductCancel.setUnitTitle2(orderCancel.getUnitTitle2());
                    reportsProductCancel.setUnitValue1(orderCancel.getUnitValue1());
                    reportsProductCancel.setUnitValue2(orderCancel.getUnitValue2());
                    reportsProductCancel.setSalesCount(0);
                    reportsProductCancel.setCancelCount(orderCancel.getItemCount());
                    reportsAfterRevisionOrderMap.put(uniqueCancelKey, reportsProductCancel);
                });

            } else if (!ObjectUtils.isEmpty(orderSlipProductInformation) && isShippedRecently && !CANCEL_STATUS.equals(
                            orderReceived.getOrderStatus())) {
                //====================
                // 注文が最近出荷されたとき
                //====================

                // 注文商品のアイテム数が ０の場合は無視します
                if (orderSlipProductInformation.getItemCount() == 0)
                    return;

                value.setSalesCount(orderSlipProductInformation.getItemCount());
                value.setCancelCount(0);
                value.setCategoryId(orderSlipProductInformation.getCategoryId());
                value.setCategoryName(orderSlipProductInformation.getCategoryName());
                value.setIconId(orderSlipProductInformation.getIconId());
                value.setIconName(orderSlipProductInformation.getIconName());
                value.setGoodsTag(orderSlipProductInformation.getGoodsTag());
            }

            if (!CANCEL_STATUS.equals(orderReceived.getOrderStatus())) {
                reportsAfterRevisionOrderMap.put(key, value);
            }
        });

        return new ArrayList<>(reportsAfterRevisionOrderMap.values());
    }

    /**
     * オーダー商品の売上をユニークキーでマージ : 修正注文後
     *
     * @param reportsProductList
     * @return ユニークキーの受注販売商品マップ
     */
    protected Map<String, ReportsProduct> getUniqueOrderSalesItem(List<ReportsProduct> reportsProductList) {
        Set<String> afterUniqueListId = new HashSet<>();
        Map<String, ReportsProduct> uniqueAfterSalesProductMap = new HashMap<>();

        // から一意の ID を作成する商品名、規格1表示名、規格2表示名、規格1、規格2、JANコード、商品単価
        reportsProductList.forEach(item -> {
            String uniqueId = item.getGoodsSeq()
                                  .concat(item.getJanCode() == null ? "null" : item.getJanCode())
                                  .concat(item.getGoodsGroupName())
                                  .concat(item.getUnitTitle1() == null ? "null" : item.getUnitTitle1())
                                  .concat(item.getUnitTitle2() == null ? "null" : item.getUnitTitle2())
                                  .concat(item.getUnitValue1() == null ? "null" : item.getUnitValue1())
                                  .concat(item.getUnitValue2() == null ? "null" : item.getUnitValue2())
                                  .concat(String.valueOf(item.getGoodsPrice()));

            if (afterUniqueListId.contains(uniqueId)) {
                int currentSalesCount = uniqueAfterSalesProductMap.get(uniqueId).getSalesCount();
                uniqueAfterSalesProductMap.get(uniqueId).setSalesCount(item.getSalesCount() + currentSalesCount);
            } else {
                uniqueAfterSalesProductMap.put(uniqueId, item);
            }
            afterUniqueListId.add(uniqueId);
        });

        return uniqueAfterSalesProductMap;
    }

    /**
     * オーダー商品の売上をユニークキーでマージ : 変更前
     *
     * @param orderSlipProductInformationList
     * @return ユニークキーの受注販売商品マップ
     */
    protected Map<String, OrderSlipProductInformation> getUniqueOrderSlipItemBeforeRevision(List<OrderSlipProductInformation> orderSlipProductInformationList) {
        Set<String> beforeUniqueListId = new HashSet<>();
        Map<String, OrderSlipProductInformation> uniqueBeforeSalesProductMap = new HashMap<>();

        // から一意の ID を作成する商品名、規格1表示名、規格2表示名、規格1、規格2、JANコード、商品単価
        orderSlipProductInformationList.forEach(item -> {
            String uniqueId = item.getGoodsSeq()
                                  .concat(item.getJanCode() == null ? "null" : item.getJanCode())
                                  .concat(item.getGoodsGroupName())
                                  .concat(item.getUnitTitle1() == null ? "null" : item.getUnitTitle1())
                                  .concat(item.getUnitTitle2() == null ? "null" : item.getUnitTitle2())
                                  .concat(item.getUnitValue1() == null ? "null" : item.getUnitValue1())
                                  .concat(item.getUnitValue2() == null ? "null" : item.getUnitValue2())
                                  .concat(String.valueOf(item.getUnitPrice()));

            if (beforeUniqueListId.contains(uniqueId)) {
                int currentItemCount = uniqueBeforeSalesProductMap.get(uniqueId).getItemCount();
                uniqueBeforeSalesProductMap.get(uniqueId).setItemCount(item.getItemCount() + currentItemCount);
            } else {
                uniqueBeforeSalesProductMap.put(uniqueId, item);
            }
            beforeUniqueListId.add(uniqueId);
        });

        return uniqueBeforeSalesProductMap;
    }

    /**
     * 顧客の年齢を計算する
     *
     * @param birthday お客様の生年月日
     * @return 顧客の年齢
     */
    protected Integer calcCustomerAge(Date birthday) {
        if (birthday == null) {
            return 0;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate from = LocalDate.parse(formatter.format(birthday));
        LocalDate currentTime = LocalDate.now();

        return Period.between(from, currentTime).getYears();
    }

    /**
     * クーポン：の設定額割引
     *
     * @param salesSlip
     * @return 使用フラグに基づく割引額
     */
    protected int settingCouponPrice(SalesSlip salesSlip) {
        if (salesSlip.getCouponUseFlag() != null && salesSlip.getCouponPaymentPrice() != null) {
            return salesSlip.getCouponUseFlag() ?
                            -salesSlip.getCouponPaymentPrice() :
                            salesSlip.getCouponPaymentPrice();
        }

        return 0;
    }

    /**
     * クーポン：の設定額割引
     *
     * @param salesSlip
     * @param salesSlipBeforeRevision
     * @return 使用フラグに基づく割引額
     */
    protected int settingCouponPrice(SalesSlip salesSlip, SalesSlip salesSlipBeforeRevision) {
        if (salesSlip.getCouponUseFlag() != null && salesSlip.getCouponPaymentPrice() != null
            && salesSlipBeforeRevision.getCouponPaymentPrice() != null) {
            if (!salesSlip.getCouponUseFlag().equals(salesSlipBeforeRevision.getCouponUseFlag())) {
                return salesSlip.getCouponUseFlag() ?
                                -salesSlip.getCouponPaymentPrice() :
                                salesSlipBeforeRevision.getCouponPaymentPrice();
            } else {
                return salesSlipBeforeRevision.getCouponPaymentPrice() - salesSlip.getCouponPaymentPrice();
            }
        }

        return 0;
    }

}