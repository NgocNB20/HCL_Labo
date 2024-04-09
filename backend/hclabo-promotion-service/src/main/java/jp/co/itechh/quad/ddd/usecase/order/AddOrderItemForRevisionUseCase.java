/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderCount;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItem;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemId;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemRevision;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeq;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderItemSeqFactory;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ValidateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 改訂用注文票注文商品追加ユースケース
 */
@Service
public class AddOrderItemForRevisionUseCase {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** コンストラクタ */
    @Autowired
    public AddOrderItemForRevisionUseCase(IOrderSlipForRevisionRepository orderSlipForRevisionRepository,
                                          IProductAdapter productAdapter) {
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
        this.productAdapter = productAdapter;
    }

    /**
     * TODO: 在庫チェックがいりそうな気がしている __要確認（一旦後回し）__
     * 改訂用注文票に注文商品を追加する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param itemId                商品ID
     * @param orderCount            注文数
     */
    public void addOrderItemForRevision(String transactionRevisionId, String itemId, Integer orderCount) {

        // バリデート例外
        ValidateException validateException = new ValidateException();

        // 改訂用取引IDで改訂用注文票を取得する
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        orderSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        // 改訂用注文票が取得できない場合はエラー
        if (orderSlipForRevisionEntity == null) {
            throw new DomainException("PROMOTION-ODER0007-E", new String[] {transactionRevisionId});
        }

        // 商品サービスから商品詳細を取得
        if (!StringUtils.isNumeric(itemId)) {
            validateException.addMessage("itemId", "VALIDATE-REQUIRED-NUMERIC", null);
        }
        if (validateException.hasMessage()) {
            throw validateException;
        }
        GoodsDetailsDto goodsDto = getGoodsDetailsDto(itemId);

        // 改訂用注文商品追加
        if (orderCount == 0) {
            validateException.addMessage("orderCount", "VALIDATE-REQUIRED-SELECT", new String[] {"追加数量"});
        }
        if (validateException.hasMessage()) {
            throw validateException;
        }
        OrderItemSeq orderItemSeq = OrderItemSeqFactory.constructOrderItemSeq(
                        (List<OrderItem>) (Object) orderSlipForRevisionEntity.getOrderItemRevisionList());
        orderSlipForRevisionEntity.addOrderItemRevision(
                        new OrderItemRevision(itemId, orderItemSeq, new OrderCount(orderCount),
                                              goodsDto.getGoodsGroupName(), goodsDto.getUnitTitle1(),
                                              goodsDto.getUnitValue1(), goodsDto.getUnitTitle2(),
                                              goodsDto.getUnitValue2(), goodsDto.getJanCode(),
                                              goodsDto.getNoveltyGoodsType(),
                                              new OrderItemId()
                        ));

        // 改訂用注文票を更新する
        orderSlipForRevisionRepository.update(orderSlipForRevisionEntity);
    }

    /**
     * 注文商品リストをもとに、商品サービスから商品詳細Dtoを取得する<br/>
     *
     * @param itemId 注文商品ID
     * @return GoodsDetailsDto 商品詳細Dto
     */
    private GoodsDetailsDto getGoodsDetailsDto(String itemId) {

        // 注文商品リストから商品IDリストを作成
        List<String> idList = new ArrayList<>();
        idList.add(itemId);

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> dtoList = this.productAdapter.getDetails(idList);

        // 商品詳細リストが取得できない場合はエラー
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new DomainException("PROMOTION-ODER0013-E");
        }

        return dtoList.get(0);
    }

}