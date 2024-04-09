/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.ddd.exception.AssertException;
import jp.co.itechh.quad.filter.LoggingServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * アプリケーションログ出力ヘルパークラス。
 */
@Component
public class ApplicationLogUtility {

    /** コンストラクタの説明・概要   */
    @Autowired
    public ApplicationLogUtility(Environment environment) {

        // 固定フォーマット
        CREATED_FORMAT = "{0} has been created. ({1})";
        DESTROYED_FORMAT = "{0} has been destroyed.";
        STANDARD_FORMAT = "{0} {1}";

        // アクセスログ用フォーマット
        ACCESS_FORMAT = environment.getProperty("access_format", "[{0,number,000}] {1,number,000.000}sec {2} {3}");

        // プレビューアクセス用フォーマット
        PREVIEW_ACCESS_FORMAT = environment.getProperty(
                        "preview_access_format",
                        "{0} administratorId = {1}, previewType = {2}, previewTargetKey = {3}, previreTime = {4}"
                                                       );

        // ユーザ情報用フォーマット
        USER_INFO_FOMAT = environment.getProperty("user_info_format",
                                                  "{0} set userUid = {2}, memberInfoSeq = {3,number,#}"
                                                 );

        // 業務エラーログフォーマット
        LOGICAL_FORMAT = environment.getProperty("logical_format", "{0} detected logical error(s) in {1}");

        // 例外ログフォーマット
        EXCEPT_FORMAT = environment.getProperty("except_format", "{0} {2} IN {1}");

        // フリーログフォーマット
        FREE_FORMAT = environment.getProperty("free_format", "{0} {1}");

        // APIログフォーマット
        API_FORMAT = environment.getProperty("api_format", "{0} {1}");

        // アクションログ生成設定
        LOG_ACTION = environment.getProperty("log_action", Boolean.class, true);

        // アクセスログ生成設定
        LOG_ACCESS = environment.getProperty("log_access", Boolean.class, true);

        // セッションログ生成設定
        LOG_SESSION = environment.getProperty("log_session", Boolean.class, true);

        // SQL例外ログ生成設定
        LOG_SQLEXCEP = environment.getProperty("log_sql_exception", Boolean.class, true);

        // 業務エラーログ生成設定
        LOG_LOGICAL = environment.getProperty("log_logical", Boolean.class, true);

        // 例外ログ生成設定
        LOG_EXCEPT = environment.getProperty("log_except", Boolean.class, true);

        // フリーログ生成設定
        LOG_FREE = environment.getProperty("log_free", Boolean.class, true);

        // APIログ生成設定
        LOG_API = environment.getProperty("log_api", Boolean.class, true);

        // アクセスログのリダイレクション出力設定
        LOG_REDIRECTION = environment.getProperty("log_redirection", Boolean.class, true);

        // アクセスログ出力抑制設定
        NO_LOGGING_EXTENSIONS = environment.getProperty("no_logging_extensions", "ico, gif, swf").split(" *, *");
    }

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLogUtility.class);

    /** ロガー */
    protected static final Logger ACCESS_LOG = LoggerFactory.getLogger("ACCESS");

    /** ロガー */
    protected static final Logger EXCEPT_LOG = LoggerFactory.getLogger("EXCEPT");

    /** API */
    protected static final Logger API_LOG = LoggerFactory.getLogger("API");

    /** ロガー */
    protected static final Logger LOGICAL_LOG = LoggerFactory.getLogger("LOGICAL");

    /** クラスパッケージプリフィックス */
    protected final String PACKAGE_PREFIX = ApplicationLogUtility.class.getName()
                                                                       .substring(0,
                                                                                  ApplicationLogUtility.class.getName()
                                                                                                             .indexOf('.',
                                                                                                                      4
                                                                                                                     )
                                                                                 );

    /** スレッドローカルオブジェクト */
    private ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();

    /** セッション生成時ログフォーマット */
    protected final String CREATED_FORMAT;

    /** セッション破棄時ログフォーマット */
    protected final String DESTROYED_FORMAT;

    /** 追加ログ出力用ログフォーマット */
    protected final String STANDARD_FORMAT;

    /** アクセスログフォーマット */
    protected final String ACCESS_FORMAT;

    /** プレビューアクセスログフォーマット */
    protected final String PREVIEW_ACCESS_FORMAT;

    /** ユーザ情報フォーマット */
    protected final String USER_INFO_FOMAT;

    /** 業務エラーログフォーマット */
    protected final String LOGICAL_FORMAT;

    /** 例外エラーログフォーマット */
    protected final String EXCEPT_FORMAT;

    /** フリーログフォーマット */
    protected final String FREE_FORMAT;

    /** APIログフォーマット */
    protected final String API_FORMAT;

    /** APIログ用の定数 空白 */
    protected final String API_JOIN_STR = " ";

    /** APIログ用の定数 コロン */
    protected final String API_PARAM_SEPARATOR = " : ";

    /** リダイレクションリクエストをログ出力するかどうか */
    protected final boolean LOG_REDIRECTION;

    /** リクエスト受付け時にログを出力するかどうか */
    protected final boolean LOG_ACTION;

    /** レスポンス送信後にログを出力するかどうか */
    protected final boolean LOG_ACCESS;

    /** セッションの生成と破棄を出力するかどうか */
    protected final boolean LOG_SESSION;

    /** SQL例外ログを出力するかどうか */
    protected final boolean LOG_SQLEXCEP;

    /** 業務ロジック例外をログ出力するかどうか */
    protected final boolean LOG_LOGICAL;

    /** 例外ログを出力するかどうか */
    protected final boolean LOG_EXCEPT;

    /** フリーログを出力するかどうか */
    protected final boolean LOG_FREE;

    /** APIログを出力するかどうか */
    protected final boolean LOG_API;

    /** ロギング不要な拡張子一覧 */
    protected final String[] NO_LOGGING_EXTENSIONS;

    /**
     * スレッドのローカルオブジェクトを登録
     * @param request   オブジェクト
     */
    public void setHttpServletRequest(final HttpServletRequest request) {
        threadLocal.set(request);
    }

    /**
     * スレッドのローカルオブジェクトを除去
     *
     */
    public void remveHttpServletRequest() {
        threadLocal.remove();
    }

    /**
     * 対象Throwable のスタックトレースを圧縮する
     *
     * @param throwable 対象のThrowable
     * @return 対象のThrowable
     */
    public Throwable shurinkStackTrace(Throwable throwable) {
        return shurinkStackTrace(throwable, "^" + PACKAGE_PREFIX + ".+$");
    }

    /**
     * 対象Throwable のスタックトレースを圧縮する
     *
     * @param throwable         対象のThrowable
     * @param includePattern    先頭５行以外のクラスでスタックトレースに残したいクラスのFQCN をあらわす正規表現
     * @return 対象のThrowable
     */
    public Throwable shurinkStackTrace(Throwable throwable, String includePattern) {

        StackTraceElement[] stack = throwable.getStackTrace();

        // 自分自身のスタックを縮める
        throwable.setStackTrace(shurink(stack, includePattern));

        // Cause のスタックを縮める
        Throwable cause = throwable.getCause();
        while (cause != null) {
            if (cause.getStackTrace().length > 0) {
                cause.setStackTrace(shurink(cause.getStackTrace(), includePattern));
            }
            cause = cause.getCause();
        }

        return throwable;
    }

    /**
     * スタックを縮める
     *
     * @param stack             スタック要素配列
     * @param includePattern    先頭５行以外のクラスでスタックトレースに残したいクラスのFQCN をあらわす正規表現
     * @return 縮められたスタック
     */
    protected StackTraceElement[] shurink(StackTraceElement[] stack, String includePattern) {

        final List<StackTraceElement> newStackList = new ArrayList<>();
        int forceOut = 5;
        for (StackTraceElement elm : stack) {

            // 直近の５件のスタックは無条件に残す
            if (forceOut > 0) {
                newStackList.add(elm);
                forceOut--;
                continue;
            }

            // Hitmall パッケージ外のクラスはスタックを出す必要なし
            if (!elm.getClassName().matches(includePattern)) {
                continue;
            }

            // 生成クラスはスタックを出す必要なし
            if (elm.getLineNumber() < 0) {
                continue;
            }

            newStackList.add(elm);
        }

        return newStackList.toArray(new StackTraceElement[] {});
    }

    /**
     * レスポンス送信後のログを出力
     *
     * @param response  レスポンス
     * @param elapsed   処理にかかった時間
     */
    public void writeAccessLog(final LoggingServletResponse response, final long elapsed) {

        if (!LOG_ACCESS) {
            return;
        }

        //
        // 出力情報の調達
        //

        final HttpServletRequest request = getRequest();
        if (request == null) {
            return;
        }

        final String requestParam = request.getQueryString();
        String requestUri = request.getRequestURI();

        // ログ出力が不要な場合は処理を抜ける
        if (checkNoLog(requestUri)) {
            return;
        }

        if (requestParam != null) {
            requestUri += "?" + requestParam;
        }

        //
        // ログ出力
        //

        final int code = response.getResponseCode();
        String location = "=> " + response.getLocation();
        if (code != 301 && code != 302 && code != 303 && code != 307) {
            // リダイレクトではない場合
            location = "";
        } else if (!LOG_REDIRECTION) {
            // リダイレクトをロギングしない場合
            return;
        }

        ACCESS_LOG.info(MessageFormat.format(ACCESS_FORMAT, code, (elapsed / 1000d), requestUri, location));
    }

    /**
     * AssertExceptionのログを出力
     *
     * @param assertException AssertException
     */
    public void writeDddErrorLog(AssertException assertException) {

        if (!LOG_LOGICAL) {
            return;
        }

        final List<String> messageList = new ArrayList<>();

        assertException.getMessageMap()
                       .forEach((fieldName, exceptionContentList) -> exceptionContentList.forEach(
                                       exceptionContent -> messageList.add(exceptionContent.getMessage())));

        String causeClassName = null;

        int forceout = 10;
        int pos = 1;

        while (true) {
            try {
                causeClassName = assertException.getStackTrace()[pos].getClassName() + "#"
                                 + assertException.getStackTrace()[1].getMethodName();
            } catch (Exception e) {
                // ignore
                break;
            }

            pos++;

            if (!causeClassName.contains(".Abstract")) {
                break;
            }

            forceout--;
            if (forceout < 0) {
                break;
            }
        }

        this.writeLogicErrorLog(causeClassName, messageList);

    }

    /**
     * AppLevelListExceptionのログを出力
     *
     * @param appLevelListException AppLevelListException
     */
    public void writeLogicErrorLog(AppLevelListException appLevelListException) {

        if (!LOG_LOGICAL) {
            return;
        }

        //
        // 出力情報の調達
        //

        final List<AppLevelException> exceptionList = appLevelListException.getErrorList();

        final List<String> messageList = new ArrayList<>();
        for (final AppLevelException app : exceptionList) {
            messageList.add(app.getMessage());
        }

        String causeClassName = null;

        int forceout = 10;
        int pos = 1;

        while (true) {
            try {
                causeClassName = appLevelListException.getStackTrace()[pos].getClassName() + "#"
                                 + appLevelListException.getStackTrace()[1].getMethodName();
            } catch (Exception e) {
                // ignore
                break;
            }

            pos++;

            if (!causeClassName.contains(".Abstract")) {
                break;
            }

            forceout--;
            if (forceout < 0) {
                break;
            }
        }

        this.writeLogicErrorLog(causeClassName, messageList);

    }

    /**
     * 業務ロジックエラーのログを出力
     *
     * @param logicName     業務ロジック
     * @param messageList   発生したエラー一覧
     */
    public void writeLogicErrorLog(final String logicName, final List<String> messageList) {

        if (!LOG_LOGICAL) {
            return;
        }

        //
        // 出力情報の調達
        //
        final String sessionId = getJSESSIONID();

        // 業務ロジックエラー発生箇所の出力
        LOGICAL_LOG.info(MessageFormat.format(LOGICAL_FORMAT, sessionId, logicName));

        // エラー内容の出力
        for (final String message : messageList) {
            LOGICAL_LOG.info(MessageFormat.format(STANDARD_FORMAT, sessionId, "    " + message));
        }
    }

    /**
     * キャッチした例外のログを出力
     *
     * @param th キャッチした例外
     */
    public void writeExceptionLog(Throwable th) {

        String causeClassName = null;

        try {
            causeClassName = th.getStackTrace()[1].getClassName() + "#" + th.getStackTrace()[1].getMethodName();
        } catch (Exception e) {
            // ignore
            LOGGER.error("キャッチした例外のクラス名とメソッド名が取得できません。");
        }

        writeExceptionLog(causeClassName, th);

    }

    /**
     * キャッチした例外のログを出力
     *
     * @param className 発生したクラス
     * @param throwable キャッチした例外
     */
    public void writeExceptionLog(final String className, Throwable throwable) {

        if (!LOG_EXCEPT || isMarked(throwable)) {
            return;
        }

        markException(throwable);

        //
        // 出力情報の調達
        //

        final String sessionId = getJSESSIONID();

        // スタックを圧縮する
        shurinkStackTrace(throwable);

        EXCEPT_LOG.info(
                        MessageFormat.format(EXCEPT_FORMAT, sessionId, className, throwable.getClass().getSimpleName()),
                        throwable
                       );
    }

    /**
     * APIにログ出力を行える。
     *
     * @param subCategory サブカテゴリ
     * @param message     メッセージ
     */
    public void writeApiLog(String subCategory, String message) {
        writeApiLog(subCategory, null, message, null);
    }

    /**
     * APIにログ出力を行える。
     *
     * @param subCategory サブカテゴリ
     * @param url         URL
     * @param message     メッセージ
     * @param paramMap    パラメータ
     */
    public void writeApiLog(String subCategory, String url, String message, Map<String, ?> paramMap) {
        if (!LOG_API) {
            return;
        }

        // セッションID
        final String sessionId = getJSESSIONID();

        /** ログの組み立て */
        StringBuilder tmpMsg = new StringBuilder();

        // サブカテゴリ
        tmpMsg.append(subCategory);
        tmpMsg.append(API_JOIN_STR);

        // URL
        if (url != null) {
            tmpMsg.append(url);
            tmpMsg.append(API_JOIN_STR);
        }

        // メッセージ設定
        if (message != null) {
            tmpMsg.append(message);
            tmpMsg.append(API_JOIN_STR);
        }

        // パラメータ設定
        if (paramMap != null && !paramMap.isEmpty()) {
            for (Map.Entry<String, ?> entry : paramMap.entrySet()) {
                tmpMsg.append(entry.getKey());
                tmpMsg.append(API_PARAM_SEPARATOR);
                tmpMsg.append(entry.getValue());
                tmpMsg.append(API_JOIN_STR);
            }
        }

        // ログ出力
        String msg = MessageFormat.format(API_FORMAT, sessionId, tmpMsg);
        API_LOG.info(msg);
    }

    /**
     * リクエストを取得する
     *
     * @return リクエスト
     */
    protected HttpServletRequest getRequest() {
        final HttpServletRequest request = threadLocal.get();
        return request;
    }

    /**
     * セッションを取得する
     *
     * @return セッション
     */
    protected HttpSession getSession() {
        final HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        final HttpSession sess = request.getSession(false);
        return sess;
    }

    /**
     * セッションIDを取得する<br />
     * セッションIDがない場合は IP アドレスを帰す
     *
     * @return セッションID か IP アドレス
     */
    protected String getJSESSIONID() {
        final HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }

        HttpSession session = getSession();
        String sessionId;

        if (session != null) {
            sessionId = session.getId();

        } else {
            String ip = request.getRemoteAddr();
            if (ip.length() < 15) {
                char[] cs = new char[15];
                Arrays.fill(cs, ' ');
                System.arraycopy(ip.toCharArray(), 0, cs, 0, ip.length());
                ip = new String(cs);
            }

            // セッションにLBルート情報が載っているとログレイアウトがずれるが…いいか
            sessionId = "ACCESS FROM " + ip + "     ";
        }

        return sessionId;
    }

    /**
     * 既にログ出力済みであることマークする
     *
     * @param throwable マーク対象の例外
     */
    protected void markException(Throwable throwable) {
        HttpServletRequest request = getRequest();

        if (request == null) {
            return;
        }

        request.setAttribute(throwable.toString(), Boolean.TRUE);
    }

    /**
     * 既にログ出力済みかどうかを確認する
     *
     * @param throwable マークされた例外かどうかを確認する
     * @return true - マークされている
     */
    protected boolean isMarked(Throwable throwable) {

        HttpServletRequest request = getRequest();

        if (request == null) {
            return false;
        }

        if (throwable == null) {
            return true;
        }

        if (request.getAttribute(throwable.toString()) == null) {
            return false;
        }

        return true;

    }

    /**
     * ロギング不要なURIかどうかを確認する
     *
     * @param uri   確認対象 URI
     * @return true ログ不要
     */
    protected boolean checkNoLog(String uri) {

        // URI がスラッシュで終わる場合は必ずアクセスログをとる
        if (uri.endsWith("/")) {
            return false;
        }

        //
        // アクセス先URIのファイル名にロギング不要拡張子に含まれる場合
        //

        int lastDotPos = uri.lastIndexOf('.');
        if (lastDotPos == -1) {
            // ドットが含まれない場合は拡張子チェックを行わない
            return false;
        }

        String ext = uri.substring(lastDotPos + 1);

        for (String noLoggingExtension : NO_LOGGING_EXTENSIONS) {
            if (ext.equalsIgnoreCase(noLoggingExtension)) {
                return true;
            }
        }

        return false;
    }
}