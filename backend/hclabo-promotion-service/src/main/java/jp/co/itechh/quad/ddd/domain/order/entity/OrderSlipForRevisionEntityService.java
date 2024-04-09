package jp.co.itechh.quad.ddd.domain.order.entity;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemRevision;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.stock.adapter.IStockAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 改訂用注文票ドメインサービス
 */
@Service
public class OrderSlipForRevisionEntityService {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 在庫アダプター */
    private final IStockAdapter stockAdapter;

    /** 顧客アダプター */
    private final ICustomerAdapter customerAdapter;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 会員業務ユーティリティクラス */
    private final MemberInfoUtility memberInfoUtility;

    /** コンストラクタ */
    @Autowired
    public OrderSlipForRevisionEntityService(IOrderSlipForRevisionRepository orderSlipForRevisionRepository,
                                             IProductAdapter productAdapter,
                                             IStockAdapter stockAdapter,
                                             ICustomerAdapter customerAdapter,
                                             ConversionUtility conversionUtility,
                                             MemberInfoUtility memberInfoUtility) {
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
        this.productAdapter = productAdapter;
        this.stockAdapter = stockAdapter;
        this.customerAdapter = customerAdapter;
        this.conversionUtility = conversionUtility;
        this.memberInfoUtility = memberInfoUtility;
    }

    /**
     * 注文商品削除<br/>
     * 改訂注文票の注文商品を削除する場合<br/>
     * 　・改訂元の注文票に含まれる注文商品を削除する場合　⇒　数量を0かつインデックスはそのままにして取り替える<br/>
     * 　・改訂元の注文票に含まれない注文商品を削除する場合　⇒　オブジェクトごと削除し、注文商品連番を再構成する
     *
     * @param orderItemSeq               注文商品連番
     * @param orderSlipForRevisionEntity 改訂用注文票
     */
    public void deleteOrderItemForRevision(OrderItemSeq orderItemSeq,
                                           OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        AssertChecker.assertNotNull("orderItemSeq ERROR", orderItemSeq);
        // 改訂用注文票チェック
        AssertChecker.assertNotNull("orderSlipForRevisionEntity ERROR", orderSlipForRevisionEntity);
        AssertChecker.assertNotEmpty("orderItemRevisionList", orderSlipForRevisionEntity.getOrderItemRevisionList());

        // 改訂元注文票の注文商品リストチェック
        List<OrderItem> originalItemList = orderSlipForRevisionEntity.getOrderItemList();
        if (CollectionUtils.isEmpty(originalItemList)) {
            throw new DomainException("PROMOTION-ODER0014-E");
        }

        // 注文商品連番で改訂用注文商品を取得する
        OrderItemRevision orderItemRevision = null;
        for (OrderItemRevision revisionItem : orderSlipForRevisionEntity.getOrderItemRevisionList()) {
            if (revisionItem.getOrderItemSeq().getValue() == orderItemSeq.getValue()) {
                orderItemRevision = revisionItem;
                break;
            }
        }

        // 改訂用注文商品の存在チェック
        if (orderItemRevision == null) {
            throw new DomainException("PROMOTION-ODER0015-E");
        }

        // 同じ注文商品連番の注文商品が、改訂元注文票に存在するかチェックする
        boolean deleteFlag = true;
        for (OrderItem item : originalItemList) {
            // 存在した場合
            if (item.getOrderItemSeq().getValue() == orderItemRevision.getOrderItemSeq().getValue()) {
                // 改訂用注文商品の数量を0に更新する
                OrderItemRevision replaceOrderItemRevision = new OrderItemRevision(orderItemRevision.getItemId(),
                                                                                   orderItemRevision.getOrderItemSeq(),
                                                                                   new OrderCount(0),
                                                                                   orderItemRevision.getItemName(),
                                                                                   orderItemRevision.getUnitTitle1(),
                                                                                   orderItemRevision.getUnitValue1(),
                                                                                   orderItemRevision.getUnitTitle2(),
                                                                                   orderItemRevision.getUnitValue2(),
                                                                                   orderItemRevision.getJanCode(),
                                                                                   orderItemRevision.getNoveltyGoodsType(),
                                                                                   orderItemRevision.getOrderItemId()
                );
                // 改訂用注文商品差し替え
                orderSlipForRevisionEntity.replaceOrderItemRevision(replaceOrderItemRevision);
                deleteFlag = false;
                break;
            }
        }

        // 存在しなかった場合
        if (deleteFlag) {
            // 改訂用注文商品削除
            orderSlipForRevisionEntity.deleteOrderItemRevision(orderItemRevision.getOrderItemSeq());
        }

        // 改訂用注文票更新
        this.orderSlipForRevisionRepository.update(orderSlipForRevisionEntity);
    }

