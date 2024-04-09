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
 * ノベルティプレゼント対象会員クラス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Entity
@Table(name = "NoveltyPresentMember")
@Data
@Component
@Scope("prototype")
public class NoveltyPresentMemberEntity implements Serializable {
    /** serialVersionUID */
    public static final long serialVersionUID = 1L;

    /** ノベルティプレゼント条件SEQ */
    @Column(name = "noveltyPresentConditionSeq")
    @Id
    private Integer noveltyPresentConditionSeq;

    /** 会員SEQ */
    @Column(name = "memberInfoSeq")
    private Integer memberInfoSeq;

    /** 受注ID */
    @Column(name = "orderreceivedId")
    private String orderreceivedId;

    /** 商品SEQ */
    @Column(name = "goodsSeq")
    private Integer goodsSeq;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
