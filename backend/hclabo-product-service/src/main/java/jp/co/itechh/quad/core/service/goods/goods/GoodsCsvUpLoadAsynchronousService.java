/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods;

import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.constant.type.HTypeUploadType;

import java.io.File;
import java.util.List;

/**
 * 商品一括アップロードインターフェース
 * 商品データCSVファイル、商品画像の一括アップロードを行う
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public interface GoodsCsvUpLoadAsynchronousService {

    /** 関連商品登録件数超過 */
    public static final String RELATIONGOODS_OVER_FAIL = "SGG000401";

    /** 関連商品で指定された商品グループが存在しない */
    public static final String RELATIONGOODS_NONE_FAIL = "SGG000402";

    /** カテゴリ設定で指定されたカテゴリが存在しない */
    public static final String CATEGORY_NONE_FAIL = "SGG000403";

    /** 関連商品に自商品を登録 */
    public static final String RELATIONGOODS_MYSELF_FAIL = "SGG000406";

    /** 関連商品重複 */
    public static final String RELATIONGOODS_REPETITION_FAIL = "SGG000407";

    /** カテゴリ重複 */
    public static final String CATEGORY_REPETITION_FAIL = "SGG000408";

    /** 更新対象規格なしエラー（更新モードで対象規格なし） */
    public static final String UPDATEGOODS_NONE_FAIL = "SGG000409";

    /** 登録規格が既に存在するエラー（追加モードで対象規格あり） */
    public static final String REGISTGOODS_EXIST_FAIL = "SGG000410";

    /** 更新対象規格が削除されているエラー（更新モードで販売状態=削除） */
    public static final String UPDATEGOODS_DELETED_FAIL = "SGG000411";

    /** 規格表示順重複 エラー */
    public static final String OREDERDISPLAY_DUPLICATE_FAIL = "SGG000412";

    /** 商品管理番号を連続して設定していない（例：110,110,111,112,110） エラー */
    public static final String SGP001086 = "SGP001086";

    /** 同一商品番号を複数設定 エラー */
    public static final String SGP001087 = "SGP001087";

    /** 値が不正です。別の値を入力してください。 */
    public static final String CSVUPLOAD002E = "CSV-UPLOAD-002-E";

    /** 別レコードの商品管理番号で既にエラー発生 */
    public static final String CSVUPLOAD_DUPLICATE_ERROR = "CSV-UPLOAD-005-";

    /** CSVヘッダバリデーションNG時メッセージ */
    public static final String CSVUPLOAD_HEADER_VALIDATOR_MSGCD = "HCsvHeaderValidator.INVALID_detail";

    /** 必須列検証のメッセージ コード */
    public static final String CSVUPLOAD_FIELD_VALIDATE_REQUIRED = "CSV-UPLOAD-007-";

    /** 必須の列コンボ検証のメッセージ コード */
    public static final String CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED = "CSV-UPLOAD-008-";

    /**
     * 商品一括アップロード処理実行
     *
     * @param administratorSeq 管理者SEQ
     * @param uploadedCsvFile  商品CSVデータアップロードファイル
     * @param uploadType       アップロード種別
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     * @return CsvUploadResult CSVアップロード結果Dto
     */
    CsvUploadResult execute(Integer administratorSeq,
                            File uploadedCsvFile,
                            HTypeUploadType uploadType,
                            Integer shopSeq,
                            List<Integer> goodsGroupSeqSuccessList) throws Exception;

}