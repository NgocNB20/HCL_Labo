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

import java.util.List;

/**
 * 関連商品Dao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsRelationSearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 除去対象商品グループSEQリスト */
    private List<Integer> goodsGroupSeqList;

    /** サイト区分 */
    private HTypeSiteType siteType;

    /** 公開状態 */
    private HTypeOpenDeleteStatus openStatus;
}