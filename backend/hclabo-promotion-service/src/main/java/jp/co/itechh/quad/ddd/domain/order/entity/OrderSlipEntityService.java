/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.entity;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.stock.adapter.IStockAdapter;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注文票ドメインサービス
 */
@Service
public class OrderSlipEntityService {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 在庫アダプター */
    private final IStockAdapter stockAdapter;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 会員業務ユーティリティクラス */
    private final MemberInfoUtility memberInfoUtility;

    /** 妥当性チェック用メッセージコードマップ（カート⇒注文） */
    private static Map<String, String> MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER = new HashMap<>();

    /** コンストラクタ */
    @Autowired
    public OrderSlipEntityService(IOrderSlipRepository orderSlipRepository,
                                  IProductAdapter productAdapter,
                                  IStockAdapter stockAdapter,
                                  ConversionUtility conversionUtility,
                                  MemberInfoUtility memberInfoUtility) {
        this.orderSlipRepository = orderSlipRepository;
        this.productAdapter = productAdapter;
        this.stockAdapter = stockAdapter;
        this.conversionUtility = conversionUtility;
        this.memberInfoUtility = memberInfoUtility;
    }

    /**
     * 注文商品統合
     *
     * @param orderSlipEntityFrom 統合元の注文票
     * @param orderSlipEntityTo   統合先の注文票
     */
    public void mergeOrderItem(OrderSlipEntity orderSlipEntityFrom, OrderSlipEntity orderSlipEntityTo) {

        // チェック
        AssertChecker.assertNotNull("orderSlipEntityFrom is null", orderSlipEntityFrom);
        AssertChecker.assertNotNull("orderSlipEntityTO is null", orderSlipEntityTo);
        // 統合元の注文票と統合先の注文票がどちらも下書き状態でない場合はエラー
        if (orderSlipEntityFrom.getOrderStatus() != OrderStatus.DRAFT
            && orderSlipEntityTo.getOrderStatus() != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }
        //  統合元の注文票に注文商品が設定されている場合
        if (!CollectionUtils.isEmpty(orderSlipEntityFrom.getOrderItemList())) {
            // 統合先の注文票に統合前の注文票の注文商品を統合する
            orderSlipEntityTo.mergeOrderItem(orderSlipEntityFrom.getOrderItemList());
        }

        // 統合前の注文票と注文商品を削除する
        this.orderSlipRepository.deleteDraftOrderSlipByCustomerId(orderSlipEntityFrom.getCustomerId());

    }

    /**
     * 注文商品チェック
     *
     * @param orderItemList  統合元の注文商品リスト
     * @param customerBirthday 顧客生年月日
     */
    public void checkOrderItem(List<OrderItem> orderItemList, Date customerBirthday) {
        // 注文商品が０件であれば、チェック未実施で処理終了（正常終了）
        if (CollectionUtils.isEmpty(orderItemList)) {
            return;
        }

        // 日付関連Helperより現在日時を取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> goodsDtoList = getGoodsDetailsDtoList(orderItemList);
        // 物流サービスから在庫詳細リストを取得
        List<StockDto> stockDtoList = getStockDtoList(orderItemList);

        // 同一注文内総商品点数
        int totalOrderCount = 0;

        // 注文商品の妥当性チェック開始
        ApplicationException applicationException = new ApplicationException();
        for (OrderItem orderItem : orderItemList) {
            // 各種Dto取得
            GoodsDetailsDto goodsDto = getGoodsDetailsDtoByItemId(goodsDtoList, orderItem.getItemId());
            StockDto stockDto = getStockDtoByItemId(stockDtoList, orderItem.getItemId());

            // ==============================
            // 商品ごとのチェック-START >>>
            // ==============================
            List<String> errorList = new ArrayList<>();

            // 注文商品の公開状態チェック
            checkOpen(goodsDto.getGoodsOpenStatusPC(), goodsDto.getOpenStartTimePC(), goodsDto.getOpenEndTimePC(),
                      currentTime, errorList
                     );

            // 注文商品の販売状態チェック
            checkSale(goodsDto.getSaleStatusPC(), goodsDto.getSaleStartTimePC(), goodsDto.getSaleEndTimePC(),
                      currentTime, errorList
                     );

            // 注文商品の注文上限数チェック
            checkPurchasedMax(goodsDto.getPurchasedMax(), orderItem.getOrderCount().getValue(), errorList);

            // 注文商品の販売可能在庫数チェック
            checkSalesPossibleStock(goodsDto.getStockManagementFlag(), stockDto.getSalesPossibleStock(),
                                    orderItem.getOrderCount().getValue(), errorList
                                   );

            // 個別配送品の同梱チェック
            checkIndividualDelivery(goodsDto.getIndividualDeliveryType(), orderItemList.size(), errorList);

            // 酒類商品販売可能チェック
            checkAlcohol(customerBirthday, goodsDto.getAlcoholFlag(), errorList);
            // ==============================
            // 商品ごとのチェック-END <<<
            // ==============================

            // 該当商品IDをキーにメッセージリストをアプリケーション例外に登録
            if (!CollectionUtils.isEmpty(errorList)) {
                for (String error : errorList) {
                    applicationException.addMessage(orderItem.getItemId(), error);
                }
            }

            totalOrderCount += orderItem.getOrderCount().getValue();
        }

        // 注文商品数量件数チェック
        checkMaxGoodsCount(orderItemList.size(), applicationException);

        // 同一注文内総商品点数チェック
        checkMaxTotalOrderCount(totalOrderCount, applicationException);

        if (applicationException.hasMessage()) {
            throw applicationException;
        }
    }

