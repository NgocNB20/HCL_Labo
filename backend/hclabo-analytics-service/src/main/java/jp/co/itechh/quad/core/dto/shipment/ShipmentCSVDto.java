/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shipment;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 出荷CSV出力用Dto
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class ShipmentCSVDto {

    /**
     * 受注番号
     */
    @Id
    @CsvColumn(order = 10, columnLabel = "受注番号")
    private String orderCode;

    /**
     * 出荷登録日
     */
    @CsvColumn(order = 20, columnLabel = "出荷登録日", dateFormat = DateUtility.YMD_SLASH)
    private Date shipmentDate;

    /**
     * 伝票番号
     */
    @CsvColumn(order = 30, columnLabel = "伝票番号")
    private String deliveryCode;
}