package jp.co.itechh.quad.admin.pc.web.admin.shop.coupon;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * クーポン検索結果アイテムクラス。
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class CouponModelItem implements Serializable {

    /** シリアルバージョンUID */
    public static final long serialVersionUID = 1L;

    /** 検索結果：クーポンSEQ */
    private Integer couponSeq;

    /** 検索結果：No */
    private Integer resultNo;

    /** 検索結果：クーポン名 */
    private String couponName;

    /** 検索結果：クーポンID */
    private String couponId;

    /** 検索結果：開催開始日時 */
    private Timestamp couponStartTime;

    /** 検索結果：開催終了日時 */
    private Timestamp couponEndTime;

    /** 検索結果：クーポンコード */
    private String couponCode;

    /** 割引率 */
    private String discountRate;

    /** 検索結果：割引金額 */
    private BigDecimal couponDiscountPrice;

    /*
     * 表示項目以外
     */

    /** クーポン状態 class */
    private String couponStatusClass;
}