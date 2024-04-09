/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *  商品グループ画像公開ステータスDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GoodsGroupImageOpenstatusDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品管理番号 */
    private String goodsGroupCode;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus goodsOpenStatusPc;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 画像種別内連番 */
    private Integer imageTypeVersionNo = 1;

    /** 画像ファイル名 */
    private String imageFileName;
}