/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 取引開始ユースケース
 */
@Service
public class StartTransactionUseCase {

    /** 注文票ドメインサービス */
    private final OrderSlipEntityService orderSlipEntityService;

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public StartTransactionUseCase(OrderSlipEntityService orderSlipEntityService,
                                   IOrderSlipRepository orderSlipRepository,
                                   IProductAdapter productAdapter,
                                   ConversionUtility conversionUtility) {
        this.orderSlipEntityService = orderSlipEntityService;
        this.orderSlipRepository = orderSlipRepository;
        this.productAdapter = productAdapter;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 取引を開始する
     *
     * @param customerId       顧客ID
     * @param transactionId    取引ID
     * @param customerBirthday 顧客生年月日
     * @return orderSlipEntity 注文票
     */
    public OrderSlipEntity startTransaction(String customerId, String transactionId, Date customerBirthday) {

        // 顧客IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);

        // 下書き注文票が取得できない場合はエラー
        if (orderSlipEntity == null) {
            throw new DomainException("PROMOTION-ODER0011-E", new String[] {customerId});
        }

        // 注文商品をチェック
        this.orderSlipEntityService.checkOrderItem(orderSlipEntity.getOrderItemList(), customerBirthday);

        // 注文商品リストから商品IDリストを作成
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < orderSlipEntity.getOrderItemList().size(); i++) {
            idList.add(orderSlipEntity.getOrderItemList().get(i).getItemId());
        }

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> goodsDtoList = getGoodsDetailsDtoList(idList);

        // 商品名称系マスタを設定する
        for (String id : idList) {
            GoodsDetailsDto dto = goodsDtoList.stream()
                                              .filter(target -> id.equals(target.getGoodsSeq().toString()))
                                              .findFirst()
                                              .orElse(new GoodsDetailsDto());
            orderSlipEntity.settingItemInfo(id, dto.getGoodsGroupName(), dto.getUnitTitle1(), dto.getUnitValue1(),
                                            dto.getUnitTitle2(), dto.getUnitValue2(), dto.getJanCode(),
                                            dto.getNoveltyGoodsType()
                                           );
        }

        // 取引IDを設定する
        orderSlipEntity.settingTransactionId(transactionId);

        // 注文票を更新する
        this.orderSlipRepository.update(orderSlipEntity);

        return orderSlipEntity;
    }

    /**
     * 注文商品リストをもとに、商品サービスから商品詳細リストを取得する
     *
     * @param idList 注文商品IDリスト
     * @return dtoList 商品詳細リスト
     */
    private List<GoodsDetailsDto> getGoodsDetailsDtoList(List<String> idList) {

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> dtoList = this.productAdapter.getDetails(idList);

        // 商品詳細リストが取得できない場合はエラー
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new DomainException("PROMOTION-ODER0013-E");
        }

        // 商品ID（goodsSeq）の存在チェックフラグ
        boolean existFlag = false;

        // 注文商品のIDリストと取得した商品詳細リストの整合性をチェック
        if (idList.size() == dtoList.size()) {

            for (String target : idList) {

                // フラグ初期化
                existFlag = false;

                // 対象注文商品の商品IDが、取得した商品詳細リストに存在するかチェック
                for (GoodsDetailsDto dto : dtoList) {
                    // 存在する場合
                    if (dto.getGoodsSeq() != null && dto.getGoodsSeq()
                                                        .equals(this.conversionUtility.toInteger(target))) {
                        existFlag = true;
                    }
                }
                // 該当する商品ID（goodsSeq）が存在しなかった場合
                if (!existFlag) {
                    throw new DomainException("PROMOTION-ODER0010-E");
                }
            }

        } else {
            throw new DomainException("PROMOTION-ODER0010-E");
        }

        return dtoList;
    }

}