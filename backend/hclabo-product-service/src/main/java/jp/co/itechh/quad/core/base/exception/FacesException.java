package jp.co.itechh.quad.core.base.exception;

/**
 * Logic/Service移行のリハーサル
 * 作成日：2021/02/25
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class FacesException extends RuntimeException {

    private static final long serialVersionUID = 3906091139550491188L;

    public FacesException() {
        super();
    }

    public FacesException(String message) {
        super(message);
    }

    public FacesException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacesException(Throwable cause) {
        super(cause);
    }

    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
