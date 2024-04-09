package jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * 受注・売上集計クエリーモデル
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderSalesQueryModel {

    /**
     * 日付データ
     */
    @Id
    private String date;

    /**
     * 検索キーワード集計リスト
     */
    List<OrderSales> dataList;
}
