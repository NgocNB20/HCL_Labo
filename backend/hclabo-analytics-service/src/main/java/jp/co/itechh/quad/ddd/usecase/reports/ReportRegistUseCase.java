package jp.co.itechh.quad.ddd.usecase.reports;

import jp.co.itechh.quad.core.constant.type.HTypeOrderSalesStatus;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.ITransactionAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.TransactionForRevision;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IMulpayBillAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IPaymentMethodAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.MulpayBill;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ICouponAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.product.adapter.ICategoryAdapter;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductCategory;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDisplays;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.IMailMagazineAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.MailMagazineSubscriptionFlag;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.IReportQuery;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsProduct;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsProductItem;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 集計用販売データ登録用 ユースケース
 */
@Service
public class ReportRegistUseCase {

    /**
     * 受注アダプター
     */
    private final IOrderReceivedAdapter orderReceivedAdapter;

    /**
     * 取引エアダプター
     */
    private final ITransactionAdapter transactionAdapter;

    /**
     * メルマガアダプター
     */
    private final IMailMagazineAdapter mailMagazineAdapter;

    /**
     * マルチペイメントアダプター
     */
    private final IMulpayBillAdapter mulpayBillAdapter;

    /**
     * カテゴリ アダプタ
     */
    private final ICategoryAdapter categoryAdapter;

    /**
     * 顧客アダプター
     */
    private final ICustomerAdapter customerAdapter;

    /**
     * 配送伝票アダプター
     */
    private final IShippingAdapter shippingAdapter;

    /**
     * 住所録アダプター
     */
    private final IAddressBookAdapter addressBookAdapter;

    /**
     * 請求アダプター
     */
    private final IBillingAdapter billingAdapter;

    /**
     * 注文アダプター
     */
    private final IOrderSlipAdapter orderSlipAdapter;

    /**
     * 商品アダプター
     */
    private final IProductAdapter productAdapter;

    /**
     * 販売企画アダプター
     */
    private final ISalesAdapter salesAdapter;

    /**
     * 販売企画アダプター
     */
    private final ICouponAdapter couponAdapter;

    /**
     * 決済方法アダプター
     */
    private final IPaymentMethodAdapter paymentMethodAdapter;

    /**
     * 注文登録用Helperクラス
     */
    private final ReportRegistUseCaseHelper reportRegistUseCaseHelper;

    /**
     * バッチ管理クエリ―実装クラス
     */
    private final IReportQuery reportQuery;

    /**
     * コンストラクタ
     *
     * @param orderReceivedAdapter      受注アダプター
     * @param transactionAdapter        取引エアダプター
     * @param mailMagazineAdapter       メルマガアダプター
     * @param mulpayBillAdapter         マルチペイメントアダプター
     * @param categoryAdapter           カテゴリーアダプター
     * @param customerAdapter           顧客アダプター
     * @param shippingAdapter           配送伝票アダプター
     * @param addressBookAdapter        住所録アダプター
     * @param billingAdapter            請求アダプター
     * @param orderSlipAdapter          注文アダプター
     * @param productAdapter            商品アダプター
     * @param salesAdapter              販売企画アダプター
     * @param couponAdapter             販売企画アダプター
     * @param paymentMethodAdapter      決済方法アダプター
     * @param reportRegistUseCaseHelper 注文登録用Helperクラス
     * @param reportQuery               バッチ管理クエリ―実装クラス
     */
    @Autowired
    public ReportRegistUseCase(IOrderReceivedAdapter orderReceivedAdapter,
                               ITransactionAdapter transactionAdapter,
                               IMailMagazineAdapter mailMagazineAdapter,
                               IMulpayBillAdapter mulpayBillAdapter,
                               ICategoryAdapter categoryAdapter,
                               ICustomerAdapter customerAdapter,
                               IShippingAdapter shippingAdapter,
                               IAddressBookAdapter addressBookAdapter,
                               IBillingAdapter billingAdapter,
                               IOrderSlipAdapter orderSlipAdapter,
                               IProductAdapter productAdapter,
                               ISalesAdapter salesAdapter,
                               ICouponAdapter couponAdapter,
                               IPaymentMethodAdapter paymentMethodAdapter,
                               ReportRegistUseCaseHelper reportRegistUseCaseHelper,
                               IReportQuery reportQuery) {
        this.orderReceivedAdapter = orderReceivedAdapter;
        this.transactionAdapter = transactionAdapter;
        this.mailMagazineAdapter = mailMagazineAdapter;
        this.mulpayBillAdapter = mulpayBillAdapter;
        this.categoryAdapter = categoryAdapter;
        this.customerAdapter = customerAdapter;
        this.shippingAdapter = shippingAdapter;
        this.addressBookAdapter = addressBookAdapter;
        this.billingAdapter = billingAdapter;
        this.orderSlipAdapter = orderSlipAdapter;
        this.productAdapter = productAdapter;
        this.salesAdapter = salesAdapter;
        this.couponAdapter = couponAdapter;
        this.paymentMethodAdapter = paymentMethodAdapter;
        this.reportRegistUseCaseHelper = reportRegistUseCaseHelper;
        this.reportQuery = reportQuery;
    }

