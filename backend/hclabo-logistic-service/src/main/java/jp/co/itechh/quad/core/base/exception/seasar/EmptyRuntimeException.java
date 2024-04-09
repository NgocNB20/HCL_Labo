package jp.co.itechh.quad.core.base.exception.seasar;

/**
 * 機能概要：＜修正要＞
 * 作成日：2021/02/25
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class EmptyRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 4625805280526951642L;

    private String targetName;

    /**
     * {@link EmptyRuntimeException}を作成します。
     *
     * @param targetName
     */
    public EmptyRuntimeException(String targetName) {
        super(targetName);
        this.targetName = targetName;
    }

    /**
     * ターゲット名を返します。
     *
     * @return
     */
    public String getTargetName() {
        return targetName;
    }

}
