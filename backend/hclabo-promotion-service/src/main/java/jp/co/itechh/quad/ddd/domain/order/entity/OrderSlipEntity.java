/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.entity;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeqFactory;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 注文票エンティティ
 */
@Getter
public class OrderSlipEntity {

    /** 注文票ID */
    protected OrderSlipId orderSlipId;

    /** 注文ステータス */
    protected OrderStatus orderStatus;

    /** 注文商品リスト */
    protected List<OrderItem> orderItemList;

    /** 顧客ID */
    protected String customerId;

    /** 取引ID */
    protected String transactionId;

    /** 登録日時 */
    protected Date registDate;

    /** ユーザーエージェント */
    protected String userAgent;

    /** コンストラクタ */
    OrderSlipEntity() {
    }

    /**
     * コンストラクタ<br/>
     * 注文票発行<br/>
     * ※パッケージプライベートメソッド<br/>
     * ※ファクトリあり
     *
     * @param customerId 顧客ID
     * @param registDate 登録日時
     */
    OrderSlipEntity(String customerId, Date registDate) {

        // チェック
        AssertChecker.assertNotEmpty("customerId is empty", customerId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        // 設定
        this.orderSlipId = new OrderSlipId();
        // 注文票ステータス下書き
        this.orderStatus = OrderStatus.DRAFT;
        this.customerId = customerId;
        this.registDate = registDate;
        this.orderItemList = new ArrayList<>();
    }

    /**
     * コンストラクタ<br/>
     * 注文票改訂(改訂用注文票から注文票生成)
     *
     * @param orderSlipForRevisionEntity 改訂用注文票
     */
    public OrderSlipEntity(OrderSlipForRevisionEntity orderSlipForRevisionEntity) {

        // チェック
        AssertChecker.assertNotNull("orderPayment is null", orderSlipForRevisionEntity);

        /* 設定 */
        // 改訂用注文決済をコピー
        copyProperties(orderSlipForRevisionEntity);
        // 改訂注文票の取引IDを設定
        this.transactionId = orderSlipForRevisionEntity.getTransactionRevisionId();
        // 改訂注文票のIDを引き継いでID設定
        this.orderSlipId = new OrderSlipId(orderSlipForRevisionEntity.getOrderSlipRevisionId().getValue());
        // 改訂注文票の注文商品を設定
        this.orderItemList = new ArrayList<>();
        for (OrderItem orderItem : orderSlipForRevisionEntity.getOrderItemRevisionList()) {
            this.orderItemList.add(orderItem);
        }
    }

    /**
     * 注文商品追加
     *
     * @param itemId     商品ID
     * @param orderCount 追加数量
     * @return 追加した注文商品値オブジェクト
     */
    public OrderItem addOrderItem(String itemId, OrderCount orderCount) {

        // チェック
        AssertChecker.assertNotEmpty("itemId is empty", itemId);
        AssertChecker.assertNotNull("orderCount is null", orderCount);
        AssertChecker.assertIntegerNotZero("orderCount is zero", orderCount.getValue());
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }

        // 注文商品リストがNullの場合、インスタンスを生成
        if (this.orderItemList == null) {
            this.orderItemList = new ArrayList<>();
        }

        // 注文商品にすでに商品があるか判断
        int existListIndex = -1; // 注文商品が存在する場合のListインデックス
        if (!CollectionUtils.isEmpty(this.orderItemList)) {
            for (int i = 0; i < this.orderItemList.size(); i++) {
                // 商品IDが存在する
                if (itemId.equals(this.orderItemList.get(i).getItemId())) {
                    existListIndex = i;
                    break;
                }
            }
        }

        if (existListIndex >= 0) {
            // すでに注文商品がある場合は数量加算
            OrderItem existOrderItem = this.orderItemList.get(existListIndex);

            // 数量加算した注文数量オブジェクト生成
            int orderCountValue = existOrderItem.getOrderCount().getValue() + orderCount.getValue();
            AssertChecker.assertIntegerNotZero("orderCount is zero", orderCountValue);

            OrderItem oldOrderItem = this.orderItemList.get(existListIndex);
            OrderItem newOrderItem = new OrderItem(oldOrderItem.getItemId(), oldOrderItem.getOrderItemSeq(),
                                                   new OrderCount(orderCountValue), oldOrderItem.getOrderItemId()
            );

            // 注文商品値オブジェクト付け替え
            this.orderItemList.remove(existListIndex);
            this.orderItemList.add(existListIndex, newOrderItem);
            // 注文商品値オブジェクトを呼出元に返却
            return newOrderItem;
        } else {
            // すでに商品がない場合は注文商品追加

            OrderItemSeq orderItemSeq = new OrderItemSeq(this.orderItemList.size());
            OrderItem newOrderItem =
                            new OrderItem(itemId, OrderItemSeqFactory.constructOrderItemSeq(this.orderItemList),
                                          orderCount, new OrderItemId()
                            );
            this.orderItemList.add(newOrderItem);
            // 注文商品値オブジェクトを呼出元に返却
            return newOrderItem;
        }
    }

