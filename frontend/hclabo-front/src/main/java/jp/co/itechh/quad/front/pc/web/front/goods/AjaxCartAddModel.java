/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

/**
 * カート追加（Ajax）Modelクラス<br/>
 *
 * @author kaneda
 */
@Data
public class AjaxCartAddModel extends AbstractModel {

    /**
     * 商品SEQ
     */
    private String gseq;

    /**
     * 商品数量
     */
    private String gcnt;

}