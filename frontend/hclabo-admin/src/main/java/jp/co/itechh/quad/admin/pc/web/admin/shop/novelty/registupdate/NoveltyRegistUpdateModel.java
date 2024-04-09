package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVGoodsCode;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateCombo;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.CheckGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.RegistUpdateGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import jp.co.itechh.quad.admin.web.PageInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@HVRDateCombo(dateLeftTarget = "noveltyPresentStartDate", timeLeftTarget = "noveltyPresentStartTime",
              dateRightTarget = "noveltyPresentEndDate", timeRightTarget = "noveltyPresentEndTime")
@HVRDateGreaterEqual(target = "admissionEndDate", comparison = "admissionStartDate")
public class NoveltyRegistUpdateModel extends AbstractModel {

    /**
     * 画面表示モード
     */
    private String md;

    /** ノベルティプレゼント条件SEQ */
    private Integer noveltyPresentConditionSeq;

    /** ノベルティプレゼント条件エンティティ */

    private NoveltyPresentConditionEntity noveltyPresentConditionEntity;

    // ---------------------------------------
    // 基本情報
    // ---------------------------------------
    /** 判定対象 */
    @NotEmpty(groups = {RegistUpdateGroup.class})
    private String enclosureUnitType;

    /** ノベルティプレゼント条件名 */
    @NotEmpty(groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {RegistUpdateGroup.class})
    @Length(min = 1, max = 120, groups = {RegistUpdateGroup.class})
    private String noveltyPresentName;

    /** ノベルティプレゼント条件状態 */
    @NotEmpty(groups = {RegistUpdateGroup.class})
    @HVItems(target = HTypeNoveltyPresentState.class, groups = {RegistUpdateGroup.class})
    private String noveltyPresentState;

    /** ノベルティプレゼント条件開始日 */
    @NotEmpty(groups = {RegistUpdateGroup.class})
    @HCDate(pattern = DateUtility.YMD_SLASH)
    @HVDate(pattern = DateUtility.YMD_SLASH, groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = '/', groups = {RegistUpdateGroup.class})
    private String noveltyPresentStartDate;

    /** ノベルティプレゼント条件開始時刻 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = ':', groups = {RegistUpdateGroup.class})
    private String noveltyPresentStartTime;

    /** ノベルティプレゼント条件開始日(比較用) */
    private String beforeNoveltyPresentStartDate;

    /** ノベルティプレゼント条件開始時刻(比較用) */
    private String beforeNoveltyPresentStartTime;

    /** ノベルティプレゼント条件終了日 */
    @HCDate(pattern = DateUtility.YMD_SLASH)
    @HVDate(pattern = DateUtility.YMD_SLASH, groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = '/', groups = {RegistUpdateGroup.class})
    private String noveltyPresentEndDate;

