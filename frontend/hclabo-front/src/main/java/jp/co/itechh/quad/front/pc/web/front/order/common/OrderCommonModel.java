/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order.common;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

/**
 * 注文フロー共通Model
 * @author Pham Quang Dieu
 */
@Data
public class OrderCommonModel extends AbstractModel {

    /**  取引ID */
    String transactionId;

    // TODO 登録済みカードのセキュリティコード未使用 https://app.clickup.com/t/2pr3853
    /** セキュリティコード */
    String securityCode;
}
