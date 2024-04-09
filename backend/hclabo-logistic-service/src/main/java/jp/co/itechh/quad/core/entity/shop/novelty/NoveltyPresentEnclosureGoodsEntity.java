/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.novelty;

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
 *
 * ノベルティプレゼント同梱商品クラス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Entity
@Table(name = "NoveltyPresentEnclosureGoods")
@Data
@Component
@Scope("prototype")
public class NoveltyPresentEnclosureGoodsEntity implements Serializable {

    /** serialVersionUID */
    public static final long serialVersionUID = 1L;

    /** ノベルティプレゼント条件SEQ */
    @Column(name = "noveltyPresentConditionSeq")
    @Id
    public Integer noveltyPresentConditionSeq;

    /** 商品SEQ */
    @Column(name = "goodsSeq")
    @Id
    public Integer goodsSeq;

    /** 優先順 */
    @Column(name = "priorityOrder")
    public Integer priorityOrder;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    public Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    public Timestamp updateTime;

}