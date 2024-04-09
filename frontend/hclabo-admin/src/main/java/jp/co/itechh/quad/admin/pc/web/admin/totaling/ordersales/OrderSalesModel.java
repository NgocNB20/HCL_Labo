package jp.co.itechh.quad.admin.pc.web.admin.totaling.ordersales;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.OrderSalesDataDto;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.OrderSalesTotalDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 受注・売上集計ページ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@HVRDateGreaterEqual(target = "processDateTo", comparison = "processDateFrom")
public class OrderSalesModel extends AbstractModel {

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /**
     * 集計期間From
     */
    @NotEmpty
    @HVDate
    @HCDate
    private String processDateFrom;

    /**
     * 集計期間To
     */
    @NotEmpty
    @HVDate
    @HCDate
    private String processDateTo;

    /**
     * 集計単位
     */
    @HVItems(targetArray = {"0", "1"}, groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String aggregateUnit = "0";

    /**
     * 受注デバイスリスト
     */
    @HVItems(target = HTypeOpenDeleteStatus.class,
             groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String[] orderDeviceTypeArray;

    /**
     * 受注デバイスItems
     */
    private Map<String, String> orderDeviceTypeItems;
    /**
     * 対象データ
     */
    @HVItems(targetArray = {"0", "1"}, groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String orderStatus = "0";

    /**
     * 決済方法IDリスト
     */
    @NotEmpty
    @HVItems(target = HTypeSettlementMethodType.class,
             groups = {SearchGroup.class, AllDownloadGroup.class, DisplayChangeGroup.class})
    private String[] paymentMethodIdArray;

    /**
     * 決済方法Items
     */
    private Map<String, String> paymentMethodIdItems;

    /** 受注・売上集計データDto一覧 */
    private List<OrderSalesDataDto> resultOrderSalesDataItems;

    /** 受注・売上集計データDto */
    private OrderSalesDataDto resultOrderSalesData;

    /** 受注・売上集計Dto一覧 */
    private List<OrderSalesTotalDto> orderSalesTotalList;

    /** 受注・売上集計Dto */
    private OrderSalesTotalDto orderSalesTotal;

    /** 小計 */
    private Integer subTotal;

    /** 受注・売上集計ページCSVの出力ボタン押下フラグ */
    private boolean selectOrderSalesCSVFlag;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true =検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return resultOrderSalesDataItems != null;
    }

    /**
     * 受注・売上集計ページCSVの出力ボタン押下フラグ
     * <p>
     * このメソッド内部でフラグを初期化
     *
     * @return true=押下
     */
    public boolean isSelectOrderSalesCSVFlag() {
        if (selectOrderSalesCSVFlag) {
            selectOrderSalesCSVFlag = false;
            return true;
        }
        return selectOrderSalesCSVFlag;
    }
}