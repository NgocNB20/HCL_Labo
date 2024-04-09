/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.news;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ニュースクラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "News")
@Data
@Component
@Scope("prototype")
public class NewsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ニュースSEQ（必須） */
    @Column(name = "newsSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "newsSeq")
    private Integer newsSeq;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** ニュースタイトル-PC */
    @Column(name = "titlePC")
    private String titlePC;

    /** ニュース本文-PC */
    @Column(name = "newsBodyPC")
    private String newsBodyPC;

    /** ニュースURL-PC */
    @Column(name = "newsUrlPC")
    private String newsUrlPC;

    /** ニュース公開状態PC（必須） */
    @Column(name = "newsOpenStatusPC")
    private HTypeOpenStatus newsOpenStatusPC = HTypeOpenStatus.NO_OPEN;

    /** ニュース公開開始日時pc */
    @Column(name = "newsOpenStartTimePC")
    private Timestamp newsOpenStartTimePC;

    /** ニュース公開終了日時pc */
    @Column(name = "newsOpenEndTimePC")
    private Timestamp newsOpenEndTimePC;

    /** ニュース日時（必須） */
    @Column(name = "newsTime")
    private Timestamp newsTime;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** ニュース詳細PC */
    @Column(name = "newsNotePC")
    private String newsNotePC;
}