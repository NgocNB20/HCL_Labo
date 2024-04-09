package jp.co.itechh.quad.ddd.usecase.transaction.query;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import lombok.Data;

import java.util.Date;

/**
 * 受注処理履歴一覧用 QueryModel
 */
@Data
public class OrderProcessHistoryQueryModel {

    /** 取引ID */
    private String transactionId;

    /** 処理日時 */
    private Date processTime;

    /** 処理種別 */
    private HTypeProcessType processType;

    /** 処理担当者名 */
    private String processPersonName;
}
