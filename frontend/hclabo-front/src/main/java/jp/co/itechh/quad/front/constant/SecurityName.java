package jp.co.itechh.quad.front.constant;

/**
 * セキュリティ名
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class SecurityName {

    private SecurityName() {
    }

    /** ユーザー名とパスワードのセッション確認　*/
    public static final String SESSION_VERIFY_USERNAME_PASSWORD = "verifyUserNamePassword";

    /** 認証コードセッション　*/
    public static final String SESSION_CERTIFICATION_CODE = "certificationCode-session";

    /** リメンバーミーセッション　*/
    public static final String SESSION_REMEMBER_ME = "rememberMe";

    /** リメンバーミーパラメータ　*/
    public static final String REMEMBER_ME_PARAMETER = "autoLoginFlg";
}
