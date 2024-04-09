/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.wishlist;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;

/**
 * お気に入り情報登録サービス
 * @author ueshima
 * @version $Revision: 1.3 $
 */
public interface WishlistRegistService {

    /**
     * 商品不在エラー
     * <code>MSGCD_GOODS_NOT_EXIST</code>
     */
    String MSGCD_GOODS_NOT_EXIST = "SMF000101";

    /**
     * お気に入り商品登録エラー
     * <code>MSGCD_WISHLIST_GOODS_REGIST_FAIL</code>
     */
    String MSGCD_WISHLIST_GOODS_REGIST_FAIL = "SMF000102";

    /**
     * サービス実行
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsCode 商品コード
     * @return 登録件数
     */
    int execute(Integer memberInfoSeq, String goodsCode);

    /**
     * サービス実行<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param siteType サイト種別
     * @param goodsCode 商品コード
     * @return 登録件数
     */
    WishlistEntity execute(Integer memberInfoSeq, HTypeSiteType siteType, String goodsCode);

}
