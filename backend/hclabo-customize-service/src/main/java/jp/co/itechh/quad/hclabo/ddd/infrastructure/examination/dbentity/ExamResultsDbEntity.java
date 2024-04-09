package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity;

import jp.co.itechh.quad.hclabo.core.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamCompletedFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 検査結果Dbエンティティ
 *
 */
@Entity
@Table(name = "ExamResults")
@Data
@Component
@Scope("prototype")
public class ExamResultsDbEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 検査キット番号 */
    @Id
    private String examKitCode;

    /** 検査項目コード */
    private String examItemCode;

    /** 検査項目名称 */
    private String examItemName;

    /** 異常値区分 */
    private HTypeAbnormalValueType abnormalValueType;

    /** 検査結果値 */
    private String examResultValue;

    /** 単位 */
    private String unit;

    /** 表示基準値 */
    private String standardValue;

    /** 結果補助コメント１内容 */
    private String comment1;

    /** 結果補助コメント２内容 */
    private String comment2;

    /** 検査完了フラグ */
    private HTypeExamCompletedFlag examCompletedFlag;

    /** 検査完了日 */
    private Date examCompletedDate;

    /** 表示順 */
    private double orderDisplay;

}
