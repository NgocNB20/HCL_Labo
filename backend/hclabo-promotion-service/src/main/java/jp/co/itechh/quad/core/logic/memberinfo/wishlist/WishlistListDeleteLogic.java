/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist;

/**
 * お気に入り情報リスト削除ロジック
 *
 * @author ueshima
 * @version $Revision: 1.3 $
 */
public interface WishlistListDeleteLogic {

    /**
     * お気に入り情報リスト削除処理
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsSeqs 商品SEQ配列
     * @return 削除件数
     */
    int execute(Integer memberInfoSeq, Integer[] goodsSeqs);

    /**
     * お気に入り情報リスト削除処理
     *
     * @param memberInfoSeq 会員SEQ
     * @return 削除件数
     */
    int execute(Integer memberInfoSeq);

    /**
     * お気に入り情報リスト削除処理
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsCodes 商品コード配列
     * @return 削除件数
     */
    int execute(Integer memberInfoSeq, String[] goodsCodes);

}