    /**
     * 注文商品の公開状態チェック<br/>
     *
     * @param openStatus    商品公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime   公開終了日時
     * @param currentTime   現在日付
     * @param errorList     エラーリスト
     */
    private void checkOpen(HTypeOpenDeleteStatus openStatus,
                           Timestamp openStartTime,
                           Timestamp openEndTime,
                           Timestamp currentTime,
                           List<String> errorList) {

        // 公開状態がOPEN以外の場合はエラー
        if (openStatus != HTypeOpenDeleteStatus.OPEN) {
            errorList.add("LCC000602W");
        } else {
            // 公開中のため日時チェック
            // 日時が設定されている場合、商品情報の公開開始日時が現在日より、前の場合はエラー
            if (openStartTime != null && currentTime.before(openStartTime)) {
                errorList.add("LCC000604W");
            }
            // 日時が設定されている場合、商品情報の公開終了日時が現在日より、後の場合はエラー
            if (openEndTime != null && currentTime.after(openEndTime)) {
                errorList.add("LCC000605W");
            }
        }
    }

    static {
        // メッセージコードマップ（カート用：注文フロー用）
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000602W", "PROMOTION-ENOS0001-E");
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000604W", "PROMOTION-ENSS0001-E");
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000605W", "PROMOTION-ENES0001-E");
    }

    /**
     * 注文商品の販売状態チェック<br/>
     *
     * @param saleStatus    商品販売状態
     * @param saleStartTime 販売開始日時
     * @param saleEndTime   販売終了日時
     * @param currentTime   現在日付
     * @param errorList     エラーリスト
     */
    private void checkSale(HTypeGoodsSaleStatus saleStatus,
                           Timestamp saleStartTime,
                           Timestamp saleEndTime,
                           Timestamp currentTime,
                           List<String> errorList) {

        // 販売状態がOPEN以外の場合はエラー
        if (saleStatus != HTypeGoodsSaleStatus.SALE) {
            errorList.add("LCC000606W");
        } else {
            // 販売中のため日時チェック
            // 日時が設定されている場合、商品情報の販売開始日時が現在日より、前の場合はエラー
            if (saleStartTime != null && currentTime.before(saleStartTime)) {
                errorList.add("LCC000608W");
            }
            // 日時が設定されている場合、商品情報の販売終了日時が現在日より、後の場合はエラー
            if (saleEndTime != null && currentTime.after(saleEndTime)) {
                errorList.add("LCC000609W");
            }
        }
    }

    static {
        // メッセージコードマップ（カート用：注文フロー用）
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000606W", "PROMOTION-ESST0001-E");
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000608W", "PROMOTION-ESST0002-E");
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000609W", "PROMOTION-ESET0001-E");
    }

    /**
     * 注文商品数の注文上限数チェック<br/>
     *
     * @param purchasedMax   注文上限数
     * @param orderItemCount 注文商品数
     * @param errorList      エラーリスト
     */
    private void checkPurchasedMax(BigDecimal purchasedMax, int orderItemCount, List<String> errorList) {

        // 注文上限数が０(無制限)ではなく、注文商品数が注文上限数より多い場合はエラー
        if (purchasedMax != null && purchasedMax.compareTo(BigDecimal.ZERO) != 0
            && orderItemCount > purchasedMax.intValue()) {
            errorList.add("LCC000612W");
        }
    }

