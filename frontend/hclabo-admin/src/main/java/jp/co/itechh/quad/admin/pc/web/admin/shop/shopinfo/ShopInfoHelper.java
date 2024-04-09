/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.shopinfo;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAutoApprovalFlag;
import jp.co.itechh.quad.admin.entity.shop.ShopEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.shop.presentation.api.param.ShopResponse;
import jp.co.itechh.quad.shop.presentation.api.param.ShopUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopInfoHelper {

    /** 変換Helper取得 */
    public ConversionUtility conversionUtility;

    @Autowired
    public ShopInfoHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理時の画面反映
     *
     * @param shopInfoModel 店舗情報詳細画面
     * @param entity ショップエンティティ
     */
    public void toPageForLoadIndex(ShopInfoModel shopInfoModel, ShopEntity entity) {
        // ショップ情報のページクラス設定
        if (entity != null) {
            // 店舗設定
            setupShopSettings(shopInfoModel, entity);
            // メタ情報
            setupMetaInfo(shopInfoModel, entity);
        }
    }

    /**
     * 店舗設定を画面に反映する<br/>
     *
     * @param shopInfoModel ページ
     * @param entity ショップEntity
     */
    protected void setupShopSettings(ShopInfoModel shopInfoModel, ShopEntity entity) {
        // ショップSEQ
        shopInfoModel.setShopSeq(conversionUtility.toString(entity.getShopSeq()));
        // ショップ名PC
        shopInfoModel.setShopNamePC(entity.getShopNamePC());
        // 自動承認フラグ設定
        shopInfoModel.setAutoApprovalFlag(entity.getAutoApprovalFlag().getLabel());
    }

    /**
     * メタ情報を画面に反映する<br/>
     *
     * @param shopInfoModel ページ
     * @param entity ショップEntity
     */
    protected void setupMetaInfo(ShopInfoModel shopInfoModel, ShopEntity entity) {
        // メタ説明文
        shopInfoModel.setMetaDescription(entity.getMetaDescription());
        // メタキーワード
        shopInfoModel.setMetaKeyword(entity.getMetaKeyword());
    }

    /**
     * 初期処理時の画面反映
     *
     * @param shopInfoModel 店舗情報設定画面
     * @param shopEntity ショップエンティティ
     */
    public void toPageForLoadUpdate(ShopInfoModel shopInfoModel, ShopEntity shopEntity) {

        // ショップ情報のページクラス設定
        if (shopEntity != null) {

            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

            // ショップ情報を画面に反映
            if (shopEntity.getShopSeq() != null) {
                shopInfoModel.setShopSeq(shopEntity.getShopSeq().toString());
            }
            shopInfoModel.setShopNamePC(shopEntity.getShopNamePC());
            shopInfoModel.setMetaDescription(shopEntity.getMetaDescription());
            shopInfoModel.setMetaKeyword(shopEntity.getMetaKeyword());
        }
    }

    /**
     * ショップ情報エンティティを生成して画面にセット
     *
     * @param shopInfoModel 画面情報
     */
    public void toShopEntityForUpdate(ShopInfoModel shopInfoModel) {
        ShopEntity shopEntity = shopInfoModel.getShopEntity();

        shopEntity.setShopNamePC(shopInfoModel.getShopNamePC());
        shopEntity.setMetaDescription(shopInfoModel.getMetaDescription());
        shopEntity.setMetaKeyword(shopInfoModel.getMetaKeyword());

        return;
    }

    /**
     * ショップ情報リクエストに変換
     *
     * @param shopEntity ショップエンティティ
     * @return ショップ情報リクエスト
     */
    public ShopUpdateRequest toShopUpdateRequest(ShopEntity shopEntity) {

        ShopUpdateRequest shopUpdateRequest = new ShopUpdateRequest();

        shopUpdateRequest.setShopId(shopEntity.getShopId());
        shopUpdateRequest.setShopNamePC(shopEntity.getShopNamePC());
        shopUpdateRequest.setUrlPC(shopEntity.getUrlPC());
        shopUpdateRequest.setMetaDescription(shopEntity.getMetaDescription());
        shopUpdateRequest.setMetaKeyword(shopEntity.getMetaKeyword());
        shopUpdateRequest.setVersionNo(shopEntity.getVersionNo());
        shopUpdateRequest.setAutoApprovalFlag(EnumTypeUtil.getValue(shopEntity.getAutoApprovalFlag()));

        return shopUpdateRequest;
    }

    /**
     * ショップエンティティに変換
     *
     * @param shopResponse ショップ情報レスポンス
     * @return ショップエンティティ
     */
    public ShopEntity toShopEntity(ShopResponse shopResponse) {

        ShopEntity shopEntity = new ShopEntity();

        shopEntity.setShopSeq(1001);
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