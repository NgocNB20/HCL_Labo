package jp.co.itechh.quad.ddd.usecase.query.batchmanagement;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * バッチログデータ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class BatchLogData {

    /**
     * バッチID
     */
    @Field("batch-id")
    private String batchId;

    /**
     * バッチ名
     */
    @Field("batch-name")
    private String batchName;

    /**
     * 起動種別（0:手動/1:スケジューラ）
     */
    @Field("start-type")
    private String startType;

    /**
     * Batch input data
     */
    @Field("input-data")
    private BatchInputData inputData;

    /**
     * 処理開始日時
     */
    @Field("start-time")
    private Date startTime;

    /**
     * 処理件数
     */
    @Field("process-count")
    private Integer processCount = 0;

    /**
     * レポート内容
     */
    @Field("report")
    private String report;

    /**
     * 処理状況（0:正常終了/1:異常終了）
     */
    @Field("result")
    private String result;

    /**
     * 処理終了日時
     */
    @Field("end-time")
    private Date endTime;
}
