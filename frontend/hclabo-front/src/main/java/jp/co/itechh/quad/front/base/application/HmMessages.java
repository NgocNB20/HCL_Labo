/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.base.application;

import jp.co.itechh.quad.front.base.application.FacesMessage.Severity;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * HIT-MALLメッセージクラス<br/>
 * 画面にメッセージ表示する際使われます<br/>
 *
 * 画面に表示する全メッセージ（複数件）を保持します<br/>
 * 保持しているメッセージの種類（エラー、インフォなど）を判別するためのメソッドも
 * 定義しています<br/>
 * @author yt23807
 *
 */
public class HmMessages extends ArrayList<AppLevelFacesMessage> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * エラーメッセージ有無判定<br/>
     * ERROR、FATALのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    public boolean hasError() {
        return hasMessage(FacesMessage.SEVERITY_FATAL, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * 警告メッセージ有無判定<br/>
     * WARNのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    public boolean hasWarn() {
        return hasMessage(FacesMessage.SEVERITY_WARN);
    }

    /**
     * 通知メッセージ有無判定<br/>
     * INFOのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    public boolean hasInfo() {
        return hasMessage(FacesMessage.SEVERITY_INFO);
    }

    /**
     * エラーメッセージ有無判定<br/>
     * ERROR、FATALのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    public boolean allError() {
        return allMessage(FacesMessage.SEVERITY_FATAL, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * 警告メッセージ有無判定<br/>
     * WARNのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    public boolean allWarn() {
        return allMessage(FacesMessage.SEVERITY_WARN);
    }

    /**
     * 通知メッセージ有無判定<br/>
     * INFOのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    public boolean allInfo() {
        return allMessage(FacesMessage.SEVERITY_INFO);
    }

    /**
     * エラーメッセージ有無判定<br/>
     * ERROR、FATALのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    protected boolean hasMessage(Severity... severities) {
        if (severities == null)
            throw new IllegalStateException("severitiesは設定必須です");

        Iterator<AppLevelFacesMessage> it = this.iterator();
        if (it != null) {
            while (it.hasNext()) {
                AppLevelFacesMessage message = it.next();
                for (Severity severity : severities) {
                    if (message.getSeverity() == severity) {
                        // 1件でも引数指定のSeverityメッセージが含まれていればtrue返却
                        return true;
                    }
                }
            }
        }
        // デフォルトはfalse返却
        return false;
    }

    /**
     * エラーメッセージ有無判定<br/>
     * ERROR、FATALのメッセージが設定されているかの判定
     * @return true=有、false=無
     */
    protected boolean allMessage(Severity... severities) {
        if (severities == null)
            throw new IllegalStateException("severitiesは設定必須です");

        Iterator<AppLevelFacesMessage> it = this.iterator();
        if (it != null) {
            while (it.hasNext()) {
                AppLevelFacesMessage message = it.next();

                // 引数指定のseverityにマッチしたかどうかフラグ
                boolean matchSeverity = false;
                for (Severity severity : severities) {
                    if (message.getSeverity() == severity) {
                        // マッチした場合、フラグをtrueにしseveritiesループを抜ける
                        matchSeverity = true;
                        break;
                    }
                }
                // 引数指定のSeverityいずれにもマッチしないメッセージが存在した場合、
                // falseを返却
                if (!matchSeverity) {
                    return false;
                }
            }
            // メッセージが存在し、且つ　全メッセージが引数指定のSeverityに一致していれば、
            // trueを返却
            return true;
        }
        // デフォルトはfalse返却
        return false;
    }
}