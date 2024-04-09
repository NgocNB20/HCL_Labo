/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *  商品画像処理エラーCSVダウンロードDtoクラス
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsImageProcErrorCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品管理番号 */
    @CsvColumn(order = 10, columnLabel = "商品管理番号")
    private String goodsGroupCode;

    /** エラー概要 */
    @CsvColumn(order = 30, columnLabel = "エラー概要")
    private String explain;

    /** エラー詳細 */
    @CsvColumn(order = 40, columnLabel = "エラー詳細")
    private String details;

    /** 画像ファイル名 */
    @CsvColumn(order = 50, columnLabel = "画像ファイル名")
    private String imageFileName;

}
