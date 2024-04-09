package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品検索結果画面情報<br/>
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsResultItem implements Serializable {
    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /************************************
     ** 検索結果項目
     ************************************/
    /**
     * resultGoodsCheck<br/>
     */
    private boolean resultGoodsCheck;

    /**
     * resultGoodsSeq<br/>
     */
    private Integer resultGoodsSeq;

    /**
     * No<br/>
     */
    private Integer resultNo;

    /**
     * goodsGroupCode<br/>
     */
    private String goodsGroupCode;

    /**
     * resultGoodsCode<br/>
     */
    private String resultGoodsCode;

    /**
     * resultGoodsGroupName<br/>
     */
    private String resultGoodsGroupName;

    /**
     * resultUnitValue1<br/>
     */
    private String resultUnitValue1;

    /**
     * resultUnitValue2<br/>
     */
    private String resultUnitValue2;

    /**
     * resultGoodsOpenStatusPC<br/>
     */
    private String resultGoodsOpenStatusPC;

    /**
     * resultGoodsOpenStartTimePC<br/>
     */
    private Timestamp resultGoodsOpenStartTimePC;

    /**
     * resultGoodsOpenEndTimePC<br/>
     */
    private Timestamp resultGoodsOpenEndTimePC;

    /**
     * resultSaleStatusPC<br/>
     */
    private String resultSaleStatusPC;

    /**
     * resultSaleStartTimePC<br/>
     */
    private Timestamp resultSaleStartTimePC;

    /**
     * resultSaleEndTimePC<br/>
     */
    private Timestamp resultSaleEndTimePC;

    /**
     * resultOpenEndTimePC<br/>
     */
    private String resultOpenEndTimePC;

    /**
     * resultGoodsPrice<br/>
     */
    private BigDecimal resultGoodsPrice;

    /**
     * resultStockManagementFlag<br/>
     */
    private String resultStockManagementFlag;

    /**
     * resultSalesPossibleStock<br/>
     */
    private BigDecimal resultSalesPossibleStock;

    /**
     * resultRealStock<br/>
     */
    private BigDecimal resultRealStock;

    /**
     * resultIndividualDeliveryType<br/>
     */
    private String resultIndividualDeliveryType;

    /**
     * resultFrontDisplay<br/>
     */
    private String resultFrontDisplay;

    /**
     * resultFrontDisplayReferenceDate<br/>
     */
    private Timestamp resultFrontDisplayReferenceDate;

    /**
     * 在庫表示判定<br/>
     *
     * @return true=resultStockManagementFlagが1, false=resultStockManagementFlagが0
     */
    public boolean isStockManagement() {
        return HTypeStockManagementFlag.ON.getValue().equals(resultStockManagementFlag);
    }
}