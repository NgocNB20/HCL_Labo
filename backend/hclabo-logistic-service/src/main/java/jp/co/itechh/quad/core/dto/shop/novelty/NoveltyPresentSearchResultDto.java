/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.novelty;

import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * ノベルティプレゼントDao用検索結果Dto
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
@Component
@Scope("prototype")
public class NoveltyPresentSearchResultDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ノベルティプレゼント条件Entity */
    private NoveltyPresentConditionEntity noveltyPresentConditionEntity;

    /** ノベルティプレゼント商品リスト */
    private List<NoveltyPresentSearchResultGoodsDto> noveltyGoodsList;
}