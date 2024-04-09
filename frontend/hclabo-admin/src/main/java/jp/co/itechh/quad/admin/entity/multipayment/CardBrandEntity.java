/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.multipayment;

import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * クレジットカードブランド
 * @author EntityGenerator
 *
 */
@Data
@Component
@Scope("prototype")
public class CardBrandEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -1623890946339409681L;

    /** カードブランドSEQ */
    private Integer cardBrandSeq;

    /** カードブランドコード */
    private String cardBrandCode;

    /** カードブランド名 */
    private String cardBrandName;

    /** カードブランド表示名PC */
    private String cardBrandDisplayPc;

    /** 表示順 */
    private Integer orderDisplay;

    /** 分割支払契約 */
    private String installment;

    /** ボーナス一括契約 */
    private String bounusSingle;

    /** ボーナス分割契約 */
    private String bounusInstallment;

    /** リボ契約 */
    private String revolving;

    /** 選択可能分割回数 */
    private String installmentCounts;

    /**
     * Front表示フラグ
     */
    private HTypeFrontDisplayFlag frontDisplayFlag;
}