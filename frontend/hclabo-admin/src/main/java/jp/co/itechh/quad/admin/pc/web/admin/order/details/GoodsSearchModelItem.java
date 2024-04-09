/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSearchResultForOrderRegistResponse;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 「新規受注：商品検索」画面の検索結果を保持するクラス<br/>
 *
 * @author nakamura
 * @author matsumoto(itec) 2012/02/06 #2761 対応
 */
@Data
@Component
@Scope("prototype")
public class GoodsSearchModelItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // 保持する情報
    private GoodsSearchResultForOrderRegistResponse resultGoodsSearchResultResponse;

    /************************************
     ** 検索結果項目
     ************************************/

    /** チェックボックス */
    private boolean resultGoodsCheck;

    /** No */
    private Integer resultNo;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品管理番号 */
    private String goodsGroupCode;

    /** 商品番号 */
    private String resultGoodsCode;

    /** JANコード */
    private String resultJanCode;

    /** 商品名 */
    private String resultGoodsGroupName;

    /** 規格1 */
    private String resultUnitValue1;

    /** 規格2 */
    private String resultUnitValue2;

    /** 単価（税抜き） */
    private BigDecimal resultGoodsPrice;

    /** 商品個別配送種別 */
    private String resultIndividualDeliveryType;

    /** 販売可能在庫数情報（表示のみ） */
    private BigDecimal resultSalesPossibleStock;

    /** 在庫管理フラグ */
    private String resultStockManagementFlag;

    /**
     * 在庫管理の有無の表示判定<br/>
     *
     * @return true=在庫管理有, false=在庫管理無
     */
    public boolean isStockManagementFlag() {
        return HTypeStockManagementFlag.ON.getValue().equals(resultStockManagementFlag);
    }

}
