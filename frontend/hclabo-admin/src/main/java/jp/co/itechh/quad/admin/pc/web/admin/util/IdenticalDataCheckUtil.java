/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.util;

/**
 * 不正操作チェックユーティル
 *
 * @author kaneda
 */
public class IdenticalDataCheckUtil {

    /**
     * メッセージコード：不正操作
     */
    public static final String MSGCD_ILLEGAL_OPERATION = "CHECK-IDENTICALDATA-INVALID-";

    /**
     * 登録・更新・削除などの処理を行うときに、画面表示時と登録時で対象が一致しているかを確認する<br/>
     * 2つ以上のタブを使用し、情報を編集したときにアクション実行時にエラーとして処理するようにする<br/>
     *
     * @param scSeq
     * @param dbSeq
     * @return 登録情報が一致している場合trueを返す
     */
    public static boolean checkIdentical(Integer scSeq, Integer dbSeq) {
        // 新規登録として登録画面に遷移し、更新情報を保持している場合エラー
        if (scSeq == null && dbSeq != null) {
            return false;

            // 更新として登録画面に遷移し、更新情報を保持していない場合エラー
        } else if (scSeq != null && dbSeq == null) {
            return false;

            // 更新情報を表示したときの情報と登録するときの情報が異なる場合エラー
        } else if (scSeq != null && !scSeq.equals(dbSeq)) {
            return false;
        }
        return true;
    }
}
