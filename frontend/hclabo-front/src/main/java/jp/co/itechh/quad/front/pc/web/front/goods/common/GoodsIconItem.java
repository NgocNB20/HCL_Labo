/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods.common;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 商品アイコンItem
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsIconItem {

    /** 商品アイコン名*/
    private String iconName;

    /** 商品アイコンカラーコード*/
    private String iconColorCode;

}