package jp.co.itechh.quad.ddd.domain.customize.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 検査結果
 */
@Data
public class ExamResults {

    /**
     * 検査項目コード
     */
    private String examItemCode;

    /**
     * 検査項目名称
     */
    private String examItemName;

    /**
     * 異常値区分
     */
    private String abnormalValueType;

    /**
     * 検査結果値
     */
    private String examResultValue;

    /**
     * 単位
     */
    private String unit;

    /**
     * 表示基準値
     */
    private String standardvalue;

    /**
     * 結果補助コメント１内容
     */
    private String comment1;

    /**
     * 結果補助コメント２内容
     */
    private String comment2;

    /**
     * 検査完了フラグ
     */
    private String examcompletedflag;

    /**
     * 検査完了日
     */
    private Date examCompletedDate;
}
