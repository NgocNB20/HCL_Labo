/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea;

import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * フリーエリア検索結果画面情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class FreeareaResultItem implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** No */
    private Integer resultNo;

    /** キー */
    private String freeAreaKey;

    /** タイトル */
    private String freeAreaTitle;

    /** 公開開始日時 */
    private Timestamp openStartTime;

    /*** 公開ステータス */
    private String freeAreaOpenStatus;

    /** フリーエリアSEQ */
    private Integer freeAreaSeq;

    /** サイトマップ出力 */
    private String siteMapFlag = HTypeSiteMapFlag.OFF.getValue();
}
