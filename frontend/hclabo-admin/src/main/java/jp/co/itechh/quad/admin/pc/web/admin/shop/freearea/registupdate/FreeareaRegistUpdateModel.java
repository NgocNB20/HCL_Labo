/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * フリーエリア登録・更新画面
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FreeareaRegistUpdateModel extends AbstractModel {

    /**
     * 正規表現エラー
     */
    public static final String MSGCD_REGULAR_EXPRESSION_ERR = "{ASF000201W}";

    /**
     *
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     *
     */
    public FreeareaRegistUpdateModel() {
        super();
    }

    /** 変更前フリーエリアエンティティ */
    private FreeAreaEntity originalFreeAreaEntity;

    /** 変更箇所リスト */
    private List<String> modifiedList;

    /** 表示対象会員リスト */
    private List<Integer> viewableMemberList;

    /**
     * フリーエリアエンティティ
     */
    private FreeAreaEntity freeAreaEntity;

    /** バリデーション限度数 */
    private Integer validLimit;

    /**
     * CSVレコード件数限度<br/>
     */
    private Integer recordLimit;

    /**
     * キー
     */
    @NotEmpty
    @Pattern(regexp = "^[\\w]+$", message = MSGCD_REGULAR_EXPRESSION_ERR)
    @Length(min = 1, max = 50)
    private String freeAreaKey;

    /**
     * 公開開始日時（日付）
     */
    @HCDate
    @NotEmpty
    @HVDate
    private String openStartDate;

    /**
     * 公開開始日時（時刻）
     */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss")
    private String openStartTime;

    /**
     * タイトル
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 100)
    private String freeAreaTitle;

    /**
     * 本文PC
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @Length(min = 0, max = 100000, message = "{HTextAreaValidator.LENGTH_detail}")
    private String freeAreaBodyPc;

    /**
     * サイトマップ出力
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}")
    @HVItems(target = HTypeSiteMapFlag.class)
    private String siteMapFlag = HTypeSiteMapFlag.OFF.getValue();

    /**
     * サイトマップ出力アイテム
     */
    private Map<String, String> siteMapFlagItems;

    /** 対象商品 */
    @HVSpecialCharacter(allowPunctuation = true)
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HCHankaku
    private String targetGoods;

    /**
     * フリーエリアSEQ
     */
    private Integer freeAreaSeq;

    /**
     * フリーエリアSEQ(画面用)
     */
    private Integer scFreeAreaSeq;

    /**
     * 前画面が確認画面であるかを判断するフラグ<br/>
     * true:確認画面
     */
    private boolean fromConfirm;

    /** 不正操作を判断するためのフラグ */
    private boolean normality;

    /**
     * @return true = 更新フロー
     */
    public boolean isUpdatePage() {
        if (originalFreeAreaEntity == null || originalFreeAreaEntity.getFreeAreaSeq() == null) {
            return false;
        }
        return true;
    }

    /************************************
     ** 登録画面：特集ページ用URL
     ************************************/

    /**
     * @return the specialPageUrlPc
     */
    public String getSpecialPageUrlPcRegist() {
        if (originalFreeAreaEntity == null) {
            return null;
        }
        String url = PropertiesUtil.getSystemPropertiesValue("special.url.pc");
        url = MessageFormat.format(url, originalFreeAreaEntity.getFreeAreaKey());
        return url;
    }

    /**
     * @return the contentsPageUrlPc
     */
    public String getContentsPageUrlPcRegist() {
        if (originalFreeAreaEntity == null) {
            return null;
        }
        String url = PropertiesUtil.getSystemPropertiesValue("contents.url.pc");
        url = MessageFormat.format(url, originalFreeAreaEntity.getFreeAreaKey());
        return url;
    }

    /**
     * @return the topicPageUrlPc
     */
    public String getTopicPageUrlPcRegist() {
        if (originalFreeAreaEntity == null) {
            return null;
        }
        String url = PropertiesUtil.getSystemPropertiesValue("topic.url.pc");
        url = MessageFormat.format(url, originalFreeAreaEntity.getFreeAreaKey());
        return url;
    }

    /************************************
     ** 確認画面：特集ページ用URL
     ************************************/

    /**
     * @return the specialPageUrlPc
     */
    public String getSpecialPageUrlPc() {
        if (StringUtils.isEmpty(freeAreaKey)) {
            return null;
        }
        String url = PropertiesUtil.getSystemPropertiesValue("special.url.pc");
        url = MessageFormat.format(url, freeAreaKey);
        return url;
    }

    /**
     * @return the contentsPageUrlPc
     */
    public String getContentsPageUrlPc() {
        if (StringUtils.isEmpty(freeAreaKey)) {
            return null;
        }
        String url = PropertiesUtil.getSystemPropertiesValue("contents.url.pc");
        url = MessageFormat.format(url, freeAreaKey);
        return url;
    }

    /**
     * @return the topicPageUrlPc
     */
    public String getTopicPageUrlPc() {
        if (StringUtils.isEmpty(freeAreaKey)) {
            return null;
        }
        String url = PropertiesUtil.getSystemPropertiesValue("topic.url.pc");
        url = MessageFormat.format(url, freeAreaKey);
        return url;
    }
}
