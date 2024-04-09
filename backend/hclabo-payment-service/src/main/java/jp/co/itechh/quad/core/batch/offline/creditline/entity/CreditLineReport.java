/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.batch.offline.creditline.entity;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 与信枠解放
 *
 * @author Kaneko(itec) チケット#2725対応　2011/12/26
 */
@Entity
@Table(name = "CreditLineReport")
@Data
@Component
@Scope("prototype")
public class CreditLineReport implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** オーダーID（必須） */
    @Column(name = "orderId")
    private String orderId;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
