/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.category;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリ排他Dtoクラス
 *
 * @author DtoGenerator
 */
@Entity
@Data
@Component
@Scope("prototype")
public class CategoryExclusiveDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリー合計 */
    private int sumCategory;

    /** 更新日時MAX */
    private Timestamp maxUpdateTime;
}