    /**
     * 集計販売データ登録用
     *
     * @param transactionRevisionId
     * @param transactionBeforeRevisionId
     */
    public void registData(String transactionRevisionId, String transactionBeforeRevisionId) {

        //(1) 受注を取得する
        OrderReceived orderReceived = orderReceivedAdapter.getByTransactionId(transactionRevisionId);

        //(2) 改訂後で取引を取得
        TransactionForRevision transactionForRevision =
                        transactionAdapter.getTransactionForRevision(transactionRevisionId);

        // (3) 顧客の受注件数を取得する
        OrderReceivedCount orderReceivedCount =
                        orderReceivedAdapter.getOrderReceivedCount(orderReceived.getCustomerId().toString());

        //(4) 会員マスタを取得
        Customer customer = customerAdapter.getCustomer(orderReceived.getCustomerId());
        AddressBook customerAddressBook = addressBookAdapter.getAddressBook(customer.getCustomerInfoAddressId());

        // (5) メルマガ会員マスタの「メールマガジン購読フラグ」を取得
        MailMagazineSubscriptionFlag mailMagazineSubscriptionFlag =
                        mailMagazineAdapter.getMailMagazineSubscriptionFlag(orderReceived.getCustomerId(),
                                                                            customer.getCustomerMail()
                                                                           );

        // (6) 配送伝票取得
        ShippingSlip shippingSlip = shippingAdapter.getShippingSlip(orderReceived.getLatestTransactionId());

        //(7) 取引にひもづく請求伝票を取得する
        BillingSlip billingSlip = billingAdapter.getBillingSlip(orderReceived.getLatestTransactionId());

        // 配送先住所を取得する
        AddressBook orderAddressBook = addressBookAdapter.getAddressBook(shippingSlip.getShippingAddressId());

        AddressBook billingAddressBook;

        if (StringUtils.isNotEmpty(billingSlip.getBillingAddressId()) && !billingSlip.getBillingAddressId()
                                                                                     .equalsIgnoreCase(
                                                                                                     shippingSlip.getShippingAddressId())) {
            // 住所を取得する
            billingAddressBook = addressBookAdapter.getAddressBook(billingSlip.getBillingAddressId());
        } else {
            billingAddressBook = orderAddressBook;
        }

        //(8) マルペイ請求テーブルを取得
        MulpayBill mulpayBill = mulpayBillAdapter.getByOrderCode(orderReceived.getOrderCode());

        // (9) 取引にひもづく販売伝票を取得する
        SalesSlip salesSlip = salesAdapter.getSalesSlip(orderReceived.getLatestTransactionId());

        Coupon coupon = new Coupon();
        // 取引にひもづく販売伝票を取得する
        if (salesSlip.getCouponSeq() != null && salesSlip.getCouponVersionNo() != null) {
            coupon = couponAdapter.getByCouponVersionNo(salesSlip.getCouponSeq(), salesSlip.getCouponVersionNo());
        }

        // (10) 取引にひもづく注文票を取得する
        OrderSlip orderSlip = orderSlipAdapter.getOrderSlipByTransactionId(orderReceived.getLatestTransactionId());

        Set<Integer> itemIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(orderSlip.getItemList())) {
            List<OrderSlipItem> itemList = orderSlip.getItemList();
            for (OrderSlipItem item : itemList) {
                if (item.getItemId() != null) {
                    itemIds.add(Integer.parseInt(item.getItemId()));
                }
            }
        }

        // (11) 商品マスタを取得
        List<ProductDetails> productDetailsList = productAdapter.getProductDetails(new ArrayList<>(itemIds));

        // リンクされたカテゴリを取得する
        List<ProductDisplays> productDisplaysList = new ArrayList<>();
        for (ProductDetails productDetails : productDetailsList) {
            productDisplaysList.add(productAdapter.getProductDisplay(productDetails.getGoodsGroupCode()));
        }
        // 全カテゴリ一覧
        List<ProductCategory> allCategoryList = categoryAdapter.getCategoryList();

        // 受注商品リスト取得
        List<ReportsProduct> orderProductList =
                        reportRegistUseCaseHelper.getOrderProductList(orderSlip, productDetailsList, salesSlip,
                                                                      productDisplaysList, allCategoryList
                                                                     );

        //(12) 改訂前取引IDが存在する場合は下記の伝票情報とマスタ情報を取得する
        SalesSlip salesSlipBeforeRevision = null;
        ReportsQueryModel previousReportsQueryModel = null;
        Boolean isShippedRecently = false;