    /**
     * 改訂用配送伝票チェック
     *
     * @param orderSlipForRevisionEntity 改訂用配送伝票
     */
    public void checkOrderSlipForRevision(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        // チェック
        AssertChecker.assertNotNull("orderSlipForRevisionEntity is null", orderSlipForRevisionEntity);

        ApplicationException appException = new ApplicationException();

        // 改訂用注文商品（改訂用配送商品）のチェック
        checkRevisionOrderItem(orderSlipForRevisionEntity, appException);

        if (appException.hasMessage()) {
            throw appException;
        }
    }

    /**
     * 改訂用注文票と商品情報を取得し、改訂用注文商品（改訂用配送商品）をチェック
     *
     * @param orderSlipForRevisionEntity 改訂用配送伝票
     * @param errorList             エラーリスト
     */
    private void checkRevisionOrderItem(OrderSlipForRevisionEntity orderSlipForRevisionEntity,
                                        ApplicationException errorList) {

        // 改訂用注文票から改訂元注文商品リストが取得できない場合はエラー
        List<OrderItem> originalItemList = orderSlipForRevisionEntity.getOrderItemList();
        if (CollectionUtils.isEmpty(originalItemList)) {
            throw new DomainException("PROMOTION-ORIL0002-E");
        }
        // 改訂用注文票から改訂用注文商品リストが取得できない場合はエラー
        List<OrderItemRevision> revisionItemList = orderSlipForRevisionEntity.getOrderItemRevisionList();
        if (CollectionUtils.isEmpty(revisionItemList)) {
            throw new DomainException("PROMOTION-ROIL0001-E");
        }

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> goodsDtoList = getGoodsDetailsDtoList(revisionItemList);

        // 物流サービスから在庫詳細リストを取得
        List<StockDto> stockDtoList = getStockDtoList(revisionItemList);

        // ユーザーサービスから顧客詳細を取得
        MemberInfoEntity memberInfoEntity = getMemberInfoEntity(orderSlipForRevisionEntity);

        /*********************************************
         数量増分のある改訂用注文商品に対するチェック
         *********************************************/
        checkOrderItemForIncreaseCount(
                        revisionItemList, originalItemList, goodsDtoList, stockDtoList, memberInfoEntity, errorList);

        /*********************************************
         サマリー数量の改訂用注文商品に対するチェック
         *********************************************/
        checkOrderItemForSummaryCount(revisionItemList, goodsDtoList, errorList);
    }

