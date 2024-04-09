/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.bundledupload;

import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 商品管理 商品一括アップロードHelper<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsBundledUploadHelper {

    /**
     * サービス処理後のページへ反映<br/>
     *
     * @param goodsBundledUploadModel アップロードページ
     * @param csvUploadResult Csvアップロード結果（商品CSV一括アップロードの場合はnull=正常終了）
     */
    public void toPageForCsvUploadResultDto(GoodsBundledUploadModel goodsBundledUploadModel,
                                            CsvUploadResult csvUploadResult) {

        // 商品一括アップロードはバッチ処理にてエラーチェックを行うためここでは行わない
        if (csvUploadResult == null) {
            /* 正常終了 */
            goodsBundledUploadModel.setNormalEndMsg(true);
            goodsBundledUploadModel.setUploadItemCount(1);
            return;
        }

        /* エラーリストのクリア */
        goodsBundledUploadModel.setErrorResultItems(null);

        /* CSVバリデータエラーの場合 */
        if (csvUploadResult.isInValid()) {
            goodsBundledUploadModel.setErrorResultItems(new ArrayList<>());
            CsvValidationResult csvValidationResult = csvUploadResult.getCsvValidationResult();
            for (InvalidDetail detail : csvValidationResult.getInvalidDetailList()) {
                goodsBundledUploadModel.getErrorResultItems()
                                       .add(cretaeCompletePageItem(detail.getRow(), detail.getColumnLabel(),
                                                                   detail.getMessage(), null
                                                                  ));
            }
            goodsBundledUploadModel.setValidLimitFlg(csvUploadResult.isValidLimitFlg());
            goodsBundledUploadModel.setValidationEndMsg(csvUploadResult.isInValid());
            goodsBundledUploadModel.setUploadItemCount(0);
            return;
        }

        /* CSVデータエラーの場合 */
        if (csvUploadResult.isError()) {
            goodsBundledUploadModel.setErrorResultItems(new ArrayList<>());
            for (CsvUploadError csvUploadError : csvUploadResult.getCsvUploadErrorlList()) {
                goodsBundledUploadModel.getErrorResultItems()
                                       .add(cretaeCompletePageItem(csvUploadError.getRow(), null,
                                                                   csvUploadError.getMessage(), csvUploadError.getArgs()
                                                                  ));
            }
            goodsBundledUploadModel.setErrorEndMeg(csvUploadResult.isError());
            goodsBundledUploadModel.setUploadItemCount(0);
            return;
        }

        /* 正常終了 */
        goodsBundledUploadModel.setNormalEndMsg(true);
        goodsBundledUploadModel.setUploadItemCount(1);
    }

    /**
     * 結果ページのItemを生成<br/>
     *
     * @param row        行番号
     * @param columnName カラム名
     * @param message    内容
     * @param args       エラーメッセージパラメータ
     * @return Item
     */
    protected GoodsBundledUploadModelItem cretaeCompletePageItem(Integer row,
                                                                 String columnName,
                                                                 String message,
                                                                 Object[] args) {
        GoodsBundledUploadModelItem item = ApplicationContextUtility.getBean(GoodsBundledUploadModelItem.class);
        item.setRowNo(row);
        // 登録済規格(商品管理番号≠商品番号)の場合、行番号(row)に-1が設定される。
        // 登録済規格の場合、アップロードのエラーメッセージに行番号でなく商品管理番号と商品番号を表示する。
        if (args != null && args.length > 0 && row == -1) {
            // エラーメッセージパラメータ(args)には以下が設定される。
            // args[args.length - 2]：商品管理番号
            // args[args.length - 1]:商品番号
            item.setGoodsGroupCode((String) args[args.length - 2]);
            item.setGoodsCode((String) args[args.length - 1]);
        }
        item.setColumnName(columnName);
        item.setFailReason(message);
        return item;
    }

}