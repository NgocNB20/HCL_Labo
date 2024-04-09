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

/**
 * ノベルティプレゼント商品数確認用 検索条件クラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
@Component
@Scope("prototype")
public class NoveltyPresentIconDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * アイコンチェック
     */
    private boolean iconCheck;

    /**
     * アイコンSEQ
     */
    private Integer iconSeq;

    /**
     * アイコン名
     */
    private String iconName;

}