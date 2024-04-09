package jp.co.itechh.quad.admin.pc.web.admin.order.validation;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.OrderModel;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.OrderSearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.OutputGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.ShipmentSearchGroup;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;

@Data
@Component
public class CouponValidator implements SmartValidator {

    /** メッセージID:入力必須チェック **/
    private static final String MSGCD_COUPON_REQUIRED = "AGG000104W";

    /** クーポン選択フラグ **/
    protected static final String FILED_NAME_COUPON_SELECT = "couponSelect";

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderModel.class.isAssignableFrom(clazz);
    }

    /**
     *
     * @param target Object
     * @param errors Errors
     * @param validationHints Object
     */
    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {

        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!OutputGroup.class.equals(validationHints[0]) && !OrderSearchGroup.class.equals(validationHints[0])
            && !ShipmentSearchGroup.class.equals(validationHints[0]) && !DisplayChangeGroup.class.equals(
                        validationHints[0])) {
            // バリデータ対象のgroupが、OutputGroup、OrderSearchGroup、ShipmentSearchGroupとDisplayChangeGroup以外の場合、チェックしない
            return;
        }

        OrderModel model = OrderModel.class.cast(target);

        // クーポンバリデータ
        errors = checkCoupon(model, errors);

        return;

    }

    /**
     * クーポンバリデータ
     *
     * @param model OrderModel
     * @param errors Errors
     * @return エラー
     */
    public Errors checkCoupon(OrderModel model, Errors errors) {
        // 必須
        if (!StringUtils.isEmpty(model.getCoupon())) {
            if (StringUtils.isEmpty(model.getCouponSelect())) {
                errors.rejectValue(
                                FILED_NAME_COUPON_SELECT, MSGCD_COUPON_REQUIRED, new String[] {"クーポンID／クーポンコード"}, null);
            }
        }
        return errors;
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}