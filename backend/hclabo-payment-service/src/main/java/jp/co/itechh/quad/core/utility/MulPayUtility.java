/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import com.gmo_pg.g_pay.client.output.BaseOutput;
import com.gmo_pg.g_pay.client.output.ErrHolder;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import org.springframework.stereotype.Component;

/**
 * マルチペイメントヘルパークラス
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/08 UtilからHelperへ変更
 */
@Component
public class MulPayUtility {

    /**
     * エラー種別 : カードエラー
     */
    public static final String ERR_CARD = "MulPayUtility.ERR_CARD";

    /**
     * エラー種別 : 通信エラー
     */
    public static final String ERR_SYSTEM = "MulPayUtility.ERR_SYSTEM";

    /**
     * エラー種別 : エラー無
     */
    public static final String ERR_NONE = "MulPayUtility.ERR_NONE";

    /**
     * GMOのカードエラーのPREFIX
     */
    public static final String GMO_CARD_ERR_PREFIX_REGEX = "^[G|C].*";

    /**
     * コンストラクタ
     */
    public MulPayUtility() {
        // nop
    }

    /**
     * 決済代行会員IDを作成する
     *
     * @param memberSeq 会員SEQ
     * @return 決済代行会員ID
     */
    public String createPaymentMemberId(Integer memberSeq) {
        if (memberSeq == null) {
            return null;
        }

        // システムプロパティから環境値取得
        String gmoPrefix = PropertiesUtil.getSystemPropertiesValue("gmo.member.prefix");

        // プロパティ設定をしていない場合、
        // 決済代行会員IDが"null会員SEQ"となるのを回避する
        if (gmoPrefix == null) {
            gmoPrefix = "";
        }

        return gmoPrefix + memberSeq.toString();
    }

    /**
     * GMOと通信した結果のエラーの種別を取得します。
     *
     * @param output GMO結果基底クラス
     * @return エラー種別
     */
    public String getErrorType(BaseOutput output) {

        if (!output.isErrorOccurred()) {
            return ERR_NONE;
        }

        for (Object obj : output.getErrList()) {
            if (obj instanceof ErrHolder) {
                String errCode = ((ErrHolder) obj).getErrCode();
                if (errCode.matches(GMO_CARD_ERR_PREFIX_REGEX)) {
                    return ERR_CARD;
                }
            }
        }

        return ERR_SYSTEM;
    }
}
