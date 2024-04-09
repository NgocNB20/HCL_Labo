package jp.co.itechh.quad.admin.pc.web.admin.shop.coupon;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * クーポン検索ページクラス。<br />
 *
 * <pre>
 * クーポン検索画面表示項目。
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@HVRDateGreaterEqual(target = "searchCouponStartTimeTo", comparison = "searchCouponStartTimeFrom")
@HVRDateGreaterEqual(target = "searchCouponEndTimeTo", comparison = "searchCouponEndTimeFrom")
public class CouponModel extends AbstractModel {

    /** クーポン名の最大文字列長 */
    protected static final int LENGTH_COUPON_NAME_MAXIMUM = 80;
    /** 日時フォーマット */
    protected static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
    /** クーポン状態 「利用終了」 */
    public static final String COUPON_STATUS_PAST = "0";
    /** クーポン状態 「利用期間中」 */
    public static final String COUPON_STATUS_OPEN = "1";
    /** クーポン状態 「利用開始前」 */
    public static final String COUPON_STATUS_FUTURE = "2";

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /** ソート項目 */
    private String orderField;

    /** ソート条件 */
    private boolean orderAsc;

    /** 検索結果総件数 */
    private int totalCount;

    /*
     * 検索条件
     */

    /** 検索条件：クーポン名 */
    @Length(min = 0, max = LENGTH_COUPON_NAME_MAXIMUM)
    @HVSpecialCharacter(allowPunctuation = true)
    private String searchCouponName;

    /** 検索条件：クーポンID */
    @Length(min = 0, max = ValidatorConstants.LENGTH_COUPON_ID_MAXIMUM)
    @Pattern(regexp = ValidatorConstants.REGEX_COUPON_ID, message = ValidatorConstants.MSGCD_COUPON_ID_INVALID)
    @HCHankaku
    private String searchCouponId;

    /** 検索条件：クーポンコード */
    @Length(min = 0, max = ValidatorConstants.LENGTH_COUPON_CODE_MAXIMUM)
    @HVSpecialCharacter(allowPunctuation = true)
    @HCHankaku
    private String searchCouponCode;

    /** 検索条件：クーポン開始日時-From */
    @HVDate
    @HCDate
    private String searchCouponStartTimeFrom;

    /** 検索条件：クーポン開始日時-To */
    @HVDate
    @HCDate
    private String searchCouponStartTimeTo;

    /** 検索条件：クーポン終了日時-From */
    @HVDate
    @HCDate
    private String searchCouponEndTimeFrom;

    /** 検索条件：クーポン終了日時-To */
    @HVDate
    @HCDate
    private String searchCouponEndTimeTo;

    /**
     * 検索条件：対象商品コード<br/>
     */
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_CODE_MAXIMUM)
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(allowPunctuation = true)
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_CODE, message = ValidatorConstants.MSGCD_REGEX_GOODS_CODE)
    @HCHankaku
    private String searchTargetGoodsCode;

    /** 検索結果一覧 */
    private List<CouponModelItem> resultItems;

    /* 画面表示判定 */

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }
}