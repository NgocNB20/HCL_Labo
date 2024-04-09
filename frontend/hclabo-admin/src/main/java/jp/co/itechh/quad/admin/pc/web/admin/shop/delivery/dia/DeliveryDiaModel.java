/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.dia;

import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.validator.HVRItems;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.OnceZipCodeAddGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.ReDisplayGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.validation.group.ZipCodeSearchGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 配送方法設定：配送不可能エリア設定検索画面用Pageクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@EqualsAndHashCode(callSuper = false)
@Data
@HVRItems(target = "prefectureName", comparison = "prefectureNameItems", groups = {ReDisplayGroup.class})
public class DeliveryDiaModel extends AbstractModel {

    /************************************
     ** GETパラメータ項目
     ************************************/
    /** 画面表示モード */
    private String md;

    /** 配送方法SEQ */
    private Integer dmcd;

    /************************************
     ** 一覧／登録-共通表示情報（上部）
     ************************************/
    /** 配送方法名 */
    private String deliveryMethodName;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 配送方法種別 */
    private HTypeDeliveryMethodType deliveryMethodType;

    /************************************
     ** 一覧画面
     ************************************/
    /** エリア(select) */
    private String prefectureName;

    /** エリア(select) オプション */
    private Map<String, String> prefectureNameItems;

    /** 総件数 */
    private int totalCount;

    /** 行番号 */
    private int resultDataIndex;

    /** 検索結果リスト */
    private List<DeliveryDiaModelItem> resultItems;

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return resultItems != null && !resultItems.isEmpty();
    }

    /************************************
     ** 登録画面
     ************************************/
    @NotEmpty(groups = {ZipCodeSearchGroup.class, OnceZipCodeAddGroup.class})
    @Length(min = 1, max = 7, groups = {ZipCodeSearchGroup.class, OnceZipCodeAddGroup.class})
    @HCHankaku
    @Pattern(regexp = RegularExpressionsConstants.ZIP_CODE_REGEX, message = "{HZipCodeValidator.INVALID_detail}",
             groups = {ZipCodeSearchGroup.class, OnceZipCodeAddGroup.class})
    private String zipCode;

    /** 住所 */
    private String address;
}