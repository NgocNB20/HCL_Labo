/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.util.Map;

/**
 * 3Dセキュア認証用Model<br/>
 * 認証画面に渡す項目は頭文字が大文字であること
 *
 * @author kimura
 */
@Data
public class SecureModel extends AbstractModel {

    /** パラメータMap ※認証画面からの戻り値用  */
    private Map<String, String> param;

}

