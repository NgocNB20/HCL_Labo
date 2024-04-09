/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.noveltypresent;

import java.util.List;

/**
 *
 *  ノベルティプレゼント登録更新用チェックサービス<br/>
 *
 * @author aoyama
 *
 */
public interface NoveltyPresentCheckService {

    /**
     * ノベルティ商品番号のチェックを行う<br/>
     *
     * ノベルティ商品番号の商品マスタが存在しない<br/>
     * or　公開状態=削除<br/>
     * or　販売状態=削除の場合はエラー<br/>
     *
     * @param shopSeq ショップSEQ
     * @param noveltyGoodsCodeList ノベルティ商品番号のList
     * @return エラーとなったノベルティ商品番号
     */
    List<String> checkNoveltyGoods(Integer shopSeq, List<String> noveltyGoodsCodeList);

    /**
     * 商品管理番号の存在チェックを行う<br/>
     *
     * ※公開状態、販売状態は判定しない
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCodeList 商品管理番号のList
     * @return True:OK、False:NG
     */
    List<String> checkGoodsGroupCode(Integer shopSeq, List<String> goodsGroupCodeList);

    /**
     * 商品番号の存在チェックを行う<br/>
     *
     * ※公開状態、販売状態は判定しない
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品番号のList
     * @return True:OK、False:NG
     */
    List<String> checkGoodsCode(Integer shopSeq, List<String> goodsCodeList);

    /**
     * カテゴリＩＤの存在チェックを行う<br/>
     *
     * ※公開状態は判定しない
     *
     * @param shopSeq ショップSEQ
     * @param categoryIdList カテゴリＩＤのList
     * @return True:OK、False:NG
     */
    List<String> checkCategoryId(Integer shopSeq, List<String> categoryIdList);

    /**
     * アイコンＩＤの存在チェックを行う<br/>
     *
     * @param iconSeqList アイコンＩＤのList
     * @return True:OK、False:NG
     */
    List<Integer> checkIconId(List<Integer> iconSeqList);

    /**
     * 商品名の存在チェックを行う<br/>
     *
     * ※部分一致、公開状態、販売状態は判定不要
     *
     * @param shopSeq ショップSEQ
     * @param goodsNameList 商品名のList
     * @return True:OK、False:NG
     */
    List<String> checkGoodsName(Integer shopSeq, List<String> goodsNameList);

    /**
     * 検索キーワードの存在チェック<br/>
     *
     * ※部分一致、公開状態、販売状態は判定不要
     *
     * @param shopSeq ショップSEQ
     * @param keywordList 検索キーワードのList
     * @return True:OK、False:NG
     */
    List<String> checkKeyword(Integer shopSeq, List<String> keywordList);

}