/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 20022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dto.goods.category;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * カテゴリ条件Dtoクラス
 *
 * @author Pham  Quang Dieu (VJP)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Scope("prototype")
public class CategoryConditionDto extends AbstractConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 条件項目 */
    private String conditionColumn;

    /** 条件値 */
    private String conditionValue;
}
