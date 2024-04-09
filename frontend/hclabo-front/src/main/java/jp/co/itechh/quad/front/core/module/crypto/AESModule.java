package jp.co.itechh.quad.front.core.module.crypto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 暗号化/復号クラス<br/>
 * AES暗号を利用した暗号化/復号を行う<br/>
 *
 * @author matsumoto
 * @version $Revision: 1.1 $
 *
 */
@Component
public class AESModule {

    /** 暗号化アルゴリズム AES */
    protected static final String ALGORITHM_AES = "AES";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AESModule.class);

    /**
     * 指定されたキーを利用して暗号化を行う<br/>
     * パラメータに 空文字, null が指定されている場合、 nullを返却します。<br/>
     * キーが2の倍数でない場合, nullを返却します。<br/>
     *
     * @param plainText 暗号化対象の文字列
     * @param keyString 16進文字列化されたキー
     * @return 暗号化された結果
     */
    public String encrypt(String plainText, String keyString) {
        // 引数に 空文字, null が含まれる場合、nullを返却
        if (StringUtils.isEmpty(plainText) || StringUtils.isEmpty(keyString) || keyString.length() % 2 != 0) {
            return null;
        }

        try {
            // 暗号化対象文字列の先頭に付与する乱数(2byte)を取得
            Random random = SecureRandom.getInstance("SHA1PRNG");
            byte[] prefix = new byte[2];
            random.nextBytes(prefix);

            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.ENCRYPT_MODE, toKey(keyString));
            cipher.update(prefix);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(encrypted));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 指定されたアルゴリズムが存在しない場合に発生
            // 環境に問題がなければ発生することはない
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error("例外処理が発生しました", e);
            // パディングがサポートされていない場合に発生
            // 環境に問題がなければ発生することはない
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error("例外処理が発生しました", e);
            // データサイズがキーの倍数でない場合に発生
            // Cipherにパディングを任せるので発生することはない
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            LOGGER.error("例外処理が発生しました", e);
            // データがパディングできなかった場合に発生
            // 環境に問題がなければ発生することはない
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 鍵が無効な場合に発生
            throw new RuntimeException(e);
        }
    }

    /**
     * 指定されたキーを利用して復号を行う<br/>
     * パラメータに 空文字, null が指定されている場合、 nullを返却します。<br/>
     * キーが2の倍数でない場合, nullを返却します。<br/>
     * パスワードの復号に失敗した場合、nullを返却します<br/>
     *
     * @param encryptedText 暗号化された文字列
     * @param keyString 16進文字列化されたキー
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
            // DBに登録されているパスワードが復号できなかった場合に発生する。
            // 復号できない原因としては、以下が考えられる。
            // データ移行時に生成したパスワードが正しくない
            // ・パスワードが暗号化キーの倍数でない。
            // ・パスワードの長さが奇数
            // ・暗号化されたパスワードが16進数から10進数に変換できない。
            // ・AES暗号がサポートされていない
            LOGGER.error("パスワードの復号に失敗しました", e);
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
}