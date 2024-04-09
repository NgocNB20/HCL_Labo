/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.news;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ニュース検索結果<br/>
 *
 * @author kimura
 *
 */
@Data
@Component
@Scope("prototype")
public class NewsModelItem implements Serializable {
    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /**
     * No
     */
    private Integer resultNo;
    /**
     * ニュース日時
     */
    private Timestamp newsTime;
    /**
     * ニュースタイトル(PC)
     */
    private String titlePC;
    /**
     * ニュースタイトル(スマートフォン)
     */
    private String titleSP;
    /**
     * ニュース公開状態(PC)
     */
    private String newsOpenStatusPC;
    /**
     * ニュースタイトル(携帯)
     */
    private String titleMB;
    /**
     * ニュース公開状態(携帯)
     */
    private String newsOpenStatusMB;
    /**
     * ニュースSEQ
     */
    private Integer newsSeq;
    /**
     * ニュース表示状態(PC)
     */
    private String newsDisplayStatusPC;
    /**
     * ニュース表示状態(携帯)
     */
    private String newsDisplayStatusMB;
    /**
     * ニュース表示状態
     */
    private String newsDisplayStatus;
    /**
     * ニュース詳細(PC)
     */
    private String newsNotePc;
    /**
     * ニュース詳細(SP)
     */
    private String newsNoteSp;

}
