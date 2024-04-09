/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup.registupdate;

import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
public class InquiryGroupRegistUpdateModel extends AbstractModel {

    /**
     * コンストラクタ
     * 初期値の設定
     */
    public InquiryGroupRegistUpdateModel() {
        super();
    }

    /**
     * 変更前問い合わせ分類エンティティ
     */
    private InquiryGroupEntity originalInquiryGroupEntity;

    /**
     * 問い合わせ分類SEQ
     */

    private Integer inquiryGroupSeq;
    /**
     * 問い合わせ分類名称
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @NotEmpty
    @Length(min = 1, max = 40)
    private String inquiryGroupName;
    /**
     * 問い合わせ分類公開状態
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}")
    private String openStatus;
    /**
     * 問い合わせ分類エンティティ
     */
    private InquiryGroupEntity inquiryGroupEntity;

    /**
     * 前画面が確認画面であるかを判断するフラグ
     * true:確認画面
     */
    private boolean fromConfirm;

    /**
     * 不正操作を判断するためのフラグ
     */
    private boolean normality;

    /************************************
     **  選択肢
     ************************************/
    /**
     * 問い合わせ分類公開状態 選択肢
     */
    //    @HUItemList(component = "inquiryGroupOpenStatus")
    private Map<String, String> openStatusItems;

    /**
     * 変更箇所リスト
     */
    private List<String> modifiedList;
}