/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品管理：商品詳細ページ 商品グループ詳細画像アイテム<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsDetailsImageItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 商品画像連番
     */
    private Integer imageNo;

    /**
     * 商品画像パス<br/>
     */
    private String imagePath;

    /**
     * 商品画像有無判定<br/>
     *
     * @return true=画像パスがある場合
     */
    public boolean isExistImage() {
        return !StringUtils.isEmpty(imagePath);
    }

}
