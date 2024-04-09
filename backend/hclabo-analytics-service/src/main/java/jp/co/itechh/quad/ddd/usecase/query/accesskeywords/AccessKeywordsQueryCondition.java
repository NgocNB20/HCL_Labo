package jp.co.itechh.quad.ddd.usecase.query.accesskeywords;

import jp.co.itechh.quad.ddd.usecase.query.AbstractQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 検索キーワード集計条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Scope("prototype")
public class AccessKeywordsQueryCondition extends AbstractQueryCondition {

    /**
     * 期間－From
     */
    private Date processDateFrom;

    /**
     * 期間－To
     */
    private Date processDateTo;

    /**
     * キーワード
     */
    private String searchKeyword;

    /**
     * 検索回数－From
     */
    private Integer searchCountFrom;

    /**
     * 検索回数－To
     */
    private Integer searchCountTo;
}
