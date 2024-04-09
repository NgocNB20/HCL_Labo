package jp.co.itechh.quad.admin.batch.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = false)
@Data
@Component
@Scope("prototype")
public class BatchManagementDetailDto implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * バッチジョッブId
     */
    private String batchId;

    private Timestamp startTime;

    private Timestamp endTime;

    private Integer processCount;

    private String report;

    private String status;

}