        if (StringUtils.isNotEmpty(transactionBeforeRevisionId)) {
            salesSlipBeforeRevision = salesAdapter.getSalesSlip(transactionBeforeRevisionId);

            OrderSlip orderSlipBeforeRevision =
                            orderSlipAdapter.getOrderSlipByTransactionId(transactionBeforeRevisionId);

            OrderReceived orderReceivedBeforeRevision =
                            orderReceivedAdapter.getByTransactionId(transactionBeforeRevisionId);

            previousReportsQueryModel = reportQuery.getByBeforeTransactionId(transactionBeforeRevisionId);

            // 注文が最近出荷されたとき
            isShippedRecently = orderReceived.getShippedFlag() && !orderReceivedBeforeRevision.getShippedFlag();

            // 商品販売一覧に変更がない場合
            Boolean isProductListNotChanged =
                            org.apache.commons.collections.CollectionUtils.isEqualCollection(orderSlip.getItemList(),
                                                                                             orderSlipBeforeRevision.getItemList()
                                                                                            )
                            && orderSlip.getTotalItemCount() == orderSlipBeforeRevision.getTotalItemCount() && (
                                            salesSlip.getBillingAmount() != null && salesSlip.getBillingAmount()
                                                                                             .equals(salesSlipBeforeRevision.getBillingAmount()))
                            && !isShippedRecently;

            if (!isProductListNotChanged) {

                Set<Integer> itemIdsPreRevisionsSet = new HashSet<>();
                if (!CollectionUtils.isEmpty(orderSlipBeforeRevision.getItemList())) {
                    List<OrderSlipItem> itemList = orderSlipBeforeRevision.getItemList();
                    for (OrderSlipItem item : itemList) {
                        if (item.getItemId() != null) {
                            itemIdsPreRevisionsSet.add(Integer.parseInt(item.getItemId()));
                        }
                    }
                }

                List<ProductDetails> productDetailsBeforeRevisionList =
                                productAdapter.getProductDetails(new ArrayList<>(itemIdsPreRevisionsSet));

                // 注文商品一覧変更時の設定データ
                List<ReportsProduct> reportsProductListForRevision =
                                reportRegistUseCaseHelper.executeForRevisionOrder(previousReportsQueryModel,
                                                                                  orderProductList,
                                                                                  orderSlipBeforeRevision,
                                                                                  salesSlipBeforeRevision,
                                                                                  productDetailsBeforeRevisionList,
                                                                                  orderReceived, isShippedRecently
                                                                                 );

                orderProductList.clear();
                orderProductList.addAll(reportsProductListForRevision);
            } else {
                // 次回の取得のために以前のデータを保存する
                ReportsQueryModel finalPreviousReportsQueryModel = previousReportsQueryModel;

                orderProductList.forEach(item -> {
                    ReportsProductItem beforeReportsProductItem = finalPreviousReportsQueryModel.getOrderItemList()
                                                                                                .stream()
                                                                                                .filter(preItem -> StringUtils.isNotEmpty(
                                                                                                                preItem.getGoodsGroupCode())
                                                                                                                   && preItem.getGoodsGroupCode()
                                                                                                                             .equals(item.getGoodsGroupCode()))
                                                                                                .findFirst()
                                                                                                .orElse(null);

                    item.setIconId(beforeReportsProductItem.getIconId());
                    item.setIconName(beforeReportsProductItem.getIconName());
                    item.setCategoryId(beforeReportsProductItem.getCategoryId());
                    item.setCategoryName(beforeReportsProductItem.getCategoryName());
                    item.setGoodsTag(beforeReportsProductItem.getGoodsTag());
                    item.setSalesCount(0);
                    item.setCancelCount(0);
                });
            }
        }

        ReportsQueryModel reportsQueryModel = new ReportsQueryModel();
        reportsQueryModel.setTransactionId(transactionRevisionId);

        reportRegistUseCaseHelper.settingCommonInfoQueryModel(reportsQueryModel, orderReceived,
                                                              transactionBeforeRevisionId, transactionForRevision,
                                                              orderSlip, isShippedRecently, shippingSlip
                                                             );

        reportRegistUseCaseHelper.settingReportsQueryModelByTransaction(reportsQueryModel, transactionForRevision);

        reportRegistUseCaseHelper.settingCustomerInfoReportsQueryModel(reportsQueryModel, previousReportsQueryModel,
                                                                       customer, customerAddressBook,
                                                                       orderReceivedCount, mailMagazineSubscriptionFlag
                                                                      );

        reportRegistUseCaseHelper.settingShippingReportsQueryModel(reportsQueryModel, shippingSlip, orderAddressBook);

        reportRegistUseCaseHelper.settingBillingReportsQueryModel(reportsQueryModel, billingSlip, billingAddressBook);

        reportRegistUseCaseHelper.settingPaymentReportsQueryModel(reportsQueryModel, billingSlip, mulpayBill);

        reportRegistUseCaseHelper.settingCouponReportsQueryModel(reportsQueryModel, coupon, salesSlip);

        reportRegistUseCaseHelper.settingPriceReportsQueryModel(
                        reportsQueryModel, salesSlip, salesSlipBeforeRevision, isShippedRecently);

        reportRegistUseCaseHelper.settingProductReportsItemsQueryModel(reportsQueryModel, orderProductList);

        reportQuery.regist(reportsQueryModel);

        if (orderReceived.getShippedFlag() && !isShippedRecently) {

            // レコード ID をリセット
            reportsQueryModel.set_id(null);
            //受注反受注映
            reportsQueryModel.setOrderStatus(HTypeOrderSalesStatus.ORDER.getValue());
            // 別のレコードを挿入する
            reportQuery.regist(reportsQueryModel);
        }
    }

}