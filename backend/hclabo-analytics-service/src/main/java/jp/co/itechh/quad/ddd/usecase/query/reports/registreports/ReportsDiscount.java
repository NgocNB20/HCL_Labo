package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 割引
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportsDiscount {

    /**
     * クーポン
     */
    private ReportsCoupon coupon;
}
