/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.doublesubmit;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ダブルサブミットチェック用トークンをセッションに保持して管理するクラス
 *
 * @author hk57400
 */
public class DoubleSubmitTokenSessionStore {

    /** セッション属性キー名称：トークンを格納するホルダー */
    private static final String ATTRIBUTE_NAME_HOLDER = DoubleSubmitTokenSessionStore.class.getName() + ".HOLDER";

    /**
     * 指定したネームスペースのトークンを生成
     *
     * @param request HttpServletRequest
     * @param nameSpace ネームスペース
     * @return 生成したトークン
     */
    public static DoubleSubmitToken create(final HttpServletRequest request, final String nameSpace) {

        // 新しいトークンを生成
        DoubleSubmitToken newToken = new DoubleSubmitToken(nameSpace, UUID.randomUUID().toString());

        // セッションへ格納
        set(request, newToken);

        return newToken;
    }

    /**
     * 指定したトークンを格納
     *
     * @param request HttpServletRequest
     * @param token トークン
     */
    public static void set(HttpServletRequest request, DoubleSubmitToken token) {

        // セッションへ格納
        HttpSession session = getSession(request);
        Object mutex = getMutex(session);
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (mutex) {
            Map<String, DoubleSubmitToken> holderMap = getHolder(session);
            holderMap.put(token.getNameSpace(), token);
        }
    }

    /**
     * 指定したネームスペースのトークンを削除
     *
     * @param request HttpServletRequest
     * @param nameSpace ネームスペース
     * @return 削除したトークン
     */
    public static DoubleSubmitToken remove(final HttpServletRequest request, final String nameSpace) {

        DoubleSubmitToken removeToken;

        // セッションから削除
        HttpSession session = getSession(request);
        Object mutex = getMutex(session);
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (mutex) {
            Map<String, DoubleSubmitToken> holderMap = getHolder(session);
            removeToken = holderMap.remove(nameSpace);
        }

        return removeToken;
    }

    /**
     * セッションを取得
     *
     * @return HttpSession
     */
    private static HttpSession getSession(final HttpServletRequest request) {
        // Interceptorは本筋外の差し込み処理にあたるため、勝手に新しいセッションは生成しない
        // Controller処理前後なので、取れないことはありえないはず（取れないようなことがあればNullPointerで落とす）
        return request.getSession(false);
    }

    /**
     * セッションを一意に識別できるオブジェクトを取得
     *
     * @param session HttpSession
     * @return Mutex
     */
    private static Object getMutex(final HttpSession session) {
        return WebUtils.getSessionMutex(session);
    }

    /**
     * トークンを格納するホルダーを取得
     *
     * @param session HttpSession
     * @return ホルダーMap
     */
    private static Map<String, DoubleSubmitToken> getHolder(final HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<String, DoubleSubmitToken> holderMap =
                        (Map<String, DoubleSubmitToken>) session.getAttribute(ATTRIBUTE_NAME_HOLDER);
        if (holderMap == null) {
            holderMap = new HashMap<>();
            session.setAttribute(ATTRIBUTE_NAME_HOLDER, holderMap);
        }

        return holderMap;
    }

}
