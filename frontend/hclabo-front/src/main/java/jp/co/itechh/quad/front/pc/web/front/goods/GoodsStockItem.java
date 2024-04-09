/**
 *
 */
package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 在庫商品Item
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@Component
@Scope("prototype")
@Primary
public class GoodsStockItem extends GoodsItem {

    /** 在庫管理フラグ*/
    private HTypeStockManagementFlag stockManagementFlag;

    // HTMLへの追加のみで表示できるよう保持
    /** 在庫状態文字列 */
    private String stockTextType;

    // HTMLへの追加のみで表示できるよう保持
    /** 商品個別配送種別 */
    private HTypeIndividualDeliveryType individualDeliveryType;

    /** 規格管理フラグ */
    private String unitManagementFlag;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /**
     * 規格管理チェック
     *
     * @return true:規格管理する
     */
    public boolean isUnitManage() {
        return HTypeUnitManagementFlag.ON.getValue().equals(unitManagementFlag);
    }

    /**
     * 商品規格２の存在チェック
     *
     * @return true:あり
     */
    public boolean isUseUnit2() {
        return StringUtils.isNotEmpty(getUnitTitle2());
    }

    /**
     * 在庫状態：在庫あり
     *
     * @return true.在庫あり
     */
    public boolean isStockPossibleSales() {
        return HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue().equals(stockTextType);
    }

    /**
     * 在庫状態：残小
     *
     * @return true.残小
     */
    public boolean isStockFew() {
        return HTypeStockStatusType.STOCK_FEW.getValue().equals(stockTextType);
    }

    /**
     * 在庫状態：在庫切れ
     *
     * @return true.在庫切れ
     */
    public boolean isStockNoStock() {
        return HTypeStockStatusType.STOCK_NOSTOCK.getValue().equals(stockTextType);
    }

}