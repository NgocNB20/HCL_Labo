/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.multipayment;

import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 決済代行会社との通信結果
 * <pre>
 * 請求不整合報告メールの送信処理で使用
 * 【32_HM3_共通部仕様書_決済代行サービスとの通信.xlsx】参照
 * </pre>
 *
 * @author kk32102
 */
@Data
@Component
@Scope("prototype")
public class CommunicateResult implements Serializable {

    /** 決済代行会社の処理表示名：金額変更 */
    protected static final String TRAN_NAME_CHANGE = "金額変更";

    /** 決済代行会社の処理表示名：売上 */
    protected static final String TRAN_NAME_SALE_FIX = "売上";

    /** 決済代行会社の処理表示名：取消 */
    protected static final String TRAN_NAME_CANCEL = "取消";

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** HIT-MALLの処理区分 */
    private String processType;

    /** 処理対象の受注番号 */
    private String orderCode;

    /** 決済代行会社で処理済みの処理 */
    private List<String[]> tranList = new ArrayList<>();

    /** 取消エラー発生時のエラー内容 */
    private AppLevelFacesMessage cancelError;

    /**
     * 初期化
     *
     * @param processType HIT-MALLの処理区分
     * @param orderCode 処理対象の受注番号
     */
    public void init(String processType, String orderCode) {
        this.processType = processType;
        this.orderCode = orderCode;
        this.tranList = new ArrayList<>();
        this.cancelError = null;
    }

    /**
     * 決済代行会社で処理済みの処理があるか？
     *
     * @return true 処理あり
     */
    public boolean isCommunicate() {
        for (String[] tran : tranList) {
            if (Boolean.parseBoolean(tran[2])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取消エラーが発生したか？
     *
     * @return true エラーあり
     */
    public boolean hasCancelError() {
        return cancelError != null;
    }

    /**
     * 金額変更の処理を行ったか？
     *
     * @return true 金額変更あり
     */
    public boolean hasChangeTran() {
        for (String[] tran : tranList) {
            if (TRAN_NAME_CHANGE.equals(tran[0])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 決済代行会社で処理済みの処理を追加
     *
     * @param name 処理名
     * @param orderId 取引ID
     * @param successFlag true = 処理成功時
     */
    public void addTran(String name, String orderId, boolean successFlag) {
        tranList.add(new String[] {name, orderId, Boolean.toString(successFlag)});
    }

    /**
     * 決済代行会社で処理済みの処理を追加（金額変更）
     *
     * @param orderId 取引ID
     * @param successFlag true = 処理成功時
     */
    public void addChangeTran(String orderId, boolean successFlag) {
        addTran(TRAN_NAME_CHANGE, orderId, successFlag);
    }

    /**
     * 決済代行会社で処理済みの処理を追加（売上）
     *
     * @param orderId 取引ID
     * @param successFlag true = 処理成功時
     */
    public void addSaleFixTran(String orderId, boolean successFlag) {
        addTran(TRAN_NAME_SALE_FIX, orderId, successFlag);
    }

    /**
     * 決済代行会社で処理済みの処理を追加（取消）
     *
     * @param orderId 取引ID
     * @param successFlag true = 処理成功時
     */
    public void addCancelTran(String orderId, boolean successFlag) {
        addTran(TRAN_NAME_CANCEL, orderId, successFlag);
    }

    /**
     * 決済代行会社で処理済みの処理を書き出す
     *
     * @return 処理済みの処理
     */
    public String writeTranInfo() {
        StringBuilder result = new StringBuilder();
        for (String[] tran : tranList) {
            if (result.length() > 0) {
                result.append("\r\n");
            }
            result.append(tran[0] + "　" + getMailBodyOrderId(tran[1]));
            if (Boolean.valueOf(tran[2])) {
                result.append("　成功");
            } else {
                result.append("　失敗");
            }
        }
        return result.toString();
    }

    /**
     * メールに出力する取引ID部分の処理共通化
     * <pre>
     * 取引ID：ST1406265457-001
     * の文言部分
     * ・このクラスのすぐ上（請求不整合チェック）
     * ・出荷登録バッチ
     * で利用
     * </pre>
     *
     * @param orderId 取引ID
     * @return メールに表示する文言
     */
    public String getMailBodyOrderId(String orderId) {
        return "取引ID：" + orderId;
    }

    /**
     * 取消エラー発生時のエラー内容を書き出す
     *
     * @return エラー内容
     */
    public String writeCancelErrorInfo() {
        if (cancelError == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        result.append(cancelError.getMessageCode() + ":");
        if (cancelError.getDetail() != null) {
            result.append(cancelError.getDetail());
        } else {
            result.append(cancelError.getSummary());
        }
        return result.toString();
    }

}