    /** ノベルティプレゼント条件終了時刻 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = ':', groups = {RegistUpdateGroup.class})
    private String noveltyPresentEndTime;

    /** ノベルティ商品番号(動的バリデータ) */
    @NotEmpty(groups = {RegistUpdateGroup.class})
    @HVGoodsCode(groups = {RegistUpdateGroup.class})
    @Length(min = 1, groups = {RegistUpdateGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {RegistUpdateGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_CODE_MULTIPLE_LINE,
             message = ValidatorConstants.MSGCD_REGEX_GOODS_CODE, groups = {RegistUpdateGroup.class})
    private String noveltyGoodsCode;

    /** 除外条件 */
    private List<NoveltyRegistUpdateExclusionNoveltyItem> exclusionNoveltyItems;

    /** 除外条件チェック */
    private boolean exclusionNoveltyCheck;

    /** 除外条件SEQ */
    private Integer exclusionNoveltySeq;

    /** 除外条件名 */
    private String exclusionNoveltyName;

    /** ノベルティプレゼント条件状態 */
    private Map<String, String> noveltyPresentStateItems;

    /** 判定対象 */
    private String noveltyPresentJudgmentType;

    // ---------------------------------------
    // 会員条件
    // ---------------------------------------
    /** プレゼント数制限 */
    @HVNumber(groups = {RegistUpdateGroup.class})
    @Digits(integer = 6, fraction = 0, groups = {RegistUpdateGroup.class})
    @HCNumber
    private String prizeGoodsLimit;

    /** 入会期間開始日 */
    @HCDate(pattern = DateUtility.YMD_SLASH)
    @HVDate(pattern = DateUtility.YMD_SLASH, groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = '/', groups = {RegistUpdateGroup.class})
    private String admissionStartDate;

    /** 入会期間開始時刻 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = ':', groups = {RegistUpdateGroup.class})
    private String admissionStartTime;

    /** 入会期間終了日 */
    @HCDate(pattern = DateUtility.YMD_SLASH)
    @HVDate(pattern = DateUtility.YMD_SLASH, groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = '/', groups = {RegistUpdateGroup.class})
    private String admissionEndDate;

    /** 入会期間終了時刻 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowCharacters = ':', groups = {RegistUpdateGroup.class})
    private String admissionEndTime;

    /** メールマガジン */
    private boolean magazineSendFlag;

    // ---------------------------------------
    // 商品情報条件
    // ---------------------------------------
    /** 商品管理番号 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HCHankaku
    @Length(min = 0, max = 10000, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    private String goodsGroupCode;

    /** 商品番号 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_CODE, message = ValidatorConstants.MSGCD_REGEX_GOODS_CODE,
             groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HCHankaku
    @Length(min = 0, max = 60000, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    private String goodsCode;

    /** カテゴリーID */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HCHankaku
    @Length(min = 0, max = 1000, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    private String categoryId;

    /** アイコン */
    private List<NoveltyRegistUpdateIconItem> iconItems;

    private String[] iconChecked;

    /** アイコンチェック */
    private boolean iconCheck;

    /** アイコンSEQ */
    private Integer iconSeq;

    /** アイコン名 */
    private String iconName;

    /** 商品名 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @Length(min = 0, max = 2400, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    private String goodsName;

    /** 検索キーワード */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    @Length(min = 0, max = 1000, groups = {CheckGoodsGroup.class, RegistUpdateGroup.class})
    private String searchkeyword;

    /** 商品管理番号用エラーメッセージ */
    private String goodsGroupCodeErrMsg;

    /** 商品番号用エラーメッセージ */
    private String goodsCodeErrMsg;

    /** カテゴリーID用エラーメッセージ */
    private String categoryIdErrMsg;

    /** アイコン用エラーメッセージ */
    private String iconErrMsg;

    /** 商品名用エラーメッセージ */
    private String goodsNameErrMsg;

    /** 検索キーワード用エラーメッセージ */
    private String keywordErrMsg;

    // ---------------------------------------
    // 受注情報条件
    // ---------------------------------------
    /** 商品金額合計 */
    @HVNumber(groups = {RegistUpdateGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {RegistUpdateGroup.class})
    @HCNumber
    private String goodsPriceTotal;

    /** 対象商品のみ */
    private boolean goodsPriceTotalFlag;

    // ---------------------------------------
    // その他
    // ---------------------------------------
    /** 管理メモ */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {RegistUpdateGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {RegistUpdateGroup.class})
    @Length(min = 0, max = 2000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {RegistUpdateGroup.class})
    private String memo;

    // ---------------------------------------
    // 画面制御用
    // ---------------------------------------
    /** 商品数 */
    private int goodsCount;

    /** 商品数確認フラグ */
    private boolean goodsCountFlag;

    /**
     * 更新モードかどうかを判定する
     *
     * @return TRUE:更新時、FALSE:登録時
     */
    public boolean isUpdateMode() {
        if (noveltyPresentConditionSeq == null) {
            return false;
        }
        return true;
    }

    /**
     * 除外条件の一覧があるかどうか
     *
     * @return TRUE:あり、FALSE:なし
     */
    public boolean isExclusionNoveltyItemsExist() {
        if (exclusionNoveltyItems == null || exclusionNoveltyItems.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * アイコンの一覧があるかどうか
     *
     * @return TRUE:あり、FALSE:なし
     */
    public boolean isIconItemsExist() {
        if (iconItems == null || iconItems.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @return the goodsCountCheck
     */
    public boolean isGoodsCountFlag() {
        return goodsCountFlag;
    }

    /**
     * @param goodsCountFlag the goodsCountFlag to set
     */
    public void setGoodsCountFlag(boolean goodsCountFlag) {
        this.goodsCountFlag = goodsCountFlag;
    }

    // ---------------------------------------
    // 商品情報条件エラーメッセージ表示制御用
    // ---------------------------------------

    /**
     * 商品管理番号にエラーがあるかを判定
     * @return TRUE:エラーあり
     */
    public boolean isGoodsGroupCodeError() {
        if (goodsGroupCodeErrMsg != null && !"".equals(goodsGroupCodeErrMsg)) {
            return true;
        }
        return false;
    }

    /**
     * 商品番号にエラーがあるかを判定
     * @return TRUE:エラーあり
     */
    public boolean isGoodsCodeError() {
        if (goodsCodeErrMsg != null && !"".equals(goodsCodeErrMsg)) {
            return true;
        }
        return false;
    }

    /**
     * カテゴリーIDにエラーがあるかを判定
     * @return TRUE:エラーあり
     */
    public boolean isCategoryIdError() {
        if (categoryIdErrMsg != null && !"".equals(categoryIdErrMsg)) {
            return true;
        }
        return false;
    }

    /**
     * アイコンにエラーがあるかを判定
     * @return TRUE:エラーあり
     */
    public boolean isIconCodeError() {
        if (iconErrMsg != null && !"".equals(iconErrMsg)) {
            return true;
        }
        return false;
    }

    /**
     * 商品名にエラーがあるかを判定
     * @return TRUE:エラーあり
     */
    public boolean isGoodsNameError() {
        if (goodsNameErrMsg != null && !"".equals(goodsNameErrMsg)) {
            return true;
        }
        return false;
    }

    /**
     * 商品管理番号にエラーがあるかを判定
     * @return TRUE:エラーあり
     */
    public boolean isKeywordError() {
        if (keywordErrMsg != null && !"".equals(keywordErrMsg)) {
            return true;
        }
        return false;
    }

    // ---------------------------------------
    // PagePage関連
    // ---------------------------------------
    /** ページ番号 */

    private Integer pageNumber;
    /** ページ情報 */

    private PageInfo pageInfo;
    /** 前リンク */

    private String prevLink;
    /**　番号リンク */

    private String numberLink;
    /** 次リンク */

    private String nextLink;
    /** 総件数 */

    private int totalCount;

    /**
     * @return the pageInfo
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * @param pageInfo the pageInfo to set
     */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

}