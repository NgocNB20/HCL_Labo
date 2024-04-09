package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleDayDao;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDayCsvDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayCsvListGetByYearLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * お届け不可日CSV出力ロジック
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class DeliveryImpossibleDayCsvListGetByYearLogicImpl extends AbstractShopLogic
                implements DeliveryImpossibleDayCsvListGetByYearLogic {

    /**
     * お届け不可日Dao
     */
    private final DeliveryImpossibleDayDao deliveryImpossibleDayDao;

    @Autowired
    public DeliveryImpossibleDayCsvListGetByYearLogicImpl(DeliveryImpossibleDayDao deliveryImpossibleDayDao) {
        this.deliveryImpossibleDayDao = deliveryImpossibleDayDao;
    }

    @Override
    public Stream<DeliveryImpossibleDayCsvDto> execute(DeliveryImpossibleDaySearchForDaoConditionDto conditionDto) {
        return this.deliveryImpossibleDayDao.exportCsvSearchDeliveryImpossibleDayList(
                        conditionDto.getYear(), conditionDto.getDeliveryMethodSeq());
    }
}