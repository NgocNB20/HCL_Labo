package jp.co.itechh.quad.core.utility;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * ワンタイムパスワードユーティリティークラス.
 */
@Component
public class OneTimePasswordUtility {

    private static final String CRYPTO = "HmacSHA1";

    private static final int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

    private static final int CERTIFICATION_LENGTH = 6;

    public String generateTOTP(String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] msg = ByteBuffer.allocate(8).putLong(Instant.now().getEpochSecond()).array();
        byte[] hash = hmacSha(CRYPTO, secret.getBytes(), msg);

        int offset = hash[19] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) |
            ((hash[offset + 1] & 0xff) << 16) |
            ((hash[offset + 2] & 0xff) << 8) |
            (hash[offset + 3] & 0xff);

        return String.format("%06d", binary % DIGITS_POWER[CERTIFICATION_LENGTH]);
    }

    private byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(crypto);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "RAW");
        hmac.init(keySpec);
        return hmac.doFinal(text);
    }
}
