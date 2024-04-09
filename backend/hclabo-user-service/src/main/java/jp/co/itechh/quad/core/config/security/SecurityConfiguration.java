package jp.co.itechh.quad.core.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring-Security セキュリティ設定Configクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Configuration
public class SecurityConfiguration {

    /**
     * パスワードエンコーダー Bean
     * ※設定値あり
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}

