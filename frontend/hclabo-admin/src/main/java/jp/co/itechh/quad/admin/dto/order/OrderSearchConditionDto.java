/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.base.dto.AbstractConditionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 受注検索一覧用条件Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
@EqualsAndHashCode(callSuper = false)
public class OrderSearchConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 検索種別：受注・入金 */
    public static final String SEARCH_UNSHIPMENT = "0";

    /** 検索種別：出荷 */
    public static final String SEARCH_SHIPMENT = "1";

    /** 処理フラグ(1:出荷登録検索 1以外:受注一覧検索/入金登録検索) */
    private String shipmentSearch = SEARCH_UNSHIPMENT;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 期間種別 */
    private String termType;

    /** 受注状態 */
    private String orderStatus;

    /** キャンセルフラグ */
    private String cancelFlag;

    /** 保留中フラグ */
    private String waitingFlag;

    /** 受注番号 */
    private String orderCode;

    /** 受注番号（複数番号検索用） */
    private List<String> orderCodeList;

    /** 受注種別選択リスト */
    private List<String> orderTypeList;

    /** 受注サイト種別 */
    private List<String> orderSiteTypeList;

    /** 検査キット番号 */
    private List<String> examKitCodeList;

    /** 検体番号 */
    private String specimenCode;

    /** 検査状態 */
    private String examStatus;

    /**
     * 期間<br/>
     * <pre>
     * ・受注日    ："1"=期間From～期間To、"2"=期間From～、"3"=～期間To
     * ・出荷登録日："4"=期間From～期間To、"5"=期間From～、"6"=～期間To
     * ・入金日    ："7"=期間From～期間To、"8"=期間From～、"9"=～期間To
     * ・更新日    ："13"=期間From～期間To、"14"=期間From～、"15"=～期間To
     * ・支払期限日："16"=期間From～期間To、"17"=期間From～、"18"=～期間To
     * ・お届け希望日："19"=期間From～期間To、"20"=期間From～、"21"=～期間To
     * ・キャンセル日時："22"=期間From～期間To、"23"=期間From～、"24"=～期間To
     * </pre>
     */
    private String timeType;

    /** 期間From */
    private Timestamp timeFrom;

    /** 期間To */
    private Timestamp timeTo;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** 氏名 */
    private String searchName;

    /** ご注文主フリガナ */
    private String orderKana;

    /** 電話番号 */
    private String searchTel;

    /** ご注文主連絡先電話番号 */
    private String orderContactTel;

    /** お客様メールアドレス */
    private String customerMail;

    /** お届け先氏名 */
    private String receiverName;

    /** お届け先フリガナ */
    private String receiverKana;

    /** お届け先電話番号 */
    private String receiverTel;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 商品コード */
    private String goodsCode;

    /** JANコード */
    private String janCode;

    /** 商品名 */
    private String goodsGroupName;

    /** キャンセルした商品を含むフラグ */
    private boolean includeCancelGoodsFlag;

    /** 商品個別配送種別 */
    private String individualDeliveryType;

    /** 販売開始日時From */
    private Timestamp saleStartTimeFrom;

    /** 販売開始日時To */
    private Timestamp saleStartTimeTo;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 伝票番号 */
    private String deliveryCode;

    /** 出荷状態 */
    private List<String> shipmentStatus;

    /** 決済条件セレクト */
    private Integer settlementSelect = 0;

    /** 決済方法SEQ */
    private Integer settlementMethodSeq;

    /** 請求種別 */
    private String billType;

    /** 請求状態 */
    private String billStatus;

    /** 異常フラグ */
    private String emergencyFlag;

    /** 入金状態 */
    private String paymentStatus;

    /** 支払期限切れ */
    private Boolean timeLimitOver;

    /** クーポン選択 */
    private String couponSelect;

    /** クーポンID*/
    private String couponId;

    /** クーポンコード*/
    private String couponCode;

    /** 全件入金フラグ
     * <pre>
     * 受注検索画面で「全件入金済みにする」が選択された場合のみtrueになる
     * trueの場合「受注金額<>入金累計」を検索条件に加える
     * </pre>
     * */
    private boolean allPaymentRegistFlag;

    /** 全件出荷フラグ
     * <pre>
     * 受注検索画面で「全件出荷済みにする」が選択された場合のみtrueになる
     * trueの場合「出荷状態=未出荷」を検索条件に加える
     * </pre>
     * */
    private boolean allShipmentRegistFlag;

    /*-- 受注商品明細選択出力用 --*/
    /** 商品SEQ */
    private Integer goodsSeq;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** 商品単価 */
    private BigDecimal goodsPrice;

    /** 商品点数 */
    private Integer goodsCount;

    /** 受注商品単位で絞り込むフラグ */
    private boolean filterOrderedProductFlag;

    /************************************
     **  カスタムアラート用
     ************************************/
    /**
     * 検索アクション種別<br />
     * <li>【デフォルト】受注番号別一覧：actionOrder IndexPage#SEARCH_ACTION_TYPE_ORDER</li>
     * <li>商品別一覧：actionGoods IndexPage#SEARCH_ACTION_TYPE_GOODS</li>
     * <li>入金登録：actionPayment IndexPage#SEARCH_ACTION_TYPE_PAYMENT</li>
     * <li>出荷登録：actionShipment IndexPage#SEARCH_ACTION_TYPE_SHIPMENT</li>
     *
     */
    private String searchActionType = "actionOrder";

    /** 受注金額From */
    private String orderPriceFrom;

    /** 受注金額To */
    private String orderPriceTo;

}
