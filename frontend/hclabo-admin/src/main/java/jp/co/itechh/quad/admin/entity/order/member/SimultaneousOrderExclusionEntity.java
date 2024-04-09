/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.member;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 同時注文排他エンティティ
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class SimultaneousOrderExclusionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 会員SEQ（必須） */
    private Integer memberInfoSeq;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}