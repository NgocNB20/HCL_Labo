/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.annotation.csv.CsvColumn;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.admin.constant.type.HTypeExamCompletedFlag;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 検査結果CSV Dtoクラス
 *
 */
@Data
@Component
@Scope("prototype")
public class ExamResultsCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 正規表現エラー：AOX000811W
     */
    private static final String MSGCD_REGULAR_EXPRESSION_EXAM_KIT_CODE_ERR = "{AOX000811W}";

    /**
     * 正規表現エラー：PKG-3680-014-S-W
     */
    private static final String MSGCD_REGULAR_EXPRESSION_SPECIMEN_CODE_ERR = "{PKG-3680-014-S-W}";

    /**
     * 日付形式エラー
     */
    private static final String MSGCD_DATE_EXPRESSION_EXAMRESULTS_DATE_ERR = "{AOX000812W}";

    /** 検査キット番号 */
    @CsvColumn(order = 10, columnLabel = "検査キット番号")
    @NotEmpty
    @Pattern(regexp = ValidatorConstants.REGEX_EXAM_KIT_CODE, message = MSGCD_REGULAR_EXPRESSION_EXAM_KIT_CODE_ERR)
    @Length(min = 1, max = ValidatorConstants.LENGTH_EXAM_KIT_CODE_MAXIMUM)
    private String examKitCode;

    /** 受付日 */
    @CsvColumn(order = 20, columnLabel = "受付日", dateFormat = "yyyyMMdd")
    @NotNull
    @HVDate(pattern = DateUtility.YMD, message = MSGCD_DATE_EXPRESSION_EXAMRESULTS_DATE_ERR)
    private Timestamp receptionDate;

    /** 検体番号 */
    @CsvColumn(order = 30, columnLabel = "検体番号")
    @NotEmpty
    @Pattern(regexp = ValidatorConstants.REGEX_SPECIMEN_CODE, message = MSGCD_REGULAR_EXPRESSION_SPECIMEN_CODE_ERR)
    @Length(min = 1, max = 6)
    private String specimenCode;

    /** 患者ID */
    @CsvColumn(order = 40, columnLabel = "患者ID")
    private String customerId;

    /** カナ氏名 */
    @CsvColumn(order = 50, columnLabel = "カナ氏名")
    private String customerNameKana;

    /** 検体コメント */
    @CsvColumn(order = 60, columnLabel = "検体コメント")
    @Length(min = 0, max = 2000)
    private String specimenComment;

    /** 検査項目コード */
    @CsvColumn(order = 70, columnLabel = "検査項目コード")
    @NotEmpty
    @Length(min = 1, max = 6)
    private String examItemCode;

    /** 検査項目名称 */
    @CsvColumn(order = 80, columnLabel = "検査項目名称")
    @NotEmpty
    @Length(min = 0, max = 200)
    private String examItemName;

    /** 異常値区分 */
    @CsvColumn(order = 90, columnLabel = "異常値区分", enumOutputType = "value")
    private HTypeAbnormalValueType abnormalValueType;

    /** 検査結果値 */
    @CsvColumn(order = 100, columnLabel = "検査結果値")
    @Length(min = 0, max = 200)
    private String examResultValue;

    /** 単位 */
    @CsvColumn(order = 110, columnLabel = "単位")
    @Length(min = 0, max = 200)
    private String unit;

    /** 表示基準値 */
    @CsvColumn(order = 120, columnLabel = "表示基準値")
    @Length(min = 0, max = 200)
    private String standardValue;

    /** 測定法 */
    @CsvColumn(order = 130, columnLabel = "測定法")
    @Length(min = 0, max = 200)
    private String comment1;

    /** 検体 */
    @CsvColumn(order = 140, columnLabel = "検体")
    @Length(min = 0, max = 200)
    private String comment2;

    /** 検査完了フラグ */
    @CsvColumn(order = 150, columnLabel = "検査完了フラグ", enumOutputType = "value")
    @NotNull
    private HTypeExamCompletedFlag examCompletedFlag;

    /** 検査完了日 */
    @CsvColumn(order = 160, columnLabel = "検査完了日", dateFormat = "yyyyMMdd")
    @HVDate(pattern = DateUtility.YMD, message = MSGCD_DATE_EXPRESSION_EXAMRESULTS_DATE_ERR)
    private Timestamp examCompletedDate;

}