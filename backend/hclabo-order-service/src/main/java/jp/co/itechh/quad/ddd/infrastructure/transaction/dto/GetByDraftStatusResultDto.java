package jp.co.itechh.quad.ddd.infrastructure.transaction.dto;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 指定日時以前の下書き状態の取引データ取得結果Dto
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GetByDraftStatusResultDto {

    /** 取引ID */
    private String transactionId;

    /** 受注番号 */
    private String orderCode;
}
