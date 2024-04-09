package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.customize.adapter.IExaminationAdapter;
import jp.co.itechh.quad.ddd.domain.customize.adapter.model.ExamKit;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingMethodAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingMethod;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IPaymentMethodAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentMethod;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ICouponAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import jp.co.itechh.quad.ddd.infrastructure.ordersearch.query.OrderRegistUpdateQueryImpl;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderProduct;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 注文登録更新用 ユースケース
 */
@Service
public class OrderRegistUpdateUseCase {

    /**
     * 受注アダプター
     */
    private final IOrderReceivedAdapter orderReceivedAdapter;

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
     * 配送伝票アダプター
     */
    private final IShippingMethodAdapter shippingMethodAdapter;

    /**
     * 検査アダプター
     */
    private final IExaminationAdapter examinationAdapter;

    /**
     * 注文登録更新用Helperクラス
     */
    private final OrderRegistUpdateUsecaseHelper orderRegistUpdateUsecaseHelper;

    /**
     * バッチ管理クエリ―実装クラス
     */
    private final OrderRegistUpdateQueryImpl orderRegistUpdateQuery;

    public static final Map<String, String> PAYMENT_STATUS_DATA_MAP;

    static {
        PAYMENT_STATUS_DATA_MAP = new HashMap<>();
        PAYMENT_STATUS_DATA_MAP.put("NOTHING_MONEY", "1");
        PAYMENT_STATUS_DATA_MAP.put("JUST_MONEY", "2");
        PAYMENT_STATUS_DATA_MAP.put("INSUFFICIENT_MONEY", "3");
        PAYMENT_STATUS_DATA_MAP.put("OVER_MONEY", "4");
    }

    /**
     * コンストラクタ
     *
     * @param orderReceivedAdapter           受注アダプター
     * @param customerAdapter                顧客アダプター
     * @param shippingAdapter                配送伝票アダプター
     * @param addressBookAdapter             住所録アダプター
     * @param billingAdapter                 請求アダプター
     * @param orderSlipAdapter               注文アダプター
     * @param productAdapter                 商品アダプター
     * @param salesAdapter                   販売企画アダプター
     * @param couponAdapter                  販売企画アダプター
     * @param paymentMethodAdapter           決済方法アダプター
     * @param shippingMethodAdapter          配送伝票アダプター
     * @param orderRegistUpdateUsecaseHelper 注文登録更新用Helperクラス
     * @param orderRegistUpdateQuery         バッチ管理クエリ―実装クラス
     */
    @Autowired
    public OrderRegistUpdateUseCase(IOrderReceivedAdapter orderReceivedAdapter,
                                    ICustomerAdapter customerAdapter,
                                    IShippingAdapter shippingAdapter,
                                    IAddressBookAdapter addressBookAdapter,
                                    IBillingAdapter billingAdapter,
                                    IOrderSlipAdapter orderSlipAdapter,
                                    IProductAdapter productAdapter,
                                    ISalesAdapter salesAdapter,
                                    ICouponAdapter couponAdapter,
                                    IPaymentMethodAdapter paymentMethodAdapter,
                                    IShippingMethodAdapter shippingMethodAdapter,
                                    IExaminationAdapter examinationAdapter,
                                    OrderRegistUpdateUsecaseHelper orderRegistUpdateUsecaseHelper,
                                    OrderRegistUpdateQueryImpl orderRegistUpdateQuery) {
        this.orderReceivedAdapter = orderReceivedAdapter;
        this.customerAdapter = customerAdapter;
        this.shippingAdapter = shippingAdapter;
        this.addressBookAdapter = addressBookAdapter;
        this.billingAdapter = billingAdapter;
        this.orderSlipAdapter = orderSlipAdapter;
        this.productAdapter = productAdapter;
        this.salesAdapter = salesAdapter;
        this.couponAdapter = couponAdapter;
        this.paymentMethodAdapter = paymentMethodAdapter;
        this.shippingMethodAdapter = shippingMethodAdapter;
        this.examinationAdapter = examinationAdapter;
        this.orderRegistUpdateUsecaseHelper = orderRegistUpdateUsecaseHelper;
        this.orderRegistUpdateQuery = orderRegistUpdateQuery;
    }

