package jp.co.itechh.quad.ddd.domain.customize.adapter.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 検査キット
 */
@Data
public class ExamKit {

    /**
     * 検査キット番号
     */
    private String examKitCode;

    /**
     * 受付日
     */
    private Date receptionDate;

    /**
     * 検体番号
     */
    private String specimenCode;

    /**
     * 検査状態
     */
    private String examStatus;

    /**
     * 検体コメント
     */
    private String specimenComment;

    /**
     * 検査結果PDF
     */
    private String examResultsPdf;

    /**
     * 注文商品ID
     */
    private String orderItemId;

    /**
     * 受注番号
     */
    private String orderCode;

    /**
     * 検査結果リスト
     */
    private List<ExamResults> examResultList;

}
