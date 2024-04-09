/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 改訂用注文商品数量変更ユースケースパラメータ
 */
@Data
@Component
@Scope("prototype")
public class ChangeOrderItemCountForRevisionUseCaseParam {

    /** 商品ID（商品サービスの商品SEQ） */
    private Integer orderItemSeq;

    /** 注文数量 */
    private Integer orderCount;
}
