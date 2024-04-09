/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文票最新化ユースケース
 */
@Service
public class ModernizeOrderSlipUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public ModernizeOrderSlipUseCase(IOrderSlipRepository iOrderSlipRepository,
                                     IProductAdapter productAdapter,
                                     ConversionUtility conversionUtility) {
        this.orderSlipRepository = iOrderSlipRepository;
        this.productAdapter = productAdapter;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 注文票を最新化する
     *
     * @param transactionId 取引ID
     */
    public void modernizeOrderSlip(String transactionId) {

        // 取引IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = orderSlipRepository.getDraftOrderSlipByTransactionId(transactionId);

        // 注文票が存在しない場合は処理をスキップする
        if (ObjectUtils.isEmpty(orderSlipEntity)) {
            return;
        }
        // 注文票に注文商品が設定されている場合、最新商品マスタで最新化された注文商品を生成
        List<OrderItem> modernizedOrderItemList = null;
        if (CollectionUtils.isNotEmpty(orderSlipEntity.getOrderItemList())) {
            modernizedOrderItemList = modernizeOrderItemList(orderSlipEntity.getOrderItemList());
        } else {
            modernizedOrderItemList = orderSlipEntity.getOrderItemList();
        }

        // 注文票のトランザクションデータを最新化
        orderSlipEntity.modernizeOrderSlipTranData(modernizedOrderItemList);

        // 注文票を更新
        orderSlipRepository.update(orderSlipEntity);
    }

    /**
     * 注文商品リストを最新化<br/>
     * 伝票に登録されている注文商品リストから商品サービスを検索し、注文商品リストを最新化する
     *
     * @param orderItemList 注文商品リスト
     * @return modernizedOrderItemList 最新化注文商品リスト
     */
    private List<OrderItem> modernizeOrderItemList(List<OrderItem> orderItemList) {

        // 戻り値生成
        List<OrderItem> modernizedOrderItemList = new ArrayList<>();

        // 注文商品リストから商品IDリストを作成
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            idList.add(orderItemList.get(i).getItemId());
        }

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> dtoList = productAdapter.getDetails(idList);

        // 注文商品リストの並び順でループして、取得した最新商品情報を設定し、注文商品リストを作成
        for (OrderItem orderItem : orderItemList) {

            // 商品詳細リストから該当商品を取得
            GoodsDetailsDto targetGoodsDto = dtoList.stream()
                                                    .filter(goodsDto -> goodsDto.getGoodsSeq() != null
                                                                        && goodsDto.getGoodsSeq()
                                                                                   .equals(conversionUtility.toInteger(
                                                                                                   orderItem.getItemId())))
                                                    .findFirst()
                                                    .orElse(null);

            if (ObjectUtils.isEmpty(targetGoodsDto)) {
                // 注文フロー内で商品マスタが更新され、該当商品マスタが取得不可となった場合は妥当性チェック側でエラーとし、元々の注文商品をそのまま設定する
                modernizedOrderItemList.add(orderItem);
            } else {
                OrderItem modernizeOrderItem = new OrderItem(orderItem.getItemId(), orderItem.getOrderItemSeq(),
                                                             orderItem.getOrderCount(),
                                                             targetGoodsDto.getGoodsGroupName(),
                                                             targetGoodsDto.getUnitTitle1(),
                                                             targetGoodsDto.getUnitValue1(),
                                                             targetGoodsDto.getUnitTitle2(),
                                                             targetGoodsDto.getUnitValue2(),
                                                             targetGoodsDto.getJanCode(),
                                                             targetGoodsDto.getNoveltyGoodsType(),
                                                             orderItem.getOrderItemId()
                );
                modernizedOrderItemList.add(modernizeOrderItem);
            }
        }
        return modernizedOrderItemList;
    }

}