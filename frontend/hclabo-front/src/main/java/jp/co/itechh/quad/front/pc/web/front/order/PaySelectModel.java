package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.base.constant.ValidatorConstants;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.ApplyCouponGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.PaySelectGroup;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * お支払い方法選択Model
 *
 * @author Pham Quang Dieu
 */
@Data
public class PaySelectModel extends AbstractModel {

    /** クーポンコード未入力正規表現（全半角スペース可） */
    public static final String REGEX_NON_INPUT_COUPON_CODE = "^[\\s　]*$";

    /** クーポンコード入力有りエラー */
    public static final String MSGCD_INPUT_COUPON_CODE_ERROR = "{FRONT-ORDER-COUPONCODE}";

    /** 決済Dtoマップ */
    private Map<String, SettlementDto> settlementDtoMap;

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice = BigDecimal.ZERO;

    /** クーポンコード */
    @NotEmpty(groups = {ApplyCouponGroup.class})
    @Length(min = 1, max = ValidatorConstants.LENGTH_COUPON_CODE_MAXIMUM, groups = {ApplyCouponGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ApplyCouponGroup.class})
    @Pattern(regexp = REGEX_NON_INPUT_COUPON_CODE, message = MSGCD_INPUT_COUPON_CODE_ERROR,
             groups = {PaySelectGroup.class})
    private String couponCode;

    /** 決済方法リスト(画面表示用アイテム) */
    private List<PaySelectModelItem> paySelectModelItems;

    /** 決済方法選択値 */
    private String settlementMethod;

    /** カード情報登録状態フラグ */
    private boolean displayCredit = false;

    /** GMOカード照会結果 */
    private SearchCardOutput resultCard;

    /** 有効期限リスト（月）*/
    private Map<String, String> expirationDateMonthItems;

    /** 有効期限リスト（年）*/
    private Map<String, String> expirationDateYearItems;

    /** 分割回数リスト */
    private Map<String, String> dividedNumberItems;

    /** 会員情報エンティティ **/
    private MemberInfoEntity memberInfoEntity;

    /** トークン */
    private String token;

    /** GMOトークン決済  APIキー */
    private String gmoApiKey;

    /** この決済で登録済みカードを使用するならtrue */
    private boolean useRegistCardFlg;

    // Change when init payment

    /** 登録されたカードがあるならtrue */
    private boolean registCredit;

    /** 別のカードを使うボタン押下フラグ */
    private boolean preCreditInformationFlag;

    /** クーポン名 */
    private String couponName;

    /**
     * コンディション<br />
     * 利用可能な決済方法が存在するかどうか
     * @return true..存在する / false..存在しない
     */
    public boolean isExistSettlementMethod() {

        if (this.settlementDtoMap == null || this.settlementDtoMap.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * コンディション<br />
     * クーポン名が存在するかどうか
     * @return true..存在する / false..存在しない
     */
    public boolean isCouponName() {

        return StringUtils.isNotEmpty(this.couponName);
    }

    /**
     * クーポン使用可否判定処理。<br />
     * @return 割引前受注金額が１円以上、かつクーポン利用前（クーポン割引額が0円）の場合 true
     */
    public boolean isCanUseCoupon() {
        return couponDiscountPrice.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 登録してあるカード情報を表示するかどうかを判定する<br/>
     * @return true:表示する false:表示しない
     */
    public boolean isUseRegistedCard() {
        return displayCredit;
    }

    /**
     * カード変更ボタンを表示するか判定する<br/>
     * @return true:表示 false:非表示
     */
    public boolean isViewCardChangeBtn() {
        return resultCard != null && CollectionUtil.isNotEmpty(resultCard.getCardList());
    }

    /**
     * カード保存のチェックボックスを表示するかどうかを判定する<br/>
     * @return true:表示 false:非表示
     */
    public boolean isViewCardSaveCheckBox() {
        if (!isUseRegistedCard() && StringUtils.isNotBlank(this.gmoApiKey)) {
            return true;
        }
        return false;
    }

    /**
     * コンディション<br />
     * 決済方法（画面表示用項目）が存在するかどうか
     * @return true..存在する / false..存在しない
     */
    public boolean isExistPaymentModelItems() {
        if (CollectionUtils.isEmpty(this.paySelectModelItems)) {
            return false;
        }
        return true;
    }

    /**
     * クーポン割引情報の表示／非表示判定処理。<br />
     * 注文情報エリアにクーポン割引情報を表示するかどうか判定する為に利用する。
     * @return クーポン割引額が１円以上の場合 true
     */
    public boolean isDisplayCouponDiscount() {
        return couponDiscountPrice.compareTo(BigDecimal.ZERO) != 0;
    }
}