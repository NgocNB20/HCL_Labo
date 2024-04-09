/*
 * Project Name : HIT-MALL
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *  商品画像処理CSVダウンロードDtoクラス
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsImageProcSuccessCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 商品グループコード */
    @CsvColumn(order = 10, columnLabel = "商品管理番号")
    private String goodsGroupCode;

    /** 画像URL */
    @CsvColumn(order = 20, columnLabel = "画像ファイルURL")
    private String imageUrl;

    /** 処理種別 */
    @CsvColumn(order = 30, columnLabel = "処理種別")
    private String mode;

    /** 商品グループ画像エンティティ */
    private GoodsGroupImageEntity goodsGroupImageEntity;

}