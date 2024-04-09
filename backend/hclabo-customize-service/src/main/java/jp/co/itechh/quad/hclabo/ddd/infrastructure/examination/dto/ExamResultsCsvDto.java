/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dto;

import jp.co.itechh.quad.hclabo.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamCompletedFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 検査結果CSV Dtoクラス
 *
 */
@Entity
@Data
@Component
@Scope("prototype")
public class ExamResultsCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 検査キット番号 */
    @CsvColumn(order = 10, columnLabel = "検査キット番号")
    private String examKitCode;

    /** 受付日 */
    @CsvColumn(order = 20, columnLabel = "受付日", dateFormat = "yyyyMMdd")
    private Timestamp receptionDate;

    /** 検体番号 */
    @CsvColumn(order = 30, columnLabel = "検体番号")
    private String specimenCode;

    /** 患者ID */
    @CsvColumn(order = 40, columnLabel = "患者ID")
    private String customerId;

    /** カナ氏名 */
    @CsvColumn(order = 50, columnLabel = "カナ氏名")
    private String customerNameKana;

    /** 検体コメント */
    @CsvColumn(order = 60, columnLabel = "検体コメント")
    private String specimenComment;

    /** 検査項目コード */
    @CsvColumn(order = 70, columnLabel = "検査項目コード")
    private String examItemCode;

    /** 検査項目名称 */
    @CsvColumn(order = 80, columnLabel = "検査項目名称")
    private String examItemName;

    /** 異常値区分 */
    @CsvColumn(order = 90, columnLabel = "異常値区分", enumOutputType = "value")
    private HTypeAbnormalValueType abnormalValueType;

    /** 検査結果値 */
    @CsvColumn(order = 100, columnLabel = "検査結果値")
    private String examResultValue;

    /** 単位 */
    @CsvColumn(order = 110, columnLabel = "単位")
    private String unit;

    /** 表示基準値 */
    @CsvColumn(order = 120, columnLabel = "表示基準値")
    private String standardValue;

    /** 測定法 */
    @CsvColumn(order = 130, columnLabel = "測定法")
    private String comment1;

    /** 検体 */
    @CsvColumn(order = 140, columnLabel = "検体")
    private String comment2;

    /** 検査完了フラグ */
    @CsvColumn(order = 150, columnLabel = "検査完了フラグ", enumOutputType = "value")
    private HTypeExamCompletedFlag examCompletedFlag;

    /** 検査完了日 */
    @CsvColumn(order = 160, columnLabel = "検査完了日", dateFormat = "yyyyMMdd")
    private Timestamp examCompletedDate;

}