/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.doublesubmit;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * ダブルサブミットチェック用トークン
 *
 * @author hk57400
 */
@AllArgsConstructor
@Value
public class DoubleSubmitToken implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 4463980736683656847L;

    /** リクエストパラメータ名称 */
    public static final String REQUEST_PARAMETER_NAME = "_dsc";

    /** ネームスペース・値を区切るセパレータ文字 */
    public static final String SEPARATOR = "~";

    /** ネームスペース ex) {@literal .member.change} */
    String nameSpace;

    /** 値 ※アクセスごとに更新されるランダム値 */
    String value;

    /**
     * コンストラクタ ※リクエストパラメータから取得して生成時用
     *
     * @param token POSTされたトークン文字列
     */
    public DoubleSubmitToken(final String token) {

        String nameSpace = "";
        String value = "";

        if (StringUtils.isNotBlank(token)) {
            try {
                String[] tokenArray = token.split(SEPARATOR);
                if (tokenArray.length == 2) {
                    nameSpace = tokenArray[0];
                    value = tokenArray[1];
                }
            } catch (Exception e) {
                // 不正POST
            }
        }

        this.nameSpace = nameSpace;
        this.value = value;
    }

    /**
     * @return トークン
     */
    public String getToken() {
        return StringUtils.joinWith(SEPARATOR, nameSpace, value);
    }

    /**
     * チェック
     *
     * @param target 比較対象のトークン
     * @return true:同じ, false:異なる
     */
    public boolean isSame(DoubleSubmitToken target) {

        if (StringUtils.isBlank(nameSpace) || StringUtils.isBlank(value)) {
            return false;
        }

        if (target == null || StringUtils.isBlank(target.getNameSpace()) || StringUtils.isBlank(target.getValue())) {
            return false;
        }

        return StringUtils.equals(getToken(), target.getToken());
    }

}
