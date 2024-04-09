/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class InquiryGroupModel extends AbstractModel {

    /**
     * コンストラクタ
     * 初期値の設定
     */
    public InquiryGroupModel() {
        super();
    }

    /**
     * 検索一覧
     */

    private List<InquiryGroupModelItem> resultItems;

    /**
     * 検索一覧のインデックス
     */
    private int resultIndex;

    /**
     * 表示順
     */
    @NotNull(message = "{ASI000404W}")
    private Integer orderDisplay;

    /**
     * 表示順(各行)
     */
    private Integer orderDisplayRadio;

    /**
     * 問い合わせ分類名称
     */
    private String inqueryGroupName;
    /**
     * 公開状態
     */
    private HTypeOpenDeleteStatus openStatus;
    /**
     * 問い合わせ分類SEQ
     */
    private Integer inquiryGroupSeq;
    /**
     * ショップSEQ
     */
    private Integer shopSeq;

    /************************************
     **  判断処理
     ************************************/

    /**
     * 検索結果表示判定
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return CollectionUtil.isNotEmpty(getResultItems());
    }
}