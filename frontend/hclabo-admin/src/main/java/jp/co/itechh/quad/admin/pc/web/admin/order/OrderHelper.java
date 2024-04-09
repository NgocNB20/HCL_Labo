package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.admin.dto.common.CheckMessageDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchConditionDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchOrderResultDto;
import jp.co.itechh.quad.admin.dto.order.ShipmentRegistDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.OptionDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OptionContent;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionUpdateRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionShipmentInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 受注検索画面Helper<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderHelper {

    /** 日付関連Utility取得 */
    private final DateUtility dateUtility;

    /** 変換Utility取得 */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param dateUtility       日付関連Utility取得
     * @param conversionUtility 変換Utility取得
     */
    @Autowired
    public OrderHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 時間種別<br/>
     *
     * @param conditionDto 検索条件Dto
     * @return 時間種別
     */
    protected String getTimeType(OrderSearchConditionDto conditionDto) {

        // 指定なし 又は数字でない場合 null
        if (!StringUtil.isNumber(conditionDto.getTimeType())) {
            return null;
        }

        // 時間で
        String timeType = null;
        Integer type = Integer.valueOf(conditionDto.getTimeType());
        if (type < 1) {
            /* 0 */
            timeType = null;
        } else if (type < 4) {
            /* 1～3 */
            timeType = "1";
        } else if (type < 7) {
            /* 4～6 */
            timeType = "2";
        } else if (type < 10) {
            /* 7～9 */
            timeType = "3";
        } else if (type < 13) {
            /* 10～12 */
            timeType = "4";
        } else if (type < 16) {
            /* 13～15 */
            timeType = "5";
        } else if (type < 19) {
            /* 16～18 */
            timeType = "6";
        } else if (type < 22) {
            /* 19～21 */
            timeType = "7";
        } else if (type < 25) {
            /* 22～24 */
            timeType = "8";
        }
        return timeType;
    }

    /**
     * 検索条件を画面に反映
     * 再検索用
     *
     * @param conditionDto 会員検索条件Dto
     * @param orderModel   会員検索ページ
     */
    public void toPageForLoad(OrderSearchConditionDto conditionDto, OrderModel orderModel) {

        /* 各検索条件を画面に反映する */
        // サイト
        List<String> orderSiteTypeList = conditionDto.getOrderSiteTypeList();
        if (orderSiteTypeList != null && !orderSiteTypeList.isEmpty()) {
            String[] orderSiteTypeArray = new String[orderSiteTypeList.size()];
            orderModel.setOrderSiteTypeArray(orderSiteTypeList.toArray(orderSiteTypeArray));
        }

        /* 受注 */
        // 受注番号
        orderModel.setConditionOrderCode(conditionDto.getOrderCode());
        // 受注種別
        List<String> orderTypeList = conditionDto.getOrderTypeList();
        if (orderTypeList != null && !orderTypeList.isEmpty()) {
            String[] orderTypeArray = new String[orderTypeList.size()];
            orderModel.setOrderTypeArray(orderTypeList.toArray(orderTypeArray));
        }
        // 複数受注番号が入力されているなら改行区切りでテキストエリアに出力
        if (conditionDto.getOrderCodeList() != null && !conditionDto.getOrderCodeList().isEmpty()) {
            orderModel.setConditionOrderCodeList(StringUtils.join(conditionDto.getOrderCodeList().toArray(), "\n"));
        } else {
            orderModel.setConditionOrderCodeList(null);
        }

        /* 期間 */
        // 受注日(対象日)
        String timeType = getTimeType(conditionDto);
        orderModel.setTimeType(timeType);
        // From(日付)
        orderModel.setDateFrom(dateUtility.formatYmdWithSlash(conditionDto.getTimeFrom()));
        // To（日付）
        orderModel.setDateTo(dateUtility.formatYmdWithSlash(conditionDto.getTimeTo()));
        if (!isNotSearchPeriodTime(timeType)) {
            // 検索対象項目が時間指定ありの場合、期間の日時を設定する
            // From（時間）
            orderModel.setTimeFrom(dateUtility.formatHM(conditionDto.getTimeFrom()));
            // To（時間）
            orderModel.setTimeTo(dateUtility.formatHM(conditionDto.getTimeTo()));
        }

        /* 会員情報 */
        // 会員SEQ
        if (conditionDto.getMemberInfoSeq() != null) {
            orderModel.setMemberInfoSeq(conditionDto.getMemberInfoSeq().toString());
        }

        /* お客様情報 */
        if (!StringUtil.isEmpty(conditionDto.getSearchName())) {
            // お客様
            orderModel.setSearchName(conditionDto.getSearchName());
        }
        if (!StringUtil.isEmpty(conditionDto.getSearchTel())) {
            // お客様
            orderModel.setSearchTel(conditionDto.getSearchTel());
        }
        if (!StringUtil.isEmpty(conditionDto.getCustomerMail())) {
            // お客様
            orderModel.setCustomerMail(conditionDto.getCustomerMail());
        }

        /* 商品情報 */
        // 個別配送
        String individual = conditionDto.getIndividualDeliveryType();
        if ("1".equals(individual)) {
            orderModel.setIndividualDeliveryType(true);
        } else {
            orderModel.setIndividualDeliveryType(false);
        }
        // 商品管理番号
        orderModel.setGoodsGroupCode(conditionDto.getGoodsGroupCode());
        // 商品番号
        orderModel.setGoodsCode(conditionDto.getGoodsCode());
        // JAN・カタログコード
        orderModel.setJanCode(conditionDto.getJanCode());
        // 商品名
        orderModel.setGoodsGroupName(conditionDto.getGoodsGroupName());
        // 販売開始日From
        orderModel.setSaleStartDateFrom(dateUtility.formatYmdWithSlash(conditionDto.getSaleStartTimeFrom()));
        // 販売開始時間From
        orderModel.setSaleStartTimeFrom(dateUtility.formatHM(conditionDto.getSaleStartTimeFrom()));
        // 販売開始日To
        orderModel.setSaleStartDateTo(dateUtility.formatYmdWithSlash(conditionDto.getSaleStartTimeTo()));
        // 販売開始時間To
        orderModel.setSaleStartTimeTo(dateUtility.formatHM(conditionDto.getSaleStartTimeTo()));

        /* 配送方法 */
        // 配送方法
        if (conditionDto.getDeliveryMethodSeq() != null) {
            orderModel.setDelivery(conditionDto.getDeliveryMethodSeq().toString());
        }
        // 伝票番号
        orderModel.setDeliveryCode(conditionDto.getDeliveryCode());
        /* 出荷状態 */
        orderModel.setShipmentStatus(conditionDto.getShipmentStatus().toArray(new String[0]));

        /* 決済方法 */
        String billType = conditionDto.getBillType();
        if (StringUtil.isNotEmpty(billType)) {
            // 決済種別
            orderModel.setSettlementSelect(1);
            // 決済方法
            orderModel.setBillType(conditionDto.getBillType());
        } else {
            // 決済種別 デフォルト
            orderModel.setSettlementSelect(0);
            // 決済請求
            Integer settlementMethodSeq = conditionDto.getSettlementMethodSeq();
            if (settlementMethodSeq != null) {
                orderModel.setSettlememnt(conditionDto.getSettlementMethodSeq().toString());
            }
        }

        /* 入金状態 */
        orderModel.setPaymentStatus(conditionDto.getPaymentStatus());
        // クーポン
        orderModel.setCouponSelect(conditionDto.getCouponSelect());
        if (OrderModel.COUPON_SELECT_COUPON_ID.equals(conditionDto.getCouponSelect())) {
            // クーポンID
            orderModel.setCoupon(conditionDto.getCouponId());
        } else {
            // クーポンコード
            orderModel.setCoupon(conditionDto.getCouponCode());
        }
    }

    /**
     * 検索結果をページに反映<br/>
     *
     * @param resultDtoList 検索結果リスト
     * @param orderModel    受注検索ページ
     */
    public void toPageForSearch(List<OrderSearchOrderResultDto> resultDtoList,
                                OrderModel orderModel,
                                OrderSearchConditionDto orderSearchConditionDto) {

        // オフセット + 1をNoにセット
        //int index = orderSearchConditionDto.getOffset() + 1 ;
        int index = orderModel.getPageInfo().getOffset() + 1;
        List<OrderModelItem> resultItems = new ArrayList<>();
        Iterator<OrderSearchOrderResultDto> ite = resultDtoList.iterator();
        for (; ite.hasNext(); index++) {
            OrderModelItem orderModelItem = ApplicationContextUtility.getBean(OrderModelItem.class);
            OrderSearchOrderResultDto resultDto = ite.next();

            orderModelItem.setResultNo(Integer.valueOf(index));

            // 受注SEQ
            orderModelItem.setResultOrderSeq(resultDto.getOrderSeq());
            // 受注履歴連番
            orderModelItem.setResultOrderVersionNo(resultDto.getOrderVersionNo());
            // 注文連番
            orderModelItem.setResultOrderConsecutiveNo(resultDto.getOrderConsecutiveNo());
            // 受注番号
            orderModelItem.setOrderCode(resultDto.getOrderCode());
            // 受注種別
            if (resultDto.getOrderType() != null) {
                orderModelItem.setOrderType(resultDto.getOrderType().getValue());
            }
            // 受注日時
            orderModelItem.setResultOrderTime(resultDto.getOrderTime());
            // 受注状態
            orderModelItem.setResultOrderStatusForSearchResult(resultDto.getOrderStatusForSearchResult());
            // ご注文主氏名
            orderModelItem.setResultOrderName(resultDto.getOrderName());
            // お届け先氏名
            orderModelItem.setResultReceiverName(resultDto.getShippingName());
            // 受注金額
            orderModelItem.setResultOrderPrice(resultDto.getOrderPrice());
            // 決済方法
            orderModelItem.setResultSettlementMethod(resultDto.getPaymentMethodName());
            // リンク決済手段名
            if (!StringUtils.isBlank(resultDto.getLinkPaymentName())) {
                orderModelItem.setLinkPaymentMethod(resultDto.getLinkPaymentName());
            }
            //伝票番号
            orderModelItem.setRegisterDeliveryCode(resultDto.getDeliveryCode());
            // 出荷日
            if (resultDto.getShipmentdate() != null) {
                orderModelItem.setShippedDate(resultDto.getShipmentdate());
            }
            // 出荷状態
            String shipmentStatus = resultDto.getShipmentStatus();
            if (shipmentStatus != null) {
                orderModelItem.setResultShipmentStatus(shipmentStatus);
                HTypeShipmentStatus shipmentStatusType =
                                EnumTypeUtil.getEnumFromValue(HTypeShipmentStatus.class, shipmentStatus);
                if (HTypeShipmentStatus.UNSHIPMENT.equals(shipmentStatusType)) {
                    orderModelItem.setShipmentStatusStyleClass(orderModel.getUnShipmentStyleClass());
                } else if (HTypeShipmentStatus.SHIPPED.equals(shipmentStatusType)) {
                    orderModelItem.setShipmentStatusStyleClass(orderModel.getShipedStyleClass());
                }
            }
            // 入金状態
            String paymentStatus = resultDto.getPaymentStatus();
            orderModelItem.setResultPaymentStatus(paymentStatus);
            if ("1".equals(paymentStatus)) {
                orderModelItem.setPaymentStatusStyleClass(orderModel.getUnPaidStyleClass());
            } else if ("2".equals(paymentStatus)) {
                orderModelItem.setPaymentStatusStyleClass(orderModel.getPaidStyleClass());
            } else if ("3".equals(paymentStatus) || "4".equals(paymentStatus)) {
                orderModelItem.setPaymentStatusStyleClass(orderModel.getPayingStyleClass());
            }

            // 入金累計
            orderModelItem.setResultReceiptPriceTotal(resultDto.getReceiptPriceTotal());
            // 入金日時
            orderModelItem.setResultReceiptTime(resultDto.getReceiptTime());
            // 配送方法
            orderModelItem.setResultDeliveryMethod(resultDto.getShippingMethodName());
            // サイト
            if (resultDto.getOrderSiteType() != null) {
                orderModelItem.setResultOrderSiteType(resultDto.getOrderSiteType().getValue());
            }
            // 備考
            if (resultDto.getDeliveryNote() != null) {
                int endIndex = 10;
                if (endIndex > resultDto.getDeliveryNote().length()) {
                    orderModelItem.setResultDeliveryNote(resultDto.getDeliveryNote());
                } else {
                    orderModelItem.setResultDeliveryNote(resultDto.getDeliveryNote().substring(0, endIndex));
                }
            }
            // 予約配送
            if (resultDto.getReservationDeliveryFlag() == null) {
                orderModelItem.setReservationDeliveryFlag(null);
            } else {
                orderModelItem.setReservationDeliveryFlag(resultDto.getReservationDeliveryFlag().getValue());
            }

            resultItems.add(orderModelItem);
        }
        orderModel.setResultItems(resultItems);
    }

    /**
     * 受注検索一覧用条件Dtoをページにセット
     *
     * @param orderModel 受注検索ページ
     */
    public void toOrderSearchListConditionDtoForPage(OrderModel orderModel) {
        orderModel.setOrderSearchConditionDto(toOrderSearchListConditionDto(orderModel));
    }

    /**
     * 受注検索一覧用条件Dto作成
     *
     * @param orderModel 受注検索ページ
     * @return orderSearchListConditionDto 受注検索一覧用条件Dto
     */
    public OrderSearchConditionDto toOrderSearchListConditionDto(OrderModel orderModel) {

        OrderSearchConditionDto orderSearchListConditionDto =
                        ApplicationContextUtility.getBean(OrderSearchConditionDto.class);
        // メッセージコードリスト初期化
        orderModel.setMsgCodeList(new ArrayList<>());
        orderModel.setMsgArgMap(new HashMap<>());

        // サイト
        orderSearchListConditionDto.setOrderSiteTypeList(Arrays.asList(orderModel.getOrderSiteTypeArray()));
        // 受注番号
        orderSearchListConditionDto.setOrderCode(orderModel.getConditionOrderCode());
        // 受注種別
        if (orderModel.getOrderTypeArray() != null && orderModel.getOrderTypeArray().length > 0) {
            orderSearchListConditionDto.setOrderTypeList(Arrays.asList(orderModel.getOrderTypeArray()));
        }
        // 受注番号（複数番号検索用）
        if (orderModel.getConditionOrderCodeList() != null && StringUtil.isNotEmpty(
                        orderModel.getConditionOrderCodeList().replaceAll("[\\s|　]", ""))) {
            // 検索に有効な文字列が存在する場合
            // 選択肢区切り文字を設定ファイルから取得
            String divchar = PropertiesUtil.getSystemPropertiesValue("order.search.order.code.list.divchar");
            String orderCodeList = orderModel.getConditionOrderCodeList();
            // 空白を削除する
            orderCodeList = orderCodeList.replaceAll("[ 　\t\\x0B\f]", "");
            // 2つ以上連続した改行コードを1つにまとめる
            orderCodeList = orderCodeList.replaceAll("(" + divchar + "){2,}", "\n");
            // 先頭または最後尾の改行コードを削除する
            orderCodeList = orderCodeList.replaceAll("^[" + divchar + "]+|[" + divchar + "]$", "");
            // 検索用複数番号を配列化する
            String[] orderCodeArray = orderCodeList.split(divchar);
            if (orderCodeArray.length > 0) {
                orderSearchListConditionDto.setOrderCodeList(Arrays.asList(orderCodeArray));

                // 受注番号桁数チェック
                for (String orderCode : orderCodeArray) {
                    if (orderCode.length() > 14) {
                        orderModel.getMsgCodeList().add("AOX000114E");
                        orderModel.getMsgArgMap().put("AOX000114E", new String[] {"受注番号(複数検索用)", "14"});
                        break;
                    }
                }

                // 受注番号数チェック(最大番号数はプロパティから取得)
                if (orderCodeArray.length > OrderModel.CONDITION_ORDER_CODE_LIST_LIMIT) {
                    orderModel.getMsgCodeList().add("AOX000115E");
                    orderModel.getMsgArgMap()
                              .put("AOX000115E", new String[] {Integer.toString(
                                              OrderModel.CONDITION_ORDER_CODE_LIST_LIMIT), "受注番号(複数検索用)"});
                }
            }
        }

        // 検査キット番号（複数番号検索用）
        if (orderModel.getConditionExamKitCodeList() != null && StringUtil.isNotEmpty(
                        orderModel.getConditionExamKitCodeList().replaceAll("[\\s|　]", ""))) {
            // 検索に有効な文字列が存在する場合
            // 選択肢区切り文字を設定
            String divChar = orderModel.getExamKitCodeDivChar();
            String examKitCodeList = orderModel.getConditionExamKitCodeList();
            // 空白を削除する
            examKitCodeList = examKitCodeList.replaceAll("[ 　\t\\x0B\f]", "");
            // 2つ以上連続した改行コードを1つにまとめる
            examKitCodeList = examKitCodeList.replaceAll("(" + divChar + "){2,}", "\n");
            // 先頭または最後尾の改行コードを削除する
            examKitCodeList = examKitCodeList.replaceAll("^[" + divChar + "]+|[" + divChar + "]$", "");
            // 検索用複数番号を配列化する
            String[] examKitCodeArray = examKitCodeList.split(divChar);
            if (examKitCodeArray.length > 0) {
                orderSearchListConditionDto.setExamKitCodeList(Arrays.asList(examKitCodeArray));

                // 検査キット番号桁数チェック
                for (String examKitCode : examKitCodeArray) {
                    if (examKitCode.length() > OrderModel.EXAM_KIT_CODE_LENGTH) {
                        orderModel.getMsgCodeList().add("AOX000114E");
                        orderModel.getMsgArgMap()
                                  .put("AOX000114E", new String[] {"検査キット番号(複数検索用)",
                                                  Integer.toString(OrderModel.EXAM_KIT_CODE_LENGTH)});
                        break;
                    }
                }

                // 検査キット番号数チェック(最大番号数はプロパティから取得)
                if (examKitCodeArray.length > orderModel.getExamKitCodeListLength()) {
                    orderModel.getMsgCodeList().add("AOX000115E");
                    orderModel.getMsgArgMap()
                              .put("AOX000115E", new String[] {Integer.toString(orderModel.getExamKitCodeListLength()),
                                              "検査キット番号(複数検索用)"});
                }
            }
        }

        // 受注商品単位で絞り込む
        orderSearchListConditionDto.setFilterOrderedProductFlag(orderModel.isFilterOrderedProductFlag());

        // 検査状態
        if (orderModel.getExamStatus() != null) {
            orderSearchListConditionDto.setExamStatus(orderModel.getExamStatus());
        }

        // 検体番号
        if (orderModel.getSpecimenCode() != null) {
            orderSearchListConditionDto.setSpecimenCode(orderModel.getSpecimenCode());
        }

        // 支払期限切れ
        orderSearchListConditionDto.setTimeLimitOver(orderModel.getTimeLimitOver());
        // 受注状態
        // ココでの受注状態は検索条件の受注状態を表す（core/src/main/resources/selectMap.dicon を参照）
        if ("5".equals(orderModel.getOrderStatus())) {
            // キャンセル
            orderSearchListConditionDto.setCancelFlag("1");
        } else if ("6".equals(orderModel.getOrderStatus())) {
            // 保留中
            orderSearchListConditionDto.setCancelFlag("0");
            orderSearchListConditionDto.setWaitingFlag("1");
        } else if ("7".equals(orderModel.getOrderStatus())) {
            // 請求決済エラー
            orderSearchListConditionDto.setEmergencyFlag("1");
        } else if ("8".equals(orderModel.getOrderStatus())) {
            // キャンセル以外
            orderSearchListConditionDto.setCancelFlag("0");
        } else if (orderModel.getOrderStatus() != null) {
            // 「入金確認中」、「商品準備中」、「出荷完了」
            orderSearchListConditionDto.setCancelFlag("0");
            orderSearchListConditionDto.setWaitingFlag("0");
            orderSearchListConditionDto.setEmergencyFlag("0");
            orderSearchListConditionDto.setOrderStatus(orderModel.getOrderStatus());
        }
        // 期間
        Integer timetype = conversionUtility.toInteger(orderModel.getTimeType());

        if (isNotSearchPeriodTime(orderModel.getTimeType())) {
            // 検索対象項目が時間指定なしの項目の場合、期間の時間を初期化する
            // From（時間）
            orderModel.setTimeFrom(null);
            // To（時間）
            orderModel.setTimeTo(null);
        } else {
            if (timetype != null) {
                // 検索対象項目が時間指定あり、かつ時間が未入力の場合、期間の時間を補完する
                // From（時間）
                if (!StringUtil.isEmpty(orderModel.getDateFrom()) && StringUtil.isEmpty(orderModel.getTimeFrom())) {
                    orderModel.setTimeFrom("00:00");
                }
                // To（時間）
                if (!StringUtil.isEmpty(orderModel.getDateTo()) && StringUtil.isEmpty(orderModel.getTimeTo())) {
                    orderModel.setTimeTo("23:59");
                }
            }
        }

        Timestamp fromTime = null;
        Timestamp toTime = null;
        if (!StringUtil.isEmpty(orderModel.getDateFrom())) {
            fromTime = conversionUtility.toTimeStampWithDefaultHms(orderModel.getDateFrom(),
                                                                   conversionUtility.addStartSecond(
                                                                                   orderModel.getTimeFrom()),
                                                                   ConversionUtility.DEFAULT_START_TIME
                                                                  );
        }
        if (!StringUtil.isEmpty(orderModel.getDateTo())) {
            toTime = conversionUtility.toTimeStampWithDefaultHms(orderModel.getDateTo(),
                                                                 conversionUtility.addEndSecond(orderModel.getTimeTo()),
                                                                 ConversionUtility.DEFAULT_END_TIME
                                                                );
        }

        if ((fromTime != null || toTime != null) && timetype == null) {
            orderModel.getMsgCodeList().add("AOX000105");
        }

        Integer type = null;
        if (timetype != null) {
            switch (timetype) {
                case 1: // 受注日時
                    type = 1;
                    break;
                case 2: // 出荷登録日時
                    type = 4;
                    break;
                case 3: // 入金日時
                    type = 7;
                    break;
                case 5: // 更新日時
                    type = 13;
                    break;
                case 6: // 支払期限日
                    type = 16;
                    break;
                case 7: // お届け希望日
                    type = 19;
                    break;
                case 8: // キャンセル日時
                    type = 22;
                    break;
                case 9: // 受付日
                    type = 25;
                    break;
                default:
                    type = null;
                    break;
            }

            if (fromTime != null && toTime != null) {
            } else if (fromTime != null) {
                type++;
                toTime = null;
            } else if (toTime != null) {
                type += 2;
                fromTime = null;
            } else {
                type = null;
                fromTime = null;
                toTime = null;
                orderModel.getMsgCodeList().add("AOX000101");
            }
            if (type != null) {
                orderSearchListConditionDto.setTimeType(type.toString());
            } else {
                orderSearchListConditionDto.setTimeType(null);
            }
            orderSearchListConditionDto.setTimeFrom(fromTime);
            orderSearchListConditionDto.setTimeTo(toTime);
        }

        // 会員情報
        // 会員SEQ
        if (StringUtil.isNotEmpty(orderModel.getMemberInfoSeq())) {
            orderSearchListConditionDto.setMemberInfoSeq(Integer.valueOf(orderModel.getMemberInfoSeq()));
        }

        // お客様 //
        // 名前
        String searchName = null;
        if (orderModel.getSearchName() != null) {
            searchName = orderModel.getSearchName().replace(" ", "").replace("　", "");
            orderSearchListConditionDto.setSearchName(searchName);
        }
        // TEL
        String searchTel = null;
        if (orderModel.getSearchTel() != null) {
            searchTel = orderModel.getSearchTel().replace(" ", "").replace("　", "");
            orderSearchListConditionDto.setSearchTel(searchTel);
        }
        // E-MAIL
        if (orderModel.getCustomerMail() != null) {
            orderSearchListConditionDto.setCustomerMail(orderModel.getCustomerMail());
        }

        // 商品情報
        // 商品管理番号
        orderSearchListConditionDto.setGoodsGroupCode(orderModel.getGoodsGroupCode());
        // 商品番号
        orderSearchListConditionDto.setGoodsCode(orderModel.getGoodsCode());
        // JANコード・カタログコード
        orderSearchListConditionDto.setJanCode(orderModel.getJanCode());
        // 商品名
        orderSearchListConditionDto.setGoodsGroupName(orderModel.getGoodsGroupName());
        // 販売開始日
        if (StringUtil.isNotEmpty(orderModel.getSaleStartDateFrom())) {
            orderSearchListConditionDto.setSaleStartTimeFrom(
                            conversionUtility.toTimeStampWithDefaultHms(orderModel.getSaleStartDateFrom(),
                                                                        conversionUtility.addStartSecond(
                                                                                        orderModel.getSaleStartTimeFrom()),
                                                                        ConversionUtility.DEFAULT_START_TIME
                                                                       ));
        }
        // 販売開始時間
        if (StringUtil.isNotEmpty(orderModel.getSaleStartDateTo())) {
            orderSearchListConditionDto.setSaleStartTimeTo(
                            conversionUtility.toTimeStampWithDefaultHms(orderModel.getSaleStartDateTo(),
                                                                        conversionUtility.addEndSecond(
                                                                                        orderModel.getSaleStartTimeTo()),
                                                                        ConversionUtility.DEFAULT_END_TIME
                                                                       ));
        }

        // 配送方法
        if (orderModel.getDelivery() != null && !"".equals(orderModel.getDelivery())) {
            orderSearchListConditionDto.setDeliveryMethodSeq(Integer.valueOf(orderModel.getDelivery()));
        }
        // 伝票番号
        orderSearchListConditionDto.setDeliveryCode(orderModel.getDeliveryCode());

        // 出荷状態
        if (orderModel.getShipmentStatus() != null && orderModel.getShipmentStatus().length > 0) {
            orderSearchListConditionDto.setShipmentStatus(Arrays.asList(orderModel.getShipmentStatus()));
        } else {
            orderSearchListConditionDto.setShipmentStatus(null);
        }

        // 決済方法
        if (orderModel.getSettlementSelect().intValue() == 0) {
            // 決済方法
            if (orderModel.getSettlememnt() != null && !"".equals(orderModel.getSettlememnt())) {
                orderSearchListConditionDto.setSettlementMethodSeq(Integer.valueOf(orderModel.getSettlememnt()));
            }
        } else {
            // 請求種別
            orderSearchListConditionDto.setBillType(orderModel.getBillType());
        }
        // 決済方法
        orderSearchListConditionDto.setSettlementSelect(orderModel.getSettlementSelect());
        // 請求状態
        orderSearchListConditionDto.setBillStatus(orderModel.getBillStatus());
        // 入金状態
        orderSearchListConditionDto.setPaymentStatus(orderModel.getPaymentStatus());

        // クーポン
        orderSearchListConditionDto.setCouponSelect(orderModel.getCouponSelect());
        if (OrderModel.COUPON_SELECT_COUPON_ID.equals(orderModel.getCouponSelect())) {
            // クーポンID
            orderSearchListConditionDto.setCouponId(orderModel.getCoupon());
        } else {
            // クーポンコード
            orderSearchListConditionDto.setCouponCode(orderModel.getCoupon());
        }

        // 処理フラグ
        orderSearchListConditionDto.setShipmentSearch(checkBoxCheck(orderModel.isShipmentRegister()));

        orderSearchListConditionDto.setOrderPriceFrom(orderModel.getOrderPriceFrom());
        orderSearchListConditionDto.setOrderPriceTo(orderModel.getOrderPriceTo());

        return orderSearchListConditionDto;
    }

    /**
     * ページ遷移情報設定<br/>
     *
     * @param orderModel 受注検索ページ
     */
    public void toOrderSearchListConditionDtoDisplayChange(OrderModel orderModel) {
        // 検索条件にページ情報設定
        if (orderModel.getOrderSearchConditionDto() != null) {
            orderModel.getOrderSearchConditionDto().setPageInfo(orderModel.getOrderSearchConditionDto().getPageInfo());
        }
        // 検索条件作成
        toOrderSearchListConditionDtoForPage(orderModel);

    }

    /**
     * 会員詳細画面から受注検索画面に遷移した場合、会員SEQで検索を行う<br/>
     * 必要な値をセットしておく
     *
     * @param orderModel 受注検索ページ
     */
    public void toOrderSearchWhenMemberInfoDetails(OrderModel orderModel) {
        orderModel.setIndividualDeliveryType(false);
    }

    /**
     * 出荷登録処理内容のリストを作成
     *
     * @param orderModel 受注検索ページ
     * @return 出荷登録DTOリスト
     */
    public List<ShipmentRegistDto> toShipmentRegistDtoForRegist(OrderModel orderModel) {

        List<ShipmentRegistDto> paymentRegistDtoList = new ArrayList<>();

        List<OrderModelItem> resultItems = orderModel.getResultItems();
        if (resultItems == null) {
            return null;
        }

        for (OrderModelItem orderModelItem : resultItems) {
            if (orderModelItem.isResultOrderCheck()) {
                ShipmentRegistDto shipmentRegistDto = ApplicationContextUtility.getBean(ShipmentRegistDto.class);
                shipmentRegistDto.setOrderCode(orderModelItem.getOrderCode());
                shipmentRegistDto.setOrderConsecutiveNo(orderModelItem.getResultOrderConsecutiveNo());
                shipmentRegistDto.setShipmentDate(
                                conversionUtility.toTimeStamp(orderModelItem.getRegisterShipmentDate()));
                shipmentRegistDto.setDeliveryCode(orderModelItem.getRegisterDeliveryCode());
                shipmentRegistDto.setSendMailFlag(orderModelItem.getNotSendMailFlag());
                paymentRegistDtoList.add(shipmentRegistDto);
            }
        }

        return paymentRegistDtoList;
    }

    /**
     * 処理結果のメッセージを作成<br/>
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param orderModel  受注検索ページ
     */
    public void setFinishPageItem(String messageCode, Object[] args, OrderModel orderModel) {
        List<CheckMessageDto> checkMessageDtoList = new ArrayList<>();
        orderModel.setCheckMessageItems(checkMessageDtoList);
        addFinishPageItem(messageCode, args, orderModel);
    }

    /**
     * 処理結果のメッセージを追加<br/>
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param orderModel  受注検索ページ
     */
    public void addFinishPageItem(String messageCode, Object[] args, OrderModel orderModel) {
        List<CheckMessageDto> checkMessageDtoList = orderModel.getCheckMessageItems();
        if (checkMessageDtoList == null) {
            checkMessageDtoList = new ArrayList<>();
        }
        String message = AppLevelFacesMessageUtil.getAllMessage(messageCode, args).getMessage();
        CheckMessageDto checkMessageDto = ApplicationContextUtility.getBean(CheckMessageDto.class);
        checkMessageDto.setMessage(message);
        checkMessageDto.setMessageId(messageCode);
        checkMessageDtoList.add(checkMessageDto);
    }

    /**
     * 単一チェックボックスのプロパティの値をチェックする<br/>
     *
     * @param checkboxProperty 単一チェックボックスのプロパティ
     * @return true:"1",false:"0"
     */
    protected String checkBoxCheck(Boolean checkboxProperty) {
        String res = "0";
        if (checkboxProperty != null && checkboxProperty) {
            res = "1";
        }
        return res;
    }

    /**
     * メソッド概要<br/>
     * メソッドの説明・概要<br/>
     *
     * @param map Items
     * @param seq SEQ
     * @return ラベル
     */
    protected String getItemName(Map<String, String> map, Integer seq) {
        if (map.containsValue(seq.toString())) {
            return map.get("label").toString();
        }
        return null;
    }

    /**
     * メソッド概要<br/>
     * メソッドの説明・概要<br/>
     *
     * @param map Items
     * @param seq SEQ
     * @return ラベル
     */
    protected String getItemNameStr(Map<String, String> map, Integer seq) {
        if (map.containsValue(seq.toString())) {
            return map.get("label").toString();
        }
        return null;
    }

    /**
     * 選択された受注を受注SEQリストに変換します
     *
     * @param orderModel OrderModel
     * @return List&lt;Integer&gt;
     */
    public List<String> convertToListForSearch(OrderModel orderModel) {
        List<OrderModelItem> resultItems = orderModel.getResultItems();
        List<String> orderCodeList = new ArrayList<>();
        if (resultItems != null && !resultItems.isEmpty()) {
            for (OrderModelItem orderModelItem : resultItems) {
                if (orderModelItem.isResultOrderCheck()) {
                    if (orderModelItem.getOrderCode() == null) {
                        continue;
                    }
                    orderCodeList.add(orderModelItem.getOrderCode());
                }
            }
        }

        return orderCodeList;
    }

    /**
     * 選択された受注を受注SEQ + 注文連番のリストに変換します
     *
     * @param orderModel OrderModel
     * @return List&lt;Integer&gt;
     */
    public List<String> convertToListForSearchShipment(OrderModel orderModel) {
        List<OrderModelItem> resultItems = orderModel.getResultItems();
        List<String> orderSeqList = new ArrayList<>();
        if (resultItems != null && !resultItems.isEmpty()) {
            for (OrderModelItem orderModelItem : resultItems) {
                if (orderModelItem.isResultOrderCheck()) {
                    if (orderModelItem.getResultOrderSeq() == null) {
                        continue;
                    }
                    orderSeqList.add(StringConversionUtil.toString(orderModelItem.getResultOrderSeq())
                                     + StringConversionUtil.toString(orderModelItem.getResultOrderConsecutiveNo()));
                }
            }
        }

        return orderSeqList;
    }

    /**
     * 「\」を「\\」エスケープします
     *
     * @param target String
     * @return String
     */
    public String escapeStr(String target) {
        if (StringUtil.isEmpty(target)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == '\\') {
                builder.append("\\\\");
            } else {
                builder.append(target.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * 単一チェックボックスのプロパティの値をチェックする<br/>
     *
     * @param value 値
     * @return true:"1",false:"0"
     */
    protected Boolean checkBoxCheck(String value) {
        if (value != null && "1".equals(value)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 検索条件：期間で時間指定の有無を判定する<br/>
     *
     * @param periodType 期間種別
     * @return true:時間指定なし,false:時間指定あり
     */
    protected boolean isNotSearchPeriodTime(String periodType) {
        // 検索条件：期間の期間種別が「支払期限日」、「お届け希望日」の場合、時間指定なし
        if ("6".equals(periodType) || "7".equals(periodType)) {
            return true;
        }
        return false;
    }

    /**
     * 取引出荷情報リストに変換
     *
     * @param shipmentRegistDtoList 出荷登録Dtoクラスリスト
     * @return 取引出荷情報リスト
     */
    public List<TransactionShipmentInfo> toTransactionShipmentRequestList(List<ShipmentRegistDto> shipmentRegistDtoList) {
        List<TransactionShipmentInfo> transactionShipmentInfoList = new ArrayList<>();
        shipmentRegistDtoList.forEach(item -> {
            TransactionShipmentInfo transactionShipmentInfo = new TransactionShipmentInfo();
            transactionShipmentInfo.setOrderCode(item.getOrderCode());
            transactionShipmentInfo.setCompleteShipmentDate(item.getShipmentDate());
            transactionShipmentInfo.setShipmentStatusConfirmCode(item.getDeliveryCode());
            transactionShipmentInfoList.add(transactionShipmentInfo);
        });
        return transactionShipmentInfoList;
    }

    /**
     * 決済方法アイテムに変換します
     *
     * @param paymentMethodListResponse 決済方法一覧レスポンス
     * @return 決済方法アイテム
     */
    public Map<String, String> toSettlementMapList(PaymentMethodListResponse paymentMethodListResponse) {
        if (CollectionUtils.isEmpty(paymentMethodListResponse.getPaymentMethodListResponse())) {
            return null;
        }
        Map<String, String> settlementMapList = new LinkedHashMap<>();
        for (PaymentMethodResponse paymentMethodResponse : paymentMethodListResponse.getPaymentMethodListResponse()) {
            settlementMapList.put(paymentMethodResponse.getSettlementMethodSeq().toString(),
                                  paymentMethodResponse.getSettlementMethodName()
                                 );
        }
        return settlementMapList;
    }

    /**
     * 配送方法アイテムに変換します
     *
     * @param shippingMethodListResponse 配送方法一覧レスポンス
     * @return 配送方法アイテム
     */
    public Map<String, String> toDeliveryMapList(ShippingMethodListResponse shippingMethodListResponse) {
        if (CollectionUtils.isEmpty(shippingMethodListResponse.getShippingMethodListResponse())) {
            return null;
        }
        Map<String, String> deliveryMapList = new LinkedHashMap<>();
        for (ShippingMethodResponse shippingMethodResponse : shippingMethodListResponse.getShippingMethodListResponse()) {
            deliveryMapList.put(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodSeq().toString(),
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodName()
                               );
        }
        return deliveryMapList;
    }

    /**
     * CSVDL オプション リストに変換します
     *
     * @param responseList 受注検索CSVDLオプションリストレスポンス
     * @return CSVDL オプション リスト
     */
    public List<CsvDownloadOptionDto> toCsvDownloadOptionDtoList(OrderSearchCsvOptionListResponse responseList) {
        List<CsvDownloadOptionDto> csvDownloadOptionDtoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(responseList) && !CollectionUtils.isEmpty(responseList.getCsvDownloadOptionList())) {
            for (OrderSearchCsvOptionResponse response : responseList.getCsvDownloadOptionList()) {
                CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();

                csvDownloadOptionDto.setOptionId(response.getOptionId());
                csvDownloadOptionDto.setDefaultOptionName(response.getDefaultOptionName());
                csvDownloadOptionDto.setOptionName(response.getOptionName());
                csvDownloadOptionDto.setOutHeader(response.getOutHeader());
                List<OptionDto> optionDtoList = new ArrayList<>();
                for (OptionContent optionContent : Objects.requireNonNull(response.getOptionContent())) {
                    OptionDto optionDto = new OptionDto();
                    optionDto.setItemName(optionContent.getItemName());
                    optionDto.setDefaultColumnLabel(optionContent.getDefaultColumnLabel());
                    optionDto.setColumnLabel(optionContent.getColumnLabel());
                    optionDto.setDefaultOrder(optionContent.getDefaultOrder());
                    optionDto.setOrder(optionContent.getOrder());
                    optionDto.setOutFlag(optionContent.getOutFlag());
                    optionDtoList.add(optionDto);
                }
                csvDownloadOptionDto.setOptionContent(optionDtoList);

                csvDownloadOptionDtoList.add(csvDownloadOptionDto);
            }
        }
        return csvDownloadOptionDtoList;
    }

    /**
     * 受注検索CSVDLオプションの更新リクエストに変換します
     *
     * @param csvDownloadOptionDto CSVDLオプショDto
     * @return orderSearchCsvOptionUpdateRequest 受注検索CSVDLオプションの更新リクエスト
     */
    public OrderSearchCsvOptionUpdateRequest toOrderSearchCsvOptionUpdateRequest(CsvDownloadOptionDto csvDownloadOptionDto) {
        OrderSearchCsvOptionUpdateRequest orderSearchCsvOptionUpdateRequest = new OrderSearchCsvOptionUpdateRequest();
        if (!ObjectUtils.isEmpty(csvDownloadOptionDto)) {
            orderSearchCsvOptionUpdateRequest.setOptionId(csvDownloadOptionDto.getOptionId());
            orderSearchCsvOptionUpdateRequest.setDefaultOptionName(csvDownloadOptionDto.getDefaultOptionName());
            orderSearchCsvOptionUpdateRequest.setOptionName(csvDownloadOptionDto.getOptionName());
            orderSearchCsvOptionUpdateRequest.setOutHeader(csvDownloadOptionDto.getOutHeader());

            List<OptionContent> optionContentList = new ArrayList<>();
            for (OptionDto optionDto : Objects.requireNonNull(csvDownloadOptionDto.getOptionContent())) {
                OptionContent optionContent = new OptionContent();
                optionContent.setItemName(optionDto.getItemName());
                optionContent.setDefaultColumnLabel(optionDto.getDefaultColumnLabel());
                optionContent.setColumnLabel(optionDto.getColumnLabel());
                optionContent.setDefaultOrder(optionDto.getDefaultOrder());
                optionContent.setOrder(optionDto.getOrder());
                optionContent.setOutFlag(optionDto.getOutFlag());
                optionContentList.add(optionContent);
            }

            orderSearchCsvOptionUpdateRequest.setOptionContent(optionContentList);
        }

        return orderSearchCsvOptionUpdateRequest;
    }

}
