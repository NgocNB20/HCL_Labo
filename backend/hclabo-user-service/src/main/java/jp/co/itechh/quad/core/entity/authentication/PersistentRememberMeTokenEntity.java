package jp.co.itechh.quad.core.entity.authentication;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Entity
@Table(name = "persistent_logins")
@Data
@Component
@Scope("prototype")
public class PersistentRememberMeTokenEntity {
    @Column(name = "username")
    private String username;

    @Column(name = "series")
    @Id
    private String series;

    @Column(name = "token")
    private String token;

    @Column(name = "last_used")
    private Timestamp last_used;
}