    static {
        // メッセージコードマップ（カート用：注文フロー用）
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000612W", "PROMOTION-ENPM0001-E");
    }

    /**
     * 個別配送品の同梱チェック
     *
     * @param type              個別配送種別
     * @param orderItemListSize 注文商品数
     * @param errorList         エラーリスト
     */
    private void checkIndividualDelivery(HTypeIndividualDeliveryType type,
                                         int orderItemListSize,
                                         List<String> errorList) {
        // 対象商品が個別配送品であり、注文商品数が1つより多い場合はエラー
        if (type == HTypeIndividualDeliveryType.ON && orderItemListSize > 1) {
            errorList.add("LCC000617W");
        }
    }

    static {
        // メッセージコードマップ（カート用：注文フロー用）
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000617W", "PROMOTION-EIND0001-E");
    }

    /**
     * 注文商品数の販売可能在庫数チェック<br/>
     *
     * @param flag               在庫管理フラグ
     * @param salesPossibleStock 販売可能在庫数
     * @param orderItemCount     注文商品数
     * @param errorList          エラーリスト
     */
    private void checkSalesPossibleStock(HTypeStockManagementFlag flag,
                                         BigDecimal salesPossibleStock,
                                         int orderItemCount,
                                         List<String> errorList) {
        // 在庫管理OFFの場合チェック不要
        if (HTypeStockManagementFlag.OFF.equals(flag)) {
            return;
        }
        // 販売可能在庫数が無い（０以下）場合はエラー
        if (0 >= salesPossibleStock.intValue()) {
            errorList.add("LCC000610W");

            // 注文商品数が販売可能在庫数より多い場合はエラー
        } else if (orderItemCount > salesPossibleStock.intValue()) {
            errorList.add("LCC000611W");
        }
    }

    static {
        // メッセージコードマップ（カート用：注文フロー用）
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000610W", "PROMOTION-ESPS0001-E");
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("LCC000611W", "PROMOTION-ESPS0002-E");
    }

    /**
     * 会員情報と商品情報をもとに、注文商品をチェック
     *
     * @param customerBirthday 顧客生年月日
     * @param alcoholFlag      酒類商品フラグ
     * @param errorList        エラーリスト
     */
    private void checkAlcohol(Date customerBirthday, HTypeAlcoholFlag alcoholFlag, List<String> errorList) {
        // 顧客生年月日が存在しない場合はチェックSKIP
        // ※ゲストユーザーの考慮（ゲストユーザーは生年月日不明のため）
        if (customerBirthday == null) {
            return;
        }
        // 酒類年齢制限チェック
        // 対象注文商品がアルコール商品かつ、未成年の場合はエラー
        if (alcoholFlag == HTypeAlcoholFlag.ALCOHOL && !this.memberInfoUtility.isAdult(
                        this.conversionUtility.toTimestamp(customerBirthday))) {
            errorList.add("PKG-4113-004-L-W");
        }
    }

    static {
        // メッセージコードマップ（カート用：注文フロー用）
        MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.put("PKG-4113-004-L-W", "PROMOTION-ENAC0002-E");
    }

    /**
     * その他注文商品をチェック<br/>
     *
     * @param orderItemListSize 注文商品数
     * @param applicationException アプリケーション例外
     */
    private void checkMaxGoodsCount(int orderItemListSize, ApplicationException applicationException) {

        int maxCartGoodsCount = PropertiesUtil.getSystemPropertiesValueToInt("max.cart.goods.count");
        // 注文商品件数の最大商品数超過チェック
        // 注文商品件数が最大商品数より多い場合はエラー
        if (orderItemListSize > maxCartGoodsCount) {
            applicationException.addMessage("LCC000715W", new String[] {String.valueOf(maxCartGoodsCount)});
        }
    }

    /**
     * 同一注文内総商品点数チェック<br/>
     *
     * @param totalOrderCount 注文商品の総注文数量
     * @param applicationException アプリケーション例外
     */
    private void checkMaxTotalOrderCount(int totalOrderCount, ApplicationException applicationException) {

        int maxTotalOrderCount = PropertiesUtil.getSystemPropertiesValueToInt("customize.max.order.goods.count");
        // 同一注文内の総商品点数超過チェック
        // 注文商品の総注文数量が同一注文内総商品点数より多い場合はエラー
        if (totalOrderCount > maxTotalOrderCount) {
            applicationException.addMessage("LCC000716W", new String[] {String.valueOf(maxTotalOrderCount)});
        }
    }

