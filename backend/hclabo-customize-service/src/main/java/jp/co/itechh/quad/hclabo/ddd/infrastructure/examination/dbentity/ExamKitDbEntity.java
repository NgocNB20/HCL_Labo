package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity;

import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamStatus;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 検査キットDbエンティティ
 *
 */
@Entity
@Table(name = "ExamKit")
@Data
@Component
@Scope("prototype")
public class ExamKitDbEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 検査キット番号 */
    @Id
    private String examKitCode;

    /** 受付日 */
    private Date receptionDate;

    /** 検体番号 */
    private String specimenCode;

    /** 検査状態 */
    private HTypeExamStatus examStatus;

    /** 検体コメント */
    private String specimenComment;

    /** 検査結果PDF */
    private String examResultsPdf;

    /** 注文商品ID */
    private String orderItemId;

    /** 受注番号 */
    private String orderCode;

}
