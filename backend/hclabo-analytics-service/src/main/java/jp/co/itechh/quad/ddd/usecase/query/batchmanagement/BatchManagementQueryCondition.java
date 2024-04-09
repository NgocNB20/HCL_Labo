package jp.co.itechh.quad.ddd.usecase.query.batchmanagement;

import jp.co.itechh.quad.ddd.usecase.query.AbstractQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * バッチ管理クエリ―条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Scope("prototype")
public class BatchManagementQueryCondition extends AbstractQueryCondition {

    /**
     * バッチ名
     */
    private String batchName;

    /**
     * 期間－From
     */
    private Date processDateFrom;

    /**
     * 期間－To
     */
    private Date processDateTo;

    /**
     * 処理状況
     */
    private String status;
}
