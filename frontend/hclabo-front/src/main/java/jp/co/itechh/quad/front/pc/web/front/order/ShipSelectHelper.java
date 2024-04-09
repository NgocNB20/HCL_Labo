package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethod;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethodListResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipMethodUpdateRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ご配送方法 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class ShipSelectHelper {

    /** お届け希望日選択肢-指定なし */
    public static final String NON_SELECT_VALUE = "指定なし";

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /** 変換Utilityクラス */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public ShipSelectHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 配送方法設定リクエストに変換
     *
     * @param shipSelectModel  配送方法Model
     * @param orderCommonModel 注文フロー共通Model
     * @return 配送方法設定リクエスト
     */
    public ShippingSlipMethodUpdateRequest toShippingSlipMethodUpdateRequest(ShipSelectModel shipSelectModel,
                                                                             OrderCommonModel orderCommonModel) {
        ShippingSlipMethodUpdateRequest shippingSlipMethodUpdateRequest = new ShippingSlipMethodUpdateRequest();

        shippingSlipMethodUpdateRequest.setTransactionId(orderCommonModel.getTransactionId());
        shippingSlipMethodUpdateRequest.setShippingMethodId(shipSelectModel.getShippingMethodId());
        if (!StringUtils.isEmpty(shipSelectModel.getReceiverDate())) {
            shippingSlipMethodUpdateRequest.setReceiverDate(
                            convertReceiverDate(shipSelectModel, shipSelectModel.getReceiverDate()));
        }
        shippingSlipMethodUpdateRequest.setReceiverTimeZone(shipSelectModel.getReceiverTimeZone());
        shippingSlipMethodUpdateRequest.setInvoiceNecessaryFlag(shipSelectModel.getInvoiceNecessaryFlag());

        return shippingSlipMethodUpdateRequest;
    }

    /**
     * 配送方法Modelに変換
     *
     * @param shipSelectModel                      配送方法Model
     * @param selectableShippingMethodListResponse 選択可能配送方法一覧レスポンス
     */
    public void updateShipSelectModel(ShipSelectModel shipSelectModel,
                                      SelectableShippingMethodListResponse selectableShippingMethodListResponse) {
        if (!ObjectUtils.isEmpty(selectableShippingMethodListResponse) && !CollectionUtils.isEmpty(
                        selectableShippingMethodListResponse.getSelectableShippingMethodList())) {

            List<SelectableShippingMethodItem> selectableShippingMethodList = new ArrayList<>();

            for (SelectableShippingMethod selectableShippingMethodParam : selectableShippingMethodListResponse.getSelectableShippingMethodList()) {
                SelectableShippingMethodItem selectableShippingMethodItem = new SelectableShippingMethodItem();

                selectableShippingMethodItem.setShippingMethodId(selectableShippingMethodParam.getShippingMethodId());
                selectableShippingMethodItem.setShippingMethodName(
                                selectableShippingMethodParam.getShippingMethodName());
                selectableShippingMethodItem.setShippingMethodNote(
                                selectableShippingMethodParam.getShippingMethodNote());
                selectableShippingMethodItem.setReceiverTimeZoneList(
                                selectableShippingMethodParam.getReceiverTimeZoneList());
                if (!ListUtils.isEmpty(selectableShippingMethodParam.getReceiverDateList())) {
                    selectableShippingMethodItem.setReceiverDateList(
                                    selectableShippingMethodParam.getReceiverDateList());
                }

                selectableShippingMethodList.add(selectableShippingMethodItem);
            }

            shipSelectModel.setSelectableShippingMethodList(selectableShippingMethodList);
        }
    }

    /**
     * 配送方法Modelに変換
     *
     * @param shipSelectModel      配送方法Model
     * @param shippingSlipResponse 配送伝票レスポンス
     */
    public void toShipSelectModel(ShipSelectModel shipSelectModel, ShippingSlipResponse shippingSlipResponse) {
        if (shippingSlipResponse != null) {

            shipSelectModel.setShippingSlipId(shippingSlipResponse.getShippingSlipId());
            shipSelectModel.setShippingAddressId(shippingSlipResponse.getShippingAddressId());
            shipSelectModel.setShippingMethodId(shippingSlipResponse.getShippingMethodId());
            shipSelectModel.setInvoiceNecessaryFlag(shippingSlipResponse.getInvoiceNecessaryFlag());
            if (shippingSlipResponse.getReceiverDate() != null) {
                shipSelectModel.setReceiverDate(dateUtility.getFormatMdWithWeek(
                                conversionUtility.toTimestamp(shippingSlipResponse.getReceiverDate())));
            }
            shipSelectModel.setReceiverTimeZone(shippingSlipResponse.getReceiverTimeZone());
        }
    }

    /**
     * お届け日（mm/dd(曜日)）をyyyyMMdd形式に変換
     * TODO 暫定対応
     *
     * @param shipSelectModel
     * @param receiverDate
     * @return コンバート後のreceiverDate
     */
    private String convertReceiverDate(ShipSelectModel shipSelectModel, String receiverDate) {

        // 選択されている配送方法と一致する配送方法をリストから取得
        SelectableShippingMethodItem target = shipSelectModel.getSelectableShippingMethodList()
                                                             .stream()
                                                             .filter(item -> StringUtils.isNotEmpty(
                                                                             item.getShippingMethodId())
                                                                             && item.getShippingMethodId()
                                                                                    .equals(shipSelectModel.getShippingMethodId()))
                                                             .findFirst()
                                                             .orElse(null);

        // ここでは必須チェック未実施
        if (StringUtils.isBlank(receiverDate) || NON_SELECT_VALUE.equals(receiverDate) || ObjectUtils.isEmpty(target)
            || CollectionUtils.isEmpty(target.getReceiverDateList()) || !target.getReceiverDateList()
                                                                               .contains(receiverDate)) {
            return null;
        }

        String year = dateUtility.getCurrentY();
        // 「mm/dd(曜日)」が入るので、切り分ける
        String month = receiverDate.substring(0, receiverDate.indexOf("/"));
        String day = receiverDate.substring(receiverDate.indexOf("/") + 1, receiverDate.indexOf("("));

        String convertReceiverDate =
                        year + "/" + (StringUtils.isNotBlank(month) && month.length() == 1 ? 0 + month : month) + "/"
                        + (StringUtils.isNotBlank(day) && day.length() == 1 ? 0 + day : day);

        return dateUtility.isDate(convertReceiverDate, dateUtility.YMD_SLASH) ? convertReceiverDate : null;
    }
}