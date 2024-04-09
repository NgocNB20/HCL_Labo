/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.memberinfo.wishlist;

import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.entity.memberinfo.wishlist.WishlistEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * お気に入りDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.4 $
 */
@Data
@Component
@Scope("prototype")
public class WishlistDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** お気に入りエンティティ */
    private WishlistEntity wishlistEntity;

    /** 商品詳細DTO */
    private GoodsDetailsDto goodsDetailsDto;

    /** 商品グループ画像エンティティリスト */
    private List<GoodsGroupImageEntity> goodsGroupImageEntityList;

    /** アイコン詳細DTO */
    private List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList;

    /** 在庫表示 */
    private String stockStatus;
}
