package jp.co.itechh.quad.ddd.domain.order.entity;

import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemRevision;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.order.ChangeOrderItemCountForRevisionUseCaseParam;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 改訂用注文票エンティティ
 */
@Getter
public class OrderSlipForRevisionEntity extends OrderSlipEntity {

    /** 改訂用注文票ID */
    private OrderSlipRevisionId orderSlipRevisionId;

    /** 改訂用注文商品リスト */
    private List<OrderItemRevision> orderItemRevisionList;

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /**
     * コンストラクタ
     * 改訂用注文票発行
     *
     * @param originalOrderSlipEntity 改訂元注文票
     * @param transactionRevisionId   改訂用取引ID
     * @param registDate              登録日時
     */
    public OrderSlipForRevisionEntity(OrderSlipEntity originalOrderSlipEntity,
                                      String transactionRevisionId,
                                      Date registDate) {

        super();

        // チェック
        AssertChecker.assertNotNull("originalOrderSlipEntity is null", originalOrderSlipEntity);
        AssertChecker.assertNotEmpty("transactionRevisionId is null", transactionRevisionId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        // 設定
        // 元注文票をコピー
        super.copyProperties(originalOrderSlipEntity);
        // 改訂用注文票設定
        this.orderSlipRevisionId = new OrderSlipRevisionId();
        this.transactionRevisionId = transactionRevisionId;
        this.registDate = registDate;

        // 注文商品連番を構成し、注文商品リストと改訂用注文商品リストを設定
        this.orderItemList = new ArrayList<>();
        this.orderItemRevisionList = new ArrayList<>();

        for (OrderItem originalOrderItem : originalOrderSlipEntity.getOrderItemList()) {

            // 改訂元注文商品リストにセット
            this.orderItemList.add(originalOrderItem);

            // 改訂用注文商品リストにセット
            this.orderItemRevisionList.add(
                            new OrderItemRevision(originalOrderItem.getItemId(), originalOrderItem.getOrderItemSeq(),
                                                  originalOrderItem.getOrderCount(), originalOrderItem.getItemName(),
                                                  originalOrderItem.getUnitTitle1(), originalOrderItem.getUnitValue1(),
                                                  originalOrderItem.getUnitTitle2(), originalOrderItem.getUnitValue2(),
                                                  originalOrderItem.getJanCode(),
                                                  originalOrderItem.getNoveltyGoodsType(),
                                                  originalOrderItem.getOrderItemId()
                            ));
        }
    }

    /**
     * 改訂用注文商品追加</br>
     * 既に同一商品IDの注文商品が設定されている場合も、末尾に注文商品を設定
     *
     * @param orderItemRevision 改訂用注文商品
     */
    public void addOrderItemRevision(OrderItemRevision orderItemRevision) {

        // チェック
        AssertChecker.assertNotNull("orderItemRevision is null", orderItemRevision);

        // 末尾に注文商品を追加
        this.orderItemRevisionList.add(orderItemRevision);
    }

    /**
     * 改訂用注文商品数量一括変更
     *
     * @param changeOrderCountParamList 商品数量変更パラメータリスト
     */
    public void changeOrderItemRevision(List<ChangeOrderItemCountForRevisionUseCaseParam> changeOrderCountParamList) {

        // チェック
        AssertChecker.assertNotEmpty("error_orderItemList", changeOrderCountParamList);
        // 下書き状態でないならエラー
        if (this.orderStatus != OrderStatus.OPEN) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }

        // 注文数量変更処理
        for (ChangeOrderItemCountForRevisionUseCaseParam changeOrderCountParam : changeOrderCountParamList) {

            int existListIndex = -1; // 注文商品が存在する場合のListインデックス
            if (!CollectionUtils.isEmpty(this.orderItemRevisionList)) {
                for (int i = 0; i < this.orderItemRevisionList.size(); i++) {
                    if (this.orderItemRevisionList.get(i).getOrderItemSeq().getValue()
                        == changeOrderCountParam.getOrderItemSeq()) {
                        existListIndex = i;
                        break;
                    }
                }
            }

            // 渡された注文商品が存在しない場合はエラー
            if (existListIndex < 0) {
                throw new DomainException("PROMOTION-ODER0006-E");
            }

            // 注文数量変更
            OrderItemRevision oldOrderItem = orderItemRevisionList.get(existListIndex);
            if (oldOrderItem.getOrderCount().getValue() != changeOrderCountParam.getOrderCount()) {

                OrderItemRevision newOrderItem =
                                new OrderItemRevision(oldOrderItem.getItemId(), oldOrderItem.getOrderItemSeq(),
                                                      new OrderCount(changeOrderCountParam.getOrderCount()),
                                                      oldOrderItem.getItemName(), oldOrderItem.getUnitTitle1(),
                                                      oldOrderItem.getUnitValue1(), oldOrderItem.getUnitTitle2(),
                                                      oldOrderItem.getUnitValue2(), oldOrderItem.getJanCode(),
                                                      oldOrderItem.getNoveltyGoodsType(), oldOrderItem.getOrderItemId()
                                );
                // 設定
                this.orderItemRevisionList.remove(existListIndex);
                this.orderItemRevisionList.add(existListIndex, newOrderItem);
            }
        }
    }

