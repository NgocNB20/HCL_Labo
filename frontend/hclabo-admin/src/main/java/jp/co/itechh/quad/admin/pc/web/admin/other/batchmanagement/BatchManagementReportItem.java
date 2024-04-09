package jp.co.itechh.quad.admin.pc.web.admin.other.batchmanagement;

import jp.co.itechh.quad.admin.constant.type.HTypeBatchDerive;
import jp.co.itechh.quad.admin.constant.type.HTypeBatchStatus;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * バッチ管理レポート画面表示項目
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Component
@Scope("prototype")
public class BatchManagementReportItem extends AbstractModel {
    /**
     * タスク識別ID
     */
    private Integer taskid;

    /**
     * 非同期処理の識別
     */
    private HTypeBatchDerive batchderive;

    /**
     * オンライン用表示名称
     */
    private String batchname;

    /**
     * 受付け時刻
     */
    private Timestamp accepttime;

    /**
     * タスク状態
     */
    private HTypeBatchStatus taskstatus;

    /**
     * 処理終了時刻
     */
    private Timestamp terminatetime;

    /**
     * オンラインプログレスバー用処理済件数
     */
    private String processedrecord;

    /**
     * 処理結果文字列
     */
    private String reportstring;

    /**
     * 終了マーク表示フラグ
     */
    private boolean endMarkDisplay;

    /**
     * 画面で表示する受付け時刻
     */
    private String accepttimeStr;

    /**
     * 画面で表示する処理終了時刻
     */
    private String terminatetimeStr;

}
