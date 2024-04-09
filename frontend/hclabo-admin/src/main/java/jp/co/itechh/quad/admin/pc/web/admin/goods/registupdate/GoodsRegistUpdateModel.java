package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVImageJpegFile;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateCombo;
import jp.co.itechh.quad.admin.annotation.validator.HVRItems;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateModel;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.AddGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.DeleteGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.DownGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.UpGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.UploadImageGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ListUtils;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理：商品登録更新ページ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateCombo(dateLeftTarget = "goodsOpenStartDatePC", timeLeftTarget = "goodsOpenStartTimePC",
              dateRightTarget = "goodsOpenEndDatePC", timeRightTarget = "goodsOpenEndTimePC",
              groups = {ConfirmGroup.class})
@HVRItems(target = "taxRate", comparison = "taxRateItems", groups = {ConfirmGroup.class})
public class GoodsRegistUpdateModel extends AbstractGoodsRegistUpdateModel {

    /** 商品タイプが試供品 かつ テキストエリアが未入力の場合エラー：PKG-3559-001-A- */
    public static final String MSGCD_NOT_INPUT_SAMPLE_LIMIT = "PKG-3559-001-A-";

    /** 商品タイプが試供品以外 かつ テキストエリアが入力状態の場合エラー：PKG-3559-002-A- */
    public static final String MSGCD_INPUT_SAMPLE_LIMIT = "PKG-3559-002-A-";

    /** 商品タイプが試供品以外 かつ プルダウンが選択状態の場合エラー：PKG-3559-004-A- */
    public static final String MSGCD_SELECT_ONLY_BUY = "PKG-3559-004-A-";

    /** 登録商品タグリスト */
    public static final String GOODSTAG_LIST = "GoodsGroupDisplayEntity.goodsTagList";

    /** 登録商品タグリスト */
    public static final String GOODSTAG_LIST_SIZE = "GoodsGroupDisplayEntity.goodsTagList.SIZE";

    /**
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     *
     */
    public GoodsRegistUpdateModel() {
        super();

        /** 修正箇所のオブジェクト名 / スタイル 設定 */
        diffObjectNameGoods = ApplicationContextUtility.getBean(GoodsEntity.class).getClass().getSimpleName();
    }

    /***************************************************************************************************************************
     ** 商品基本設定
     ***************************************************************************************************************************/
    /************************************
     ** 入力項目
     ************************************/
    /**
     * 登録日時<br/>
     */
    private Timestamp registTime;

    /**
     * 更新日時<br/>
     */
    private Timestamp updateTime;

    /**
     * 新着日時<br/>
     */
    @HVDate(groups = {ConfirmGroup.class})
    @HCDate
    private String whatsnewDate;

