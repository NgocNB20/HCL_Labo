/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup;

import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class InquiryGroupModelItem implements Serializable {
    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /************************************
     **  検索結果項目
     ************************************/
    /**
     * 表示順
     */
    private Integer orderDisplayRadio;
    /**
     * 問い合わせ分類名称
     */
    private String inqueryGroupName;
    /**
     * 公開状態
     */
    private HTypeOpenDeleteStatus openStatus;
    /**
     * 問い合わせ分類SEQ
     */
    private Integer inquiryGroupSeq;
    /**
     * ショップSEQ
     */
    private Integer shopSeq;

}
