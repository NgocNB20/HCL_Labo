/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.doublesubmit.interceptor;

import jp.co.itechh.quad.front.util.async.AsyncUtil;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitCheckInfo;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitCheckInfoCache;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitException;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitToken;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitTokenSessionStore;
import jp.co.itechh.quad.web.servlet.support.doublesubmit.DoubleSubmitTokenRequestDataValueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ダブルサブミットチェック用Interceptor
 *
 * @author hk57400
 */
public class DoubleSubmitCheckInterceptor implements HandlerInterceptor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DoubleSubmitCheckInterceptor.class);

    /** リクエスト属性キー名称：画面再描画を伴わないPOST処理で、トークンを維持する必要がある場合に設定 */
    private static final String ATTRIBUTE_NAME_PRESERVE_TOKEN =
                    DoubleSubmitCheckInterceptor.class.getName() + ".PRESERVE_TOKEN";

    /** チェック制御情報のキャッシュ */
    private final DoubleSubmitCheckInfoCache checkInfoCache = new DoubleSubmitCheckInfoCache();

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {

        // 本メソッドは、Controllerの処理が始まる前に呼ばれる

        logDebug("Controller実行前処理 開始", handler);

        // ファイルDL処理などで非同期レスポンスを返すメソッドの場合、Spring仕様として
        // レスポンスのストリーム完了後に、DispatcherServletに再度ディスパッチされてしまう
        // この際、再度Interceptorが挟まって2回目のpreHandleが実行されるため、対策
        if (AsyncUtil.isDispatchedToResume(request)) {
            logDebug("  -> チェック 対象外（非同期処理による2回目の実行）", handler);
            return true;
        }

        // メソッド以外が来ることは想定していない ※trueを返してController処理は継続
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        DoubleSubmitCheckInfo checkInfo = checkInfoCache.getInfo(handlerMethod);

        removeAndCheck(request, handlerMethod, checkInfo);

        logDebug("Controller実行前処理 終了", handler);

        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           @Nullable final ModelAndView modelAndView) {

        // 本メソッドは、Controllerの処理が終わった後に呼ばれる（Viewレンダリング前）
        // Controllerでcatchされないエラーが発生した際は、実行されない
        // ・正常終了               ⇒ 通る
        // ・Validationエラー       ⇒ 通る
        // ・Controllerでリダイレクト ⇒ 通る
        // ・Controllerでエラースロー ⇒ 通らない   ※このタイミングでは生成されず、リダイレクト先のpostHandle時に生成される

        logDebug("Controller実行後処理 開始", handler);

        // メソッド以外が来ることは想定していない
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // preHandleで生成せず、なるべくレンダリング直前のpostHandleで生成 ※多重POST時に、preHandleで消されるのを防ぐため
        DoubleSubmitToken preserveToken = (DoubleSubmitToken) request.getAttribute(ATTRIBUTE_NAME_PRESERVE_TOKEN);
        if (preserveToken == null) {
            DoubleSubmitCheckInfo checkInfo = checkInfoCache.getInfo(handlerMethod);
            create(request, handlerMethod, checkInfo);
        } else {
            preserve(request, handlerMethod, preserveToken);
        }

        logDebug("Controller実行後処理 終了", handler);
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                @Nullable final Exception ex) {

        // 本メソッドは、Viewの処理が終わった後に呼ばれる
        // Controller・Viewレンダリングのどちらでエラーが発生した際も、実行される
        // exには、レンダリング中に発生した例外が設定（Controllerで発生した例外は、ExceptionHandlerで処理されてここには入らない）
        // ・正常終了               ⇒ 通る＆ex:null
        // ・Validationエラー       ⇒ 通る＆ex:null
        // ・Controllerでリダイレクト ⇒ 通る＆ex:null
        // ・Controllerでエラースロー ⇒ 通る＆ex:null
        // ・Viewでエラースロー       ⇒ 通る＆ex:設定あり

        // TERASOLUNAはアノテーションで更新指示するまで同一トークンで動くため、このタイミングでエラーが発生したらPOSTされたトークンを消している
        // HIT-MALLではpreHandle時に必ず消して毎回更新するようにしているので、処理なし
        //   ※postHandle時に新しく生成したトークンは、消さない（中途半端に画面レンダリングされることがあり、処理続行できる時があるため）
    }

    /**
     * トークン生成
     *
     * @param request HttpServletRequest
     * @param handlerMethod 実行メソッド
     * @param checkInfo チェック制御情報
     */
    private void create(final HttpServletRequest request,
                        HandlerMethod handlerMethod,
                        DoubleSubmitCheckInfo checkInfo) {

        // 基本的に無条件で毎回新トークンを生成・更新する方針
        // 生成対象外にする場合は、設定でexclude-path-patternsにパス登録する
        // 常に生成はしておかないと、特定のメソッドだけチェックを除外ということができない
        //   ※該当メソッド通過後に次のPOSTで、トークンがセットされていないのでチェックできずエラーとなる
        //    リンク決済のコールバックPOSTだけチェックを外したい etc
        DoubleSubmitToken newToken = DoubleSubmitTokenSessionStore.create(request, checkInfo.getNameSpace());

        // hiddenへ埋め込み用に受け渡し
        request.setAttribute(DoubleSubmitTokenRequestDataValueProcessor.ATTRIBUTE_NAME_OUTPUT_TOKEN, newToken);

        // 新トークン生成完了により、トークン消失対策不要
        request.removeAttribute(DoubleSubmitTokenRequestDataValueProcessor.ATTRIBUTE_NAME_LAST_NAME_SPACE);

        logDebug(String.format("  -> 生成 %s", newToken.getToken()), handlerMethod);
    }

    /**
     * トークン維持
     *
     * @param request HttpServletRequest
     * @param handlerMethod 実行メソッド
     * @param preserveToken 維持するトークン
     */
    private void preserve(HttpServletRequest request, HandlerMethod handlerMethod, DoubleSubmitToken preserveToken) {

        // 保持していたトークンを削除し、セッションに格納
        request.removeAttribute(ATTRIBUTE_NAME_PRESERVE_TOKEN);
        DoubleSubmitTokenSessionStore.set(request, preserveToken);

        // ファイルDL処理やAjaxなどの画面描画更新を伴わない処理用のため、hiddenへ埋め込み用の受け渡しは不要
        // 画面レンダリングができる状況であれば、このメソッドではなくcreate()メソッドを通すべき

        // トークン維持完了により、トークン消失対策不要
        request.removeAttribute(DoubleSubmitTokenRequestDataValueProcessor.ATTRIBUTE_NAME_LAST_NAME_SPACE);

        logDebug(String.format("  -> 維持 %s", preserveToken.getToken()), handlerMethod);
    }

    /**
     * トークン削除＆チェック
     *
     * @param request HttpServletRequest
     * @param handlerMethod 実行メソッド
     * @param checkInfo チェック制御情報
     */
    private void removeAndCheck(final HttpServletRequest request,
                                HandlerMethod handlerMethod,
                                DoubleSubmitCheckInfo checkInfo) {

        // セッションから取得しつつ、削除
        String nameSpace = checkInfo.getNameSpace();
        DoubleSubmitToken sessionToken = DoubleSubmitTokenSessionStore.remove(request, nameSpace);

        // トークン消失対策で、最後に処理したネームスペースを一時保管
        // 詳細は、保管値を利用しているDoubleSubmitTokenRequestDataValueProcessor内のコメントを参照
        request.setAttribute(DoubleSubmitTokenRequestDataValueProcessor.ATTRIBUTE_NAME_LAST_NAME_SPACE, nameSpace);

        // 無効フラグが立っている場合
        // ・RestController ⇒ DoubleSubmitCheckInfo で無効フラグを立てている
        // ・その他 ⇒ @DoubleSubmitCheck アノテーションで個別指定可能
        if (checkInfo.isDisable()) {
            logDebug("  -> チェック 対象外（無効指定）", handlerMethod);
            return;
        }

        // POST時は、基本的に無条件でチェックする方針
        // チェックを外したい場合は、設定でexclude-path-patternsにパス登録するか、無効フラグを立てる
        String method = request.getMethod().toUpperCase();
        if (!HttpMethod.POST.matches(method)) {
            logDebug(String.format("  -> チェック 対象外（%s）", method), handlerMethod);
            return;
        }

        // 送信されたトークン
        DoubleSubmitToken submitToken = getSubmitToken(request);

        if (LOGGER.isDebugEnabled()) {
            logDebug(String.format("  -> チェック SESSION: %s, POST: %s",
                                   sessionToken == null ? "null" : sessionToken.getToken(),
                                   request.getParameter(DoubleSubmitToken.REQUEST_PARAMETER_NAME)
                                  ), handlerMethod);
        }

        // チェック
        if (!submitToken.isSame(sessionToken)) {
            processError();
        }

        // Viewが指定されていない場合、ファイルDL処理やAjaxなど、画面描画更新を伴わない処理になる
        // この場合、旧トークンを保持し続けなければならない（同一画面内で、2回目のPOSTがエラーとなってしまうため）
        if (!hasView(handlerMethod)) {
            request.setAttribute(ATTRIBUTE_NAME_PRESERVE_TOKEN, sessionToken);
        }
    }

    /**
     * クライアント側から送信されたトークンを取得
     *
     * @param request HttpServletRequest
     * @return トークン
     */
    private DoubleSubmitToken getSubmitToken(final HttpServletRequest request) {
        return new DoubleSubmitToken(request.getParameter(DoubleSubmitToken.REQUEST_PARAMETER_NAME));
    }

    /**
     * チェック例外射出
     */
    private void processError() {
        throw new DoubleSubmitException();
    }

    /**
     * Viewを戻すメソッドかどうか判定
     *
     * @see <a href="https://spring.pleiades.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/return-types.html">公式リファレンス</a>
     * @param handlerMethod 実行メソッド
     * @return true:Viewを戻すメソッド, false:Viewを戻さないメソッド
     */
    private boolean hasView(HandlerMethod handlerMethod) {

        // まずはアノテーション判定
        // 上記JavaDoc記載の型の中から、以下アノテーションがついているものはViewではないと判断
        // ・@ResponseBody
        ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {
            return false;
        }

        // 次に戻り値の型判定
        // 上記JavaDoc記載の型の中から、以下はView利用と判断
        // ・String
        // ・View
        // × Map MapはRequestToViewNameTranslatorを介して暗黙的に決定されたビュー名になるとのことだが、QUADではAjax戻り値として使っている
        // ・Model
        // ・ModelAndView
        Class<?> returnClazz = handlerMethod.getReturnType().getParameterType();
        if (String.class.isAssignableFrom(returnClazz)) {
            return true;
        }
        if (View.class.isAssignableFrom(returnClazz)) {
            return true;
        }
        if (Model.class.isAssignableFrom(returnClazz)) {
            return true;
        }
        if (ModelAndView.class.isAssignableFrom(returnClazz)) {
            return true;
        }

        return false;
    }

    /**
     * デバッグログ出力
     *
     * @param value ログ内容
     * @param handler 実行メソッド
     */
    private void logDebug(String value, final Object handler) {

        if (!LOGGER.isDebugEnabled()) {
            return;
        }

        LOGGER.debug(String.format("[token check] [%s] %s", handler, value));
    }

}
