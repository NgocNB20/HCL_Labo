/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * ニュース詳細 Model
 *
 * @author kimura
 */
@Data
public class NewsModel extends AbstractModel {

    /**
     * ニュースSEQ<br/>
     * リクエストパラメータに利用されるため、小文字で宣言
     */
    private String nseq;

    /** ニュース本文 */
    private String newsBodyPC;

    /** ニュース詳細 */
    private String newsNotePC;

    /** ニュースタイトル */
    private String titlePC;

    /** ニュース日時 */
    private Timestamp newsTime;

    /**
     * ニュース公開チェック<br/>
     *
     * @return true:公開している
     */
    public boolean isOpenNews() {
        return newsTime != null;
    }

    /**
     * @return titlePC is null?
     */
    public boolean isNullTitlePC() {
        if (titlePC == null || titlePC.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @return titlePC is not null?
     */
    public boolean isNotNullTitlePC() {
        if (titlePC == null || titlePC.isEmpty()) {
            return false;
        }
        return true;
    }

}
