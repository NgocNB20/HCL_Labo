/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.goodsgroup.stock;

import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品グループ在庫表示クラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class StockStatusDisplayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ（必須） */
    private Integer goodsGroupSeq;

    /** 在庫状態PC（必須） */
    private HTypeStockStatusType stockStatusPc;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}