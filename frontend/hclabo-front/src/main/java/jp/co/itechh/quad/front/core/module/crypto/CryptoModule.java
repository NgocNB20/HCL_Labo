package jp.co.itechh.quad.front.core.module.crypto;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import org.springframework.stereotype.Component;

/**
 * 暗号化/暗号化/復号モジュールクラス
 *
 * @author matsumoto
 *
 */
@Component
public class CryptoModule {
    /** 端末識別番号の暗号化キーをシステムプロパティから取得するためのキー */
    protected static final String ACCESS_UID_ENCRYPT_KEY = "accessUidEncryptKey";

    /** 顧客IDの暗号化キーをシステムプロパティから取得するためのキー */
    protected static final String CUSTOMER_ID_ENCRYPT_KEY = "customerIdEncryptKey";

    /**
     * 端末識別番号の暗号化
     *
     * @param accessUid 端末識別番号
     * @return 端末識別番号を暗号化した文字列
     */
    public String encryptAccessUid(String accessUid) {
        String keyString = PropertiesUtil.getSystemPropertiesValue(ACCESS_UID_ENCRYPT_KEY);
        return ApplicationContextUtility.getBean(AESModule.class).encrypt(accessUid, keyString);
    }

    /**
     * 暗号化された端末識別番号の復号
     *
     * @param encryptedAccessUid 暗号化された端末識別番号の文字列
     * @return 復号した端末識別番号
     */
    public String decryptAccessUid(String encryptedAccessUid) {
        String keyString = PropertiesUtil.getSystemPropertiesValue(ACCESS_UID_ENCRYPT_KEY);
        return ApplicationContextUtility.getBean(AESModule.class).decrypt(encryptedAccessUid, keyString);
    }

    /**
     * 顧客IDの暗号化
     *
     * @param customerId 顧客ID
     * @return 顧客IDを暗号化した文字列
     */
    public String encryptCustomerId(String customerId) {
        String keyString = PropertiesUtil.getSystemPropertiesValue(CUSTOMER_ID_ENCRYPT_KEY);
        return ApplicationContextUtility.getBean(AESModule.class).encrypt(customerId, keyString);
    }

    /**
     * 暗号化された顧客IDの復号
     *
     * @param encryptedCustomerId 暗号化された顧客IDの文字列
     * @return 復号した顧客ID
     */
    public String decryptCustomerId(String encryptedCustomerId) {
        String keyString = PropertiesUtil.getSystemPropertiesValue(CUSTOMER_ID_ENCRYPT_KEY);
        return ApplicationContextUtility.getBean(AESModule.class).decrypt(encryptedCustomerId, keyString);
    }
}
