package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * クーポン
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportsCoupon {

    /**
     * クーポン名
     */
    private String couponName;

    /**
     * クーポンSEQ
     */
    private Integer couponSeq;

    /**
     * クーポン連番
     */
    private Integer couponVersionNo;

}