    /**
     * 注文商品チェック
     *
     * @param orderItemList  統合元の注文商品リスト
     * @param customerBirthday 顧客生年月日
     */
    public void checkOrderItemInTransaction(List<OrderItem> orderItemList, Date customerBirthday) {
        // 注文商品が０件であれば、チェック未実施で処理終了（正常終了）
        if (CollectionUtils.isEmpty(orderItemList)) {
            return;
        }
        // 日付関連Helperより現在日時を取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> goodsDtoList = getGoodsDetailsDtoList(orderItemList);
        // 物流サービスから在庫詳細リストを取得
        List<StockDto> stockDtoList = getStockDtoList(orderItemList);

        // 同一注文内総商品点数
        int totalOrderCount = 0;

        // 注文商品の妥当性チェック開始
        ApplicationException appException = new ApplicationException();
        for (OrderItem orderItem : orderItemList) {
            // 各種Dto取得
            GoodsDetailsDto goodsDto = getGoodsDetailsDtoByItemId(goodsDtoList, orderItem.getItemId());
            StockDto stockDto = getStockDtoByItemId(stockDtoList, orderItem.getItemId());

            // メッセージ用商品名＋規格情報を生成
            String itemNameInfo = orderItem.createItemNameInfo();

            // ==============================
            // 商品ごとのチェック-START >>>
            // ==============================
            // 注文商品の公開状態チェック
            checkOpen(goodsDto.getGoodsOpenStatusPC(), goodsDto.getOpenStartTimePC(), goodsDto.getOpenEndTimePC(),
                      currentTime, itemNameInfo, appException
                     );

            // 注文商品の販売状態チェック
            checkSale(goodsDto.getSaleStatusPC(), goodsDto.getSaleStartTimePC(), goodsDto.getSaleEndTimePC(),
                      currentTime, itemNameInfo, appException
                     );

            // 注文商品の注文上限数チェック
            checkPurchasedMax(goodsDto.getPurchasedMax(), orderItem.getOrderCount().getValue(), itemNameInfo,
                              appException
                             );

            // 個別配送品の同梱チェック
            checkIndividualDelivery(
                            goodsDto.getIndividualDeliveryType(), orderItemList.size(), itemNameInfo, appException);

            // 注文商品の販売可能在庫数チェック
            checkSalesPossibleStock(goodsDto.getStockManagementFlag(), stockDto.getSalesPossibleStock(),
                                    orderItem.getOrderCount().getValue(), itemNameInfo, appException
                                   );

            // 酒類商品販売可能チェック
            checkAlcohol(customerBirthday, goodsDto.getAlcoholFlag(), itemNameInfo, appException);
            // ==============================
            // 商品ごとのチェック-END <<<
            // ==============================

            totalOrderCount += orderItem.getOrderCount().getValue();
        }

        // 注文商品数量件数チェック
        checkMaxGoodsCount(orderItemList.size(), appException);

        // 注文商品数量件数チェック
        checkMaxTotalOrderCount(totalOrderCount, appException);

        if (appException.hasMessage()) {
            throw appException;
        }
    }

    /**
     * 注文商品の公開状態チェック<br/>
     *
     * @param openStatus    商品公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime   公開終了日時
     * @param currentTime   現在日付
     * @param itemNameInfo   商品名称情報
     * @param appException   アプリケーション例外
     */
    private void checkOpen(HTypeOpenDeleteStatus openStatus,
                           Timestamp openStartTime,
                           Timestamp openEndTime,
                           Timestamp currentTime,
                           String itemNameInfo,
                           ApplicationException appException) {
        // カート時と同じチェックを行い、エラーがあればエラー情報を調整する
        List<String> errorList = new ArrayList<>();
        this.checkOpen(openStatus, openStartTime, openEndTime, currentTime, errorList);
        exchangeErrorInfo(errorList, appException, new String[] {itemNameInfo});
    }

