/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.shopinfo;

import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.entity.shop.ShopEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class ShopInfoModel extends AbstractModel {

    /**
     * ショップ情報エンティティ
     */
    private ShopEntity shopEntity;

    /**
     * ショップSEQ
     */
    private String shopSeq;

    /**
     * ショップ名pc
     */
    @NotEmpty
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 1, max = 80)
    private String shopNamePC;

    /**
     * meta-description
     */

    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'})
    @Length(min = 1, max = 400)
    private String metaDescription;

    /**
     * meta-keyword
     */

    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'})
    @Length(min = 1, max = 400)
    private String metaKeyword;

    /** 自動承認フラグ */
    //    @NotEmpty
    //    @HVItems
    private String autoApprovalFlag;

    /** 自動承認フラグ */
    //    @HUItemList(component = "valuetype.autoApprovalFlag")
    private Map<String, String> autoApprovalFlagItems;

}