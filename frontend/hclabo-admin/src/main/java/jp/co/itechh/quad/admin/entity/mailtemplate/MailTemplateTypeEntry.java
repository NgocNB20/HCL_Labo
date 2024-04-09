package jp.co.itechh.quad.admin.entity.mailtemplate;

import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.transform.Transformer;
import java.io.Serializable;

/**
 * 使用可能なメールテンプレートタイプ登録
 *
 * @author tm27400
 * @version $Revision: 1.1 $
 *
 */
@Data
@Component
@Scope("prototype")
public class MailTemplateTypeEntry implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** テンプレートタイプ */
    private HTypeMailTemplateType templateType;

    /** オブジェクトから値マップへの変換に使用するトランスフォーマ */
    private Transformer transformer;

    /** 並び順　*/
    private Integer order;
}