    /**
     * 注文商品の販売状態チェック<br/>
     *
     * @param saleStatus    商品販売状態
     * @param saleStartTime 販売開始日時
     * @param saleEndTime   販売終了日時
     * @param currentTime   現在日付
     * @param itemNameInfo   商品名称情報
     * @param appException   アプリケーション例外
     */
    private void checkSale(HTypeGoodsSaleStatus saleStatus,
                           Timestamp saleStartTime,
                           Timestamp saleEndTime,
                           Timestamp currentTime,
                           String itemNameInfo,
                           ApplicationException appException) {
        // カート時と同じチェックを行い、エラーがあればエラー情報を調整する
        List<String> errorList = new ArrayList<>();
        this.checkSale(saleStatus, saleStartTime, saleEndTime, currentTime, errorList);
        exchangeErrorInfo(errorList, appException, new String[] {itemNameInfo});
    }

    /**
     * 注文商品数の注文上限数チェック<br/>
     *
     * @param purchasedMax   注文上限数
     * @param orderItemCount 注文商品数（配送商品数）
     * @param itemNameInfo   商品名称情報
     * @param appException   アプリケーション例外
     */
    private void checkPurchasedMax(BigDecimal purchasedMax,
                                   int orderItemCount,
                                   String itemNameInfo,
                                   ApplicationException appException) {
        // カート時と同じチェックを行い、エラーがあればエラー情報を調整する
        List<String> errorList = new ArrayList<>();
        this.checkPurchasedMax(purchasedMax, orderItemCount, errorList);
        exchangeErrorInfo(errorList, appException, new String[] {itemNameInfo});
    }

    /**
     * 個別配送品の同梱チェック
     *
     * @param individualDeliveryType 個別配送種別
     * @param orderItemListSize 注文商品数（配送商品数）
     * @param itemNameInfo   商品名称情報
     * @param appException   アプリケーション例外
     */
    private void checkIndividualDelivery(HTypeIndividualDeliveryType individualDeliveryType,
                                         int orderItemListSize,
                                         String itemNameInfo,
                                         ApplicationException appException) {
        // カート時と同じチェックを行い、エラーがあればエラー情報を調整する
        List<String> errorList = new ArrayList<>();
        this.checkIndividualDelivery(individualDeliveryType, orderItemListSize, errorList);
        exchangeErrorInfo(errorList, appException, new String[] {itemNameInfo});
    }

    /**
     * 注文商品数の販売可能在庫数チェック<br/>
     *
     * @param flag               在庫管理フラグ
     * @param salesPossibleStock 販売可能在庫数
     * @param orderItemCount     注文商品数（配送商品数）
     * @param itemNameInfo       商品名称情報
     * @param appException   アプリケーション例外
     */
    private void checkSalesPossibleStock(HTypeStockManagementFlag flag,
                                         BigDecimal salesPossibleStock,
                                         int orderItemCount,
                                         String itemNameInfo,
                                         ApplicationException appException) {
        // カート時と同じチェックを行い、エラーがあればエラー情報を調整する
        List<String> errorList = new ArrayList<>();
        this.checkSalesPossibleStock(flag, salesPossibleStock, orderItemCount, errorList);
        exchangeErrorInfo(errorList, appException, new String[] {itemNameInfo});
    }

    /**
     * 会員情報と商品情報をもとに、注文商品をチェック
     *
     * @param customerBirthday 顧客生年月日
     * @param alcoholFlag      アルコールフラグ
     * @param appException     アプリケーション例外
     * @param itemNameInfo     商品名称情報
     */
    private void checkAlcohol(Date customerBirthday,
                              HTypeAlcoholFlag alcoholFlag,
                              String itemNameInfo,
                              ApplicationException appException) {
        // 独自チェック　生年月日未設定はエラー
        if (customerBirthday == null) {
            appException.addMessage("PROMOTION-ENAC0001-E");
        }
        // カート時と同じチェックを行い、エラーがあればエラー情報を調整する
        List<String> errorList = new ArrayList<>();
        this.checkAlcohol(customerBirthday, alcoholFlag, errorList);
        exchangeErrorInfo(errorList, appException, new String[] {itemNameInfo});
    }

    /**
     * エラー情報変換
     *
     * @param errorList チェック結果エラーリスト
     * @param appException アプリケーション例外
     * @param msgArgs     メッセージ引数
     */
    private void exchangeErrorInfo(List<String> errorList, ApplicationException appException, String[] msgArgs) {
        if (!CollectionUtils.isEmpty(errorList)) {
            String msgCode = exchangeMessageCode(errorList.get(0));
            appException.addMessage(msgCode, msgArgs);
        }
    }

