/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.novelty;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * ノベルティプレゼントDao用検索条件Dto(管理機能用）クラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
@Component
@Scope("prototype")
public class NoveltyPresentSearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** ノベルティプレゼント条件名 */
    private String noveltyPresentName;

    /** ノベルティプレゼント条件状態 */
    private List<String> noveltyPresentState;

    /** ノベルティプレゼント条件開始日-From */
    private Timestamp noveltyPresentStartTimeFrom;

    /** ノベルティプレゼント条件開始日-To */
    private Timestamp noveltyPresentStartTimeTo;

    /** ノベルティプレゼント条件終了日-From */
    private Timestamp noveltyPresentEndTimeFrom;

    /** ノベルティプレゼント条件終了日-To */
    private Timestamp noveltyPresentEndTimeTo;

    /** ノベルティ商品番号 */
    private String noveltyPresentGoodsCode;

    /** ノベルティ商品在庫数-From */
    private Integer noveltyPresentGoodsCountFrom;

    /** ノベルティ商品在庫数-To */
    private Integer noveltyPresentGoodsCountTo;

    /** 商品SEQリスト */
    private List<Integer> goodsSeqList;

    /** 商品SEQ */
    private Integer noveltyGoodsSeq;
}