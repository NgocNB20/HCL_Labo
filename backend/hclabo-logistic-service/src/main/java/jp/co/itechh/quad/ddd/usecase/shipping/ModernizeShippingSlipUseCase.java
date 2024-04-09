/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.ddd.domain.method.proxy.ShippingMethodProxyService;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingCount;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送伝票最新化ユースケース
 */
@Service
public class ModernizeShippingSlipUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 配送方法プロキシサービス */
    private final ShippingMethodProxyService shippingMethodProxyService;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public ModernizeShippingSlipUseCase(IShippingSlipRepository shippingSlipRepository,
                                        ShippingMethodProxyService shippingMethodProxyService,
                                        IProductAdapter productAdapter,
                                        ConversionUtility conversionUtility) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.shippingMethodProxyService = shippingMethodProxyService;
        this.productAdapter = productAdapter;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 配送伝票を最新化する
     *
     * @param transactionId 取引ID
     */
    public void modernizeShippingSlip(String transactionId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = shippingSlipRepository.getByTransactionId(transactionId);
        // 配送伝票が存在しない場合は処理をスキップする
        if (ObjectUtils.isEmpty(shippingSlipEntity)) {
            return;
        }
        // 配送方法が未設定の場合は処理をスキップする
        if (StringUtils.isEmpty(shippingSlipEntity.getShippingMethodId())) {
            return;
        }

        // 配送方法詳細取得
        DeliveryMethodDetailsDto deliveryMethodDto = shippingMethodProxyService.getByDeliveryMethodSeq(
                        conversionUtility.toInteger(shippingSlipEntity.getShippingMethodId()));
        // 最新化配送方法名を生成
        String modernizedShippingMethodName = (ObjectUtils.isNotEmpty(deliveryMethodDto) && ObjectUtils.isNotEmpty(
                        deliveryMethodDto.getDeliveryMethodEntity())) ?
                        deliveryMethodDto.getDeliveryMethodEntity().getDeliveryMethodName() :
                        shippingSlipEntity.getShippingMethodName();

        // 配送伝票に配送商品が設定されている場合、最新商品マスタで最新化された配送商品を生成
        List<SecuredShippingItem> modernizedShippingItemList = null;
        if (CollectionUtils.isNotEmpty(shippingSlipEntity.getSecuredShippingItemList())) {
            modernizedShippingItemList = modernizeShippingItemList(shippingSlipEntity.getSecuredShippingItemList());
        } else {
            modernizedShippingItemList = shippingSlipEntity.getSecuredShippingItemList();
        }

        // 配送伝票のトランザクションデータを最新化
        shippingSlipEntity.modernizeShippingSlipTranData(modernizedShippingMethodName, modernizedShippingItemList);

        // 配送伝票更新
        shippingSlipRepository.update(shippingSlipEntity);
    }

    /**
     * 配送商品リストを最新化<br/>
     * 伝票に登録されている配送商品リストから商品サービスを検索し、配送商品リストを最新化する
     *
     * @param shippingItemList 配送商品リスト
     * @return modernizedShippingItemList 最新化配送商品リスト
     */
    private List<SecuredShippingItem> modernizeShippingItemList(List<SecuredShippingItem> shippingItemList) {

        // 戻り値生成
        List<SecuredShippingItem> modernizedShippingItemList = new ArrayList<>();

        // 配送商品リストから商品IDリストを作成
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < shippingItemList.size(); i++) {
            idList.add(shippingItemList.get(i).getItemId());
        }

        // 商品サービスから商品詳細リストを取得
        List<GoodsDetailsDto> dtoList = productAdapter.getDetails(idList);

        // 配送商品リストの並び順でループして、取得した最新商品情報を設定し、配送商品リストを作成
        for (SecuredShippingItem shippingItem : shippingItemList) {

            // 商品詳細リストから該当商品を取得
            GoodsDetailsDto targetGoodsDto = dtoList.stream()
                                                    .filter(goodsDto -> goodsDto.getGoodsSeq() != null
                                                                        && goodsDto.getGoodsSeq()
                                                                                   .equals(conversionUtility.toInteger(
                                                                                                   shippingItem.getItemId())))
                                                    .findFirst()
                                                    .orElse(null);

            if (ObjectUtils.isEmpty(targetGoodsDto)) {
                // 注文フロー内で商品マスタが更新され、該当商品マスタが取得できなかった場合は妥当性チェック側でエラーとし、元々の配送商品をそのまま設定する
                modernizedShippingItemList.add(shippingItem);
            } else {
                SecuredShippingItem modernizeShippingItem =
                                new SecuredShippingItem(shippingItem.getShippingItemSeq(), shippingItem.getItemId(),
                                                        targetGoodsDto.getGoodsGroupName(),
                                                        targetGoodsDto.getUnitTitle1(), targetGoodsDto.getUnitValue1(),
                                                        targetGoodsDto.getUnitTitle2(), targetGoodsDto.getUnitValue2(),
                                                        new ShippingCount(shippingItem.getShippingCount().getValue())
                                );
                modernizedShippingItemList.add(modernizeShippingItem);
            }
        }
        return modernizedShippingItemList;
    }

}