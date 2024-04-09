package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.SelectShipmentRegistGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 受注一覧検索結果画面情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderModelItem implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** resultOrderCheck */
    private boolean resultOrderCheck;

    /** resultOrderSeq */
    private Integer resultOrderSeq;

    /** resultOrderVersionNo */
    private Integer resultOrderVersionNo;

    /* 検索結果(受注番号別) */
    /** No */
    private Integer resultNo;

    /** 受注番号 */
    private String orderCode;

    /** 受注種別 */
    private String orderType;

    /** 受注日時 */
    private Timestamp resultOrderTime;

    /** 受注状態 */
    private String resultOrderStatus;

    /** 検索結果表示用受注状態 */
    private String resultOrderStatusForSearchResult;

    /** 受注状態スタイル */
    private String orderStatusStyleClass;

    /** ご注文主氏名 */
    private String resultOrderName;

    /** お届け先氏名(姓) */
    private String resultReceiverName;

    /** 受注金額 */
    private BigDecimal resultOrderPrice;

    /** お支払い方法 */
    private String resultSettlementMethod;

    /** リンク決済手段 */
    private String linkPaymentMethod;

    /** 入金状態 */
    private String resultPaymentStatus;

    /** 入金状態 スタイル */
    private String paymentStatusStyleClass;

    /** 入金日時 */
    private Timestamp resultReceiptTime;

    /** 入金金額 */
    private BigDecimal resultReceiptPriceTotal;

    /** 配送方法 */
    private String resultDeliveryMethod;

    /** 出荷状態 */
    private String resultShipmentStatus;

    /** 出荷状態スタイル */
    private String shipmentStatusStyleClass;

    /** サイト */
    private String resultOrderSiteType;

    /** 備考 */
    private String resultDeliveryNote;

    /* 検索結果(出荷登録用) */
    /** 注文連番 */
    private Integer resultOrderConsecutiveNo;

    /** 発送日 */
    private Timestamp shippedDate;

    /** 出荷日 */
    @HVDate(groups = {SelectShipmentRegistGroup.class})
    @HCDate
    private String registerShipmentDate;

    /** 伝票番号 */
    @Pattern(regexp = ValidatorConstants.REGEX_DELIVERY_CODE, message = ValidatorConstants.MSGCD_REGEX_DELIVERY_CODE,
             groups = {SelectShipmentRegistGroup.class})
    @Length(min = 0, max = 40, groups = {SelectShipmentRegistGroup.class})
    @HCHankaku
    private String registerDeliveryCode;

    /** 予約配送フラグ */
    private String reservationDeliveryFlag;

    /** メール送信不要フラグ */
    private Boolean notSendMailFlag;

}