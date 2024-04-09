/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.servlet.support.doublesubmit;

import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitToken;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitTokenSessionStore;
import jp.co.itechh.quad.web.servlet.support.RequestDataValueProcessorAdaptor;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ダブルサブミットトークン埋め込み用Processor
 *
 * @author hk57400
 */
public class DoubleSubmitTokenRequestDataValueProcessor extends RequestDataValueProcessorAdaptor {

    /** リクエスト属性キー名称：処理対象外のmethodの場合に設定 */
    private static final String ATTRIBUTE_NAME_NOT_TARGET =
                    DoubleSubmitTokenRequestDataValueProcessor.class.getName() + ".NOT_TARGET";

    /** リクエスト属性キー名称：トークンをHTMLに埋め込んでチェックする場合に設定 */
    public static final String ATTRIBUTE_NAME_OUTPUT_TOKEN =
                    DoubleSubmitTokenRequestDataValueProcessor.class.getName() + ".OUTPUT_TOKEN";

    /** リクエスト属性キー名称：最後に処理したトークンのネームスペースを設定 */
    public static final String ATTRIBUTE_NAME_LAST_NAME_SPACE =
                    DoubleSubmitTokenRequestDataValueProcessor.class.getName() + ".LAST_NAME_SPACE";

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {

        // 該当するformタグのmethodがPOSTかどうかを調べ、hidden値の出力制御用にフラグを立てる
        if (HttpMethod.POST.matches(httpMethod.toUpperCase())) {
            request.removeAttribute(ATTRIBUTE_NAME_NOT_TARGET);
        } else {
            request.setAttribute(ATTRIBUTE_NAME_NOT_TARGET, Boolean.TRUE);
        }

        return action;
    }

    @Override
    @Nullable
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {

        // GETなどでhiddenタグを埋め込むと、submit後、URLにトークンがついてしまうので回避
        if (Boolean.TRUE.equals(request.getAttribute(ATTRIBUTE_NAME_NOT_TARGET))) {
            request.removeAttribute(ATTRIBUTE_NAME_NOT_TARGET);
            return null;
        }

        // InterceptorのpostHandleで生成したトークン
        DoubleSubmitToken token = (DoubleSubmitToken) request.getAttribute(ATTRIBUTE_NAME_OUTPUT_TOKEN);
        if (token == null) {

            // Controller処理内でエラー -> FrontExceptionHandlerでcatch -> 自画面リライト とされると、
            // InterceptorのpostHandleを1度も通らないためトークンが消失してしまい、次のPOSTが必ずエラーとなる
            // （最後がリダイレクトであれば問題ないが、リライトだとController処理が発生しない）
            // この場合は、しょうがないのでここで生成
            String lastNameSpace = (String) request.getAttribute(ATTRIBUTE_NAME_LAST_NAME_SPACE);
            if (lastNameSpace == null) {
                return null;
            } else {
                // 1画面内に複数のformが存在することがあるので、生成したものを保管する
                token = DoubleSubmitTokenSessionStore.create(request, lastNameSpace);
                request.setAttribute(ATTRIBUTE_NAME_OUTPUT_TOKEN, token);
                request.removeAttribute(ATTRIBUTE_NAME_LAST_NAME_SPACE);
            }
        }

        Map<String, String> map = new HashMap<>(2); // 1だとputした時に拡張走るから2なんかな…（75％以上の利用で容量拡張）
        map.put(DoubleSubmitToken.REQUEST_PARAMETER_NAME, token.getToken());

        return map;
    }

}
