/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 販売伝票確定 ユースケース
 */
@Service
public class OpenSalesSlipUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 受注アダプター */
    private final IOrderReceivedAdapter orderReceivedAdapter;

    /**
     * コンストラクタ
     */
    @Autowired
    public OpenSalesSlipUseCase(ISalesSlipRepository salesSlipRepository, IOrderReceivedAdapter orderReceivedAdapter) {
        this.salesSlipRepository = salesSlipRepository;
        this.orderReceivedAdapter = orderReceivedAdapter;
    }

    /**
     * 販売伝票を確定する
     *
     * @param transactionId
     */
    public void openSalesSlip(String transactionId) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);
        // 販売伝票が存在しない場合はエラー
        if (salesSlipEntity == null) {
            throw new DomainException("PRICE-PLANNING-SASE0001-E", new String[] {transactionId});
        }

        // 取引IDに紐づく受注番号を取得する
        OrderReceived orderReceived = orderReceivedAdapter.get(transactionId);
        // 受注情報が存在しない場合はエラー
        if (orderReceived == null) {
            throw new DomainException("PRICE-PLANNING-ORDS0001-E", new String[] {transactionId});
        }

        // 販売伝票を確定する
        salesSlipEntity.openSlip(orderReceived.getOrderCode(), new Date());

        // 商品リストを個数単位で分割し、数量をすべて1にする
        salesSlipEntity.itemListDivision();

        // 販売伝票を更新する
        salesSlipRepository.update(salesSlipEntity);
    }

}