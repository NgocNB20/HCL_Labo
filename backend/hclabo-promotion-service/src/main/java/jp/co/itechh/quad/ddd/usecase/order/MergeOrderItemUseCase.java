/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityFactory;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 下書き注文票の注文商品統合ユースケース
 */
@Service
public class MergeOrderItemUseCase {

    /** 注文票ドメインサービス */
    private final OrderSlipEntityService orderSlipEntityService;

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public MergeOrderItemUseCase(OrderSlipEntityService orderSlipEntityService,
                                 IOrderSlipRepository orderSlipRepository) {
        this.orderSlipEntityService = orderSlipEntityService;
        this.orderSlipRepository = orderSlipRepository;
    }

    /**
     * 下書き注文票の注文商品を統合する<br/>
     * ※ログイン時にゲストから会員へカートマージ<br/>
     * ※会員登録時にゲストから会員へカートマージ<br/>
     * ※会員退会時に会員からゲストへカートマージ
     * <p>
     * TODO 会員退会時は「統合先後顧客ID」で下書き伝票取得できた場合はエラーにしたいが、現状は同一UCで実現しているため対応していない
     *
     * @param customerIdFrom 統合元顧客ID
     * @param customerIdTo   統合先後顧客ID
     */
    public void mergeOrderItem(String customerIdFrom, String customerIdTo) {

        // 統合前後で同じ顧客IDの場合はマージ処理不要
        if (StringUtils.isNotBlank(customerIdFrom) && StringUtils.isNotBlank(customerIdTo) && customerIdFrom.equals(
                        customerIdTo)) {
            return;
        }

        // 顧客IDで統合「元」下書き注文票を取得する
        OrderSlipEntity orderSlipEntityFrom = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerIdFrom);

        // 下書き注文票が取得できなかった場合は、返却
        if (orderSlipEntityFrom == null || orderSlipEntityFrom.getOrderStatus() != OrderStatus.DRAFT) {
            return;
        }

        // 顧客IDで統合「先」下書き注文票を取得する
        OrderSlipEntity orderSlipEntityTo = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerIdTo);

        // 下書き状態でない場合はエラー
        if (orderSlipEntityTo != null && orderSlipEntityTo.getOrderStatus() != OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0012-E");
        }

        // 統合「先」注文票が存在しない場合は、新規発行する
        if (orderSlipEntityTo == null) {

            // 顧客IDで下書き注文票を取得する
            OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerIdTo);

            // 統合先伝票発行
            orderSlipEntityTo = OrderSlipEntityFactory.orderSlipEntity(orderSlipEntity, customerIdTo);

            // 統合先注文票を登録する
            this.orderSlipRepository.save(orderSlipEntityTo);
        }

        // 注文商品を統合する
        this.orderSlipEntityService.mergeOrderItem(orderSlipEntityFrom, orderSlipEntityTo);

        // 統合先注文票を更新する
        this.orderSlipRepository.update(orderSlipEntityTo);
    }

}