/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityFactory;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.exception.ValidateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 注文商品追加ユースケース
 */
@Service
public class AddOrderItemUseCase {

    /** 注文票ドメインサービス */
    private final OrderSlipEntityService orderSlipEntityService;

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public AddOrderItemUseCase(OrderSlipEntityService orderSlipEntityService,
                               IOrderSlipRepository orderSlipRepository) {
        this.orderSlipEntityService = orderSlipEntityService;
        this.orderSlipRepository = orderSlipRepository;
    }

    /**
     * 注文商品を追加する
     *
     * @param customerId       顧客ID
     * @param itemId           商品ID
     * @param orderCount       注文数
     * @param customerBirthday 顧客生年月日
     */
    public void addOrderItem(String customerId, String itemId, Integer orderCount, Date customerBirthday) {

        // 顧客IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);

        ValidateException validateException = new ValidateException();
        // 下書き注文票が存在するか判断
        if (orderSlipEntity == null) {

            // 顧客IDで下書き注文票を取得する
            OrderSlipEntity draftOrderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);

            // 下書き注文票が存在しない場合、新規発行する
            // 注文票発行
            OrderSlipEntity newOrderSlipEntity = OrderSlipEntityFactory.orderSlipEntity(draftOrderSlipEntity, customerId);

            // 注文商品追加

            if (orderCount == 0) {
                validateException.addMessage("orderCount", "VALIDATE-REQUIRED-SELECT", new String[] {"追加数量"});
            }
            if (!StringUtils.isNumeric(itemId)) {
                validateException.addMessage("itemId", "VALIDATE-REQUIRED-NUMERIC", null);
            }
            if (validateException.hasMessage()) {
                throw validateException;
            }

            newOrderSlipEntity.addOrderItem(itemId, new OrderCount(orderCount));

            // 注文商品をチェック
            this.orderSlipEntityService.checkOrderItem(newOrderSlipEntity.getOrderItemList(), customerBirthday);

            // 注文票を登録する
            this.orderSlipRepository.save(newOrderSlipEntity);

        } else {

            // 下書き注文票が存在する場合
            // 注文商品追加
            if (orderCount == 0) {
                validateException.addMessage("orderCount", "VALIDATE-REQUIRED-SELECT", new String[] {"追加数量"});
            }
            if (!StringUtils.isNumeric(itemId)) {
                validateException.addMessage("itemId", "VALIDATE-REQUIRED-NUMERIC", null);
            }
            if (validateException.hasMessage()) {
                throw validateException;
            }

            OrderItem newOrderItem = orderSlipEntity.addOrderItem(itemId, new OrderCount(orderCount));

            // 妥当性チェック用に、今回追加した注文商品オブジェクトだけのリストを作成する
            List<OrderItem> itemNeedCheckList = new ArrayList<>();
            itemNeedCheckList.add(newOrderItem);

            // 注文商品をチェック
            this.orderSlipEntityService.checkOrderItem(itemNeedCheckList, customerBirthday);

            // 注文票を更新する
            this.orderSlipRepository.update(orderSlipEntity);
        }

    }

}