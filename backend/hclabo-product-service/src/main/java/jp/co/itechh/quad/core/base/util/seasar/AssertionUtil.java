package jp.co.itechh.quad.core.base.util.seasar;

import jp.co.itechh.quad.core.base.exception.seasar.EmptyRuntimeException;

/**
 * 機能概要：＜修正要＞
 * 作成日：2021/02/26
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class AssertionUtil {

    /**
     * インスタンスを構築します。
     */
    protected AssertionUtil() {
    }

    /**
     * <code>null</code>でないことを表明します。
     *
     * @param message
     * @param obj
     * @throws NullPointerException
     *             <code>null</code>の場合。
     */
    public static void assertNotNull(String message, Object obj) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 文字列が空あるいは<code>null</code>でないことを表明します。
     *
     * @param message
     * @param s
     * @throws EmptyRuntimeException
     *             文字列が空あるいは<code>null</code>の場合。
     */
    public static void assertNotEmpty(String message, String s) throws EmptyRuntimeException {
        if (StringUtil.isEmpty(s)) {
            throw new EmptyRuntimeException(message);
        }
    }

}