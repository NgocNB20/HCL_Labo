/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.stock;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫CSVダウンロードDtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Data
@Component
@Scope("prototype")
public class StockDownloadCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループコード */
    @CsvColumn(order = 10, columnLabel = "商品管理番号")
    private String goodsGroupCode;

    /** 商品コード */
    @CsvColumn(order = 20, columnLabel = "商品番号")
    private String goodsCode;

    /** JANコード */
    @CsvColumn(order = 30, columnLabel = "JANコード")
    private String janCode;

    /** 商品グループ名 */
    @CsvColumn(order = 50, columnLabel = "商品グループ名")
    private String goodsGroupName;

    /** 規格値１ */
    @CsvColumn(order = 60, columnLabel = "規格1")
    private String unitValue1;

    /** 規格値２ */
    @CsvColumn(order = 70, columnLabel = "規格2")
    private String unitValue2;

    /** 販売可能在庫数 */
    @CsvColumn(order = 80, columnLabel = "販売可能在庫数")
    private BigDecimal salesPossibleStock;

    /** 実在庫数 */
    @CsvColumn(order = 90, columnLabel = "実在庫数")
    private BigDecimal realStock;

    /** 受注確保在庫数 */
    @CsvColumn(order = 100, columnLabel = "受注確保在庫数")
    private BigDecimal orderReserveStock;

    /** 残少表示在庫数 */
    @CsvColumn(order = 110, columnLabel = "残少表示在庫数")
    private BigDecimal remainderFewStock;

    /** 発注点在庫数 */
    @CsvColumn(order = 120, columnLabel = "発注点在庫数")
    private BigDecimal orderPointStock;

    /** 安全在庫数 */
    @CsvColumn(order = 130, columnLabel = "安全在庫数")
    private BigDecimal safetyStock;

    /** 商品公開状態PC */
    @CsvColumn(order = 160, columnLabel = "公開状態PC", enumOutputType = "value")
    private HTypeOpenDeleteStatus goodsOpenStatusPC;

    /** 商品公開開始日時PC */
    @CsvColumn(order = 170, columnLabel = "公開開始日時PC", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp openStartTimePC;

    /** 商品公開終了日時PC */
    @CsvColumn(order = 180, columnLabel = "公開終了日時PC", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp openEndTimePC;

    /** 販売状態PC */
    @CsvColumn(order = 220, columnLabel = "販売状態PC", enumOutputType = "value")
    private HTypeGoodsSaleStatus saleStatusPC;

    /** 販売開始日時PC */
    @CsvColumn(order = 230, columnLabel = "販売開始日時PC", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp saleStartTimePC;

    /** 販売終了日時PC */
    @CsvColumn(order = 240, columnLabel = "販売終了日時PC", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp saleEndTimePC;

}