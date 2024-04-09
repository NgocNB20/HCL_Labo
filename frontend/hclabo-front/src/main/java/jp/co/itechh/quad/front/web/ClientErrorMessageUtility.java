package jp.co.itechh.quad.front.web;

import jp.co.itechh.quad.addressbook.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クライアントエラーメッセージユーティリティ
 *
 * @author thangdoan
 */
@Component
public class ClientErrorMessageUtility {

    /**
     *
     * @param msgBody メッセージ内容
     * @return エラーメッセージリスト
     */
    public List<String> getMessage(String msgBody) {
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
     * @return エラーメッセージリスト
     */
    public Map<String, List<String>> getErrorContent(String msgBody) {
        // クライアントエラー
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        Map<String, List<String>> errorResult = new HashMap<>();

        ClientErrorResponse clientError = conversionUtility.toObject(msgBody, ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {
                List<ErrorContent> errorResponseList = entry.getValue();
                List<String> errorMsg = new ArrayList<>();
                for (ErrorContent errorContent : errorResponseList) {
                    errorMsg.add(errorContent.getMessage());
                }
                errorResult.put(entry.getKey(), errorMsg);
            }
        }
        return errorResult;
    }
}