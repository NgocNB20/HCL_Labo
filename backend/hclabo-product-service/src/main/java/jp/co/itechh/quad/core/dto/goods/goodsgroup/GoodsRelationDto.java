/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品グループDtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class GoodsRelationDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 関連商品エンティティ */
    private GoodsRelationEntity goodsRelationEntity;

    /** 商品グループDto */
    private GoodsGroupDto goodsGroupDto;
}
