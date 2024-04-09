/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistListGetService;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.infrastructure.product.adapter.ProductAdapterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * お気に入り情報リスト取得サービス
 *
 * @author ueshima
 *
 */
@Service
public class WishlistListGetServiceImpl extends AbstractShopService implements WishlistListGetService {

    /** お気に入り情報リスト取得ロジック **/
    private final WishlistListGetLogic wishlistListGetLogic;

    /**
     * 商品アダプター
     */
    private final IProductAdapter productAdapter;

    @Autowired
    public WishlistListGetServiceImpl(WishlistListGetLogic wishlistListGetLogic, ProductAdapterImpl productAdapter) {
        this.wishlistListGetLogic = wishlistListGetLogic;
        this.productAdapter = productAdapter;
    }

    /**
     * お気に入り情報リスト取得処理<br/>
     *
     * ログイン会員のお気に入り情報を取得する。<br/>
     *
     * @param siteType サイト種別
     * @param wishlistConditionDto お気に入り検索条件Dto
     * @return お気に入りDTOリスト
     */
    @Override
    public List<WishlistDto> execute(HTypeSiteType siteType, WishlistSearchForDaoConditionDto wishlistConditionDto) {
        // 共通情報の取得
        Integer shopSeq = 1001;

        // パラメタチェック
        ArgumentCheckUtil.assertNotNull("wishlistConditionDto", wishlistConditionDto);
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("siteType", siteType);

        // ロジックパラメタの作成
        wishlistConditionDto.setShopSeq(shopSeq);
        wishlistConditionDto.setSiteType(siteType);

        // お気に入り情報の検索
        List<WishlistEntity> wishlistEntityList = wishlistListGetLogic.execute(wishlistConditionDto);
        if (wishlistEntityList == null || wishlistEntityList.isEmpty()) {
            // データ不在の場合は空リストを戻す
            return new ArrayList<>(0);
        }

        // 商品SEQリストの作成
        List<String> goodsSeqList = new ArrayList<>(wishlistEntityList.size());
        for (WishlistEntity wishlistEntity : wishlistEntityList) {
            goodsSeqList.add(wishlistEntity.getGoodsSeq().toString());
        }

        // 商品詳細マップ取得
        List<GoodsDetailsDto> goodsDtoMap = productAdapter.getDetails(goodsSeqList);

        // 商品アイコンマップ取得
        Map<Integer, List<GoodsInformationIconDetailsDto>> goodsIconMap = new HashMap<>();

        // 商品グループSEQリストの作成
        List<Integer> goodsGroupSeqList = new ArrayList<>(wishlistEntityList.size());
        if (CollectionUtil.isNotEmpty(goodsDtoMap)) {
            for (GoodsDetailsDto goodsDetailsDto : goodsDtoMap) {
                Integer goodsSeq = goodsDetailsDto.getGoodsSeq();
                if (!goodsGroupSeqList.contains(goodsSeq)) {
                    goodsGroupSeqList.add(goodsDetailsDto.getGoodsGroupSeq());
                }
                goodsIconMap.put(goodsSeq, goodsDetailsDto.getGoodsIconList());
            }
        }

        // お気に入りDtoリスト作成
        return getWishlistDtoList(wishlistEntityList, goodsDtoMap, goodsIconMap);
    }

    /**
     * お気に入りDto作成
     *
     * @param wishlistEntityList お気にリエンティティ
     * @param goodsDtoMap 商品詳細DtoMap
     * @param goodsIconMap 商品インフォメーションアイコンMap
     * @return お気に入りDtoリスト
     */
    protected List<WishlistDto> getWishlistDtoList(List<WishlistEntity> wishlistEntityList,
                                                   List<GoodsDetailsDto> goodsDtoMap,
                                                   Map<Integer, List<GoodsInformationIconDetailsDto>> goodsIconMap) {
        // お気に入りDTOリストの作成
        List<WishlistDto> wishlistDtoList = new ArrayList<>(wishlistEntityList.size());

        if (CollectionUtil.isEmpty(wishlistEntityList)) {
            return new ArrayList<>();
        }

        for (WishlistEntity wishlistEntity : wishlistEntityList) {
            WishlistDto wishlistDto = getComponent(WishlistDto.class);
            wishlistDto.setWishlistEntity(wishlistEntity);
            for (GoodsDetailsDto goodsDetailsDto : goodsDtoMap) {
                if (wishlistEntity.getGoodsSeq() != null && wishlistEntity.getGoodsSeq()
                                                                          .equals(goodsDetailsDto.getGoodsSeq())) {
                    wishlistDto.setGoodsDetailsDto(goodsDetailsDto);
                    wishlistDto.setGoodsGroupImageEntityList(goodsDetailsDto.getGoodsGroupImageEntityList());
                    wishlistDto.setGoodsInformationIconDetailsDtoList(goodsIconMap.get(goodsDetailsDto.getGoodsSeq()));
                }
            }
            wishlistDtoList.add(wishlistDto);
        }
        return wishlistDtoList;
    }
}