package jp.co.itechh.quad.ddd.usecase.query.reports;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * 商品販売個数集計クエリモデル
 */
@Data
public class GoodsSaleQueryModel {

    /**
     * 商品販売
     */
    @Id
    private GoodsSale goodsSale;

    /**
     * 日ごとのリスト
     */
    private List<DateCount> dateList;

    /**
     * 商品規格単位の販売総数
     */
    private int totalSales;

    /**
     * 商品規格単位のキャンセル総数
     */
    private int totalCancel;
}
