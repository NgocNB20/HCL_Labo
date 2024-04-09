/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 商品管理：商品登録更新（商品画像設定） 商品グループ詳細画像アイテム<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsRegistUpdateImageItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アップロード画像ファイル削除失敗メッセージ */
    public static final String MSGCD_FAIL_DELETE = "AGG001403";

    /**
     * 画像種別連番
     */
    private Integer imageNo;

    /**
     * 商品画像ファイル<br/>
     */
    @JsonIgnore
    private MultipartFile imageFile;

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
        return !StringUtil.isEmpty(imagePath);
    }

}