    /**
     * 改訂用注文商品削除
     *
     * @param orderItemSeq 注文商品連番
     */
    void deleteOrderItemRevision(OrderItemSeq orderItemSeq) {

        AssertChecker.assertNotEmpty("orderItemRevisionList is Empty", orderItemRevisionList);

        for (OrderItemRevision orderItemRevision : orderItemRevisionList) {
            if (orderItemRevision.getOrderItemSeq().getValue() == orderItemSeq.getValue()) {
                this.orderItemRevisionList.remove(orderItemRevision);
                break;
            }
        }
    }

    /**
     * 改訂用注文商品差し替え
     *
     * @param orderItemRevision 改訂用注文商品
     */
    void replaceOrderItemRevision(OrderItemRevision orderItemRevision) {

        // 設定
        // 注文商品連番に一致する注文商品を差し替える
        for (int i = 0; i < this.orderItemRevisionList.size(); i++) {
            if (orderItemRevisionList.get(i).getOrderItemSeq().getValue() == orderItemRevision.getOrderItemSeq()
                                                                                              .getValue()) {
                this.orderItemRevisionList.remove(i);
                this.orderItemRevisionList.add(i, orderItemRevision);
                break;
            }
        }
    }

    /**
     * 改訂用注文票取消
     */
    public void cancelOrderSlipRevision() {

        // チェック
        // 確定状態でないならエラー
        if (this.orderStatus != OrderStatus.OPEN) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }

        // 注文商品の数量を0に変更
        List<OrderItemRevision> targetList = new ArrayList<>();
        for (OrderItemRevision orderItemRevision : this.orderItemRevisionList) {
            targetList.add(new OrderItemRevision(orderItemRevision.getItemId(), orderItemRevision.getOrderItemSeq(),
                                                 new OrderCount(0), orderItemRevision.getItemName(),
                                                 orderItemRevision.getUnitTitle1(), orderItemRevision.getUnitValue1(),
                                                 orderItemRevision.getUnitTitle2(), orderItemRevision.getUnitValue2(),
                                                 orderItemRevision.getJanCode(),
                                                 orderItemRevision.getNoveltyGoodsType(),
                                                 orderItemRevision.getOrderItemId()
            ));
        }
        this.orderItemRevisionList = targetList;

        // 注文票ステータス取消
        this.orderStatus = OrderStatus.CANCEL;
    }

    /**
     * コンストラクタ
     * 改訂用注文票発行
     *
     * @param orderSlipRevisionId
     * @param orderItemRevisionList
     * @param transactionRevisionId
     * @param orderSlipId
     * @param orderStatus
     * @param orderItemList
     * @param customerId
     * @param transactionId
     * @param registDate
     */
    public OrderSlipForRevisionEntity(String orderSlipRevisionId,
                                      List<OrderItemRevision> orderItemRevisionList,
                                      String transactionRevisionId,
                                      String orderSlipId,
                                      String orderStatus,
                                      List<OrderItem> orderItemList,
                                      String customerId,
                                      String transactionId,
                                      Date registDate) {

        this.orderSlipRevisionId = new OrderSlipRevisionId(orderSlipRevisionId);
        this.orderItemRevisionList = orderItemRevisionList;
        this.transactionRevisionId = transactionRevisionId;

        this.orderSlipId = new OrderSlipId(orderSlipId);
        this.orderStatus = OrderStatus.valueOf(orderStatus);
        this.orderItemList = orderItemList;
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.registDate = registDate;
    }

}