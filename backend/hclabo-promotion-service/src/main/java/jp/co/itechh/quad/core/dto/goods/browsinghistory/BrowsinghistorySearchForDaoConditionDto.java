/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.dto.goods.browsinghistory;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * あしあと商品Dao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 *
 */
@Data
@Component
@Scope("prototype")
public class BrowsinghistorySearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 端末識別情報 */
    private String accessUid;

    /** 除去対象商品グループSEQリスト */
    private List<Integer> goodsGroupSeqList;
}