    /**
     * 数量増分のある改訂用注文商品に対するチェック
     *
     * @param revisionItemList 改訂用注文商品リスト
     * @param originalItemList 改訂元注文商品リスト
     * @param goodsDtoList 商品詳細リスト
     * @param stockDtoList 在庫詳細リスト
     * @param memberInfoEntity 顧客詳細
     * @param errorList エラーリスト
     */
    private void checkOrderItemForIncreaseCount(List<OrderItemRevision> revisionItemList,
                                                List<OrderItem> originalItemList,
                                                List<GoodsDetailsDto> goodsDtoList,
                                                List<StockDto> stockDtoList,
                                                MemberInfoEntity memberInfoEntity,
                                                ApplicationException errorList) {
        // 数量増分のある改訂用注文商品リストを取得
        List<OrderItemRevision> targetOrderItem1 =
                        getOrderItemRevisionListForCheck01(revisionItemList, originalItemList);
        for (OrderItemRevision orderItemRevisionForCheck : targetOrderItem1) {
            // 各種Dto取得
            GoodsDetailsDto goodsDto = getGoodsDetailsDtoByItemId(goodsDtoList, orderItemRevisionForCheck.getItemId());
            StockDto stockDto = getStockDtoByItemId(stockDtoList, orderItemRevisionForCheck.getItemId());

            // メッセージ用商品名＋規格情報を生成
            String itemNameInfo = orderItemRevisionForCheck.createItemNameInfo();

            // 公開状態が削除ステータスかどうかチェック
            checkOpenIsDelete(goodsDto.getGoodsOpenStatusPC(), goodsDto.getNoveltyGoodsType(), itemNameInfo, errorList);

            // 販売状態が削除ステータスかどうかチェック
            checkSaleIsDelete(goodsDto.getSaleStatusPC(), goodsDto.getNoveltyGoodsType(), itemNameInfo, errorList);

            // 販売可能在庫数チェック
            checkSalesPossibleStock(goodsDto.getStockManagementFlag(), stockDto.getSalesPossibleStock(),
                                    orderItemRevisionForCheck.getOrderCount().getValue(), itemNameInfo, errorList
                                   );

            // アルコールチェック
            checkAlcohol(memberInfoEntity.getMemberInfoBirthday(), goodsDto.getAlcoholFlag(), itemNameInfo, errorList);
        }
    }

    /**
     * サマリー数量の改訂用注文商品に対するチェック
     *
     * @param revisionItemList 改訂用注文商品リスト
     * @param goodsDtoList 商品詳細リスト
     * @param errorList エラーリスト
     */
    private void checkOrderItemForSummaryCount(List<OrderItemRevision> revisionItemList,
                                               List<GoodsDetailsDto> goodsDtoList,
                                               ApplicationException errorList) {
        List<OrderItemRevision> targetOrderItem2 = getOrderItemRevisionListForCheck02(revisionItemList);
        long sizeWithoutNovelty = targetOrderItem2.stream()
                                                  .filter(orderItem -> HTypeNoveltyGoodsType.NORMAL_GOODS.equals(
                                                                  orderItem.getNoveltyGoodsType()))
                                                  .count();
        for (OrderItemRevision orderItemRevisionForCheck : targetOrderItem2) {
            // 各種Dto取得
            GoodsDetailsDto goodsDto = getGoodsDetailsDtoByItemId(goodsDtoList, orderItemRevisionForCheck.getItemId());
            // メッセージ用商品名＋規格情報を生成
            String itemNameInfo = orderItemRevisionForCheck.createItemNameInfo();
            // 注文上限数チェック
            checkPurchasedMax(goodsDto.getPurchasedMax(), orderItemRevisionForCheck.getOrderCount().getValue(),
                              itemNameInfo, errorList
                             );
            // 個別配送品の同梱チェック
            checkIndividualDelivery(goodsDto.getIndividualDeliveryType(), goodsDto.getNoveltyGoodsType(),
                                    conversionUtility.toInteger(sizeWithoutNovelty), itemNameInfo, errorList
                                   );
        }
    }

    /**
     * 注文商品（配送商品）の公開状態が削除ステータスかどうかチェック<br/>
     *
     * @param openStatus   商品公開状態
     * @param noveltyGoodsType ノベルティ商品タイプ
     * @param itemNameInfo 商品名称情報
     * @param errorList    エラーリスト
     */
    private void checkOpenIsDelete(HTypeOpenDeleteStatus openStatus,
                                   HTypeNoveltyGoodsType noveltyGoodsType,
                                   String itemNameInfo,
                                   ApplicationException errorList) {
        // 公開状態がDELETEの場合はエラー
        if (openStatus == HTypeOpenDeleteStatus.DELETED && HTypeNoveltyGoodsType.NORMAL_GOODS.equals(
                        noveltyGoodsType)) {
            errorList.addMessage("PROMOTION-EOSD0001-E", new String[] {itemNameInfo});
        }
    }

