package jp.co.itechh.quad.ddd.usecase.query.reports;

import jp.co.itechh.quad.ddd.usecase.query.AbstractQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 商品販売クエリ条件
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Scope("prototype")
public class GoodsSaleQueryCondition extends AbstractQueryCondition {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 集計期間From
     */
    private Timestamp aggregateTimeFrom;

    /**
     * 集計期間To
     */
    private Timestamp aggregateTimeTo;

    /**
     * 集計単位
     */
    private String aggregateUnit;

    /**
     * 受注デバイス
     */
    private List<String> orderDeviceList;

    /**
     * 対象データ
     */
    private String orderStatus;

    /**
     * 商品番号
     */
    private String goodsCode;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * カテゴリーIDリスト
     */
    private List<String> categoryIdList;

    /**
     * ページ番号
     */
    private Integer page;

    /**
     * 表示最大件数
     */
    private Integer limit;

    /**
     * ソート項目
     */
    private String orderBy;

    /**
     * ソート
     */
    private Boolean sort = true;
}