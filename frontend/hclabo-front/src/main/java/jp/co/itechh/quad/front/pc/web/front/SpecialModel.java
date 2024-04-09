/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 特集ページ Model
 *
 * @author Pham Quang Dieu
 *
 */
@Data
public class SpecialModel extends AbstractModel {

    /** 特集ページキー */
    private String fkey;

    /** 特集ページタイトル */
    private String freeAreaTitle;

    /** 特集ページ本文（PC） */
    private String freeAreaBodyPc;

    // コンディション

    /**
     * @return freeAreaTitle is empty?
     */
    public boolean isEmptyFreeAreaTitle() {
        return StringUtils.isEmpty(freeAreaTitle);
    }

}