    /**
     * 注文登録更新用
     *
     * @param orderReceivedId 受注ID
     * @return 受注検索クエリーモデル
     */
    public void orderRegistUpdate(String orderReceivedId) {

        // 受注を取得する
        OrderReceived orderReceived = orderReceivedAdapter.getByOrderReceivedId(orderReceivedId);

        // 顧客の受注件数を取得する
        OrderReceivedCount orderReceivedCount =
                        orderReceivedAdapter.getOrderReceivedCount(orderReceived.getCustomerId().toString());

        // bottom-up ユーザー情報取得
        Customer customer = customerAdapter.getCustomer(orderReceived.getCustomerId());

        // 配送伝票取得
        ShippingSlip shippingSlip = shippingAdapter.getShippingSlip(orderReceived.getLatestTransactionId());

        // 配送方法取得
        ShippingMethod shippingMethod = shippingMethodAdapter.getShippingMethod(shippingSlip.getShippingMethodId());

        // 取引にひもづく請求伝票を取得する
        BillingSlip billingSlip = billingAdapter.getBillingSlip(orderReceived.getLatestTransactionId());

        // bottom-up【決済方法管理】決済方法を取得する
        PaymentMethod paymentMethod = paymentMethodAdapter.getPaymentMethod(billingSlip.getPaymentMethodId());

        // 住所を取得する
        AddressBook orderAddressBook = addressBookAdapter.getAddressBook(billingSlip.getBillingAddressId());

        AddressBook receiverAddressBook;

        if (StringUtils.isNotEmpty(billingSlip.getBillingAddressId()) && !billingSlip.getBillingAddressId()
                                                                                     .equalsIgnoreCase(
                                                                                                     shippingSlip.getShippingAddressId())) {
            // 住所を取得する
            receiverAddressBook = addressBookAdapter.getAddressBook(shippingSlip.getShippingAddressId());
        } else {
            receiverAddressBook = orderAddressBook;
        }

        // 取引にひもづく注文票を取得する
        OrderSlip orderSlip = orderSlipAdapter.getOrderSlipByTransactionId(orderReceived.getLatestTransactionId());

        LinkedHashSet<Integer> itemIdsHashSet = new LinkedHashSet<>();
        if (!ObjectUtils.isEmpty(orderSlip) && !CollectionUtils.isEmpty(orderSlip.getItemList())) {
            List<OrderSlipItem> itemList = orderSlip.getItemList();
            for (OrderSlipItem item : itemList) {
                if (StringUtils.isNotBlank(item.getItemId())) {
                    itemIdsHashSet.add(Integer.parseInt(item.getItemId()));
                }
            }
        }

        // bottom-up【商品】商品情報取得
        List<Integer> itemIds = new ArrayList<>(itemIdsHashSet);
        List<ProductDetails> productDetailsList = productAdapter.getProductDetails(itemIds);
        productDetailsList.sort(
                        Comparator.comparingInt(productDetails -> itemIds.indexOf(productDetails.getGoodsGroupSeq())));

        // 取引にひもづく販売伝票を取得する
        SalesSlip salesSlip = salesAdapter.getSalesSlip(orderReceived.getLatestTransactionId());

        Coupon coupon = new Coupon();
        // 取引にひもづく販売伝票を取得する
        if (salesSlip.getCouponSeq() != null && salesSlip.getCouponVersionNo() != null) {
            coupon = couponAdapter.getByCouponVersionNo(salesSlip.getCouponSeq(), salesSlip.getCouponVersionNo());
        }

        // 注文商品IDリストにひもづく検査キット情報の取得
        List<String> orderItemIdList = orderSlip.getItemList()
                                                .stream()
                                                .map(OrderSlipItem::getOrderItemId)
                                                .collect(Collectors.toList());
        List<ExamKit> examKitList = examinationAdapter.getExamKitList(orderItemIdList);

        List<OrderProduct> orderProductList =
                        orderRegistUpdateUsecaseHelper.getOrderProductList(orderSlip, productDetailsList, salesSlip,
                                                                           examKitList
                                                                          );

        OrderSearchQueryModel orderSearchQueryModel = new OrderSearchQueryModel();

        orderSearchQueryModel.setOrderProductList(orderProductList);

        orderSearchQueryModel.setGoodsPriceTotal(salesSlip.getGoodsPriceTotal());

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByOrderReceived(
                        orderSearchQueryModel, orderReceived);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByShippingSlip(orderSearchQueryModel, shippingSlip);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelBySalesSlip(orderSearchQueryModel, salesSlip);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByCoupon(orderSearchQueryModel, coupon);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByOrderAddressBook(
                        orderSearchQueryModel, orderAddressBook);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByReceiverAddressBook(
                        orderSearchQueryModel, receiverAddressBook);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByCustomer(orderSearchQueryModel, customer);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByPaymentMethod(
                        orderSearchQueryModel, paymentMethod);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByBillingSlip(orderSearchQueryModel, billingSlip);

        orderRegistUpdateUsecaseHelper.settingOrderSearchQueryModelByNecessaryConditions(
                        orderReceived, orderReceivedCount, paymentMethod, orderSearchQueryModel);

        orderRegistUpdateUsecaseHelper.searchNameEmUc(orderSearchQueryModel);

        orderRegistUpdateUsecaseHelper.searchTelEn(orderSearchQueryModel);

        orderRegistUpdateUsecaseHelper.youPackJudgement(orderSearchQueryModel);

        orderSearchQueryModel.setShippingMethodId(shippingMethod.getShippingMethodId());

        orderRegistUpdateQuery.registUpdate(orderSearchQueryModel);

    }

}