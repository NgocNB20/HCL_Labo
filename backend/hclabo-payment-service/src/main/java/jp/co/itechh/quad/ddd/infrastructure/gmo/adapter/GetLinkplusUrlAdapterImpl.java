package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmo_pg.g_pay.client.output.ErrHolder;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreateGmoPaymentUrlDto;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.IGetLinkplusUrlPaymentAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GMOリンク決済URL取得アダプター
 */
@Component
public class GetLinkplusUrlAdapterImpl implements IGetLinkplusUrlPaymentAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GetLinkplusUrlAdapterImpl.class);

    /**
     * リンク決済URL取得実施
     *
     * @param urlparamInfo
     * @param transactionInfo
     * @param customerInfo
     * @param settingId
     * @param merpay
     * @return LinkPay取引登録決済出力
     */
    @Override
    public CreateGmoPaymentUrlDto createGetLinkPaymentURL(Map<String, String> urlparamInfo,
                                                          Map<String, String> transactionInfo,
                                                          Map<String, String> customerInfo,
                                                          String settingId,
                                                          Map<String, Object> merpay) {

        Map<String, Object> jsonRequest = new HashMap<>();

        String requestUrl = PropertiesUtil.getSystemPropertiesValue("linkpay.request.link.url");

        HttpURLConnection con = null;

        CreateGmoPaymentUrlDto linkPaymentUrlResponse = new CreateGmoPaymentUrlDto();

        try {
            jsonRequest.put("geturlparam", urlparamInfo);
            jsonRequest.put("transaction", transactionInfo);
            jsonRequest.put("customer", customerInfo);
            jsonRequest.put("configid", settingId);
            if (merpay != null) {
                jsonRequest.put("merpay", merpay);
            }

            URL url = new URL(requestUrl);
            // リクエストコネクションの設定
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // ログ出力用
            Map<String, Object> jsonRequest4log = new HashMap<>(jsonRequest);
            jsonRequest4log.put("customer", "***");
            LOGGER.info("createLinkPaymentURL request:" + jsonRequest4log);

            // リクエスト送信
            ObjectMapper objectMapper = new ObjectMapper();
            try (BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8))) {
                bw.write(objectMapper.writeValueAsString(jsonRequest));
                bw.flush();
            }

            // レスポンスチェック
            StringBuilder responseSb = null;
            if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // レスポンスの取得
                responseSb = readResponseStream(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            } else {
                // エラーレスポンスの取得
                responseSb = readResponseStream(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
            }

            LOGGER.info("createLinkPaymentURL response:" + responseSb);

            try {
                linkPaymentUrlResponse = objectMapper.readValue(responseSb.toString(), CreateGmoPaymentUrlDto.class);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                // エラーリスト設定
                linkPaymentUrlResponse.setErrList(
                                objectMapper.readValue(responseSb.toString(), new TypeReference<List<ErrHolder>>() {
                                }));
            }
        } catch (Exception e) {
            LOGGER.error(AppLevelFacesMessageUtil.getAllMessage(MSGCD_LINKPAY_COM_FAIL, null).getMessage(), e);
            throw new DomainException(MSGCD_LINKPAY_COM_FAIL);
        } finally {
            //切断する
            if (con != null) {
                con.disconnect();
            }
        }

        return linkPaymentUrlResponse;
    }

    /**
     * 応答を読む
     *
     * @param inputStream
     * @return 応答文字列
     */
    private StringBuilder readResponseStream(InputStreamReader inputStream) {
        StringBuilder responseString = new StringBuilder();
        BufferedReader br = new BufferedReader(inputStream);

        String line;
        try {
            while ((line = br.readLine()) != null) {
                responseString.append(line);
                responseString.append("\r\n");
            }
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(MSGCD_LINKPAY_COM_FAIL);
        }
        return responseString;
    }
}