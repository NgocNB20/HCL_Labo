package jp.co.itechh.quad.ddd.infrastructure.billing.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 改訂用注文決済エンティティDbEntityクラス
 */

@Entity
@Table(name = "OrderPaymentForRevision")
@Data
@Component
@Scope("prototype")
public class OrderPaymentForRevisionDbEntity {

    /**
     * 改訂用注文決済ID
     */
    @Id
    private String orderPaymentRevisionId;

    /**
     * 注文決済ID
     */
    private String orderPaymentId;

    /**
     * 注文決済ステータス
     */
    private String orderPaymentStatus;

    /**
     * 決済種別
     */
    private String settlementMethodType;

    /**
     * 決済方法ID(マスタ)
     */
    private String paymentMethodId;

    /**
     * 決済方法名(マスタ)
     */
    private String paymentMethodName;

    /**
     * 受注番号
     */
    private String orderCode;

    /**
     * 入金期限日時
     */
    private Date paymentLimitDate;

    /**
     * 取消可能日時
     */
    private Date cancelAbleDate;

    /**
     * 有効期限(年) ※後ろ2桁
     */
    private String expirationYear;

    /**
     * 有効期限(月) ※2桁
     */
    private String expirationMonth;

    /**
     * 決済トークン
     */
    private String paymentToken;

    /**
     * マスク済みカード番号
     */
    private String maskedCardNo;

    /**
     * 支払区分（1：一括, 2：分割, 5：リボ）
     */
    private String paymentType;

    /**
     * 分割回数
     */
    private String dividedNumber;

    /**
     * 3Dセキュア有効フラグ
     */
    private boolean enable3dSecureFlag;

    /**
     * カード保存フラグ（保存時true）
     */
    private boolean registCardFlag;

    /**
     * 登録済カード使用フラグ（登録済みtrue）
     */
    private boolean useRegistedCardFlag;

    /**
     * オーソリ期限日
     */
    private Date authLimitDate;

    /**
     * GMO連携解除フラグ
     */
    private boolean gmoReleaseFlag;

    /**
     * 改訂用請求伝票ID
     */
    private String billingSlipRevisionId;

    /**
     * GMO決済キャンセル状態
     */
    private String gmoPaymentCancelStatus;

    /**
     * 決済手段識別子
     */
    private String payMethod;

    /**
     * 決済方法(GMO)
     */
    private String payType;

    /**
     * リンク決済タイプ
     */
    private String linkPaymentType;

    /**
     * 決済手段名
     */
    private String payTypeName;

    /**
     * GMOキャンセル期限日
     */
    private Timestamp cancelLimit;

    /**
     * GMO後日払い支払期限日時
     */
    private Timestamp laterDateLimit;
}