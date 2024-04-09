/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.wishlist;

/**
 * お気に入り情報リスト削除サービス<br/>
 *
 * @author ueshima
 * @version $Revision: 1.3 $
 */
public interface WishlistListDeleteService {

    /**
     * サービス実行<br/>
     *
     * ログイン会員のお気に入り情報を削除する。<br/>
     *
     * @param goodsCode 商品コード
     * @return 削除件数
     */
    int execute(Integer memberInfoSeq, String goodsCode);

    /**
     * お気に入り削除処理<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @return 削除件数
     */
    int execute(Integer memberInfoSeq);
}

