package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDayCsvDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;

import java.util.stream.Stream;

/**
 * お届け不可日CSV出力ロジック
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface DeliveryImpossibleDayCsvListGetByYearLogic {

    Stream<DeliveryImpossibleDayCsvDto> execute(DeliveryImpossibleDaySearchForDaoConditionDto conditionDto);

}