    /**
     * 注文商品（配送商品）の販売状態が削除ステータスかどうかチェック<br/>
     *
     * @param saleStatus   商品販売状態
     * @param noveltyGoodsType ノベルティ商品タイプ
     * @param itemNameInfo 商品名称情報
     * @param errorList    エラーリスト
     */
    private void checkSaleIsDelete(HTypeGoodsSaleStatus saleStatus,
                                   HTypeNoveltyGoodsType noveltyGoodsType,
                                   String itemNameInfo,
                                   ApplicationException errorList) {
        // 販売状態がDELETEの場合はエラー
        if (saleStatus == HTypeGoodsSaleStatus.DELETED && HTypeNoveltyGoodsType.NORMAL_GOODS.equals(noveltyGoodsType)) {
            errorList.addMessage("PROMOTION-ESSD0001-E", new String[] {itemNameInfo});
        }
    }

    /**
     * 注文商品数（配送商品数）の販売可能在庫数チェック<br/>
     *
     * @param flag               在庫管理フラグ
     * @param salesPossibleStock 販売可能在庫数
     * @param orderItemCount     注文商品数（配送商品数）
     * @param itemNameInfo       商品名称情報
     * @param errorList          エラーリスト
     */
    private void checkSalesPossibleStock(HTypeStockManagementFlag flag,
                                         BigDecimal salesPossibleStock,
                                         int orderItemCount,
                                         String itemNameInfo,
                                         ApplicationException errorList) {
        // 在庫管理OFFの場合チェック不要
        if (HTypeStockManagementFlag.OFF.equals(flag)) {
            return;
        }
        // 販売可能在庫数が無い（０以下）場合はエラー
        if (0 >= salesPossibleStock.intValue()) {
            errorList.addMessage("PROMOTION-ESPS0001-E", new String[] {itemNameInfo});

            // 注文商品数が販売可能在庫数より多い場合はエラー
        } else if (orderItemCount > salesPossibleStock.intValue()) {
            errorList.addMessage("PROMOTION-ESPS0002-E", new String[] {itemNameInfo});
        }
    }

    /**
     * 注文商品数（配送商品数）の注文上限数チェック<br/>
     *
     * @param purchasedMax   注文上限数
     * @param orderItemCount 注文商品数（配送商品数）
     * @param itemNameInfo   商品名称情報
     * @param errorList      エラーリスト
     */
    private void checkPurchasedMax(BigDecimal purchasedMax,
                                   int orderItemCount,
                                   String itemNameInfo,
                                   ApplicationException errorList) {

        // 注文上限数が０(無制限)ではなく、注文商品数が注文上限数より多い場合はエラー
        if (purchasedMax != null && purchasedMax.compareTo(BigDecimal.ZERO) != 0
            && orderItemCount > purchasedMax.intValue()) {
            errorList.addMessage("PROMOTION-ENPM0001-E", new String[] {itemNameInfo});
        }
    }

    /**
     * 個別配送品の同梱チェック
     *
     * @param individualDeliveryType 個別配送種別
     * @param orderItemListSize 注文商品数（配送商品数）
     * @param itemNameInfo      商品名称情報
     * @param errorList         エラーリスト
     */
    private void checkIndividualDelivery(HTypeIndividualDeliveryType individualDeliveryType,
                                         HTypeNoveltyGoodsType noveltyGoodsType,
                                         int orderItemListSize,
                                         String itemNameInfo,
                                         ApplicationException errorList) {

        // 対象商品が個別配送品であり、注文商品数が1つより多い場合はエラー
        if (individualDeliveryType == HTypeIndividualDeliveryType.ON && orderItemListSize > 1
            && HTypeNoveltyGoodsType.NORMAL_GOODS.equals(noveltyGoodsType)) {
            errorList.addMessage("PROMOTION-EIND0001-E", new String[] {itemNameInfo});
        }
    }

