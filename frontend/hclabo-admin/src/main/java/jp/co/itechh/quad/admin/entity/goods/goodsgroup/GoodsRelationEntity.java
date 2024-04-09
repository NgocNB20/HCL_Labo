/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.goodsgroup;

import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 関連商品クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsRelationEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ (FK)（必須） */
    private Integer goodsGroupSeq;

    /** 関連商品グループSEQ（必須） */
    private Integer goodsRelationGroupSeq;

    /** 表示順 */
    private Integer orderDisplay;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    // テーブル項目外追加フィールド

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 商品公開状態PC */
    private HTypeOpenDeleteStatus goodsOpenStatusPC;
}