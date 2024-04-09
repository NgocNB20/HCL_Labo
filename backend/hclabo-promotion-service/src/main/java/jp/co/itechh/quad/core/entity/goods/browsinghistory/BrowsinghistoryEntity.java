/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.browsinghistory;

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
 * あしあと商品クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "Footprint")
@Data
@Component
@Scope("prototype")
public class BrowsinghistoryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 端末識別情報（必須） */
    @Column(name = "accessUid")
    @Id
    private String accessUid;

    /** 商品グループSEQ (FK)（必須） */
    @Column(name = "goodsGroupSeq")
    @Id
    private Integer goodsGroupSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
