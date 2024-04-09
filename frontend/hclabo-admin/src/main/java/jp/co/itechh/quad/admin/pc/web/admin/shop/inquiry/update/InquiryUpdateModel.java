package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.admin.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update.validation.group.CompletionGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update.validation.group.OnceRegistMemoGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update.validation.group.OnceRegistStatusGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update.validation.group.OnceRelationMemberGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update.validation.group.SendingGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
public class InquiryUpdateModel extends AbstractModel {

    /**
     * コンストラクタ
     * 初期値の設定
     */
    public InquiryUpdateModel() {
        //        super();
        diffObjectName = ApplicationContextUtility.getBean(InquiryEntity.class).getClass().getSimpleName();
    }

    /**
     * 修正箇所の比較に使用するオブジェクト名
     */
    public final String diffObjectName;

    /**
     * 変更前問い合わせエンティティ
     */
    private InquiryEntity originalInquiryEntity;

    /**
     * 問い合わせSEQ
     */
    private Integer inquirySeq;

    /**
     * 問い合わせ詳細Dto
     */
    private InquiryDetailsDto inquiryDetailsDto;

    /**
     * 問い合わせコード
     */
    private String inquiryCode;
    /**
     * 問い合わせ分類名
     */
    private String inquiryGroupName;
    /**
     * 問い合わせ状態
     */
    @NotEmpty(groups = {OnceRegistStatusGroup.class})
    @HVItems(target = HTypeInquiryStatus.class, groups = {OnceRegistStatusGroup.class})
    private String inquiryStatus;

    /**
     * 問い合わせ者氏名(姓)
     */
    private String inquiryLastName;
    /**
     * 問い合わせ者氏名(名)
     */
    private String inquiryFirstName;
    /**
     * 問い合わせ者カナ(姓)
     */
    private String inquiryLastKana;
    /**
     * 問い合わせ者カナ(名)
     */
    private String inquiryFirstKana;
    /**
     * 問い合わせ者メールアドレス
     */
    private String inquiryMail;
    /**
     * メール送信
     */
    private boolean sendMail;
    /**
     * 前画面が確認画面であるかを判断するフラグ
     * true:確認画面
     */
    private boolean fromConfirm;

    /**
     * 画面表示モード
     */
    private String md;

    /**
     * 不正操作を判断するためのフラグ
     */
    private boolean normality;

    /**
     * 問い合わせ状態の背景色
     */
    private String bgColorConfirmInquiryStatusClass;

    /**
     * 変更前の問い合わせ状態
     */
    private String preInquiryStatus;

    /**
     * 会員SEQ
     */
    private String memberInfoSeq;

    /**
     * 会員ID
     */
    @NotEmpty(groups = {OnceRelationMemberGroup.class})
    @Length(min = 1, max = ValidatorConstants.LENGTH_MEMBERINFO_ID_MAXIMUM, groups = {OnceRelationMemberGroup.class})
    @Email(groups = {OnceRelationMemberGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.HANKAKU_REGEX, message = "{HMailAddressValidator.NOT_HANKAKU_detail}",
             groups = {OnceRelationMemberGroup.class})
    @HCHankaku
    private String memberInfoId;

    /**
     * 問い合わせ状態アイテム
     */
    private Map<String, String> inquiryStatusItems;

    /**
     * 問い合わせ種類
     */
    private String inquiryType;

    /**
     * 問い合わせ者TEL
     */

    private String inquiryTel;

    /**
     * 初回問い合わせ日時（必須）
     */

    @HCDate(pattern = "yyyy/MM/dd HH:mm:ss")
    private Timestamp firstInquiryTime;

    /**
     * 問い合わせ内容
     */
    @NotEmpty(groups = {SendingGroup.class})
    @Length(min = 1, max = 4000, groups = {SendingGroup.class})
    @HVSpecialCharacter(allowPunctuation = true)
    private String inputInquiryBody;

    /**
     * 完了報告
     */
    @NotEmpty(groups = {CompletionGroup.class})
    @Length(min = 1, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {CompletionGroup.class})
    @HVSpecialCharacter(allowPunctuation = true)
    private String inputCompletionReport;

    /**
     * 管理メモ
     */
    @Length(min = 0, max = 2000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {OnceRegistMemoGroup.class})
    @HVSpecialCharacter(allowPunctuation = true)
    private String memo;

    /**
     * 連携メモ
     */
    @Length(min = 0, max = 100, groups = {OnceRegistMemoGroup.class})
    @HVSpecialCharacter(allowPunctuation = true)
    private String cooperationMemo;

    /**
     * お問合せメール送信フラグ
     */
    private boolean sendMailFlag = true;

    /**
     * 完了報告メール送信フラグ
     */
    private boolean completionReportFlag = false;

    /**
     * 問い合わせ内容リスト
     */

    private List<InquiryDetailItem> inquiryDetailItems;

    /**
     * 返信・完了判定フラグ
     */

    private String inquiryStatusFlg;
    /**
     * 新規問い合わせ判定フラグ
     */
    private boolean newInquiryFlg;

    /**
     * 問い合わせ詳細DTO(確認画面用)
     */

    private InquiryDetailsDto confirmInquiryDetailsDto;

    private List<InquiryDetailItem> confirmInquiryDetailItems;

    // items 項目 start

    /**
     * 連番
     */
    private Integer inquiryVersionNo;
    /**
     * 発信者種別
     */
    private String requestType;
    /**
     * 問い合わせ日時
     *
     * @see InquiryDetailItem#inquiryTime
     */
    private Timestamp inquiryTime;
    /**
     * 問い合わせ内容
     *
     * @see InquiryDetailItem#
     */
    private String inquiryBody;
    /**
     * 部署名
     */
    private String divisionName;
    /**
     * 担当者
     */
    private String representative;
    /**
     * 連絡先TEL
     */
    private String contactTel;
    /**
     * 処理担当者
     */
    private String operator;
    /**
     * 表示用（問い合わせ者）
     */
    private String inquiryMan;
    /**
     * 問い合わせ内容の背景色(問い合わせ者)
     */
    private String bgColorInquiryDetailManClass;
    /**
     * 問い合わせ内容の背景色(問い合わせ日時)
     */
    private String bgColorInquiryDetailTimeClass;
    /**
     * 問い合わせ内容の背景色(問い合わせ内容)
     */
    private String bgColorInquiryDetailBodyClass;

    private boolean resetMemberInfoId = false;

    // items 項目 end

    /************************************
     **  動的バリデータ
     ************************************/

    /**
     * 会員詳細画面遷移有無判定
     *
     * @return 会員詳細画面遷移有無判定結果
     */
    public boolean isMemberTransition() {
        return memberInfoSeq != null && (!memberInfoSeq.equals("0"));
    }

    /**
     * 会員解除ボタン表示有無判定
     *
     * @return true:表示する false:表示しない
     */
    public boolean isDispMemberDeleteButton() {
        return memberInfoSeq != null && (!memberInfoSeq.equals("0"));
    }

    /**
     * 問合せ状態が完了かどうかを判定する
     *
     * @return true..完了 / false..完了以外
     */
    public boolean isCompletion() {
        return HTypeInquiryStatus.COMPLETION.getValue().equals(getInquiryStatus());
    }
}