    /**
     * 会員情報と商品情報をもとに、注文商品をチェック
     *
     * @param customerBirthday 顧客生年月日
     * @param alcoholFlag      アルコールフラグ
     * @param itemNameInfo     商品名称情報
     * @param errorList        エラーリスト
     */
    private void checkAlcohol(Date customerBirthday,
                              HTypeAlcoholFlag alcoholFlag,
                              String itemNameInfo,
                              ApplicationException errorList) {

        // 生年月日が存在しない場合
        if (customerBirthday == null) {
            errorList.addMessage("PROMOTION-ENAC0001-E");
        }
        // 酒類年齢制限チェック
        // 対象注文商品がアルコール商品かつ、未成年の場合はエラー
        if (alcoholFlag == HTypeAlcoholFlag.ALCOHOL && !this.memberInfoUtility.isAdult(
                        this.conversionUtility.toTimestamp(customerBirthday))) {
            errorList.addMessage("PROMOTION-ENAC0002-E", new String[] {itemNameInfo});
        }
    }

    /**
     * 妥当性チェックのための改訂用注文商品リストを取得<br/>
     * 数量増分のあった改訂用注文商品のリストを返却する
     *
     * @param revisionItemList 改訂用注文商品リスト
     * @param originalItemList 改訂元注文商品リスト
     * @return 数量増分のあった改訂用注文商品のリスト
     */
    private List<OrderItemRevision> getOrderItemRevisionListForCheck01(List<OrderItemRevision> revisionItemList,
                                                                       List<OrderItem> originalItemList) {
        // 数量増分のあった改訂用注文商品を抽出する
        List<OrderItemRevision> checkTargetList = new ArrayList<>();

        // 改訂用注文商品の件数分ループ
        for (OrderItemRevision orderItemRevision : revisionItemList) {
            // 改訂元注文商品を取得
            OrderItem orderItemOriginal = getOriginal(orderItemRevision, originalItemList);
            // 数量増分を取得
            int increaseCount = getIncreaseCount(orderItemRevision, orderItemOriginal);
            // 数量増分有り
            if (increaseCount > 0) {
                // 数量増加商品はチェック対象とする（チェック用にインスタンス生成）
                // ★数量は増分値をもたせる（チェック対象数量は増分値であるため）★
                OrderItemRevision newInstanceForCheck = new OrderItemRevision(orderItemRevision.getItemId(),
                                                                              orderItemRevision.getOrderItemSeq(),
                                                                              new OrderCount(increaseCount),
                                                                              orderItemRevision.getItemName(),
                                                                              orderItemRevision.getUnitTitle1(),
                                                                              orderItemRevision.getUnitValue1(),
                                                                              orderItemRevision.getUnitTitle2(),
                                                                              orderItemRevision.getUnitValue2(),
                                                                              orderItemRevision.getJanCode(),
                                                                              orderItemRevision.getNoveltyGoodsType(),
                                                                              orderItemRevision.getOrderItemId()
                );
                checkTargetList.add(newInstanceForCheck);
            }
        }
        // チェック用リストを返却
        return checkTargetList;
    }

    /**
     * 改訂元注文商品を取得<br/>
     * 第一引数で渡された改訂用注文商品と同じ注文商品連番を持つ改訂元注文商品を返却する<br/>
     * 該当する改訂元注文商品が存在しなければnullを返却する
     *
     * @param orderItemRevision 改訂用注文商品
     * @param originalItemList 改訂元注文商品リスト
     * @return 改訂元注文商品
     */
    private OrderItem getOriginal(OrderItemRevision orderItemRevision, List<OrderItem> originalItemList) {
        // 改訂元注文商品の件数分ループ
        for (OrderItem original : originalItemList) {
            // 注文商品連番が一致する改訂元注文商品を返却
            if (orderItemRevision.getOrderItemSeq().getValue() == original.getOrderItemSeq().getValue()) {
                return original;
            }
        }
        // 上記以外はfalseを返却
        return null;
    }

