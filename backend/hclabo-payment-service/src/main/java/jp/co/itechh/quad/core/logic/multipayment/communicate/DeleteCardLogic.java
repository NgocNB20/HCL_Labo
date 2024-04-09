/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate;

import com.gmo_pg.g_pay.client.output.DeleteCardOutput;
import jp.co.itechh.quad.core.dto.multipayment.CardDto;

/**
 * カード削除通信ロジック
 */
public interface DeleteCardLogic {

    /** 通信処理中エラー発生時：LMC000051 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000051";

    /**
     * 実行メソッド
     *
     * @param cardDto      カードDto
     * @param dbUpdateFlag DB更新フラグ true:カード情報変更後会員情報TBLを更新します false:更新しません
     * @return 結果
     */
    DeleteCardOutput execute(CardDto cardDto, boolean dbUpdateFlag);
}