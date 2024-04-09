/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 休日 CSV ダウンロード・アップロード用の専用Dto
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
@Entity
@Component
@Scope("prototype")
public class HolidayCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    @CsvColumn(order = 0, columnLabel = "配送方法SEQ")
    private Integer deliveryMethodSeq;

    /** 年月日 */
    @CsvColumn(order = 1, columnLabel = "年月日", dateFormat = "yyyy/MM/dd")
    private Date date;

    /** 名前 */
    @CsvColumn(order = 2, columnLabel = "名前")
    private String name;

    /** 登録日時 */
    @CsvColumn(order = 3, columnLabel = "登録日時", dateFormat = "yyyy/MM/dd HH:mm:ss", isOnlyDownload = true)
    private Timestamp registtime;

    /** 年 */
    private Integer year;

}
