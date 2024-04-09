/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 配送特別料金エリア詳細Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class DeliverySpecialChargeAreaResultDto implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 郵便番号 */
    private String zipcode;

    /** 送料 */
    private BigDecimal carriage;

    /** 都道府県名 */
    private String prefecture;

    /** 市区町村名 */
    private String city;

    /** 町域名 */
    private String town;

    /** 丁目 */
    private String numbers;

    /** 住所一覧 */
    private String addressList;
}
