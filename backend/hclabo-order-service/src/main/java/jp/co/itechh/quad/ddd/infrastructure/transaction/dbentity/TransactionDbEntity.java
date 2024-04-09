package jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 取引DbEntityクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "Transaction")
@Data
@Component
@Scope("prototype")
public class TransactionDbEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 取引ID */
    @Id
    private String transactionId;

    /** 取引ステータス */
    private String transactionStatus;

    /** 入金済みフラグ */
    private Boolean paidFlag;

    /** 入金不足フラグ(入金状態詳細) */
    private boolean insufficientMoneyFlag;

    /** 入金超過フラグ(入金状態詳細) */
    private boolean overMoneyFlag;

    /** 出荷済みフラグ */
    private Boolean shippedFlag;

    /** 入金関連通知実施フラグ */
    private Boolean notificationFlag;

    /** 入金督促通知済みフラグ */
    private Boolean reminderSentFlag;

    /** 入金期限切れ通知済みフラグ */
    private Boolean expiredSentFlag;

    /** 請求決済エラーフラグ */
    private Boolean billPaymentErrorFlag;

    /** 登録日時 */
    private Date registDate;

    /** 顧客ID */
    private String customerId;

    /** 管理メモ */
    private String adminMemo;

    /** 受注ID */
    private String orderReceivedId;

    /** 処理日時 */
    private Date processTime;

    /** 処理種別 */
    private String processType;

    /** 処理担当者名 */
    private String processPersonName;

    /** 前請求フラグ */
    private boolean preClaimFlag;

    /** ノベルティプレゼント判定状態 */
    private String noveltyPresentJudgmentStatus;

}
