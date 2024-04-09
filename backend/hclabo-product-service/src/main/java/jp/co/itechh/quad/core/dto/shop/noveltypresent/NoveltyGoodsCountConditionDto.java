/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.noveltypresent;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ノベルティプレゼント商品数確認用 検索条件クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class NoveltyGoodsCountConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    public static final long serialVersionUID = 1L;

    /** ショップSEQ */
    public Integer shopSeq;

    /** 商品管理番号 */
    public List<String> goodsGroupCodeList;

    /** 商品番号 */
    public List<String> goodsCodeList;

    /** カテゴリーID */
    public List<String> categoryIdList;

    /** カテゴリーSEQ */
    public List<Integer> categorySeqList;

    /** アイコンSEQ */
    public List<String> iconSeqList;

    /** 商品名 */
    public List<String> goodsNameList;

    /** 検索キーワード */
    public List<String> searchKeywordList;

}
