package jp.co.itechh.quad.ddd.usecase.query.reports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日付データ
 */
@Getter
@Setter
@NoArgsConstructor
public class DateCount {

    /**
     * 日付データ
     */
    private String date;

    /**
     * 販売個数
     */
    private Integer sales;

    /**
     * キャンセル数
     */
    private Integer cancel;
}
