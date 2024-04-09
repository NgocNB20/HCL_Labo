/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.goods.goodsgroup;

import jp.co.itechh.quad.front.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSiteType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品グループDao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsGroupSearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** キーワード条件1 */
    private String keywordLikeCondition1;

    /** キーワード条件2 */
    private String keywordLikeCondition2;

    /** キーワード条件3 */
    private String keywordLikeCondition3;

    /** キーワード条件4 */
    private String keywordLikeCondition4;

    /** キーワード条件5 */
    private String keywordLikeCondition5;

    /** キーワード条件6 */
    private String keywordLikeCondition6;

    /** キーワード条件7 */
    private String keywordLikeCondition7;

    /** キーワード条件8 */
    private String keywordLikeCondition8;

    /** キーワード条件9 */
    private String keywordLikeCondition9;

    /** キーワード条件10 */
    private String keywordLikeCondition10;

    /** カテゴリID */
    private String categoryId;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 下限金額 */
    private BigDecimal minPrice;

    /** 上限金額 */
    private BigDecimal maxPrice;

    /** サイト区分 */
    private HTypeSiteType siteType;

    /** 公開状態 */
    private HTypeOpenDeleteStatus openStatus;

    /** 対象外公開状態 */
    private HTypeOpenDeleteStatus outOpenStatus;

    /** カテゴリーパス */
    private String categoryPath;

    /** 在庫ありステータス */
    private String[] stcockExistStatus;

    /** 商品グループSEQリスト */
    private List<Integer> goodsGroupSeqList;

}