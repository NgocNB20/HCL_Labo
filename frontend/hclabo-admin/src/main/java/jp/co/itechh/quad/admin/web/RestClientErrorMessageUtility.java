package jp.co.itechh.quad.admin.web;

import jp.co.itechh.quad.addressbook.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.addressbook.presentation.api.param.ServerErrorResponse;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RestClientエラーメッセージユーティリティ
 *
 * @author thangdoan
 */
@Component
public class RestClientErrorMessageUtility {

    /**
     *
     * @param msgBody メッセージ内容
     * @return エラーメッセージリスト
     */
    public List<String> getClientErrorMessage(String msgBody) {
        // クライアントエラー
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ClientErrorResponse clientError = conversionUtility.toObject(msgBody, ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        List<String> errorMsg = new ArrayList<>();
        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {
                List<ErrorContent> errorResponseList = entry.getValue();
                for (ErrorContent errorContent : errorResponseList) {
                    errorMsg.add(errorContent.getMessage());
                }
            }
        }
        return errorMsg;
    }

    /**
     *
     * @param msgBody メッセージ内容
     * @return エラーメッセージ
     */
    public String getServerErrorMessage(String msgBody) {
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ServerErrorResponse serverError = conversionUtility.toObject(msgBody, ServerErrorResponse.class);
        if ((serverError != null) && (MapUtils.isNotEmpty(serverError.getMessages()))) {
            for (List<ErrorContent> errorResponseList : serverError.getMessages().values()) {
                if (CollectionUtils.isNotEmpty(errorResponseList)) {
                    return errorResponseList.get(0).getMessage();
                }
            }
        }
        return null;
    }
}