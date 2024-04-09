package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateCombo;
import jp.co.itechh.quad.admin.annotation.validator.HVRItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRNumberGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeDate;
import jp.co.itechh.quad.admin.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderOutData;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSite;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderType;
import jp.co.itechh.quad.admin.constant.type.HTypePerson;
import jp.co.itechh.quad.admin.constant.type.HTypeSelectMapOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSelectionOrderOutData;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.common.CheckMessageDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchConditionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.OrderSearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.OutputGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.SelectionOutput1Group;
import jp.co.itechh.quad.admin.pc.web.admin.order.validation.group.ShipmentSearchGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 受注検索モデル<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateCombo(dateLeftTarget = "dateFrom", timeLeftTarget = "timeFrom", timeLeftFormat = "HH:mm",
              dateRightTarget = "dateTo", timeRightTarget = "timeTo", timeRightFormat = "HH:mm",
              groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
@HVRDateCombo(dateLeftTarget = "saleStartDateFrom", timeLeftTarget = "saleStartTimeFrom", timeLeftFormat = "HH:mm",
              dateRightTarget = "saleStartDateTo", timeRightTarget = "saleStartTimeTo", timeRightFormat = "HH:mm",
              groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
@HVRItems(target = "delivery", comparison = "deliveryItems",
          groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
@HVRItems(target = "settlememnt", comparison = "settlememntItems",
          groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
@HVRNumberGreaterEqual(target = "orderPriceTo", comparison = "orderPriceFrom",
                       groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                       DisplayChangeGroup.class})
public class OrderModel extends AbstractModel {

    /** 「クーポン」ラジオボタンが「クーポンID」 **/
    static final String COUPON_SELECT_COUPON_ID = "0";

    /** 「クーポン」ラジオボタンが「クーポンコード」 **/
    static final String COUPON_SELECT_COUPON_CODE = "1";

    /** 受注番号（複数番号検索用）の最大数 */
    public static final int CONDITION_ORDER_CODE_LIST_LIMIT =
                    PropertiesUtil.getSystemPropertiesValueToInt("order.search.order.code.list.length");

    /** 検査キット番号の桁数 */
    public static final int EXAM_KIT_CODE_LENGTH = 20;

    /** 検索アクション種別：受注番号別一覧 */
    public static final String SEARCH_ACTION_TYPE_ORDER = "actionOrder";

    /** 検索アクション種別：出荷登録 */
    public static final String SEARCH_ACTION_TYPE_SHIPMENT = "actionShipment";

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /** ソート項目 */
    private String orderField;

    /** ソート条件 */
    private boolean orderAsc;

    /** 検索結果総件数 */
    private int totalCount;

    /** トップページのアラートのパラメータ */
    private String alertParam;

    /** トップページのピッキングアラートのパラメータ */
    private String pickingAlertParam;

    /**
     * 納品書(PDF)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer deliverySheetPdfLimit;

    /**
     * 受注明細(PDF)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer orderDetailPdfLimit;

    /**
     * 受注商品明細(PDF)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer orderGoodsDetailPdfLimit;

    /**
     * 出荷登録(CSV)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer shipmentCsvLimit;

    /**
     * 入金登録(CSV)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer paymentCsvLimit;

    /**
     * 受注(CSV)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer orderCsvLimit;

    /**
     * 受注商品(CSV)出力限界値<br/>
     * ※HTMLで指定 デフォルト-1=無制限とする
     */
    private Integer orderGoodsCsvLimit;

    /**
     * 受注番号別・出荷登録・入金登録
     * 検索一覧<br/>
     */
    @Valid
    private List<OrderModelItem> resultItems;

    /** セッションチェック用商品SEQリスト */
    private List<Integer> sessionGoodsSeqList;

    /** 入金登録及び出荷登録結果メッセージリスト */
    private List<CheckMessageDto> checkMessageItems;

    /** 登録結果メッセージ */
    private String message;

    /** メッセージコード(検索条件用) */
    private List<String> msgCodeList;

    /** メッセージ引数マップ */
    private Map<String, String[]> msgArgMap;

    /** 検索条件 */
    private OrderSearchConditionDto orderSearchConditionDto;

    /** 結果部分のCSVダウンロードオプション Id */
    private String optionTemplateIndexResult;

    /** CSVダウンロードオプション DTO */
    @Valid
    private CsvDownloadOptionDto csvDownloadOptionDto;

    /** CSVダウンロードオプション一覧 DTO */
    @Valid
    private List<CsvDownloadOptionDto> csvDownloadOptionDtoList;

    /* 画面検索項目 */

    /** 受付サイト */
    // 初期値設定（全選択状態）
    @NotEmpty(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HVItems(target = HTypeOrderSite.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String[] orderSiteTypeArray = {"0", "1", "2", "3"};

    /** 受付サイトアイテム */
    private Map<String, String> orderSiteTypeArrayItems;

    /** 受注状態 */
    @HVItems(target = HTypeSelectMapOrderStatus.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String orderStatus;

    /** 受注状態アイテム */
    private Map<String, String> orderStatusItems;

    /** 受注番号 */
    @Pattern(regexp = ValidatorConstants.REGEX_ORDER_CODE, message = "{AOX000104W}",
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = ValidatorConstants.LENGTH_ORDER_CODE_MAXIMUM,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String conditionOrderCode;

    /** 受注番号（複数番号検索用） */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                     DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @HCHankaku
    private String conditionOrderCodeList;

    /** 受注種別 */
    @HVItems(target = HTypeOrderType.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String[] orderTypeArray;

    /** 受注種別アイテム */
    private Map<String, String> orderTypeArrayItems;

    /** 期間条件タイプ */
    @HVNumber(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String termType;

    /** 期間種別 */
    @HVItems(target = HTypeDate.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String timeType;

    /** 期間種別アイテム */
    private Map<String, String> timeTypeItems;

    /** 期間開始日 */
    @HCDate
    @HVDate(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String dateFrom;

    /** 期間開始時間 */
    @HCDate(pattern = "HH:mm")
    @HVDate(pattern = "HH:mm",
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String timeFrom;

    /** 期間終了日 */
    @HCDate
    @HVDate(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String dateTo;

    /** 期間終了時間 */
    @HCDate(pattern = "HH:mm")
    @HVDate(pattern = "HH:mm",
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String timeTo;

    /** 会員SEQ */
    @HVNumber(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Digits(integer = 10, fraction = 0, message = "{HNumberLengthValidator.FRACTION_MAX_ZERO_detail}",
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String memberInfoSeq;

    /** 顧客 */
    @HVItems(target = HTypePerson.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String orderPerson;

    /** 顧客アイテム */
    private Map<String, String> orderPersonItems;

    /** 名前 */
    @Length(min = 0, max = 80)
    @HCZenkaku
    private String searchName;

    /** Tel */
    @Length(min = 0, max = 11,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}",
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String searchTel;

    /** メールアドレス */
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @Length(min = 0, max = 160)
    @HCHankaku
    private String customerMail;

    /** 商品管理番号 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                     DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_GROUP_CODE,
             message = ValidatorConstants.MSGCD_REGEX_GOODS_GROUP_CODE,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_GROUP_CODE_MAXIMUM,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String goodsGroupCode;

    /** 商品番号 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                     DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_CODE, message = ValidatorConstants.MSGCD_REGEX_GOODS_CODE,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_CODE_MAXIMUM,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String goodsCode;

    /** JANコード（or カタログコード） */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                     DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_CATALOG_CODE, message = ValidatorConstants.MSGCD_REGEX_JAN_CATALOG_CODE,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = ValidatorConstants.LENGTH_JAN_CODE_MAXIMUM,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String janCode;

    /** 商品名 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                     DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @Length(min = 0, max = 120,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String goodsGroupName;

    /** 個別配送フラグ */
    private Boolean individualDeliveryType;

    /** 販売開始日From */
    @HCDate
    @HVDate(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String saleStartDateFrom;

    /** 販売開始時間From */
    @HCDate(pattern = "HH:mm")
    @HVDate(pattern = "HH:mm",
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String saleStartTimeFrom;

    /** 販売開始日To */
    @HCDate
    @HVDate(groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String saleStartDateTo;

    /** 販売開始時間To */
    @HCDate(pattern = "HH:mm")
    @HVDate(pattern = "HH:mm",
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String saleStartTimeTo;

    /** 配送方法 */
    private String delivery;

    /** 配送方法アイテム */
    private Map<String, String> deliveryItems;

    /** 伝票番号 */
    @Pattern(regexp = ValidatorConstants.REGEX_DELIVERY_CODE, message = ValidatorConstants.MSGCD_REGEX_DELIVERY_CODE,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = 40,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String deliveryCode;

    /** 出荷状態 */
    @HVItems(target = HTypeShipmentStatus.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String[] shipmentStatus;

    /** 出荷状態アイテム */
    private Map<String, String> shipmentStatusItems;

    /** 決済条件セレクト */
    private Integer settlementSelect = 0;

    /** 決済方法 */
    private String settlememnt;

    /** 決済方法アイテム */
    private Map<String, String> settlememntItems;

    /** 請求種別 */
    @HVItems(target = HTypeBillType.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String billType;

    /** 請求種別アイテム */
    private Map<String, String> billTypeItems;

    /** 請求状態 */
    @HVItems(target = HTypeBillStatus.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String billStatus;

    /** 請求状態アイテム */
    private Map<String, String> billStatusItems;

    /** 入金状態 */
    private String paymentStatus;

    /** 入金状態アイテム */
    private Map<String, String> paymentStatusItems;

    /** クーポン選択フラグ */
    private String couponSelect = "0";

    /** クーポンID／クーポンコード */
    @Length(min = 0, max = ValidatorConstants.LENGTH_COUPON_CODE_MAXIMUM,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @HCHankaku
    private String coupon;

    /** 出力タイプ */
    @NotEmpty(groups = {OutputGroup.class}, message = "{HRequiredValidator.REQUIRED_detail}")
    @HVItems(groups = {OutputGroup.class}, target = HTypeOrderOutData.class)
    private String orderOutData;

    /** 出力タイプアイテム */
    private Map<String, String> orderOutDataItems;

    /** 出力タイプ */
    @NotEmpty(groups = {SelectionOutput1Group.class}, message = "{HRequiredValidator.REQUIRED_detail}")
    @HVItems(groups = {SelectionOutput1Group.class}, target = HTypeSelectionOrderOutData.class)
    private String orderOutData1;

    /** 出力タイプアイテム */
    private Map<String, String> orderOutData1Items;

    /** 支払い期限切れ */
    private Boolean timeLimitOver;

    /** 検査状態タイプアイテム */
    private Map<String, String> examStatusItems;

    /** 検査状態 */
    @HVItems(target = HTypeExamStatus.class,
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    private String examStatus;

    /** 検体番号 */
    @Pattern(regexp = ValidatorConstants.REGEX_SPECIMEN_CODE, message = "{PKG-3680-014-S-W}",
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = 6,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String specimenCode;

    /** 受注番号（複数番号検索用） */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                     DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class,
                                        DisplayChangeGroup.class})
    @HCHankaku
    private String conditionExamKitCodeList;

    /** 検査キット番号（複数番号検索用）検索できる件数の最大数 */
    private int examKitCodeListLength;

    /** 検査キット番号（複数番号検索用）選択肢区切り文字 */
    private String examKitCodeDivChar;

    /* 検索結果ヘッダー部項目 */

    /** 受注番号 */
    private String orderCodeSort;

    /** 注文連番 */
    private String orderConsecutiveNoSort;

    /** 受注種別 */
    private String orderTypeSort;

    /** 受注日時 */
    private String orderTimeSort;

    /** 受注状態 */
    private String orderStatusSort;

    /** 検索結果表示用受注状態用ソート */
    private String orderStatusForSearchResultSort;

    /** ご注文主 */
    private String orderNameSort;

    /** 受注金額 */
    private String orderPriceSort;

    /** お支払い方法 */
    private String settlementMethodSort;

    /** 入金状態 */
    private String paymentStatusSort;

    /** 入金日 */
    private String receiptTimeSort;

    /** 入金額 */
    private String receiptPriceTotalSort;

    /** 配送方法 */
    private String deliveryMethodSort;

    /** 出荷状態 */
    private String shipmentStatusSort;

    /** サイト */
    private String orderSiteTypeSort;

    /** 備考欄 */
    private String deliveryNoteSort;

    /** 商品管理番号 */
    private String goodsGroupCodeSort;

    /** 商品番号 */
    private String goodsCodeSort;

    /** JANコード */
    private String janCodeSort;

    /** 商品名 */
    private String goodsGroupNameSort;

    /** 規格１ */
    private String unitValue1Sort;

    /** 規格２ */
    private String unitValue2Sort;

    /** 商品個別配送種別 */
    private String individualDeliveryTypeSort;

    /** 商品単価 */
    private String goodsPriceSort;

    /** 購入数 */
    private String goodsCountSort;

    /** お届け先 */
    private String receiverNameSort;

    /** 配送種別 */
    private String reservationDeliveryFlagSort;

    /** 受注番号別一覧検索フラグ */
    private boolean orderSearch;

    /** 出荷登録フラグ */
    private boolean shipmentRegister;

    /** 入金登録フラグ */
    private boolean paymentRegister;

    /** 入金状態 未入金スタイル */
    private String unPaidStyleClass;

    /** 入金状態 過不足スタイル */
    private String payingStyleClass;

    /** 入金状態 入金済みスタイル */
    private String paidStyleClass;

    /** 出荷状態 未出荷スタイル */
    private String unShipmentStyleClass;

    /** 出荷状態 出荷済みスタイル */
    private String shipedStyleClass;

    /** 受注状態 請求決済エラースタイル */
    private String emergencyStyleClass;

    /** 受注情報検索結果 */
    private String orderResultItems;

    /** 商品情報検索結果 */
    private String goodsResultItems;

    /** 受注CSVの出力ボタン押下フラグ */
    private boolean selectOrderCSVFlag;

    /** 出荷CSVの出力ボタン押下フラグ */
    private boolean selectShipmentCSVFlag;

    /** 受注商品単位で絞り込むフラグ */
    private boolean filterOrderedProductFlag;

    /** 受注金額From */
    @Length(min = 0, max = 8,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.PRICE_NUMBER_REGEX, message = "{HNumberValidator.NOT_NUMBER_detail}",
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String orderPriceFrom;

    /** 受注金額To */
    @Length(min = 0, max = 8,
            groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.PRICE_NUMBER_REGEX, message = "{HNumberValidator.NOT_NUMBER_detail}",
             groups = {OutputGroup.class, OrderSearchGroup.class, ShipmentSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String orderPriceTo;

    /** */
    public void onOrderSearch() {
        this.orderSearch = true;
        this.shipmentRegister = true;
    }

    /**  */
    public void onShipmentRegister() {
        this.orderSearch = false;
        this.shipmentRegister = true;
    }

    /**
     * 検索結果リスト有無判定<br/>
     *
     * @return true=無、false=有
     */
    public boolean isResultItemsExist() {
        return getResultItems() == null || getResultItems().isEmpty();
    }

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }

    /**
     * 受注CSVの出力ボタンを押したかどうか<br/>
     * このメソッド内部でフラグを初期化
     *
     * @return true=押下
     */
    public boolean isSelectOrderCSV() {

        if (selectOrderCSVFlag) {
            // 初期化
            selectOrderCSVFlag = false;
            return true;
        }
        return false;
    }

    /**
     * 出荷CSVの出力ボタンを押したかどうか<br/>
     * このメソッド内部でフラグを初期化
     *
     * @return true=押下
     */
    public boolean isSelectShipmentCSV() {
        if (selectShipmentCSVFlag) {
            selectShipmentCSVFlag = false;
            return true;
        }
        return false;
    }

}
