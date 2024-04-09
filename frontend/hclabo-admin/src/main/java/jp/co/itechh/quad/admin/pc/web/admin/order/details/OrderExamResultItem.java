/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.admin.constant.type.HTypeExamCompletedFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 検査結果 ModelItem
 */
@Data
@Component
@Scope("prototype")
public class OrderExamResultItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 検査項目コード */
    private String examItemCode;

    /** 検査項目名称 */
    private String examItemName;

    /** 異常値区分 */
    private HTypeAbnormalValueType abnormalValueType;

    /** 検査結果値 */
    private String examResultValue;

    /** 単位 */
    private String unit;

    /** 表示基準値 */
    private String standardValue;

    /** 結果補助コメント１内容 */
    private String comment1;

    /** 結果補助コメント２内容 */
    private String comment2;

    /** 検査完了日 */
    private String examCompletedDate;

    /** 検査完了フラグ */
    private HTypeExamCompletedFlag examCompletedFlag;

    /** 表示順 */
    private Integer orderDisplay;
}
