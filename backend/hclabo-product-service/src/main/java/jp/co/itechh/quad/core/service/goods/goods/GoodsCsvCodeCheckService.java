/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods;

/**
 * 商品CSVアップロードコードチェック
 * <pre>
 * http://vcs.ihh.jp/trac/hitmall3/ticket/2913
 * </pre>
 *
 * @author tezuka-mk 2012/11/20
 */
public interface GoodsCsvCodeCheckService {

    /**
     * 初期化する
     * <pre>
     * チェック開始前に必ず呼び出してください
     * チェック済みの商品管理番号と商品番号を初期化します
     * </pre>
     */
    void init();

    /**
     * 引数の商品管理番号が登録可能か検証する
     * <pre>
     * assertTrue(GoodsCsvCodeCheckService.canSaveGoodsGroupCode("GG01"));
     * assertTrue(GoodsCsvCodeCheckService.canSaveGoodsGroupCode("GG01"));
     * assertTrue(GoodsCsvCodeCheckService.canSaveGoodsGroupCode("GG02"));
     * assertFalse(GoodsCsvCodeCheckService.canSaveGoodsGroupCode("GG01"));
     * </pre>
     *
     * @param code 商品管理番号
     * @return 直前のコードと異なる and 2回目以上出現 の場合はfalse
     */
    boolean canSaveGoodsGroupCode(String code);

    /**
     * 引数の商品番号が登録可能か検証する
     * <pre>
     * assertTrue(GoodsCsvCodeCheckService.canSaveGoodsCode("GG0101"));
     * assertFalse(GoodsCsvCodeCheckService.canSaveGoodsCode("GG0101"));
     * </pre>
     * <pre>
     * assertTrue(GoodsCsvCodeCheckService.canSaveGoodsCode("GG0101"));
     * assertTrue(GoodsCsvCodeCheckService.canSaveGoodsCode("GG0102"));
     * assertFalse(GoodsCsvCodeCheckService.canSaveGoodsCode("GG0101"));
     * </pre>
     *
     * @param code 商品番号
     * @return 重複の場合はfalse
     */
    boolean canSaveGoodsCode(String code);

    /**
     * 引数の規格値が登録可能か検証する
     * <pre>
     * テストコード有り。{@link GoodsCsvCodeCheckServiceImplTest}
     * </pre>
     *
     * @param unitValue1 規格1
     * @param unitValue2 規格2
     * @param breakKey   ブレイクキー（商品管理番号）
     * @return 規格値が重複している場合はfalse
     */
    boolean canSaveUnitvalue(String unitValue1, String unitValue2, String breakKey);
}