    /**
     * 数量増分を取得<br/>
     * 改訂元注文商品⇒改訂用注文商品で、数量の増分値を返却する<br/>
     * 改訂元注文商品がない場合、改訂元数量を0として計算する
     * @param orderItemRevision 改訂用注文商品
     * @param orderItemOriginal 改訂元注文商品
     * @return 数量増分値
     */
    private int getIncreaseCount(OrderItemRevision orderItemRevision, OrderItem orderItemOriginal) {
        // 今回数量を取得
        int revOrderCount = orderItemRevision == null ? 0 : orderItemRevision.getOrderCount().getValue();
        // 改訂元数量を取得（改訂元が無ければ0)
        int orgOrderCount = orderItemOriginal == null ? 0 : orderItemOriginal.getOrderCount().getValue();
        // 今回数量 ― 改訂元数量を返却
        return (revOrderCount - orgOrderCount);
    }

    /**
     * 妥当性チェックのための改訂用注文商品リストを取得<br/>
     * 同一商品IDの別レコードがあった場合に、その数量をサマリーした改訂用注文商品のリストを返却する<br/>
     * 尚、数量0の商品は含めない（キャンセル商品であるため、むしろ、チェック対象に含めてはいけない）<br/>
     *
     * @param revisionItemList 改訂用注文商品リスト
     * @return サマリー数量のあった改訂用注文商品のリスト
     */
    private List<OrderItemRevision> getOrderItemRevisionListForCheck02(List<OrderItemRevision> revisionItemList) {
        // 商品IDで数量サマリーした改訂用注文商品リスト
        List<OrderItemRevision> checkTargetList = new ArrayList<>();

        // 改訂用注文商品の件数分ループ
        for (OrderItemRevision orderItemRevision : revisionItemList) {
            // チェック用リストに、すでに同一商品IDが含まれている場合はループスキップ
            if (existItemId(orderItemRevision, checkTargetList)) {
                continue;
            }
            // サマリー数量を取得
            int summaryCount = getSummaryCount(orderItemRevision, revisionItemList);
            // サマリー数量有り
            if (summaryCount > 0) {
                // 数量増加商品はチェック対象とする（チェック用にインスタンス生成）
                // ★数量はサマリー値をもたせる（チェック対象数量は増分値であるため）★
                OrderItemRevision newInstanceForCheck = new OrderItemRevision(orderItemRevision.getItemId(),
                                                                              orderItemRevision.getOrderItemSeq(),
                                                                              new OrderCount(summaryCount),
                                                                              orderItemRevision.getItemName(),
                                                                              orderItemRevision.getUnitTitle1(),
                                                                              orderItemRevision.getUnitValue1(),
                                                                              orderItemRevision.getUnitTitle2(),
                                                                              orderItemRevision.getUnitValue2(),
                                                                              orderItemRevision.getJanCode(),
                                                                              orderItemRevision.getNoveltyGoodsType(),
                                                                              orderItemRevision.getOrderItemId()
                );
                checkTargetList.add(newInstanceForCheck);
            }
        }
        // チェック用リストを返却
        return checkTargetList;
    }

    /**
     * 同一商品ID存在チェック
     * @param orderItemRevision 改訂用注文商品
     * @param checkTargetList 存在チェックターゲットリスト
     * @return チェック結果...true：存在
     */
    private boolean existItemId(OrderItemRevision orderItemRevision, List<OrderItemRevision> checkTargetList) {
        for (OrderItemRevision currentItem : checkTargetList) {
            if (StringUtils.equals(orderItemRevision.getItemId(), currentItem.getItemId())) {
                // 同一商品IDのレコードが存在すればtrueを返却
                return true;
            }
        }
        // 同一商品IDのレコードが存在しなければfalseを返却
        return false;
    }

