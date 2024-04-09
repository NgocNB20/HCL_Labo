/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.method;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 選択可能配送方法一覧取得ユースケース
 */
@Data
@Component
@Scope("prototype")
public class GetSelectableShippingMethodListUseCaseDto {

    /** 配送方法ID */
    private String shippingMethodId;

    /** 配送方法名 */
    private String shippingMethodName;

    /** 配送方法説明文 */
    private String shippingMethodNote;

    /** お届け希望日 */
    private List<String> receiverDateList;

    /** お届け希望時間帯 */
    private List<String> receiverTimeZoneList;

}
