package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * InquiryModel Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "searchInquiryTimeTo", comparison = "searchInquiryTimeFrom")
public class InquiryModel extends AbstractModel {

    /**
     * 正規表現エラー
     */
    public static final String MSGCD_REGULAR_EXPRESSION_ERR = "{ASI000101W}";

    /**
     * 問い合わせ種別　全て
     */
    private static final String INQUIRY_TYPE_ALL = "2";

    /**
     * 正規表現：SQL文のエスケープ文字は、入力不可とする。<br/>
     */
    public static final String REGULAR_PATTERN_NO_ESCAPE_CHAR = "[^%_\\\\]*$";

    /**
     * ページ番号
     */
    private String pageNumber;

    /**
     * 最大表示件数
     */
    private int limit;

    /**
     * ソート項目
     */
    private String orderField;

    /**
     * ソート条件
     */
    private boolean orderAsc;

    /**
     * 検索結果総件数
     */
    private int totalCount;

    /**
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     */
    public InquiryModel() {
        super();
        // 問い合わせ種別 初期値設定：全て
        searchInquiryType = INQUIRY_TYPE_ALL;
    }

    /**
     * 検索一覧<br/>
     */
    private List<InquiryModelItem> resultItems;

    /**
     * limit動的Validator target<br/>
     *
     * @return target
     */
    /************************************
     **  検索条件
     ************************************/
    /**
     * 検索条件：問い合わせ分類SEQ
     */
    private Integer searchInquiryGroupSeq;

    /**
     * 検索条件：問い合わせ状態
     */
    private String searchInquiryStatus;
    /**
     * 検索条件：問い合わせコード
     */
    @Length(max = 12)
    @HCHankaku
    private String searchInquiryCode;
    /**
     * 検索条件：問い合わせ日時(FROM)
     */
    @HCDate
    @HVDate
    private String searchInquiryTimeFrom;
    /**
     * 検索条件：問い合わせ日時(TO)
     */
    @HCDate
    @HVDate
    private String searchInquiryTimeTo;
    /**
     * 検索条件：問い合わせ者(氏名)
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @Pattern(regexp = REGULAR_PATTERN_NO_ESCAPE_CHAR, message = MSGCD_REGULAR_EXPRESSION_ERR)
    @HCZenkaku
    private String searchInquiryName;
    /**
     * 検索条件：問い合わせ者(E-Mail)
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(max = 160)
    @HCHankaku
    private String searchInquiryMail;
    /**
     * 検索条件：問い合わせ種別
     */
    private String searchInquiryType;
    /**
     * 検索条件：注文番号
     */
    @Length(max = ValidatorConstants.LENGTH_ORDER_CODE_MAXIMUM)
    @HCHankaku
    private String searchOrderCode;
    /**
     * 検索条件：電話番号
     */
    @Length(max = 11)
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}")
    @HCHankaku
    private String searchInquiryTel;
    /**
     * 検索条件：会員ID(メールアドレス)
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(max = 160)
    @HCHankaku
    private String searchMemberInfoMail;
    /**
     * 検索条件：担当者（最終担当者）
     */
    @Length(max = 18)
    @HCZenkaku
    private String searchLastRepresentative;
    /**
     * 検索条件：連携メモ
     */
    @Length(max = 100)
    private String searchCooperationMemo;

    /************************************
     **  選択肢
     ************************************/
    /**
     * 問い合わせ分類
     */
    private Map<String, String> searchInquiryGroupSeqItems;
    /**
     * 問い合わせ状態
     */
    private Map<String, String> searchInquiryStatusItems;
    /**
     * 問い合わせ種別
     */
    private Map<String, String> searchInquiryTypeItems;

    /**
     * 会員ID(メールアドレス)
     */
    private String resultMemberInfoMail;

    /**
     * 問い合わせ検索用メールアドレス
     */
    private String resultInquiryMemberInfoMail;

    /************************************
     **  コンディション機能
     ************************************/
    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }

}