    /**
     * 注文商品数量一括変更
     *
     * @param changeOrderItemCountDomainParamList 注文商品数量変更ユースケースパラメータ
     */
    public void changeOrderItemCount(List<ChangeOrderItemCountDomainParam> changeOrderItemCountDomainParamList) {

        // チェック
        AssertChecker.assertNotEmpty("orderItemList is empty", changeOrderItemCountDomainParamList);
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }
        for (int target = 0; target < orderItemList.size(); target++) {

            // 商品数量が0の場合はエラー
            if (orderItemList.get(target).getOrderCount().getValue() == 0) {
                throw new DomainException("PROMOTION-ODER0005-E");
            }

            // 渡された注文商品リストの商品IDを重複チェック
            for (int i = target + 1; i < orderItemList.size(); i++) {
                if (orderItemList.get(target).getItemId().equals(orderItemList.get(i).getItemId())) {
                    throw new DomainException("PROMOTION-ODER0003-E");
                }
            }
        }

        // 注文数量変更処理
        for (ChangeOrderItemCountDomainParam changeOrderCountParam : changeOrderItemCountDomainParamList) {

            int existListIndex = -1; // 注文商品が存在する場合のListインデックス
            if (!CollectionUtils.isEmpty(this.orderItemList)) {
                for (int i = 0; i < this.orderItemList.size(); i++) {
                    if (this.orderItemList.get(i).getItemId().equals(changeOrderCountParam.getItemId())) {
                        existListIndex = i;
                        break;
                    }
                }
            }

            // 渡された注文商品の商品IDが存在しない場合はエラー
            if (existListIndex < 0) {
                throw new DomainException("PROMOTION-ODER0009-E");
            }

            // 注文数量上書き変更
            OrderItem oldOrderItem = this.orderItemList.get(existListIndex);
            OrderItem newOrderItem = new OrderItem(oldOrderItem.getItemId(), oldOrderItem.getOrderItemSeq(),
                    new OrderCount(changeOrderCountParam.getOrderCount()),
                    oldOrderItem.getOrderItemId()
            );

            // 設定
            this.orderItemList.remove(existListIndex);
            this.orderItemList.add(existListIndex, newOrderItem);
        }
    }

    /**
     * 注文商品削除
     *
     * @param itemId 商品ID
     */
    public void deleteOrderItem(String itemId) {

        // チェック
        AssertChecker.assertNotEmpty("itemId is empty", itemId);
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }

        int existListIndex = -1; // 商品が存在する場合のListインデックス
        if (!CollectionUtils.isEmpty(this.orderItemList)) {
            for (int i = 0; i < this.orderItemList.size(); i++) {
                if (StringUtils.equals(itemId, this.orderItemList.get(i).getItemId())) {
                    existListIndex = i;
                    break;
                }
            }
        }

        // 渡された商品IDが存在しない場合は何もしない
        if (existListIndex < 0) {
            return;
        }

        // 設定
        this.orderItemList.remove(existListIndex);

        if (this.orderItemList == null) {
            this.orderItemList = new ArrayList<>();
        }
    }

    /**
     * 注文商品統合<br/>
     * ※パッケージプライベートメソッド
     *
     * @param orderItemListFrom 統合元の注文商品リスト
     */
    void mergeOrderItem(List<OrderItem> orderItemListFrom) {

        // 統合元の注文商品リストをループして商品マージ
        for (OrderItem orderItemFrom : orderItemListFrom) {

            // 統合先（本エンティティ）に統合元データを追加する
            addOrderItem(orderItemFrom.getItemId(), orderItemFrom.getOrderCount());
        }
    }

    /**
     * 商品商品のマスタ設定<br/>
     * 取引開始のタイミングで呼び出され、このタイミングで初めて注文商品に商品マスタの情報が設定される
     *
     * @param itemId           商品ID（商品サービスの商品SEQ）
     * @param itemName         商品名
     * @param unitTitle1       規格タイトル1
     * @param unitValue1       規格値1
     * @param unitTitle2       規格タイトル2
     * @param unitValue2       規格値2
     * @param janCode          JANコード
     * @param noveltyGoodsType ノベルティ商品フラグ
     */
    public void settingItemInfo(String itemId,
                                String itemName,
                                String unitTitle1,
                                String unitValue1,
                                String unitTitle2,
                                String unitValue2,
                                String janCode,
                                HTypeNoveltyGoodsType noveltyGoodsType) {

        // チェック
        // 渡された商品IDが設定されていない場合はエラー
        AssertChecker.assertNotEmpty("itemId is empty", itemId);
        // 渡された商品名が設定されていない場合はエラー
        AssertChecker.assertNotEmpty("itemName is empty", itemName);
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }
        // 注文商品リストが設定されていない場合はエラー
        AssertChecker.assertNotEmpty("orderItemList　is empty", this.orderItemList);

        // 対象の注文商品とインデックスを取得し、注文商品に引数でわたってきた商品マスタの情報を設定
        OrderItem target = null;
        for (int i = 0; i < this.orderItemList.size(); i++) {
            if (this.orderItemList.get(i).getItemId().equals(itemId)) {
                target = this.orderItemList.get(i);
                this.orderItemList.remove(i);
                this.orderItemList.add(i, new OrderItem(target.getItemId(), target.getOrderItemSeq(),
                                                        target.getOrderCount(), itemName, unitTitle1, unitValue1,
                                                        unitTitle2, unitValue2, janCode, noveltyGoodsType,
                                                        target.getOrderItemId()
                ));
            }
        }
        // 対象の注文商品が存在しなかった場合
        if (ObjectUtils.isEmpty(target)) {
            AssertChecker.assertNotNull("orderItem　is empty", this.orderItemList);
        }
    }

    /**
     * 注文票トランザクションデータ最新化
     *
     * @param orderItemList 注文商品リスト
     */
    public void modernizeOrderSlipTranData(List<OrderItem> orderItemList) {
        // 設定
        this.orderItemList = orderItemList;
    }

    /**
     * 注文商品数量合計取得
     *
     * @return totalOrderItemCount 注文数量合計
     */
    public Integer countOrderItemCountTotal() {

        // 注文商品数合計
        Integer totalOrderItemCount = 0;

        // 注文数量が空でない場合のみ、商品数合計を計算する
        if (!CollectionUtils.isEmpty(this.orderItemList)) {
            for (int i = 0; i < this.orderItemList.size(); i++) {
                totalOrderItemCount += this.orderItemList.get(i).getOrderCount().getValue();
            }
        }

        return totalOrderItemCount;
    }

    /**
     * 取引ID設定(注文フロー開始)
     *
     * @param transactionId 取引ID
     */
    public void settingTransactionId(String transactionId) {

        // チェック
        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }
        // 注文商品リストが設定されていない場合はエラー
        AssertChecker.assertNotEmpty("orderItemList　is empty", this.orderItemList);

        // 設定
        this.transactionId = transactionId;
    }

    /**
     * 取引確定
     */
    public void openSlip() {

        // チェック
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }
        // 注文商品リストが設定されていない場合はエラー
        AssertChecker.assertNotEmpty("orderItemList　is empty", this.orderItemList);

        // 注文票ステータス確定
        this.orderStatus = OrderStatus.OPEN;
    }

    /**
     * ユーザーエージェントの設定
     */
    public void settingUserAgent(String userAgent) {

        // チェック
        AssertChecker.assertNotEmpty("userAgent is empty", userAgent);

        // ユーザーエージェント
        this.userAgent = userAgent;
    }

    void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    /**
     *  商品リストを個数単位で分割し、数量をすべて1にする
     */
    public void itemListDivision() {
        List<OrderItem> itemListDivision = new ArrayList<>();
        OrderItem orderItemNew;

        // 分割前は商品連番順にソートされている想定だ
        this.getOrderItemList().sort(Comparator.comparing(orderItem -> orderItem.getOrderItemSeq().getValue()));

        for (OrderItem orderItem : this.getOrderItemList()) {

            int count = orderItem.getOrderCount().getValue();
            OrderItemSeq orderItemSeqCur = OrderItemSeqFactory.constructOrderItemSeq(itemListDivision);

            // 数量が1の場合は、変更後商品リストの末尾に追加する
            if (count == 1) {
                orderItemNew = new OrderItem(
                    orderItem.getItemId(),
                    orderItemSeqCur,
                    new OrderCount(1),
                    orderItem.getItemName(),
                    orderItem.getUnitTitle1(),
                    orderItem.getUnitValue1(),
                    orderItem.getUnitTitle2(),
                    orderItem.getUnitValue2(),
                    orderItem.getJanCode(),
                    orderItem.getNoveltyGoodsType(),
                    orderItem.getOrderItemId()
                );
                itemListDivision.add(orderItemNew);
            }
            // 数量が2以上の場合は、注文数量が1つずつになるように変更後商品リストの末尾に追加する
            else if (count > 1) {
                for (int i = 0; i < count; i++) {
                    OrderItemId orderItemIdNew = orderItem.getOrderItemId();
                    if (i > 0) {
                        orderItemIdNew = new OrderItemId();
                    }
                    orderItemNew = new OrderItem(
                        orderItem.getItemId(),
                        orderItemSeqCur,
                        new OrderCount(1),
                        orderItem.getItemName(),
                        orderItem.getUnitTitle1(),
                        orderItem.getUnitValue1(),
                        orderItem.getUnitTitle2(),
                        orderItem.getUnitValue2(),
                        orderItem.getJanCode(),
                        orderItem.getNoveltyGoodsType(),
                        orderItemIdNew
                    );
                    orderItemSeqCur = new OrderItemSeq(orderItemSeqCur.getValue() + 1);
                    itemListDivision.add(orderItemNew);
                }
            }

        }

        this.setOrderItemList(itemListDivision);
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public OrderSlipEntity(OrderSlipId orderSlipId,
                           OrderStatus orderStatus,
                           List<OrderItem> orderItemList,
                           String customerId,
                           String transactionId,
                           Date registDate,
                           String userAgent) {
        this.orderSlipId = orderSlipId;
        this.orderStatus = orderStatus;
        this.orderItemList = orderItemList;
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.registDate = registDate;
        this.userAgent = userAgent;
    }

    /**
     * プロパティコピー<br/>
     * ※改訂用注文票発行
     *
     * @param orderSlipEntity 注文票
     */
    protected void copyProperties(OrderSlipEntity orderSlipEntity) {
        this.orderSlipId = orderSlipEntity.getOrderSlipId();
        this.orderStatus = orderSlipEntity.getOrderStatus();
        this.orderItemList = orderSlipEntity.getOrderItemList();
        this.customerId = orderSlipEntity.getCustomerId();
        this.transactionId = orderSlipEntity.getTransactionId();
        this.registDate = orderSlipEntity.getRegistDate();
        this.userAgent = orderSlipEntity.getUserAgent();
    }

}