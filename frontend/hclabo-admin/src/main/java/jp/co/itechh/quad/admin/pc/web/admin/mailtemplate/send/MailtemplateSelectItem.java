package jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class MailtemplateSelectItem implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -4826093651247094948L;

    /** メールテンプレートSEQ */
    private Integer mailTemplateSeq;

    /** メールテンプレート種別 */
    private String mailTemplateType;

    /** メールテンプレート種別名 */
    private String mailTemplateTypeName;

    /** メールテンプレート名 */
    private String mailTemplateName;

    /** メールテンプレート標準フラグ */
    private boolean mailTemplateDefaultFlag;

    /** 同一メールテンプレート種別内のテンプレート数 */
    private Integer mailTemplateTypeRowRowspan;

    /** 同一メールテンプレート内で先頭のメールテンプレートか */
    private boolean newTemplateType;

    /** テンプレート登録されていない種別かどうか */
    private boolean emptyTemplate;

}