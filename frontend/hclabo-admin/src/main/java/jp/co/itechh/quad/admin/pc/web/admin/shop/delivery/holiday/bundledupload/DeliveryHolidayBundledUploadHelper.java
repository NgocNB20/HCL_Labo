package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.holiday.bundledupload;

import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.admin.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.admin.base.util.csvupload.InvalidDetail;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.holiday.presentation.api.param.CsvUploadErrorResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.CsvValidationResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayCsvUploadResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayInvalidDetail;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 一括アップロード<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class DeliveryHolidayBundledUploadHelper {

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public DeliveryHolidayBundledUploadHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * サービス処理後のページへ反映<br/>
     *
     * @param uploadPage アップロードファイル
     * @param csvUploadResult Csvアップロード結果
     */
    public void toPageForCsvUploadResultDto(DeliveryHolidayBundledUploadModel uploadPage,
                                            CsvUploadResult csvUploadResult) {

        /* エラーリストのクリア */
        uploadPage.setResultItems(null);

        /* CSVバリデータエラーの場合 */
        if (csvUploadResult.isInValid()) {
            uploadPage.setResultItems(new ArrayList<DeliveryHolidayBundledUploadFinishModelItem>());
            CsvValidationResult csvValidationResult = csvUploadResult.getCsvValidationResult();
            for (InvalidDetail detail : csvValidationResult.getInvalidDetailList()) {
                uploadPage.getResultItems()
                          .add(cretaeUploadFinishPageItem(detail.getRow(), detail.getColumnLabel(),
                                                          detail.getMessage()
                                                         ));
            }
            uploadPage.setValidLimitFlg(csvUploadResult.isValidLimitFlg());
            return;
        }

        /* CSVデータエラーの場合 */
        if (csvUploadResult.isError()) {
            uploadPage.setResultItems(new ArrayList<DeliveryHolidayBundledUploadFinishModelItem>());
            for (CsvUploadError csvUploadError : csvUploadResult.getCsvUploadErrorlList()) {
                uploadPage.getResultItems()
                          .add(cretaeUploadFinishPageItem(csvUploadError.getRow(), null, csvUploadError.getMessage()));
            }
            return;
        }

        /* 正常終了した場合 */

        // 正常完了(ヘッダー行分を-2している)
        uploadPage.setUploadCount(csvUploadResult.getRecordCount() - 2);
    }

    /**
     * 完了ページのItemを生成<br/>
     *
     * @param row 行番号
     * @param columnName カラム名
     * @param message 内容
     * @return Item
     */
    protected DeliveryHolidayBundledUploadFinishModelItem cretaeUploadFinishPageItem(Integer row,
                                                                                     String columnName,
                                                                                     String message) {
        DeliveryHolidayBundledUploadFinishModelItem item =
                        ApplicationContextUtility.getBean(DeliveryHolidayBundledUploadFinishModelItem.class);
        item.setRow(row);
        item.setColumnName(columnName);
        item.setMessage(message);
        return item;
    }

    /**
     * 検索結果をUploadPageに反映します
     *
     * @param uploadPage UploadPage
     * @param resultEntity DeliveryMethodEntity
     */
    public void convertToRegistPageForResult(DeliveryHolidayBundledUploadModel uploadPage,
                                             DeliveryMethodEntity resultEntity) {
        uploadPage.setDeliveryMethodName(resultEntity.getDeliveryMethodName());
        uploadPage.setDeliveryMethodType(resultEntity.getDeliveryMethodType());
        uploadPage.setOpenStatusPC(resultEntity.getOpenStatusPC());
    }

    /**
     * @param shippingMethodResponse 配送方法レスポンス
     * @return 配送方法クラス
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodResponse shippingMethodResponse) {
        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();
        DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();

        deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodResponse.getDeliveryMethodSeq());
        deliveryMethodEntity.setShopSeq(1001);
        deliveryMethodEntity.setDeliveryMethodName(deliveryMethodResponse.getDeliveryMethodName());
        deliveryMethodEntity.setDeliveryMethodDisplayNamePC(deliveryMethodResponse.getDeliveryMethodDisplayNamePC());
        deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                           deliveryMethodResponse.getOpenStatusPC()
                                                                          ));
        deliveryMethodEntity.setDeliveryNotePC(deliveryMethodResponse.getDeliveryNotePC());
        deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                 deliveryMethodResponse.getDeliveryMethodType()
                                                                                ));
        deliveryMethodEntity.setEqualsCarriage(deliveryMethodResponse.getEqualsCarriage());
        deliveryMethodEntity.setLargeAmountDiscountPrice(deliveryMethodResponse.getLargeAmountDiscountPrice());
        deliveryMethodEntity.setLargeAmountDiscountCarriage(deliveryMethodResponse.getLargeAmountDiscountCarriage());
        deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                   deliveryMethodResponse.getShortfallDisplayFlag()
                                                                                  ));
        deliveryMethodEntity.setDeliveryLeadTime(deliveryMethodResponse.getDeliveryLeadTime());
        deliveryMethodEntity.setDeliveryChaseURL(deliveryMethodResponse.getDeliveryChaseURL());
        deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                        deliveryMethodResponse.getDeliveryChaseURLDisplayPeriod());
        deliveryMethodEntity.setPossibleSelectDays(deliveryMethodResponse.getPossibleSelectDays());
        deliveryMethodEntity.setReceiverTimeZone1(deliveryMethodResponse.getReceiverTimeZone1());
        deliveryMethodEntity.setReceiverTimeZone2(deliveryMethodResponse.getReceiverTimeZone2());
        deliveryMethodEntity.setReceiverTimeZone3(deliveryMethodResponse.getReceiverTimeZone3());
        deliveryMethodEntity.setReceiverTimeZone4(deliveryMethodResponse.getReceiverTimeZone4());
        deliveryMethodEntity.setReceiverTimeZone5(deliveryMethodResponse.getReceiverTimeZone5());
        deliveryMethodEntity.setReceiverTimeZone6(deliveryMethodResponse.getReceiverTimeZone6());
        deliveryMethodEntity.setReceiverTimeZone7(deliveryMethodResponse.getReceiverTimeZone7());
        deliveryMethodEntity.setReceiverTimeZone8(deliveryMethodResponse.getReceiverTimeZone8());
        deliveryMethodEntity.setReceiverTimeZone9(deliveryMethodResponse.getReceiverTimeZone9());
        deliveryMethodEntity.setReceiverTimeZone10(deliveryMethodResponse.getReceiverTimeZone10());
        deliveryMethodEntity.setOrderDisplay(deliveryMethodResponse.getOrderDisplay());
        deliveryMethodEntity.setRegistTime(conversionUtility.toTimestamp(deliveryMethodResponse.getRegistTime()));
        deliveryMethodEntity.setUpdateTime(conversionUtility.toTimestamp(deliveryMethodResponse.getUpdateTime()));

        return deliveryMethodEntity;
    }

    /**
     * 倉庫休日CSVアップロードレスポンスに変換
     *
     * @param holidayCsvUploadResponse 倉庫休日CSVアップロードレスポンス
     * @return Csvアップロード結果Dto
     */
    public CsvUploadResult toCsvUploadResult(HolidayCsvUploadResponse holidayCsvUploadResponse) {
        CsvUploadResult csvUploadResult = new CsvUploadResult();

        csvUploadResult.setCsvValidationResult(
                        toCsvValidationResult(holidayCsvUploadResponse.getCsvValidationResult()));
        csvUploadResult.setCsvUploadErrorlList(toCsvUploadErrorlList(holidayCsvUploadResponse.getCsvUploadErrorList()));
        if (holidayCsvUploadResponse.getValidLimitFlg() != null) {
            csvUploadResult.setValidLimitFlg(holidayCsvUploadResponse.getValidLimitFlg());
        }
        if (holidayCsvUploadResponse.getRecordCount() != null) {
            csvUploadResult.setRecordCount(holidayCsvUploadResponse.getRecordCount());
        } else {
            csvUploadResult.setRecordCount(1);
        }

        return csvUploadResult;
    }

    /**
     * Csvバリデータ結果に変換
     *
     * @param csvValidationResponse CSVバリテーションレスポンス
     * @return CSV Validation 時に発生した ValidatorException を保持するためのクラス。
     */
    public CsvValidationResult toCsvValidationResult(CsvValidationResponse csvValidationResponse) {
        CsvValidationResult csvValidationResult = new CsvValidationResult();
        if (csvValidationResponse != null) {
            csvValidationResult.setDetailList(toHolidayInvalidDetailList(csvValidationResponse.getDetailList()));
        }

        return csvValidationResult;
    }

    /**
     * Csvアップロードエラー格納Dtoに変換
     *
     * @param csvUploadErrorResponses Csvアップロードエラー格納レスポンス
     * @return Csvアップロードエラー格納Dto
     */
    public List<CsvUploadError> toCsvUploadErrorlList(List<CsvUploadErrorResponse> csvUploadErrorResponses) {
        List<CsvUploadError> csvUploadErrorList = new ArrayList<>();

        if (csvUploadErrorResponses != null) {

            for (CsvUploadErrorResponse csvUploadErrorResponse : csvUploadErrorResponses) {
                CsvUploadError csvUploadError = new CsvUploadError();

                csvUploadError.setRow(csvUploadErrorResponse.getRow());
                csvUploadError.setMessage(csvUploadErrorResponse.getMessage());

                csvUploadErrorList.add(csvUploadError);
            }
        }
        return csvUploadErrorList;
    }

    /**
     * 検証NG詳細リストに変換
     *
     * @param invalidDetailList 検証NG詳細リスト
     * @return 検証NG詳細リスト
     */
    public List<InvalidDetail> toHolidayInvalidDetailList(List<HolidayInvalidDetail> invalidDetailList) {
        List<InvalidDetail> holidayInvalidDetailList = new ArrayList<>();

        if (invalidDetailList != null) {
            for (HolidayInvalidDetail holidayInvalidDetail : invalidDetailList) {
                InvalidDetail invalidDetail = new InvalidDetail();

                invalidDetail.setRow(holidayInvalidDetail.getRow());
                invalidDetail.setColumnLabel(holidayInvalidDetail.getColumnLabel());
                invalidDetail.setMessage(holidayInvalidDetail.getMessage());

                holidayInvalidDetailList.add(invalidDetail);
            }
        }
        return holidayInvalidDetailList;
    }
}