package jp.co.itechh.quad.admin.pc.web.admin.other.batchmanagement;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.batch.core.dto.BatchManagementDetailDto;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchDerive;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchName;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStatus;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementGetRequest;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * バッチ管理Helper
 */
@Component
public class BatchManagementHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public BatchManagementHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * バッチ管理レポート画面表示項目に変換
     *
     * @param detailDto
     * @return バッチ管理レポート画面表示項目
     */
    public BatchManagementReportItem convertToBatchManagementReportItem(BatchManagementDetailDto detailDto,
                                                                        Integer index) {

        BatchManagementReportItem reportModel = new BatchManagementReportItem();

        reportModel.setTaskid(index);

        if (HTypeBatchName.BATCH_GOODS_ASYNCHRONOUS.getValue().equals(detailDto.getBatchId())
            || HTypeBatchName.BATCH_SHIPMENT_REGIST.getValue().equals(detailDto.getBatchId())
            || HTypeBatchName.BATCH_ORDER_CSV_ASYNCHRONOUS.getValue().equals(detailDto.getBatchId())
            || HTypeBatchName.BATCH_GOODSIMAGE_UPDATE.getValue().equals(detailDto.getBatchId())) {
            reportModel.setBatchderive(HTypeBatchDerive.ONLINE);
        } else {
            reportModel.setBatchderive(HTypeBatchDerive.OFFLINE);
        }

        String batchName = null;

        if (EnumTypeUtil.getEnumFromValue(HTypeBatchName.class, detailDto.getBatchId()) != null
            && EnumTypeUtil.getLabelValue(EnumTypeUtil.getEnumFromValue(HTypeBatchName.class, detailDto.getBatchId()))
               != null) {
            batchName = EnumTypeUtil.getLabelValue(
                            EnumTypeUtil.getEnumFromValue(HTypeBatchName.class, detailDto.getBatchId()));
        }

        reportModel.setBatchname(batchName);

        reportModel.setAccepttime(detailDto.getStartTime());

        reportModel.setTaskstatus(EnumTypeUtil.getEnumFromLabel(HTypeBatchStatus.class, detailDto.getStatus()));

        reportModel.setTerminatetime(detailDto.getEndTime());

        reportModel.setProcessedrecord(conversionUtility.toString(detailDto.getProcessCount()));
        reportModel.setReportstring(detailDto.getReport());

        if (detailDto.getStatus().equals(HTypeBatchStatus.COMPLETED.getValue())) {
            reportModel.setEndMarkDisplay(false);
        } else {
            reportModel.setEndMarkDisplay(true);
        }

        return reportModel;
    }

    /**
     * バッチ管理レポート画面表示項目 GET
     *
     * @param batchManagementModel バッチ管理画面モデル
     * @return バッチ管理レポート画面表示項目
     */
    public BatchManagementReportItem getBatchManagementReportItem(BatchManagementModel batchManagementModel) {
        for (BatchManagementReportItem model : batchManagementModel.getResultItems()) {
            if (model.getTaskid() != null && model.getTaskid().equals(batchManagementModel.getReportTaskId())) {
                return model;
            }
        }
        return new BatchManagementReportItem();
    }

    /**
     * バッチ管理検索一覧リクエストに変換
     *
     * @param model バッチ管理画面モデル
     * @return バッチ管理検索一覧リクエスト
     */
    public BatchManagementGetRequest toBatchManagementGetRequest(BatchManagementModel model) {

        BatchManagementGetRequest batchManagementGetRequest = new BatchManagementGetRequest();

        if (!StringUtils.isEmpty(model.getBatchtypes())) {
            batchManagementGetRequest.setBatchName(model.getBatchtypes());
        }

        if (!StringUtils.isEmpty(model.getTaskstatuses())) {
            batchManagementGetRequest.setStatus(model.getTaskstatuses());
        }

        if (!StringUtils.isEmpty(model.getAccepttimeFrom())) {
            batchManagementGetRequest.setCreateTime(conversionUtility.toTimeStamp(model.getAccepttimeFrom()));
        }

        if (!StringUtils.isEmpty(model.getAccepttimeTo())) {
            Timestamp endTimeRequest = conversionUtility.toTimeStamp(model.getAccepttimeTo());
            setEndTime(endTimeRequest);

            batchManagementGetRequest.setEndTime(endTimeRequest);
        }
        ;

        return batchManagementGetRequest;
    }

    /**
     * To batch management detail dto list
     *
     * @param batchManagementList バッチ管理検索レスポンスのリスト
     * @return the list
     */
    public List<BatchManagementDetailDto> toBatchManagementDetailDtoList(List<BatchManagementResponse> batchManagementList) {
        List<BatchManagementDetailDto> batchManagementDetailDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(batchManagementList)) {
            batchManagementList.forEach(item -> {
                BatchManagementDetailDto batchManagementDetailDto = new BatchManagementDetailDto();

                batchManagementDetailDto.setBatchId(item.getBatchId());
                batchManagementDetailDto.setStartTime(conversionUtility.toTimestamp(item.getStartTime()));
                batchManagementDetailDto.setEndTime(conversionUtility.toTimestamp(item.getEndTime()));
                batchManagementDetailDto.setStatus(item.getResult());
                batchManagementDetailDto.setProcessCount(item.getProcessCount());
                batchManagementDetailDto.setReport(item.getReport());

                batchManagementDetailDtoList.add(batchManagementDetailDto);
            });
        }

        return batchManagementDetailDtoList;
    }

    /**
     * 終日時間を設定します
     *
     * @param time TimeStamp
     */
    protected void setEndTime(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        time.setTime(cal.getTimeInMillis());
    }
}