package jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject;

import jp.co.itechh.quad.hclabo.core.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamCompletedFlag;
import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.Date;

/**
 * 検査結果 値オブジェクト
 */
@Getter
public class ExamResult {

    /** 検査キット番号 */
    private final ExamKitCode examKitCode;

    /** 検査項目コード */
    private final String examItemCode;

    /** 検査項目名称 */
    private final String examItemName;

    /** 異常値区分 */
    private final HTypeAbnormalValueType abnormalValueType;

    /** 検査結果値 */
    private final String examResultValue;

    /** 単位 */
    private final String unit;

    /** 表示基準値 */
    private final String standardValue;

    /** 結果補助コメント１内容 */
    private final String comment1;

    /** 結果補助コメント２内容 */
    private final String comment2;

    /** 検査完了フラグ */
    private final HTypeExamCompletedFlag examCompletedFlag;

    /** 検査完了日 */
    private final Date examCompletedDate;

    /** 表示順 */
    private final OrderDisplay orderDisplay;

    /**
     * コンストラクタ<br/>
     * 検査結果情報を設定するための暫定用コンストラクタ<br/>
     *
     * @param examKitCode       検査キット番号
     * @param examItemCode      検査項目コード
     * @param examItemName      検査項目名称
     * @param abnormalValueType 異常値区分
     * @param examResultValue   検査結果値
     * @param unit              単位
     * @param standardValue     表示基準値
     * @param comment1          結果補助コメント１内容
     * @param comment2          結果補助コメント２内容
     * @param examCompletedFlag 検査完了フラグ
     * @param examCompletedDate 検査完了日
     * @param orderDisplay      表示順
     */
    public ExamResult(ExamKitCode examKitCode, String examItemCode, String examItemName, HTypeAbnormalValueType abnormalValueType,
                      String examResultValue, String unit, String standardValue, String comment1, String comment2,
                      HTypeExamCompletedFlag examCompletedFlag, Date examCompletedDate, OrderDisplay orderDisplay) {

        // チェック
        AssertChecker.assertNotNull("examKitCode is null", examKitCode);
        AssertChecker.assertNotEmpty("examItemCode is empty", examItemCode);

        // 設定
        this.examKitCode = examKitCode;
        this.examItemCode = examItemCode;
        this.examItemName = examItemName;
        this.abnormalValueType = abnormalValueType;
        this.examResultValue = examResultValue;
        this.unit = unit;
        this.standardValue = standardValue;
        this.comment1 = comment1;
        this.comment2 = comment2;
        this.examCompletedFlag = examCompletedFlag;
        this.examCompletedDate = examCompletedDate;
        this.orderDisplay = orderDisplay;
    }

    /**
     * コンストラクタ<br/>
     *
     * @param examKitCode       検査キット番号
     * @param examItemCode      検査項目コード
     * @param examItemName      検査項目名称
     * @param abnormalValueType 異常値区分
     * @param examResultValue   検査結果値
     * @param unit              単位
     * @param standardValue     表示基準値
     * @param comment1          結果補助コメント１内容
     * @param comment2          結果補助コメント２内容
     * @param examCompletedFlag 検査完了フラグ
     * @param examCompletedDate 検査完了日
     */
    public ExamResult(ExamKitCode examKitCode,
                      String examItemCode,
                      String examItemName,
                      HTypeAbnormalValueType abnormalValueType,
                      String examResultValue,
                      String unit,
                      String standardValue,
                      String comment1,
                      String comment2,
                      HTypeExamCompletedFlag examCompletedFlag,
                      Date examCompletedDate) {

        // チェック
        AssertChecker.assertNotNull("examKitCode is null", examKitCode);
        AssertChecker.assertNotEmpty("examItemCode is empty", examItemCode);

        // 設定
        this.examKitCode = examKitCode;
        this.examItemCode = examItemCode;
        this.examItemName = examItemName;
        this.abnormalValueType = abnormalValueType;
        this.examResultValue = examResultValue;
        this.unit = unit;
        this.standardValue = standardValue;
        this.comment1 = comment1;
        this.comment2 = comment2;
        this.examCompletedFlag = examCompletedFlag;
        this.examCompletedDate = examCompletedDate;
        this.orderDisplay = null;
    }
}
