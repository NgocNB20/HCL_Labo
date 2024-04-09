package jp.co.itechh.quad.ddd.infrastructure.sales.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 販売伝票DbEntityクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "SalesSlip")
@Data
@Component
@Scope("prototype")
public class SalesSlipDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 販売伝票ID */
    @Id
    private String salesSlipId;

    /** 販売ステータス */
    private String salesStatus;

    /** クーポンコード */
    private String couponCode;

    /** クーポンSEQ */
    private Integer couponSeq;

    /** クーポン連番 */
    private Integer couponVersionNo;

    /** クーポン名 */
    private String couponName;

    /** クーポン支払い額 */
    private int couponPaymentPrice;

    /** クーポン利用フラグ */
    private boolean couponUseFlag;

    /** 請求金額 */
    private Integer billingAmount;

    /** 商品購入価格合計（税込） */
    private Integer itemPurchasePriceTotal;

    /** 送料（税込） */
    private Integer carriage;

    /** 手数料（税込） */
    private Integer commission;

    /** 標準税率対象金額 */
    private int standardTaxTargetPrice;

    /** 軽減税率対象金額 */
    private int reducedTaxTargetPrice;

    /** 標準税額 */
    private int standardTax;

    /** 軽減税額 */
    private int reducedTax;

    /** 取引ID */
    private String transactionId;

    /** 顧客ID */
    private String customerId;

    /** 受注番号 */
    private String orderCode;

    /** 登録日時 */
    private Date registDate;

    /** 販売確定日時 */
    private Date salesOpenDate;

}