    /**
     * サマリー数量を取得<br/>
     * 改訂元注文商品リスト内で同一商品IDの数量をサマリーした値を返却する<br/>
     * @param orderItemRevision 改訂用注文商品
     * @param revisionItemList 改訂用注文商品リスト
     * @return サマリー数量
     */
    private int getSummaryCount(OrderItemRevision orderItemRevision, List<OrderItemRevision> revisionItemList) {
        // 今回数量を取得
        int revOrderCount = ObjectUtils.isEmpty(orderItemRevision) ? 0 : orderItemRevision.getOrderCount().getValue();

        for (OrderItemRevision currentItem : revisionItemList) {
            // 自分自身はスキップ
            if (ObjectUtils.isNotEmpty(orderItemRevision)
                && orderItemRevision.getOrderItemSeq().getValue() == currentItem.getOrderItemSeq().getValue()) {
                continue;
            }
            // 自分自身以外で同じ商品IDのレコードがあれば数量を足しこむ
            if (ObjectUtils.isNotEmpty(orderItemRevision) && StringUtils.equals(
                            orderItemRevision.getItemId(), currentItem.getItemId())) {
                revOrderCount += currentItem.getOrderCount().getValue();
            }
        }
        // サマリー数量を返却
        return (revOrderCount);
    }

    /**
     * 注文商品リストをもとに、商品サービスから商品詳細リストを取得する<br/>
     * 重複する商品IDは除外
     *
     * @param revisionItemList 注文商品リスト
     * @return 商品詳細リスト
     */
    private List<GoodsDetailsDto> getGoodsDetailsDtoList(List<OrderItemRevision> revisionItemList) {

        // 注文商品リストから商品IDリストを作成
        List<String> idList = getItemIdList(revisionItemList);

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> dtoList = this.productAdapter.getDetails(idList);

        // 商品詳細リストが取得できない場合はエラー
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new DomainException("PROMOTION-GOOD0001-E");
        }

        return dtoList;
    }

    /**
     * 注文商品リストをもとに、商品サービスから商品詳細リストを取得する<br/>
     * 重複する商品IDは除外
     *
     * @param revisionItemList 改訂用注文商品リスト
     * @return 商品詳細リスト
     */
    private List<StockDto> getStockDtoList(List<OrderItemRevision> revisionItemList) {

        // 注文商品リストから商品IDリストを作成
        List<String> idList = getItemIdList(revisionItemList);

        // 物流サービスから在庫詳細リストを取得
        List<StockDto> dtoList = this.stockAdapter.getDetails(idList);

        // 在庫詳細リストが取得できない場合はエラー
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new DomainException("PROMOTION-STOK0001-E");
        }

        return dtoList;
    }

    /**
     * 商品IDリスト取得
     * @param revisionItemList 改訂用注文商品リスト
     * @return 商品IDリスト
     */
    private List<String> getItemIdList(List<OrderItemRevision> revisionItemList) {
        List<String> itemIdList = new ArrayList<>();

        // 注文商品リストから商品IDリストを作成
        if (!CollectionUtils.isEmpty(revisionItemList)) {
            revisionItemList.forEach(orderItem -> {
                // 重複する商品IDの重複は除外
                if (!itemIdList.contains(orderItem.getItemId())) {
                    itemIdList.add(orderItem.getItemId());
                }
            });
        }
        return itemIdList;
    }

    /**
     * ユーザーサービスから会員Entityを取得
     * @param orderSlipForRevisionEntity 改訂用注文票
     * @return 会員Entity
     */
    private MemberInfoEntity getMemberInfoEntity(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {
        Integer customerId = this.conversionUtility.toInteger(orderSlipForRevisionEntity.getCustomerId());
        return customerAdapter.getMemberInfoEntity(customerId);
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