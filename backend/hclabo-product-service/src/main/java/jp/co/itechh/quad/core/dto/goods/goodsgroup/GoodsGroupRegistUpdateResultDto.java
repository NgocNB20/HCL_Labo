/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.common.FileRegistDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 商品グループ登録更新 結果Dto
 * 商品グル―プ登録更新サービスの結果を保管するDto
 *
 * @author s_tsuru
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsGroupRegistUpdateResultDto implements Serializable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** 商品グループ登録件数 */
    private int resultGoodsGroupRegist = 0;

    /** 商品グループ表示登録件数 */
    private int resultGoodsGroupDisplayRegist = 0;

    /** 商品グループ人気登録件数 */
    private int resultGoodsGroupPopularityRegist = 0;

    /** 商品グループ更新件数 */
    private int resultGoodsGroupUpdate = 0;

    /** 商品グループ表示更新件数 */
    private int resultGoodsGroupDisplayUpdate = 0;

    /** カテゴリ登録商品登録件数 */
    private int resultCategoryGoodsRegist = 0;

    /** カテゴリ登録商品削除件数 */
    private int resultCategoryGoodsDelete = 0;

    /** 関連商品登録件数 */
    private int resultGoodsRelationRegist = 0;

    /** 関連商品更新件数 */
    private int resultGoodsRelationUpdate = 0;

    /** 関連商品削除件数 */
    private int resultGoodsRelationDelete = 0;

    /** 商品グループ画像登録件数 */
    private int resultGoodsGroupImageRegist = 0;

    /** 商品グループ画像更新件数 */
    private int resultGoodsGroupImageUpdate = 0;

    /** 商品グループ画像削除件数 */
    private int resultGoodsGroupImageDelete = 0;

    /** 削除画像ファイルパスリスト */
    private List<String> deleteImageFilePathList = null;

    /** 登録画像ファイルパスリスト */
    private List<FileRegistDto> registImageFilePathList = null;

    /** 商品登録件数 */
    private int resultGoodsRegist = 0;

    /** 商品更新件数 */
    private int resultGoodsUpdate = 0;

    /** 商品グループ画像ファイル登録件数 */
    private int resultGoodsGroupImageFileRegist = 0;

    /** 商品グループ画像ファイル削除件数 */
    private int resultGoodsGroupImageFileDelete = 0;

    /** ワーニングメッセージ(ActionメッセージIDのカンマ区切り) */
    private String warningMessage = "";
}
