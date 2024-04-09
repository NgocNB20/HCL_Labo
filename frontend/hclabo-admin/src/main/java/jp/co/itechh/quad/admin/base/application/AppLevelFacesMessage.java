/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.application;

/**
 * メッセージを保持するクラス<br/>
 * FacesMessageを継承する<br/>
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public class AppLevelFacesMessage extends FacesMessage {

    /**
     *  シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * エラーのタイプ:F システムエラー<br/>
     */
    public static final String TYPE_FATAL = "F";

    /**
     * エラーのタイプ:E エラーメッセージ<br/>
     */
    public static final String TYPE_ERROR = "E";

    /**
     * エラーのタイプ:W 警告メッセージ<br/>
     */
    public static final String TYPE_WARN = "W";

    /**
     * エラーのタイプ:I 情報メッセージ<br/>
     */
    public static final String TYPE_INFO = "I";

    /**
     * SeverityをERRORとし、AppLevelFacesMessageを作成する<br/>
     */
    public AppLevelFacesMessage() {
        this(SEVERITY_ERROR, null, null);
    }

    /**
     * SeverityをERRORとし、引数のsummayでAppLevelFacesMessageを作成する<br/>
     *
     * @param summary   メッセージ概要
     */
    public AppLevelFacesMessage(String summary) {
        this(SEVERITY_ERROR, summary, null);
    }

    /**
     * SeverityをERRORとし、引数のsummay,detailでAppLevelFacesMessageを作成する<br/>
     *
     * @param summary   メッセージ概要
     * @param detail    メッセージ詳細
     */
    public AppLevelFacesMessage(String summary, String detail) {
        this(SEVERITY_ERROR, summary, detail);
    }

    /**
     * 引数のseverity,summary,detailからAppLevelFacesMessageを作成する<br/>
     *
     * @param severity  メッセージレベル
     * @param summary   メッセージ概要
     * @param detail    メッセージ詳細
     */
    public AppLevelFacesMessage(Severity severity, String summary, String detail) {
        super(severity, summary, detail);
    }

    /**
     * FacesMessageからAppLevelFacesMessageを作成する<br/>
     *
     * @param facesMessage FacesMessageオブジェクト
     */
    public AppLevelFacesMessage(FacesMessage facesMessage) {
        this(facesMessage.getSeverity(), facesMessage.getSummary(), facesMessage.getDetail());
    }

    /**
     * 引数facesMessage, messageCode, argsからAppLevelFacesMessageを作成する<br/>
     *
     * @param facesMessage  FacesMessageオブジェクト
     * @param messageCode   メッセージコード
     * @param args          メッセージ引数
     * @param componentId   コンポーネントID
     */
    public AppLevelFacesMessage(FacesMessage facesMessage, String messageCode, Object[] args, String componentId) {

        super(facesMessage.getSeverity(), facesMessage.getSummary(), facesMessage.getDetail());

        // *******************************************
        // ★Severityはメッセージコードをもとに計算して再設定する★
        // ※SeverityはThymeleaf側で、メッセージ種別ごとに
        // スタイルを出し分けたいときに利用する
        // *******************************************
        Severity severity = getSeverityByMessageId(messageCode);
        if (severity != null) {
            this.setSeverity(getSeverityByMessageId(messageCode));
        }

        this.messageCode = messageCode;
        this.args = args;
        this.componentId = componentId;
    }

    /**
     * メッセージを返す<br/>    ッセージを、<br/>
     * ない場合は、概要メッセージを返す<br/>
     *
     * @return メッセージ
     */
    public String getMessage() {
        String message = getDetail();
        if (message == null) {
            message = getSummary();
        }
        return message;
    }

    /**
     * メッセージコード<br/>
     */
    private String messageCode;

    /**
     * メッセージコードを返す<br/>
     *
     * @return String メッセージコード
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * メッセージ引数<br/>
     */
    private Object[] args;

    /**
     * メッセージ引数を返す<br/>
     *
     * @return Object[] メッセージ引数
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * コンポーネントID<br/>
     */
    private String componentId;

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId コンポーネントID
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * INFOメッセージか判定
     * @return true..INFOメッセージである
     */
    public boolean isInfo() {
        return SEVERITY_INFO == this.getSeverity();
    }

    /**
     * WARNメッセージか判定
     * @return true..WARNメッセージである
     */
    public boolean isWarn() {
        return SEVERITY_WARN == this.getSeverity();
    }

    /**
     * ERRORメッセージか判定
     * @return true..ERRORメッセージである
     */
    public boolean isError() {
        return SEVERITY_ERROR == this.getSeverity();
    }

    /**
     * FATAlメッセージか判定
     * @return true..FATALメッセージである
     */
    public boolean isFatal() {
        return SEVERITY_FATAL == this.getSeverity();
    }

    /**
     * メッセージIDからSeverityTypeを設定<br/>
     * @param messageId メッセージID
     */
    private Severity getSeverityByMessageId(String messageId) {

        // メッセージIDの末尾の文字列をseverityTypeとする
        String severityType = null;
        if (messageId != null && messageId.length() > 1) {
            severityType = messageId.substring(messageId.length() - 1);
        }

        // 末尾文字列から、SEVERITYを判定する
        if (TYPE_FATAL.equals(severityType)) {
            // FATAL
            return SEVERITY_FATAL;

        } else if (TYPE_ERROR.equals(severityType)) {
            // ERROR
            return SEVERITY_ERROR;

        } else if (TYPE_WARN.equals(severityType)) {
            // WARN
            return SEVERITY_WARN;

        } else if (TYPE_INFO.equals(severityType)) {
            // INFO
            return SEVERITY_INFO;
        }
        // 上記以外はnull返却
        return null;
    }
}