    /**
     * メッセージコード変換
     *
     * @param msgCode 変換前メッセージコード
     * @param itemNameInfo     商品名称情報
     */
    private String exchangeMessageCode(String msgCode) {
        // メッセージ変換が行えた場合は変換後のメッセージコード、行えなかった場合は元のメッセージコードを返却
        String newMsgCode = MSGCODEMAP_FOR_ORDERCHECK_TO_EXCHANGE_CART_TO_ORDER.get(msgCode);
        return newMsgCode != null ? newMsgCode : msgCode;
    }

    /**
     * 注文商品リストをもとに、商品サービスから商品詳細リストを取得する<br/>
     * 商品サービスから取得した商品詳細リストは、注文商品リストの商品ID順に並び変える
     *
     * @param orderItemList 注文商品リスト
     * @return 商品詳細リスト
     */
    private List<GoodsDetailsDto> getGoodsDetailsDtoList(List<OrderItem> orderItemList) {

        // 注文商品リストから商品IDリストを作成
        List<String> idList = getItemIdList(orderItemList);

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> dtoList = this.productAdapter.getDetails(idList);

        // 商品詳細リストのチェック
        // 商品詳細リストが取得できない場合はエラー
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new DomainException("PROMOTION-GOOD0001-E");
        }
        // 商品詳細リストを返却
        return dtoList;
    }

    /**
     * 注文商品リストをもとに、物流サービスから在庫詳細リストを取得する<br/>
     * 物流サービスから取得した在庫詳細リストは、注文商品リストの商品ID順に並び変える
     *
     * @param orderItemList 注文商品リスト
     * @return 在庫詳細リスト
     */
    private List<StockDto> getStockDtoList(List<OrderItem> orderItemList) {

        // 注文商品リストから商品IDリストを作成
        List<String> idList = getItemIdList(orderItemList);

        // 物流サービスから在庫詳細リストを取得
        List<StockDto> dtoList = this.stockAdapter.getDetails(idList);

        // 在庫詳細リストのチェック
        // 在庫詳細リストが取得できない場合はエラー
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new DomainException("PROMOTION-STOK0001-E");
        }
        // 在庫詳細リストを返却
        return dtoList;
    }

    /**
     * 商品IDリスト取得
     * @param orderItemList 注文商品リスト
     * @return 商品IDリスト
     */
    private List<String> getItemIdList(List<OrderItem> orderItemList) {
        List<String> itemIdList = new ArrayList<>();

        // 注文商品リストから商品IDリストを作成
        if (!CollectionUtils.isEmpty(orderItemList)) {
            orderItemList.forEach(orderItem -> {
                itemIdList.add(orderItem.getItemId());
            });
        }
        return itemIdList;
    }

    /**
     * 商品詳細Dto取得<br/>
     *
     * @param goodsDetailsDtoList 商品詳細リスト
     * @param itemId 商品ID
     * @return 商品詳細Dto
     */
    private GoodsDetailsDto getGoodsDetailsDtoByItemId(List<GoodsDetailsDto> goodsDetailsDtoList, String itemId) {
        // 商品IDが一致するDtoを返却
        if (!CollectionUtils.isEmpty(goodsDetailsDtoList)) {
            for (GoodsDetailsDto goodsDetailsDto : goodsDetailsDtoList) {
                if (goodsDetailsDto.getGoodsSeq() != null && goodsDetailsDto.getGoodsSeq()
                                                                            .equals(conversionUtility.toInteger(
                                                                                            itemId))) {
                    return goodsDetailsDto;
                }
            }
        }
        // 見つからなければ例外スロー
        throw new DomainException("PROMOTION-GOOD0002-E");
    }

    /**
     * 在庫詳細Dto取得<br/>
     *
     * @param stockDtoList 在庫詳細リスト
     * @param itemId 商品ID
     * @return 在庫詳細Dto
     */
    private StockDto getStockDtoByItemId(List<StockDto> stockDtoList, String itemId) {
        // 商品IDが一致するDtoを返却
        if (!CollectionUtils.isEmpty(stockDtoList)) {
            for (StockDto stockDto : stockDtoList) {
                if (stockDto.getGoodsSeq() != null && stockDto.getGoodsSeq()
                                                              .equals(conversionUtility.toInteger(itemId))) {
                    return stockDto;
                }
            }
        }
        // 見つからなければ例外スロー
        throw new DomainException("PROMOTION-STOK0002-E");
    }

}
