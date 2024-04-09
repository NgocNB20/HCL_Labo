/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistRegistLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistRegistService;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * お気に入り情報登録サービス
 * @author ueshima
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class WishlistRegistServiceImpl extends AbstractShopService implements WishlistRegistService {

    /**
     * お気に入りデータチェックロジック
     */
    private final WishlistDataCheckLogic wishlistDataCheckLogic;

    /**
     * お気に入り情報更新ロジック
     */
    private final WishlistUpdateLogic wishlistUpdateLogic;

    /**
     * お気に入り情報登録ロジック
     */
    private final WishlistRegistLogic wishlistRegistLogic;

    private final IProductAdapter productAdapter;

    @Autowired
    public WishlistRegistServiceImpl(WishlistDataCheckLogic wishlistDataCheckLogic,
                                     WishlistUpdateLogic wishlistUpdateLogic,
                                     WishlistRegistLogic wishlistRegistLogic,
                                     IProductAdapter productAdapter) {
        this.wishlistDataCheckLogic = wishlistDataCheckLogic;
        this.wishlistUpdateLogic = wishlistUpdateLogic;
        this.wishlistRegistLogic = wishlistRegistLogic;
        this.productAdapter = productAdapter;
    }

    /**
     * サービス実行
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsCode     商品コード
     * @return 登録件数
     */
    @Override
    public int execute(Integer memberInfoSeq, String goodsCode) {
        // 共通情報の取得
        Integer shopSeq = 1001;
        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotNull("goodsCode", goodsCode);
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);

        // 商品検索
        // 公開状態＝公開
        GoodsDetailsDto goodsDetailsDto = productAdapter.getDetailsByGoodCode(goodsCode, HTypeOpenDeleteStatus.OPEN);

        if (ObjectUtils.isEmpty(goodsDetailsDto)) {
            // 商品不在エラー
            throwMessage(MSGCD_GOODS_NOT_EXIST);
        }

        // エンティティの作成
        WishlistEntity wishlistEntity = getComponent(WishlistEntity.class);
        wishlistEntity.setMemberInfoSeq(memberInfoSeq);
        wishlistEntity.setGoodsSeq(goodsDetailsDto.getGoodsSeq());

        // 既存データ確認
        wishlistDataCheckLogic.execute(wishlistEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // お気に入り更新処理
        wishlistEntity.setRegistTime(dateUtility.getCurrentTime());
        int result = wishlistUpdateLogic.execute(wishlistEntity);
        if (result == 0) {

            // お気に入り登録処理
            result = wishlistRegistLogic.execute(wishlistEntity);

            // 両方失敗の場合
            if (result == 0) {
                throwMessage(MSGCD_WISHLIST_GOODS_REGIST_FAIL);
            }
        }
        return result;
    }

    /**
     * サービス実行<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param siteType サイト種別
     * @param goodsCode 商品コード
     * @return 登録件数
     */
    @Override
    public WishlistEntity execute(Integer memberInfoSeq, HTypeSiteType siteType, String goodsCode) {

        // 共通情報の取得
        Integer shopSeq = 1001;

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotNull("goodsCode", goodsCode);
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("siteType", siteType);

        // 商品検索
        // 公開状態＝公開
        GoodsDetailsDto goodsDetailsDto = productAdapter.getDetailsByGoodCode(goodsCode, HTypeOpenDeleteStatus.OPEN);

        if (ObjectUtils.isEmpty(goodsDetailsDto)) {
            // 商品不在エラー
            throwMessage(MSGCD_GOODS_NOT_EXIST);
        }

        // エンティティの作成
        WishlistEntity wishlistEntity = getComponent(WishlistEntity.class);
        wishlistEntity.setMemberInfoSeq(memberInfoSeq);
        wishlistEntity.setGoodsSeq(goodsDetailsDto.getGoodsSeq());

        // 既存データ確認
        wishlistDataCheckLogic.execute(wishlistEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // お気に入り更新処理
        wishlistEntity.setRegistTime(dateUtility.getCurrentTime());
        int result = wishlistUpdateLogic.execute(wishlistEntity);
        if (result == 0) {

            // お気に入り登録処理
            result = wishlistRegistLogic.execute(wishlistEntity);

            // 両方失敗の場合
            if (result == 0) {
                throwMessage(MSGCD_WISHLIST_GOODS_REGIST_FAIL);
            }
        }
        return wishlistEntity;
    }
}