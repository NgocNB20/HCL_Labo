/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistListDeleteLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.wishlist.WishlistListDeleteService;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * お気に入り情報リスト削除サービス<br/>
 * @author ueshima
 * @version $Revision: 1.4 $
 */
@Service
public class WishlistListDeleteServiceImpl extends AbstractShopService implements WishlistListDeleteService {

    /**
     * お気に入り情報リスト削除ロジック
     */
    private final WishlistListDeleteLogic wishlistListDeleteLogic;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    @Autowired
    public WishlistListDeleteServiceImpl(WishlistListDeleteLogic wishlistListDeleteLogic,
                                         IProductAdapter productAdapter) {
        this.wishlistListDeleteLogic = wishlistListDeleteLogic;
        this.productAdapter = productAdapter;
    }

    /**
     * サービス実行<br/>
     *
     * @param goodsCode 商品コード
     * @return 削除件数
     */
    @Override
    public int execute(Integer memberInfoSeq, String goodsCode) {
        // 引数チェック
        ArgumentCheckUtil.assertNotNull("goodsCode", goodsCode);
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        String[] goodsCodes = new String[1];
        goodsCodes[0] = goodsCode;

        GoodsDetailsDto goodsDetailsDto = productAdapter.getDetailsByGoodCode(goodsCode, HTypeOpenDeleteStatus.OPEN);
        Integer[] goodsSeqs = new Integer[1];
        goodsSeqs[0] = goodsDetailsDto.getGoodsSeq();

        return wishlistListDeleteLogic.execute(memberInfoSeq, goodsSeqs);
    }

    /**
     * サービス実行<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsCodes 商品コード配列
     * @return 削除件数
     */
    public int execute(Integer memberInfoSeq, String[] goodsCodes) {
        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotEmpty("goodsCodes", goodsCodes);
        return wishlistListDeleteLogic.execute(memberInfoSeq, goodsCodes);
    }

    /**
     * お気に入り削除処理<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @return 削除件数
     */
    public int execute(Integer memberInfoSeq) {
        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        return wishlistListDeleteLogic.execute(memberInfoSeq);
    }
}