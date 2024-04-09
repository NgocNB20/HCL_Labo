/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.annotation.validator.HVCsvHeader;
import jp.co.itechh.quad.admin.annotation.validator.HVMultipartFile;
import jp.co.itechh.quad.admin.dto.order.ShipmentCsvDto;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 出荷アップロード画面モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderShipmentUploadModel extends AbstractModel {

    /** アップロード完了メッセージ */
    public static final String MSGCD_SUCCESS_UPLOAD = "AOX000801";

    /** アップロード失敗メッセージ */
    public static final String MSGCD_FAIL_UPLOAD = "AOX000802";

    /** ファイル削除失敗メッセージ */
    public static final String MSGCD_FAIL_DELETE = "AOX000803";

    /** アップロードファイル */
    @HVMultipartFile(fileExtension = {"csv"})
    @HVCsvHeader(csvDtoClass = ShipmentCsvDto.class)
    private MultipartFile registUploadFile;

    /** インフォメーションメッセージ */
    private String infoMsg;

    /** 一時領域へ保存されたアップロードファイル */
    private File savedTempraryFile;

    /** 出荷登録データ件数 */
    private Integer registCount;

    /** エラーリスト */
    private List<OrderShipmentUploadModelItem> resultItems;

    /** バリデーション限度数 */
    private Integer validLimit;

    //    /**
    //     * @return the commit
    //     */
    //    public boolean isCommit() {
    //        return (this.registUploadFile != null);
    //    }

    /** 列名称 */
    private String columnLabel;

    /**
     * 処理が成功したかどうか
     *
     * @return true 成功
     */
    public boolean isSuccess() {

        if (resultItems == null) {
            return true;
        }

        return resultItems.isEmpty();
    }

}