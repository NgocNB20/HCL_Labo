/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.memo;

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
 * 受注メモクラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderMemo")
@Data
@Component
@Scope("prototype")
public class OrderMemoEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注メモ連番（必須） */
    @Column(name = "orderMemoVersionNo")
    @Id
    private Integer orderMemoVersionNo;

    /** メモ */
    @Column(name = "memo")
    private String memo;

    /** お客様へのメッセージ */
    @Column(insertable = false, updatable = false)
    private String customerMemo;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}