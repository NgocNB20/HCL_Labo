package jp.co.itechh.quad.admin.pc.web.admin.goods.validation;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.GoodsRegistUpdateModel;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 商品タグの動的バリデータ
 *
 */
@Data
@Component
public class GoodsTagValidator implements SmartValidator {

    /** エラーコード：必須 */
    public static final String MSGCD_NUMBER_OF_DIGITS = "SEARCH-GOODS-TAG-001";
    public static final String MSGCD_NUMBER_OF_RECORD = "GOODS-TAG-REGIST-001";
    public static final String MSGSD_SPECIAL_CHARACTER = "GOODS-TAG-REGIST-002";

    /** 商品タグ */
    public static final String FILED_NAME_GOODS_TAG = "goodsTagList";

    /**
     * 全角スペース
     */
    public static final String FULL_WIDTH_SPACE_CHARACTER = "\\u3000";

    /** 制御文字 0x80 - 0x9F \p{Cntrl}には含まれない為独自に定義する */
    public static final String CONTROL_CHARCTER_0X80_0X9F = "\\x80\\x81\\x82\\x83\\x84\\x85\\x86"
                                                            + "\\x87\\x88\\x89\\x8a\\x8b\\x8c\\x8d\\x8e\\x8f\\x90\\x91\\x92\\x93\\x94"
                                                            + "\\x95\\x96\\x97\\x98\\x99\\x9a\\x9b\\x9c\\x9d\\x9e\\x9f";

    /** 特殊文字が含まれない正規表現 */
    public static final String NO_SPECIAL_CHARACTER_REGEX =
                    "[^\\p{Cntrl}\\p{Punct}\\p{Space}" + FULL_WIDTH_SPACE_CHARACTER + CONTROL_CHARCTER_0X80_0X9F + "]*";

    @Override
    public boolean supports(Class<?> clazz) {
        return GoodsRegistUpdateModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!ConfirmGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、ConfirmGroup以外の場合、チェックしない
            return;
        }

        GoodsRegistUpdateModel model = (GoodsRegistUpdateModel) target;

        if (CollectionUtils.isNotEmpty(model.getGoodsTagList())) {
            // 50以上の商品タグを登録
            if (model.getGoodsTagList().size() > 50) {
                errors.rejectValue(FILED_NAME_GOODS_TAG, MSGCD_NUMBER_OF_RECORD);
            }

            for (String goodsTag : model.getGoodsTagList()) {
                // １００桁を超えている場合はエラー
                if (goodsTag.length() > 100) {
                    errors.rejectValue(FILED_NAME_GOODS_TAG, MSGCD_NUMBER_OF_DIGITS);
                    break;
                }
                if (!Pattern.matches(NO_SPECIAL_CHARACTER_REGEX, goodsTag)) {
                    errors.rejectValue(FILED_NAME_GOODS_TAG, MSGSD_SPECIAL_CHARACTER);
                    break;
                }
            }
        }
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }

}