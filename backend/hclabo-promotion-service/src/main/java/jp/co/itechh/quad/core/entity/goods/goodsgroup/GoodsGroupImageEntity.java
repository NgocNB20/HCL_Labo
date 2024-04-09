/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.goodsgroup;

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
 * 商品グループ画像クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "GoodsGroupImage")
@Data
@Component
@Scope("prototype")
public class GoodsGroupImageEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ (FK)（必須） */
    @Column(name = "goodsGroupSeq")
    @Id
    private Integer goodsGroupSeq;

    /** 画像種別内連番（必須） */
    @Column(name = "imageTypeVersionNo")
    @Id
    private Integer imageTypeVersionNo = 1;

    /** 画像ファイル名 */
    @Column(name = "imageFileName")
    private String imageFileName;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
