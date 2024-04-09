/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.batch.queue;

import lombok.Data;

import java.util.List;

/**
 * キューメッセージ
 */
@Data
public class SyncUpdateCategoryProductQueueMessage {

    /*** カテゴリーSEQ */
    private Integer categorySeq;

    /** 商品管理SEQのリスト */
    private List<Integer> goodsGroupSeqList;

}