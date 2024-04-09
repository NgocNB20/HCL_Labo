/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DownloadBottomGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DownloadTopGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 会員検索モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "endDate", comparison = "startDate",
                     groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
public class MemberInfoModel extends AbstractModel {

    /**
     * 不正操作：AMX000102
     */
    public static final String MSGCD_ILLEGAL_OPERATION = "AMX000102";

    /**
     * CSV選択DL時チェックされているデータがない：AMX000103
     */
    public static final String MSGCD_NOT_SELECTED_DATE = "AMX000103";

    /**
     * 期間プルダウンが選択されていて、日付に入力がない時：AMX000104W
     */
    public static final String MSGCD_PERIOD_REQUIRED = "AMX000104W";

    /**
     * 正規表現エラー：AMX000105W
     */
    public static final String MSGCD_REGULAR_EXPRESSION_ERR = "AMX000105W";

    /**
     * 選択なしメッセージ：AMX000101
     */
    public static final String MSGCD_NO_CHECK = "AMX000101";

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
     * Csv出力限界値<br/>
     * ※デフォルト-1=無制限とする
     */
    private Integer csvLimit;

    /* 検索条件項目 */
    /**
     * 会員氏名<br/>
     */
    @HCZenkaku
    private String memberInfoName;

    /**
     * 会員ID<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String memberInfoId;

    /**
     * 会員SEQ
     */
    @HVNumber(groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @Digits(integer = 10, fraction = 0, groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String searchMemberInfoSeq;

    /**
     * 性別<br/>
     */
    @HVItems(target = HTypeSexUnnecessaryAnswer.class,
             groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String memberInfoSex;

    /**
     * 性別選択値<br/>
     */
    private Map<String, String> memberInfoSexItems;

    /**
     * 生年月日<br/>
     */
    @HVDate(groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String memberInfoBirthday;

    /**
     * 会員状態<br/>
     */
    @HVItems(target = HTypeMemberInfoStatus.class,
             groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String memberInfoStatus;

    /**
     * 会員状態選択値<br/>
     */
    private Map<String, String> memberInfoStatusItems;

    /**
     * 会員Tel<br/>
     */
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}",
             groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String memberInfoTel;

    /**
     * 郵便番号<br/>
     */
    @Pattern(regexp = "^[0-9]*$", message = "{AMX000105W}",
             groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String memberInfoZipCode;

    /**
     * 都道府県<br/>
     */
    private String memberInfoPrefecture;

    /**
     * 都道府県アイテム<br/>
     */
    private Map<String, String> memberInfoPrefectureItems;

    /**
     * 会員住所<br/>
     */
    @HCZenkaku
    private String memberInfoAddress;

    /**
     * 期間選択<br/>
     * 動的バリデータあり。
     */
    @HVItems(target = HTypeMemberInfoStatus.class)
    private String periodType;

    /**
     * 期間選択リスト<br/>
     */
    private Map<String, String> periodTypeItems;

    /**
     * 開始時間<br/>
     */
    @HVDate(groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String startDate;

    /**
     * 終了時間<br/>
     */
    @HVDate(groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String endDate;

    /**
     * 選択された会員SEQ<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String checkSeqArray;

    /**
     * 最終ログインユーザーエージェント
     */
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String lastLoginUserAgent;

    /**
     * メールマガジン購読フラグ
     */
    private boolean mailMagazine;

    /**
     * 本会員フラグ
     */
    private boolean mainMemberFlag;

    /* 検索結果項目 */
    /**
     * 検索一覧<br/>
     */
    private List<MemberInfoResultItem> resultItems;

    /**
     * 全件出力タイプ<br />
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = AllDownloadGroup.class)
    private String memberOutData;

    /**
     * 全件出力タイプアイテム<br />
     */
    private Map<String, String> memberOutDataItems;

    /**
     * 選択出力タイプ<br />
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = DownloadTopGroup.class)
    private String checkedMemberOutData1;

    /**
     * 選択出力タイプアイテム<br />
     */
    private Map<String, String> checkedMemberOutData1Items;

    /**
     * 選択出力タイプ<br />
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = DownloadBottomGroup.class)
    private String checkedMemberOutData2;

    /**
     * 選択出力タイプアイテム<br />
     */
    private Map<String, String> checkedMemberOutData2Items;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }

    /** 結果部分のCSVダウンロードオプション Id */
    private String optionTemplateIndexResult;

    /** CSVダウンロードオプション DTO */
    @Valid
    private CsvDownloadOptionDto csvDownloadOptionDto;

    /** CSVダウンロードオプション一覧 DTO */
    @Valid
    private List<CsvDownloadOptionDto> csvDownloadOptionDtoList;
}