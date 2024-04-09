/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.novelty;

import jp.co.itechh.quad.core.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ノベルティプレゼント判定用パラメータDto<br/>
 *
 * @author Doan Thang (VJP)
 *
 */
@Data
public class NoveltyPresentJudgmentDto implements Serializable {

    /** シリアルID */
    private static final long serialVersionUID = 1L;

    /** ノベルティプレゼント条件のリスト */
    private List<NoveltyPresentConditionEntity> noveltyPresentConditionEntityList;

    /** チェック対象の受注商品リスト（HM用） */
    private List<SecuredShippingItem> hmTargetOrderGoodsList;

    /** 条件判定 */
    private ConditionJudgmentDto conditionJudgmentDto;

    /** 同梱単位区分 */
    private HTypeEnclosureUnitType enclosureUnitType;

    /** 商品の条件に一致した受注商品MAP */
    private Map<Integer, List<SecuredShippingItem>> conditionMatchOrderGoodsMap;

}
