/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.multipayment;

import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * クレジットカードブランド
 * @author EntityGenerator
 *
 */
@Entity
@Table(name = "CardBrand")
@Data
@Component
@Scope("prototype")
public class CardBrandEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -1623890946339409681L;

    /** カードブランドSEQ */
    @Column(name = "cardBrandSeq")
    @Id
    private Integer cardBrandSeq;

    /** カードブランドコード */
    @Column(name = "cardBrandCode")
    private String cardBrandCode;

    /** カードブランド名 */
    @Column(name = "cardBrandName")
    private String cardBrandName;

    /** カードブランド表示名PC */
    @Column(name = "cardBrandDisplayPc")
    private String cardBrandDisplayPc;

    /** 表示順 */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 分割支払契約 */
    @Column(name = "installment")
    private String installment;

    /** ボーナス一括契約 */
    @Column(name = "bounusSingle")
    private String bounusSingle;

    /** ボーナス分割契約 */
    @Column(name = "bounusInstallment")
    private String bounusInstallment;

    /** リボ契約 */
    @Column(name = "revolving")
    private String revolving;

    /** 選択可能分割回数 */
    @Column(name = "installmentCounts")
    private String installmentCounts;

    /**
     * Front表示フラグ
     */
    @Column(name = "frontDisplayFlag")
    private HTypeFrontDisplayFlag frontDisplayFlag;
}