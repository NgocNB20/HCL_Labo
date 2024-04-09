/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.dto.order.OrderSearchConditionDto;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

/**
 * 処理履歴一覧モデル
 *
 * @author kimura
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderProcessHistoryModel extends AbstractModel {

    /** 検索条件（セッション） */
    private OrderSearchConditionDto conditionDto;

    /** 処理履歴アイテムクラスリスト */
    @Valid
    private List<OrderProcessHistoryModelItem> processHistoryModelItems;

    /** 受注履歴番号（表示） */
    private String orderCode;

    /** 受注履歴連番 パラメータ用 */
    private Integer versionNo;

    /** キャンセルフラグ */
    private boolean cancel = false;
}
