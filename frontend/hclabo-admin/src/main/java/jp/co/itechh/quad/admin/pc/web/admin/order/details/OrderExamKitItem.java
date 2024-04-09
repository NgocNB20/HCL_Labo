/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.constant.type.HTypeExamStatus;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 検査キット ModelItem
 */
@Data
@Component
@Scope("prototype")
public class OrderExamKitItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 検査キット番号 */
    private String examKitCode;

    /** 検査状態 */
    private HTypeExamStatus examStatus;

    /** 受付日 */
    private String receptionDate;

    /** 検体番号 */
    private String specimenCode;

    /** 検体コメント */
    private String specimenComment;

    /** 検査結果PDF */
    private String examResultsPdf;

    /** 注文商品ID */
    private String orderItemId;

    /** 受注番号 */
    private String orderCode;

    /** 検査結果リスト */
    private List<OrderExamResultItem> examResultList;

}
