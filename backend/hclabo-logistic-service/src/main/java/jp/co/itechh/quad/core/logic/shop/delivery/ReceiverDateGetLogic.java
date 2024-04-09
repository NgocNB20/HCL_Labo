/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.dto.shop.delivery.ReceiverDateDto;

import java.util.Date;
import java.util.List;

/**
 * お届け希望日取得ロジック<br/>
 *
 * @author hs32101
 */
public interface ReceiverDateGetLogic {

    /**
     * お届け希望日DTO作成処理<br/>
     * お届け希望日DTOを作成し、配送DTOにセットする<br/>
     *
     * @param deliveryDtoList 配送DTOリスト
     * @param nonSelectFlag   指定なし選択肢有無(true..あり、false..なし)
     */
    void createReceiverDateList(List<DeliveryDto> deliveryDtoList, boolean nonSelectFlag);

    /**
     * お届け希望日DTO作成判定<br/>
     * 配送リードタイム、選択可能日数を元に、お届け希望日DTOを作成するか判定<br/>
     *
     * @param leadTime          配送リードタイム
     * @param selectDays        選択可能日数
     * @param nonSelectFlag     指定なし選択肢有無(true..あり、false..なし)
     * @param deliveryMethodSeq 配送方法SEQ
     * @return お届け希望日DTO
     */
    ReceiverDateDto checkCreateReceiverDateList(int leadTime,
                                                int selectDays,
                                                boolean nonSelectFlag,
                                                Integer deliveryMethodSeq);

    /**
     * お届け不可日判定<br/>
     *
     * @param receiverDate      お届け日
     * @param deliveryMethodSeq 配送方法SEQ
     * @return エラー:true エラーではない:false
     */
    boolean checkDeliveryImpossibleDay(Date receiverDate, Integer deliveryMethodSeq);

}