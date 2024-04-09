/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.stock;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫詳細ページアイテムクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class StockDetailsModelItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /* 入庫実績リスト */

    /**
     * 入庫日時
     */
    private Timestamp resultSupplementTime;

    /**
     * 入庫数
     */
    private BigDecimal resultSupplementCount;

    /**
     * 実在個数
     */
    private BigDecimal resultRealStock;
    ;

    /**
     * 担当者
     */
    private String resultPersonSeq;

    /**
     * 備考
     */
    private String resultNote;

    /**
     * 画面で表示する入庫日時
     */
    private String resultSupplementTimeStr;
}