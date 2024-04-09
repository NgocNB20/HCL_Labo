package jp.co.itechh.quad.core.web;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Objects;

/**
 * リクエストパラメーターユーティル
 *
 * @author Doan Thang (VJP)
 */
@Component
public class RequestParamsUtility {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestParamsUtility.class);

    /**
     * Cookie 名：端末識別番号  accessUid
     */
    protected static final String COOKIE_NAME_ACCESSUID = "AUI";

    /**
     * 暗号化アルゴリズム AES
     */
    protected static final String ALGORITHM_AES = "AES";

    /**
     * 端末識別番号の暗号化キーをシステムプロパティから取得するためのキー
     */
    protected static final String ACCESS_UID_ENCRYPT_KEY = "accessUidEncryptKey";

    /**
     * セッションIDの取得
     *
     * @return セッションID
     */
    public String getSessionId() {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getSession().getId();
    }

    /**
     * 端末識別番号の取得
     *
     * @return 端末識別番号
     */
    public String getAccessUid() {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();

        // クッキーから取得
        String accessUid = null;
        Cookie cookie = getCookie(request, COOKIE_NAME_ACCESSUID);
        if (cookie != null) {
            try {
                // 暗号化された端末識別番号
                String encryptedAccessUid = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);

                // 復号
                accessUid = decryptAccessUid(encryptedAccessUid);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                return null;
            }
        }
        return accessUid;
    }

    /**
     * 暗号化された端末識別番号の復号
     *
     * @param encryptedAccessUid 暗号化された端末識別番号の文字列
     * @return 復号した端末識別番号
     */
    public String decryptAccessUid(String encryptedAccessUid) {
        String keyString = PropertiesUtil.getSystemPropertiesValue(ACCESS_UID_ENCRYPT_KEY);
        return decrypt(encryptedAccessUid, keyString);
    }

    /**
     * 指定されたキーを利用して復号を行う<br/>
     * パラメータに 空文字, null が指定されている場合、 nullを返却します。<br/>
     * キーが2の倍数でない場合, nullを返却します。<br/>
     * パスワードの復号に失敗した場合、nullを返却します<br/>
     *
     * @param encryptedText 暗号化された文字列
     * @param keyString     16進文字列化されたキー
     * @return 復号された文字列
     */
    public String decrypt(String encryptedText, String keyString) {
        // 引数に 空文字, null が含まれる場合、nullを返却
        if (StringUtils.isEmpty(encryptedText) || StringUtils.isEmpty(keyString) || keyString.length() % 2 != 0) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.DECRYPT_MODE, toKey(keyString));

            // 先頭2byteは暗号化時に付与されたものなので除去する
            byte[] decryptedByte = cipher.doFinal(toByteArray(encryptedText));
            byte[] plainTextByte = new byte[decryptedByte.length - 2];
            System.arraycopy(decryptedByte, 2, plainTextByte, 0, decryptedByte.length - 2);
            return new String(plainTextByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            // DBに登録されているパスワードが復号できなかった場合に発生する。
            // 復号できない原因としては、以下が考えられる。
            // データ移行時に生成したパスワードが正しくない
            // ・パスワードが暗号化キーの倍数でない。
            // ・パスワードの長さが奇数
            // ・暗号化されたパスワードが16進数から10進数に変換できない。
            // ・AES暗号がサポートされていない
            return null;
        }
    }

    /**
     * 16進文字列化されたキーを共通鍵オブジェクトに変換する<br/>
     *
     * @param keyString 16進文字列化されたキー
     * @return 共通鍵オブジェクト
     */
    protected Key toKey(String keyString) {
        return new SecretKeySpec(toByteArray(keyString), ALGORITHM_AES);
    }

    /**
     * 16進文字列をバイト配列に変換する
     *
     * @param hexString 16進文字列
     * @return byte配列
     */
    protected byte[] toByteArray(String hexString) {
        return DatatypeConverter.parseHexBinary(hexString);
    }

    /**
     * クッキー取得<br/>
     *
     * @param request    リクエスト
     * @param cookieName クッキー名
     * @return Cookie クッキー情報
     */
    protected Cookie getCookie(HttpServletRequest request, String cookieName) {
        // 現行HM HttpUtilityから移植
        // クッキーから取得
        Cookie[] cookieArray = request.getCookies();
        if (cookieArray != null) {
            for (Cookie cookie : cookieArray) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
}