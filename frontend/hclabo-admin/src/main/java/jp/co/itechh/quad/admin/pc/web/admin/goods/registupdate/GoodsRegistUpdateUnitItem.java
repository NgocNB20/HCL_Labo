/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateCombo;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatusWithNoDeleted;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.AddGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.DeleteGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.DownGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.UpGoodsGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品管理：商品登録更新（商品規格設定）ページ情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
@HVRDateCombo(dateLeftTarget = "saleStartDatePC", timeLeftTarget = "saleStartTimePC", dateRightTarget = "saleEndDatePC",
              timeRightTarget = "saleEndTimePC",
              groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                              DownGoodsGroup.class})
public class GoodsRegistUpdateUnitItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /************************************
     ** 商品規格項目
     ************************************/
    /**
     * 販売開始日時PC<br/>
     */
    private Timestamp saleStartDateTimePC;

    /**
     * 販売終了日時PC<br/>
     */
    private Timestamp saleEndDateTimePC;

    /**
     * No
     */
    private Integer unitDspNo;

    /**
     * 商品SEQ<br/>
     */
    private Integer goodsSeq;

    /**
     * 商品コード<br/>
     */
    @NotEmpty(groups = {ConfirmGroup.class, AddGoodsGroup.class, UpGoodsGroup.class, DownGoodsGroup.class})
    @HVBothSideSpace(groups = {ConfirmGroup.class, AddGoodsGroup.class, UpGoodsGroup.class, DownGoodsGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_CODE, message = ValidatorConstants.MSGCD_REGEX_GOODS_CODE,
             groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                             DownGoodsGroup.class})
    @Length(min = 1, max = 20,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    private String goodsCode;

    /**
     * 規格表示順<br/>
     */
    private Integer orderDisplay;

    /**
     * JANコード<br/>
     */
    @Pattern(regexp = ValidatorConstants.REGEX_JAN_CODE, message = ValidatorConstants.MSGCD_REGEX_JAN_CODE,
             groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                             DownGoodsGroup.class})
    @HVBothSideSpace(groups = {ConfirmGroup.class, AddGoodsGroup.class, UpGoodsGroup.class, DownGoodsGroup.class})
    @Length(min = 0, max = ValidatorConstants.LENGTH_JAN_CODE_MAXIMUM,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    private String janCode;

    /**
     * 規格値１<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                     DownGoodsGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    @Length(min = 0, max = 100,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    private String unitValue1;

    /**
     * 規格値２<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                     DownGoodsGroup.class})
    @HVSpecialCharacter(allowPunctuation = true,
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    @Length(min = 0, max = 100,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    private String unitValue2;

    /**
     * 販売状態PC<br/>
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}",
              groups = {ConfirmGroup.class, AddGoodsGroup.class, UpGoodsGroup.class, DownGoodsGroup.class})
    @HVItems(target = HTypeGoodsSaleStatusWithNoDeleted.class,
             groups = {ConfirmGroup.class, AddGoodsGroup.class, UpGoodsGroup.class, DownGoodsGroup.class})
    private String goodsSaleStatusPC;

    /**
     * 販売開始年月日PC<br/>
     */
    @HCDate
    @HVDate(groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                    DownGoodsGroup.class})
    @HVSpecialCharacter(allowCharacters = '/',
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    private String saleStartDatePC;

    /**
     * 販売開始時刻PC<br/>
     */
    @HCDate(pattern = DateUtility.HMS)
    @HVDate(pattern = DateUtility.HMS,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    @HVSpecialCharacter(allowCharacters = ':',
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    private String saleStartTimePC;

    /**
     * 販売開始日時PC<br/>
     */
    @HCDate
    @HVDate(groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                    DownGoodsGroup.class})
    @HVSpecialCharacter(allowCharacters = '/',
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    private String saleEndDatePC;

    /**
     * 販売終了時刻PC<br/>
     */
    @HCDate(pattern = DateUtility.HMS)
    @HVDate(pattern = DateUtility.HMS,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    @HVSpecialCharacter(allowCharacters = ':',
                        groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                                        DownGoodsGroup.class})
    private String saleEndTimePC;

    /**
     * 最大購入可能数<br/>
     */
    @NotEmpty(groups = {ConfirmGroup.class, AddGoodsGroup.class, UpGoodsGroup.class, DownGoodsGroup.class})
    @HVNumber(groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                    DownGoodsGroup.class})
    @Range(min = 1, max = 9999,
           groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                           DownGoodsGroup.class})
    @Digits(integer = 4, fraction = 0,
            groups = {ConfirmGroup.class, AddGoodsGroup.class, DeleteGoodsGroup.class, UpGoodsGroup.class,
                            DownGoodsGroup.class})
    @HCNumber
    private String purchasedMax;

    /**
     * 商品規格登録有無<br/>
     * 商品規格が新規登録（商品SEQ=null）かどうかを返す
     *
     * @return true=規格登録
     */
    public boolean isGoodsRegist() {
        return (goodsSeq == null);
    }
}