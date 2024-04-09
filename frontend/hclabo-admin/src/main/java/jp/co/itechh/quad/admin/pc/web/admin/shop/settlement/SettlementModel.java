package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayBottomGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayDownGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayTopGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayUpGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class SettlementModel extends AbstractModel {

    /**
     * 決済方法エンティティリスト
     */
    private List<SettlementModelItem> resultItems;

    /**
     * 表示順値
     */
    @NotNull(message = "移動する決済方法 を選択してください。",
             groups = {DisplayUpGroup.class, DisplayDownGroup.class, DisplayTopGroup.class, DisplayBottomGroup.class})
    private Integer orderDisplay;

    /**
     * @return orderDisplayRadioValue
     */
    public String getOrderDisplayRadioValue() {
        return String.valueOf(orderDisplay);
    }

    /**
     * @return true=決済方法リストあり
     */
    public boolean isResult() {
        return resultItems != null && !resultItems.isEmpty();
    }

}