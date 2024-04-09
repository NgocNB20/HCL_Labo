package jp.co.itechh.quad.admin.pc.web.admin.order.details.validation;

import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.DetailsUpdateModel;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.OrderGoodsUpdateItem;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.AdditionalChargeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OrderGoodsDeleteGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OrderGoodsModifyGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.ReCalculateGroup;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;

/**
 * 受注商品数量バリデータクラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
public class OrderGoodsCountValidator implements SmartValidator {

    private final String REJECT_GOODS_COUNT_PATTERN = "orderReceiverItem.orderGoodsUpdateItems[%d].updateGoodsCount";

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public OrderGoodsCountValidator(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DetailsUpdateModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!ConfirmGroup.class.equals(validationHints[0])
            && !ReCalculateGroup.class.equals(validationHints[0])
            && !AdditionalChargeGroup.class.equals(validationHints[0])
            && !OrderGoodsDeleteGroup.class.equals(validationHints[0])
            && !OrderGoodsModifyGroup.class.equals(validationHints[0])
        ) {
            return;
        }

        DetailsUpdateModel detailsUpdateModel = (DetailsUpdateModel) target;

        int index = 0;
        for (OrderGoodsUpdateItem orderGoodsUpdateItem : detailsUpdateModel.getOrderReceiverItem().getOrderGoodsUpdateItems()) {
            Integer updateGoodsCount = conversionUtility.toInteger(orderGoodsUpdateItem.getUpdateGoodsCount());
            Integer originGoodsCount = conversionUtility.toInteger(orderGoodsUpdateItem.getOriginGoodsCount());

            if (HTypeNoveltyGoodsType.NOVELTY_GOODS.equals(orderGoodsUpdateItem.getNoveltyGoodsType())) {
                index++;
                continue;
            }

            if (originGoodsCount == null) {
                if (updateGoodsCount > 1) {
                    errors.rejectValue(String.format(REJECT_GOODS_COUNT_PATTERN, index), "ORDER-GCUC0001-E");
                }
            } else {
                if (originGoodsCount == 0 && updateGoodsCount > 0) {
                    errors.rejectValue(String.format(REJECT_GOODS_COUNT_PATTERN, index), "ORDER-GCUC0002-E");
                } else if (originGoodsCount >= 1 && updateGoodsCount > 1) {
                    errors.rejectValue(String.format(REJECT_GOODS_COUNT_PATTERN, index), "ORDER-GCUC0001-E");
                }
            }

            index++;
        }

    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}
