/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.novelty;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ノベルティプレゼント商品数確認用 検索条件クラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
@Component
@Scope("prototype")
public class NoveltyPresentValidateDto extends AbstractConditionDto {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ノベルティ商品番号
     */
    private List<String> noveltyGoodsCodeList;

    /**
     * 商品管理番号
     */
    private List<String> goodsGroupCodeList;

    /**
     * 商品番号
     */
    private List<String> goodsCodeList;

    /**
     * カテゴリーID
     */
    private List<String> categoryIdList;

    /**
     * カテゴリーSEQ
     */
    private List<Integer> categorySeqList;

    /**
     * アイコン
     */
    private List<NoveltyPresentIconDto> iconList;

    /**
     * 商品名
     */
    private List<String> goodsNameList;

    /**
     * 検索キーワード
     */
    private List<String> searchKeywordList;

    /**
     * 除外条件
     */
    private List<NoveltyPresentExclusionNoveltyDto> exclusionNoveltyList;
}
