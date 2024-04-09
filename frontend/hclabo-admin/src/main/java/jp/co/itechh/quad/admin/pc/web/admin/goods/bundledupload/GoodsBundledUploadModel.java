/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2011 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload;

import jp.co.itechh.quad.admin.annotation.validator.HVCsvHeader;
import jp.co.itechh.quad.admin.annotation.validator.HVMultipartFile;
import jp.co.itechh.quad.admin.constant.type.HTypeUploadType;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ZipUploadGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 商品管理 商品一括アップロード画面<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsBundledUploadModel extends AbstractModel {

    /** アップロードCSVファイル削除失敗メッセージ */
    public static final String MSGCD_FAIL_DELETE = "AGG001403";

    /**
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     *
     */
    public GoodsBundledUploadModel() {
        uploadType = HTypeUploadType.REGIST.getValue();
    }

    /**
     * アップロード商品データファイル<br/>
     * 動的バリデータあり
     */
    @HVMultipartFile(fileExtension = {"csv"}, groups = {UploadGroup.class})
    private MultipartFile uploadCsvFile;

    /** アップロード種別 */
    @NotEmpty(groups = {UploadGroup.class})
    private String uploadType;

    /**
     * CSVデータチェック限度<br/>
     */
    private Integer validLimit;

    /**
     * CSVレコード件数限度<br/>
     */
    private Integer recordLimit;

    /**
     * エラー発生結果リスト<br/>
     */
    private List<GoodsBundledUploadModelItem> errorResultItems;

    /**
     * アップロード更新件数<br/>
     */
    private int uploadItemCount;

    /** バリデーション限度数で終了したか否かのフラグ */
    private boolean validLimitFlg;

    /**
     * 正常終了メッセージ<br/>
     */
    private boolean normalEndMsg;

    /**
     * Zipアップロードファイル
     */
    @HVMultipartFile(fileExtension = {"zip"}, groups = {ZipUploadGroup.class})
    private MultipartFile zipImageFile;

    /** Zipアップロード先 */
    @NotEmpty(groups = {ZipUploadGroup.class})
    private String zipImageTarget;

    /** アップロードしたファイル数 */
    private int uploadFileCount;

    /**
     * バリデートエラーメッセージ<br/>
     */
    private boolean validationEndMsg;

    /**
     * データチェックエラーメッセージ<br/>
     */
    private boolean errorEndMeg;

    /**
     * モード判定<br/>
     *
     * @return true=登録、false=更新
     */
    public boolean isRegist() {
        return HTypeUploadType.REGIST.getValue().equals(uploadType);
    }

    /**
     * エラーテーブル表示判定
     *
     * @return the errorEndTbl
     */
    public boolean isErrorEndTbl() {
        return errorResultItems != null && !errorResultItems.isEmpty();
    }

}