package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * カテゴリ条件項目
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class CategoryConditionItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 条件No */
    private Integer conditionNo;

    /** 条件項目 */
    private String conditionColumn;

    /** 条件演算子 */
    private String conditionOperator;

    /** 条件値 */
    @Length(min = 0, max = 100)
    private String conditionValue;

}
