package jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales;

import jp.co.itechh.quad.ddd.usecase.query.AbstractQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 受注・売上集計用検索条件クラス
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Scope("prototype")
public class OrderSalesSearchQueryCondition extends AbstractQueryCondition {

    /**
     * 期間－From
     */
    private Date aggregateTimeFrom;

    /**
     * 期間－To
     */
    private Date aggregateTimeTo;

    /**
     * 集計単位
     */
    private String aggregateUnit;

    /**
     * 受注デバイスリスト
     */
    public List<String> orderDeviceList;

    /**
     * 対象データ
     */
    public String orderStatus;

    /**
     * 決済方法
     */
    public List<String> paymentMethodIdList;
}