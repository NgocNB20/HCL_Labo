/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook.query.model;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 住所クエリーコンディション<br/>
 */
@Data
@Component
@Scope("prototype")
public class AddressQueryCondition extends AbstractConditionDto {

    /** 顧客ID */
    private String customerId;

    /** 除外住所ID */
    private String exclusionAddressId;

}
