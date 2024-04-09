/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * カテゴリー検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class CategoryItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 連番 */
    private Integer resultNo;

    /** カテゴリID（必須） */
    private String categoryId;

    /** カテゴリ名（必須） */
    private String categoryName;

    /** カテゴリ商品数 */
    private Integer openGoodsCount;

    /** 商品ひもづけ条件 */
    private String categoryCondition;

    /** フロント表示 */
    private String frontDisplay;

    /** フロント表示基準日時(プレビュー日時) */
    private Timestamp frontDisplayReferenceDate;

}