package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryGoodsManualSort;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryGoodsSort;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * CategoryGoodsSort
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class CategoryGoodsSort {

    /** オプションソート */
    @HVItems(target = HTypeCategoryGoodsSort.class)
    private String optionSort;

    /** 商品グループチェック済み */
    private List<Integer> goodsGroupChecked;

    /** 位置 */
    @Range(min = 1, max = Integer.MAX_VALUE)
    private Integer position;

    /** オプション 手動ソート */
    @HVItems(target = HTypeCategoryGoodsManualSort.class)
    private String optionSortManual;
}
