package jp.co.itechh.quad.core.base.util.seasar;

public class Base64Util {
    private static final char[] ENCODE_TABLE =
                    {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                                    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
                                    '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    private static final char PAD = '=';

    private static final byte[] DECODE_TABLE = new byte[128];

    static {
        for (int i = 0; i < DECODE_TABLE.length; i++) {
            DECODE_TABLE[i] = Byte.MAX_VALUE;
        }
        for (int i = 0; i < ENCODE_TABLE.length; i++) {
            DECODE_TABLE[ENCODE_TABLE[i]] = (byte) i;
        }
    }

    /**
     * Base64でエンコードします。
     *
     * @param inData
     * @return エンコードされたデータ
     */
    public static String encode(final byte[] inData) {
        if (inData == null || inData.length == 0) {
            return "";
        }
        int mod = inData.length % 3;
        int num = inData.length / 3;
        char[] outData = null;
        if (mod != 0) {
            outData = new char[(num + 1) * 4];
        } else {
            outData = new char[num * 4];
        }
        for (int i = 0; i < num; i++) {
            encode(inData, i * 3, outData, i * 4);
        }
        switch (mod) {
            case 1:
                encode2pad(inData, num * 3, outData, num * 4);
                break;
            case 2:
                encode1pad(inData, num * 3, outData, num * 4);
                break;
        }
        return new String(outData);
    }

    private static void encode(final byte[] inData, final int inIndex, final char[] outData, final int outIndex) {

        int i = ((inData[inIndex] & 0xff) << 16) + ((inData[inIndex + 1] & 0xff) << 8) + (inData[inIndex + 2] & 0xff);
        outData[outIndex] = ENCODE_TABLE[i >> 18];
        outData[outIndex + 1] = ENCODE_TABLE[(i >> 12) & 0x3f];
        outData[outIndex + 2] = ENCODE_TABLE[(i >> 6) & 0x3f];
        outData[outIndex + 3] = ENCODE_TABLE[i & 0x3f];
    }

    private static void encode2pad(final byte[] inData, final int inIndex, final char[] outData, final int outIndex) {

        int i = inData[inIndex] & 0xff;
        outData[outIndex] = ENCODE_TABLE[i >> 2];
        outData[outIndex + 1] = ENCODE_TABLE[(i << 4) & 0x3f];
        outData[outIndex + 2] = PAD;
        outData[outIndex + 3] = PAD;
    }

    private static void encode1pad(final byte[] inData, final int inIndex, final char[] outData, final int outIndex) {

        int i = ((inData[inIndex] & 0xff) << 8) + (inData[inIndex + 1] & 0xff);
        outData[outIndex] = ENCODE_TABLE[i >> 10];
        outData[outIndex + 1] = ENCODE_TABLE[(i >> 4) & 0x3f];
        outData[outIndex + 2] = ENCODE_TABLE[(i << 2) & 0x3f];
        outData[outIndex + 3] = PAD;
    }

}