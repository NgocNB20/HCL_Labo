/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.logic.common.impl;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfoShop;
import jp.co.itechh.quad.front.application.commoninfo.impl.CommonInfoShopImpl;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAutoApprovalFlag;
import jp.co.itechh.quad.front.entity.shop.ShopEntity;
import jp.co.itechh.quad.front.logic.common.CommonInfoShopCreateLogic;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.shop.presentation.api.ShopApi;
import jp.co.itechh.quad.shop.presentation.api.param.ShopResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * ショップ情報作成ロジック(共通情報)<br/>
 *
 * @author natume
 * @author sakai
 * @version $Revision: 1.3 $
 *
 */
@Component
public class CommonInfoShopCreateLogicImpl implements CommonInfoShopCreateLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonInfoShopCreateLogicImpl.class);

    /** ショップAPI */
    private final ShopApi shopApi;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param shopApi ショップAPI
     * @param conversionUtility 変換ユーティリティクラス
     */
    public CommonInfoShopCreateLogicImpl(ShopApi shopApi, ConversionUtility conversionUtility) {
        this.shopApi = shopApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * ショップ情報作成（共通情報）<br/>
     *
     * @param shopEntity ショップエンティティ
     * @return ショップ情報(共通情報)
     */
    protected CommonInfoShop createCommonInfoShop(ShopEntity shopEntity) {

        // ショップ情報作成
        CommonInfoShopImpl commonInfoShopImpl = new CommonInfoShopImpl();
        commonInfoShopImpl.setShopMetaDescription(shopEntity.getMetaDescription());
        commonInfoShopImpl.setShopMetaKeyword(shopEntity.getMetaKeyword());
        commonInfoShopImpl.setShopNamePC(shopEntity.getShopNamePC());
        commonInfoShopImpl.setUrlPC(shopEntity.getUrlPC());
        return commonInfoShopImpl;
    }

    /**
     * ショップ情報作成処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @return ショップ情報(共通情報)
     */
    @Override
    public CommonInfoShop execute(Integer shopSeq) {
        try {
            // ショップ情報取得
            ShopResponse shopResponse = shopApi.get();

            ShopEntity shopEntity = toShopEntity(shopResponse, shopSeq);

            CommonInfoShopImpl commonInfoShopImpl = (CommonInfoShopImpl) createCommonInfoShop(shopEntity);

            return commonInfoShopImpl;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            return null;
        }
    }

    /**
     * ショップエンティティに変換
     *
     * @param shopResponse ショップ情報レスポンス
     * @param shopSeq      ショップSEQ
     * @return ショップエンティティ
     */
    protected ShopEntity toShopEntity(ShopResponse shopResponse, Integer shopSeq) {
        ShopEntity shopEntity = new ShopEntity();

        shopEntity.setShopSeq(shopSeq);
        shopEntity.setShopId(shopResponse.getShopId());
        shopEntity.setShopNamePC(shopResponse.getShopNamePC());
        shopEntity.setUrlPC(shopResponse.getUrlPC());
        shopEntity.setMetaDescription(shopResponse.getMetaDescription());
        shopEntity.setMetaKeyword(shopResponse.getMetaKeyword());
        shopEntity.setVersionNo(shopResponse.getVersionNo());
        shopEntity.setAutoApprovalFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeAutoApprovalFlag.class, shopResponse.getAutoApprovalFlag()));

        return shopEntity;
    }

}