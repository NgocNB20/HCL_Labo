/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品グループ画像登録更新用Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.3 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsGroupImageRegistUpdateDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループ画像エンティティ */
    private GoodsGroupImageEntity goodsGroupImageEntity;

    /** 登録先画像ファイル名 */
    private String imageFileName;

    /** 登録画像テンポラリファイル名 */
    private String tmpImageFileName;

    /** 登録画像テンポラリファイルパス */
    private String tmpImageFilePath;

    /** 削除フラグ */
    private Boolean deleteFlg = false;
}