    /**
     * 税率
     */
    @NotNull(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    private BigDecimal taxRate;

    /**
     * 税率Items
     */
    private Map<BigDecimal, String> taxRateItems;

    /**
     * 酒類フラグ<br/>
     */
    @HVItems(target = HTypeAlcoholFlag.class)
    private String alcoholFlag;

    /**
     * ノベルティ商品フラグ<br/>
     */
    @HVItems(target = HTypeNoveltyGoodsType.class, groups = {ConfirmGroup.class})
    private String noveltyGoodsType;

    /**
     * 酒類フラグ(ラジオボタン)<br/>
     */
    private Map<String, String> alcoholFlagItems;

    /************************************
     ** 外部連携設定
     ************************************/
    /**
     * SNS連携フラグItems<br/>
     */
    private Map<String, String> snsLinkFlagItems;

    /** SNS連携フラグ */
    @HVItems(target = HTypeSnsLinkFlag.class, groups = {ConfirmGroup.class})
    private String snsLinkFlag;

    /**
     * 個別配送Items<br/>
     */
    private Map<String, String> individualDeliveryTypeItems;

    /**
     * 個別配送<br/>
     */
    @HVItems(target = HTypeIndividualDeliveryType.class, groups = {ConfirmGroup.class})
    private String individualDeliveryType;

    /**
     * 無料配送<br/>
     */
    private Map<String, String> freeDeliveryFlagItems;

    /**
     * 無料配送<br/>
     */
    @HVItems(target = HTypeFreeDeliveryFlag.class, groups = {ConfirmGroup.class})
    private String freeDeliveryFlag;

    /************************************
     ** 商品公開状態PC
     ************************************/
    /**
     * 商品公開状態PCItems<br/>
     */
    private Map<String, String> goodsOpenStatusPCItems;

    /**
     * 商品公開状態PC<br/>
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeOpenDeleteStatus.class, groups = {ConfirmGroup.class})
    private String goodsOpenStatusPC;

    /**
     * 商品公開開始日PC<br/>
     */
    @HVDate(groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowCharacters = '/', groups = {ConfirmGroup.class})
    @HCDate
    private String goodsOpenStartDatePC;

    /**
     * 商品公開開始時刻(時分秒)PC<br/>
     */
    @HVDate(pattern = "HH:mm:ss", groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowCharacters = ':', groups = {ConfirmGroup.class})
    @HCDate(pattern = "HH:mm:ss")
    private String goodsOpenStartTimePC;

    /**
     * 商品公開終了日PC<br/>
     */
    @HVDate(groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowCharacters = '/', groups = {ConfirmGroup.class})
    @HCDate
    private String goodsOpenEndDatePC;

    /**
     * 商品公開終了時刻(時分秒)PC<br/>
     */
    @HVDate(pattern = "HH:mm:ss", groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowCharacters = ':', groups = {ConfirmGroup.class})
    @HCDate(pattern = "HH:mm:ss")
    private String goodsOpenEndTimePC;

    /**
     * 単価＝商品単価（税抜）<br/>
     */
    @NotEmpty(groups = {ConfirmGroup.class})
    @HVNumber(groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    @HCNumber
    private String goodsPrice;

    /***************************************************************************************************************************
     ** 商品詳細設定
     ***************************************************************************************************************************/

    /** ページ番号 */
    private Integer pageNumber;

    /** 前リンク */
    private String prevLink;

    /** 番号リンク */
    private String numberLink;

    /** 次リンク */
    private String nextLink;

    /** 総件数 */
    private int totalCount;

    /************************************
     ** 入力項目
     ************************************/
    /**
     * 検索キーワード<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 1000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String searchKeyword;

    /**
     * metaDescription<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'}, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 400, groups = {ConfirmGroup.class})
    private String metaDescription;

    /**
     * metaKeyword<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'}, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 200, groups = {ConfirmGroup.class})
    private String metaKeyword;

    /**
     * 選択画像<br/>
     */
    private String selectImage;

    /************************************
     ** 全アイコン情報リスト
     ************************************/
    /**
     * インフォメーションアイコン情報リスト<br/>
     * （ショップに登録された全アイコンリスト）
     */
    private List<GoodsInformationIconDto> iconList;

    /**
     * インフォメーションアイコン情報<br/>
     */
    private GoodsRegistUpdateIconItem goodsInformationIcon;

    /**
     * インフォメーションアイコン情報リスト<br/>
     * （現在登録更新中の商品に設定している情報を保持しているリスト）
     */
    @Valid
    private List<GoodsRegistUpdateIconItem> goodsInformationIconItems;

    /***************************************************************************************************************************
     ** 商品詳細テキスト設定
     ***************************************************************************************************************************/
    /************************************
     ** 入力項目
     ************************************/
    /**
     * 商品納期<br/>
     */
    @NotEmpty(groups = {ConfirmGroup.class})
    @HVBothSideSpace(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 50, groups = {ConfirmGroup.class})
    private String deliveryType;

    /**
     * 商品説明01<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote1;

    /**
     * 商品説明02<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote2;

    /**
     * 商品説明03<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote3;

    /**
     * 商品説明04<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote4;

    /**
     * 商品説明05<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote5;

    /**
     * 商品説明06<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote6;

    /**
     * 商品説明07<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote7;

    /**
     * 商品説明08<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote8;

    /**
     * 商品説明09<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote9;

    /**
     * 商品説明10<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String goodsNote10;

    /***************************************************************************************************************************
     ** 受注連携設定
     ***************************************************************************************************************************/
    /************************************
     ** 入力項目
     ************************************/
    /**
     * 受注連携設定01<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting1;

    /**
     * 受注連携設定02<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting2;

    /**
     * 受注連携設定03<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting3;

    /**
     * 受注連携設定04<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting4;

    /**
     * 受注連携設定05<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting5;

    /**
     * 受注連携設定06<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting6;

    /**
     * 受注連携設定07<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting7;

    /**
     * 受注連携設定08<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting8;

    /**
     * 受注連携設定09<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting9;

    /**
     * 受注連携設定10<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 4000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String orderSetting10;

    /***************************************************************************************************************************
     ** 商品規格設定
     ***************************************************************************************************************************/
    /************************************
     ** 規格表示設定
     ************************************/
    /**
     * 規格管理フラグ<br/>
     */
    private Map<String, String> unitManagementFlagItems;

    /**
     * 規格管理フラグ<br/>
     */
    private String unitManagementFlag;

    /**
     * 規格１表示名<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    @Length(min = 0, max = 100,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    private String unitTitle1;

    /**
     * 規格２表示名<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    @Length(min = 0, max = 100,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    private String unitTitle2;

    /**
     * 選択表示番号<br/>
     */
    private Integer selectDspNo;

    /************************************
     ** 規格設定
     ************************************/
    /**
     * 規格数（hidden項目）<br/>
     */
    private int unitCount;

    /**
     * 商品規格リスト<br/>
     */
    @Valid
    private List<GoodsRegistUpdateUnitItem> unitItems;

    /**
     * 商品販売状態PC（商品規格内）<br/>
     */
    private Map<String, String> goodsSaleStatusPCItems;

    /**
     * 商品販売状態PC（商品規格内ダミー退避用）<br/>
     */
    private Map<String, String> dummyGoodsSaleStatusPCItems;

    /************************************
     ** 判定
     ************************************/
    /**
     * 規格削除可能判定<br/>
     * 規格が1件の場合は規格削除不可
     *
     * @return true=規格削除可能
     */
    public boolean isUnitDeletable() {
        return (unitCount > 1);
    }

    /**
     * @return the goodsSaleStatusPCItems
     */
    public Map<String, String> getGoodsSaleStatusPCItems() {
        // 不具合回避用です
        if (dummyGoodsSaleStatusPCItems == null && goodsSaleStatusPCItems != null) {
            dummyGoodsSaleStatusPCItems = goodsSaleStatusPCItems;
        }
        if (dummyGoodsSaleStatusPCItems != null && goodsSaleStatusPCItems == null) {
            goodsSaleStatusPCItems = dummyGoodsSaleStatusPCItems;
        }
        return goodsSaleStatusPCItems;
    }

    /***************************************************************************************************************************
     ** 商品画像設定
     ***************************************************************************************************************************/
    /** アップロード画像ファイル削除失敗メッセージ */
    public static final String MSGCD_FAIL_DELETE = "AGG001403";

    /** 一覧画像表示順 */
    public static final Integer ORDERDISPLAY_LIST = 0;

    /************************************
     ** 商品グループ詳細画像項目
     ************************************/

    /**
     * 選択画像No（hidden項目）
     */
    private Integer selectImageNo;

    /**
     * 商品画像アイテムリスト
     */
    @Valid
    private List<GoodsRegistUpdateImageItem> goodsImageItems;

    // *****************************************************************************
    // 新HIT-MALL：複数画像アップロード - START
    // *****************************************************************************
    /**
     * 商品グループ詳細画像
     */
    @JsonIgnore
    @HVImageJpegFile(groups = UploadImageGroup.class)
    private MultipartFile[] uploadedGoodsImages;

    /***************************************************************************************************************************
     ** 商品在庫設定
     ***************************************************************************************************************************/
    /************************************
     ** 入力項目
     ************************************/
    /**
     * 在庫管理フラグ<br/>
     */
    private String stockManagementFlag;

    /************************************
     ** リスト項目
     ************************************/
    /**
     * 商品在庫リスト<br/>
     */
    @Valid
    private List<GoodsRegistUpdateStockItem> stockItems;

    /************************************
     ** コンボボックス
     ************************************/
    /**
     * 在庫管理フラグ<br/>
     */
    private Map<String, String> stockManagementFlagItems;

    /************************************
     ** 判定
     ************************************/
    /**
     * 商品規格0件判定<br/>
     *
     * @return true=商品規格0件
     */
    public boolean isGoodsUnitNoData() {
        return (unitCount == 0);
    }

    /***************************************************************************************************************************
     ** 関連商品設定
     ***************************************************************************************************************************/
    /************************************
     ** 関連商品リスト項目
     ************************************/
    /**
     * 関連商品グループ情報<br/>
     */
    private List<GoodsRelationEntity> tmpGoodsRelationEntityList;

    /**
     * 関連商品グループ情報（ページ間リダイレクト受け渡し用）<br/>
     */
    private List<GoodsRelationEntity> redirectGoodsRelationEntityList;

    /**
     * 関連商品リスト<br/>
     */
    @Valid
    private List<GoodsRegistUpdateRelationItem> relationItems;

    /**
     * 関連商品リスト（空）<br/>
     */
    @Valid
    private List<GoodsRegistUpdateRelationItem> relationNoItems;

    /************************************
     ** 関連商品リスト表示項目
     ************************************/
    /**
     * 関連商品リスト 選択関連商品グループコード<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    private String selectRelationGoodsGroupCode;

    /**
     * 関連商品追加可能判定<br/>
     *
     * @return true=関連商品追加可能
     */
    public boolean isAddPossible() {
        if (tmpGoodsRelationEntityList == null) {
            tmpGoodsRelationEntityList = new ArrayList<>();
        }
        return (tmpGoodsRelationEntityList.size() < getGoodsRelationAmount());
    }

    /** 商品DTO商品グループ名 */
    public final String diffObjectNameGoods;

    /** 商品グループ(GoodsGroupDto)変更箇所リスト　*/
    private List<String> modifiedGoodeGroupList = new ArrayList<>();

    /** 商品(GoodsEntity)変更箇所リスト */
    private List<String> modifiedCommonGoodsList = new ArrayList<>();

    /** 商品グループ（GoodsGroupEntity）の変更箇所リスト */
    private List<String> modifiedGoodsGroupList;

    /** 商品グループ表示（GoodsGroupDisplayEntity）の変更箇所リスト */
    private List<String> modifiedGoodsGroupDspList;

    /** 商品Dtoの商品（GoodsEntity）の変更箇所リスト */
    private List<List<String>> modifiedGoodsList;

    /** 商品Dtoの在庫Dto（StockDto）の変更箇所リスト */
    private List<List<String>> modifiedStockList;

    /** 関連商品（GoodsRelationEntity）の変更箇所リスト */
    private List<List<String>> modifiedGoodsRelationList;

    /** カテゴリ登録商品グループ（CategoryGoodsEntity）の変更箇所リスト */
    private List<List<String>> modifiedCategoryGoodsEntityList;

    /**
     * カテゴリ登録商品リスト(変更前)<br/>
     */
    private List<CategoryGoodsEntity> masterCategoryGoodsEntityList = new ArrayList<>();

    /**
     * インフォメーションアイコンPC(変更前)<br/>
     */
    private List<String> masterInformationIconPcList = new ArrayList<>();

    /**
     * 商品Dtoリスト(変更前)<br/>
     */
    private List<GoodsDto> masterGoodsDtoList = new ArrayList<>();

    /**
     * 関連商品エンティティリスト(変更前)<br/>
     */
    private List<GoodsRelationEntity> masterGoodsRelationEntityList = new ArrayList<>();

    /**
     * 商品グループ画像Dtoマップ(変更前) key=連番, values=GoodsGroupImageEntity <br/>
     */
    private Map<Integer, GoodsGroupImageEntity> masterGoodsGroupImageEntityMap = new HashMap<>();

    /************************************
     ** 表示項目（カテゴリ設定部分）
     ************************************/
    /**
     * 登録カテゴリリスト<br/>
     */
    private List<CategoryEntity> registCategoryEntityList;

    /**
     * カテゴリリスト<br/>
     */
    private List<CategoryEntity> categoryEntityList;

    /**
     * ひもづいているカテゴリーリスト<br/>
     */
    private List<CategoryEntity> linkedCategoryList;

    /**
     * カテゴリ検索<br/>
     */
    private String categorySearch;

    /************************************
     ** 表示項目（商品タグ設定部分）
     ************************************/
    /**
     * 登録商品タグリリスト<br/>
     */
    private List<String> goodsTagList;

    /**
     * マスタに登録商品タグリリスト<br/>
     */
    private List<String> oldGoodsTagList;

    /**
     * 商品タグインプット
     */
    private String goodsTagInput;

    /************************************
     ** リスト項目（商品規格設定）
     ************************************/
    /**
     * 商品規格リスト<br/>
     */
    private List<GoodsRegistUpdateUnitItem> confirmUnitItems;

    /************************************
     ** リスト項目（商品在庫部分）
     ************************************/
    /**
     * 商品在庫リスト<br/>
     */
    private GoodsRegistUpdateStockItem stock;

    /************************************
     ** 商品グループ詳細画像項目
     ************************************/

    /**
     * 商品グループ詳細画像アイテムリスト
     */
    private List<GoodsRegistUpdateImageItem> confirmGoodsImageItems;

    /**
     * 公開開始日PC<br/>
     */
    private Timestamp openStartDatePC;

    /**
     * 公開終了日PC<br/>
     */
    private Timestamp openEndDatePC;

    /**
     * 公開開始時間PC<br/>
     */
    private Timestamp openStartTimePC;

    /**
     * 公開終了時間PC<br/>
     */
    private Timestamp openEndTimePC;

    /**
     * 画面リロード後の自動スクロールのターゲットポジション<br/>
     */
    private String targetAutoScrollTagId;

    /**
     * 背景色処理
     *
     * @return true..表示する
     */
    public boolean isGoodsTagDiff() {
        if (!ListUtils.isEmpty(modifiedGoodsGroupDspList)) {
            for (String item : modifiedGoodsGroupDspList) {
                if (GOODSTAG_LIST.equals(item) || GOODSTAG_LIST_SIZE.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

}