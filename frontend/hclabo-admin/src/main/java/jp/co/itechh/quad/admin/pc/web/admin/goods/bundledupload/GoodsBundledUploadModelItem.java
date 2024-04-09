/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload;

import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品管理 商品一括アップロード結果ページアイテムクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsBundledUploadModelItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 行番号<br/>
     */
    private int rowNo;

    /**
     * 項目名称<br/>
     */
    private String columnName;

    /**
     * エラー内容<br/>
     */
    private String failReason;

    /**
     * 商品管理番号(CSVアップロード対象でない規格の場合)<br/>
     */
    private String goodsGroupCode;

    /**
     * 商品番号(CSVアップロード対象でない規格の場合)<br/>
     */
    private String goodsCode;

    /**
     * 項目名判定
     *
     * @return true=有、false=無
     */
    public boolean isColumn() {
        return !StringUtil.isEmpty(columnName);
    }

    /**
     * CSVアップロード対象規格判定
     *
     * @return true=アップロード対象規格、false=アップロード対象外規格
     */
    public boolean isUploadData() {
        return (rowNo > 0);
    }

    /**
     * CSVアップロード対象なし商品管理番号＆商品番号有無
     *
     * @return true=CSVアップロード対象なし商品管理番号＆商品番号有
     *         false=CSVアップロード対象なし商品管理番号＆商品番号なし
     */
    public boolean isExistGoodsData() {
        return (rowNo == -1 && goodsGroupCode != null && !"".equals(goodsGroupCode) && goodsCode != null && !"".equals(
                        goodsCode));
    }
}