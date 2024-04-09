/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.conveni;

import jp.co.itechh.quad.core.constant.type.HTypeUseConveni;
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
 * コンビニ名称クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "Convenience")
@Data
@Component
@Scope("prototype")
public class ConvenienceEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** コンビニSEQ（必須） */
    @Column(name = "conveniSeq")
    @Id
    private Integer conveniSeq;

    /** 支払コード（必須） */
    @Column(name = "payCode")
    private String payCode;

    /** コンビニコード（必須） */
    @Column(name = "conveniCode")
    private String conveniCode;

    /** 支払名称（必須） */
    @Column(name = "payName")
    private String payName;

    /** コンビニ名称（必須） */
    @Column(name = "conveniName")
    private String conveniName;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    @Column(name = "displayOrder")
    private Integer displayOrder;

    /** 利用するコンビニ */
    @Column(name = "useFlag")
    private HTypeUseConveni useFlag;
}
