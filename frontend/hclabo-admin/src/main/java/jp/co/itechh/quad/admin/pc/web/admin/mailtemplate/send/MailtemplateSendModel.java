package jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send;

import jp.co.itechh.quad.admin.annotation.validator.HVMailAddressExtended;
import jp.co.itechh.quad.admin.annotation.validator.HVMailAddressExtendedArray;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.dto.mail.MailDto;
import jp.co.itechh.quad.admin.dto.mail.MailSendDto;
import jp.co.itechh.quad.admin.entity.mailtemplate.MailTemplateTypeEntry;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation.group.SelectGroup;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation.group.SendTestGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MailtemplateSendModel extends AbstractModel {

    /** エラーコード：必須入力、タブ・半角スペースのみ入力時エラー */
    public static final String MSGCD_REQUIRED_EMPTY_FAIL = "AYM001702W";

    /** 遷移前アプリケーションより受け取った値 */
    private MailSendDto mailSendDto;

    /** テキスト版メールを選択可能か */
    private boolean selectableTextMailAddresses;

    /** HTML版メールを選択可能か */
    private boolean selectableHtmlMailAddresses;

    /** 携帯版メールを選択可能か */
    private boolean selectableMobileMailAddresses;

    /** テンプレートSEQ */
    @NotNull(message = "テンプレート を選択してください。", groups = {SelectGroup.class})
    private Integer mailTemplateSeq;

    /** テンプレート名称 */
    private String mailTemplateName;

    /** テンプレートタイプ */
    private String mailTemplateType;

    /** テンプレートタイプ名称 */
    private String mailTemplateTypeName;

    /** getMailBodyValidator() で使用する mailTemplateType.dicon の内容 */
    public List<MailTemplateTypeEntry> mailTemplateTypeList;

    /** 初期化済み */
    private boolean initialized;

    /** 見出しアイテムリスト */
    private List<MailtemplateSelectItem> indexItems;

    /** FROM アドレス */
    @NotEmpty(groups = {ConfirmGroup.class, SendTestGroup.class})
    @Length(min = 1, max = 200, groups = {ConfirmGroup.class, SendTestGroup.class})
    @HVMailAddressExtended(groups = {ConfirmGroup.class, SendTestGroup.class})
    private String fromAddress;

    /** BCCアドレス */
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(max = 200, groups = {ConfirmGroup.class, SendTestGroup.class})
    @HVMailAddressExtendedArray(groups = {ConfirmGroup.class})
    private String bccAddress;

    /** テスト送信アドレス */
    @NotEmpty(groups = {SendTestGroup.class})
    @Length(min = 1, max = 200, groups = {ConfirmGroup.class, SendTestGroup.class})
    @HVMailAddressExtended(groups = {ConfirmGroup.class, SendTestGroup.class})
    private String testAddress;

    /** 編集中のメール件名  - 動的バリデーション適用項目 */
    private String mailTitle;

    /** 編集中のメール本文  - 動的バリデーション適用項目 */
    private String mailBody;

    /** 現在編集中の版 */
    private int editingVersion;

    /** 直前まで編集していた版 */
    private int lastEditingVersion;

    /** プレビュー表示中かどうか */
    private boolean showingPreview;

    /** プレースホルダガイド */
    private String placeholderGuide;

    /** 確認画面から戻ってきたかどうか */
    private boolean backwardTransition;

    /** テストメールを送信した */
    private boolean mailSent;

    /** テストメール送信失敗 */
    private boolean mailSendFailure;

    /** 機能名称 */
    private String appName;

    /** テンプレート選択ページをスキップしたかどうか */
    private boolean skippedSelectPage;

    /** 戻り先ページ */
    private String previousPage;

    /** 再検索キー */
    private String md;

    /** 自分にもメールを送信するか */
    private boolean sendMeToo;

    /** 送信失敗情報 */
    private List<MailDto> failureList;

    /** メール送信したかどうか */
    private boolean sendOk = false;

    /** 送信処理に失敗したかどうか */
    private boolean failure;

    //
    // 画面項目用アクセッサ
    //

    /**
     * 「戻る」ページが設定されてるか
     *
     * @return true ある
     */
    public boolean isHavingBackwardPage() {
        return this.previousPage != null;
    }

    /**
     * メールの件数を返す
     *
     * @return 件数
     */
    public String getTotalToAddressCount() {
        return Integer.toString(this.mailSendDto.getMailDtoList().size());
    }

    /**
     * カテゴリ系画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isLocalNavCategory() {
        if (this.mailSendDto.getApplication() == null) {
            return false;
        }

        return MailSendDto.CATEGORY == this.mailSendDto.getApplication();
    }

    /**
     * 商品系画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isLocalNavGoods() {
        if (this.mailSendDto.getApplication() == null) {
            return false;
        }

        return MailSendDto.GOODS == this.mailSendDto.getApplication();
    }

    /**
     * 会員系画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isLocalNavMember() {
        if (this.mailSendDto.getApplication() == null) {
            return false;
        }

        return MailSendDto.MEMBER == this.mailSendDto.getApplication();
    }

    /**
     * 会員系画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isLocalNavOrder() {
        if (this.mailSendDto.getApplication() == null) {
            return false;
        }

        return MailSendDto.ORDER == this.mailSendDto.getApplication();
    }

    /**
     * ショップ計画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isLocalNavShop() {
        if (this.mailSendDto.getApplication() == null) {
            return false;
        }

        return MailSendDto.SHOP == this.mailSendDto.getApplication();
    }

    /**
     * システム系画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isLocalNavSystem() {
        if (this.mailSendDto.getApplication() == null) {
            return false;
        }

        return MailSendDto.SYSTEM == this.mailSendDto.getApplication();
    }

    /**
     * 定義されていない画面からの遷移か
     *
     * @return そうである場合 true
     */
    public boolean isKnownApplication() {

        return this.mailSendDto.getApplication() == null;
    }

}