/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
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
@Entity
@Table(name = "GoodsRelation")
@Data
@Component
@Scope("prototype")
public class GoodsRelationEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ (FK)（必須） */
    @Column(name = "goodsGroupSeq")
    @Id
    private Integer goodsGroupSeq;

    /** 関連商品グループSEQ（必須） */
    @Column(name = "goodsRelationGroupSeq")
    @Id
    private Integer goodsRelationGroupSeq;

    /** 表示順 */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    // テーブル項目外追加フィールド

    /** 商品グループコード */
    @Column(insertable = false, updatable = false)
    private String goodsGroupCode;

    /** 商品グループ名 */
    @Column(insertable = false, updatable = false)
    private String goodsGroupName;

    /** 商品公開状態PC */
    @Column(insertable = false, updatable = false)
    private HTypeOpenDeleteStatus goodsOpenStatusPC;
}