package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.HolidayCsvDto;
import jp.co.itechh.quad.core.dto.shop.delivery.HolidaySearchForDaoConditionDto;

import java.util.stream.Stream;

/**
 * 休日CSV出力ロジック
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface HolidayCsvListGetByYearLogic {

    Stream<HolidayCsvDto> execute(HolidaySearchForDaoConditionDto conditionDto);

}