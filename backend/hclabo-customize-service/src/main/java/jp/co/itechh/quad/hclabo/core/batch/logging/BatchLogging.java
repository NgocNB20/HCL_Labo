package jp.co.itechh.quad.hclabo.core.batch.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jp.co.itechh.quad.hclabo.core.batch.queue.BatchQueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * バッチログ出力
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchLogging {
    /** ロガー */
    private static final Logger BATCH_LOG = LoggerFactory.getLogger("BATCH");

    /** ログ内容 */
    private final Map<String, Object> logObj;

    /**
     * コンストラクタ
     */
    public BatchLogging() {
        this.logObj = new LinkedHashMap<>();
    }

    /**
     * バッチID設定
     *
     * @param batchId バッチID
     */
    public void setBatchId(String batchId) {
        this.logObj.put("batch-id", batchId);
    }

    /**
     * バッチ名設定
     *
     * @param batchName バッチ名
     */
    public void setBatchName(String batchName) {
        this.logObj.put("batch-name", batchName);
    }

    /**
     * バッチ起動種別設定
     *
     * @param startType バッチ起動種別
     */
    public void setStartType(String startType) {
        this.logObj.put("start-type", startType);
    }

    /**
     * 入力データ設定
     *
     * @param message メッセージ
     */
    public void setInputData(BatchQueueMessage message) {
        this.logObj.put("input-data", message);
    }

    /**
     * 処理開始日時設定
     *
     * @param timestamp 日時
     */
    public void setStartTime(Timestamp timestamp) {
        this.logObj.put("start-time", timestamp.getTime());
    }

    /**
     * 処理件数設定
     *
     * @param count カウント
     */
    public void setProcessCount(Integer count) {
        this.logObj.put("process-count", Objects.requireNonNullElse(count, ""));
    }

    /**
     * 報告内容設定
     *
     * @param report 報告内容
     */
    public void setReport(StringBuilder report) {
        this.logObj.put("report", report);
    }

    /**
     * 処理結果設定
     *
     * @param result 結果
     */
    public void setResult(String result) {
        this.logObj.put("result", result);
    }

    /**
     * 処理終了日時設定
     *
     * @param timestamp 日時
     */
    public void setEndTime(Timestamp timestamp) {
        this.logObj.put("end-time", timestamp.getTime());
    }

    /**
     * ログ出力
     *
     * @param key ログ出力キー
     * @throws JsonProcessingException JsonProcessingException
     */
    public void log(String key) throws JsonProcessingException {
        String raw = convertObjectToJson(logObj);
        BATCH_LOG.info(net.logstash.logback.marker.Markers.appendRaw(key, raw), null);
    }

    /**
     * ObjectからJsonに変換<br/>
     *
     * @param object Object
     * @return String
     * @throws JsonProcessingException JsonProcessingException
     */
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();
        return ow.writeValueAsString(object);
    }
}
