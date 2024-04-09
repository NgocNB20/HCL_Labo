package jp.co.itechh.quad.front.pc.web.front.certification;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 認証モデル
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Data
public class CertificationModel extends AbstractModel {

    /**
     * 認証コード
     */
    @NotEmpty(message = "{CODE-001-001-A-E}")
    @Length(min = 6, max = 6)
    @HCHankaku
    private String certificationCode;

}
