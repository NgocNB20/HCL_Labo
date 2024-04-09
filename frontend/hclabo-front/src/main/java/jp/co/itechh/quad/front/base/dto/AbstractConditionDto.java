/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.base.dto;

import jp.co.itechh.quad.front.web.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 検索条件DTO基底クラス<br/>
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@Data
public abstract class AbstractConditionDto implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ページ情報 */
    private PageInfo pageInfo;
}