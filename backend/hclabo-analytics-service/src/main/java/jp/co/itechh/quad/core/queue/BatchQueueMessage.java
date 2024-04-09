package jp.co.itechh.quad.core.queue;

import lombok.Data;

/**
 * キューメッセージ
 *
 * @author Doan Thang (VJP)
 */
@Data
public class BatchQueueMessage {
    /**
     * 運営者Seq
     */
    private Integer administratorSeq;

    /**
     * ファイルパス
     */
    private String filePath;

    /**
     * 起動種別
     */
    private String startType;

    /**
     * アップロード種別
     */
    private String uploadType;

    /* 全国一括登録フラグ */
    private Boolean allFlag;
}
