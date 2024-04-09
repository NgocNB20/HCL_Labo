/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate;

import com.gmo_pg.g_pay.client.output.SaveMemberOutput;
import jp.co.itechh.quad.core.dto.multipayment.CardDto;

/**
 * マルチペイメント会員登録通信ロジック
 * @author s_nose
 *
 */
public interface SaveMemberLogic {

    /** 通信処理中エラー発生時：LMC000051 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000051";

    /**
     * 実行メソッド
     * @param cardDto カードDto
     * @return 会員登録出力パラメータ
     */
    SaveMemberOutput execute(CardDto cardDto);

}
