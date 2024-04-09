/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 配送方法Dao用検索条件DTOクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class DeliverySearchForDaoConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC;

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** 商品金額合計 */
    private BigDecimal totalGoodsPrice;

    /** 郵便番号 */
    private String zipcode;

    /** 配送不可エリア判定無効化フラグ */
    private boolean ignoreImpossibleAreaFlag;
}
