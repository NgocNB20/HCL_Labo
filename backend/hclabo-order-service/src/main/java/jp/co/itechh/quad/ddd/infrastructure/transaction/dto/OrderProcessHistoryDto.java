package jp.co.itechh.quad.ddd.infrastructure.transaction.dto;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 受注処理履歴DTO
 */
@Entity
@Data
@Component
@Scope("prototype")
public class OrderProcessHistoryDto implements Serializable {

    /** 取引ID */
    private String transactionId;

    /** 処理日時 */
    private Date processTime;

    /** 処理種別 */
    private String processType;

    /** 処理担当者名 */
    private String processPersonName;
}
